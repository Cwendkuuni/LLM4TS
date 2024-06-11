import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.schedule.model.JobType;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    private DatabaseRepository databaseRepository;
    private ScheduleRepository scheduleRepository;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ConnectionService connectionService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        databaseRepository = mock(DatabaseRepository.class);
        scheduleRepository = mock(ScheduleRepository.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        connectionService = mock(ConnectionService.class);
        oscService.setDatabaseRepository(databaseRepository);
        oscService.setScheduleRepository(scheduleRepository);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
        oscService.setConnectionService(connectionService);
    }

    @Test
    public void testGetOscDatabaseInfo_databaseNotPresent() {
        Long databaseId = UUID.randomUUID().longValue();
        when(databaseRepository.findById(databaseId)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(databaseId);
        Assert.assertNull(result);

        verify(databaseRepository, times(1)).findById(databaseId);
    }

    @Test
    public void testGetOscDatabaseInfo_databasePresent() {
        Long databaseId = UUID.randomUUID().longValue();
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(databaseId);
        when(databaseRepository.findById(databaseId)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(databaseId);
        Assert.assertEquals(databaseId, result.getDatabaseId());
        Assert.assertFalse(result.isLockDatabaseUserRequired());

        verify(databaseRepository, times(1)).findById(databaseId);
    }

    @Test
    public void testSwapTable_scheduleTaskNotPresent() {
        Long scheduleTaskId = UUID.randomUUID().longValue();
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());

        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);
        Assert.assertNull(result);

        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
    }

    @Test
    public void testSwapTable_scheduleTaskPresent_invalidJobType() {
        Long scheduleTaskId = UUID.randomUUID().longValue();
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("INVALID_JOB_TYPE");
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });

        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
    }

    @Test
    public void testSwapTable_scheduleTaskPresent_validJobType_manualSwapTableDisabled() {
        Long scheduleTaskId = UUID.randomUUID().longValue();
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("VALID_JOB_TYPE");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("VALID_SCHEDULE_ID");
        scheduleEntity.setJobParametersJson("{\"swapTableType\":\"MANUAL\"}");
        when(scheduleRepository.findById(Long.parseLong(scheduleTaskEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), OnlineSchemaChangeParameters.class))
                .thenReturn(oscParameters);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);

        when(scheduleTaskEntity.getResultJson()).thenReturn(JsonUtils.toJson(result));

        OscSwapTableVO resultSwapTable = oscService.swapTable(scheduleTaskId);
        Assert.assertNull(resultSwapTable);

        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
        verify(scheduleRepository, times(1)).findById(Long.parseLong(scheduleTaskEntity.getJobName()));
        verify(JsonUtils, times(1)).fromJson(scheduleEntity.getJobParametersJson(), OnlineSchemaChangeParameters.class);
        verify(JsonUtils, times(1)).fromJson(scheduleTaskEntity.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class);
    }

    @Test
    public void testSwapTable_scheduleTaskPresent_validJobType_manualSwapTableEnabled() {
        Long scheduleTaskId = UUID.randomUUID().longValue();
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("VALID_JOB_TYPE");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("VALID_SCHEDULE_ID");
        scheduleEntity.setJobParametersJson("{\"swapTableType\":\"MANUAL\"}");
        when(scheduleRepository.findById(Long.parseLong(scheduleTaskEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), OnlineSchemaChangeParameters.class))
                .thenReturn(oscParameters);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        when(scheduleTaskEntity.getResultJson()).thenReturn(JsonUtils.toJson(result));

        OscSwapTableVO resultSwapTable = oscService.swapTable(scheduleTaskId);
        Assert.assertEquals(scheduleTaskId, resultSwapTable.getScheduleTaskId());

        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
        verify(scheduleRepository, times(1)).findById(Long.parseLong(scheduleTaskEntity.getJobName()));
        verify(JsonUtils, times(1)).fromJson(scheduleEntity.getJobParametersJson(), OnlineSchemaChangeParameters.class);
        verify(JsonUtils, times(1)).fromJson(scheduleTaskEntity.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class);
    }
}