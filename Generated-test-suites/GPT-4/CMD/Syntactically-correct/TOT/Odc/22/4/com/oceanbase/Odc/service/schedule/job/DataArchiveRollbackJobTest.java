package com.oceanbase.odc.service.schedule.job;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;

public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskEntity taskEntity;

    @Mock
    private DlmTask dlmTask;

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testExecute_WithValidData() throws JobExecutionException , Exceptionfo{
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        List<DlmTask> taskUnits = Arrays.asList(dlmTask);

        when(context.getResult()).thenReturn(taskEntity); 
        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));
        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);
        
        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
    }
    
    @Test
    public void testExecute_WithMissingDataArchiveTask() throws Exception {
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.empty());
        
        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }
}