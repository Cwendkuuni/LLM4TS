import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

    private OscService oscService;

    @Mock
    private DatabaseRepository databaseRepository;
    @Mock
    private ConnectionService connectionService;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private ScheduleRepository scheduleRepository;

    @Before
    public void setUp() {
        oscService = new OscService();
        oscService.databaseRepository = databaseRepository;
        oscService.connectionService = connectionService;
        oscService.scheduleTaskRepository = scheduleTaskRepository;
        oscService.scheduleRepository = scheduleRepository;
    }

    @Test
    public void testGetOscDatabaseInfo() {
        // Mocking
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        // Test
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        // Verify
        assertNotNull(result);
        assertEquals(1L, result.getDatabaseId().longValue());
    }

    @Test
    public void testSwapTable() {
        // Mocking
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeScheduleTaskResult taskResult = new OnlineSchemaChangeScheduleTaskResult();
        taskResult.setManualSwapTableEnabled(true);
        String jobParametersJson = "{\"swapTableType\":\"MANUAL\"}";
        when(scheduleTaskEntity.getResultJson()).thenReturn(JsonUtils.toJson(taskResult));
        when(scheduleEntity.getJobParametersJson()).thenReturn(jobParametersJson);

        // Test
        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);

        // Verify
        assertNotNull(result);
        assertEquals(scheduleTaskId, result.getScheduleTaskId());
    }

    @Test
    public void testGetLockUserIsRequired() {
        // Mocking
        Long connectionId = 1L;
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(connectionConfig);
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Test
        boolean result = oscService.getLockUserIsRequired(connectionId);

        // Verify
        assertFalse(result);
    }
}