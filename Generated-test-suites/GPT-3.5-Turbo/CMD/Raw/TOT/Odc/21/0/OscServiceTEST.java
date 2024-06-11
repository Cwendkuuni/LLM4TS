import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
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
        // Initialize any necessary mock data or configurations
    }

    @Test
    public void testGetOscDatabaseInfo() {
        // Test case for getting OscLockDatabaseUserInfo
        Long databaseId = 1L;
        OscLockDatabaseUserInfo expected = new OscLockDatabaseUserInfo();
        expected.setDatabaseId(databaseId);
        expected.setLockDatabaseUserRequired(true);

        when(databaseRepository.findById(databaseId)).thenReturn(Optional.of(new DatabaseEntity()));
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(databaseId);

        assertEquals(expected, result);
    }

    @Test
    public void testSwapTable() {
        // Test case for swapping tables
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");

        // Mock necessary data and method calls
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(new ScheduleEntity()));
        
        // Perform the swap table operation and validate the result
        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);

        assertNotNull(result);
        assertEquals(scheduleTaskId, result.getScheduleTaskId());
    }

}