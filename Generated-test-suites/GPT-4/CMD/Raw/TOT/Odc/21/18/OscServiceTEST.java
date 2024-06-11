
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OscServiceTest {

    private OscService oscService;
    private DatabaseRepository databaseRepository;
    private ConnectionService connectionService;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ScheduleRepository scheduleRepository;

    @Before
    public void setUp() {
        databaseRepository = mock(DatabaseRepository.class);
        connectionService = mock(ConnectionService.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        scheduleRepository = mock(ScheduleRepository.class);
        oscService = new OscService(databaseRepository, connectionService, scheduleTaskRepository, scheduleRepository);
    }

    @Test
    public void testGetOscDatabaseInfo_success() {
        Long id = 1L;
        DatabaseEntity entity = new DatabaseEntity();
        entity.setDatabaseId(id);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(entity));
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertNotNull(result);
        assertEquals(id, result.getDatabaseId());
    }

    @Test
    public void testGetOscDatabaseInfo_notFound() {
        Long id = 1L;
        when(databaseRepository.findById(id)).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertNotNull(result);
        assertNull(result.getDatabaseId());
    }

    @Test(expected = ScheduleTaskNotFoundException.class)
    public void testSwapTable_notFound() {
        Long id = 1L;
        when(scheduleTaskRepository.findById(id)).thenReturn(Optional.empty());
        oscService.swapTable(id);
    }

    // similar test cases for other scenarios
}
