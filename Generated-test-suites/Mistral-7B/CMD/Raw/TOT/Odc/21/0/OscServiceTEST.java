import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@ActiveProfiles("test")
public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    @MockBean
    private DatabaseRepository databaseRepository;

    @MockBean
    private ConnectionService connectionService;

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    @MockBean
    private ScheduleRepository scheduleRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    // Test getOscDatabaseInfo method
    @Test
    public void testGetOscDatabaseInfoWithExistingDatabase() {
        Long databaseId = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(databaseId);
        when(databaseRepository.findById(databaseId)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo expectedOscDatabaseInfo = new OscLockDatabaseUserInfo();
        expectedOscDatabaseInfo.setDatabaseId(databaseId);
        expectedOscDatabaseInfo.setLockDatabaseUserRequired(true);

        OscLockDatabaseUserInfo actualOscDatabaseInfo = oscService.getOscDatabaseInfo(databaseId);
        assertEquals(expectedOscDatabaseInfo, actualOscDatabaseInfo);
    }

    @Test
    public void testGetOscDatabaseInfoWithNonExistentDatabase() {
        Long databaseId = 1L;
        when(databaseRepository.findById(databaseId)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo actualOscDatabaseInfo = oscService.getOscDatabaseInfo(databaseId);
        assertNull(actualOscDatabaseInfo);
    }

    // Test swapTable method
    @Test
    public void testSwapTableWithValidScheduleTask() {
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobParametersJson("{}");
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setScheduleTaskId(scheduleTaskId);
        scheduleTaskEntity.setResultJson("{}");
        scheduleTaskEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        oscService.swapTable(scheduleTaskId);

        ScheduleTaskEntity updatedScheduleTaskEntity = scheduleTaskRepository.findById(scheduleTaskId).get();
        assertTrue(updatedScheduleTaskEntity.isSwapTableStarted());
        assertFalse(updatedScheduleTaskEntity.isManualSwapTableEnabled());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTableWithInvalidScheduleTask() {
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobParametersJson("{}");
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setScheduleTaskId(scheduleTaskId);
        scheduleTaskEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());

        oscService.swapTable(scheduleTaskId);

        assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(scheduleTaskId));
    }

    @Test(expected = IllegalStateException.class)
    public void testSwapTableWithInvalidJobType() {
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobParametersJson("{}");
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setScheduleTaskId(scheduleTaskId);
        scheduleTaskEntity.setResultJson("{}");
        scheduleTaskEntity.setJobType(JobType.BACKUP);
        when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        oscService.swapTable(scheduleTaskId);

        assertThrows(IllegalStateException.class, () -> oscService.swapTable(scheduleTaskId));
    }

    @Test(expected = IllegalStateException.class)
    public void testSwapTableWithInvalidSwapTableType() {
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobParametersJson("{\"swapTableType\":\"INVALID\"}");
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setScheduleTaskId(scheduleTaskId);
        scheduleTaskEntity.setResultJson("{}");
        scheduleTaskEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        oscService.swapTable(scheduleTaskId);

        assertThrows(IllegalStateException.class, () -> oscService.swapTable(scheduleTaskId));
    }

    @Test(expected = IllegalStateException.class)
    public void testSwapTableWithDisabledManualSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobParametersJson("{\"swapTableType\":\"MANUAL\",\"manualSwapTableEnabled\":false}");
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setScheduleTaskId(scheduleTaskId);
        scheduleTaskEntity.setResultJson("{}");
        scheduleTaskEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        oscService.swapTable(scheduleTaskId);

        assertThrows(IllegalStateException.class, () -> oscService.swapTable(scheduleTaskId));
    }

    // Test getLockUserIsRequired method
    @Test
    public void testGetLockUserIsRequiredWithConnectionThatRequiresLockUser() {
        Long connectionId = 1L;
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("OBDC");
        when(() -> OscDBUserUtil.isLockUserRequired(connectionConfig.getDialectType(), () -> {
            ConnectionSessionFactory factory = new DefaultConnectSessionFactory(connectionConfig);
            String version = null;
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
            return version;
        })).thenReturn(true);

        boolean result = oscService.getLockUserIsRequired(connectionId);
        assertTrue(result);
    }

    @Test
    public void testGetLockUserIsRequiredWithConnectionThatDoesNotRequireLockUser() {
        Long connectionId = 1L;
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");
        when(() -> OscDBUserUtil.isLockUserRequired(connectionConfig.getDialectType(), () -> {
            ConnectionSessionFactory factory = new DefaultConnectSessionFactory(connectionConfig);
            String version = null;
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
            return version;
        })).thenReturn(false);

        boolean result = oscService.getLockUserIsRequired(connectionId);
        assertFalse(result);
    }
}