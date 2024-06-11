import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;

public class OscServiceTest {

    OscService oscService;
    
    @Mock
    DatabaseRepository databaseRepository;
    
    @Mock
    ConnectionService connectionService;
    
    @Mock
    ScheduleTaskRepository scheduleTaskRepository;
    
    @Mock
    ScheduleRepository scheduleRepository;
    
    @Before
    public void setUp() {
        oscService = new OscService();
        oscService.databaseRepository = databaseRepository;
        oscService.connectionService = connectionService;
        oscService.scheduleTaskRepository = scheduleTaskRepository;
        oscService.scheduleRepository = scheduleRepository;
    }

    @Test
    public void testGetOscDatabaseInfo() {
        OscLockDatabaseUserInfo expected = new OscLockDatabaseUserInfo();
        expected.setDatabaseId(1L);
        expected.setLockDatabaseUserRequired(true);
        
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(new DatabaseEntity()));
        
        OscLockDatabaseUserInfo actual = oscService.getOscDatabaseInfo(1L);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSwapTableType(SwapTableType.MANUAL);
        
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(parameters));
        
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(result));
        
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(Long.parseLong(scheduleTaskEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        
        OscSwapTableVO expected = new OscSwapTableVO();
        expected.setScheduleTaskId(scheduleTaskId);
        
        OscSwapTableVO actual = oscService.swapTable(scheduleTaskId);
        
        assertEquals(expected, actual);
    }

}