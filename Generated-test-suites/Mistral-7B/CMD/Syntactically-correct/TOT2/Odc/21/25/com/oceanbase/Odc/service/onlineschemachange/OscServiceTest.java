import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@SpringJUnitConfig(classes = OscService.class)
@TestPropertySource("/application-test.properties")
public class OscServiceTest {

 @Autowired
 private OscService oscService;
 @Autowired
 private DatabaseRepository databaseRepository;
 @Autowired
 private ScheduleTaskRepository scheduleTaskRepository;
 @Autowired
 private ScheduleRepository scheduleRepository;

 @BeforeEach
 void initMocks() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 void testGetOscDatabaseInfo_WhenDatabaseNotPresent_ThenReturnEmptyInfo() {
 OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(-1L);
 assertNotNull(oscDatabaseInfo);
 assertFalse(oscDatabaseInfo.isLockDatabaseUserRequired());
 }

 @Test
 void testGetOscDatabaseInfo_WhenDatabasePresent_ThenReturnDatabaseInfo() {
 DatabaseEntity databaseEntity = new DatabaseEntity();
 databaseEntity.setDatabaseId(1L);
 databaseRepository.save(databaseEntity);

 OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(databaseEntity.getId());
 assertNotNull(oscDatabaseInfo);
 assertFalse(oscDatabaseInfo.isLockDatabaseUserRequired());
 }

 @Test
 void testSwapTable_WhenScheduleTaskNotPresent_ThenThrowException() {
 assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(-1L));
 }

 @Test
 void testSwapTable_WhenScheduleTaskPresent_ThenSwapTableSuccess() {
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobId(1L);
 scheduleEntity.setJobName("test");
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
 scheduleRepository.save(scheduleEntity);

 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setScheduleTaskId(1L);
 scheduleTaskEntity.setScheduleTaskJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));
 scheduleTaskEntity.setScheduleId(scheduleEntity.getId());
 scheduleTaskRepository.save(scheduleTaskEntity);

 OnlineSchemaChangeScheduleTaskResult result = oscService.swapTable(scheduleTaskEntity.getScheduleTaskId());
 assertNotNull(result);
 assertNull(result.getManualSwapTableEnabled());
 assertNull(result.getManualSwapTableStarted());
 }

 @Test
 void testSwapTable_WhenScheduleTaskManualSwapTableDisabled_ThenThrowException() {
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobId(1L);
 scheduleEntity.setJobName("test");
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
 scheduleRepository.save(scheduleEntity);

 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setScheduleTaskId(1L);
 scheduleTaskEntity.setScheduleTaskJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));
 scheduleTaskEntity.setScheduleId(scheduleEntity.getId());
 scheduleTaskEntity.setManualSwapTableEnabled(false);
 scheduleTaskRepository.save(scheduleTaskEntity);

 assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(scheduleTaskEntity.getScheduleTaskId()));
 }

 @Test
 void testSwapTable_WhenScheduleTaskManualSwapTableEnabled_ThenSwapTableSuccess() {
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobId(1L);
 scheduleEntity.setJobName("test");
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
 scheduleRepository.save(scheduleEntity);

 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setScheduleTaskId(1L);
 scheduleTaskEntity.setScheduleTaskJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));
 scheduleTaskEntity.setScheduleId(scheduleEntity.getId());
 scheduleTaskEntity.setManualSwapTableEnabled(true);
 scheduleTaskRepository.save(scheduleTaskEntity);

 OnlineSchemaChangeScheduleTaskResult result = oscService.swapTable(scheduleTaskEntity.getScheduleTaskId());
 assertNotNull(result);
 assertFalse(result.isManualSwapTableEnabled());
 assertTrue(result.isManualSwapTableStarted());
 }

 @Test
 void testGetLockUserIsRequired_WhenConnectionIsNotPresent_ThenReturnFalse() {
 assertFalse(oscService.getLockUserIsRequired(-1L));
 }

 @Test
 void testGetLockUserIsRequired_WhenConnectionPresent_ThenReturnTrue() {
 ConnectionEntity connectionEntity = new ConnectionEntity();
 connectionEntity.setConnectionId(1L);
 connectionService.saveConnection(connectionEntity);

 assertTrue(oscService.getLockUserIsRequired(connectionEntity.getConnectionId()));
 }
}