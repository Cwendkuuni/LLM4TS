import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    public void testGetOscDatabaseInfo_ValidId_ReturnsOscLockDatabaseUserInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertNotNull(result);
        assertEquals(id, result.getDatabaseId());
    }

    @Test
    public void testGetOscDatabaseInfo_InvalidId_ReturnsEmptyOscLockDatabaseUserInfo() {
        Long id = 2L;
        when(databaseRepository.findById(id)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertNotNull(result);
        assertNull(result.getDatabaseId());
    }

    @Test
    public void testSwapTable_ValidScheduleTaskId_ReturnsOscSwapTableVO() {
        Long scheduleTaskId = 101L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("101");
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        String jobParametersJson = JsonUtils.toJson(oscParameters);
        when(scheduleRepository.findById(Long.parseLong(scheduleTaskEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeScheduleTaskResult scheduleTaskResult = new OnlineSchemaChangeScheduleTaskResult();
        scheduleTaskResult.setManualSwapTableEnabled(true);
        when(scheduleTaskRepository.updateTaskResult(scheduleTaskId, JsonUtils.toJson(scheduleTaskResult))).thenReturn(true);

        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);

        assertNotNull(result);
        assertEquals(scheduleTaskId, result.getScheduleTaskId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_InvalidScheduleTaskId_ThrowsIllegalArgumentException() {
        Long scheduleTaskId = 102L;
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());

        oscService.swapTable(scheduleTaskId);
    }
}