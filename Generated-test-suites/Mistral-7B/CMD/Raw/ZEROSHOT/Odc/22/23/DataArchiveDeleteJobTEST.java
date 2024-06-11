package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
public class DataArchiveDeleteJobTest {

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ObjectMapper objectMapper;

    private ScheduleTaskEntity scheduleTaskEntity;
    private DataArchiveClearParameters dataArchiveClearParameters;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("dataArchiveTaskId");

        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId("taskId");
        scheduleTaskEntity.setJobName("jobName");
        scheduleTaskEntity.setJobGroup("jobGroup");
        scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
        scheduleTaskEntity.setResultJson(JsonUtils.toJsonString(Collections.emptyList()));
        scheduleTaskEntity.setStatus(TaskStatus.DONE);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        // given
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        // when
        dataArchiveDeleteJob.execute(null);

        // then
        verify(scheduleTaskRepository, times(1))
                .updateStatusById(eq(scheduleTaskEntity.getId()), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_DataArchiveTaskNotFinished() {
        // given
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        // when
        DataArchiveClearParameters newDataArchiveClearParameters = new DataArchiveClearParameters();
        newDataArchiveClearParameters.setDataArchiveTaskId("dataArchiveTaskId");
        newDataArchiveClearParameters.setStatus(TaskStatus.RUNNING);

        when(scheduleTaskRepository.findById(eq("dataArchiveTaskId")))
                .thenReturn(Optional.of(new ScheduleTaskEntity()));

        dataArchiveDeleteJob.execute(null);

        // then
        verify(scheduleTaskRepository, times(1))
                .updateStatusById(eq(scheduleTaskEntity.getId()), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_Success() {
        // given
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());

        when(objectMapper.readValue(anyString(), new TypeReference<List<DlmTask>>() {}))
                .thenReturn(dlmTasks);

        // when
        dataArchiveDeleteJob.execute(null);

        // then
        verify(scheduleTaskRepository, times(1))
                .updateStatusById(eq(scheduleTaskEntity.getId()),
                        eq(TaskStatus.values()[TaskStatus.values().length - 1]));
    }
}