import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SuppressWarnings("unchecked")
public class OscServiceTest {

 private static final Long ID = 1L;
 private static final Long SCHEDULE_TASK_ID = 1L;
 private static final Long SCHEDULE_ID = 1L;
 private static final JobType JOB_TYPE = JobType.ONLINE_SCHEMA_CHANGE_COMPLETE;
 private static final SwapTableType SWAP_TABLE_TYPE = SwapTableType.MANUAL;
 private static final boolean MANUAL_SWAP_TABLE_ENABLED = true;

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
 // Given
 DatabaseEntity databaseEntity = new DatabaseEntity();
 databaseEntity.setDatabaseId(ID);
 databaseEntity.setConnectionId(ID);
 when(databaseRepository.findById(ID)).thenReturn(Optional.of(databaseEntity));

 OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(ID);

 // Then
 assertNotNull(oscDatabaseInfo);
 assertEquals(ID, oscDatabaseInfo.getDatabaseId());
 assertFalse(oscDatabaseInfo.isLockDatabaseUserRequired());
 }

 @Test
 public void testGetOscDatabaseInfo_whenDatabaseDoesNotExist() {
 // Given
 when(databaseRepository.findById(ID)).thenReturn(Optional.empty());

 OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(ID);

 // Then
 assertNull(oscDatabaseInfo);
 }

 @Test
 public void testSwapTable() {
 // Given
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(SCHEDULE_TASK_ID);
 scheduleTaskEntity.setJobName(String.valueOf(SCHEDULE_ID));
 scheduleTaskEntity.setResultJson("{}");
 when(scheduleTaskRepository.findById(SCHEDULE_TASK_ID)).thenReturn(Optional.of(scheduleTaskEntity));

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(SCHEDULE_ID);
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
 scheduleEntity.setJobType(JOB_TYPE);
 when(scheduleRepository.findById(SCHEDULE_ID)).thenReturn(Optional.of(scheduleEntity));

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SWAP_TABLE_TYPE);

 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(MANUAL_SWAP_TABLE_ENABLED);
 result.setManualSwapTableStarted(false);

 when(connectionService.getForConnectionSkipPermissionCheck(ID)).thenReturn(null);

 // When
 oscService.swapTable(SCHEDULE_TASK_ID);

 // Then
 verify(scheduleTaskRepository, times(1)).updateTaskResult(SCHEDULE_TASK_ID,
 ArgumentMatchers.eq(JsonUtils.toJson(result)));

 OscSwapTableVO oscSwapTable = new OscSwapTableVO();
 oscSwapTable.setScheduleTaskId(SCHEDULE_TASK_ID);
 assertEquals(SCHEDULE_TASK_ID, oscSwapTable.getScheduleTaskId());
 }

 @Test
 public void testSwapTable_whenDatabaseLockUserIsRequired() {
 // Given
 DatabaseEntity databaseEntity = new DatabaseEntity();
 databaseEntity.setDatabaseId(ID);
 databaseEntity.setConnectionId(ID);
 databaseEntity.setLockDatabaseUserRequired(true);
 when(databaseRepository.findById(ID)).thenReturn(Optional.of(databaseEntity));

 ConnectionConfig decryptedConnConfig = new ConnectionConfig();
 decryptedConnConfig.setDialectType("dialectType");
 when(connectionService.getForConnectionSkipPermissionCheck(ID)).thenReturn(decryptedConnConfig);

 when(databaseRepository.findById(ID)).thenThrow(new RuntimeException());

 // When & Then
 assertThrows(RuntimeException.class, () -> oscService.swapTable(SCHEDULE_TASK_ID));
 }

 @Test
 public void testSwapTable_whenSwapTableIsNotManual() {
 // Given
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(SCHEDULE_TASK_ID);
 scheduleTaskEntity.setJobName(String.valueOf(SCHEDULE_ID));
 scheduleTaskEntity.setResultJson("{}");
 when(scheduleTaskRepository.findById(SCHEDULE_TASK_ID)).thenReturn(Optional.of(scheduleTaskEntity));

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(SCHEDULE_ID);
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
 scheduleEntity.setJobType(JOB_TYPE);
 when(scheduleRepository.findById(SCHEDULE_ID)).thenReturn(Optional.of(scheduleEntity));

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.AUTOMATIC);

 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(MANUAL_SWAP_TABLE_ENABLED);
 result.setManualSwapTableStarted(false);

 when(connectionService.getForConnectionSkipPermissionCheck(ID)).thenReturn(null);

 // When
 oscService.swapTable(SCHEDULE_TASK_ID);

 // Then
 verify(scheduleTaskRepository, never()).updateTaskResult(SCHEDULE_TASK_ID, ArgumentMatchers.anyString());
 }

 @Test
 public void testSwapTable_whenManualSwapTableIsNotEnabled() {
 // Given
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(SCHEDULE_TASK_ID);
 scheduleTaskEntity.setJobName(String.valueOf(SCHEDULE_ID));
 scheduleTaskEntity.setResultJson("{}");
 when(scheduleTaskRepository.findById(SCHEDULE_TASK_ID)).thenReturn(Optional.of(scheduleTaskEntity));

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(SCHEDULE_ID);
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
 scheduleEntity.setJobType(JOB_TYPE);
 when(scheduleRepository.findById(SCHEDULE_ID)).thenReturn(Optional.of(scheduleEntity));

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SWAP_TABLE_TYPE);

 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(false);
 result.setManualSwapTableStarted(false);

 when(connectionService.getForConnectionSkipPermissionCheck(ID)).thenReturn(null);

 // When
 oscService.swapTable(SCHEDULE_TASK_ID);

 // Then
 verify(scheduleTaskRepository, never()).updateTaskResult(SCHEDULE_TASK_ID, ArgumentMatchers.anyString());
 }
}