
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;

public class OscServiceTest {

    @Mock
    DatabaseRepository databaseRepository;
    @Mock
    ConnectionService connectionService;
    @Mock
    ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    ScheduleRepository scheduleRepository;

    private OscService oscService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        oscService = new OscService();
        oscService.setDatabaseRepository(databaseRepository);
        oscService.setConnectionService(connectionService);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
        oscService.setScheduleRepository(scheduleRepository);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        // Test case to check if getOscDatabaseInfo returns correct database info
        // Test data
        Long databaseId = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(databaseId);
        OscLockDatabaseUserInfo expectedOutput = new OscLockDatabaseUserInfo();
        expectedOutput.setDatabaseId(databaseId);
        // Mock behavior
        when(databaseRepository.findById(databaseId)).thenReturn(Optional.of(databaseEntity));
        // Actual test
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(databaseId);
        // Assertion
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testSwapTable() {
        // Test case to check if swapTable returns correct OscSwapTableVO
        // Test data
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{ \"swapTableType\": \"MANUAL\" }");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        // Mock behavior
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(Long.parseLong(scheduleTaskEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        // Actual test
        OscSwapTableVO oscSwapTableVO = oscService.swapTable(scheduleTaskId);
        // Assertion
        assertFalse(oscSwapTableVO.isManualSwapTableEnabled());
        assertTrue(oscSwapTableVO.isManualSwapTableStarted());
    }

    @Test
    public void testGetLockUserIsRequired() {
        // Test case to check if getLockUserIsRequired returns correct boolean value
        // Test data
        Long connectionId = 1L;
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");
        // Mock behavior
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(connectionConfig);
        // Actual test
        boolean lockUserRequired = oscService.getLockUserIsRequired(connectionId);
        // Assertion
        assertTrue(lockUserRequired);
    }
}
