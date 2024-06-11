
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private ConnectionService connectionService;

    @InjectMocks
    private OscService oscService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGetOscDatabaseInfo(){

        DatabaseEntity databaseEntity = mock(DatabaseEntity.class);
        Optional<DatabaseEntity> database = Optional.of(databaseEntity);
        
        when(databaseRepository.findById(anyLong())).thenReturn(database);
        
        OscLockDatabaseUserInfo returnedValue = oscService.getOscDatabaseInfo(1L);
      
        assertEquals(returnedValue.getDatabaseId(), databaseEntity.getDatabaseId());
    }

    @Test
    public void testSwapTable(){
        
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        
        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(scheduleTask);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(scheduleTaskOptional);
        
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        
        Optional<ScheduleEntity> scheduleEntityOptional = Optional.of(scheduleEntity);
        
        when(scheduleRepository.findById(anyLong())).thenReturn(scheduleEntityOptional);
        
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));
        
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        
        scheduleTask.setResultJson(JsonUtils.toJson(result));

        OscSwapTableVO returnedValue = oscService.swapTable(1L);

        assertEquals(returnedValue.getScheduleTaskId(), scheduleTask.getTaskId());
    }

    @Test
    public void testLockUserIsRequired(){
        
        ConnectionConfig connConfig = mock(ConnectionConfig.class);
        
        when(connConfig.getDialectType()).thenReturn("testDialectType");
        
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(connConfig);

        boolean returnedValue = oscService.getLockUserIsRequired(1L);

        assertNotNull(returnedValue);
    }
}
