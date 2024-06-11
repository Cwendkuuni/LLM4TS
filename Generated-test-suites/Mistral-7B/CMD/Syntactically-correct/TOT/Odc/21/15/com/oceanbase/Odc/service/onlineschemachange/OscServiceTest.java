import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.OscDBUserUtil;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Optional;

@SuppressWarnings("unchecked")
public class OscServiceTest {

    @MockBean
    private ConnectionService connectionService;

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    @MockBean
    private DatabaseRepository databaseRepository;

    @MockBean
    private ScheduleRepository scheduleRepository;

    @MockBean
    private OscDBUserUtil oscDBUserUtil;

    @InjectMocks
    private OscService oscService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setFieldValue(oscService, "oscDBUserUtil", oscDBUserUtil);
    }

    @Test
    public void testGetOscDatabaseInfoWithValidId_thenReturnsOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        databaseEntity.setConnectionId(1L);

        Optional<DatabaseEntity> databaseOptional = Optional.of(databaseEntity);
        given(databaseRepository.findById(id)).willReturn(databaseOptional);

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        verify(databaseRepository).findById(id);
        assertEquals(id, result.getDatabaseId());
        assertTrue(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfoWithInvalidId_thenReturnsEmptyOscDatabaseInfo() {
        Long id = 1L;
        given(databaseRepository.findById(id)).willReturn(Optional.empty());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertNull(result);
    }

    @Test
    public void testSwapTableWithValidParameters_thenReturnsSwapTableVO() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName("1");
        scheduleTask.setResultJson("{}");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        scheduleEntity.setJobParametersJson("{\"swapTableType\":\"MANUAL\"}");

        Optional<ScheduleEntity> scheduleEntityOptional = Optional.of(scheduleEntity);
        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(scheduleTask);

        given(scheduleRepository.findById(1L)).willReturn(scheduleEntityOptional);
        given(scheduleTaskRepository.findById(scheduleTaskId)).willReturn(scheduleTaskOptional);

        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);

        verify(scheduleRepository).findById(1L);
        verify(scheduleTaskRepository).findById(scheduleTaskId);
        assertEquals(scheduleTaskId, result.getScheduleTaskId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTableWithInvalidScheduleTaskId_thenThrowsIllegalArgumentException() {
        Long scheduleTaskId = -1L;
        given(scheduleTaskRepository.findById(scheduleTaskId)).willReturn(Optional.empty());

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testSwapTableWithInvalidJobType_thenReturnsError() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName("1");
        scheduleTask.setResultJson("{\"manualSwapTableEnabled\":true,\"manualSwapTableStarted\":false}");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.BACKUP);
        scheduleEntity.setJobParametersJson("{\"swapTableType\":\"MANUAL\"}");

        Optional<ScheduleEntity> scheduleEntityOptional = Optional.of(scheduleEntity);
        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(scheduleTask);

        given(scheduleRepository.findById(1L)).willReturn(scheduleEntityOptional);
        given(scheduleTaskRepository.findById(scheduleTaskId)).willReturn(scheduleTaskOptional);

        oscService.swapTable(scheduleTaskId);

        verify(scheduleTaskRepository).findById(scheduleTaskId);
        verify(scheduleTaskRepository).save(scheduleTask);

        ScheduleTaskEntity updatedScheduleTask = scheduleTaskOptional.get();
        String updatedResultJson = updatedScheduleTask.getResultJson();
        assertEquals("{\"errorMessage\":\"Task type is not ONLINE_SCHEMA_CHANGE.\"}", updatedResultJson);
    }

    @Test
    public void testSwapTableWithInvalidSwapTableType_thenReturnsError() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName("1");
        scheduleTask.setResultJson("{\"manualSwapTableEnabled\":true,\"manualSwapTableStarted\":false}");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        scheduleEntity.setJobParametersJson("{\"swapTableType\":\"INVALID\"}");

        Optional<ScheduleEntity> scheduleEntityOptional = Optional.of(scheduleEntity);
        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(scheduleTask);

        given(scheduleRepository.findById(1L)).willReturn(scheduleEntityOptional);
        given(scheduleTaskRepository.findById(scheduleTaskId)).willReturn(scheduleTaskOptional);

        oscService.swapTable(scheduleTaskId);

        verify(scheduleTaskRepository).findById(scheduleTaskId);
        verify(scheduleTaskRepository).save(scheduleTask);

        ScheduleTaskEntity updatedScheduleTask = scheduleTaskOptional.get();
        String updatedResultJson = updatedScheduleTask.getResultJson();
        assertEquals("{\"errorMessage\":\"Swap table type is not MANUAL.\"}", updatedResultJson);
    }

    @Test
    public void testSwapTableWithValidSwapTable_thenReturnsEmptySwapTableVO() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName("1");
        scheduleTask.setResultJson("{\"manualSwapTableEnabled\":true,\"manualSwapTableStarted\":false}");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        scheduleEntity.setJobParametersJson("{\"swapTableType\":\"MANUAL\"}");

        Optional<ScheduleEntity> scheduleEntityOptional = Optional.of(scheduleEntity);
        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(scheduleTask);

        given(scheduleRepository.findById(1L)).willReturn(scheduleEntityOptional);
        given(scheduleTaskRepository.findById(scheduleTaskId)).willReturn(scheduleTaskOptional);

        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);

        verify(scheduleTaskRepository).findById(scheduleTaskId);
        verify(scheduleTaskRepository).save(scheduleTask);

        assertEquals(Collections.emptyList(), result.getSwapTableInfos());
    }

    @Test
    public void testGetLockUserIsRequiredWithValidConnectionId_thenReturnsTrue() {
        Long connectionId = 1L;

        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("OBServer");
        decryptedConnConfig.setUsername("user");
        decryptedConnConfig.setPassword("password");
        decryptedConnConfig.setPort(1000);

        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
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

        boolean expected = OscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType(), () -> {
            ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
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

            return version != null;
        });

        assertTrue(oscService.getLockUserIsRequired(connectionId));
    }

    @Test
    public void testGetLockUserIsRequiredWithInvalidConnectionId_thenReturnsFalse() {
        Long connectionId = -1L;

        assertFalse(oscService.getLockUserIsRequired(connectionId));
    }
}