import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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

    @InjectMocks
    private OscService oscService;

    @Before
    public void setUp() throws Exception {
        // Initialize any mock data or setup required for the tests
    }

    @Test
    public void testGetOscDatabaseInfo() {
        // Test for valid database ID
        Long validId = 1L;
        // Prepare mock data
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(validId);
        databaseEntity.setConnectionId(1L);
        when(databaseRepository.findById(validId)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo oscLockDatabaseUserInfo = oscService.getOscDatabaseInfo(validId);
        assertEquals(validId, oscLockDatabaseUserInfo.getDatabaseId());

        // Test for invalid database ID
        Long invalidId = 2L;
        when(databaseRepository.findById(invalidId)).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo oscLockDatabaseUserInfoInvalid = oscService.getOscDatabaseInfo(invalidId);
        assertNull(oscLockDatabaseUserInfoInvalid.getDatabaseId());
    }

    @Test
    public void testSwapTable() {
        // Test for valid schedule task ID
        Long validScheduleTaskId = 1L;
        // Prepare mock data
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setTaskId(validScheduleTaskId);
        when(scheduleTaskRepository.findById(validScheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        String jsonParams = ""; // Provide valid JSON data for parameters
        String jsonResult = ""; // Provide valid JSON data for result
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(new ScheduleEntity()));
        when(JsonUtils.fromJson(anyString(), eq(OnlineSchemaChangeParameters.class))).thenReturn(new OnlineSchemaChangeParameters());
        when(JsonUtils.fromJson(anyString(), eq(OnlineSchemaChangeScheduleTaskResult.class))).thenReturn(new OnlineSchemaChangeScheduleTaskResult());
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(new ConnectionConfig());

        OscSwapTableVO oscSwapTableVO = oscService.swapTable(validScheduleTaskId);
        assertEquals(validScheduleTaskId, oscSwapTableVO.getScheduleTaskId());

        // Test for invalid schedule task ID
        Long invalidScheduleTaskId = 2L;
        when(scheduleTaskRepository.findById(invalidScheduleTaskId)).thenReturn(Optional.empty());
        OscSwapTableVO oscSwapTableVOInvalid = oscService.swapTable(invalidScheduleTaskId);
        assertNull(oscSwapTableVOInvalid.getScheduleTaskId());
    }

    @Test
    public void testGetLockUserIsRequired() {
        // Test for valid connection ID
        Long validConnectionId = 1L;
        // Prepare mock data
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("test");
        when(connectionService.getForConnectionSkipPermissionCheck(validConnectionId)).thenReturn(connectionConfig);
        // Mock the behavior of OscDBUserUtil method
        when(OscDBUserUtil.isLockUserRequired(anyString(), any())).thenReturn(true);

        boolean lockUserRequired = oscService.getLockUserIsRequired(validConnectionId);
        assertTrue(lockUserRequired);

        // Test for invalid connection ID
        Long invalidConnectionId = 2L;
        // Prepare mock data
        when(connectionService.getForConnectionSkipPermissionCheck(invalidConnectionId)).thenReturn(connectionConfig);
        // Mock the behavior of OscDBUserUtil method
        when(OscDBUserUtil.isLockUserRequired(anyString(), any())).thenReturn(false);

        boolean lockUserNotRequired = oscService.getLockUserIsRequired(invalidConnectionId);
        assertFalse(lockUserNotRequired);
    }
}