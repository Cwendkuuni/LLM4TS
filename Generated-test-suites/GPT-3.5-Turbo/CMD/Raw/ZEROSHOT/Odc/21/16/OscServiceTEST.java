import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class OscServiceTest {

    @Test
    public void testGetOscDatabaseInfo() {
        // Setup
        OscService oscService = new OscService();
        DatabaseRepository databaseRepository = mock(DatabaseRepository.class);
        oscService.databaseRepository = databaseRepository;
        
        OscLockDatabaseUserInfo expectedOscDatabase = new OscLockDatabaseUserInfo();
        expectedOscDatabase.setDatabaseId(1L);
        expectedOscDatabase.setLockDatabaseUserRequired(true);
        
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(new DatabaseEntity()));
        
        // Test
        OscLockDatabaseUserInfo actualOscDatabase = oscService.getOscDatabaseInfo(1L);
        
        // Verify
        assertEquals(expectedOscDatabase.getDatabaseId(), actualOscDatabase.getDatabaseId());
        assertEquals(expectedOscDatabase.isLockDatabaseUserRequired(), actualOscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        // Setup
        OscService oscService = new OscService();
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        ScheduleRepository scheduleRepository = mock(ScheduleRepository.class);
        
        oscService.scheduleTaskRepository = scheduleTaskRepository;
        oscService.scheduleRepository = scheduleRepository;
        
        Long scheduleTaskId = 1L;
        
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        
        // Test
        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);
        
        // Verify
        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
    }

    @Test
    public void testGetLockUserIsRequired() {
        // Setup
        OscService oscService = new OscService();
        ConnectionService connectionService = mock(ConnectionService.class);
        oscService.connectionService = connectionService;
        
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");
        
        when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(connectionConfig);
        
        OscDBUserUtil oscDBUserUtil = mock(OscDBUserUtil.class);
        when(oscDBUserUtil.isLockUserRequired("mysql", () -> "version")).thenReturn(true);
        
        // Test
        boolean lockUserRequired = oscService.getLockUserIsRequired(1L);
        
        // Verify
        assertEquals(true, lockUserRequired);
    }
}