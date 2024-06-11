import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OscServiceTest {

    private OscService oscService;
    private DatabaseRepository databaseRepository;
    private ConnectionService connectionService;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ScheduleRepository scheduleRepository;

    @Test
    public void testGetOscDatabaseInfo() {
        oscService = new OscService();
        
        databaseRepository = mock(DatabaseRepository.class);
        oscService.databaseRepository = databaseRepository;
        
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));
        
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);
        
        assertNotNull(oscDatabase);
        assertEquals(id, oscDatabase.getDatabaseId());
    }

    @Test
    public void testSwapTable() {
        oscService = new OscService();
        
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        oscService.scheduleTaskRepository = scheduleTaskRepository;
        
        scheduleRepository = mock(ScheduleRepository.class);
        oscService.scheduleRepository = scheduleRepository;
        
        Long scheduleTaskId = 1L;
        Long scheduleId = 2L;
        Long connectionId = 3L;
        
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        scheduleTaskEntity.setJobName(String.valueOf(scheduleId));
        
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson("{}");
        
        OscSwapTableVO oscSwapTableExpected = new OscSwapTableVO();
        oscSwapTableExpected.setScheduleTaskId(scheduleTaskId);
        
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleEntity));
        
        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);
        
        assertNotNull(oscSwapTable);
        assertEquals(oscSwapTableExpected.getScheduleTaskId(), oscSwapTable.getScheduleTaskId());
    }

    @Test
    public void testGetLockUserIsRequired() {
        oscService = new OscService();
        
        connectionService = mock(ConnectionService.class);
        oscService.connectionService = connectionService;
        
        Long connectionId = 1L;
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");
        
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(connectionConfig);
        
        assertTrue(oscService.getLockUserIsRequired(connectionId));
    }
}