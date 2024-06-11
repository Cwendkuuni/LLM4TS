package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

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
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;

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

    @Test
    public void testGetOscDatabaseInfo_databaseNotFound() {
        Optional<DatabaseEntity> databaseOptional = Optional.empty();
        when(databaseRepository.findById((Long) 1L)).thenReturn(databaseOptional);

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);

        assertNull(result);
    }

    @Test
    public void testGetOscDatabaseInfo_databaseFound() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);

        Optional<DatabaseEntity> databaseOptional = Optional.of(databaseEntity);
        when(databaseRepository.findById((Long) 1L)).thenReturn(databaseOptional);

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);

        assertSame(databaseEntity.getDatabaseId(), result.getDatabaseId());
        assertTrue(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable_scheduleTaskNotFound() {
        Long scheduleTaskId = 1L;

        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.empty();
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(scheduleTaskOptional);

        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);

        assertNull(result);
    }

    @Test
    public void testSwapTable_scheduleTaskFound() {
        Long scheduleTaskId = 1L;

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);

        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(scheduleTask);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(scheduleTaskOptional);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson("{}");

        Optional<ScheduleEntity> scheduleEntityOptional = Optional.of(scheduleEntity);
        when(scheduleRepository.findById(1L)).thenReturn(scheduleEntityOptional);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        when(scheduleTask.getResultJson()).thenReturn(JsonUtils.toJson(result));

        when(scheduleEntity.getJobType()).thenReturn(JobType.ONLINE_SCHEMA_CHANGE);

        OscSwapTableVO oscSwapTable = new OscSwapTableVO();
        oscSwapTable.setScheduleTaskId(scheduleTaskId);

        oscSwapTable = oscService.swapTable(scheduleTaskId);

        assertSame(scheduleTaskId, oscSwapTable.getScheduleTaskId());
    }

    @Test
    public void testGetLockUserIsRequired_versionNull() {
        ConnectionConfig decryptedConnConfig = mock(ConnectionConfig.class);

        when(decryptedConnConfig.getDialectType()).thenReturn("unknown");

        boolean result = oscService.getLockUserIsRequired(1L);

        assertFalse(result);
    }

    @Test
    public void testGetLockUserIsRequired_versionNotNull() {
        ConnectionConfig decryptedConnConfig = mock(ConnectionConfig.class);
        ConnectionSessionFactory factory = mock(ConnectionSessionFactory.class);
        ConnectionSession connSession = mock(ConnectionSession.class);
        String version = "5.0.0";

        when(decryptedConnConfig.getDialectType()).thenReturn("MySQL");
        when(decryptedConnConfig.getDialectVersion()).thenReturn(version);
        when(factory.generateSession()).thenReturn(connSession);
        when(ConnectionSessionUtil.getVersion(connSession)).thenReturn(version);

        assertTrue(oscService.getLockUserIsRequired(1L));
    }
}