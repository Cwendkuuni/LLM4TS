import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
 public void testGetOscDatabaseInfo_ValidDatabaseId_ReturnsOscDatabaseInfo() {
 // given
 Long id = 1L;
 DatabaseEntity databaseEntity = new DatabaseEntity();
 databaseEntity.setDatabaseId(id);

 // when
 OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);

 // then
 assertEquals(id, oscDatabase.getDatabaseId());
 assertFalse(oscDatabase.isLockDatabaseUserRequired());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetOscDatabaseInfo_InvalidDatabaseId_ThrowsIllegalArgumentException() {
 // given
 Long id = 0L;

 // when & then
 oscService.getOscDatabaseInfo(id);
 }

 @Test
 public void testSwapTable_ValidScheduleTaskId_ReturnsOscSwapTableVO() {
 // given
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobName("scheduleId");
 scheduleEntity.setJobParametersJson("{}");
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
 scheduleTask.setSchedule(scheduleEntity);
 ScheduleTaskEntity savedScheduleTask = saveAndReturn(scheduleTaskRepository, scheduleTask);

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);

 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(false);
 result.setManualSwapTableStarted(false);

 doReturn(oscParameters).when(scheduleEntity).getJobParametersJson();
 doReturn(result).when(scheduleTask).getResultJson();
 doReturn(savedScheduleTask).when(scheduleTaskRepository).findById(scheduleTaskId);

 // when
 OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);

 // then
 assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
 assertFalse(oscSwapTable.isSwapTableStarted());
 }

 @Test
 public void testSwapTable_InvalidScheduleTaskId_ThrowsIllegalArgumentException() {
 // given
 Long scheduleTaskId = -1L;

 // when & then
 assertThrows(IllegalArgumentException.class, () -> {
 oscService.swapTable(scheduleTaskId);
 });
 }

 @Test
 public void testSwapTable_InvalidJobType_ThrowsIllegalArgumentException() {
 // given
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobName("scheduleId");
 scheduleEntity.setJobParametersJson("{}");
 scheduleEntity.setJobType(JobType.BACKUP);
 scheduleTask.setSchedule(scheduleEntity);
 doReturn(scheduleEntity).when(scheduleTask).getSchedule();
 doReturn(scheduleTask).when(scheduleTaskRepository).findById(scheduleTaskId);

 // when & then
 assertThrows(IllegalArgumentException.class, () -> {
 oscService.swapTable(scheduleTaskId);
 });
 }

 @Test
 public void testSwapTable_InvalidSwapTableType_ThrowsIllegalArgumentException() {
 // given
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobName("scheduleId");
 scheduleEntity.setJobParametersJson("{}");
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
 scheduleTask.setSchedule(scheduleEntity);
 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.BACKUP);
 doReturn(oscParameters).when(scheduleEntity).getJobParametersJson();
 doReturn(scheduleTask).when(scheduleTaskRepository).findById(scheduleTaskId);

 // when & then
 assertThrows(IllegalArgumentException.class, () -> {
 oscService.swapTable(scheduleTaskId);
 });
 }

 @Test
 public void testSwapTable_AlreadySwappedTable_ThrowsIllegalStateException() {
 // given
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobName("scheduleId");
 scheduleEntity.setJobParametersJson("{}");
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
 scheduleTask.setSchedule(scheduleEntity);
 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);
 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(true);
 result.setManualSwapTableStarted(true);
 doReturn(oscParameters).when(scheduleEntity).getJobParametersJson();
 doReturn(result).when(scheduleTask).getResultJson();
 doReturn(scheduleTask).when(scheduleTaskRepository).findById(scheduleTaskId);

 // when & then
 assertThrows(IllegalStateException.class, () -> {
 oscService.swapTable(scheduleTaskId);
 });
 }

 // Add more test cases for other methods and edge cases as needed

}