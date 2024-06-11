import com.oceanbase.odc.service.onlineschemachange.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Optional;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        OscLockDatabaseUserInfo expectedInfo = new OscLockDatabaseUserInfo();
        expectedInfo.setDatabaseId(1L);
        expectedInfo.setLockDatabaseUserRequired(true);

        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);

        assertEquals(expectedInfo.getDatabaseId(), result.getDatabaseId());
        assertEquals(expectedInfo.isLockDatabaseUserRequired(), result.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("1");
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        when(scheduleTaskRepository.updateTaskResult(1L, "{}")).thenReturn(true);

        OscSwapTableVO expectedSwapTable = new OscSwapTableVO();
        expectedSwapTable.setScheduleTaskId(1L);

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);

        assertEquals(expectedSwapTable.getScheduleTaskId(), oscSwapTable.getScheduleTaskId());
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(new ConnectionConfig());

        assertTrue(oscService.getLockUserIsRequired(connectionId));
    }

}