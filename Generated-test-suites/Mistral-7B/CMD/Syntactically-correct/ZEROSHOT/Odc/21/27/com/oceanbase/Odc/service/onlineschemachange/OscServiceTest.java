import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleType;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfigDto;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

@SuppressWarnings("unchecked")
public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private ObjectMapper objectMapper;

    private OscLockDatabaseUserInfo expectedOscDatabaseInfo;
    private DatabaseEntity expectedDatabaseEntity;
    private ScheduleEntity expectedScheduleEntity;
    private ScheduleTaskEntity expectedScheduleTaskEntity;
    private ConnectionConfig expectedConnectionConfig;
    private ConnectionConfigDto expectedConnectionConfigDto;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        expectedDatabaseEntity = new DatabaseEntity();
        expectedDatabaseEntity.setDatabaseId(1L);
        expectedDatabaseEntity.setConnectionId(1L);

        expectedOscDatabaseInfo = new OscLockDatabaseUserInfo();
        expectedOscDatabaseInfo.setDatabaseId(1L);
        expectedOscDatabaseInfo.setLockDatabaseUserRequired(true);

        expectedScheduleEntity = new ScheduleEntity();
        expectedScheduleEntity.setId(1L);
        expectedScheduleEntity.setJobName("1");
        expectedScheduleEntity.setJobParametersJson("{}");
        expectedScheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        expectedConnectionConfig = new ConnectionConfig();
        expectedConnectionConfig.setDialectType("testDialectType");

        expectedConnectionConfigDto = new ConnectionConfigDto();
        expectedConnectionConfigDto.setDialectType("testDialectType");

        when(databaseRepository.findById(anyLong())).thenAnswer(new Answer<Optional<DatabaseEntity>>() {
            @Override
            public Optional<DatabaseEntity> answer(InvocationOnMock invocation) throws Throwable {
                return Optional.ofNullable(expectedDatabaseEntity);
            }
        });

        when(scheduleRepository.findById(anyLong())).thenAnswer(new Answer<Optional<ScheduleEntity>>() {
            @Override
            public Optional<ScheduleEntity> answer(InvocationOnMock invocation) throws Throwable {
                return Optional.ofNullable(expectedScheduleEntity);
            }
        });

        when(scheduleTaskRepository.findById(anyLong())).thenAnswer(new Answer<Optional<ScheduleTaskEntity>>() {
            @Override
            public Optional<ScheduleTaskEntity> answer(InvocationOnMock invocation) throws Throwable {
                return Optional.ofNullable(expectedScheduleTaskEntity);
            }
        });

        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(expectedConnectionConfig);

        when(objectMapper.readValue(expectedScheduleEntity.getJobParametersJson(), OnlineSchemaChangeParameters.class))
                .thenReturn(new OnlineSchemaChangeParameters());
        when(objectMapper.readValue(expectedScheduleEntity.getJobParametersJson(), OnlineSchemaChangeScheduleTaskResult.class))
                .thenReturn(new OnlineSchemaChangeScheduleTaskResult());
    }

    @Test
    public void testGetOscDatabaseInfo() {
        assertNull(oscService.getOscDatabaseInfo(null));
        assertSame(expectedOscDatabaseInfo, oscService.getOscDatabaseInfo(expectedDatabaseEntity.getId()));
    }

    @Test
    public void testSwapTable() {
        expectedScheduleTaskEntity = new ScheduleTaskEntity();
        expectedScheduleTaskEntity.setId(1L);
        expectedScheduleTaskEntity.setResultJson("{}");

        doReturn(true).when(oscService).getLockUserIsRequired(expectedConnectionConfig.getConnectionId());

        oscService.swapTable(1L);

        assertTrue(expectedScheduleTaskEntity.isManualSwapTableStarted());
        assertFalse(expectedScheduleTaskEntity.isManualSwapTableEnabled());
    }

    @Test(expected = IllegalStateException.class)
    public void testSwapTableWithLockUserNotRequired() {
        expectedDatabaseEntity.setConnectionId(2L);
        doReturn(false).when(oscService).getLockUserIsRequired(expectedDatabaseEntity.getConnectionId());

        oscService.swapTable(1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTableWithInvalidJobType() {
        expectedScheduleEntity.setJobType(JobType.BACKUP);

        oscService.swapTable(1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTableWithInvalidSwapTableType() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSwapTableType(SwapTableType.AUTOMATIC);

        expectedScheduleEntity.setJobParametersJson(JsonUtils.toJson(parameters));

        oscService.swapTable(1L);
    }

    @Test(expected = IllegalStateException.class)
    public void testSwapTableWithNotCompletedJob() {
        expectedScheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        expectedScheduleEntity.setJobStatus(ScheduleType.SCHEDULED);

        expectedScheduleTaskEntity.setId(1L);
        expectedScheduleTaskEntity.setResultJson("{}");

        oscService.swapTable(1L);
    }
}