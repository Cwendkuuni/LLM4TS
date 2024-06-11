package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskService;

public class OnlineSchemaChangeFlowableTaskTest {

    private OnlineSchemaChangeFlowableTask task;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private ScheduleService scheduleService;
    @Mock
    private ScheduleTaskService scheduleTaskService;
    @Mock
    private QuartzJobService quartzJobService;
    @Mock
    private OnlineSchemaChangeTaskHandler taskHandler;
    @Mock
    private OscTaskCompleteHandler completeHandler;
    @Mock
    private OrganizationService organizationService;
    @Mock
    private TaskService taskService;
    @Mock
    private DelegateExecution execution;

    @Before
    public void setUp() {
        task = new OnlineSchemaChangeFlowableTask();
        task.setScheduleTaskRepository(scheduleTaskRepository);
        task.setScheduleService(scheduleService);
        task.setScheduleTaskService(scheduleTaskService);
        task.setQuartzJobService(quartzJobService);
        task.setTaskHandler(taskHandler);
        task.setCompleteHandler(completeHandler);
        task.setOrganizationService(organizationService);
    }

    @Test
    public void testStart() throws Exception {
        when(scheduleTaskService.listTask(any(Pageable.class), anyLong()))
                .thenReturn(new PageImpl<>(Collections.emptyList()));
        task.start(1L, taskService, execution);
        verify(taskService, times(1)).start(1L);
    }

    @Test
    public void testIsSuccessful() {
        task.setStatus(TaskStatus.DONE);
        assertEquals(true, task.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        task.setStatus(TaskStatus.FAILED);
        assertEquals(true, task.isFailure());
    }

    @Test
    public void testOnFailure() {
        task.onFailure(1L, taskService);
        verify(taskService, times(1)).onFailure(1L);
    }

    @Test
    public void testOnSuccessful() {
        task.onSuccessful(1L, taskService);
        verify(taskService, times(1)).onSuccessful(1L);
    }

    @Test
    public void testOnTimeout() {
        task.onTimeout(1L, taskService);
        assertNotNull(taskService);
    }

    @Test
    public void testOnProgressUpdate() {
        task.onProgressUpdate(1L, taskService);
        assertNotNull(taskService);
    }

    @Test
    public void testSingleTaskPercentage() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setStatus(TaskStatus.PREPARING);
        assertEquals(0.0, task.singleTaskPercentage(scheduleTask), 0);
    }

    @Test
    public void testCancel() {
        task.cancel(true, 1L, taskService);
        assertTrue(task.isCancelled());
    }

}