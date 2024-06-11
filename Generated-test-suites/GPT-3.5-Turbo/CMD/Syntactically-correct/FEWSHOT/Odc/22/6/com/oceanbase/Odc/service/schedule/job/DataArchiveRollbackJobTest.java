package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveRollbackJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    private DataArchiveRollbackJob rollbackJob;

    @Before
    public void setUp() {
        rollbackJob = new DataArchiveRollbackJob();
        rollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 123}");
        when(context.getResult()).thenReturn(taskEntity);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setResultJson("[{\"id\":1,\"sourceDatabaseId\":1,\"targetDatabaseId\":2,\"jobType\":\"ROLLBACK\",\"status\":\"PREPARING\"}]");
        when(scheduleTaskRepository.findById(123L)).thenReturn(dataArchiveTask);

        rollbackJob.execute(context);

        assertNotNull(taskEntity.getId());
        assertEquals(TaskStatus.DONE, taskEntity.getStatus());
    }

    @Test
    public void testExecute_TaskNotFound() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 123}");
        when(context.getResult()).thenReturn(taskEntity);

        when(scheduleTaskRepository.findById(123L)).thenReturn(null);

        rollbackJob.execute(context);

        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecute_NullDataArchiveTask() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 123}");
        when(context.getResult()).thenReturn(taskEntity);

        when(scheduleTaskRepository.findById(123L)).thenReturn(Optional.empty());

        rollbackJob.execute(context);

        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecute_NullTaskEntity() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(null);

        rollbackJob.execute(context);
    }
}