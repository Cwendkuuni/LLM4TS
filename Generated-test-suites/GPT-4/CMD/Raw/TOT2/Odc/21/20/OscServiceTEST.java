
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

public class OscServiceTest {

  @InjectMocks
  OscService oscService;

  @Mock
  DatabaseRepository databaseRepository;

  @Mock
  ConnectionService connectionService;

  @Mock
  ScheduleTaskRepository scheduleTaskRepository;

  @Mock
  ScheduleRepository scheduleRepository;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testGetOscDatabaseInfo() {
    Long id = 1L;
    OscLockDatabaseUserInfo expectedOscDatabase = new OscLockDatabaseUserInfo();
    when(databaseRepository.findById(id)).thenReturn(Optional.of(new DatabaseEntity()));
    OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);
    assertEquals(expectedOscDatabase, oscDatabase);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSwapTableInvalidScheduleTask() {
    Long scheduleTaskId = 1L;
    when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());
    OscSwapTableVO swapTable = oscService.swapTable(scheduleTaskId);
  }

  @Test
  public void testSwapTableValidScheduleTask() {
    Long scheduleTaskId = 1L;
    OscSwapTableVO expectedOscSwapTable = new OscSwapTableVO();
    expectedOscSwapTable.setScheduleTaskId(scheduleTaskId);

    OnlineSchemaChangeScheduleTaskResult taskResult = new OnlineSchemaChangeScheduleTaskResult();
    taskResult.setManualSwapTableEnabled(true);

    ScheduleEntity schedule = new ScheduleEntity();
    schedule.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

    ScheduleTaskEntity task = new ScheduleTaskEntity();
    task.setJobName("1");
    task.setResultJson(JsonUtils.toJson(taskResult));

    when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(task));
    when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
    OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);
    assertEquals(expectedOscSwapTable, oscSwapTable);
  }
}
