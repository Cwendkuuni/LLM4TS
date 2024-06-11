import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import com.oceanbase.odc.core.shared.PreConditions;
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
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

@SuppressWarnings("unchecked")
public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    private OscService oscService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(databaseRepository.findById(anyLong())).thenAnswer(new Answer<Optional<DatabaseEntity>>() {
            @Override
            public Optional<DatabaseEntity> answer(InvocationOnMock invocation) {
                Long id = (Long) invocation.getArguments()[0];
                if (id == 1L) {
                    DatabaseEntity databaseEntity = new DatabaseEntity();
                    databaseEntity.setConnectionId(1L);
                    return Optional.of(databaseEntity);
                }
                return Optional.empty();
            }
        });
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(new ConnectionConfig());
        when(scheduleRepository.findById(anyLong())).thenAnswer(new Answer<Optional<ScheduleEntity>>() {
            @Override
            public Optional<ScheduleEntity> answer(InvocationOnMock invocation) {
                Long id = (Long) invocation.getArguments()[0];
                if (id == 1L) {
                    ScheduleEntity scheduleEntity = new ScheduleEntity();
                    scheduleEntity.setId(1L);
                    scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
                    scheduleEntity.setJobName("1");
                    return Optional.of(scheduleEntity);
                }
                return Optional.empty();
            }
        });
        when(scheduleTaskRepository.findById(anyLong())).thenAnswer(new Answer<Optional<ScheduleTaskEntity>>() {
            @Override
            public Optional<ScheduleTaskEntity> answer(InvocationOnMock invocation) {
                Long id = (Long) invocation.getArguments()[0];
                if (id == 1L) {
                    ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
                    scheduleTask.setId(1L);
                    scheduleTask.setResultJson("{}");
                    return Optional.of(scheduleTask);
                }
                return Optional.empty();
            }
        });
    }

    @After
    public void tearDown() {
        reset(databaseRepository, connectionService, scheduleRepository, scheduleTaskRepository);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);
        assertTrue(oscDatabase.getDatabaseId() == id);
        assertFalse(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_DatabaseNotExist() {
        Long id = 2L;
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);
        assertEquals(oscDatabase, new OscLockDatabaseUserInfo());
    }

    @Test(expected = RuntimeException.class)
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testSwapTable_InvalidJobType() {
        Long scheduleTaskId = 1L;
        PreConditions.expectBadArgumentState(oscService, ErrorCodes.BadArgument,
                new Object[] {JobType.OFFLINE_DB_CONVERT}, "Task type is not " + TaskType.ONLINE_SCHEMA_CHANGE.name());
        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testSwapTable_InvalidSwapTableType() {
        Long scheduleTaskId = 1L;
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.SCHEDULED);
        PreConditions.expectBadArgumentState(oscService, ErrorCodes.BadArgument,
                new Object[] {SwapTableType.SCHEDULED}, "Swap table type is not " + SwapTableType.MANUAL.name());
        oscService.swapTable(scheduleTaskId, oscParameters);
    }

    @Test
    public void testSwapTable_DisabledManualSwapTable() {
        Long scheduleTaskId = 1L;
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setResultJson(JsonUtils.toJson(result));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        PreConditions.expectArgumentState(oscService, ErrorCodes.BadArgument,
                new Object[] {"Manual Swap table is not enabled"}, "Manual Swap table type is not enable ");
        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testSwapTable_Success() {
        Long scheduleTaskId = 1L;
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setResultJson(JsonUtils.toJson(result));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);
        assertEquals(oscSwapTable.getScheduleTaskId(), scheduleTaskId);
    }

}