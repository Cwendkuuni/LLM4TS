import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class OscServiceTest {

    private OscService oscService;

    @Before
    public void setup() {
        oscService = new OscService();
        oscService.databaseRepository = mock(DatabaseRepository.class);
        oscService.connectionService = mock(ConnectionService.class);
        oscService.scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        oscService.scheduleRepository = mock(ScheduleRepository.class);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        // Test case for existing database
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        when(oscService.databaseRepository.findById(1L)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo oscLockDatabaseUserInfo = oscService.getOscDatabaseInfo(1L);

        assertNotNull(oscLockDatabaseUserInfo);
        assertEquals(1L, oscLockDatabaseUserInfo.getDatabaseId());
        assertFalse(oscLockDatabaseUserInfo.isLockDatabaseUserRequired());

        // Test case for non-existing database
        when(oscService.databaseRepository.findById(2L)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo emptyDatabaseInfo = oscService.getOscDatabaseInfo(2L);

        assertNotNull(emptyDatabaseInfo);
        assertEquals(0L, emptyDatabaseInfo.getDatabaseId());
        assertFalse(emptyDatabaseInfo.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        // Test case for valid swap table
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        when(oscService.scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
        when(oscService.scheduleRepository.findById(1L)).thenReturn(Optional.of(new ScheduleEntity("1", JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, "params")));
        
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        scheduleTaskEntity.setResultJson("result");
        when(oscService.scheduleTaskRepository.updateTaskResult(1L, "result")).thenReturn(true);
        
        OscSwapTableVO oscSwapTableVO = oscService.swapTable(1L);

        assertNotNull(oscSwapTableVO);
        assertEquals(1L, oscSwapTableVO.getScheduleTaskId());

        // Test case for invalid swap table parameters
        ScheduleEntity scheduleEntityInvalid = new ScheduleEntity("1", JobType.RELOAD_JOB, "params");
        when(oscService.scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntityInvalid));

        try {
            oscService.swapTable(1L);
            fail("Expected BadArgument exception");
        } catch (Exception e) {
            assertEquals("Task type is not ONLINE_SCHEMA_CHANGE", e.getMessage());
        }
    }

    @Test
    public void testGetLockUserIsRequired() {
        // Test case for lock user required
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("dialect");
        ConnectionService mockConnectionService = mock(ConnectionService.class);
        when(mockConnectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(connectionConfig);
        oscService.connectionService = mockConnectionService;

        boolean isLockUserRequired = oscService.getLockUserIsRequired(1L);

        assertTrue(isLockUserRequired);

        // Test case for lock user not required
        ConnectionConfig connectionConfigNoLock = new ConnectionConfig();
        connectionConfigNoLock.setDialectType("other");
        when(mockConnectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(connectionConfigNoLock);

        isLockUserRequired = oscService.getLockUserIsRequired(2L);

        assertFalse(isLockUserRequired);
    }
}