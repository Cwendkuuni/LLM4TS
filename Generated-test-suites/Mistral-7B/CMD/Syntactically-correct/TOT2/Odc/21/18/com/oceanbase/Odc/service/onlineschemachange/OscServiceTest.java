import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import com.oceanbase.odc.service.testbase.BaseTest;
import com.oceanbase.odc.service.testbase.TestDataFactory;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class OscServiceTest extends BaseTest {

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

    private OngoingStubbing<DatabaseEntity> databaseEntityStub;

    private OngoingStubbing<ConnectionConfig> connectionConfigStub;

    private OngoingStubbing<ScheduleEntity> scheduleEntityStub;

    private OngoingStubbing<ScheduleTaskEntity> scheduleTaskEntityStub;

    private OscSwapTableVO oscSwapTableVO;

    private Long scheduleTaskId = 1L;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        oscSwapTableVO = new OscSwapTableVO();
        oscSwapTableVO.setScheduleTaskId(scheduleTaskId);
        databaseEntityStub = mock(DatabaseEntity.class);
        connectionConfigStub = mock(ConnectionConfig.class);
        scheduleEntityStub = mock(ScheduleEntity.class);
        scheduleTaskEntityStub = mock(ScheduleTaskEntity.class);
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(databaseEntityStub));
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(connectionConfigStub);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntityStub));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntityStub));
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long databaseId = 1L;
        when(databaseEntityStub.getDatabaseId()).thenReturn(databaseId);
        when(getLockUserIsRequired(databaseId)).thenReturn(true);
        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(databaseId);
        assertEquals(databaseId, oscDatabaseInfo.getDatabaseId());
        assertTrue(oscDatabaseInfo.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_DatabaseNotPresent() {
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(1L);
        assertNull(oscDatabaseInfo);
    }

    @Test
    public void testSwapTable() throws Exception {
        Long scheduleId = 1L;
        OnlineSchemaChangeParameters oscParameters = TestDataFactory.getOnlineSchemaChangeParameters();
        OnlineSchemaChangeScheduleTaskResult result = TestDataFactory.getOnlineSchemaChangeScheduleTaskResult();
        when(scheduleEntityStub.getJobName()).thenReturn(scheduleId.toString());
        when(scheduleEntityStub.getJobParametersJson()).thenReturn(JsonUtils.toJson(oscParameters));
        when(scheduleEntityStub.getJobType()).thenReturn(JobType.ONLINE_SCHEMA_CHANGE);
        when(scheduleTaskEntityStub.getResultJson()).thenReturn(JsonUtils.toJson(result));
        when(databaseEntityStub.getConnectionId()).thenReturn(1L);
        oscService.swapTable(scheduleTaskId);
        verify(scheduleTaskRepository).updateTaskResult(scheduleTaskId, eq(JsonUtils.toJson(result)));
        verify(databaseRepository).findById(anyLong());
        verifyZeroInteractions(connectionService, connectionConfigStub, scheduleRepository);
    }

    @Test(expected = RuntimeException.class)
    public void testSwapTable_ScheduleTaskNotPresent() {
        oscService.swapTable(scheduleTaskId + 1);
    }

    @Test(expected = RuntimeException.class)
    public void testSwapTable_ScheduleNotOnlineSchemaChange() {
        when(scheduleEntityStub.getJobType()).thenReturn(JobType.DATA_MIGRATION);
        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testSwapTable_ManualSwapTableNotEnabled() {
        OnlineSchemaChangeScheduleTaskResult result = TestDataFactory.getOnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);
        when(scheduleTaskEntityStub.getResultJson()).thenReturn(JsonUtils.toJson(result));
        when(databaseEntityStub.getConnectionId()).thenReturn(1L);
        oscService.swapTable(scheduleTaskId);
        verify(scheduleTaskRepository, never()).updateTaskResult(scheduleTaskId, anyString());
        verify(databaseRepository, never()).findById(anyLong());
    }

    @Test
    public void testSwapTable_ManualSwapTableAlreadyEnabled() {
        OnlineSchemaChangeScheduleTaskResult result = TestDataFactory.getOnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        when(scheduleTaskEntityStub.getResultJson()).thenReturn(JsonUtils.toJson(result));
        when(databaseEntityStub.getConnectionId()).thenReturn(1L);
        oscService.swapTable(scheduleTaskId);
        verify(scheduleTaskRepository, never()).updateTaskResult(scheduleTaskId, anyString());
        verify(databaseRepository, never()).findById(anyLong());
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        ConnectionConfig connectionConfig = TestDataFactory.getConnectionConfig();
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(connectionConfig);
        when(DefaultConnectSessionFactory.generateSession()).thenThrow(new RuntimeException());
        assertTrue(oscService.getLockUserIsRequired(connectionId));
    }

    @Test
    public void testGetLockUserIsRequired_DbVersionRetrievalSuccess() {
        Long connectionId = 1L;
        ConnectionConfig connectionConfig = TestDataFactory.getConnectionConfig();
        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(connectionConfig);
        ConnectionSession connSession = factory.generateSession();
        String version = "5.0.0";
        when(DefaultConnectSessionFactory.generateSession()).thenReturn(connSession);
        when(ConnectionSessionUtil.getVersion(connSession)).thenReturn(version);
        assertFalse(oscService.getLockUserIsRequired(connectionId));
    }

}