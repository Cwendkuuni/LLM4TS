import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
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
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unchecked")
public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    private static ScheduleTaskRepository scheduleTaskRepository;
    private static ScheduleRepository scheduleRepository;
    private static ConnectionService connectionService;

    @BeforeAll
    public static void init() {
        MockitoAnnotations.initMocks(OscServiceTest.class);
        scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        scheduleRepository = Mockito.mock(ScheduleRepository.class);
        connectionService = Mockito.mock(ConnectionService.class);
        Mockito.when(scheduleTaskRepository.findById(Mockito.anyLong())).thenReturn(Mockito.mock(ScheduleTaskEntity.class));
        Mockito.when(scheduleRepository.findById(Mockito.anyLong())).thenReturn(Mockito.mock(ScheduleEntity.class));
        Mockito.when(connectionService.getForConnectionSkipPermissionCheck(Mockito.anyLong())).thenReturn(new ConnectionConfig());
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = UUID.randomUUID().longValue();
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setConnectionId(id);
        Optional<DatabaseEntity> databaseOptional = Optional.of(databaseEntity);

        OscLockDatabaseUserInfo expected = new OscLockDatabaseUserInfo();
        expected.setDatabaseId(id);
        expected.setLockDatabaseUserRequired(false);

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        Assertions.assertEquals(expected, result);
        Mockito.verify(databaseRepository, Mockito.times(1)).findById(id);
    }

    @Test
    public void testGetOscDatabaseInfoWithDatabaseNotFound() {
        Long id = UUID.randomUUID().longValue();

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        Assertions.assertEquals(new OscLockDatabaseUserInfo(), result);
        Mockito.verify(databaseRepository, Mockito.times(1)).findById(id);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = UUID.randomUUID().longValue();

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(UUID.randomUUID().longValue());
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        Mockito.when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(scheduleTask);
        Mockito.when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(scheduleEntity);

        oscService.swapTable(scheduleTaskId);

        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateTaskResult(scheduleTaskId, JsonUtils.toJson(result));
        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).findById(scheduleTaskId);
        Mockito.verify(scheduleRepository, Mockito.times(1)).findById(Long.parseLong(scheduleEntity.getJobName()));
    }

    @Test
    public void testSwapTableWithDatabaseNotFound() {
        Long scheduleTaskId = UUID.randomUUID().longValue();

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(UUID.randomUUID().longValue());
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        Mockito.when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(scheduleTask);
        Mockito.when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(null);

        Assertions.assertThrows(RuntimeException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });

        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).findById(scheduleTaskId);
        Mockito.verify(scheduleRepository, Mockito.times(1)).findById(Long.parseLong(scheduleEntity.getJobName()));
        Mockito.verifyNoInteractions(scheduleTaskRepository).updateTaskResult(scheduleTaskId, anyString());
    }

    @Test
    public void testGetLockUserIsRequiredWithObProxy() {
        Long connectionId = UUID.randomUUID().longValue();
        Boolean expected = false;

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(connectionId);
        Assertions.assertEquals(expected, result.isLockDatabaseUserRequired());

        Mockito.verify(connectionService, Mockito.times(1)).getForConnectionSkipPermissionCheck(connectionId);
    }

    @Test
    public void testGetLockUserIsRequiredWithObServer() {
        Long connectionId = UUID.randomUUID().longValue();
        Boolean expected = true;

        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        Mockito.when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(connectionConfig);
        Mockito.when(connectionConfig.getDialectType()).thenReturn("MySQL");

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(connectionId);
        Assertions.assertEquals(expected, result.isLockDatabaseUserRequired());

        Mockito.verify(connectionService, Mockito.times(1)).getForConnectionSkipPermissionCheck(connectionId);
        Mockito.verify(connectionConfig, Mockito.times(1)).getDialectType();
    }

}