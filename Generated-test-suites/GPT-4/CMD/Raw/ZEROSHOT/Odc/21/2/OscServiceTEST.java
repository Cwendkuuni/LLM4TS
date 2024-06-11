
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

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
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    // Test Get Osc Database Info
    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 123L;
        oscService.getOscDatabaseInfo(id);
        verify(databaseRepository, times(1)).findById(id);
    }

    // Test Swap Table 
    @Test
    public void testSwapTable() {
        Long id = 123L;
        oscService.swapTable(id);
        verify(scheduleTaskRepository, times(1)).findById(id);
    }

}
