import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class OscServiceTest {

 @Autowired
 private OscService oscService;

 @MockBean
 private ConnectionService connectionService;

 @MockBean
 private ScheduleTaskRepository scheduleTaskRepository;

 @MockBean
 private ScheduleRepository scheduleRepository;

 @Autowired
 private ConnectionSessionFactory connectionSessionFactory;

 @Test
 public void testGetOscDatabaseInfo() {
 DatabaseEntity database = new DatabaseEntity();
 database.setDatabaseId(1L);
 when(databaseRepository.findById(1L)).thenReturn(Optional.of(database));
 OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
 assertTrue(result.getDatabaseId() == 1L);
 }

 @Test
 public void testGetOscDatabaseInfoNotFound() {
 when(databaseRepository.findById(1L)).thenReturn(Optional.empty());
 OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
 assertNull(result.getDatabaseId());
 }

 @Test
 public void testGetLockUserIsRequired() {
 ConnectionConfig connConfig = new ConnectionConfig();
 when(connectionService.getForConnectionSkipPermissionCheck(Mockito.anyLong())).thenReturn(connConfig);
 when(OscDBUserUtil.isLockUserRequired(Mockito.any(), Mockito.any())).thenReturn(true);
 assertTrue(oscService.getLockUserIsRequired(1L));
 }

 @Test
 public void testGetLockUserIsRequiredFalse() {
 ConnectionConfig connConfig = new ConnectionConfig();
 when(connectionService.getForConnectionSkipPermissionCheck(Mockito.anyLong())).thenReturn(connConfig);
 when(OscDBUserUtil.isLockUserRequired(Mockito.any(), Mockito.any())).thenReturn(false);
 assertFalse(oscService.getLockUserIsRequired(1L));
 }

 @Test
 public void testSwapTable() {
 // Given
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(1L);
 scheduleTask.setJobName("1");
 ScheduleEntity schedule = new ScheduleEntity();
 schedule.setId(1L);
 schedule.setJobParametersJson("{}");
 schedule.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 SwapTableType swapTableType = SwapTableType.MANUAL;
 oscParameters.setSwapTableType(swapTableType);
 schedule.setJobParametersJson(JsonUtils.toJson(oscParameters));
 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTask));
 // When
 OscSwapTableVO result = oscService.swapTable(1L);
 // Then
 assertEquals(1L, result.getScheduleTaskId());
 }

 @Test
 public void testSwapTableNotFound() {
 when(scheduleRepository.findById(1L)).thenReturn(Optional.empty());
 OscSwapTableVO result = oscService.swapTable(1L);
 assertNull(result.getScheduleTaskId());
 }

 @Test
 public void testSwapTableInvalidJobType() {
 ScheduleEntity schedule = new ScheduleEntity();
 schedule.setId(1L);
 schedule.setJobParametersJson("{}");
 schedule.setJobType(JobType.BACKUP);
 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
 assertThrows(RuntimeException.class, () -> oscService.swapTable(1L));
 }

 @Test
 public void testSwapTableInvalidSwapTableType() {
 ScheduleEntity schedule = new ScheduleEntity();
 schedule.setId(1L);
 schedule.setJobParametersJson("{}");
 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 SwapTableType swapTableType = SwapTableType.INTEGRATION;
 oscParameters.setSwapTableType(swapTableType);
 schedule.setJobParametersJson(JsonUtils.toJson(oscParameters));
 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
 assertThrows(RuntimeException.class, () -> oscService.swapTable(1L));
 }

 @Test
 public void testSwapTableInvalidManualSwapTableEnabled() {
 ScheduleEntity schedule = new ScheduleEntity();
 schedule.setId(1L);
 schedule.setJobParametersJson("{}");
 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(false);
 schedule.setJobParametersJson(JsonUtils.toJson(result));
 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(new ScheduleTaskEntity()));
 assertThrows(RuntimeException.class, () -> oscService.swapTable(1L));
 }

 @Test
 public void testSwapTableSuccess() {
 // Given
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(1L);
 scheduleTask.setJobName("1");
 ScheduleEntity schedule = new ScheduleEntity();
 schedule.setId(1L);
 schedule.setJobParametersJson("{}");
 schedule.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 SwapTableType swapTableType = SwapTableType.MANUAL;
 oscParameters.setSwapTableType(swapTableType);
 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(true);
 result.setManualSwapTableStarted(false);
 schedule.setJobParametersJson(JsonUtils.toJson(oscParameters, result));
 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTask));
 // When
 OscSwapTableVO resultOscSwapTable = oscService.swapTable(1L);
 // Then
 assertEquals(1L, resultOscSwapTable.getScheduleTaskId());
 verify(scheduleTaskRepository, times(1)).updateTaskResult(1L, JsonUtils.toJson(result));
 }

 @AfterAll
 public static void tearDown() {
 ConnectionSessionFactory connectionSessionFactory = Mockito.spy(ConnectionSessionFactory.class);
 ConnectionSession connectionSession = Mockito.spy(ConnectionSession.class);
 DefaultConnectSessionFactory defaultConnectSessionFactory = Mockito.spy(DefaultConnectSessionFactory.class);
 ConnectionConfig connectionConfig = Mockito.spy(ConnectionConfig.class);
 OscDBUserUtil oscDBUserUtil = Mockito.spy(OscDBUserUtil.class);
 try {
 connectionSessionFactory.setDefaultConnectSessionFactory(defaultConnectSessionFactory);
 defaultConnectSessionFactory.setConnectionSessionFactory(connectionSessionFactory);
 connectionSessionFactory.setConnectionSession(connectionSessionFactory.createSession());
 connectionSessionFactory.setOscDBUserUtil(oscDBUserUtil);
 } finally {
 Mockito.reset(connectionSessionFactory, connectionSession, defaultConnectSessionFactory, connectionConfig, oscDBUserUtil);
 }
 }
}