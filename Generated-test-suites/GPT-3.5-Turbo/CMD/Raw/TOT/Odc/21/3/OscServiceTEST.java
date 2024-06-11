import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
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
        // Test for valid database id
        Long validId = 1L;
        DatabaseEntity dbEntity = new DatabaseEntity();
        dbEntity.setDatabaseId(validId);
        when(databaseRepository.findById(validId)).thenReturn(Optional.of(dbEntity));
        
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(validId);
        assertNotNull(result);
        assertEquals(result.getDatabaseId(), validId);
        
        // Test for invalid database id
        Long invalidId = 2L;
        when(databaseRepository.findById(invalidId)).thenReturn(Optional.empty());
        
        OscLockDatabaseUserInfo invalidResult = oscService.getOscDatabaseInfo(invalidId);
        assertNotNull(invalidResult);
        assertEquals(invalidResult.getDatabaseId(), null);
    }

    @Test
    public void testSwapTable() {
        // Test for valid schedule task id
        Long validScheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        when(scheduleTaskRepository.findById(validScheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(oscParameters));
        when(scheduleTaskEntity.getResultJson()).thenReturn(JsonUtils.toJson(result));
        
        OscSwapTableVO swapResult = oscService.swapTable(validScheduleTaskId);
        assertFalse(result.isManualSwapTableEnabled());
        assertTrue(result.isManualSwapTableStarted());
        assertEquals(swapResult.getScheduleTaskId(), validScheduleTaskId);
        
        // Test for invalid schedule task id
        Long invalidScheduleTaskId = 2L;
        when(scheduleTaskRepository.findById(invalidScheduleTaskId)).thenReturn(Optional.empty());
        
        OscSwapTableVO invalidResult = oscService.swapTable(invalidScheduleTaskId);
        assertNull(invalidResult);
    }

    @Test
    public void testGetLockUserIsRequired() {
        // Test for lock user required
        Long validConnectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("mysql");
        
        when(connectionService.getForConnectionSkipPermissionCheck(validConnectionId)).thenReturn(decryptedConnConfig);
        when(OscDBUserUtil.isLockUserRequired(anyString(), any())).thenReturn(true);
        
        assertTrue(oscService.getLockUserIsRequired(validConnectionId));
        
        // Test for lock user not required
        Long invalidConnectionId = 2L;
        ConnectionConfig invalidDecryptedConnConfig = new ConnectionConfig();
        invalidDecryptedConnConfig.setDialectType("oracle");
        
        when(connectionService.getForConnectionSkipPermissionCheck(invalidConnectionId)).thenReturn(invalidDecryptedConnConfig);
        when(OscDBUserUtil.isLockUserRequired(anyString(), any())).thenReturn(false);
        
        assertFalse(oscService.getLockUserIsRequired(invalidConnectionId));
    }

}