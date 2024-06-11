import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
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

@SuppressWarnings("unchecked")
public class OscServiceTest {

 private DatabaseRepository databaseRepository;
 private ConnectionService connectionService;
 private ScheduleRepository scheduleRepository;
 private ScheduleTaskRepository scheduleTaskRepository;
 private OscService oscService;

 @Before
 public void setup() {
 databaseRepository = mock(DatabaseRepository.class);
 connectionService = mock(ConnectionService.class);
 scheduleRepository = mock(ScheduleRepository.class);
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 oscService = new OscService();
 oscService.setDatabaseRepository(databaseRepository);
 oscService.setConnectionService(connectionService);
 oscService.setScheduleRepository(scheduleRepository);
 oscService.setScheduleTaskRepository(scheduleTaskRepository);
 }

 @Test
 public void testGetOscDatabaseInfo() {
 Long id = 1L;
 DatabaseEntity databaseEntity = new DatabaseEntity();
 databaseEntity.setDatabaseId(id);
 Optional<DatabaseEntity> database = Optional.of(databaseEntity);
 when(databaseRepository.findById(id)).thenReturn(database);

 OscLockDatabaseUserInfo expectedOscDatabaseInfo = new OscLockDatabaseUserInfo();
 expectedOscDatabaseInfo.setDatabaseId(id);
 expectedOscDatabaseInfo.setLockDatabaseUserRequired(false);

 OscLockDatabaseUserInfo actualOscDatabaseInfo = oscService.getOscDatabaseInfo(id);
 assertEquals(expectedOscDatabaseInfo, actualOscDatabaseInfo);
 }

 @Test
 public void testGetOscDatabaseInfoWhenDatabaseNotFound() {
 Long id = 1L;
 when(databaseRepository.findById(id)).thenReturn(Optional.empty());

 OscLockDatabaseUserInfo actualOscDatabaseInfo = oscService.getOscDatabaseInfo(id);
 assertNull(actualOscDatabaseInfo);
 }

 @Test
 public void testSwapTable() {
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(scheduleTaskId);
 scheduleTask.setJobName("scheduleId");

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(Long.parseLong("scheduleId"));
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

 when(scheduleRepository.findById("scheduleId")).thenReturn(Optional.of(scheduleEntity));
 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);

 OnlineSchemaChangeScheduleTaskResult expectedResult = new OnlineSchemaChangeScheduleTaskResult();
 expectedResult.setManualSwapTableEnabled(true);
 expectedResult.setManualSwapTableStarted(false);

 when(databaseRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(new DatabaseEntity()));

 oscService.swapTable(scheduleTaskId);
 verify(scheduleTaskRepository, times(1)).updateTaskResult(scheduleTaskId, ArgumentMatchers.anyString());
 verify(scheduleTaskRepository, times(1)).updateTaskResult(scheduleTaskId, ArgumentMatchers.eq(JsonUtils.toJson(expectedResult)));

 OscSwapTableVO actualOscSwapTable = oscService.swapTable(scheduleTaskId);
 assertNotNull(actualOscSwapTable);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSwapTableWhenJobTypeNotOnlineSchemaChange() {
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(scheduleTaskId);
 scheduleTask.setJobName("scheduleId");

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(Long.parseLong("scheduleId"));
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
 scheduleEntity.setJobType(JobType.ANALYSIS);

 when(scheduleRepository.findById("scheduleId")).thenReturn(Optional.of(scheduleEntity));
 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

 oscService.swapTable(scheduleTaskId);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSwapTableWhenSwapTableTypeNotManual() {
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(scheduleTaskId);
 scheduleTask.setJobName("scheduleId");

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(Long.parseLong("scheduleId"));
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.AUTO);

 when(scheduleRepository.findById("scheduleId")).thenReturn(Optional.of(scheduleEntity));
 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

 oscService.swapTable(scheduleTaskId);
 }

 @Test
 public void testSwapTableWhenManualSwapTableNotEnabled() {
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(scheduleTaskId);
 scheduleTask.setJobName("scheduleId");

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(Long.parseLong("scheduleId"));
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

 OnlineSchemaChangeScheduleTaskResult expectedResult = new OnlineSchemaChangeScheduleTaskResult();
 expectedResult.setManualSwapTableEnabled(false);
 expectedResult.setManualSwapTableStarted(false);

 when(scheduleRepository.findById("scheduleId")).thenReturn(Optional.of(scheduleEntity));
 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

 when(databaseRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(new DatabaseEntity()));

 oscService.swapTable(scheduleTaskId);
 verify(scheduleTaskRepository, times(1)).updateTaskResult(scheduleTaskId, ArgumentMatchers.anyString());

 OnlineSchemaChangeScheduleTaskResult actualResult = JsonUtils.fromJson(scheduleTask.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class);
 assertFalse(actualResult.isManualSwapTableEnabled());
 }

 @Test
 public void testGetLockUserIsRequired() {
 Long connectionId = 1L;
 ConnectionConfig connectionConfig = new ConnectionConfig();
 when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(connectionConfig);

 assertTrue(oscService.getLockUserIsRequired(connectionId));
 }

 @Test
 public void testGetLockUserIsRequiredWhenGetConnectionFailed() {
 Long connectionId = 1L;
 when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenThrow(new RuntimeException());

 assertFalse(oscService.getLockUserIsRequired(connectionId));
 }

 @Test
 public void testGetLockUserIsRequiredWhenDatabaseVersionIsOlderThan5_1_9() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType("mysql");

 ConnectionSessionFactory factory = new DefaultConnectSessionFactory(connectionConfig);
 ConnectionSession connSession = null;
 when(factory.generateSession()).thenThrow(new RuntimeException());

 assertFalse(oscService.getLockUserIsRequired(connectionConfig.getId()));
 }

 @Test
 public void testGetLockUserIsRequiredWhenDatabaseVersionIs5_1_9OrLater() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType("mysql");

 ConnectionSessionFactory factory = new DefaultConnectSessionFactory(connectionConfig);
 ConnectionSession connSession = mock(ConnectionSession.class);
 when(factory.generateSession()).thenReturn(connSession);
 when(ConnectionSessionUtil.getVersion(connSession)).thenReturn("5.1.9");

 assertTrue(oscService.getLockUserIsRequired(connectionConfig.getId()));
 }
}