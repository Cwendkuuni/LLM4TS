
package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.Optional;

public class OnlineSchemaChangeFlowableTaskTest {

    OnlineSchemaChangeFlowableTask task;

    @Before
    public void setUp() {
        task = new OnlineSchemaChangeFlowableTask();
        task.scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        task.scheduleService = mock(ScheduleService.class);
        task.scheduleTaskService = mock(ScheduleTaskService.class);
        task.quartzJobService = mock(QuartzJobService.class);
        task.taskHandler = mock(OnlineSchemaChangeTaskHandler.class);
        task.completeHandler = mock(OscTaskCompleteHandler.class);
        task.organizationService = mock(OrganizationService.class);
    }
    
    @Test
    public void testStart() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        task.start(1L, new MockTaskService(), execution); // add necessary mocking/example data inside start
        assertEquals(TaskStatus.RUNNING, task.status);
    }

    @Test
    public void testIsSuccessful() {
        task.status = TaskStatus.DONE;
        assertTrue(task.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        task.status = TaskStatus.FAILED;
        assertTrue(task.isFailure());
    }

    @Test
    public void testOnFailure() {
        task.onFailure(1L, new MockTaskService());
        assertEquals(TaskStatus.FAILED, task.status);
    }

    @Test
    public void testOnSuccessful() {
        task.onSuccessful(1L, new MockTaskService());
        assertEquals(TaskStatus.DONE, task.status);
    }

    @Test
    public void testCancel() {
        task.status = TaskStatus.RUNNING;
        assertTrue(task.cancel(true, 1L, new MockTaskService()));
        assertEquals(TaskStatus.CANCELED, task.status);
    }

    @Test
    public void testIsCancelled() {
        task.status = TaskStatus.CANCELED;
        assertTrue(task.isCancelled());
    }
    
    @Test
    public void testOnProgressUpdate(){
        // Mock expected behaviour and necessary data here
    }

    @Test
    public void testOnTimeout(){
        // Mock expected behaviour and necessary data here
    }

}
