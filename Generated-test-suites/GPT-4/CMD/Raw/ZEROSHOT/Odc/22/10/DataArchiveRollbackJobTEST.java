
package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext context;
    
    @Mock
    private ScheduleTaskEntity taskEntity;

    @Mock
    private DataArchiveRollbackParameters rollbackParameters;

    @InjectMocks
    private DataArchiveRollbackJob job = new DataArchiveRollbackJob();

    @Test
    public void testExecute() {

        when(context.getResult()).thenReturn(taskEntity);
        
        UUID id = UUID.randomUUID();
        when(rollbackParameters.getDataArchiveTaskId()).thenReturn(id);
        when(scheduleTaskRepository.findById(id)).thenReturn(Optional.of(taskEntity));
        
        DlmTask task = new DlmTask();
        task.setSourceDatabaseId(1L);
        task.setTargetDatabaseId(2L);
        task.setStatus(TaskStatus.PREPARING);

        List<DlmTask> tasks = Arrays.asList(task);

        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(rollbackParameters));
        when(taskEntity.getResultJson()).thenReturn(JsonUtils.toJson(tasks));
        when(scheduleTaskRepository.findById(id)).thenReturn(Optional.of(taskEntity));
        
        job.execute(context);

        verify(taskEntity, times(1)).setJobType(JobType.ROLLBACK);
        verify(taskEntity, times(1)).setStatus(TaskStatus.DONE);
        verify(scheduleTaskRepository, times(2)).updateStatusById(any(), any());
    }

    @Test
    public void testExecuteWithoutDataArchiveTask() {

        when(context.getResult()).thenReturn(taskEntity);

        UUID id = UUID.randomUUID();
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(rollbackParameters));
        when(rollbackParameters.getDataArchiveTaskId()).thenReturn(id);

        when(scheduleTaskRepository.findById(id)).thenReturn(Optional.empty());

        job.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }
}
