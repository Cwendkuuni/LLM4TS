
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {
    
    @Mock
    DatabaseRepository databaseRepository;
    @Mock
    ConnectionService connectionService;
    @Mock
    ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    ScheduleRepository scheduleRepository;
    
    @InjectMocks
    OscService oscService;
    
    @Before
    public void setUp() {
        // Optionally initialize mock objects here
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity dbEntity = new DatabaseEntity();
        dbEntity.setDatabaseId(id);
        dbEntity.setConnectionId(id);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(dbEntity));
        when(connectionService.getForConnectionSkipPermissionCheck(id)).thenReturn(any(ConnectionConfig.class));

        OscLockDatabaseUserInfo userInfo = oscService.getOscDatabaseInfo(id);

        Assert.assertEquals(id, userInfo.getDatabaseId());
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetOscDatabaseInfo_NotFoundException() {
        Long id = 1L;
        when(databaseRepository.findById(id)).thenReturn(Optional.empty());
        
        oscService.getOscDatabaseInfo(id);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setJobName(String.valueOf(scheduleTaskId));

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(taskEntity));
        
        // Add logical checks for running swapTable method here
        
        OscSwapTableVO oscSwapTableVO = oscService.swapTable(scheduleTaskId);

        Assert.assertEquals(scheduleTaskId, oscSwapTableVO.getScheduleTaskId());
    }

    @Test(expected = NullPointerException.class)
    public void testSwapTable_NotFoundException() {
        Long scheduleTaskId = 1L;

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());

        oscService.swapTable(scheduleTaskId);
    }
}
