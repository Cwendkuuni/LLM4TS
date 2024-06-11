import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.Optional;

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
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 123L;
        when(databaseRepository.findById(id)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo userInfo = oscService.getOscDatabaseInfo(id);

        assertTrue("Should return an empty object when the database with such id doesn't exist.",
                userInfo.getDatabaseId() == null);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 123L;
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());

        try {
            oscService.swapTable(scheduleTaskId);
            fail("Should fail when the schedule task with such id doesn't exist.");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    // To test private methods, you might have to break the encapsulation or find a way to trigger the private method indirectly from a public method if possible.
    // In this case, you can test the getLockUserIsRequired method indirectly through `getOscDatabaseInfo` method.
}