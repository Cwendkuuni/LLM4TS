import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
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
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;

@SuppressWarnings("unchecked")
@Slf4j
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
 // given
 Long databaseId = 1L;
 DatabaseEntity databaseEntity = new DatabaseEntity();
 databaseEntity.setDatabaseId(databaseId);
 databaseEntity.setConnectionId(1L);
 when(databaseRepository.findById(databaseId)).thenReturn(Optional.of(databaseEntity));

 // when
 OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(databaseId);

 // then
 assertNotNull(oscDatabase);
 assertEquals(databaseId, oscDatabase.getDatabaseId());
 assertFalse(oscDatabase.isLockDatabaseUserRequired());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetOscDatabaseInfoWithInvalidId() {
 // given
 when(databaseRepository.findById(1L)).thenReturn(Optional.empty());

 // when, then
 oscService.getOscDatabaseInfo(1L);
 }

 @Test
 public void testSwapTableWithValidScheduleTaskId() {
 // given
 Long scheduleTaskId = 1L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 scheduleEntity.setJobName("1");
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 scheduleEntity.setJobParametersJson("{}");

 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(scheduleTaskId);
 scheduleTaskEntity.setScheduleTaskStatus(2);
 scheduleTaskEntity.setResultJson("{}");
 scheduleTaskEntity.setScheduleEntity(scheduleEntity);

 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

 // when
 OscSwapTableVO result = oscService.swapTable(scheduleTaskId);

 // then
 assertNotNull(result);
 assertEquals(scheduleTaskId, result.getScheduleTaskId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSwapTableWithInvalidScheduleTaskId() {
 // given
 when(scheduleRepository.findById(1L)).thenReturn(Optional.empty());

 // when, then
 oscService.swapTable(1L);
 }

 @Test(expected = IllegalStateException.class)
 public void testSwapTableWithNonOnlineSchemaChangeCompleteTask() {
 // given
 Long scheduleTaskId = 1L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 scheduleEntity.setJobName("1");
 scheduleEntity.setJobType(JobType.NORMAL);
 scheduleEntity.setJobParametersJson("{}");

 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

 // when, then
 oscService.swapTable(scheduleTaskId);
 }

 @Test
 public void testGetLockUserIsRequiredWithNonLockUserRequiredConnection() {
 // given
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType("OB_SQL");

 // when
 boolean result = oscService.getLockUserIsRequired(1L);

 // then
 assertFalse(result);
 }

 @Test
 public void testGetLockUserIsRequiredWithLockUserRequiredConnection() {
 // given
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType("MySQL");

 // when
 boolean result = oscService.getLockUserIsRequired(1L);

 // then
 assertTrue(result);
 }

 @Test
 public void testGetLockUserIsRequiredWithConnectionFailure() {
 // given
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType("OB_SQL");

 // when
 assertThrows(RuntimeException.class, () -> oscService.getLockUserIsRequired(1L));
 }
}