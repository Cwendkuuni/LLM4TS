import org.junit.Test;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.junit.Before;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import java.util.Optional;

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

    @Test
    public void testGetOscDatabaseInfo() {
        Long testId = 1L;
        Mockito.when(databaseRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(testId);
        Assert.assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTableWithInvalidTaskType() {
        Long testId = 1L;
        ScheduleEntity testEntity = new ScheduleEntity();
        testEntity.setJobType(JobType.BIG_JOB);
        Mockito.when(scheduleRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(testEntity));
        oscService.swapTable(testId);
    }

    // Add additional test methods here
}