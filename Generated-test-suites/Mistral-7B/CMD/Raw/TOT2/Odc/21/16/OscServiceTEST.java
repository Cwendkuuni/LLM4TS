import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@SpringBootTest
@ActiveProfiles("test")
public class OscServiceTest {

    @Autowired
    private OscService oscService;

    @Autowired
    private ConnectionService connectionService;

    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Test
    @Rollback(false)
    public void testGetOscDatabaseInfo() {
        Long id = 1L;

        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setConnectionId(id);
        databaseEntity.setDatabaseId(id);

        Optional<DatabaseEntity> databaseOptional = Optional.of(databaseEntity);

        given(databaseRepository.findById(id))
                .willReturn(databaseOptional);

        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(id);

        Assertions.assertEquals(id, oscDatabaseInfo.getDatabaseId());
        Assertions.assertFalse(oscDatabaseInfo.isLockDatabaseUserRequired());
    }

    @Test
    @Rollback(false)
    public void testGetOscDatabaseInfo_DatabaseNotFound() {
        Long id = 1L;

        given(databaseRepository.findById(id))
                .willReturn(Optional.empty());

        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(id);

        Assertions.assertNull(oscDatabaseInfo);
    }

    @Test
    @Rollback(false)
    public void testSwapTable() {
        Long scheduleTaskId = 1L;

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setScheduleId(scheduleTaskId);
        scheduleTaskEntity.setId(scheduleTaskId);
        scheduleTaskEntity.setJobName("testJobName");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleTaskId);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson("{}");

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(false);

        given(scheduleRepository.findById(scheduleTaskId))
                .willReturn(Optional.of(scheduleEntity));

        given(jsonUtil.fromJson(ArgumentMatchers.anyString(), OnlineSchemaChangeParameters.class))
                .willReturn(oscParameters);

        given(jsonUtil.fromJson(ArgumentMatchers.anyString(), OnlineSchemaChangeScheduleTaskResult.class))
                .willReturn(result);

        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);

        Assertions.assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
    }

    @Test
    @Rollback(false)
    public void testSwapTable_JobTypeNotOnlineSchemaChange() {
        Long scheduleTaskId = 1L;

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setScheduleId(scheduleTaskId);
        scheduleTaskEntity.setId(scheduleTaskId);
        scheduleTaskEntity.setJobName("testJobName");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleTaskId);
        scheduleEntity.setJobType(JobType.BACKUP_DATABASE);

        given(scheduleRepository.findById(scheduleTaskId))
                .willReturn(Optional.of(scheduleEntity));

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });
    }

    @Test
    @Rollback(false)
    public void testSwapTable_ManualSwapTableDisabled() {
        Long scheduleTaskId = 1L;

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        scheduleTaskEntity.setJobName("testJobName");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleTaskId);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson("{}");

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);
        result.setManualSwapTableStarted(false);

        given(scheduleRepository.findById(scheduleTaskId))
                .willReturn(Optional.of(scheduleEntity));

        given(jsonUtil.fromJson(ArgumentMatchers.anyString(), OnlineSchemaChangeParameters.class))
                .willReturn(oscParameters);

        given(jsonUtil.fromJson(ArgumentMatchers.anyString(), OnlineSchemaChangeScheduleTaskResult.class))
                .willReturn(result);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });
    }

    @Test
    @Rollback(false)
    public void testSwapTable_CloseSwapTable() {
        Long scheduleTaskId = 1L;

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        scheduleTaskEntity.setJobName("testJobName");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleTaskId);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson("{}");

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(true);

        given(scheduleRepository.findById(scheduleTaskId))
                .willReturn(Optional.of(scheduleEntity));

        given(jsonUtil.fromJson(ArgumentMatchers.anyString(), OnlineSchemaChangeParameters.class))
                .willReturn(oscParameters);

        given(jsonUtil.fromJson(ArgumentMatchers.anyString(), OnlineSchemaChangeScheduleTaskResult.class))
                .willReturn(result);

        oscService.swapTable(scheduleTaskId);

        then(scheduleTaskRepository).should().updateTaskResult(
                ArgumentMatchers.eq(scheduleTaskId),
                ArgumentMatchers.eq(JsonUtils.toJson(result)));
    }

    @BeforeEach
    public void setup() {
        connectionService = mock(ConnectionService.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        scheduleRepository = mock(ScheduleRepository.class);
        jsonUtil = mock(JsonUtils.class);
        oscService = new OscService();
        oscService.setConnectionService(connectionService);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
        oscService.setScheduleRepository(scheduleRepository);
        oscService.setJsonUtil(jsonUtil);
    }
}