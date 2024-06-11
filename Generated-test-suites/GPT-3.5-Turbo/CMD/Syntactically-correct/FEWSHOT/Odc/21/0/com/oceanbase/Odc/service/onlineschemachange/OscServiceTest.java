import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class OscServiceTest {

    @Test
    public void testGetOscDatabaseInfo() {
        // Setup
        OscService oscService = new OscService();
        DatabaseRepository databaseRepository = mock(DatabaseRepository.class);
        oscService.setDatabaseRepository(databaseRepository);

        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        // Test
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);

        // Verify
        assertEquals(id, oscDatabase.getDatabaseId());
        assertFalse(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        // Setup
        OscService oscService = new OscService();
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        ScheduleRepository scheduleRepository = mock(ScheduleRepository.class);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
        oscService.setScheduleRepository(scheduleRepository);

        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson("{}");
        when(scheduleRepository.findById(any(Long.class))).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        when(scheduleTaskEntity.getResultJson()).thenReturn("{}");

        // Test
        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);

        // Verify
        verify(scheduleTaskRepository, times(1)).updateTaskResult(eq(scheduleTaskId), anyString());
        assertFalse(result.isManualSwapTableEnabled());
        assertTrue(result.isManualSwapTableStarted());
        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
    }

    @Test
    public void testGetLockUserIsRequired() {
        // Setup
        OscService oscService = new OscService();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        ConnectionService connectionService = mock(ConnectionService.class);
        oscService.setConnectionService(connectionService);

        Long connectionId = 1L;
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(connectionConfig);

        // Mocking OscDBUserUtil.isLockUserRequired() behavior
        String version = "1.0.0";
        DefaultConnectSessionFactory factory = mock(DefaultConnectSessionFactory.class);
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        when(factory.generateSession()).thenReturn(connectionSession);
        when(connectionSession.getVersion()).thenReturn(version);
        when(OscDBUserUtil.isLockUserRequired(anyString(), any())).thenReturn(true);

        // Test
        boolean lockUserRequired = oscService.getLockUserIsRequired(connectionId);

        // Verify
        assertTrue(lockUserRequired);
        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(eq(connectionId));
        verify(connectionSession, times(1)).expire();
    }
}