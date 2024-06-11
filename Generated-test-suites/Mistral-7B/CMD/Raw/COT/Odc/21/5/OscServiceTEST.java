package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

@Service
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

    @After
    public void teardown() {
        reset(databaseRepository, connectionService, scheduleTaskRepository, scheduleRepository);
    }

    @Test
    public void testGetOscDatabaseInfo_databaseNotFound() {
        // Given
        Long id = 1L;

        // When
        Optional<DatabaseEntity> database = Optional.empty();
        when(databaseRepository.findById(id)).thenReturn(database);

        // Then
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetOscDatabaseInfo_databaseFound() {
        // Given
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        Optional<DatabaseEntity> database = Optional.of(databaseEntity);

        // When
        when(databaseRepository.findById(id)).thenReturn(database);

        // Then
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertEquals(id, result.getDatabaseId());
        assertTrue(result.isLockDatabaseUserRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_scheduleTaskNotFound() {
        // Given
        Long scheduleTaskId = 1L;

        // When
        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.empty();
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(scheduleTaskOptional);

        // Then
        oscService.swapTable(scheduleTaskId);
        fail("Expected IllegalArgumentException not thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_jobTypeNotOnlineSchemaChange() {
        // Given
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setJobName("Test");
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        // When
        oscService.swapTable(scheduleTaskId);

        // Then
        fail("Expected IllegalArgumentException not thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_swapTableTypeNotManual() {
        // Given
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setJobName("Test");
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.AUTOMATIC);
        scheduleTask.setJobParametersJson(JsonUtils.toJson(oscParameters));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        // When
        oscService.swapTable(scheduleTaskId);

        // Then
        fail("Expected IllegalArgumentException not thrown");
    }

    @Test(expected = IllegalStateException.class)
    public void testSwapTable_manualSwapTableDisabled() {
        // Given
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setJobName("Test");
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);
        scheduleTask.setResultJson(JsonUtils.toJson(result));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        // When
        oscService.swapTable(scheduleTaskId);

        // Then
        fail("Expected IllegalStateException not thrown");
    }

    @Test
    public void testSwapTable_manualSwapTableEnabled() {
        // Given
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setJobName("Test");
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        scheduleTask.setResultJson(JsonUtils.toJson(result));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        // When
        OscSwapTableVO resultOscSwapTable = oscService.swapTable(scheduleTaskId);

        // Then
        assertEquals(scheduleTaskId, resultOscSwapTable.getScheduleTaskId());
    }

    @Test
    public void testGetLockUserIsRequired_connectionNotFound() {
        // Given
        Long connectionId = 1L;

        // When
        ConnectionConfig decryptedConnConfig = null;

        // Then
        assertFalse(oscService.getLockUserIsRequired(connectionId));
    }

    @Test
    public void testGetLockUserIsRequired_connectionFound() {
        // Given
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        assertTrue(OscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType(), () -> {
            ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
            ConnectionSession connSession = null;
            try {
                connSession = factory.generateSession();
                String version = ConnectionSessionUtil.getVersion(connSession);
            } catch (Exception e) {
                log.info("Get connection occur error", e);
            } finally {
                if (connSession != null) {
                    connSession.expire();
                }
            }
            return version;
        }));

        // When
        boolean result = oscService.getLockUserIsRequired(connectionId);

        // Then
        assertTrue(result);
    }
}