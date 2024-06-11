import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertNotNull(result);
        assertEquals(databaseEntity.getDatabaseId(), result.getDatabaseId());
        assertFalse(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        when(scheduleRepository.findById(Long.parseLong(scheduleTaskEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult scheduleTaskResult = new OnlineSchemaChangeScheduleTaskResult();
        scheduleTaskResult.setManualSwapTableEnabled(true);
        when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), OnlineSchemaChangeParameters.class)).thenReturn(oscParameters);
        when(JsonUtils.fromJson(scheduleTaskEntity.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class)).thenReturn(scheduleTaskResult);

        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);

        assertFalse(scheduleTaskResult.isManualSwapTableEnabled());
        assertTrue(scheduleTaskResult.isManualSwapTableStarted());
        assertEquals(scheduleTaskId, result.getScheduleTaskId());
    }
}