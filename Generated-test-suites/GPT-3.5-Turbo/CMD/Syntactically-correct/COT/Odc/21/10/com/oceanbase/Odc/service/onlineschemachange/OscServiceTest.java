import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private ConnectionService connectionService;

    @Test
    public void testGetOscDatabaseInfo() {
        OscService oscService = new OscService();
        Long id = 1L;
        OscLockDatabaseUserInfo oscDatabase = new OscLockDatabaseUserInfo();
        
        // Test for existing database entity
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        Optional<DatabaseEntity> optionalDatabaseEntity = Optional.of(databaseEntity);
        Mockito.when(databaseRepository.findById(id)).thenReturn(optionalDatabaseEntity);
        
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        
        assertEquals(id, result.getDatabaseId());
        assertFalse(result.isLockDatabaseUserRequired());
        
        // Test for non-existing database entity
        Mockito.when(databaseRepository.findById(2L)).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo resultNotExist = oscService.getOscDatabaseInfo(2L);

        assertEquals(0L, resultNotExist.getDatabaseId());
        assertFalse(resultNotExist.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        OscService oscService = new OscService();
        Long scheduleTaskId = 1L;
        OscSwapTableVO oscSwapTable = new OscSwapTableVO();
        
        // Mock schedule task entity
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        Optional<ScheduleTaskEntity> optionalScheduleTaskEntity = Optional.of(scheduleTaskEntity);
        Mockito.when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(optionalScheduleTaskEntity);
        
        // Mock schedule entity
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        Mockito.when(scheduleRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleEntity));
        
        // Test for manual swap table
        // Add more test cases for edge cases and exceptions
    }
}