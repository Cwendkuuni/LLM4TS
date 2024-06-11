package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
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
import com.oceanbase.odc.service.connection.model.ConnectionConfig.DialectType;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.OscDBUserUtil;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

@SuppressWarnings("ResultOfMethodCallIgnored")
@Service @Slf4j
public class OscServiceTest {

    @InjectMocks private OscService oscService;
    @Mock private DatabaseRepository databaseRepository;
    @Mock private ScheduleTaskRepository scheduleTaskRepository;
    @Mock private ScheduleRepository scheduleRepository;
    @Mock private ConnectionService connectionService;

    @Before public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test public void testGetOscDatabaseInfo_databaseIdNotFound() {
        when(databaseRepository.findById(-1L)).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(-1L);
        assertTrue(result.isDatabaseIdUndefined());
    }

    @Test public void testGetOscDatabaseInfo_databaseFound() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(databaseEntity));
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
        assertEquals(1L, result.getDatabaseId());
        assertFalse(result.isLockDatabaseUserRequired());
    }

    @Test public void testSwapTable_scheduleTaskNotFound() {
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(1L));
        verifyZeroInteractions(databaseRepository, connectionService, scheduleTaskRepository, scheduleRepository);
    }

    @Test public void testSwapTable_scheduleTaskFound() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setResultJson("{}");
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setTaskId(1L);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));

        doReturn(oscParameters).when(oscService).createOscParameters();

        OnlineSchemaChangeScheduleTaskResult result = oscService.swapTable(1L);
        assertNotNull(result);
        assertFalse(result.isManualSwapTableEnabled());
        assertTrue(result.isManualSwapTableStarted());

        verify(databaseRepository, times(1)).findById(anyLong());
        verifyZeroInteractions(connectionService, scheduleRepository);
    }

    @Test public void testSwapTable_scheduleTaskFound_invalidJobType() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.SCHEDULE);
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setResultJson("{}");
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setTaskId(1L);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));

        doThrow(new IllegalStateException("Invalid job type")).when(oscService).validateJobType(scheduleEntity.getJobType());
        assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(1L));
        verifyZeroInteractions(databaseRepository, connectionService, scheduleRepository);
    }

    @Test public void testSwapTable_scheduleTaskFound_invalidSwapTableType() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.INVALID);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setResultJson("{}");
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setTaskId(1L);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));

        doThrow(new IllegalStateException("Invalid swap table type")).when(oscService).validateSwapTableType(oscParameters.getSwapTableType());
        assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(1L));
        verifyZeroInteractions(databaseRepository, connectionService, scheduleRepository);
    }

    @Test public void testSwapTable_scheduleTaskFound_invalidResult() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setResultJson("{\"manualSwapTableEnabled\": false, \"manualSwapTableStarted\": true}");
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));

        doThrow(new IllegalStateException("Invalid result")).when(oscService).parseSwapTableResult(scheduleTaskEntity.getResultJson());
        assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(1L));
        verifyZeroInteractions(databaseRepository, connectionService, scheduleRepository);
    }

    @Test public void testGetLockUserIsRequired_connectionConfigNotFound() {
        ConnectionConfig decryptedConnConfig = null;
        assertFalse(oscService.getLockUserIsRequired(-1L));
    }

    @Test public void testGetLockUserIsRequired_connectionConfigFound() {
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType(DialectType.MYSQL);
        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
        ConnectionSession session = new ConnectionSession();
        ConnectionSessionUtil.setVersion(session, "5.6.30");
        ConnectionSessionUtil.setAllowLockUser(session, false);

        assertTrue(oscService.getLockUserIsRequired(-1L));
    }

    @Test public void testGetLockUserIsRequired_connectionConfigFound_supportsLockUser() {
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType(DialectType.MYSQL);
        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
        ConnectionSession session = new ConnectionSession();
        ConnectionSessionUtil.setVersion(session, "5.6.30");
        ConnectionSessionUtil.setAllowLockUser(session, true);

        assertFalse(oscService.getLockUserIsRequired(-1L));
    }

    @Test public void testCreateOscParameters() {
        OnlineSchemaChangeParameters result = oscService.createOscParameters();
        assertNotNull(result);
        assertEquals(SwapTableType.MANUAL, result.getSwapTableType());
    }

    @Test public void testValidateJobType() {
        assertFalse(oscService.validateJobType(JobType.SCHEDULE));
        assertTrue(oscService.validateJobType(JobType.ONLINE_SCHEMA_CHANGE));
    }

    @Test public void testValidateSwapTableType() {
        assertFalse(oscService.validateSwapTableType(SwapTableType.INVALID));
        assertTrue(oscService.validateSwapTableType(SwapTableType.MANUAL));
    }

    @Test public void testParseSwapTableResult() {
        String resultJson = "{\"manualSwapTableEnabled\": false, \"manualSwapTableStarted\": false}";
        OnlineSchemaChangeScheduleTaskResult result = oscService.parseSwapTableResult(resultJson);
        assertNotNull(result);
        assertFalse(result.isManualSwapTableEnabled());
        assertFalse(result.isManualSwapTableStarted());

        resultJson = "{\"manualSwapTableEnabled\": true, \"manualSwapTableStarted\": true}";
        result = oscService.parseSwapTableResult(resultJson);
        assertNotNull(result);
        assertTrue(result.isManualSwapTableEnabled());
        assertTrue(result.isManualSwapTableStarted());
    }
}