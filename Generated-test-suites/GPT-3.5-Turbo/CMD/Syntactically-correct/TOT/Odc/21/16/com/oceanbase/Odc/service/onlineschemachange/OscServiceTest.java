import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OscServiceTest {

    private OscService oscService;

    @Before
    public void setUp() {
        oscService = new OscService();
    }

    @Test
    public void testGetOscDatabaseInfo() {
        // Test for valid database id
        Long validId = 1L;
        assertEquals(true, oscService.getOscDatabaseInfo(validId).isLockDatabaseUserRequired());
        
        // Test for invalid database id
        Long invalidId = -1L;
        assertEquals(false, oscService.getOscDatabaseInfo(invalidId).isLockDatabaseUserRequired());
        
        // Add more test cases for edge cases and error scenarios
    }

    @Test
    public void testSwapTable() {
        // Mock repositories
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        ScheduleRepository scheduleRepository = mock(ScheduleRepository.class);
        
        // Test for valid schedule task id
        Long validScheduleTaskId = 1L;
        assertNotNull(oscService.swapTable(validScheduleTaskId));
        
        // Test for invalid schedule task id
        Long invalidScheduleTaskId = -1L;
        assertNull(oscService.swapTable(invalidScheduleTaskId));
        
        // Add more test cases for edge cases and error scenarios
    }

}