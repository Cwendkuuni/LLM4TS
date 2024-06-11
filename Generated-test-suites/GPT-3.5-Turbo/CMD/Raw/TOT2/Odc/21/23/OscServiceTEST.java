import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        oscService = new OscService();
        oscService.setDatabaseRepository(databaseRepository);
        oscService.setConnectionService(connectionService);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
        oscService.setScheduleRepository(scheduleRepository);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(id);
        
        // Test if oscDatabaseInfo is not null after calling the method
        assertNotNull(oscDatabaseInfo);
        
        // Test if lockDatabaseUserRequired is false for non-existing database
        assertFalse(oscDatabaseInfo.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setResultJson("{'manualSwapTableEnabled': true}");
        
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        
        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);
        
        // Test if oscSwapTable is not null after calling the method
        assertNotNull(oscSwapTable);
        
        // Test if manualSwapTableEnabled is set to false after swapTable
        ScheduleTaskEntity updatedScheduleTaskEntity = new ScheduleTaskEntity();
        updatedScheduleTaskEntity.setJobName("1");
        updatedScheduleTaskEntity.setResultJson("{'manualSwapTableEnabled': false}");
        verify(scheduleTaskRepository).updateTaskResult(scheduleTaskId, JsonUtils.toJson(updatedScheduleTaskEntity));
    }
}