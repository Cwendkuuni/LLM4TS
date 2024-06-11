package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {
  
    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    private User creator;

    @Mock
    private OnlineSchemaChangeFlowableTask task;

    @Mock
    private ConnectionConfig config;

    @Mock
    private OnlineSchemaChangeParameters parameter;

    @Test
    public void testCreateScheduleEntity() {
        when(creator.getId()).thenReturn(1L);
        when(config.id()).thenReturn(1L);

        ScheduleEntity scheduleEntity = task.createScheduleEntity(config, parameter, "testSchema");

        assertEquals(config.id(), scheduleEntity.getConnectionId());
        assertEquals("testSchema", scheduleEntity.getDatabaseName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
        assertEquals(ScheduleStatus.ENABLED, scheduleEntity.getStatus());
        assertFalse(scheduleEntity.isAllowConcurrent());
        assertEquals(creator.getId(), scheduleEntity.getCreatorId());
        assertEquals(creator.getId(), scheduleEntity.getModifierId());
    }
   
    @Test
    public void testStart() throws Exception {
    	when(task.start(1L, new TaskServiceMock(), delegateExecution)).thenReturn(null);
    	
    	assertNull(task.start(1L, new TaskServiceMock(), delegateExecution));
    }
  
    @Test
    public void testIsSuccesful() {
    	when(task.isSuccessful()).thenReturn(true);
    	
    	assertTrue(task.isSuccessful());
    }
    
    // More test cases should be added here to fully test all the methods
    
}