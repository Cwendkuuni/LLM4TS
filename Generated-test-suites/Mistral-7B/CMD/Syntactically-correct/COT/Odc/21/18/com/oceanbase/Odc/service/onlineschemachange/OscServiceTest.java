package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.JobType;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OscServiceTest {

    private OscService oscService;
    private DatabaseRepository databaseRepository;
    private ScheduleRepository scheduleRepository;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ConnectionService connectionService;

    @Before
    public void setup() {
        databaseRepository = Mockito.mock(DatabaseRepository.class);
        scheduleRepository = Mockito.mock(ScheduleRepository.class);
        scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        connectionService = Mockito.mock(ConnectionService.class);
        oscService = new OscService() {
            {
                this.databaseRepository = databaseRepository;
                this.scheduleRepository = scheduleRepository;
                this.scheduleTaskRepository = scheduleTaskRepository;
                this.connectionService = connectionService;
            }
        };
    }

    // Test getOscDatabaseInfo method
    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        Optional<DatabaseEntity> databaseOptional = Optional.of(new DatabaseEntity());
        when(databaseRepository.findById(id)).thenReturn(databaseOptional);

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertNull(result);

        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setConnectionId(1L);

        boolean isLockUserRequired = oscService.getLockUserIsRequired(databaseEntity.getConnectionId());
        assertTrue(isLockUserRequired);

        OscLockDatabaseUserInfo expectedResult = new OscLockDatabaseUserInfo();
        expectedResult.setDatabaseId(databaseEntity.getDatabaseId());
        expectedResult.setLockDatabaseUserRequired(isLockUserRequired);

        assertEquals(expectedResult, result);
    }

    // Test swapTable method
    @Test
    public void testSwapTable_Success() {
        Long scheduleTaskId = 1L;

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("test_schedule_id");
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()).getBytes());
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        scheduleTaskEntity.setScheduleId(1L);
        scheduleTaskEntity.setJobName("test_schedule_id");
        scheduleTaskEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()).getBytes());
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()).getBytes());

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = oscService.swapTable(scheduleTaskId);

        assertTrue(result.isManualSwapTableEnabled());
        assertFalse(result.isManualSwapTableStarted());

        OnlineSchemaChangeScheduleTaskResult expectedResult = new OnlineSchemaChangeScheduleTaskResult();
        expectedResult.setManualSwapTableEnabled(true);
        expectedResult.setManualSwapTableStarted(false);

        assertEquals(expectedResult, result);
    }

    // Test swapTable method with invalid schedule task id
    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_InvalidScheduleTaskId() {
        Long scheduleTaskId = -1L;
        oscService.swapTable(scheduleTaskId);
    }

    // Test getLockUserIsRequired method
    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("MySQL");

        ConnectionSessionFactory factory = Mockito.mock(ConnectionSessionFactory.class);
        ConnectionSession connSession = Mockito.mock(ConnectionSession.class);
        when(factory.generateSession()).thenReturn(connSession);
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);
        when(ConnectionSessionUtil.getVersion(connSession)).thenReturn("5.6.30");

        boolean isLockUserRequired = oscService.getLockUserIsRequired(connectionId);
        assertTrue(isLockUserRequired);

        ConnectionConfig decryptedConnConfig2 = new ConnectionConfig();
        decryptedConnConfig2.setDialectType("MyISAM");

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig2);
        when(ConnectionSessionUtil.getVersion(connSession)).thenReturn("5.5.30");

        boolean isLockUserRequired2 = oscService.getLockUserIsRequired(connectionId);
        assertFalse(isLockUserRequired2);
    }

    // Test getLockUserIsRequired method with invalid connection id
    @Test
    public void testGetLockUserIsRequired_InvalidConnectionId() {
        oscService.getLockUserIsRequired(-1L);
    }

}