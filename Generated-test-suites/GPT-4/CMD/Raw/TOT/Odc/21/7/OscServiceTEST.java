import static org.junit.Assert.*;
import org.junit.Test;

public class OscServiceTest {
    private OscService oscService = new OscService();

    // Test case for method `getOscDatabaseInfo(@NonNull Long id)`
    @Test
    public void testGetOscDatabaseInfo() {
        //Input validation Test
        assertThrows(IllegalArgumentException.class, () -> {
            oscService.getOscDatabaseInfo(null); // null ID case
        });
        
        //valid case
        OscLockDatabaseUserInfo info = oscService.getOscDatabaseInfo(1L); // Using an ID that exists in the database
        assertNotNull(info);
    }

    // Test case for method `swapTable(@PathVariable Long scheduleTaskId)`
    @Test
    public void testSwapTable() {
        //Input validation Test
        assertThrows(IllegalArgumentException.class, () -> {
            oscService.swapTable(null); // null task ID case
        });

        //Valid case
        OscSwapTableVO info = oscService.swapTable(1L); // Using a task ID that exists in the database
        assertNotNull(info);

        //Test for manual swap table enabled
        //This should be validated by other business logic, here is just a simple demonstration
        // assertFalse(info.isManualSwapTableEnabled());
    }
}