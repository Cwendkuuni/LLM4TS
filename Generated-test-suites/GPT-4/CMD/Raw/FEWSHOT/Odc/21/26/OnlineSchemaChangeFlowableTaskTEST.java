
package com.oceanbase.odc.service.onlineschemachange;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.flowable.engine.delegate.DelegateExecution;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;

import java.util.ArrayList;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private DelegateExecution execution;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    @Mock
    private TaskService taskService;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private User creator;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask task;

    @Before
    public void init(){
        when(creator.getId()).thenReturn(1L);
        when(creator.getOrganizationId()).thenReturn(1L);
        
        task.setCreatorId(1L);
        task.setOrganizationId(1L);
        task.setStatus(TaskStatus.RUNNING);
        task.setPercentage(0.0);
        task.setContinueOnError(false);
        task.setTaskId(1L);
    }

    @Test
    public void testStart() throws Exception{
        task.start(1L, taskService, execution);
        verify(taskService).start(1L);
    }

    @Test
    public void testIsSuccessful() {
        task.setStatus(TaskStatus.DONE);
        assert task.isSuccessful();
    }

    @Test
    public void testIsFailure(){
        task.setStatus(TaskStatus.FAILED);
        assert task.isFailure();
    }

    @Test
    public void testOnFailure(){
        task.onFailure(1L, taskService);
        verify(taskService).failure(1L);
    }
 
    @Test
    public void testOnSuccessful(){
        task.onSuccessful(1L, taskService);
        verify(taskService).successful(1L);
    }

    @Test
    public void testCancel(){
        task.cancel(false, 1L, taskService);
        verify(taskService).cancel(1L);
    }

    @Test
    public void testIsCancelled(){
        task.setStatus(TaskStatus.CANCELED);
        assert task.isCancelled();
    }
}
