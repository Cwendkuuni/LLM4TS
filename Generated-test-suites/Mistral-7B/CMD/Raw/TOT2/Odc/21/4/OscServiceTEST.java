import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.JobType;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

@ActiveProfiles("test")
public class OscServiceTest {

 @MockBean
 private DatabaseRepository databaseRepository;
 @MockBean
 private ScheduleTaskRepository scheduleTaskRepository;
 @MockBean
 private ScheduleRepository scheduleRepository;
 @MockBean
 private ConnectionService connectionService;

 @Autowired
 @InjectMocks
 private OscService oscService;

 @Before
 public void init() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testGetOscDatabaseInfo_ValidInput_ReturnsOscDatabaseInfo() {
 Long id = 1L;
 DatabaseEntity databaseEntity = new DatabaseEntity();
 databaseEntity.setDatabaseId(id);

 // when
 OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);

 // then
 Assert.assertNotNull(oscDatabase);
 Assert.assertEquals(id, oscDatabase.getDatabaseId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetOscDatabaseInfo_InvalidInput_ThrowsIllegalArgumentException() {
 Long id = null;

 // when and then
 Assert.assertThrows(IllegalArgumentException.class, () -> {
 oscService.getOscDatabaseInfo(id);
 });
 }

 @Test
 public void testSwapTable_ValidInput_ReturnsOscSwapTableVO() {
 Long scheduleTaskId = 1L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobName("scheduleId");
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
 JobType jobType = JobType.ONLINE_SCHEMA_CHANGE;

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);

 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(scheduleTaskId);
 scheduleTask.setScheduleId(Long.parseLong(scheduleEntity.getJobName()));
 scheduleTask.setResultJson("");
 scheduleTask.setJobParametersJson(JsonUtils.toJson(oscParameters));
 scheduleTask.setJobName(String.valueOf(scheduleEntity.getJobName()));
 scheduleTask.setJobType(jobType);

 // when
 OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);

 // then
 Assert.assertNotNull(oscSwapTable);
 Assert.assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSwapTable_InvalidInput_ThrowsIllegalArgumentException() {
 Long scheduleTaskId = null;

 // when and then
 Assert.assertThrows(IllegalArgumentException.class, () -> {
 oscService.swapTable(scheduleTaskId);
 });
}

 @Test
 public void testGetLockUserIsRequired_ValidInput_ReturnsTrue() {
 Long connectionId = 1L;
 ConnectionConfig decryptedConnConfig = new ConnectionConfig();
 decryptedConnConfig.setDialectType("MySQL");

 // when
 boolean lockUserIsRequired = oscService.getLockUserIsRequired(connectionId);

 // then
 Assert.assertTrue(lockUserIsRequired);
 }

 @Test
 public void testGetLockUserIsRequired_InvalidInput_ReturnsFalse() {
 Long connectionId = null;

 // when
 boolean lockUserIsRequired = oscService.getLockUserIsRequired(connectionId);

 // then
 Assert.assertFalse(lockUserIsRequired);
 }

}