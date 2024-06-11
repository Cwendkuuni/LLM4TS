
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import java.util.Optional;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.JobType;
import com.oceanbase.odc.core.shared.constant.SwapTableType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;

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

    @Test
    public void getOscDatabaseInfoTest() {
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(new DatabaseEntity()));
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
        verify(databaseRepository, times(1)).findById(1L);
        // assert additional conditions you want to test in result
    }

    @Test
    public void swapTableTest() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSwapTableType(SwapTableType.MANUAL);
        when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(),
            OnlineSchemaChangeParameters.class)).thenReturn(parameters);
        when(JsonUtils.fromJson(taskEntity.getResultJson(),
            OnlineSchemaChangeScheduleTaskResult.class)).thenReturn(result);
        OscSwapTableVO oscSwapTableVO = oscService.swapTable(1L);
        verify(scheduleTaskRepository, times(1)).findById(1L);
        verify(scheduleRepository, times(1)).findById(1L);
        verify(JsonUtils, times(2)).fromJson(anyString(), any());
        // assert additional conditions you want to test in oscSwapTableVO
    }

    // Example edge case: when returned optional is empty
    @Test(expected = Exception.class)
    public void getOscDatabaseInfoEmptyOptionalTest() {
        when(databaseRepository.findById(1L)).thenReturn(Optional.empty());
        oscService.getOscDatabaseInfo(1L);
    }

    // more edge and exception handling test cases...
}
