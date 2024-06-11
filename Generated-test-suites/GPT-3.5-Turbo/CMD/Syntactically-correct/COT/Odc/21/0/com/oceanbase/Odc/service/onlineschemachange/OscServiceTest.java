
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.schedule.model.JobType;

public class OscServiceTest {

    private DatabaseRepository databaseRepository = mock(DatabaseRepository.class);
    private ConnectionService connectionService = mock(ConnectionService.class);
    private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
    private ScheduleRepository scheduleRepository = mock(ScheduleRepository.class);
    private OscService oscService = new OscService();

    @Test
    public void testGetOscDatabaseInfo() {
        when(databaseRepository.findById(any(Long.class))).thenReturn(Optional.of(new DatabaseEntity()));
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);
        assertEquals(1L, oscDatabase.getDatabaseId());
        assertFalse(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        when(scheduleTaskRepository.findById(any(Long.class))).thenReturn(Optional.of(scheduleTaskEntity));
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        when(scheduleRepository.findById(any(Long.class))).thenReturn(Optional.of(scheduleEntity));

        scheduleTaskEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson("{}");
        scheduleTaskEntity.setResultJson("{}");
        
        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        assertEquals(1L, oscSwapTable.getScheduleTaskId());
    }

    @Test
    public void testGetLockUserIsRequired() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("Oracle");
        when(connectionService.getForConnectionSkipPermissionCheck(any(Long.class))).thenReturn(connectionConfig);
        boolean lockUserRequired = oscService.getLockUserIsRequired(1L);
        assertTrue(lockUserRequired);
    }
}
