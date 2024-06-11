import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class OscServiceTest {

    @Test
    public void testGetOscDatabaseInfo() {
        // Test for valid database ID
        DatabaseRepository databaseRepository = mock(DatabaseRepository.class);
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(new DatabaseEntity()));
        OscService oscService = new OscService();
        oscService.databaseRepository = databaseRepository;

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);

        assertEquals(1, result.getDatabaseId());
        assertEquals(false, result.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        // Test for valid schedule task ID
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        ScheduleRepository scheduleRepository = mock(ScheduleRepository.class);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(false);

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(new ScheduleEntity()));

        OscService oscService = new OscService();
        oscService.scheduleTaskRepository = scheduleTaskRepository;
        oscService.scheduleRepository = scheduleRepository;

        OscSwapTableVO swapTableResult = oscService.swapTable(1L);

        assertEquals(1, swapTableResult.getScheduleTaskId());
    }

    @Test
    public void testGetLockUserIsRequired() {
        // Test edge case where lock user is required
        ConnectionService connectionService = mock(ConnectionService.class);
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong()))
                .thenReturn(new ConnectionConfig());

        OscDBUserUtil oscDBUserUtil = mock(OscDBUserUtil.class);
        when(oscDBUserUtil.isLockUserRequired(anyString(), any())).thenReturn(true);

        OscService oscService = new OscService();
        oscService.connectionService = connectionService;

        boolean lockUserRequired = oscService.getLockUserIsRequired(1L);

        assertEquals(true, lockUserRequired);
    }

}