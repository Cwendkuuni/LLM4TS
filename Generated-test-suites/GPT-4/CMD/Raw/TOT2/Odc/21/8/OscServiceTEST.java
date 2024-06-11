
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import org.junit.*;
import org.mockito.*;

public class OscServiceTest {

    private OscService oscService;
    @Mock
    private DatabaseRepository databaseRepository;
    @Mock
    private ConnectionService connectionService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        oscService = new OscService(databaseRepository, connectionService);
    }

    @Test
    public void getOscDatabaseInfo_TestWithValidId() {
        // Arrange
        DatabaseEntity testEntity = new DatabaseEntity();
        testEntity.setDatabaseId(1L);
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(testEntity));

        // Act
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getDatabaseId());
    }

    @Test
    public void getOscDatabaseInfo_NotFoundTest() {
        // Arrange
        when(databaseRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);

        // Assert
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void swapTable_TestWithInvalidJobType() {
        // Arrange
        ScheduleTaskEntity testTaskEntity = new ScheduleTaskEntity();
        ScheduleEntity testEntity = new ScheduleEntity();
        testEntity.setJobType(JobType.EXAMPLE_JOB);

        // Act
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(testEntity));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(testEntity));
        oscService.swapTable(1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void swapTable_TestWithInvalidSwapTableType() {
        // Arrange
        ScheduleTaskEntity testTaskEntity = new ScheduleTaskEntity();
        ScheduleEntity testEntity = new ScheduleEntity();

        // Act
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(testEntity));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(testEntity));
        oscService.swapTable(1L);
    }

    @Test
    public void swapTable_ValidTest() {
        // Arrange
        ScheduleTaskEntity testTaskEntity = new ScheduleTaskEntity();
        ScheduleEntity testEntity = new ScheduleEntity();

        // Act
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(testEntity));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(testEntity));
        OscSwapTableVO result = oscService.swapTable(1L);

        // Assert
        assertNotNull(result);
    }

}
