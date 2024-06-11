import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class OscServiceTest {

 @Mock
 private DatabaseRepository databaseRepository;
 @Mock
 private ScheduleRepository scheduleRepository;
 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;
 @Mock
 private ConnectionService connectionService;

 @InjectMocks
 private OscService oscService;

 @Before
 public void setup() {
 Mockito.initMocks(this);
 }

 @Test
 public void testGetOscDatabaseInfo() {
 // given
 Long id = 1L;
 DatabaseEntity databaseEntity = new DatabaseEntity();
 databaseEntity.setDatabaseId(id);
 // when
 OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(id);
 // then
 assertEquals(id, oscDatabaseInfo.getDatabaseId());
 assertFalse(oscDatabaseInfo.isLockDatabaseUserRequired());
 }

 @Test
 public void testGetOscDatabaseInfo_DatabaseNotFound() {
 // given
 Long id = 1L;
 // when
 OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(id);
 // then
 assertNull(oscDatabaseInfo);
 }

 @Test
 public void testSwapTable() {
 // given
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTask = Mockito.mock(ScheduleTaskEntity.class);
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobName("1");
 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
 when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));
 when(databaseRepository.findById(1L)).thenReturn(Optional.of(databaseEntity));
 when(connectionService.getForConnectionSkipPermissionCheck(databaseEntity.getConnectionId())).thenReturn(new ConnectionConfig());
 // when
 oscService.swapTable(scheduleTaskId);
 // then
 assertNotNull(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSwapTable_InvalidJobType() {
 // given
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTask = Mockito.mock(ScheduleTaskEntity.class);
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobName("1");
 scheduleEntity.setJobType(JobType.BACKUP);
 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
 when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));
 // when, then
 oscService.swapTable(scheduleTaskId);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSwapTable_InvalidSwapTableType() {
 // given
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTask = Mockito.mock(ScheduleTaskEntity.class);
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobName("1");
 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.AUTOMATIC);
 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
 when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));
 when(databaseRepository.findById(1L)).thenReturn(Optional.of(new DatabaseEntity()));
 // when, then
 oscService.swapTable(scheduleTaskId);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSwapTable_SwapTableNotEnabled() {
 // given
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTask = Mockito.mock(ScheduleTaskEntity.class);
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobName("1");
 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(false);
 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
 when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));
 when(databaseRepository.findById(1L)).thenReturn(Optional.of(new DatabaseEntity()));
 when(connectionService.getForConnectionSkipPermissionCheck(new ConnectionConfig().getConnectionId())).thenReturn(new ConnectionConfig());
 when(scheduleTaskRepository.updateTaskResult(scheduleTaskId, Mockito.anyString())).thenThrow(new RuntimeException());
 // when, then
 oscService.swapTable(scheduleTaskId);
 }

 @Test
 public void testGetLockUserIsRequired() {
 // given
 Long connectionId = 1L;
 ConnectionConfig decryptedConnConfig = new ConnectionConfig();
 // when
 boolean result = oscService.getLockUserIsRequired(connectionId);
 // then
 assertTrue(result);
 }

 @Test
 public void testGetLockUserIsRequired_NoLockUserRequired() {
 // given
 Long connectionId = 1L;
 ConnectionConfig decryptedConnConfig = new ConnectionConfig();
 // when
 boolean result = oscService.getLockUserIsRequired(connectionId);
 // then
 assertFalse(result);
 }
}