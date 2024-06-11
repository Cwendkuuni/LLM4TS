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
        databaseRepository = mock(DatabaseRepository.class);
        connectionService = mock(ConnectionService.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        scheduleRepository = mock(ScheduleRepository.class);

        oscService = new OscService();
        oscService.setDatabaseRepository(databaseRepository);
        oscService.setConnectionService(connectionService);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
        oscService.setScheduleRepository(scheduleRepository);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        // Test when database is present
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setId(id);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertNotNull(result);
        assertEquals(id, result.getDatabaseId());
        assertFalse(result.isLockDatabaseUserRequired());

        // Test when database is not present
        when(databaseRepository.findById(2L)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo result2 = oscService.getOscDatabaseInfo(2L);
        assertNotNull(result2);
        assertNull(result2.getDatabaseId());
        assertFalse(result2.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        // Setup test data
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        String resultJson = JsonUtils.toJson(result);
        when(scheduleTaskEntity.getResultJson()).thenReturn(resultJson);

        // Test swapTable method
        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);
        assertNotNull(oscSwapTable);
        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
        assertFalse(result.isManualSwapTableEnabled());
        assertTrue(result.isManualSwapTableStarted());

        // Verify that updateTaskResult was called with correct arguments
        verify(scheduleTaskRepository).updateTaskResult(eq(scheduleTaskId), anyString());
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("MySQL");

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);
        when(OscDBUserUtil.isLockUserRequired(eq("MySQL"), any())).thenReturn(true);

        boolean lockUserRequired = oscService.getLockUserIsRequired(connectionId);
        assertTrue(lockUserRequired);
    }
}