import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class OscServiceTest {

    private OscService oscService;

    @Before
    public void setUp() {
        oscService = new OscService();
    }

    @Test
    public void testGetOscDatabaseInfo() {
        // Mock DatabaseEntity
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        
        // Mock DatabaseRepository
        DatabaseRepository databaseRepository = mock(DatabaseRepository.class);
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(databaseEntity));
        oscService.setDatabaseRepository(databaseRepository);
        
        // Test getOscDatabaseInfo method
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);
        assertEquals(1L, oscDatabase.getDatabaseId().longValue());
        assertTrue(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        // Mock ScheduleTaskEntity
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setTaskId(1L);
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setResultJson("{}");
        
        // Mock ScheduleTaskRepository
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
        
        // Mock ScheduleEntity
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        
        // Mock ScheduleRepository
        ScheduleRepository scheduleRepository = mock(ScheduleRepository.class);
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        oscService.setScheduleRepository(scheduleRepository);
        
        // Test swapTable method
        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        assertEquals(1L, oscSwapTable.getScheduleTaskId().longValue());
    }

    @Test
    public void testGetLockUserIsRequired() {
        // Mock ConnectionService
        ConnectionService connectionService = mock(ConnectionService.class);
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(new ConnectionConfig());
        oscService.setConnectionService(connectionService);
        
        // Test getLockUserIsRequired method
        boolean result = oscService.getLockUserIsRequired(1L);
        assertFalse(result);
    }
}