package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.common.json.JsonUtils;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;
    
    @Mock
    private JobExecutionContext context;
    
    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;
    
    @Mock
    private DataArchiveRollbackParameters rollbackParameters;

    @Mock
    private DlmTask dlmTask;

    @Test
    public void testExecuteWhenOptionPresent() {
        when(context.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskEntity.getParametersJson())
          .thenReturn(JsonUtils.toJson(rollbackParameters));
        when(rollbackParameters.getDataArchiveTaskId()).thenReturn(1L);
        when(scheduleTaskRepository.findById(1L))
          .thenReturn(Optional.of(scheduleTaskEntity));
        
        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(dlmTask);
        when(JsonUtils.fromJson(scheduleTaskEntity.getResultJson(), any())).thenReturn(taskUnits);

        dataArchiveRollbackJob.execute(context);
        
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any());
    }

    @Test
    public void testExecuteWhenOptionNotPresent() {
        when(context.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskEntity.getParametersJson())
          .thenReturn(JsonUtils.toJson(rollbackParameters));
        when(rollbackParameters.getDataArchiveTaskId()).thenReturn(1L);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());
    
        dataArchiveRollbackJob.execute(context);
        
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any());
    }
}