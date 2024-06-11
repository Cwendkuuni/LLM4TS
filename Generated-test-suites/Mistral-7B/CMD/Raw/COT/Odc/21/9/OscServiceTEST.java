import static org.junit.Assert.*;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.SwapTableType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
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
 public void initMock() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testGetOscDatabaseInfo() {
 // given
 Long id = 1L;
 DatabaseEntity databaseEntity = new DatabaseEntity();
 databaseEntity.setDatabaseId(id);
 Optional<DatabaseEntity> databaseOptional = Optional.of(databaseEntity);

 // when
 OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);

 // then
 assertTrue(oscDatabase.getDatabaseId() == id);
 assertFalse(oscDatabase.isLockDatabaseUserRequired());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetOscDatabaseInfoWithInvalidId() {
 // given
 Long id = -1L;
 Optional<DatabaseEntity> databaseOptional = Optional.empty();

 // when
 oscService.getOscDatabaseInfo(id);
 }

 @Test
 @Transactional
 public void testSwapTable() {
 // given
 Long scheduleTaskId = 1L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobName("scheduleId");
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(scheduleTaskId);
 scheduleTask.setJobName("scheduleTaskId");
 scheduleTask.setResultJson("{}");
 ScheduleTaskEntity savedScheduleTask = new ScheduleTaskEntity();
 ScheduleTaskEntity expectedScheduleTask = new ScheduleTaskEntity();
 savedScheduleTask.setId(scheduleTaskId);
 expectedScheduleTask.setId(scheduleTaskId);
expectedScheduleTask.setJobName("scheduleTaskId");
 // when
 OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);

 // then
 assertSame(savedScheduleTask, oscService.scheduleTaskRepository.findById(scheduleTaskId).get());
 assertSame(expectedScheduleTask, oscService.scheduleTaskRepository.findById(scheduleTaskId).get());
 assertNotNull(oscSwapTable);
 assertSame(scheduleTaskId, oscSwapTable.getScheduleTaskId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSwapTableWithInvalidScheduleTaskId() {
 // given
 Long scheduleTaskId = -1L;

 // when
 oscService.swapTable(scheduleTaskId);
 }

 @Test
 @Transactional
 public void testSwapTableWithInvalidScheduleId() {
 // given
 Long scheduleTaskId = 1L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobName("scheduleId");
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(scheduleTaskId);
 scheduleTask.setJobName("scheduleTaskId");
 scheduleTask.setResultJson("{}");
 ScheduleTaskEntity savedScheduleTask = new ScheduleTaskEntity();
 ScheduleTaskEntity expectedScheduleTask = new ScheduleTaskEntity();
 savedScheduleTask.setId(scheduleTaskId);
 expectedScheduleTask.setId(scheduleTaskId);
expectedScheduleTask.setJobName("scheduleTaskId");
 // when
 // then
 assertThrows(IllegalArgumentException.class, () -> {
 oscService.swapTable(scheduleTaskId);
 });
 assertSame(savedScheduleTask, oscService.scheduleTaskRepository.findById(scheduleTaskId).get());
 assertSame(expectedScheduleTask, oscService.scheduleTaskRepository.findById(scheduleTaskId).get());
 }

 @Test
 @Transactional
 public void testSwapTableWithInvalidJobType() {
 // given
 Long scheduleTaskId = 1L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobName("scheduleId");
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_FAILED);
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(scheduleTaskId);
 scheduleTask.setJobName("scheduleTaskId");
 scheduleTask.setResultJson("{}");
 ScheduleTaskEntity savedScheduleTask = new ScheduleTaskEntity();
 ScheduleTaskEntity expectedScheduleTask = new ScheduleTaskEntity();
 savedScheduleTask.setId(scheduleTaskId);
 expectedScheduleTask.setId(scheduleTaskId);
expectedScheduleTask.setJobName("scheduleTaskId");
 // when
 // then
 assertThrows(IllegalArgumentException.class, () -> {
 oscService.swapTable(scheduleTaskId);
 });
 assertSame(savedScheduleTask, oscService.scheduleTaskRepository.findById(scheduleTaskId).get());
 assertSame(expectedScheduleTask, oscService.scheduleTaskRepository.findById(scheduleTaskId).get());
 }

 @Test
 @Transactional
 public void testSwapTableWithInvalidSwapTableType() {
 // given
 Long scheduleTaskId = 1L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobName("scheduleId");
 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.AUTOMATIC);
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(scheduleTaskId);
 scheduleTask.setJobName("scheduleTaskId");
 scheduleTask.setResultJson("{}");
 ScheduleTaskEntity savedScheduleTask = new ScheduleTaskEntity();
 ScheduleTaskEntity expectedScheduleTask = new ScheduleTaskEntity();
 savedScheduleTask.setId(scheduleTaskId);
 expectedScheduleTask.setId(scheduleTaskId);
expectedScheduleTask.setJobName("scheduleTaskId");
 // when
 // then
 assertThrows(IllegalArgumentException.class, () -> {
 oscService.swapTable(scheduleTaskId);
 });
 assertSame(savedScheduleTask, oscService.scheduleTaskRepository.findById(scheduleTaskId).get());
 assertSame(expectedScheduleTask, oscService.scheduleTaskRepository.findById(scheduleTaskId).get());
 }

 @Test
 @Transactional
 public void testSwapTableWithInvalidManualSwapTableEnabled() {
 // given
 Long scheduleTaskId = 1L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobName("scheduleId");
 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);
 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(false);
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));
 scheduleEntity.setResultJson(JsonUtils.toJson(result));
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(scheduleTaskId);
 scheduleTask.setJobName("scheduleTaskId");
 scheduleTask.setResultJson("{}");
 ScheduleTaskEntity savedScheduleTask = new ScheduleTaskEntity();
 ScheduleTaskEntity expectedScheduleTask = new ScheduleTaskEntity();
 savedScheduleTask.setId(scheduleTaskId);
 expectedScheduleTask.setId(scheduleTaskId);
expectedScheduleTask.setJobName("scheduleTaskId");
 // when
 // then
 assertThrows(IllegalArgumentException.class, () -> {
 oscService.swapTable(scheduleTaskId);
 });
 assertSame(savedScheduleTask, oscService.scheduleTaskRepository.findById(scheduleTaskId).get());
 assertSame(expectedScheduleTask, oscService.scheduleTaskRepository.findById(scheduleTaskId).get());
 }
}