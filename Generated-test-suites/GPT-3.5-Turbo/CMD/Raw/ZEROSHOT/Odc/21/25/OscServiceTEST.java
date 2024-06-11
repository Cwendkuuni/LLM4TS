import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class OscServiceTest {

    private OscService oscService;

    private DatabaseRepository databaseRepository;
    private ConnectionService connectionService;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ScheduleRepository scheduleRepository;

    @Before
    public void setUp() {
        oscService = new OscService();
        databaseRepository = mock(DatabaseRepository.class);
        connectionService = mock(ConnectionService.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        scheduleRepository = mock(ScheduleRepository.class);
        oscService.setDatabaseRepository(databaseRepository);
        oscService.setConnectionService(connectionService);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
        oscService.setScheduleRepository(scheduleRepository);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        // Mock data
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        // Test method
        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(id);

        // Verify result
        assertNotNull(oscDatabaseInfo);
        assertEquals(id, oscDatabaseInfo.getDatabaseId());
        assertFalse(oscDatabaseInfo.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        // Mock data
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        // Test method
        OscSwapTableVO oscSwapTableVO = oscService.swapTable(scheduleTaskId);

        // Verify result
        assertNotNull(oscSwapTableVO);
        assertEquals(scheduleTaskId, oscSwapTableVO.getScheduleTaskId());
    }

    @Test
    public void testGetLockUserIsRequired() {
        // Mock data
        Long connectionId = 1L;
        ConnectionConfig connectionConfig = new ConnectionConfig();
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(connectionConfig);

        // Test method
        boolean lockUserRequired = oscService.getLockUserIsRequired(connectionId);

        // Verify result
        assertFalse(lockUserRequired);
    }

}