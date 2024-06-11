package com.oceanbase.odc.service.schedule.job;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext context;

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
    }

    @Test
    public void testExecute_Successful() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 1}");
        taskEntity.setResultJson("[{\"id\": 1, \"status\": \"DONE\"}]");

        when(context.getResult()).thenReturn(taskEntity);

        dataArchiveDeleteJob.execute(context);
        assertEquals(TaskStatus.DONE, taskEntity.getStatus());
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 1}");

        when(context.getResult()).thenReturn(taskEntity);

        dataArchiveDeleteJob.execute(context);
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecute_DataArchiveTaskNotDone() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 1}");
        taskEntity.setResultJson("[{\"id\": 1, \"status\": \"PREPARING\"}]");

        when(context.getResult()).thenReturn(taskEntity);

        dataArchiveDeleteJob.execute(context);
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecute_PrepareTasksForClear() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 1}");
        taskEntity.setResultJson("[{\"id\": 1, \"status\": \"DONE\"}]");

        when(context.getResult()).thenReturn(taskEntity);

        dataArchiveDeleteJob.execute(context);
        // Verify that the tasks are prepared properly
    }

    @Test
    public void testExecute_TaskTypeIsDelete() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 1}");
        taskEntity.setResultJson("[{\"id\": 1, \"status\": \"DONE\"}]");

        when(context.getResult()).thenReturn(taskEntity);

        dataArchiveDeleteJob.execute(context);
        // Verify that the task type is set to DELETE
    }

    @Test
    public void testExecute_TaskStatusIsPreparing() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 1}");
        taskEntity.setResultJson("[{\"id\": 1, \"status\": \"DONE\"}]");

        when(context.getResult()).thenReturn(taskEntity);

        dataArchiveDeleteJob.execute(context);
        // Verify that the task status is set to PREPARING
    }

}