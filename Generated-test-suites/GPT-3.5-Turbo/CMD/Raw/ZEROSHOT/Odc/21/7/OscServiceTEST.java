import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.runner.RunWith;

@RunWith(MockitoJUnitRunner.class)
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

    private final Long databaseId = 1L;

    @Before
    public void setup() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(databaseId);
        
        when(databaseRepository.findById(eq(databaseId))).thenReturn(Optional.of(databaseEntity));
    }

    @Test
    public void testGetOscDatabaseInfo() {
        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(databaseId);
        
        assertNotNull(oscDatabaseInfo);
        assertEquals(oscDatabaseInfo.getDatabaseId(), databaseId);
        // Add more assertions here as needed
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName(String.valueOf(scheduleTaskId));
        
        when(scheduleTaskRepository.findById(eq(scheduleTaskId))).thenReturn(Optional.of(scheduleTaskEntity));
        
        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);
        
        assertNotNull(oscSwapTable);
        assertEquals(oscSwapTable.getScheduleTaskId(), scheduleTaskId);
        // Add more assertions here as needed
    }
}