import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SuppressWarnings("unchecked")
public class OscServiceTest {

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
 public void testGetOscDatabaseInfo_existingDatabase() {
 // given
 Long id = 1L;
 DatabaseEntity databaseEntity = new DatabaseEntity();
 databaseEntity.setDatabaseId(id);
 when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

 // when
 OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);

 // then
 assertEquals(id, oscDatabase.getDatabaseId());
 assertTrue(oscDatabase.isLockDatabaseUserRequired());
 }

 @Test
 public void testGetOscDatabaseInfo_nonExistingDatabase() {
 // given
 Long id = 1L;
 when(databaseRepository.findById(id)).thenReturn(Optional.empty());

 // when
 OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);

 // then
 assertNull(oscDatabase);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSwapTable_nullScheduleTaskId() {
 // when
 oscService.swapTable(null);
 }

 @Test
 public void testSwapTable_existingScheduleTask() {
 // given
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(scheduleTaskId);
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 scheduleEntity.setJobParametersJson("{}");
 scheduleEntity.setJobName("1");
 scheduleTask.setJobId(scheduleEntity.getId());
 scheduleTask.setResultJson("{}");
 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

 // when
 oscService.swapTable(scheduleTaskId);

 // then
 verify(scheduleTaskRepository, times(1)).updateTaskResult(scheduleTaskId, anyString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSwapTable_nonExistingScheduleTask() {
 // given
 Long scheduleTaskId = 1L;
 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());

 // when
 oscService.swapTable(scheduleTaskId);
 }

 @Test
 public void testSwapTable_swappingTableTypeMismatch() {
 // given
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(scheduleTaskId);
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 scheduleEntity.setJobParametersJson("{\"swapTableType\":\"MANUAL\"}");
 scheduleEntity.setJobName("1");
 scheduleTask.setJobId(scheduleEntity.getId());
 scheduleTask.setResultJson("{}");
 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.AUTOMATIC);
 doReturn(oscParameters).when(oscService).parseOnlineSchemaChangeParameters(anyString());

 // when, then
 assertFalse(oscService.swapTable(scheduleTaskId));
 }

 @Test
 public void testSwapTable_manualSwapTableDisabled() {
 // given
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(scheduleTaskId);
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 scheduleEntity.setJobParametersJson("{\"swapTableType\":\"MANUAL\"}");
 scheduleEntity.setJobName("1");
 scheduleTask.setJobId(scheduleEntity.getId());
 scheduleTask.setResultJson("{\"manualSwapTableEnabled\":false,\"manualSwapTableStarted\":false}");
 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

 // when, then
 assertFalse(oscService.swapTable(scheduleTaskId));
 }

 @Test
 public void testGetLockUserIsRequired_dialectTypeObis() {
 // given
 Long connectionId = 1L;
 String version = "Obis";
 ConnectionConfig decryptedConnConfig = new ConnectionConfig();
 decryptedConnConfig.setConnectionId(connectionId);
 decryptedConnConfig.setDialectType(version);
 doReturn(decryptedConnConfig).when(connectionService).getForConnectionSkipPermissionCheck(connectionId);

 // when
 boolean lockUserIsRequired = oscService.getLockUserIsRequired(connectionId);

 // then
 assertTrue(lockUserIsRequired);
 }

 @Test
 public void testGetLockUserIsRequired_dialectTypeMySQL() {
 // given
 Long connectionId = 1L;
 String version = "MySQL";
 ConnectionConfig decryptedConnConfig = new ConnectionConfig();
 decryptedConnConfig.setConnectionId(connectionId);
 decryptedConnConfig.setDialectType(version);
 doReturn(decryptedConnConfig).when(connectionService).getForConnectionSkipPermissionCheck(connectionId);

 // when
 boolean lockUserIsRequired = oscService.getLockUserIsRequired(connectionId);

 // then
 assertFalse(lockUserIsRequired);
 }
}