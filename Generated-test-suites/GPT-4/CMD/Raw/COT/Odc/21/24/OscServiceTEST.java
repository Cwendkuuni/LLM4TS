
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.runner.RunWith;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

    @InjectMocks
    OscService oscService;

    @Mock
    DatabaseRepository databaseRepository;

    // Other mocks for the dependencies you need 
    // ...

    // getOscDatabaseInfo(Long id) test
    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity entity = new DatabaseEntity();
        Mockito.when(databaseRepository.findById(id)).thenReturn(Optional.of(entity));
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        Assert.assertNotNull(result);
        // You can add more checks relevant to your application
    }

    // swapTable(Long scheduleTaskId) test
    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        Mockito.when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        Mockito.when(scheduleRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(scheduleEntity));
        
        // You can add mock for the JsonUtils fromJson and toJson, also for PreConditions.validArgumentState if needed

        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);
        Assert.assertNotNull(result);
        // You can add more checks relevant to your application
    }
    
    // getLockUserIsRequired(Long connectionId) test
    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        // You need to add mocks for the ConnectionConfig and possibly for the OscDBUserUtil.isLockUserRequired
        // For ConnectionConfig decryption you also need a mock

        boolean result = oscService.getLockUserIsRequired(connectionId);
        // Assert according to your logic
    }

    // You should also add tests for the exceptions that you throw in your methods
    // . . .
}
