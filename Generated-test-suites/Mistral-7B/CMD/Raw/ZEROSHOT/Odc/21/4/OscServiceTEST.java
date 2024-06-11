import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.core.shared.PreConditions;
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
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.OscServiceTest;

public class OscServiceTest {

 private DatabaseRepository databaseRepository;
 private ScheduleRepository scheduleRepository;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ConnectionService connectionService;
 private ObjectMapper objectMapper;
 private OscService oscService;

 @Before
 public void setUp() {
 databaseRepository = Mockito.mock(DatabaseRepository.class);
 scheduleRepository = Mockito.mock(ScheduleRepository.class);
 scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
 connectionService = Mockito.mock(ConnectionService.class);
 objectMapper = new ObjectMapper();
 oscService = new OscService();
 oscService.setDatabaseRepository(databaseRepository);
 oscService.setScheduleRepository(scheduleRepository);
 oscService.setConnectionService(connectionService);
 oscService.setScheduleTaskRepository(scheduleTaskRepository);
 }

 @After
 public void tearDown() {
 oscService = null;
 databaseRepository = null;
 scheduleRepository = null;
 scheduleTaskRepository = null;
 connectionService = null;
 objectMapper = null;
 }

 @Test
 public void testGetOscDatabaseInfo() {
 Long id = 1L;
 DatabaseEntity databaseEntity = new DatabaseEntity();
 databaseEntity.setDatabaseId(id);
 databaseEntity.setConnectionId(1L);
 when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));
 OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(id);
 assertTrue(oscDatabaseInfo.isPresent());
 assertEquals(id, oscDatabaseInfo.get().getDatabaseId());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetOscDatabaseInfoWhenDatabaseNotPresent() {
 Long id = 1L;
 when(databaseRepository.findById(id)).thenReturn(Optional.empty());
 oscService.getOscDatabaseInfo(id);
 }

 @Test
 public void testSwapTable() throws Exception {
 Long scheduleTaskId = 1L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobId(scheduleTaskId);
 scheduleEntity.setJobName("testJob");
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()).getBytes());
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE.getCode());
 when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleEntity));
 OnlineSchemaChangeParameters oscParameters = JsonUtils.fromJson(scheduleEntity.getJobParametersJson(),
 OnlineSchemaChangeParameters.class);
 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(true);
 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(new ScheduleTaskEntity()));
 when(scheduleTaskRepository.updateTaskResult(scheduleTaskId, anyString())).thenAnswer(i -> {
 // update task result
 return null;
 });
 oscService.swapTable(scheduleTaskId);
 verify(scheduleTaskRepository, times(1)).updateTaskResult(scheduleTaskId, anyString());
 }

 @Test(expected = IllegalStateException.class)
 public void testSwapTableWhenScheduleTaskNotPresent() {
 Long scheduleTaskId = 1L;
 when(scheduleRepository.findById(Long.parseLong(scheduleTaskId.toString()))).thenReturn(Optional.empty());
 oscService.swapTable(scheduleTaskId);
 }

 @Test
 public void testSwapTableWhenScheduleJobNotOnlineSchemaChange() {
 Long scheduleTaskId = 1L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobId(scheduleTaskId);
 scheduleEntity.setJobName("testJob");
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()).getBytes());
 scheduleEntity.setJobType(JobType.DATA_BACKUP.getCode());
 when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleEntity));
 oscService.swapTable(scheduleTaskId);
 }

 @Test
 public void testSwapTableWhenManualSwapTableDisabled() throws Exception {
 Long scheduleTaskId = 1L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobId(scheduleTaskId);
 scheduleEntity.setJobName("testJob");
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()).getBytes());
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE.getCode());
 OnlineSchemaChangeParameters oscParameters = JsonUtils.fromJson(scheduleEntity.getJobParametersJson(),
 OnlineSchemaChangeParameters.class);
 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(false);
 when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleEntity));
 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(new ScheduleTaskEntity()));
 when(scheduleTaskRepository.updateTaskResult(scheduleTaskId, anyString())).thenAnswer(i -> {
 // update task result
 return null;
 });
 oscService.swapTable(scheduleTaskId);
 assertFalse(result.isManualSwapTableEnabled());
 }

 @Test
 public void testGetLockUserIsRequired() {
 Long connectionId = 1L;
 ConnectionConfig decryptedConnConfig = new ConnectionConfig();
 decryptedConnConfig.setDialectType("MySQL");
 when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
 .thenReturn(decryptedConnConfig);
 ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
 ConnectionSession connSession = Mockito.mock(ConnectionSession.class);
 String version = "5.5.5";
 when(factory.generateSession()).thenReturn(connSession);
 when(ConnectionSessionUtil.getVersion(connSession)).thenReturn(version);
 assertTrue(oscService.getLockUserIsRequired(connectionId));
 }

 @Test
 public void testGetLockUserIsRequiredWhenConnectionNotPresent() {
 Long connectionId = 1L;
 when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(null);
 assertFalse(oscService.getLockUserIsRequired(connectionId));
 }
}