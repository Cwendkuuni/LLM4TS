package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {
    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private ScheduleService scheduleService;
    @Mock
    private ScheduleTaskService scheduleTaskService;

    @Test
    public void testStart() throws Exception {
        Long taskId = 1L;
        DelegateExecution delegateExecution = mock(DelegateExecution.class);
        TaskService taskService = new TaskService();
        User mockCreator = new User();
        mockCreator.setId(123L);
        mockCreator.setOrganizationId(111L);
        when(FlowTaskUtil.getTaskCreator(delegateExecution)).thenReturn(mockCreator);
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 
        onlineSchemaChangeFlowableTask.start(taskId, taskService, delegateExecution); 

        verify(scheduleTaskRepository, times(1)).saveAndFlush(any(ScheduleTaskEntity.class));
    }

    // More tests for isSuccessful, isFailure, onFailure, onSuccessful, onTimeout, onProgressUpdate,
    // cancel, isCancelled can be added as per project specific requirements.
}