import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    // GetOscDatabaseInfo test cases

    @Test
    public void testGetOscDatabaseInfoWithValidId() {
        Long databaseId = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(databaseId);
        when(databaseRepository.findById(databaseId)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(databaseId);
        assertEquals(databaseId, result.getDatabaseId());
        verify(databaseRepository, times(1)).findById(databaseId);
    }

    @Test
    public void testGetOscDatabaseInfoWithInvalidId() {
        Long databaseId = 1L;
        when(databaseRepository.findById(databaseId)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(databaseId);
        assertNull(result);
        verify(databaseRepository, times(1)).findById(databaseId);
    }

    // swapTable test cases

    @Test
    public void testSwapTableWithValidScheduleTaskId() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);
        assertEquals(scheduleTaskId, result.getScheduleTaskId());
        verify(databaseRepository, never()).findById(anyLong());
        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
    }

    @Test(expected = RuntimeException.class)
    public void testSwapTableWithInvalidScheduleTaskId() {
        Long scheduleTaskId = 1L;
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(new ScheduleEntity()));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());

        oscService.swapTable(scheduleTaskId);
        fail("Method should have thrown an exception");
        verify(databaseRepository, never()).findById(anyLong());
        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
    }

    @Test
    public void testSwapTableWithInvalidJobType() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.DATA_PROCESSING);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        try {
            oscService.swapTable(scheduleTaskId);
            fail("Method should have thrown an exception");
        } catch (RuntimeException ex) {
            assertEquals("Task type is not ONLINE_SCHEMA_CHANGE", ex.getMessage());
            verify(databaseRepository, never()).findById(anyLong());
            verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
        }
    }

    // getLockUserIsRequired test cases

    @Test
    public void testGetLockUserIsRequiredWithValidConnectionId() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("MySQL");

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .thenReturn(decryptedConnConfig);
        when(OscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType()))
                .thenReturn(true);

        boolean result = oscService.getLockUserIsRequired(connectionId);
        assertTrue(result);
        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(connectionId);
    }

    @Test
    public void testGetLockUserIsRequiredWithInvalidConnectionId() {
        Long connectionId = 1L;

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .thenThrow(new RuntimeException("Connection not found"));

        boolean result = oscService.getLockUserIsRequired(connectionId);
        assertFalse(result);
        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(connectionId);
    }

    // Helper method for testing with mocked DatabaseEntity and ConnectionConfig
    private OngoingStubbing<ConnectionConfig> setupDatabaseAndConnectionMocks() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setConnectionId(1L);
        databaseEntity.setDatabaseId(2L);

        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("MySQL");
        decryptedConnConfig.setConnectionId(databaseEntity.getConnectionId());
        decryptedConnConfig.setDatabaseId(databaseEntity.getDatabaseId());

        return mock(ConnectionService.class)
                .when(connectionService)
                .getForConnectionSkipPermissionCheck(databaseEntity.getConnectionId())
                .thenReturn(decryptedConnConfig);
    }
}