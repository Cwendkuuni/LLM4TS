package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskService;


public class OnlineSchemaChangeFlowableTaskTest {
    
    @InjectMocks
    OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private ScheduleService scheduleService;
    @Mock
    private ScheduleTaskService scheduleTaskService;
    @Mock
    private OnlineSchemaChangeTaskHandler taskHandler;
    @Mock
    private OscTaskCompleteHandler completeHandler;
    @Mock
    private User creator;
    @Mock
    private DelegateExecution delegateExecution;
   
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        onlineSchemaChangeFlowableTask.setCreatorId(1L);
        onlineSchemaChangeFlowableTask.setOrganizationId(1L);
    }
    
    @Test
    public void testStart() throws Exception {
        when(creator.getId()).thenReturn(1L);
        when(creator.getOrganizationId()).thenReturn(1L);
        
        onlineSchemaChangeFlowableTask.start(1L, new TaskService(), delegateExecution);

        verify(scheduleService, times(1)).create(new ScheduleEntity());
        verify(completeHandler, times(1)).handle();
    }

    @Test
    public void testIsSuccessful() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);
        
        assertEquals(true, onlineSchemaChangeFlowableTask.isSuccessful());
    }
    
    @Test
    public void testIsFailure() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);
        
        assertEquals(true, onlineSchemaChangeFlowableTask.isFailure());
    }
    
    
    @Test
    public void testOnProgressUpdate() {
        List<ScheduleTaskEntity> scheduleTaskEntities = new ArrayList<>();
        scheduleTaskEntities.add(new ScheduleTaskEntity());

        Page<ScheduleTaskEntity> scheduleTaskEntityPage = new PageImpl<>(scheduleTaskEntities);
        when(scheduleTaskService.listTask(Pageable.unpaged(), 1L)).thenReturn(scheduleTaskEntityPage);
        
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, new TaskService());

        verify(scheduleTaskService, times(1)).listTask(Pageable.unpaged(), 1L);
    }
    
    @Test
    public void testCancel() {
        assertEquals(true, onlineSchemaChangeFlowableTask.cancel(true, 1L, new TaskService()));
        assertEquals(TaskStatus.CANCELED, onlineSchemaChangeFlowableTask.getStatus());
    }
    
    @Test
    public void testIsCancelled() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.CANCELED);
        
        assertEquals(true, onlineSchemaChangeFlowableTask.isCancelled());
    }    

    @Test
    public void testCreateScheduleEntity() {
        ScheduleEntity result = onlineSchemaChangeFlowableTask.createScheduleEntity(new ConnectionConfig(),
                                            new OnlineSchemaChangeParameters(), "schema");

        assertNotNull(result);
    }

}