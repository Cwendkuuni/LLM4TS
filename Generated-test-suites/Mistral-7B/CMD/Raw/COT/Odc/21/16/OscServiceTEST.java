import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;

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
 public void testGetOscDatabaseInfo() {
 Long id = 1L;
 DatabaseEntity databaseEntity = new DatabaseEntity();
 databaseEntity.setDatabaseId(id);
 when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));
 OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);
 verify(databaseRepository).findById(id);
 assertTrue(oscDatabase.getDatabaseId() == id);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetOscDatabaseInfoWithEmptyDatabase() {
 Long id = 1L;
 when(databaseRepository.findById(id)).thenReturn(Optional.empty());
 oscService.getOscDatabaseInfo(id);
 }

 @Test
 public void testSwapTable() {
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(scheduleTaskId);
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 scheduleEntity.setJobName("scheduleId");
 scheduleEntity.setJobParametersJson("{\"SwapTableType\":\"MANUAL\"}");
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);
 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(true);
 result.setManualSwapTableStarted(false);
 when(jsonUtils().fromJson(scheduleEntity.getJobParametersJson(), OnlineSchemaChangeParameters.class))
 .thenReturn(oscParameters);
 when(jsonUtils().fromJson(scheduleTask.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class))
 .thenReturn(result);
 OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);
 verify(scheduleTaskRepository).findById(scheduleTaskId);
 verify(scheduleRepository).findById(1L);
 verify(jsonUtils()).fromJson(scheduleEntity.getJobParametersJson(), OnlineSchemaChangeParameters.class);
 verify(jsonUtils()).fromJson(scheduleTask.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class);
 assertTrue(oscSwapTable.getScheduleTaskId() == scheduleTaskId);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSwapTableWithInvalidJobType() {
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(scheduleTaskId);
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 scheduleEntity.setJobName("scheduleId");
 scheduleEntity.setJobParametersJson("{\"SwapTableType\":\"MANUAL\"}");
 scheduleEntity.setJobType(JobType.BACKUP);
 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);
 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(true);
 result.setManualSwapTableStarted(false);
 when(jsonUtils().fromJson(scheduleEntity.getJobParametersJson(), OnlineSchemaChangeParameters.class))
 .thenReturn(oscParameters);
 when(jsonUtils().fromJson(scheduleTask.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class))
 .thenReturn(result);
 oscService.swapTable(scheduleTaskId);
 }

 @Test
 public void testGetLockUserIsRequired() {
 Long connectionId = 1L;
 ConnectionConfig decryptedConnConfig = new ConnectionConfig();
 decryptedConnConfig.setDialectType("MySQL");
 when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
 .thenReturn(decryptedConnConfig);
 ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
 ConnectionSession connSession = mock(ConnectionSession.class);
 when(factory.generateSession()).thenReturn(connSession);
 String version = "5.7.23";
 when(ConnectionSessionUtil.getVersion(connSession)).thenReturn(version);
 assertTrue(oscService.getLockUserIsRequired(connectionId));
 }

 @Test
 public void testGetLockUserIsRequiredWithEmptyVersion() {
 Long connectionId = 1L;
 ConnectionConfig decryptedConnConfig = new ConnectionConfig();
 decryptedConnConfig.setDialectType("MySQL");
 when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
 .thenReturn(decryptedConnConfig);
 ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
 ConnectionSession connSession = mock(ConnectionSession.class);
 when(factory.generateSession()).thenThrow(new Exception());
 when(ConnectionSessionUtil.getVersion(connSession)).thenReturn(null);
 assertFalse(oscService.getLockUserIsRequired(connectionId));
 }

}