import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
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

    @Test
    public void testGetOscDatabaseInfo() {
        // Typical use case
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(id);
        assertNotNull(oscDatabaseInfo);
        assertEquals(id, oscDatabaseInfo.getDatabaseId());
        
        // Edge case - database not found
        Long nonExistentId = 2L;
        when(databaseRepository.findById(nonExistentId)).thenReturn(Optional.empty());
        
        OscLockDatabaseUserInfo oscNonExistentDatabase = oscService.getOscDatabaseInfo(nonExistentId);
        assertNotNull(oscNonExistentDatabase);
        assertEquals(0L, oscNonExistentDatabase.getDatabaseId());
    }

    @Test
    public void testSwapTable() {
        // Typical use case
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{}");
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        String resultJson = "{}";
        when(scheduleTaskEntity.getResultJson()).thenReturn(resultJson);
        
        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);
        assertNotNull(oscSwapTable);
        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
        
        // Edge case - schedule task not found
        Long nonExistentTaskId = 2L;
        when(scheduleTaskRepository.findById(nonExistentTaskId)).thenReturn(Optional.empty());
        
        OscSwapTableVO nonExistentSwapTable = oscService.swapTable(nonExistentTaskId);
        assertNull(nonExistentSwapTable);
    }
}