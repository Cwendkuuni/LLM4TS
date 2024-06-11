package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
public class DataArchiveDeleteJobTest {
    private ScheduleTaskRepository scheduleTaskRepository;
    private ObjectMapper objectMapper;
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        objectMapper = new ObjectMapper();
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(-1L);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
        doReturn(Optional.empty()).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

        dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFinished() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
        taskEntity.setStatus(TaskStatus.RUNNING);
        doReturn(Optional.of(taskEntity)).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

        dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_Success() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
        taskEntity.setStatus(TaskStatus.DONE);
        doReturn(Optional.of(taskEntity)).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

        List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());

        dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.COMPLETED);
        verify(dataArchiveDeleteJob, times(1)).executeTask(taskEntity.getId(), taskUnits);
    }
}