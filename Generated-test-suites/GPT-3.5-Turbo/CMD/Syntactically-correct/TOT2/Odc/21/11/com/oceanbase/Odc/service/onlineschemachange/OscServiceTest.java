import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
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
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        OscLockDatabaseUserInfo expectedInfo = new OscLockDatabaseUserInfo();
        expectedInfo.setDatabaseId(1L);
        expectedInfo.setLockDatabaseUserRequired(true);

        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        databaseEntity.setConnectionId(1L);

        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo actualInfo = oscService.getOscDatabaseInfo(id);

        assertEquals(expectedInfo.getDatabaseId(), actualInfo.getDatabaseId());
        assertEquals(expectedInfo.isLockDatabaseUserRequired(), actualInfo.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(false);

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        String jobParametersJson = JsonUtils.toJson(oscParameters);
        String resultJson = JsonUtils.toJson(result);
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleEntity.getJobParametersJson()).thenReturn(jobParametersJson);
        when(scheduleTaskEntity.getResultJson()).thenReturn(resultJson);

        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);

        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
        assertEquals(true, result.isManualSwapTableStarted());
        assertEquals(false, result.isManualSwapTableEnabled());

    }

}