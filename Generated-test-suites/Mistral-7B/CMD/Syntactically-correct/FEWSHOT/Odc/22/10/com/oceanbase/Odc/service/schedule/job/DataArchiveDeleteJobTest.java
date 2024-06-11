package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class DataArchiveDeleteJobTest {

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private JsonUtils jsonUtils;

    private DataArchiveClearParameters dataArchiveClearParameters;
    private ScheduleTaskEntity scheduleTaskEntity;
    private ScheduleTaskEntity dataArchiveTask;
    private List<DlmTask> taskUnits;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        ObjectMapper objectMapper = new ObjectMapper();
        dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("dataArchiveTaskId");
        String json = "{\"parametersJson\":\"" + new GsonBuilder().setPrettyPrint().create().toJson(dataArchiveClearParameters) + "\"}";
        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId("taskId");
        scheduleTaskEntity.setJobName("jobName");
        scheduleTaskEntity.setJobGroup("jobGroup");
        scheduleTaskEntity.setParametersJson(json);

        dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("dataArchiveTaskId");
        dataArchiveTask.setStatus(TaskStatus.DONE);

        taskUnits = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId("taskUnitId");
        dlmTask.setStatus(TaskStatus.PREPARING);
        taskUnits.add(dlmTask);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(null);

        verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFinished() {
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        dataArchiveTask.setStatus(TaskStatus.RUNNING);

        dataArchiveDeleteJob.execute(null);

        verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskFinished() {
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        dataArchiveTask.setStatus(TaskStatus.DONE);

        doNothing().when(jsonUtils).fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {});

        dataArchiveDeleteJob.execute(null);

        verify(jsonUtils, times(1)).fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {});
        verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.COMPLETED);
    }

    @Test
    public void testExecute_DataArchiveTaskFailed() {
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        dataArchiveTask.setStatus(TaskStatus.FAILED);

        dataArchiveDeleteJob.execute(null);

        verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_TasksPrepared() {
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        dataArchiveTask.setStatus(TaskStatus.DONE);

        when(jsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {}))
                .thenReturn(taskUnits);

        dataArchiveDeleteJob.execute(null);

        for (DlmTask dlmTask : taskUnits) {
            verify(jsonUtils, times(1)).toJson(dlmTask);
            verify(dataArchiveDeleteJob, times(1)).executeTask(scheduleTaskEntity.getId(), asList(dlmTask));
        }

        verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.COMPLETED);
    }
}