
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import com.oceanbase.odc.service.onlineschemachange.OscService;

public class OscServiceTest {

    OscService oscService;

    @Mock
    DatabaseRepository databaseRepository;

    @Mock
    ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    ScheduleRepository scheduleRepository;

    @BeforeEach
    public void setUp() {
        oscService = new OscService();
    }

    @Test
    public void testGetOscDatabaseInfo() {
        // Test when database is present
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(new DatabaseEntity()));
        assertNotNull(oscService.getOscDatabaseInfo(1L));

        // Test when database is not present
        when(databaseRepository.findById(2L)).thenReturn(Optional.empty());
        assertNotNull(oscService.getOscDatabaseInfo(2L));
    }

    @Test
    public void testSwapTable() {
        // Test when schedule task and schedule are present
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(new ScheduleEntity()));
        assertNotNull(oscService.swapTable(1L));

        // Test when schedule task or schedule is not present
        when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.empty());
        when(scheduleRepository.findById(2L)).thenReturn(Optional.of(new ScheduleEntity()));
        assertNotNull(oscService.swapTable(2L));

        when(scheduleTaskRepository.findById(3L)).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleRepository.findById(3L)).thenReturn(Optional.empty());
        assertNotNull(oscService.swapTable(3L));
    }

    @Test
    public void testGetLockUserIsRequired() {
        assertNotNull(oscService.getLockUserIsRequired(1L));
    }

}
