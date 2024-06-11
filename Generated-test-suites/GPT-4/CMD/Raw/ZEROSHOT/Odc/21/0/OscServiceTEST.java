
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.*;
import com.oceanbase.odc.service.schedule.model.JobType;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
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

    private OnlineSchemaChangeParameters oscParameters;
    
    private OnlineSchemaChangeScheduleTaskResult scheduleTaskResult;

    private OscSwapTableVO oscSwapTableVO;
    
    @Before
    public void setUp() {
        oscParameters = new OnlineSchemaChangeParameters();
        scheduleTaskResult = new OnlineSchemaChangeScheduleTaskResult();
        oscSwapTableVO = new OscSwapTableVO();
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        when(databaseRepository.findById(id)).thenReturn(Optional.of(new DatabaseEntity()));
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertNotNull(result);
        assertEquals(id, result.getDatabaseId());
    }

    @Test
    public void testSwapTable() {
        Long id = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(scheduleTaskResult));
        when(scheduleTaskRepository.findById(id)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(id)).thenReturn(Optional.of(scheduleEntity));

        OscSwapTableVO result = oscService.swapTable(id);
        assertNotNull(result);
        assertEquals(id, result.getScheduleTaskId());
    }

    @Test(expected = ErrorCodes.BadArgument.class)
    public void testSwapTableException() {
        Long id = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        oscParameters.setSwapTableType(SwapTableType.AUTOMATIC);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));
        scheduleEntity.setJobType(JobType.PREPARATION);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(scheduleTaskResult));
        when(scheduleTaskRepository.findById(id)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(id)).thenReturn(Optional.of(scheduleEntity));

        oscService.swapTable(id);
    }

}
