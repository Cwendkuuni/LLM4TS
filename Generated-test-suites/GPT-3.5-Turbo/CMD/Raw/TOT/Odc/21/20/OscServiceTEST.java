import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private OscDBUserUtil oscDBUserUtil;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        OscService oscService = new OscService();
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertNotNull(result);
        assertEquals(result.getDatabaseId(), id);
    }

    @Test
    public void testSwapTable() {
        OscService oscService = new OscService();
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson("{}");

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(scheduleTaskEntity.getJobName())).thenReturn(Optional.of(scheduleEntity));
        when(oscDBUserUtil.isLockUserRequired()).thenReturn(true);

        OscSwapTableVO swapTableVO = oscService.swapTable(scheduleTaskId);

        verify(scheduleTaskRepository, times(1)).updateTaskResult(scheduleTaskId, JsonUtils.toJson(result));
        assertNotNull(swapTableVO);
        assertEquals(swapTableVO.getScheduleTaskId(), scheduleTaskId);
    }
}