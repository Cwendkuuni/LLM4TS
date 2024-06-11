import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.OscDBUserUtil;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

 private OscService oscService;
 @Mock private ConnectionService connectionService;
 @Mock private ScheduleTaskRepository scheduleTaskRepository;
 @Mock private DatabaseRepository databaseRepository;
 @Mock private OscDBUserUtil osdBUtil;

 @Before
 public void setUp() {
 oscService = new OscService();
 oscService.setConnectionService(connectionService);
 oscService.setDatabaseRepository(databaseRepository);
 oscService.setOscDBUserUtil(osdBUtil);
 }

 @Test
 public void testGetOscDatabaseInfo() {
 Long id = 1L;
 DatabaseEntity dbEntity = new DatabaseEntity();
 dbEntity.setDatabaseId(id);
 when(databaseRepository.findById(id)).thenReturn(Optional.of(dbEntity));

 OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
 assertNull(result);

 verify(databaseRepository, times(1)).findById(id);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetOscDatabaseInfoWithDatabaseNotFound() {
 Long id = 1L;
 when(databaseRepository.findById(id)).thenReturn(Optional.empty());

 oscService.getOscDatabaseInfo(id);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testSwapTable() {
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(scheduleTaskId);
 scheduleTask.setResultJson("{}");
 scheduleTask.setJobName("1");

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 scheduleEntity.setJobParametersJson("{}");
 scheduleEntity.setJobName("1");
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);

 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(true);
 result.setManualSwapTableStarted(false);

 when(osdBUtil.isLockUserRequired(any(), any())).thenReturn(true);

 oscService.swapTable(scheduleTaskId);

 assertEquals(SwapTableType.MANUAL, oscParameters.getSwapTableType());
 assertTrue(result.isManualSwapTableEnabled());
 assertFalse(result.isManualSwapTableStarted());

 verify(connectionService, never()).getForConnectionSkipPermissionCheck(anyLong());
 verify(databaseRepository, never()).findById(anyLong());

 verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
 verify(scheduleRepository, times(1)).findById(1L);
 verify(osdBUtil, times(1)).isLockUserRequired(any(), any());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSwapTableWithInvalidJobType() {
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(scheduleTaskId);
 scheduleTask.setResultJson("{}");
 scheduleTask.setJobName("1");

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 scheduleEntity.setJobParametersJson("{}");
 scheduleEntity.setJobName("1");
 scheduleEntity.setJobType(JobType.BACKUP);

 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

 oscService.swapTable(scheduleTaskId);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testSwapTableWithManualSwapTableDisabled() {
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(scheduleTaskId);
 scheduleTask.setResultJson("{\"manualSwapTableEnabled\": false}");
 scheduleTask.setJobName("1");

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 scheduleEntity.setJobParametersJson("{}");
 scheduleEntity.setJobName("1");
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
 when(osdBUtil.isLockUserRequired(any(), any())).thenReturn(true);

 oscService.swapTable(scheduleTaskId);

 assertFalse(scheduleTask.isManualSwapTableStarted());

 verify(connectionService, never()).getForConnectionSkipPermissionCheck(anyLong());
 verify(databaseRepository, never()).findById(anyLong());

 verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
 verify(scheduleRepository, times(1)).findById(1L);
 verify(osdBUtil, times(1)).isLockUserRequired(any(), any());
 }

}