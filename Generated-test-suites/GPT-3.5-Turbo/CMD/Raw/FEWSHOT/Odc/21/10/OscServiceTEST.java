package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OscServiceTest {

    @Test
    public void testGetOscDatabaseInfo() {
        // Test case where database with specified id exists
        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(1L);
        assertEquals(1L, oscDatabaseInfo.getDatabaseId());
        assertFalse(oscDatabaseInfo.isLockDatabaseUserRequired());

        // Test case where database with specified id does not exist
        OscLockDatabaseUserInfo oscDatabaseInfoNotExist = oscService.getOscDatabaseInfo(100L);
        assertNotNull(oscDatabaseInfoNotExist);
        assertNull(oscDatabaseInfoNotExist.getDatabaseId());
    }

    @Test
    public void testSwapTable() {
        // Test case where schedule task exists
        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        assertEquals(1L, oscSwapTable.getScheduleTaskId());

        // Test case where schedule task does not exist
        OscSwapTableVO oscSwapTableNotExist = oscService.swapTable(100L);
        assertNull(oscSwapTableNotExist);
    }

    @Test
    public void testGetLockUserIsRequired() {
        // Test case where lock user is required
        boolean lockUserRequired = oscService.getLockUserIsRequired(1L);
        assertTrue(lockUserRequired);

        // Test case where lock user is not required
        boolean lockUserNotRequired = oscService.getLockUserIsRequired(2L);
        assertFalse(lockUserNotRequired);
    }
}