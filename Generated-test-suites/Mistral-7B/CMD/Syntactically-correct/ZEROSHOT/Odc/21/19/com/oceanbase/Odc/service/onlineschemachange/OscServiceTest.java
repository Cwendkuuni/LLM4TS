import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class OscServiceTest {

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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void teardown() {
        // Add any teardown code here
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        given(databaseRepository.findById(id)).willReturn(Optional.of(new DatabaseEntity()));
        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(id);
        // Add assertions here
        verify(databaseRepository).findById(id);
    }

    @Test
    public void testGetOscDatabaseInfoWhenDatabaseNotPresent() {
        Long id = 1L;
        given(databaseRepository.findById(id)).willReturn(Optional.empty());
        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(id);
        // Add assertions here
        verify(databaseRepository).findById(id);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        given(scheduleTaskRepository.findById(scheduleTaskId)).willReturn(Optional.of(scheduleTask));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        given(scheduleRepository.findById(1L)).willReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(false);

        given(scheduleTask.getResultJson()).willReturn(JsonUtils.toJson(result));

        oscService.swapTable(scheduleTaskId);

        // Add assertions here
        verify(scheduleTaskRepository).findById(scheduleTaskId);
        verify(scheduleRepository).findById(1L);
        verify(scheduleTaskRepository).updateTaskResult(scheduleTaskId, JsonUtils.toJson(result));
    }

    @Test(expected = RuntimeException.class)
    public void testSwapTableWhenScheduleTypeNotOnlineSchemaChange() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        given(scheduleTaskRepository.findById(scheduleTaskId)).willReturn(Optional.of(scheduleTask));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.TEST);
        given(scheduleRepository.findById(1L)).willReturn(Optional.of(scheduleEntity));

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        ConnectionConfig connectionConfig = new ConnectionConfig();
        given(connectionService.getForConnectionSkipPermissionCheck(connectionId)).willReturn(connectionConfig);
        boolean isLockUserRequired = oscService.getLockUserIsRequired(connectionId);
        // Add assertions here
        verify(connectionService).getForConnectionSkipPermissionCheck(connectionId);
    }
}