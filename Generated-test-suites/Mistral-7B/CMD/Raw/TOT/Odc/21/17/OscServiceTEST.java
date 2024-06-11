import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

@SpringBootTest
public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    @MockBean
    private DatabaseRepository databaseRepository;

    @MockBean
    private ConnectionService connectionService;

    @MockBean
    private ScheduleRepository scheduleRepository;

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo_databaseNotFound() {
        Mockito.when(databaseRepository.findById(Mockito.anyLong()))
                .thenAnswer(new Answer<Optional<DatabaseEntity>>() {
                    @Override
                    public Optional<DatabaseEntity> answer(InvocationOnMock invocation) {
                        return Optional.empty();
                    }
                });

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
        Assertions.assertNull(result);
    }

    @Test
    public void testGetOscDatabaseInfo_databaseFound() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        Mockito.when(databaseRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
        Assertions.assertEquals(1L, result.getDatabaseId());
        Assertions.assertTrue(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable_scheduleTaskNotFound() {
        Mockito.when(scheduleTaskRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Executable testMethod = () -> oscService.swapTable(1L);
        Assertions.assertThrows(IllegalArgumentException.class, testMethod);
    }

    @Test
    public void testSwapTable_scheduleTaskFound() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("test");
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        Mockito.when(scheduleRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(scheduleEntity));
        Mockito.when(scheduleTaskRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(scheduleTask));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        Mockito.when(scheduleTask.getResultJson())
                .thenReturn(JsonUtils.toJson(result));

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        Assertions.assertEquals(1L, oscSwapTable.getScheduleTaskId());
    }

    @Test
    public void testSwapTable_scheduleTaskAlreadyCompleted() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("test");
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        Mockito.when(scheduleRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(scheduleEntity));
        Mockito.when(scheduleTaskRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(scheduleTask));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(true);

        Mockito.when(scheduleTask.getResultJson())
                .thenReturn(JsonUtils.toJson(result));

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        Assertions.assertEquals(1L, oscSwapTable.getScheduleTaskId());
        Assertions.assertThrows(IllegalStateException.class, () -> oscService.swapTable(1L));
    }

    @Test
    public void testGetLockUserIsRequired_connectionNotFound() {
        Mockito.when(connectionService.getForConnectionSkipPermissionCheck(Mockito.anyLong()))
                .thenThrow(new RuntimeException());

        Assertions.assertFalse(oscService.getLockUserIsRequired(1L));
    }

    @Test
    public void testGetLockUserIsRequired_connectionFound() {
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("MySql");

        DefaultConnectSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
        ConnectionSession connSession = factory.generateSession();
        String version = "5.7.21";

        Mockito.when(connectionService.getForConnectionSkipPermissionCheck(Mockito.anyLong()))
                .thenReturn(decryptedConnConfig);
        Mockito.when(ConnectionSessionUtil.getVersion(Mockito.any()))
                .thenReturn(version);

        Assertions.assertTrue(oscService.getLockUserIsRequired(1L));
    }

}