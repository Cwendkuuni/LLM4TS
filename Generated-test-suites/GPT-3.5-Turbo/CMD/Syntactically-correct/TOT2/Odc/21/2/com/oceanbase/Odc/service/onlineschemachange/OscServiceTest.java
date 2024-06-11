import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        when(oscService.databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertNotNull(result);
        assertEquals(databaseEntity.getDatabaseId(), result.getDatabaseId());
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        when(oscService.scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        when(oscService.scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        when(JsonUtils.fromJson(anyString(), eq(OnlineSchemaChangeParameters.class))).thenReturn(oscParameters);
        when(JsonUtils.fromJson(anyString(), eq(OnlineSchemaChangeScheduleTaskResult.class))).thenReturn(result);

        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);

        assertNotNull(oscSwapTable);
        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
        assertFalse(result.isManualSwapTableEnabled());
        assertTrue(result.isManualSwapTableStarted());
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        when(oscService.connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(connectionConfig);
        when(connectionSession.getVersion()).thenReturn("5.7");

        boolean lockUserRequired = oscService.getLockUserIsRequired(connectionId);

        assertTrue(lockUserRequired);
    }

}