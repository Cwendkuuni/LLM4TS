import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
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
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@SpringJUnitConfig(classes = OscService.class)
@ActiveProfiles("test")

public class OscServiceTest {

 private DatabaseRepository databaseRepository;
 private ScheduleRepository scheduleRepository;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ConnectionService connectionService;
 private OscService oscService;
 private DefaultConnectSessionFactory sessionFactory;

 @Autowired
 public void setUpMocks(DatabaseRepository databaseRepository, ScheduleRepository scheduleRepository,
 ScheduleTaskRepository scheduleTaskRepository, ConnectionService connectionService) {
 this.databaseRepository = databaseRepository;
 this.scheduleRepository = scheduleRepository;
 this.scheduleTaskRepository = scheduleTaskRepository;
 this.connectionService = connectionService;
 this.oscService = new OscService();
 this.oscService.setDatabaseRepository(databaseRepository);
 this.oscService.setConnectionService(connectionService);
 this.oscService.setScheduleTaskRepository(scheduleTaskRepository);
 this.oscService.setScheduleRepository(scheduleRepository);
 this.sessionFactory = new DefaultConnectSessionFactory(new ConnectionConfig());
 }

 @BeforeEach
 public void setUp() {
 DatabaseEntity databaseEntity = new DatabaseEntity();
 databaseEntity.setDatabaseId(1L);
 databaseEntity.setConnectionId(1L);
 when(databaseRepository.findById(1L)).thenReturn(Optional.of(databaseEntity));
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobName("1");
 scheduleEntity.setJobParametersJson("{}");
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
 }

 @Test
 public void testGetOscDatabaseInfo() {
 OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
 assertAll(() -> assertEquals(1L, result.getDatabaseId()),
 () -> assertTrue(result.isLockDatabaseUserRequired()));
 }

 @Test
 public void testGetOscDatabaseInfo_invalidDatabaseId() {
 OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(2L);
 assertNull(result);
 }

 @Test
 public void testSwapTable() {
 OscSwapTableVO result = oscService.swapTable(1L);
 assertEquals(1L, result.getScheduleTaskId());
 }

 @Test
 public void testSwapTable_invalidScheduleTaskId() {
 OscSwapTableVO result = oscService.swapTable(2L);
 assertNull(result);
 }

 @Test
 public void testSwapTable_invalidSwapTableType() {
 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.BACKUP);
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobName("1");
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));
 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
 assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(1L));
 }

 @Test
 public void testSwapTable_invalidManualSwapTableEnabled() {
 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(false);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setResultJson(JsonUtils.toJson(result));
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
 assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(1L));
 }

 @Test
 public void testSwapTable_invalidJobType() {
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobName("1");
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
 scheduleEntity.setJobType(JobType.BACKUP);
 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
 assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(1L));
 }

 @Test
 public void testGetLockUserIsRequired() {
 ConnectionConfig connectionConfig = connectionService.getForConnectionSkipPermissionCheck(1L);
 assertTrue(oscService.getLockUserIsRequired(connectionConfig.getConnectionId()));
 }

 @Test
 public void testGetLockUserIsRequired_notLockUser() {
 ConnectionConfig connectionConfig = connectionService.getForConnectionSkipPermissionCheck(1L);
 connectionConfig.setDialectType("MySQL");
 assertFalse(oscService.getLockUserIsRequired(connectionConfig.getConnectionId()));
 }
}