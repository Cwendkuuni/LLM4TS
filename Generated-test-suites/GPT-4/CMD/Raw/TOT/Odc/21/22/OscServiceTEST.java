import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;

// other imports..

@RunWith(SpringJUnit4ClassRunner.class)
public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;
    @Mock
    private ConnectionService connectionService;

    @InjectMocks
    OscService oscService;

    @Before
    public void setUp() {
        Mockito.when(databaseRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new DatabaseEntity()));
        Mockito.when(connectionService.getForConnectionSkipPermissionCheck(Mockito.anyLong())).thenReturn(new ConnectionConfig());
    }

    // with above setup you can create test methods for getOscDatabaseInfo and swapTable

    @Test
    public void testGetOscDatabaseInfo() {
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
        // assertions
        Assert.assertTrue(result.getDatabaseId() != null);
    }

    @Test
    public void testSwapTable() {
        OscSwapTableVO result = oscService.swapTable(1L);
        // assertions
        Assert.assertTrue(result.getScheduleTaskId() != null);
    }
}