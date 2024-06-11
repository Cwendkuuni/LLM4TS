package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.InjectMocks;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.task.TaskService;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    @Mock
    DelegateExecution delegateExecution;
    @Mock
    User user;
    @Mock
    TaskService taskService;

    @Test
    public void testStart() throws Exception {
        Long taskId = 1L;
        when(user.getId()).thenReturn(1L);
        when(user.getOrganizationId()).thenReturn(1L);
        when(delegateExecution.getVariable("taskCreator")).thenReturn(user);
        
        //will use more mock objects to fill other required data. This is just an example.
        
        onlineSchemaChangeFlowableTask.start(taskId, taskService, delegateExecution);
    }

    @Test
    public void testIsSuccessful() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;
        boolean result = onlineSchemaChangeFlowableTask.isSuccessful();
        assertTrue(result);
    }

    //Will continue to create test methods for all methods like `testIsFailure`, `testOnFailure` and so on...
}