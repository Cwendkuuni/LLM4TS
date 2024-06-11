import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

@Service
public class OscServiceTest {

 @InjectMocks
 private OscService oscService;
 @Mock
 private DatabaseRepository databaseRepository;
 @Mock
 private ConnectionService connectionService;
 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;
 @Mock
 private ScheduleRepository scheduleRepository;
 @Mock
 private WebApplicationContext applicationContext;

 @Before
 public void initMocks() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testGetOscDatabaseInfo() {
 // given
 Long id = 1L;
 DatabaseEntity databaseEntity = new DatabaseEntity();
 databaseEntity.setDatabaseId(id);
 DatabaseEntity database = Optional.of(databaseEntity);
 when(databaseRepository.findById(id)).thenReturn(database);

 // when
 OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

 // then
 assertEquals(id, result.getDatabaseId());
 assertEquals(false, result.isLockDatabaseUserRequired());

 // verify
 verify(databaseRepository, times(1)).findById(id);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetOscDatabaseInfoWithDatabaseNotFound() {
 // given
 Long id = 1L;
 when(databaseRepository.findById(id)).thenReturn(Optional.empty());

 // when & then
 oscService.getOscDatabaseInfo(id);
 }

 @Test
 @Transactional
 public void testSwapTable() {
 // given
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(scheduleTaskId);
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 scheduleEntity.setJobParametersJson("{}");
 ScheduleTaskEntity savedScheduleTask = new ScheduleTaskEntity();
 savedScheduleTask.setId(scheduleTaskId);
 savedScheduleTask.setResultJson("{}");
 savedScheduleTask.setJobName("1");
 savedScheduleTask.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
 when(databaseRepository.findById(anyLong())).thenAnswer(invocation -> Optional.empty());
 when(scheduleTaskRepository.save(any(ScheduleTaskEntity.class))).thenReturn(savedScheduleTask);

 // when
 OscSwapTableVO result = oscService.swapTable(scheduleTaskId);

 // then
 assertNull(result);

 // verify
 verify(databaseRepository, never()).findById(anyLong());
 verify(scheduleRepository, times(1)).findById(1L);
 verify(scheduleTaskRepository, times(1)).save(any(ScheduleTaskEntity.class));
 }

 @Test(expected = IllegalArgumentException.class)
 @Transactional
 public void testSwapTableWithJobTypeNotOnlineSchemaChange() {
 // given
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(scheduleTaskId);
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 scheduleEntity.setJobParametersJson("{}");
 scheduleEntity.setJobType(JobType.BACKUP);

 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
 when(databaseRepository.findById(anyLong())).thenAnswer(invocation -> Optional.empty());
 when(scheduleTaskRepository.save(any(ScheduleTaskEntity.class))).thenReturn(new ScheduleTaskEntity());

 // when & then
 oscService.swapTable(scheduleTaskId);
 }

 @Test(expected = IllegalArgumentException.class)
 @Transactional
 public void testSwapTableWithManualSwapTableNotEnabled() {
 // given
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(scheduleTaskId);
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 scheduleEntity.setJobParametersJson("{}");
 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(false);
 result.setManualSwapTableStarted(false);
 scheduleEntity.setResultJson(JsonUtils.toJson(result));

 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
 when(databaseRepository.findById(anyLong())).thenAnswer(invocation -> Optional.empty());
 when(scheduleTaskRepository.save(any(ScheduleTaskEntity.class))).thenReturn(new ScheduleTaskEntity());

 // when & then
 oscService.swapTable(scheduleTaskId);
 }

 @Test(expected = IllegalArgumentException.class)
 @Transactional
 public void testSwapTableWithManualSwapTableStartedButNotEnabled() {
 // given
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(scheduleTaskId);
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 scheduleEntity.setJobParametersJson("{}");
 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(true);
 result.setManualSwapTableStarted(true);
 scheduleEntity.setResultJson(JsonUtils.toJson(result));

 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
 when(databaseRepository.findById(anyLong())).thenAnswer(invocation -> Optional.empty());
 when(scheduleTaskRepository.save(any(ScheduleTaskEntity.class))).thenReturn(new ScheduleTaskEntity());

 // when & then
 oscService.swapTable(scheduleTaskId);
 }

 @Test
 @Transactional
 public void testGetLockUserIsRequired() {
 // given
 Long connectionId = 1L;
 ConnectionConfig decryptedConnConfig = new ConnectionConfig();
 decryptedConnConfig.setDialectType("MySQL");
 ConnectionSessionFactory factory = mock(ConnectionSessionFactory.class);
 ConnectionSession connSession = mock(ConnectionSession.class);
 String version = "5.6";

 when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);
 when(factory.generateSession()).thenReturn(connSession);
 when(ConnectionSessionUtil.getVersion(connSession)).thenReturn(version);

 // when
 boolean result = oscService.getLockUserIsRequired(connectionId);

 // then
 assertTrue(result);

 // verify
 verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(connectionId);
 verify(factory, times(1)).generateSession();
 verify(ConnectionSessionUtil, times(1)).getVersion(connSession);
 }

 @Test
 @Transactional
 public void testGetLockUserIsRequiredWithConnectionNotOpen() {
 // given
 Long connectionId = 1L;
 ConnectionConfig decryptedConnConfig = new ConnectionConfig();
 decryptedConnConfig.setDialectType("MySQL");
 ConnectionSessionFactory factory = mock(ConnectionSessionFactory.class);
 ConnectionSession connSession = mock(ConnectionSession.class);
 String version = null;

 when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);
 when(factory.generateSession()).thenThrow(new RuntimeException());

 // when
 boolean result = oscService.getLockUserIsRequired(connectionId);

 // then
 assertFalse(result);

 // verify
 verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(connectionId);
 verify(factory, times(1)).generateSession();
 }

 @Test
 @Transactional
 public void testGetLockUserIsRequiredWithMySQLVersionLessThan5_6() {
 // given
 Long connectionId = 1L;
 ConnectionConfig decryptedConnConfig = new ConnectionConfig();
 decryptedConnConfig.setDialectType("MySQL");
 ConnectionSessionFactory factory = mock(ConnectionSessionFactory.class);
 ConnectionSession connSession = mock(ConnectionSession.class);
 String version = "5.5";

 when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);
 when(factory.generateSession()).thenReturn(connSession);
 when(ConnectionSessionUtil.getVersion(connSession)).thenReturn(version);

 // when
 boolean result = oscService.getLockUserIsRequired(connectionId);

 // then
 assertFalse(result);

 // verify
 verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(connectionId);
 verify(factory, times(1)).generateSession();
 verify(ConnectionSessionUtil, times(1)).getVersion(connSession);
 }
}