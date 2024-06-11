import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfigDTO;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SuppressWarnings("unchecked")
public class OscServiceTest {

 @Mock
 private DatabaseRepository databaseRepository;
 @Mock
 private ConnectionService connectionService;
 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;
 @Mock
 private ScheduleRepository scheduleRepository;

 @InjectMocks
 private OscService oscService;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testGetOscDatabaseInfoWithValidId() {
 Long id = 1L;
 DatabaseEntity databaseEntity = new DatabaseEntity();
 databaseEntity.setDatabaseId(id);
 when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

 OscLockDatabaseUserInfo expectedOscDatabaseInfo = new OscLockDatabaseUserInfo();
 expectedOscDatabaseInfo.setDatabaseId(id);
 expectedOscDatabaseInfo.setLockDatabaseUserRequired(false);

 OscLockDatabaseUserInfo actualOscDatabaseInfo = oscService.getOscDatabaseInfo(id);

 assertEquals(expectedOscDatabaseInfo, actualOscDatabaseInfo);

 verify(databaseRepository, times(1)).findById(id);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetOscDatabaseInfoWithInvalidId() {
 Long id = 1L;
 when(databaseRepository.findById(id)).thenReturn(Optional.empty());

 oscService.getOscDatabaseInfo(id);
 }

 @Test
 public void testSwapTableWithValidScheduleTaskId() {
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(scheduleTaskId);
 scheduleTaskEntity.setJobName("scheduleId");

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
 scheduleEntity.setJobParametersJson("{\"swpTableType\":\"MANUAL\"}");

 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);

 OnlineSchemaChangeScheduleTaskResult expectedResult = new OnlineSchemaChangeScheduleTaskResult();
 expectedResult.setManualSwapTableEnabled(false);
 expectedResult.setManualSwapTableStarted(true);

 oscService.swapTable(scheduleTaskId);

 verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
 verify(scheduleRepository, times(1)).findById(1L);
 verify(scheduleTaskRepository, times(1)).updateTaskResult(scheduleTaskId, eq("{\"manualSwapTableEnabled\":false,\"manualSwapTableStarted\":true}"));

 assertEquals(expectedResult, JsonUtils.fromJson(scheduleTaskEntity.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSwapTableWithInvalidScheduleTaskId() {
 Long scheduleTaskId = 1L;
 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());

 oscService.swapTable(scheduleTaskId);
 }

 @Test
 public void testGetLockUserIsRequiredWithMySQL() {
 Long connectionId = 1L;
 ConnectionConfig connectionConfig = new ConnectionConfigDTO();
 connectionConfig.setDialectType("MySQL");
 ConnectionConfig decryptedConnConfig = connectionConfig;
 String version = "5.7.23";

 when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
 .thenReturn(decryptedConnConfig);
 when(decryptedConnConfig.getDialectType()).thenReturn("MySQL");

 ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
 ConnectionSession connSession = mock(ConnectionSession.class);
 when(factory.generateSession()).thenReturn(connSession);
 when(ConnectionSessionUtil.getVersion(connSession)).thenReturn(version);

 assertTrue(oscService.getLockUserIsRequired(connectionId));

 verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(connectionId);
 verify(decryptedConnConfig, times(1)).getDialectType();
 verify(factory, times(1)).generateSession();
 verify(ConnectionSessionUtil, times(1)).getVersion(connSession);
 }

 @Test
 public void testGetLockUserIsRequiredWithObServer() {
 Long connectionId = 1L;
 ConnectionConfig connectionConfig = new ConnectionConfigDTO();
 connectionConfig.setDialectType("ObServer");
 ConnectionConfig decryptedConnConfig = connectionConfig;
 String version = "3.0.5";

 when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
 .thenReturn(decryptedConnConfig);
 when(decryptedConnConfig.getDialectType()).thenReturn("ObServer");

 ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
 ConnectionSession connSession = mock(ConnectionSession.class);
 when(factory.generateSession()).thenReturn(connSession);
 when(ConnectionSessionUtil.getVersion(connSession)).thenReturn(version);

 assertFalse(oscService.getLockUserIsRequired(connectionId));

 verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(connectionId);
 verify(decryptedConnConfig, times(1)).getDialectType();
 verify(factory, times(1)).generateSession();
 verify(ConnectionSessionUtil, times(1)).getVersion(connSession);
 }
}