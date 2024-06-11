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
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);
        assertNotNull(oscDatabase);
        assertEquals(id, oscDatabase.getDatabaseId());
        assertFalse(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        String jobName = "1";
        Long scheduleId = Long.parseLong(jobName);
        String jobParametersJson = "{}";
        String resultJson = "{}";

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        scheduleTaskEntity.setJobName(jobName);
        scheduleTaskEntity.setResultJson(resultJson);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        scheduleEntity.setJobParametersJson(jobParametersJson);
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeScheduleTaskResult taskResult = new OnlineSchemaChangeScheduleTaskResult();
        taskResult.setManualSwapTableEnabled(true);
        taskResult.setManualSwapTableStarted(false);
        String updatedResultJson = "{}";
        when(scheduleTaskRepository.updateTaskResult(scheduleTaskId, updatedResultJson)).thenReturn(true);

        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);
        assertNotNull(oscSwapTable);
        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        ConnectionConfig connectionConfig = new ConnectionConfig();
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(connectionConfig);

        assertTrue(oscService.getLockUserIsRequired(connectionId));
    }
}