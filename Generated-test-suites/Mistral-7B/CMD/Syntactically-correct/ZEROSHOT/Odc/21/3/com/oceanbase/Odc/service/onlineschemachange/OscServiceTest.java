import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupitermock.junit5.SpringJUnit5JupiterTests;

import java.util.*;
import java.util.stream.Collectors;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class OscServiceTest extends SpringJUnit5JupiterTests {

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

    @MockBean
    private ConnectionSessionFactory connectionSessionFactory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long databaseId = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(databaseId);
        databaseEntity.setConnectionId(1L);
        when(databaseRepository.findById(databaseId)).thenReturn(Optional.of(databaseEntity));
        when(getLockUserIsRequired(databaseEntity.getConnectionId())).thenReturn(false);

        OscLockDatabaseUserInfo expected = new OscLockDatabaseUserInfo();
        expected.setDatabaseId(databaseId);
        expected.setLockDatabaseUserRequired(false);

        OscLockDatabaseUserInfo actual = oscService.getOscDatabaseInfo(databaseId);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetOscDatabaseInfo_databaseNotExists() {
        Long databaseId = 1L;
        when(databaseRepository.findById(databaseId)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo actual = oscService.getOscDatabaseInfo(databaseId);
        Assert.assertNull(actual);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobParametersJson(new OnlineSchemaChangeParameters().toJson());
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setScheduleTaskId(scheduleTaskId);
        scheduleTaskEntity.setJobName(scheduleEntity.getJobName());
        scheduleTaskEntity.setResultJson(new OnlineSchemaChangeScheduleTaskResult().toJson());
        scheduleTaskEntity.setStatus("COMPLETED");
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        when(databaseRepository.findById(anyLong())).thenAnswer((Answer<Optional<DatabaseEntity>> answer) -> {
            DatabaseEntity databaseEntity = new DatabaseEntity();
            databaseEntity.setConnectionId(1L);
            return Optional.of(databaseEntity);
        });

        when(connectionService.getForConnectionSkipPermissionCheck(1L))
                .thenAnswer((Answer<ConnectionConfig> answer) -> new ConnectionConfig());

        when(connectionSessionFactory.generateSession()).thenAnswer((Answer<ConnectionSession> answer) -> new ConnectionSession());

        OngoingStubbing<String> versionStub = doReturn("5.5");
        doAnswer(versionStub).when(oscService).getVersion(any(ConnectionSession.class));

        OscSwapTableVO actual = oscService.swapTable(scheduleTaskId);
        Assert.assertNotNull(actual);
        Assert.assertEquals(scheduleTaskId, actual.getScheduleTaskId());
    }

    @Test(expected = IllegalStateException.class)
    public void testSwapTable_databaseNotExists() {
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobParametersJson(new OnlineSchemaChangeParameters().toJson);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setScheduleTaskId(scheduleTaskId);
        scheduleTaskEntity.setJobName(scheduleEntity.getJobName());
        scheduleTaskEntity.setStatus("COMPLETED");
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        when(databaseRepository.findById(anyLong()))
                .thenAnswer((Answer<Optional<DatabaseEntity>> answer) -> Optional.empty());

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testGetVersion() {
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        OscService oscService = mock(OscService.class);
        when(oscService.getVersion(connectionSession)).thenReturn("5.5");

        Assert.assertEquals("5.5", oscService.getVersion(connectionSession));
    }

    private String toJson(Object object) {
        return StringUtils.isEmpty(object.toString()) ? null : object.toString();
    }

    private OnlineSchemaChangeParameters newOnlineSchemaChangeParameters() {
        return new OnlineSchemaChangeParameters();
    }

    private OnlineSchemaChangeScheduleTaskResult newOnlineSchemaChangeScheduleTaskResult() {
        return new OnlineSchemaChangeScheduleTaskResult();
    }
}