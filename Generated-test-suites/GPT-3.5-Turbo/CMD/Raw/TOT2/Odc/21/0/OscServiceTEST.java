import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class OscServiceTest {

    @Test
    public void testGetOscDatabaseInfo() {
        // Test for positive case
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);
        assertEquals(1L, oscDatabase.getDatabaseId().longValue());
        assertTrue(oscDatabase.isLockDatabaseUserRequired());

        // Test for negative case
        oscDatabase = oscService.getOscDatabaseInfo(100L);
        assertEquals(0L, oscDatabase.getDatabaseId().longValue());
        assertFalse(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        // Test for positive case
        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        assertEquals(1L, oscSwapTable.getScheduleTaskId().longValue());

        // Test for negative cases

        // Test for invalid ScheduleTaskId
        oscSwapTable = oscService.swapTable(100L);
        assertEquals(0L, oscSwapTable.getScheduleTaskId().longValue());
        
    }

    @Test
    public void testGetLockUserIsRequired() {
        // Test for positive case
        boolean lockUserRequired = oscService.getLockUserIsRequired(1L);
        assertTrue(lockUserRequired);

        // Test for negative case
        lockUserRequired = oscService.getLockUserIsRequired(100L);
        assertFalse(lockUserRequired);
    }
}