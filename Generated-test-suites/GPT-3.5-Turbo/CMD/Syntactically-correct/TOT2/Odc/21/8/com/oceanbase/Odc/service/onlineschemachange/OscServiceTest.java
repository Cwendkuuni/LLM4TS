import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class OscServiceTest {

    private OscService oscService;

    @Before
    public void setUp() {
        oscService = new OscService();
        oscService.databaseRepository = mock(DatabaseRepository.class);
        oscService.connectionService = mock(ConnectionService.class);
        oscService.scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        oscService.scheduleRepository = mock(ScheduleRepository.class);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        // Test for existing database entry
        Long databaseId = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(databaseId);
        when(oscService.databaseRepository.findById(databaseId)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(databaseId);
        assertEquals(databaseId, result.getDatabaseId());

        // Test for non-existing database entry
        Long nonExistingDatabaseId = 2L;
        when(oscService.databaseRepository.findById(nonExistingDatabaseId)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo nonExistingResult = oscService.getOscDatabaseInfo(nonExistingDatabaseId);
        assertNull(nonExistingResult.getDatabaseId());
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        when(oscService.scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSwapTableType(SwapTableType.MANUAL);
        when(oscService.scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleEntity));

        // Test for successful swap table
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        String resultJson = JsonUtils.toJson(result);
        when(oscService.scheduleTaskRepository.updateTaskResult(scheduleTaskId, resultJson)).thenReturn(true);

        OscSwapTableVO swapTableResult = oscService.swapTable(scheduleTaskId);
        assertEquals(scheduleTaskId, swapTableResult.getScheduleTaskId());
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);

        ConnectionService connectionService = mock(ConnectionService.class);
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(connectionConfig);

        boolean lockUserRequired = oscService.getLockUserIsRequired(connectionId);
        assertTrue(lockUserRequired);
    }
}