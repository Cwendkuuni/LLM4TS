import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.session.ConnectionConfig;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.OscServiceTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class OscServiceTest {

    @MockBean
    private DatabaseRepository databaseRepository;
    @MockBean
    private ConnectionService connectionService;
    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;
    @MockBean
    private ScheduleRepository scheduleRepository;

    @Autowired
    private OscService oscService;

    @Before
    public void setup() {
        Mockito.doNothing().when(databaseRepository).save(ArgumentMatchers.any());
        Mockito.doReturn(true).when(connectionService).isConnectionExist(ArgumentMatchers.anyLong());
        Mockito.doReturn(new ConnectionConfig()).when(connectionService).getForConnectionSkipPermissionCheck(ArgumentMatchers.anyLong());
        Mockito.doReturn(new DatabaseEntity()).when(databaseRepository).findById(ArgumentMatchers.anyLong()).get();
        Mockito.doReturn(new ScheduleEntity()).when(scheduleRepository).findById(ArgumentMatchers.anyLong()).get();
        Mockito.doReturn(new ScheduleTaskEntity()).when(scheduleTaskRepository).findById(ArgumentMatchers.anyLong()).get();
        Mockito.doReturn(new OnlineSchemaChangeScheduleTaskResult()).when(scheduleTaskRepository).findLastResultByScheduleIdAndSwapTableType(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(SwapTableType.MANUAL));
        Mockito.doReturn(new OnlineSchemaChangeParameters()).when(JsonUtils::fromJson);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertNull(result.getDatabaseId());
        assertFalse(result.isLockDatabaseUserRequired());
        OscLockDatabaseUserInfo expected = new OscLockDatabaseUserInfo();
        expected.setDatabaseId(1L);
        expected.setLockDatabaseUserRequired(false);
        oscService.getOscDatabaseInfo(id);
        assertEquals(expected, result);
    }

    @Test
    public void testSwapTable_Success() {
        Long scheduleTaskId = 1L;
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        when(scheduleTaskRepository.findLastResultByScheduleIdAndSwapTableType(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(SwapTableType.MANUAL)))
                .thenReturn(result);
        OnlineSchemaChangeScheduleTaskResult expected = new OnlineSchemaChangeScheduleTaskResult();
        expected.setManualSwapTableEnabled(false);
        expected.setManualSwapTableStarted(true);
        oscService.swapTable(scheduleTaskId);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_InvalidScheduleTask() {
        Long scheduleTaskId = 1L;
        when(scheduleTaskRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());
        oscService.swapTable(scheduleTaskId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_InvalidSchedule() {
        Long scheduleId = 1L;
        when(scheduleRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSwapTableType(SwapTableType.MANUAL);
        when(JsonUtils.fromJson(ArgumentMatchers.anyString(), OnlineSchemaChangeParameters.class))
                .thenReturn(parameters);
        when(scheduleEntity.getJobType()).thenReturn(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        Long scheduleTaskId = 1L;
        oscService.swapTable(scheduleTaskId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_InvalidSwapTableType() {
        Long scheduleTaskId = 1L;
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSwapTableType(SwapTableType.BACKUP);
        when(JsonUtils.fromJson(ArgumentMatchers.anyString(), OnlineSchemaChangeParameters.class))
                .thenReturn(parameters);
        oscService.swapTable(scheduleTaskId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_InvalidResultJson() {
        Long scheduleTaskId = 1L;
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);
        result.setManualSwapTableStarted(false);
        when(scheduleTaskRepository.findLastResultByScheduleIdAndSwapTableType(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(SwapTableType.MANUAL)))
                .thenReturn(result);
        oscService.swapTable(scheduleTaskId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLockUserIsRequired_InvalidConnection() {
        Long connectionId = 1L;
        oscService.getLockUserIsRequired(connectionId);
    }

    @Test
    public void testGetLockUserIsRequired_LockUserRequired() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("OBServer");
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .thenReturn(decryptedConnConfig);
        String version = null;
        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
        ConnectionSession connSession = null;
        try {
            connSession = factory.generateSession();
            version = ConnectionSessionUtil.getVersion(connSession);
        } catch (Exception e) {
            log.info("Get connection occur error", e);
        } finally {
            if (connSession != null) {
                connSession.expire();
            }
        }
        assertTrue(OscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType(), () -> {
            ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
            String version = null;
            ConnectionSession connSession = null;
            try {
                connSession = factory.generateSession();
                version = ConnectionSessionUtil.getVersion(connSession);
            } catch (Exception ex) {
                log.info("Get connection occur error", ex);
            } finally {
                if (connSession != null) {
                    connSession.expire();
                }
            }
            return version;
        }));
        oscService.getLockUserIsRequired(connectionId);
        assertTrue(oscService.getLockUserIsRequired(connectionId));
    }

    @Test
    public void testGetLockUserIsRequired_NoLockUserRequired() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("MySQL");
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .thenReturn(decryptedConnConfig);
        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
        String version = "5.5.55";
        ConnectionSession connSession = null;
        try {
            connSession = factory.generateSession();
            version = ConnectionSessionUtil.getVersion(connSession);
        } catch (Exception e) {
            log.info("Get connection occur error", e);
        } finally {
            if (connSession != null) {
                connSession.expire();
            }
        }
        assertFalse(OscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType(), () -> {
            ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
            String version = null;
            ConnectionSession connSession = null;
            try {
                connSession = factory.generateSession();
                version = ConnectionSessionUtil.getVersion(connSession);
            } catch (Exception ex) {
                log.info("Get connection occur error", ex);
            } finally {
                if (connSession != null) {
                    connSession.expire();
                }
            }
            return version;
        }));
        oscService.getLockUserIsRequired(connectionId);
        assertFalse(oscService.getLockUserIsRequired(connectionId));
    }
}