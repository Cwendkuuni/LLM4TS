package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @Mock
    JobExecutionContext jobExecutionContext;
    
    @InjectMocks
    DataArchiveRollbackJob dataArchiveRollbackJob;
    
    @Mock
    ScheduleTaskEntity scheduleTaskEntity;
    
    @Test
    public void testExecute() {
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\":\"1\"}");
        
        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(anyString())).thenReturn(dataArchiveTaskOption);
        
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setSourceDatabaseId(1L);
        taskUnit.setTargetDatabaseId(2L);
        taskUnits.add(taskUnit);
        when(dataArchiveTask.getResultJson()).thenReturn(taskUnits);
        dataArchiveRollbackJob.execute(jobExecutionContext);
        
        verify(scheduleTaskEntity).setResultJson(anyString());
        verify(scheduleTaskRepository).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
        verify(scheduleTaskRepository).findById(anyString());
    }

    @Test
    public void testExecuteWhenDataArchiveTaskNotPresent() {
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\":\"1\"}");
        
        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.empty();
        when(scheduleTaskRepository.findById(anyString())).thenReturn(dataArchiveTaskOption);
        
        dataArchiveRollbackJob.execute(jobExecutionContext);
        
        verify(scheduleTaskEntity, never()).setResultJson(anyString());
        verify(scheduleTaskRepository).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
        verify(scheduleTaskRepository).findById(anyString());
    }
}