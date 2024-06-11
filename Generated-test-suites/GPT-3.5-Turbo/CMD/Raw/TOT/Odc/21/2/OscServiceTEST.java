import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class OscServiceTest {

    private OscService oscService;
    private DatabaseRepository databaseRepository;
    private ConnectionService connectionService;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ScheduleRepository scheduleRepository;

    @Before
    public void setup() {
        oscService = new OscService();
        databaseRepository = mock(DatabaseRepository.class);
        connectionService = mock(ConnectionService.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        scheduleRepository = mock(ScheduleRepository.class);

        oscService.setDatabaseRepository(databaseRepository);
        oscService.setConnectionService(connectionService);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
        oscService.setScheduleRepository(scheduleRepository);
    }

    @Test
    public void testGetOscDatabaseInfo_Success() {
        // Setup
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        // Test
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        // Verify
        assertEquals(databaseEntity.getDatabaseId(), result.getDatabaseId());
        assertFalse(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable_Success() {
        // Setup
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult taskResult = new OnlineSchemaChangeScheduleTaskResult();
        taskResult.setManualSwapTableEnabled(true);
        taskResult.setManualSwapTableStarted(false);

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        // Test
        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);

        // Verify
        assertTrue(result.getScheduleTaskId() == scheduleTaskId);
    }
}