import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

@SpringBootTest
@ActiveProfiles("test")
@JdbcTest
public class OscServiceTest {

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    @MockBean
    private ScheduleRepository scheduleRepository;

    @MockBean
    private ConnectionService connectionService;

    @Autowired
    private OscService oscService;

    @Test
    public void testGetOscDatabaseInfoSuccess() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        Mockito.when(databaseRepository.findById(ArgumentMatchers.eq(id)))
                .thenReturn(Optional.of(databaseEntity));
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        Assertions.assertEquals(id, result.getDatabaseId());
        Assertions.assertFalse(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfoNotFound() {
        Long id = 1L;
        Mockito.when(databaseRepository.findById(ArgumentMatchers.eq(id)))
                .thenReturn(Optional.empty());
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        Assertions.assertNull(result);
    }

    @Test
    public void testSwapTableSuccess() {
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        Mockito.when(scheduleRepository.findById(ArgumentMatchers.eq(1L)))
                .thenReturn(Optional.of(scheduleEntity));
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setScheduleTaskId(scheduleTaskId);
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));
        Mockito.when(scheduleTaskRepository.findById(ArgumentMatchers.eq(scheduleTaskId)))
                .thenReturn(Optional.of(scheduleTaskEntity));
        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);
        Assertions.assertEquals(scheduleTaskId, result.getScheduleTaskId());
    }

    @Test
    public void testSwapTableInvalidJobType() {
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.BACKUP);
        Mockito.when(scheduleRepository.findById(ArgumentMatchers.eq(1L)))
                .thenReturn(Optional.of(scheduleEntity));
        Assertions.assertThrows(RuntimeException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });
    }

    @Test
    public void testSwapTableInvalidSwapTableType() {
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobParametersJson("{\"swapTableType\": \"INVALID\"}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        Mockito.when(scheduleRepository.findById(ArgumentMatchers.eq(1L)))
                .thenReturn(Optional.of(scheduleEntity));
        Assertions.assertThrows(RuntimeException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });
    }

    @Test
    public void testSwapTableInvalidManualSwapTableStatus() {
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobParametersJson("{\"swapTableType\": \"MANUAL\"}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setScheduleTaskId(scheduleTaskId);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);
        result.setManualSwapTableStarted(true);
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(result));
        Mockito.when(scheduleRepository.findById(ArgumentMatchers.eq(1L)))
                .thenReturn(Optional.of(scheduleEntity));
        Mockito.when(scheduleTaskRepository.findById(ArgumentMatchers.eq(scheduleTaskId)))
                .thenReturn(Optional.of(scheduleTaskEntity));
        Assertions.assertThrows(RuntimeException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });
    }

    @Test
    public void testSwapTableSuccessWithLockUser() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        DatabaseEntity databaseEntity2 = new DatabaseEntity();
        databaseEntity2.setDatabaseId(2L);
        ConnectionEntity connectionEntity = new ConnectionEntity();
        connectionEntity.setConnectionId(1L);
        connectionEntity.setDialectType("OBDC");
        Mockito.when(databaseRepository.findById(ArgumentMatchers.eq(id)))
                .thenReturn(Optional.of(databaseEntity));
        Mockito.when(databaseRepository.findById(ArgumentMatchers.eq(2L)))
                .thenReturn(Optional.of(databaseEntity2));
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setConnectionId(connectionEntity.getConnectionId());
        decryptedConnConfig.setDialectType(connectionEntity.getDialectType());
        Mockito.when(connectionService.getForConnectionSkipPermissionCheck(connectionEntity.getConnectionId()))
                .thenReturn(decryptedConnConfig);
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        Assertions.assertEquals(id, result.getDatabaseId());
        Assertions.assertTrue(result.isLockDatabaseUserRequired());
    }
}