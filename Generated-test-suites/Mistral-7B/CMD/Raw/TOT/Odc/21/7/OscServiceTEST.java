import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OMock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class OscServiceTest {

    @InjectMocks
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
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo_ValidDatabaseId_ReturnsOscDatabaseInfo() {
        Long databaseId = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(databaseId);
        when(databaseRepository.findById(databaseId)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo expectedOscDatabaseInfo = new OscLockDatabaseUserInfo();
        expectedOscDatabaseInfo.setDatabaseId(databaseId);
        expectedOscDatabaseInfo.setLockDatabaseUserRequired(true);

        OscLockDatabaseUserInfo actualOscDatabaseInfo = oscService.getOscDatabaseInfo(databaseId);

        verify(databaseRepository).findById(databaseId);
        assertEquals(expectedOscDatabaseInfo, actualOscDatabaseInfo);
    }

    @Test(expected = RuntimeException.class)
    public void testGetOscDatabaseInfo_InvalidDatabaseId_ThrowsRuntimeException() {
        Long databaseId = 1L;
        when(databaseRepository.findById(databaseId)).thenReturn(Optional.empty());

        oscService.getOscDatabaseInfo(databaseId);
    }

    @Test
    public void testSwapTable_ValidScheduleTaskId_ReturnsOscSwapTableVO() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("schedule_id_1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(oscParameters));

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        when(scheduleTask.getResultJson()).thenReturn(JsonUtils.toJson(result));

        OscSwapTableVO expectedOscSwapTable = new OscSwapTableVO();
        expectedOscSwapTable.setScheduleTaskId(scheduleTaskId);

        OscSwapTableVO actualOscSwapTable = oscService.swapTable(scheduleTaskId);

        verify(scheduleTaskRepository).findById(scheduleTaskId);
        verify(scheduleRepository).findById(Long.parseLong(scheduleEntity.getJobName()));
        assertEquals(expectedOscSwapTable, actualOscSwapTable);
    }

    @Test(expected = RuntimeException.class)
    public void testSwapTable_InvalidScheduleTaskId_ThrowsRuntimeException() {
        Long scheduleTaskId = 1L;
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testGetLockUserIsRequired_ConnectionWithLockUserRequired_ReturnsTrue() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("dialect_type_1");
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .thenReturn(decryptedConnConfig);

        String version = "version_1";
        when(() -> OscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType(), () -> {
            ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
            ConnectionSession connSession = mock(ConnectionSession.class);
            when(factory.generateSession()).thenReturn(connSession);
            when(ConnectionSessionUtil.getVersion(connSession)).thenReturn(version);
            return version;
        })).thenReturn(true);

        boolean expectedLockUserIsRequired = true;
        boolean actualLockUserIsRequired = oscService.getLockUserIsRequired(connectionId);

        verify(connectionService).getForConnectionSkipPermissionCheck(connectionId);
        assertTrue(actualLockUserIsRequired);
    }

    @Test
    public void testGetLockUserIsRequired_ConnectionWithoutLockUserRequired_ReturnsFalse() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("dialect_type_1");
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .thenReturn(decryptedConnConfig);

        String version = "version_1";
        when(() -> OscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType(), () -> {
            ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
            ConnectionSession connSession = mock(ConnectionSession.class);
            when(factory.generateSession()).thenReturn(connSession);
            when(ConnectionSessionUtil.getVersion(connSession)).thenReturn(version);
            return version;
        })).thenReturn(false);

        boolean expectedLockUserIsRequired = false;
        boolean actualLockUserIsRequired = oscService.getLockUserIsRequired(connectionId);

        verify(connectionService).getForConnectionSkipPermissionCheck(connectionId);
        assertFalse(actualLockUserIsRequired);
    }
}