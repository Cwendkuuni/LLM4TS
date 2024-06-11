package com.oceanbase.odc.service.onlineschemachange;

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

import com.oceanbase.odc.common.json.JsonUtils;
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
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfigBuilder;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * Test class for OscService
 */
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
 public void testGetOscDatabaseInfo_DatabaseNotFound() {
 // given
 when(databaseRepository.findById(-1L)).thenReturn(Optional.empty());

 // when
 OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(-1L);

 // then
 assertNull(result);
 }

 @Test
 public void testGetOscDatabaseInfo_DatabaseFound() {
 // given
 DatabaseEntity dbEntity = new DatabaseEntity();
 dbEntity.setDatabaseId(1L);
 when(databaseRepository.findById(1L)).thenReturn(Optional.of(dbEntity));

 // when
 OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);

 // then
 assertNotNull(result);
 assertEquals(1L, result.getDatabaseId());
 }

 @Test
 public void testSwapTable_NoSuchScheduleTask() {
 // given
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

 // when, then
 assertThrows(IllegalArgumentException.class, () -> {
 oscService.swapTable(1L);
 });
 }

 @Test
 public void testSwapTable_SwapTableTypeMismatch() {
 // given
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setJobName("1");
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));

 // when, then
 assertThrows(IllegalArgumentException.class, () -> {
 oscService.swapTable(1L);
 });
 }

 @Test
 public void testSwapTable_ManualSwapTableDisabled() {
 // given
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setJobName("1");
 scheduleTaskEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));

 // when
 OnlineSchemaChangeScheduleTaskResult result = oscService.swapTable(1L);

 // then
 assertNotNull(result);
 assertFalse(result.isManualSwapTableEnabled());
 }

 @Test
 public void testSwapTable_ManualSwapTableEnabled() {
 // given
 ConnectionConfig connectionConfig = new ConnectionConfigBuilder()
 .setConnectionId(1L)
 .setDialectType("OBServer")
 .build();
 ConnectionSessionFactory factory = new DefaultConnectSessionFactory(connectionConfig);
 ConnectionSession connectionSession = mock(ConnectionSession.class);
 when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(connectionConfig);
 when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(connectionConfig);
 when(factory.generateSession()).thenReturn(connectionSession);
 when(connectionSession.expire()).thenReturn(null);
 when(connectionSession.isClosed()).thenReturn(false);
 when(connectionSession.isOpen()).thenReturn(true);
 when(connectionSession.getVersion()).thenReturn("6.0");
 when(databaseRepository.findById(1L)).thenReturn(Optional.of(new DatabaseEntity()));
 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(new ScheduleEntity()));
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(new ScheduleTaskEntity()));

 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSwapTableType(SwapTableType.MANUAL);
 ScheduleEntity scheduleEntityMock = new ScheduleEntity();
 scheduleEntityMock.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
 scheduleEntityMock.setJobParametersJson(JsonUtils.toJson(parameters));

 // when
 OnlineSchemaChangeScheduleTaskResult result = oscService.swapTable(1L);

 // then
 assertNotNull(result);
 assertTrue(result.isManualSwapTableEnabled());
 }

}