import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.testproperty.TestPropertySource;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.JobType;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleType;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobParameters;
import com.oceanbase.odc.service.session.SessionFactory;

@SpringBootTest
@SpringJUnitConfig(classes = { OscService.class })
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    @Autowired
    private DatabaseRepository databaseRepository;

    @Autowired
    private ConnectionService connectionService;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;

    @Test
    @DisplayName("Test getOscDatabaseInfo method with valid database ID")
    public void testGetOscDatabaseInfoWithValidDatabaseId() {
        Long databaseId = 1L;
        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(databaseId);
        Assertions.assertNotNull(oscDatabaseInfo);
        DatabaseEntity databaseEntity = Mockito.mock(DatabaseEntity.class);
        Mockito.when(databaseEntity.getId()).thenReturn(databaseId);
        Mockito.when(databaseRepository.findById(databaseId)).thenReturn(Optional.of(databaseEntity));
        Assertions.assertEquals(databaseId, oscDatabaseInfo.getDatabaseId());
        Assertions.assertFalse(oscDatabaseInfo.isLockDatabaseUserRequired());
    }

    @Test
    @DisplayName("Test getOscDatabaseInfo method with invalid database ID")
    public void testGetOscDatabaseInfoWithInvalidDatabaseId() {
        Long databaseId = -1L;
        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(databaseId);
        Assertions.assertNull(oscDatabaseInfo);
        DatabaseEntity databaseEntity = Mockito.mock(DatabaseEntity.class);
        Mockito.when(databaseRepository.findById(databaseId)).thenReturn(Optional.empty());
        Mockito.verify(databaseRepository, Mockito.times(1)).findById(databaseId);
    }

    @Test
    @DisplayName("Test swapTable method with valid schedule task ID")
    public void testSwapTableWithValidScheduleTaskId() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = Mockito.mock(ScheduleTaskEntity.class);
        ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
        OnlineSchemaChangeParameters oscParameters = Mockito.mock(OnlineSchemaChangeParameters.class);
        OnlineSchemaChangeScheduleTaskResult result = Mockito.mock(OnlineSchemaChangeScheduleTaskResult.class);
        JobParameters jobParameters = Mockito.mock(JobParameters.class);

        Mockito.when(scheduleTaskEntity.getId()).thenReturn(scheduleTaskId);
        Mockito.when(scheduleTaskEntity.getJobName()).thenReturn("1");
        Mockito.when(scheduleEntity.getId()).thenReturn(1L);
        Mockito.when(scheduleEntity.getJobType()).thenReturn(JobType.ONLINE_SCHEMA_CHANGE);
        Mockito.when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters));
        Mockito.when(jobParameters.getJobParameters()).thenReturn(new Object[] { oscParameters });
        Mockito.when(oscParameters.getSwapTableType()).thenReturn(SwapTableType.MANUAL);

        Mockito.when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        Mockito.when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        oscService.swapTable(scheduleTaskId);

        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).findById(scheduleTaskId);
        Mockito.verify(scheduleRepository, Mockito.times(1)).findById(1L);
        Mockito.verifyNoMoreInteractions(databaseRepository);
        Mockito.verifyNoMoreInteractions(connectionService);
        Mockito.verify(oscService, Mockito.times(1)).getLockUserIsRequired(Mockito.anyLong());
    }

    @Test
    @DisplayName("Test swapTable method with invalid schedule task ID")
    public void testSwapTableWithInvalidScheduleTaskId() {
        Long scheduleTaskId = -1L;
        ScheduleTaskEntity scheduleTaskEntity = Mockito.mock(ScheduleTaskEntity.class);
        ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);

        Mockito.when(scheduleTaskEntity.getId()).thenReturn(scheduleTaskId);
        Mockito.when(scheduleTaskEntity.getJobName()).thenReturn("1");
        Mockito.when(scheduleEntity.getId()).thenReturn(1L);

        Mockito.when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());
        Mockito.when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });

        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).findById(scheduleTaskId);
        Mockito.verify(scheduleRepository, Mockito.times(1)).findById(1L);
        Mockito.verifyNoMoreInteractions(databaseRepository);
        Mockito.verifyNoMoreInteractions(connectionService);
        Mockito.verifyNoMoreInteractions(oscService);
    }

    @Test
    @DisplayName("Test getLockUserIsRequired method with valid connection ID and MySql dialect")
    public void testGetLockUserIsRequiredWithValidConnectionIdAndMySqlDialect() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = Mockito.mock(ConnectionConfig.class);
        Mockito.when(decryptedConnConfig.getDialectType()).thenReturn("mysql");

        String version = "5.7.31";
        ConnectionSession connSession = Mockito.mock(ConnectionSession.class);
        Mockito.when(sessionFactory.generateSession()).thenReturn(connSession);
        Mockito.when(ConnectionSessionUtil.getVersion(connSession)).thenReturn(version);

        Assertions.assertTrue(oscService.getLockUserIsRequired(connectionId));
    }

    @Test
    @DisplayName("Test getLockUserIsRequired method with valid connection ID and Oracle dialect")
    public void testGetLockUserIsRequiredWithValidConnectionIdAndOracleDialect() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = Mockito.mock(ConnectionConfig.class);
        Mockito.when(decryptedConnConfig.getDialectType()).thenReturn("oracle");

        String version = "12.2.0.1.0";
        ConnectionSession connSession = Mockito.mock(ConnectionSession.class);
        Mockito.when(sessionFactory.generateSession()).thenReturn(connSession);
        Mockito.when(ConnectionSessionUtil.getVersion(connSession)).thenReturn(version);

        Assertions.assertFalse(oscService.getLockUserIsRequired(connectionId));
    }

    @Test
    @DisplayName("Test getLockUserIsRequired method with invalid connection ID")
    public void testGetLockUserIsRequiredWithInvalidConnectionId() {
        Long connectionId = -1L;
        ConnectionConfig decryptedConnConfig = Mockito.mock(ConnectionConfig.class);

        Mockito.when(decryptedConnConfig.getDialectType()).thenReturn("mysql");

        Assertions.assertFalse(oscService.getLockUserIsRequired(connectionId));
        Mockito.verify(connectionService, Mockito.times(1)).getForConnectionSkipPermissionCheck(connectionId);
        Mockito.verifyNoMoreInteractions(sessionFactory);
    }
}