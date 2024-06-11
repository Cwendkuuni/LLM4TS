package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;


public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    private User user;

    @Mock
    private ScheduleEntity scheduleEntity;
	
    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask flowableTask;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStart() throws Exception {
        Mockito.when(user.getId()).thenReturn(1L);
        Mockito.when(user.getOrganizationId()).thenReturn(1L);
	Mockito.when(scheduleEntity.getId()).thenReturn(1L);
	Mockito.when(scheduleTaskEntity.getId()).thenReturn(1L);
        Mockito.when(delegateExecution.hasVariable(any())).thenReturn(true);
        assertEquals(null, flowableTask.start(1L, new javax.inject.Provider<TaskService>() {}, delegateExecution));
    }

    @Test
    public void testIsSuccessful() {
        flowableTask.status = TaskStatus.DONE;
        assertEquals(true, flowableTask.isSuccessful());
    }
	
    @Test
    public void testIsFailure() {
        flowableTask.status = TaskStatus.FAILED;
        assertEquals(true, flowableTask.isFailure());
    }
	
    @Test
    public void testIsCancelled() {
        flowableTask.status = TaskStatus.CANCELED;
        assertEquals(true, flowableTask.isCancelled());
    }
	
    @Test
    public void testOnProgressUpdate() {
        flowableTask.scheduleId = 1L;
	flowableTask.status = TaskStatus.RUNNING;
	flowableTask.percentage = 0.2;
	Page<ScheduleTaskEntity> page = new PageImpl<>(Arrays.asList(scheduleTaskEntity));
	Mockito.when(flowableTask.scheduleTaskService()).thenReturn(page);
	flowableTask.onProgressUpdate(1L, new javax.inject.Provider<TaskService>() {});
	assertNotNull(flowableTask.percentage);
    }
	
    @Test
    public void testCreateScheduleEntity() {
        assertNotNull(flowableTask.createScheduleEntity(new ConnectionConfig(), new OnlineSchemaChangeParameters(), "temp_schema"));
    }
	
    @Test
    public void testCreateScheduleTaskEntity() {
        assertNotNull(flowableTask.createScheduleTaskEntity(1L, new OnlineSchemaChangeScheduleTaskParameters()));
    }
}