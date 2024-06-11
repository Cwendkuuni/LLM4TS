import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.OscDBUserUtil;
import com.oceanbase.odc.service.onlineschemachange.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;

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
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    // Test for getOscDatabaseInfo method
    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1);
        databaseEntity.setConnectionId(1);
        
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));
        
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        
        assertEquals(result.getDatabaseId(), databaseEntity.getDatabaseId());
        assertTrue(result.isLockDatabaseUserRequired());
    }
    
    // Test for swapTable method
    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        Long scheduleId = 1L;
        
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName(String.valueOf(scheduleId));
        
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson("{ \"swapTableType\": \"MANUAL\" }");
        
        OnlineSchemaChangeScheduleTaskResult scheduleTaskResult = new OnlineSchemaChangeScheduleTaskResult();
        scheduleTaskResult.setManualSwapTableEnabled(true);
        
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleEntity));
        
        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);
        
        assertNotNull(result);
        assertEquals(result.getScheduleTaskId(), scheduleTaskId);
    }
    
    // Test for getLockUserIsRequired method
    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");
        
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(connectionConfig);
        
        assertTrue(OscDBUserUtil.isLockUserRequired(connectionConfig.getDialectType(), () -> "1.0"));
    }

}