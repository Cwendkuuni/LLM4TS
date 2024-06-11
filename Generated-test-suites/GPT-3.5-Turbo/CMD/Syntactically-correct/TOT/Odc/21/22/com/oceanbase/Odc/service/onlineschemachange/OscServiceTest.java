import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;
    @Mock
    private ConnectionService connectionService;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private OscService oscService;

    @Before
    public void setup() {
        // Setup mock data or other initialization steps
    }

    @Test
    public void testGetOscDatabaseInfo() {
        // Typical use case
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));
        
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertEquals(result.getDatabaseId(), id);

        // Edge case - Database not present
        when(databaseRepository.findById(2L)).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo emptyResult = oscService.getOscDatabaseInfo(2L);
        assertEquals(emptyResult.getDatabaseId(), null);

        // Error scenario - Null input
        assertThrows(IllegalArgumentException.class, () -> oscService.getOscDatabaseInfo(null));
    }

    @Test
    public void testSwapTable() {
        // Typical use case
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
        
        OnlineSchemaChangeScheduleTaskResult scheduleTaskResult = new OnlineSchemaChangeScheduleTaskResult();
        scheduleTaskResult.setManualSwapTableEnabled(true);
        
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        
        String resultJson = JsonUtils.toJson(scheduleTaskResult);
        
        when(scheduleTaskOptional.getResultJson()).thenReturn(resultJson);
        
        OscSwapTableVO oscSwapTableVO = oscService.swapTable(scheduleTaskId);
        assertFalse(oscSwapTableVO.isManualSwapTableEnabled());

        // Error scenario - Invalid task type
        scheduleEntity.setJobType(JobType.OTHER);
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        
        assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(scheduleTaskId));

        // Error scenario - Manual swap table not enabled
        scheduleTaskResult.setManualSwapTableEnabled(false);
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskOptional.getResultJson()).thenReturn(JsonUtils.toJson(scheduleTaskResult));
        
        assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(scheduleTaskId));

        // Error scenario - Invalid swap table type
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        OnlineSchemaChangeParameters invalidParameters = new OnlineSchemaChangeParameters();
        invalidParameters.setSwapTableType(SwapTableType.AUTO);
        when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), OnlineSchemaChangeParameters.class))
            .thenReturn(invalidParameters);
        
        assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(scheduleTaskId));
    }

}