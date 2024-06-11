import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleType;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("unchecked")
public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    private DatabaseRepository databaseRepository;
    private ConnectionService connectionService;
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        databaseRepository = Mockito.mock(DatabaseRepository.class);
        connectionService = Mockito.mock(ConnectionService.class);
        scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        oscService.setDatabaseRepository(databaseRepository);
        oscService.setConnectionService(connectionService);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testGetOscDatabaseInfo_DatabaseNotExist_ReturnsEmptyOscDatabaseInfo() {
        Long id = 1L;
        Mockito.when(databaseRepository.findById(id)).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(id);
        assertNull(oscDatabaseInfo.getDatabaseId());
        assertFalse(oscDatabaseInfo.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_DatabaseExist_ReturnsOscDatabaseInfoWithDatabaseIdAndLockUserRequired() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        Mockito.when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));
        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(id);
        assertTrue(oscDatabaseInfo.getDatabaseId() == 1L);
        assertTrue(oscDatabaseInfo.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable_InvalidScheduleTaskId_ThrowsException() {
        Long scheduleTaskId = -1L;
        Mockito.when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());
        try {
            oscService.swapTable(scheduleTaskId);
            assertTrue(false);
        } catch (Exception e) {
            //expected exception
        }
    }

    @Test
    public void testSwapTable_ValidScheduleTask_ReturnsOscSwapTableVO() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("test");
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        Mockito.when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        Mockito.when(databaseRepository.findById(1L)).thenReturn(Optional.of(new DatabaseEntity()));
        Mockito.when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);
        result.setManualSwapTableStarted(false);

        Mockito.when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), OnlineSchemaChangeParameters.class))
                .thenReturn(oscParameters);
        Mockito.when(JsonUtils.fromJson(scheduleTask.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class))
                .thenReturn(result);

        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);
        assertTrue(oscSwapTable.getScheduleTaskId() == scheduleTaskId);
    }

    @Test
    public void testGetLockUserIsRequired_ConnectionConfigWithLockUser_ReturnsTrue() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("OBServer");
        String version = "3.0.0";
        connectionConfig.setVersion(version);
        assertTrue(OscDBUserUtil.isLockUserRequired(connectionConfig.getDialectType(), () -> {
            ConnectionSessionFactory factory = new DefaultConnectSessionFactory(connectionConfig);
            ConnectionSession connSession = factory.generateSession();
            ConnectionSessionUtil.getVersion(connSession);
            return version;
        }));
    }

    @Test
    public void testGetLockUserIsRequired_ConnectionConfigWithoutLockUser_ReturnsFalse() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");
        String version = "3.0.0";
        connectionConfig.setVersion(version);
        assertFalse(OscDBUserUtil.isLockUserRequired(connectionConfig.getDialectType(), () -> {
            ConnectionSessionFactory factory = new DefaultConnectSessionFactory(connectionConfig);
            ConnectionSession connSession = factory.generateSession();
            ConnectionSessionUtil.getVersion(connSession);
            return version;
        }));
    }

    @Test
    public void testGetLockUserIsRequired_ConnectionConfigWithOBServerAndInvalidVersion_ReturnsFalse() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("OBServer");
        String version = "3.1.0";
        connectionConfig.setVersion(version);
        assertFalse(OscDBUserUtil.isLockUserRequired(connectionConfig.getDialectType(), () -> {
            ConnectionSessionFactory factory = new DefaultConnectSessionFactory(connectionConfig);
            ConnectionSession connSession = factory.generateSession();
            throw new RuntimeException("Test exception");
        }));
    }
}