package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {
	
    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;
    
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    
    @Mock
    private JobExecutionContext context;
    
    private ScheduleTaskEntity taskEntity;
    private DataArchiveRollbackParameters rollbackParameters;
    private Optional<ScheduleTaskEntity> dataArchiveTaskOption;
    private ScheduleTaskEntity dataArchiveTask;
    private List<DlmTask> taskUnits;
    
    @Before
    public void setUp() {
    	taskEntity = mock(ScheduleTaskEntity.class);
    	rollbackParameters = mock(DataArchiveRollbackParameters.class);
    	dataArchiveTaskOption = Optional.of(mock(ScheduleTaskEntity.class));
    	dataArchiveTask = mock(ScheduleTaskEntity.class);
    	taskUnits = new ArrayList<>();
    	
    	when(context.getResult()).thenReturn(taskEntity);
    	when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(rollbackParameters));
    	when(rollbackParameters.getDataArchiveTaskId()).thenReturn(1L);
    	when(scheduleTaskRepository.findById(1L)).thenReturn(dataArchiveTaskOption);
    	when(dataArchiveTaskOption.get()).thenReturn(dataArchiveTask);
    	when(dataArchiveTask.getResultJson()).thenReturn(JsonUtils.toJson(taskUnits));
    }

    @Test
    public void testExecute() {
    	dataArchiveRollbackJob.execute(context);
        verify(scheduleTaskRepository, times(1)).findById(rollbackParameters.getDataArchiveTaskId());
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
    }
}
