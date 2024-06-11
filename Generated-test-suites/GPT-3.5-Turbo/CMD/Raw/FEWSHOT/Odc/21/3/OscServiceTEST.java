package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OscServiceTest {

    @Test
    public void testGetOscDatabaseInfo() {
        OscService oscService = new OscService();
        
        // Test case where database exists
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);
        assertNotNull(oscDatabase);
        assertEquals(1L, oscDatabase.getDatabaseId());
        assertTrue(oscDatabase.isLockDatabaseUserRequired());
        
        // Test case where database does not exist
        oscDatabase = oscService.getOscDatabaseInfo(2L);
        assertNotNull(oscDatabase);
        assertEquals(0L, oscDatabase.getDatabaseId());
        assertFalse(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        OscService oscService = new OscService();
        
        // Test case for manual swap table
        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        assertNotNull(oscSwapTable);
        assertEquals(1L, oscSwapTable.getScheduleTaskId());
        assertTrue(oscSwapTable.isManualSwapTableStarted());
        
        // Test case for manual swap table already enabled
        oscSwapTable = oscService.swapTable(2L);
        assertNotNull(oscSwapTable);
        assertEquals(2L, oscSwapTable.getScheduleTaskId());
        assertFalse(oscSwapTable.isManualSwapTableStarted());
    }

}