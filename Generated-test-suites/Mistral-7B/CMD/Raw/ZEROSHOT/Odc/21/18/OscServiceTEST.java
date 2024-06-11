import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.JobType;
import com.oceanbase.odc.metadb.schedule.OnlineSchemaChangeParameters;
import com.oceanbase.odc.metadb.schedule.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfigBuilder;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

/**
 * Test class for OscService
 *
 * @author yaobin
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
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

    private ConnectionConfig connectionConfig;
    private DatabaseEntity databaseEntity;
    private ScheduleEntity scheduleEntity;
    private ScheduleTaskEntity scheduleTaskEntity;
    private ScheduleTaskEntity savedScheduleTaskEntity;
    private OnlineSchemaChangeParameters oscParameters;
    private OnlineSchemaChangeScheduleTaskResult result;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        connectionConfig = ConnectionConfigBuilder.newBuilder()
                .withDialectType("mock")
                .withConnectionUrl("mock")
                .build();

        databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        databaseEntity.setConnectionId(1L);

        scheduleEntity = new ScheduleEntity();
        scheduleEntity.setDatabaseId(1L);
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));

        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setScheduleTaskId(1L);
        scheduleTaskEntity.setScheduleId(scheduleEntity.getDatabaseId());
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

        savedScheduleTaskEntity = new ScheduleTaskEntity();
        savedScheduleTaskEntity.setScheduleTaskId(1L);
        savedScheduleTaskEntity.setScheduleId(scheduleEntity.getDatabaseId());
        savedScheduleTaskEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

        oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        result = new OnlineSchemaChangeScheduleTaskResult();
    }

    @Test
    public void testGetOscDatabaseInfo() {
        when(databaseRepository.findById(databaseEntity.getDatabaseId())).thenReturn(Optional.of(databaseEntity));
        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(databaseEntity.getDatabaseId());
        assertEquals(databaseEntity.getDatabaseId(), oscDatabaseInfo.getDatabaseId());
        assertFalse(oscDatabaseInfo.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_whenDatabaseNotExists() {
        when(databaseRepository.findById(databaseEntity.getDatabaseId())).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(databaseEntity.getDatabaseId());
        assertNull(oscDatabaseInfo);
    }

    @Test
    public void testSwapTable() {
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
        when(databaseRepository.findById(databaseEntity.getDatabaseId())).thenReturn(Optional.of(databaseEntity));
        when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        when(scheduleRepository.save(scheduleEntity)).thenReturn(scheduleEntity);
        when(connectionService.getForConnectionSkipPermissionCheck(databaseEntity.getConnectionId()))
                .thenReturn(connectionConfig);
        OscSwapTableVO oscSwapTableVO = oscService.swapTable(1L);
        assertEquals(1L, oscSwapTableVO.getScheduleTaskId());

        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(databaseEntity.getConnectionId());
        verify(databaseRepository, times(1)).findById(databaseEntity.getDatabaseId());
        verify(scheduleRepository, times(1)).findById(Long.parseLong(scheduleEntity.getJobName()));
        verify(scheduleRepository, times(1)).save(scheduleEntity);
        verifyNoMoreInteractions(databaseRepository);
        verifyNoMoreInteractions(connectionService);
        verifyNoMoreInteractions(scheduleRepository);
    }

    @Test(expected = IllegalStateException.class)
    public void testSwapTable_whenScheduleTaskNotFound() {
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());
        oscService.swapTable(1L);
    }

    @Test(expected = IllegalStateException.class)
    public void testSwapTable_whenDatabaseNotFound() {
        when(databaseRepository.findById(databaseEntity.getDatabaseId())).thenReturn(Optional.empty());
        oscService.swapTable(1L);
    }

    @Test
    public void testSwapTable_whenJobTypeMismatch() {
        scheduleEntity.setJobType(JobType.BACKUP);
        when(databaseRepository.findById(databaseEntity.getDatabaseId())).thenReturn(Optional.of(databaseEntity));
        when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        oscService.swapTable(1L);
    }

    @Test
    public void testSwapTable_whenSwapTableTypeMismatch() {
        oscParameters.setSwapTableType(SwapTableType.AUTOMATIC);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));
        when(databaseRepository.findById(databaseEntity.getDatabaseId())).thenReturn(Optional.of(databaseEntity));
        when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        oscService.swapTable(1L);
    }

    @Test
    public void testSwapTable_whenManualSwapTableDisabled() {
        result.setManualSwapTableEnabled(false);
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(result));
        when(databaseRepository.findById(databaseEntity.getDatabaseId())).thenReturn(Optional.of(databaseEntity));
        when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
        oscService.swapTable(1L);
    }

    @Test
    public void testGetLockUserIsRequired() {
        assertTrue(oscService.getLockUserIsRequired(1L));
        ConnectionSessionFactory factory = mock(ConnectionSessionFactory.class);
        ConnectionSession connSession = mock(ConnectionSession.class);
        String version = "mockVersion";
        when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(connectionConfig);
        when(factory.generateSession()).thenReturn(connSession);
        when(ConnectionSessionUtil.getVersion(connSession)).thenReturn(version);
        assertTrue(OscDBUserUtil.isLockUserRequired(connectionConfig.getDialectType(), () -> {
            factory.generateSession();
            return version;
        }));
    }

    @Test
    public void testGetLockUserIsRequired_whenConnectionFailed() {
        when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenThrow(new RuntimeException());
        assertFalse(oscService.getLockUserIsRequired(1L));
    }
}