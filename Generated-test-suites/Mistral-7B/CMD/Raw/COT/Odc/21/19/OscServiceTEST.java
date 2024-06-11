package com.oceanbase.odc.service.onlineschemachange.test;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.session.ConnectionSession;
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
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
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
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        Optional<DatabaseEntity> databaseOptional = Optional.of(databaseEntity);
        Mockito.when(databaseRepository.findById(id)).thenReturn(databaseOptional);

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);
        assertTrue(oscDatabase.getDatabaseId() == id);
        assertFalse(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfoWhenDatabaseNotFound() {
        Long id = 1L;
        Mockito.when(databaseRepository.findById(id)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);
        assertNull(oscDatabase);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(scheduleTask);
        Mockito.when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(scheduleTaskOptional);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("testSchedule");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        scheduleEntity.setJobParametersJson("{}");
        Optional<ScheduleEntity> scheduleEntityOptional = Optional.of(scheduleEntity);
        Mockito.when(scheduleRepository.findById(1L)).thenReturn(scheduleEntityOptional);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(false);

        Mockito.doNothing().when(oscService).updateTaskResult(scheduleTaskId, "{}");

        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);
        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
        assertTrue(result.isManualSwapTableEnabled());
        assertFalse(result.isManualSwapTableStarted());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTableWhenJobTypeNotOnlineSchemaChange() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(scheduleTask);
        Mockito.when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(scheduleTaskOptional);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("testSchedule");
        scheduleEntity.setJobType(JobType.SCHEDULE_TASK);
        scheduleEntity.setJobParametersJson("{}");
        Optional<ScheduleEntity> scheduleEntityOptional = Optional.of(scheduleEntity);
        Mockito.when(scheduleRepository.findById(1L)).thenReturn(scheduleEntityOptional);

        oscService.swapTable(scheduleTaskId);
        fail("IllegalArgumentException should be thrown");
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("OBServer");
        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
        ConnectionSession connSession = factory.generateSession();
        String version = "3.10.0";
        ConnectionSessionUtil.setVersion(connSession, version);

        boolean result = oscService.getLockUserIsRequired(connectionId);
        assertTrue(result);
    }

    @Test
    public void testGetLockUserIsRequiredWhenConnectionFailed() {
        Long connectionId = 1L;

        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("OBServer");
        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
        Exception ex = new RuntimeException("Get connection occur error");
        Mockito.doThrow(ex).when(connectionService).getForConnectionSkipPermissionCheck(connectionId);

        boolean result = oscService.getLockUserIsRequired(connectionId);
        assertFalse(result);
    }

}