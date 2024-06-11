package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class OscServiceTest {

    @Test
    public void testGetOscDatabaseInfo() {
        OscService oscService = new OscService();
        
        DatabaseRepository databaseRepository = mock(DatabaseRepository.class);
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(new DatabaseEntity(1L, 1L)));
        oscService.setDatabaseRepository(databaseRepository);
        
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
        
        assertNotNull(result);
        assertEquals(1L, result.getDatabaseId());
        assertFalse(result.isLockDatabaseUserRequired());
        
        // Test when database is not present
        when(databaseRepository.findById(2L)).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo result2 = oscService.getOscDatabaseInfo(2L);
        
        assertNotNull(result2);
        assertEquals(0L, result2.getDatabaseId());
        assertFalse(result2.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        OscService oscService = new OscService();
        
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        ScheduleRepository scheduleRepository = mock(ScheduleRepository.class);
        
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(new ScheduleTaskEntity("1", "data")));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(new ScheduleEntity()));
        
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
        oscService.setScheduleRepository(scheduleRepository);
        
        OscSwapTableVO result = oscService.swapTable(1L);
        
        assertNotNull(result);
        assertEquals(1L, result.getScheduleTaskId());
        
        // Test when schedule task is not present
        when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.empty());
        
        OscSwapTableVO result2 = oscService.swapTable(2L);
        
        assertNull(result2);
    }

    @Test
    public void testGetLockUserIsRequired() {
        OscService oscService = new OscService();
        
        ConnectionService connectionService = mock(ConnectionService.class);
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("test");
        
        when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(connectionConfig);
        oscService.setConnectionService(connectionService);
        
        assertTrue(oscService.getLockUserIsRequired(1L));
        
        // Test when connection service throws an exception
        when(connectionService.getForConnectionSkipPermissionCheck(2L)).thenThrow(new RuntimeException());
        
        assertFalse(oscService.getLockUserIsRequired(2L));
    }
}