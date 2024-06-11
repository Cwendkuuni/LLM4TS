import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OscServiceTest {

 private OscService oscService;
 private ScheduleTaskRepository scheduleTaskRepository;

 @BeforeAll
 void setUp() {
 MockitoAnnotations.initMocks(this);
 oscService = new OscService();
 ReflectionTestUtils.setFieldValue(oscService, "databaseRepository", mock(DatabaseRepository.class));
 ReflectionTestUtils.setFieldValue(oscService, "scheduleTaskRepository", scheduleTaskRepository = mock(ScheduleTaskRepository.class));
 }

 @Test
 void testGetOscDatabaseInfo_validDatabaseId() {
 DatabaseEntity database = new DatabaseEntity();
 database.setDatabaseId(1L);
 when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(database));
 OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
 assertNotNull(result);
 }

 @Test
 void testGetOscDatabaseInfo_invalidDatabaseId() {
 when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());
 OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
 assertNull(result);
 }

 @Test
 void testSwapTable_validScheduleTaskId() {
 ScheduleEntity schedule = new ScheduleEntity();
 schedule.setJobName("1");
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setScheduleId(1L);
 scheduleTask.setResultJson("{}");
 scheduleTask.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
 schedule.setId(1L);
 schedule.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
 schedule.setEnabled(true);
 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));
 OscSwapTableVO result = oscService.swapTable(1L);
 assertNotNull(result);
 }

 @Test
 void testSwapTable_invalidScheduleTaskId() {
 when(scheduleRepository.findById(1L)).thenReturn(Optional.empty());
 oscService.swapTable(1L);
 verifyZeroInteractions(scheduleTaskRepository);
 }

 @Test
 void testSwapTable_invalidSwapTableType() {
 ScheduleEntity schedule = new ScheduleEntity();
 schedule.setJobName("1");
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setScheduleId(1L);
 scheduleTask.setResultJson("{}");
 scheduleTask.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters().setSwapTableType(SwapTableType.BACKUP)));
 schedule.setId(1L);
 schedule.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
 schedule.setEnabled(true);
 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));
 oscService.swapTable(1L);
 verify(scheduleTaskRepository, times(0)).updateTaskResult(anyLong(), anyString());
 }

 @Test
 void testSwapTable_invalidManualSwapTableEnabled() {
 ScheduleEntity schedule = new ScheduleEntity();
 schedule.setJobName("1");
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setScheduleId(1L);
 scheduleTask.setResultJson("{\"manualSwapTableEnabled\":false}");
 scheduleTask.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
 schedule.setId(1L);
 schedule.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
 schedule.setEnabled(true);
 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));
 oscService.swapTable(1L);
 verify(scheduleTaskRepository, times(0)).updateTaskResult(anyLong(), anyString());
 }

 @Test
 void testGetLockUserIsRequired_validConnectionId() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType("OBServer");
 when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(connectionConfig);
 ReflectionTestUtils.setFieldValue(oscService, "connectionService", connectionService);
 assertTrue(oscService.getLockUserIsRequired(1L));
 }

 @Test
 void testGetLockUserIsRequired_invalidConnectionId() {
 when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenThrow(new RuntimeException());
 assertFalse(oscService.getLockUserIsRequired(1L));
 }
}