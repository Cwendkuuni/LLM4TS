package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;
    
    @Mock
    private ConnectionService connectionService;
    
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    
    @Mock
    private ScheduleRepository scheduleRepository;
    
    private OscService oscService;
    
    @Before
    public void setUp() {
        this.oscService = new OscService();
        this.oscService.setDatabaseRepository(databaseRepository);
        this.oscService.setConnectionService(connectionService);
        this.oscService.setScheduleTaskRepository(scheduleTaskRepository);
        this.oscService.setScheduleRepository(scheduleRepository);
    }
    
    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);
        
        assertNotNull(oscDatabase);
        assertEquals(id, oscDatabase.getDatabaseId());
        assertFalse(oscDatabase.isLockDatabaseUserRequired());
    }
    
    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);
        
        assertNotNull(oscSwapTable);
        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
    }
    
    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(new ConnectionConfig());
        when(OscDBUserUtil.isLockUserRequired(any(), any())).thenReturn(true);
        
        boolean lockUserRequired = oscService.getLockUserIsRequired(connectionId);
        
        assertTrue(lockUserRequired);
    }
}