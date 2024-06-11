
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.schedule.model.JobType;

@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;
    
    @Mock
    private ConnectionService connectionService;

    @BeforeEach
    public void setup() {
        // Setup mock data or perform any necessary setup before tests
    }

    @Test
    public void testGetOscDatabaseInfo() {
        OscService oscService = new OscService();
        OscLockDatabaseUserInfo expectedInfo = new OscLockDatabaseUserInfo();
        expectedInfo.setDatabaseId(1L);
        expectedInfo.setLockDatabaseUserRequired(true);
        
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(new DatabaseEntity(1L, true)));
        OscLockDatabaseUserInfo actualInfo = oscService.getOscDatabaseInfo(1L);
        
        assertEquals(expectedInfo.getDatabaseId(), actualInfo.getDatabaseId());
        assertTrue(actualInfo.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        OscService oscService = new OscService();
        OscSwapTableVO expectedSwapTable = new OscSwapTableVO();
        expectedSwapTable.setScheduleTaskId(1L);
        
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(new ScheduleTaskEntity(1L, "1", "{}")));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(new ScheduleEntity(1L, JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, "{}")));
        
        OscSwapTableVO actualSwapTable = oscService.swapTable(1L);
        
        assertEquals(expectedSwapTable.getScheduleTaskId(), actualSwapTable.getScheduleTaskId());
    }
}
