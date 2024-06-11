package com.oceanbase.odc.service.onlineschemachange;

import static com.oceanbase.odc.service.onlineschemachange.OscService.ErrorCodes.BadArgument;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

@Component
public class OscServiceTest {

    private DatabaseRepository databaseRepository;
    private ConnectionService connectionService;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ScheduleRepository scheduleRepository;

    @Autowired
    public void setUpMocks(DatabaseRepository databaseRepository, ConnectionService connectionService,
            ScheduleTaskRepository scheduleTaskRepository, ScheduleRepository scheduleRepository) {
        this.databaseRepository = databaseRepository;
        this.connectionService = connectionService;
        this.scheduleTaskRepository = scheduleTaskRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo_databaseNotPresent() {
        Optional<DatabaseEntity> databaseOptional = Optional.empty();
        when(databaseRepository.findById(1L)).thenReturn(databaseOptional);

        OscService oscService = new OscService();
        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(1L);
        assertNull(oscDatabaseInfo);
    }

    @Test
    public void testGetOscDatabaseInfo_databasePresent() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(databaseEntity));

        OscService oscService = new OscService();
        Optional<DatabaseEntity> databaseOptional = Optional.of(databaseEntity);
        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(1L);
        assertEquals(databaseEntity.getDatabaseId(), oscDatabaseInfo.getDatabaseId());
        assertTrue(oscDatabaseInfo.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable_scheduleTaskNotFound() {
        Long scheduleTaskId = 1L;
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());

        OscService oscService = new OscService();
        assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(scheduleTaskId));
    }

    @Test
    public void testSwapTable_scheduleTaskPresent() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        OscService oscService = new OscService();
        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testGetLockUserIsRequired_connectionNotFound() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(connectionConfig);

        boolean result = new OscService().getLockUserIsRequired(1L);
        assertFalse(result);
    }

    @Test
    public void testGetLockUserIsRequired_connectionFound() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(connectionConfig);
        when(connectionConfig.getDialectType()).thenReturn("OBServer");

        ConnectionSessionFactory factory = mock(ConnectionSessionFactory.class);
        ConnectionSession connSession = mock(ConnectionSession.class);
        String version = "1.0";
        when(factory.generateSession()).thenReturn(connSession);
        when(ConnectionSessionUtil.getVersion(connSession)).thenReturn(version);

        boolean result = new OscService().getLockUserIsRequired(1L);
        assertTrue(result);
    }

    private void assertEquals(Long expected, Long actual) {
        assertEquals(expected.longValue(), actual.longValue());
    }

    private void assertEquals(DatabaseEntity expected, DatabaseEntity actual) {
        assertEquals(expected.getDatabaseId(), actual.getDatabaseId());
    }

    private void assertEquals(OscLockDatabaseUserInfo expected, OscLockDatabaseUserInfo actual) {
        assertEquals(expected.getDatabaseId(), actual.getDatabaseId());
        assertEquals(expected.isLockDatabaseUserRequired(), actual.isLockDatabaseUserRequired());
    }

    private void assertNull(OscLockDatabaseUserInfo actual) {
        assertNull(actual);
    }

    private void assertSame(Long expected, Long actual) {
        assertEquals(expected, actual);
    }

    private void assertThrows(Class<? extends Throwable> expectedExceptionType, Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable throwable) {
            if (expectedExceptionType.isInstance(throwable)) {
                return;
            }
            throw new AssertionError("Unexpected exception: " + throwable.getMessage(), throwable);
        }
        fail("Expected exception of type " + expectedExceptionType + " not thrown");
    }
}