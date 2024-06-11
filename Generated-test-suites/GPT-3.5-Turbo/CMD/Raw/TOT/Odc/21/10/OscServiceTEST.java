import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private OscService oscService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        // Test for existing database
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(new DatabaseEntity()));
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);
        assertNotNull(oscDatabase);
        
        // Test for non-existing database
        when(databaseRepository.findById(2L)).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo oscDatabaseNonExisting = oscService.getOscDatabaseInfo(2L);
        assertNotNull(oscDatabaseNonExisting);
    }

    @Test
    public void testSwapTable() {
        // Test for successful swap table
        ScheduleTaskEntity mockScheduleTask = new ScheduleTaskEntity();
        mockScheduleTask.setJobName("1");
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(mockScheduleTask));
        
        ScheduleEntity mockScheduleEntity = new ScheduleEntity();
        mockScheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        mockScheduleEntity.setJobParametersJson("{ \"swapTableType\": \"MANUAL\" }");
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(mockScheduleEntity));
        
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(false);
        mockScheduleTask.setResultJson(JsonUtils.toJson(result));
        
        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        assertNotNull(oscSwapTable);
        assertTrue(result.isManualSwapTableStarted());
    }

    // Add more test cases for edge cases and error scenarios

}