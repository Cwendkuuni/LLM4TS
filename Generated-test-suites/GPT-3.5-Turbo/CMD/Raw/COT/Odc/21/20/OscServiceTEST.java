
import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mock;
import static org.mockito.Mockito.when;

public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;
    @Mock
    private ConnectionService connectionService;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private ScheduleRepository scheduleRepository;

    @Test
    public void testGetOscDatabaseInfo() {
        OscService oscService = new OscService();
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(new DatabaseEntity()));
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);
        Assert.assertNotNull(oscDatabase);
        Assert.assertEquals(1L, oscDatabase.getDatabaseId());
    }

    @Test
    public void testSwapTable() {
        OscService oscService = new OscService();
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{parameterJson}");
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        
        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        Assert.assertNotNull(oscSwapTable);
        Assert.assertEquals(1L, oscSwapTable.getScheduleTaskId());
    }

    @Test
    public void testGetLockUserIsRequired() {
        OscService oscService = new OscService();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");
        when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(connectionConfig);
        boolean lockUserRequired = oscService.getLockUserIsRequired(1L);
        Assert.assertFalse(lockUserRequired);
    }
}
