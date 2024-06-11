import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    @Autowired
    private ConnectionService connectionService;

    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfoWithValidId() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);

        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertEquals(id, result.getDatabaseId());
        assertTrue(result.isLockDatabaseUserRequired());

        verify(databaseRepository, times(1)).findById(id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOscDatabaseInfoWithInvalidId() {
        Long id = -1L;

        when(databaseRepository.findById(id)).thenReturn(Optional.empty());

        oscService.getOscDatabaseInfo(id);

        verify(databaseRepository, times(1)).findById(id);
    }

    @Test
    public void testSwapTableWithValidScheduleTaskId() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("scheduleId");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));

        oscService.swapTable(scheduleTaskId);

        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
        verify(scheduleRepository, times(1)).findById(Long.parseLong(scheduleEntity.getJobName()));

        // close manual swap table
        result.setManualSwapTableEnabled(false);
        verify(scheduleTaskRepository).updateTaskResult(scheduleTaskId, JsonUtils.toJson(result));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTableWithInvalidScheduleTaskId() {
        Long scheduleTaskId = -1L;

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());

        oscService.swapTable(scheduleTaskId);

        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
    }

    @Test
    public void testGetLockUserIsRequiredWithMysqlDialect() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("MySQL");

        assertTrue(oscService.getLockUserIsRequired(connectionId));

        ConnectionSessionFactory factory = Mockito.mock(ConnectionSessionFactory.class);
        ConnectionSession connSession = Mockito.mock(ConnectionSession.class);
        String version = "5.6.30";
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);
        when(factory.generateSession()).thenReturn(connSession);
        when(ConnectionSessionUtil.getVersion(connSession)).thenReturn(version);

        assertTrue(OscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType(), () -> {
            ConnectionSessionFactory factory1 = new DefaultConnectSessionFactory(decryptedConnConfig);
            ConnectionSession connSession1 = factory1.generateSession();
            return ConnectionSessionUtil.getVersion(connSession1);
        }));
    }

    @Test
    public void testGetLockUserIsRequiredWithObServerDialect() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("ObServer");

        assertFalse(oscService.getLockUserIsRequired(connectionId));

        ConnectionSessionFactory factory = Mockito.mock(ConnectionSessionFactory.class);
        ConnectionSession connSession = Mockito.mock(ConnectionSession.class);
        String version = "3.0.7";
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);
        when(factory.generateSession()).thenReturn(connSession);
        when(ConnectionSessionUtil.getVersion(connSession)).thenReturn(version);

        assertFalse(OscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType(), () -> {
            ConnectionSessionFactory factory1 = new DefaultConnectSessionFactory(decryptedConnConfig);
            ConnectionSession connSession1 = factory1.generateSession();
            return ConnectionSessionUtil.getVersion(connSession1);
        }));
    }
}