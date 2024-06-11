import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

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
 public void testGetOscDatabaseInfo() {
 // given
 Long id = 1L;
 DatabaseEntity databaseEntity = new DatabaseEntity();
 databaseEntity.setDatabaseId(id);
 when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

 // when
 OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);

 // then
 assertEquals(id, oscDatabase.getDatabaseId());
 assertFalse(oscDatabase.isLockDatabaseUserRequired());
 }

 @Test
 public void testGetOscDatabaseInfo_whenDatabaseIsNotPresent() {
 // given
 Long id = 1L;
 when(databaseRepository.findById(id)).thenReturn(Optional.empty());

 // when
 OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);

 // then
 assertNull(oscDatabase);
 }

 @Test
 public void testSwapTable() {
 // given
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobName("1");
 scheduleEntity.setJobParametersJson(new OnlineSchemaChangeParameters().toString());
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
 when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));
 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

 // when
 oscService.swapTable(scheduleTaskId);

 // then
 verify(scheduleTaskRepository, times(1)).updateTaskResult(scheduleTaskId, anyString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSwapTable_whenJobTypeIsNotOnlineSchemaChange() {
 // given
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobName("1");
 scheduleEntity.setJobParametersJson(new OnlineSchemaChangeParameters().toString());
 scheduleEntity.setJobType(JobType.BACKUP);
 when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));
 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

 // when, then
 oscService.swapTable(scheduleTaskId);
 }

 @Test
 public void testGetLockUserIsRequired() {
 // given
 Long connectionId = 1L;
 ConnectionConfig decryptedConnConfig = new ConnectionConfig();
 ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
 ConnectionSession connSession = mock(ConnectionSession.class);
 String version = "5.5.21";
 when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);
 when(factory.generateSession()).thenReturn(connSession);
 when(ConnectionSessionUtil.getVersion(connSession)).thenReturn(version);
 when(OscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType(), () -> {
 ConnectionSessionFactory factoryMock = mock(ConnectionSessionFactory.class);
 ConnectionSession connSessionMock = mock(ConnectionSession.class);
 String versionMock = "5.5.21";
 when(factoryMock.generateSession()).thenReturn(connSessionMock);
 when(ConnectionSessionUtil.getVersion(connSessionMock)).thenReturn(versionMock);
 return OscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType(), factoryMock);
 })).thenReturn(true);

 // when
 boolean lockUserIsRequired = oscService.getLockUserIsRequired(connectionId);

 // then
 assertTrue(lockUserIsRequired);
 }

 @Test
 public void testGetLockUserIsRequired_whenLockUserIsNotRequired() {
 // given
 Long connectionId = 1L;
 ConnectionConfig decryptedConnConfig = new ConnectionConfig();
 ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
 ConnectionSession connSession = mock(ConnectionSession.class);
 String version = "5.5.21";
 when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);
 when(factory.generateSession()).thenThrow(new RuntimeException());
 when(OscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType(), () -> {
 ConnectionSessionFactory factoryMock = mock(ConnectionSessionFactory.class);
 ConnectionSession connSessionMock = mock(ConnectionSession.class);
 String versionMock = "5.5.21";
 when(factoryMock.generateSession()).thenReturn(connSessionMock);
 when(ConnectionSessionUtil.getVersion(connSessionMock)).thenReturn(versionMock);
 return OscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType(), factoryMock);
 })).thenReturn( false);

 // when
 boolean lockUserIsRequired = oscService.getLockUserIsRequired(connectionId);

 // then
 assertFalse(lockUserIsRequired);
 }
}