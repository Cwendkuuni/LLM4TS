import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Optional;

@SuppressWarnings("unchecked")
public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    private DatabaseRepository databaseRepository;
    private ScheduleRepository scheduleRepository;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ConnectionService connectionService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        databaseRepository = mock(DatabaseRepository.class);
        scheduleRepository = mock(ScheduleRepository.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        connectionService = mock(ConnectionService.class);
        oscService.setDatabaseRepository(databaseRepository);
        oscService.setScheduleRepository(scheduleRepository);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
        oscService.setConnectionService(connectionService);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;

        given(databaseRepository.findById(id))
                .willReturn(Optional.of(new DatabaseEntity()));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        verify(databaseRepository).findById(id);
        then(result).isNotNull();
    }

    @Test
    public void testGetOscDatabaseInfo_DatabaseNotFound() {
        Long id = 1L;

        given(databaseRepository.findById(id))
                .willReturn(Optional.empty());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        verify(databaseRepository).findById(id);
        then(result).isNull();
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;

        given(scheduleTaskRepository.findById(scheduleTaskId))
                .willReturn(Optional.of(new ScheduleTaskEntity()));
        given(scheduleRepository.findById(Long.parseLong(scheduleTaskRepository.findById(scheduleTaskId)
                .get().getJobName())))
                .willReturn(Optional.of(new ScheduleEntity()));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        given(scheduleEntity.getJobParametersJson())
                .willReturn(JsonUtils.toJson(oscParameters));

        given(scheduleTaskRepository.findById(scheduleTaskId))
                .willAnswer((OngoingStubbing<Optional<ScheduleTaskEntity>> stubbing) -> {
                    ScheduleTaskEntity scheduleTask = stubbing.getArgument(0);
                    scheduleTask.setResultJson("{}");
                    return Optional.of(scheduleTask);
                });

        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);
        verify(scheduleTaskRepository).findById(scheduleTaskId);
        verify(scheduleRepository).findById(Long.parseLong(scheduleTaskRepository.findById(scheduleTaskId)
                .get().getJobName()));
        then(result).isNotNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_InvalidScheduleTask() {
        Long scheduleTaskId = 1L;

        given(scheduleTaskRepository.findById(scheduleTaskId))
                .willReturn(Optional.of(new ScheduleTaskEntity()));
        given(scheduleRepository.findById(Long.parseLong(scheduleTaskRepository.findById(scheduleTaskId)
                .get().getJobName())))
                .willReturn(Optional.empty());

        oscService.swapTable(scheduleTaskId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_InvalidJobType() {
        Long scheduleTaskId = 1L;

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.SCHEDULE_TEST);
        given(scheduleRepository.findById(Long.parseLong(scheduleTaskRepository.findById(scheduleTaskId)
                .get().getJobName())))
                .willReturn(Optional.of(scheduleEntity));

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;

        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("test");

        given(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .willReturn(decryptedConnConfig);

        boolean result = oscService.getLockUserIsRequired(connectionId);
        verify(connectionService).getForConnectionSkipPermissionCheck(connectionId);
        then(result).isTrue();
    }

    @Test
    public void testGetLockUserIsRequired_NoLockUserRequired() {
        Long connectionId = 1L;

        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("test");

        given(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .willReturn(decryptedConnConfig);

        given(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .willReturn(decryptedConnConfig);

        boolean result = oscService.getLockUserIsRequired(connectionId);
        verify(connectionService).getForConnectionSkipPermissionCheck(connectionId);
        verify(connectionService).getForConnectionSkipPermissionCheck(connectionId);
        then(result).isFalse();
    }
}