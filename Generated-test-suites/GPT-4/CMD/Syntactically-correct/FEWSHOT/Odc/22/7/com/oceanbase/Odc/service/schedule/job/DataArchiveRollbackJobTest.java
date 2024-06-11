
package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @Mock
    JobExecutionContext context;
    
    @Mock
    ScheduleTaskEntity taskEntity;

    @Mock
    ScheduleTaskEntity dataArchiveTask;

    @Mock
    DlmTask taskUnit;

    @InjectMocks
    DataArchiveRollbackJob rollbackJobInstance;

    @Before
    public void setup() {
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(new DataArchiveRollbackParameters()));
        when(taskEntity.getResultJson()).thenReturn(JsonUtils.toJson(Arrays.asList(taskUnit)));
        when(context.getResult()).thenReturn(taskEntity);
    }
   
    @Test
    public void testExecute() {
        Long dataArchiveTaskId = 1L;
        when(taskEntity.getId()).thenReturn(dataArchiveTaskId);
        when(dataArchiveTask.getId()).thenReturn(dataArchiveTaskId);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));

        when(taskUnit.getSourceDatabaseId()).thenReturn(1L);
        when(taskUnit.getTargetDatabaseId()).thenReturn(2L);
        when(taskUnit.getStatus()).thenReturn(TaskStatus.PREPARING);

        rollbackJobInstance.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
    }

    @Test
    public void testExecuteDataArchiveTaskNotFound() {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        rollbackJobInstance.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }   
}
