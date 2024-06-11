import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import com.oceanbase.odc.core.shared.PreConditions;
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

@SpringJUnitConfig(classes = OscService.class)
@ActiveProfiles("test")
public class OscServiceTest {

 @MockBean
 private DatabaseRepository databaseRepository;
 @MockBean
 private ConnectionService connectionService;
 @MockBean
 private ScheduleRepository scheduleRepository;
 @MockBean
 private ScheduleTaskRepository scheduleTaskRepository;

 @Autowired
 private OscService oscService;

 @Test
 public void testGetOscDatabaseInfo_ValidId() {
 DatabaseEntity database = Mockito.mock(DatabaseEntity.class);
 MockitoAnnotations.initMocks(this);

 when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(database));

 OscLockDatabaseUserInfo osdInfo = oscService.getOscDatabaseInfo(1L);

 Assertions.assertNotNull(osdInfo);
 }

 @Test
 public void testGetOscDatabaseInfo_InvalidId() {
 OscLockDatabaseUserInfo osdInfo = oscService.getOscDatabaseInfo(1L);

 Assertions.assertNull(osdInfo);
 }

 @Test
 public void testSwapTable_ValidScheduleTaskId() {
 ScheduleEntity schedule = Mockito.mock(ScheduleEntity.class);
 ScheduleTaskEntity scheduleTask = Mockito.mock(ScheduleTaskEntity.class);
 OnlineSchemaChangeParameters oscParams = new OnlineSchemaChangeParameters();
 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();

 when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));
 when(databaseRepository.findById(any())).thenReturn(Optional.empty());

 oscService.swapTable(1L);
 }

 @Test
 public void testSwapTable_InvalidScheduleTaskId() {
 OscSwapTableVO oscSwapTable = oscService.swapTable(1L);

 Assertions.assertNull(oscSwapTable);
 }

 @Test
 public void testSwapTable_InvalidDatabaseId() {
 Long scheduleId = 1L;
 ScheduleEntity schedule = Mockito.mock(ScheduleEntity.class);
 ScheduleTaskEntity scheduleTask = Mockito.mock(ScheduleTaskEntity.class);
 OnlineSchemaChangeParameters oscParams = new OnlineSchemaChangeParameters();
 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();

 when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));
 when(databaseRepository.findById(any())).thenReturn(Optional.empty());

 oscService.swapTable(scheduleId);
 }

 @Test
 public void testGetLockUserIsRequired_ValidConnectionId() {
 ConnectionConfig decryptedConnConfig = Mockito.mock(ConnectionConfig.class);
 ConnectionSessionFactory factory = Mockito.mock(ConnectionSessionFactory.class);
 ConnectionSession connSession = Mockito.mock(ConnectionSession.class);
 String version = "5.5.11";

 when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(decryptedConnConfig);
 when(factory.generateSession()).thenReturn(connSession);
 when(ConnectionSessionUtil.getVersion(connSession)).thenReturn(version);
 when(OscDBUserUtil.isLockUserRequired(any(), any())).thenReturn(true);

 boolean isLockUserRequired = oscService.getLockUserIsRequired(1L);

 Assertions.assertTrue(isLockUserRequired);
 }

 @Test
 public void testGetLockUserIsRequired_InvalidConnectionId() {
 ConnectionConfig decryptedConnConfig = Mockito.mock(ConnectionConfig.class);
 ConnectionSessionFactory factory = Mockito.mock(ConnectionSessionFactory.class);
 ConnectionSession connSession = Mockito.mock(ConnectionSession.class);
 String version = "5.5.11";

 when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(decryptedConnConfig);
 when(factory.generateSession()).thenThrow(new RuntimeException());
 when(OscDBUserUtil.isLockUserRequired(any(), any())).thenReturn(true);

 boolean isLockUserRequired = oscService.getLockUserIsRequired(1L);

 Assertions.assertFalse(isLockUserRequired);
 }
}