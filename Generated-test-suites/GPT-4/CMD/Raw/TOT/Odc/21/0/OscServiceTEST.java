import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;       

    @Mock
    private ConnectionService connectionService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    private OscService oscService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // injecting mock objects into oscService
        oscService = new OscService(databaseRepository, connectionService, scheduleTaskRepository, scheduleRepository);
    }

    @Test
    public void testSwapTable() {
        // Setting up the mock behavior
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(new ScheduleEntity()));

        // Call the method to test
        OscSwapTableVO result = oscService.swapTable(1L);

        // Assertions to check the result
        assertNotNull(result);
    }
}