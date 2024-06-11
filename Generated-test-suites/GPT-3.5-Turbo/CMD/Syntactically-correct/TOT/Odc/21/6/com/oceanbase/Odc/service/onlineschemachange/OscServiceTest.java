import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    private OscService oscService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        oscService = new OscService();
        oscService.setDatabaseRepository(databaseRepository);
        oscService.setConnectionService(connectionService);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
        oscService.setScheduleRepository(scheduleRepository);
    }

    @Test
    public void testGetOscDatabaseInfo_whenDatabaseExists_returnOscLockDatabaseUserInfo() {
        // Setup
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        // Verify
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        // Validate
        assertNotNull(result);
        assertEquals(id, result.getDatabaseId());
        assertFalse(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_whenDatabaseDoesNotExist_returnEmptyOscLockDatabaseUserInfo() {
        // Setup
        Long id = 1L;
        when(databaseRepository.findById(id)).thenReturn(Optional.empty());

        // Verify
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        // Validate
        assertNotNull(result);
        assertNull(result.getDatabaseId());
        assertFalse(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable_whenScheduleTaskExists_returnOscSwapTableVO() {
        // Setup
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setJobName("1");
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{\"swapTableType\": \"MANUAL\"}");
        when(scheduleRepository.findById(Long.parseLong(scheduleTask.getJobName()))).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        when(scheduleTask.getResultJson()).thenReturn(JsonUtils.toJson(result));

        // Verify
        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);

        // Validate
        assertNotNull(oscSwapTable);
        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
        assertFalse(result.isManualSwapTableEnabled());
        assertTrue(result.isManualSwapTableStarted());
    }

}