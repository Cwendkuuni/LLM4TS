package com.oceanbase.odc.service.onlineschemachange.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

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

 @Test
 public void testGetOscDatabaseInfoWithValidId() {
 DatabaseEntity databaseEntity = new DatabaseEntity();
 databaseEntity.setDatabaseId(1L);
 databaseEntity.setConnectionId(1L);
 doReturn(Optional.of(databaseEntity)).when(databaseRepository).findById(1L);

 OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(1L);
 assertTrue(oscDatabaseInfo.isPresent());
 assertEquals(1L, oscDatabaseInfo.get().getDatabaseId());
 }

 @Test
 public void testGetOscDatabaseInfoWithInvalidId() {
 doReturn(Optional.empty()).when(databaseRepository).findById(1L);
 OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(1L);
 assertFalse(oscDatabaseInfo.isPresent());
 }

 @Test
 public void testSwapTableWithValidScheduleTaskId() {
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobName("1");
 scheduleEntity.setId(1L);
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
 scheduleEntity.setJobParametersJson("{}");
 doReturn(Optional.of(scheduleEntity)).when(scheduleRepository).findById(1L);

 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setScheduleId(1L);
 scheduleTaskEntity.setResultJson("{}");
 doReturn(Optional.of(scheduleTaskEntity)).when(scheduleTaskRepository).findById(1L);

 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType("testDialect");
 doReturn(connectionConfig).when(connectionService).getForConnectionSkipPermissionCheck(1L);

 OscSwapTableVO oscSwapTableVO = oscService.swapTable(1L);
 assertNotNull(oscSwapTableVO);
 assertEquals(1L, oscSwapTableVO.getScheduleTaskId());
 }

 @Test(expected = IllegalStateException.class)
 public void testSwapTableWithInvalidScheduleTaskId() {
 doReturn(Optional.empty()).when(scheduleRepository).findById(1L);
 oscService.swapTable(1L);
 }

 @Test
 public void testGetLockUserIsRequiredWithNoConnection() {
 doReturn(null).when(connectionService).getForConnectionSkipPermissionCheck(1L);
 assertFalse(oscService.getLockUserIsRequired(1L));
 }

 @Test
 public void testGetLockUserIsRequiredWithConnection() {
 ConnectionSessionFactory factory = mock(ConnectionSessionFactory.class);
 ConnectionSession session = mock(ConnectionSession.class);
 when(factory.generateSession()).thenReturn(session);
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType("testDialect");
 doReturn(connectionConfig).when(connectionService).getForConnectionSkipPermissionCheck(1L);
 doReturn(factory).when(connectionService).getConnectionSessionFactory();
 String version = "testVersion";
 when(session.getVersion()).thenReturn(version);
 assertTrue(oscService.getLockUserIsRequired(1L));
 }

}