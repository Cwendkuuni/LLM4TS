package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import java.util.ArrayList;
import java.util.Optional;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private TaskService taskService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private ScheduleTaskService scheduleTaskService;

    @Mock
    private Page<ScheduleTaskEntity> tasks;

    @Mock
    private DelegateExecution execution;

    @Mock
    private User creator;

    @Mock
    private ScheduleEntity scheduleEntity;

    @Mock
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Test
    public void testIsCancelled() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.CANCELED;
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());

        onlineSchemaChangeFlowableTask.status = TaskStatus.RUNNING;
        assertFalse(onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    public void testIsSuccessful() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());

        onlineSchemaChangeFlowableTask.status = TaskStatus.RUNNING;
        assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.FAILED;
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());

        onlineSchemaChangeFlowableTask.status = TaskStatus.RUNNING;
        assertFalse(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testStart() throws Exception {
        onlineSchemaChangeFlowableTask.continueOnError = true;
        onlineSchemaChangeFlowableTask.scheduleId = 1L;
        onlineSchemaChangeFlowableTask.creatorId = 1L;
        onlineSchemaChangeFlowableTask.status = TaskStatus.RUNNING;

        when(execution.getVariable("creator", User.class)).thenReturn(creator);
        when(creator.getId()).thenReturn(onlineSchemaChangeFlowableTask.creatorId);
        when(creator.getOrganizationId()).thenReturn(onlineSchemaChangeFlowableTask.organizationId);
        when(scheduleService.create(scheduleEntity)).thenReturn(scheduleEntity);
        when(scheduleEntity.getId()).thenReturn(onlineSchemaChangeFlowableTask.scheduleId);

        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

        assertTrue(onlineSchemaChangeFlowableTask.status == TaskStatus.RUNNING);
        assertTrue(onlineSchemaChangeFlowableTask.scheduleId == 1L);
        assertTrue(onlineSchemaChangeFlowableTask.creatorId == 1L);
    }

    @Test
    public void testProgressStatusUpdate() {
        ArrayList<ScheduleTaskEntity> scheduleTaskEntities = new ArrayList<>();
        ScheduleTaskEntity task1 = new ScheduleTaskEntity();
        ScheduleTaskEntity task2 = new ScheduleTaskEntity();
        task1.setStatus(TaskStatus.DONE);
        task2.setStatus(TaskStatus.FAILED);
        scheduleTaskEntities.add(task1);
        scheduleTaskEntities.add(task2);
        when(tasks.getContent()).thenReturn(scheduleTaskEntities);
        
        onlineSchemaChangeFlowableTask.progressStatusUpdate(tasks);
        
        assertTrue(onlineSchemaChangeFlowableTask.status == TaskStatus.FAILED);

        scheduleTaskEntities.clear();
        task1.setStatus(TaskStatus.PREPARING);
        task2.setStatus(TaskStatus.RUNNING);
        scheduleTaskEntities.add(task1);
        scheduleTaskEntities.add(task2);
        onlineSchemaChangeFlowableTask.progressStatusUpdate(tasks);
        
        assertTrue(onlineSchemaChangeFlowableTask.status == null);
    }

    @Test
    public void testSingleTaskPercentageStatusPreparesAndRuns() {
        ScheduleTaskEntity task = new ScheduleTaskEntity();
        task.setStatus(TaskStatus.PREPARING);
        assertTrue(0 == onlineSchemaChangeFlowableTask.singleTaskPercentage(task));

        task.setStatus(TaskStatus.RUNNING);
        task.setProgressPercentage(50);
        assertTrue(0.5 == onlineSchemaChangeFlowableTask.singleTaskPercentage(task));
    }

    @Test
    public void testSingleTaskPercentageDefault() {
        ScheduleTaskEntity task = new ScheduleTaskEntity();
        task.setStatus(TaskStatus.DONE);
        assertTrue(1 == onlineSchemaChangeFlowableTask.singleTaskPercentage(task));
    }

    @Test
    public void cancelTests() {
        ScheduleTaskEntity task = new ScheduleTaskEntity();
        task.setId(1L);
        task.setStatus(TaskStatus.RUNNING);
        when(scheduleTaskService.listTask(Pageable.unpaged(), 1L)).thenReturn(Optional.of(task));
        boolean result = onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);
        assertTrue(result);
        assertTrue(onlineSchemaChangeFlowableTask.status == TaskStatus.CANCELED);
    }
}
