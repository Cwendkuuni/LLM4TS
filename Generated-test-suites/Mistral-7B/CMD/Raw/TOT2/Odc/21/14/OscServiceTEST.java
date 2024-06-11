import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.JobType;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import com.oceanbase.odc.service.test.util.TestUtils;

import static org.junit.Assert.*;
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

    // Initialize mocks
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setFieldValue(oscService, "databaseRepository", databaseRepository);
        ReflectionTestUtils.setFieldValue(oscService, "connectionService", connectionService);
        ReflectionTestUtils.setFieldValue(oscService, "scheduleTaskRepository", scheduleTaskRepository);
        ReflectionTestUtils.setFieldValue(oscService, "scheduleRepository", scheduleRepository);
        TestUtils.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        databaseEntity.setConnectionId(1L);

        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(id);
        assertEquals(id, oscDatabaseInfo.getDatabaseId());
        assertFalse(oscDatabaseInfo.isLockDatabaseUserRequired());

        verify(databaseRepository, times(1)).findById(id);
    }

    @Test
    public void testGetOscDatabaseInfoNotFound() {
        Long id = 1L;

        when(databaseRepository.findById(id)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(id);
        assertNull(oscDatabaseInfo);

        verify(databaseRepository, times(1)).findById(id);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setId(scheduleTaskId);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        oscService.swapTable(scheduleTaskId);

        verify(databaseRepository, never()).save(any(DatabaseEntity.class));
        verify(connectionService, never()).getForConnectionSkipPermissionCheck(anyLong());
        verify(scheduleTaskRepository, never()).updateTaskResult(scheduleTaskId, anyString());
    }

    @Test
    public void testSwapTable_InvalidJobType() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setId(scheduleTaskId);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.BACKUP);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        assertThrows(IllegalArgumentException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });

        verify(databaseRepository, never()).save(any(DatabaseEntity.class));
        verify(connectionService, never()).getForConnectionSkipPermissionCheck(anyLong());
        verify(scheduleTaskRepository, never()).updateTaskResult(scheduleTaskId, anyString());
    }

    @Test
    public void testSwapTable_InvalidSwapTableType() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setId(scheduleTaskId);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobParametersJson("{\"swapTableType\": \"INVALID\"}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        assertThrows(IllegalArgumentException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });

        verify(databaseRepository, never()).save(any(DatabaseEntity.class));
        verify(connectionService, never()).getForConnectionSkipPermissionCheck(anyLong());
        verify(scheduleTaskRepository, never()).updateTaskResult(scheduleTaskId, anyString());
    }

    @Test
    public void testSwapTable_ManualSwapTableDisabled() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setId(scheduleTaskId);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setResultJson(JsonUtils.toJson(result));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        assertThrows(IllegalArgumentException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });

        verify(databaseRepository, never()).save(any(DatabaseEntity.class));
        verify(connectionService, never()).getForConnectionSkipPermissionCheck(anyLong());
        verify(scheduleTaskRepository, never()).updateTaskResult(scheduleTaskId, anyString());
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("MySQL");

        String version = "5.6.30";

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .thenReturn(decryptedConnConfig);
        when(decryptedConnConfig.getDialectType()).thenReturn("MySQL");

        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
        ConnectionSession connSession = factory.generateSession();
        String connVersion = ConnectionSessionUtil.getVersion(connSession);

        assertTrue(OscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType(), () -> {
            ConnectionSessionUtil.getVersion(connSession);
            return connVersion;
        }));

        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(connectionId);
    }

    @Test
    public void testGetLockUserIsRequired_NoVersionGiven() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("MySQL");

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .thenReturn(decryptedConnConfig);

        assertFalse(OscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType(), () -> {
            throw new RuntimeException();
        }));

        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(connectionId);
    }

    @Test
    public void testGetLockUserIsRequired_VersionNotMySQL() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("Oracle");

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .thenReturn(decryptedConnConfig);

        assertFalse(OscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType(), () -> {
            throw new RuntimeException();
        }));

        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(connectionId);
    }

    @Test
    public void testSwapTable_Success() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setId(scheduleTaskId);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobParametersJson("{\"swapTableType\": \"MANUAL\"}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        oscService.swapTable(scheduleTaskId);

        verify(databaseRepository, never()).save(any(DatabaseEntity.class));
        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(anyLong());
        verify(scheduleTaskRepository, times(1)).updateTaskResult(scheduleTaskId, anyString());
    }
}