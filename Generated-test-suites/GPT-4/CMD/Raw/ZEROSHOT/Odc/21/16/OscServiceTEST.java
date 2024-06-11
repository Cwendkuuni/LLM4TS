
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

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
    
    @Test
    public void testGetOscDatabaseInfo() {
        Long databaseId = 1L;
        when(databaseRepository.findById(databaseId)).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(databaseId);
        Assert.assertNotNull(result);
    }
    
    @Test(expected = Exception.class)
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(new ScheduleEntity()));
        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .thenReturn(new ConnectionConfig());
        Assert.assertFalse(oscService.getLockUserIsRequired(connectionId));
    }
}
