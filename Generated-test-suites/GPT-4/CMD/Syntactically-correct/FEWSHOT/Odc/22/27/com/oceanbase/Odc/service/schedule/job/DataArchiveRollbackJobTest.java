package com.oceanbase.odc.service.schedule.job;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @Test
    public void executeTest() {
        // Mock Dependencies
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        ScheduleTaskEntity dataArchiveTaskEntity = mock(ScheduleTaskEntity.class);
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        DlmTask dlmTask = mock(DlmTask.class);
        
        // Mock Behavior
        when(context.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(new DataArchiveRollbackParameters()));
        when(scheduleTaskEntity.getResultJson()).thenReturn(JsonUtils.toJson(Arrays.asList(dlmTask)));
        when(scheduleTaskEntity.getId()).thenReturn("1");
        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTaskEntity));
        when(dlmTask.getStatus()).thenReturn(TaskStatus.PREPARING);
        doNothing().when(dataArchiveTaskEntity).updateStatusById();

        // Create instance and test
        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.execute(context);

        // Verify the interactions
        verify(scheduleTaskRepository, times(1)).findById(anyString());
        verify(scheduleTaskEntity, times(1)).getId();
        verify(dlmTask, times(1)).getStatus();
        assertEquals("1", dlmTask.getId());
        assertEquals(JobType.ROLLBACK, dlmTask.getJobType());
        assertEquals(TaskStatus.PREPARING, dlmTask.getStatus());
    }
}