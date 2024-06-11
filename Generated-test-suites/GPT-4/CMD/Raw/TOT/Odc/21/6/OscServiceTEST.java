import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class OscServiceTest {

  private OscService oscService;

  @Autowired
  private DatabaseRepository databaseRepository;
  @Autowired
  private ConnectionService connectionService;
  @Autowired
  private ScheduleTaskRepository scheduleTaskRepository;
  @Autowired
  private ScheduleRepository scheduleRepository;

  @Before
  public void setUp() {
    oscService = new OscService(databaseRepository, connectionService, scheduleTaskRepository, scheduleRepository);
  }

  @Test
  public void testGetOscDatabaseInfo() {
    Long testId = 1L;
    when(databaseRepository.findById(testId)).thenReturn(Optional.of(new DatabaseEntity()));
    when(getLockUserIsRequired(any())).thenReturn(Boolean.TRUE);
    OscLockDatabaseUserInfo userInfo = oscService.getOscDatabaseInfo(testId);
    assertEquals(testId, userInfo.getDatabaseId());
    assertEquals(Boolean.TRUE, userInfo.getLockDatabaseUserRequired());
  }

  // Additional test cases for complete coverage can be added here  
}