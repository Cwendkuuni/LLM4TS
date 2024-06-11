import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


public class OscServiceTest {
    
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
        MockitoAnnotations.initMocks(this);
        oscService = new OscService(databaseRepository, connectionService, scheduleTaskRepository, scheduleRepository);
    }

    @Test
    public void testGetOscDatabaseInfoWithExistentId() {
        Mockito.when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(new DatabaseEntity()));
        assertTrue(!oscService.getOscDatabaseInfo(1L).getDatabaseId().isEmpty());
    }

    @Test
    public void testGetOscDatabaseInfoWithNonexistentId() {
        Mockito.when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertTrue(oscService.getOscDatabaseInfo(1L).getDatabaseId().isEmpty());
    }

    @Test
    public void testSwapTableWithExistentId() {
        Mockito.when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(new ScheduleTaskEntity()));
        Mockito.when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(new ScheduleEntity()));
        assertTrue(oscService.swapTable(1L).getScheduleTaskId().equals(1L));
    }

    @Test
    public void testSwapTableWithNonexistentId() {
        Mockito.when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());
        Mockito.when(scheduleRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertFalse(oscService.swapTable(1L).getManualSwapTableStarted());
    }

    @Test
    public void testGetLockUserIsRequired() {
        Mockito.when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(new ConnectionConfig());
        assertTrue(oscService.getLockUserIsRequired(1L));
    }

    @Test(expected = Exception.class)
    public void testGetLockUserIsRequiredWhenExceptionThrown() {
        Mockito.when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenThrow(Exception.class);
        oscService.getLockUserIsRequired(1L);
    }

}