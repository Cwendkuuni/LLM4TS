import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unchecked")
public class OscServiceTest {

    private OscService oscService;
    private ScheduleTaskRepository scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);

    @Autowired
    public void setUp(OscService oscService) {
        this.oscService = oscService;
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Before
    public void init() {
        // Initialize mocks
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long databaseId = 1L;
        DatabaseEntity databaseEntity = Mockito.mock(DatabaseEntity.class);
        Mockito.when(databaseRepository.findById(databaseId)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(databaseId);
        Assert.assertEquals(databaseId, result.getDatabaseId());
        Assert.assertFalse(result.isLockDatabaseUserRequired());

        Mockito.verify(databaseRepository, Mockito.times(1)).findById(databaseId);
    }

    @Test
    public void testGetOscDatabaseInfoWhenDatabaseNotFound() {
        Long databaseId = 1L;

        Mockito.when(databaseRepository.findById(databaseId)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(databaseId);
        Assert.assertNull(result);

        Mockito.verify(databaseRepository, Mockito.times(1)).findById(databaseId);
    }

    @Test
    @Transactional
    public void testSwapTable() {
        Long scheduleTaskId = UUID.randomUUID().toString();
        ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        Mockito.when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        Mockito.when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(oscParameters));
        Mockito.when(scheduleEntity.getJobType()).thenReturn(JobType.ONLINE_SCHEMA_CHANGE);

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setScheduleId(1L);
        scheduleTask.setResultJson("{}");
        Mockito.when(scheduleTaskRepository.save(Mockito.any(ScheduleTaskEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);
        Assert.assertEquals(scheduleTaskId, result.getScheduleTaskId());

        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).findById(scheduleTaskId);
        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).save(Mockito.any(ScheduleTaskEntity.class));
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testSwapTableWhenInvalidJobType() {
        Long scheduleTaskId = UUID.randomUUID().toString();
        ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        Mockito.when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        Mockito.when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(oscParameters));
        Mockito.when(scheduleEntity.getJobType()).thenReturn(JobType.BACKUP);

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    @Transactional
    public void testSwapTableWhenSwapTableTypeIsNotManual() {
        Long scheduleTaskId = UUID.randomUUID().toString();
        ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.AUTO);
        Mockito.when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        Mockito.when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(oscParameters));
        Mockito.when(scheduleEntity.getJobType()).thenReturn(JobType.ONLINE_SCHEMA_CHANGE);

        oscService.swapTable(scheduleTaskId);

        Mockito.verify(scheduleTaskRepository, Mockito.never()).save(Mockito.any(ScheduleTaskEntity.class));
    }

    @Test(expected = IllegalStateException.class)
    @Transactional
    public void testSwapTableWhenManualSwapTableIsNotEnabled() {
        Long scheduleTaskId = UUID.randomUUID().toString();
        ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        Mockito.when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        Mockito.when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(oscParameters));
        Mockito.when(scheduleEntity.getJobType()).thenReturn(JobType.ONLINE_SCHEMA_CHANGE);

        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson("{\"manualSwapTableEnabled\":false}", OnlineSchemaChangeScheduleTaskResult.class);
        Mockito.when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(new ScheduleTaskEntity()));
        Mockito.when(scheduleTaskRepository.updateTaskResult(scheduleTaskId, JsonUtils.toJson(result))).thenAnswer(i -> i.getArguments()[0]);

        oscService.swapTable(scheduleTaskId);
    }
}