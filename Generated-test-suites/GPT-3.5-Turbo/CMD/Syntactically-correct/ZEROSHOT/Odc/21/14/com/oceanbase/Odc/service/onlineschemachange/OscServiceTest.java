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
        // Test case for getOscDatabaseInfo method

        // Mock data
        Long databaseId = 123L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(databaseId);
        when(databaseRepository.findById(databaseId)).thenReturn(Optional.of(databaseEntity));

        // Test the method
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(databaseId);

        // Verify the result
        assertNotNull(oscDatabase);
        assertEquals(databaseId, oscDatabase.getDatabaseId());
        assertFalse(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        // Test case for swapTable method

        // Mock data
        Long scheduleTaskId = 456L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("789");
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        when(scheduleRepository.findById(Long.parseLong(scheduleTaskEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        when(JsonUtils.fromJson(scheduleTaskEntity.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class)).thenReturn(result);

        // Test the method
        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);

        // Verify the result
        assertNotNull(oscSwapTable);
        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
        assertFalse(result.isManualSwapTableEnabled());
        assertTrue(result.isManualSwapTableStarted());
    }

    @Test
    public void testGetLockUserIsRequired() {
        // Test case for getLockUserIsRequired method
        
        // Mock data
        Long connectionId = 789L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("mysql");

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);

        ConnectionSessionFactory factory = mock(DefaultConnectSessionFactory.class);
        ConnectionSession connSession = mock(ConnectionSession.class);

        when(factory.generateSession()).thenReturn(connSession);
        when(ConnectionSessionUtil.getVersion(connSession)).thenReturn("10.0.0");

        // Test the method
        boolean lockUserRequired = oscService.getLockUserIsRequired(connectionId);

        // Verify the result
        assertTrue(lockUserRequired);
    }
}