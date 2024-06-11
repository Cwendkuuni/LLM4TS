package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

@SuppressWarnings("unchecked")
public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ConnectionService connectionService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    // Test getOscDatabaseInfo method

    @Test
    public void testGetOscDatabaseInfoWithValidId() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);

        Optional<DatabaseEntity> database = Optional.of(databaseEntity);

        when(databaseRepository.findById(id)).thenReturn(database);

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertEquals(id, result.getDatabaseId());
        assertFalse(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfoWithInvalidId() {
        Long id = 1L;

        when(databaseRepository.findById(id)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertNull(result);
    }

    // Test swapTable method

    @Test
    public void testSwapTableWithValidScheduleTaskId() {
        Long scheduleTaskId = 1L;

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("scheduleId");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(result));
        scheduleTaskEntity.setJobName(scheduleEntity.getJobName());

        when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        OscSwapTableVO resultOscSwapTable = oscService.swapTable(scheduleTaskId);

        assertEquals(scheduleTaskId, resultOscSwapTable.getScheduleTaskId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTableWithInvalidJobType() {
        Long scheduleTaskId = 1L;

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("scheduleId");
        scheduleEntity.setJobType(JobType.IMPORT_DATA_COMPLETE);

        when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testSwapTableWithInvalidSwapTableType() {
        Long scheduleTaskId = 1L;

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("scheduleId");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.BACKUP);

        when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));

        assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(scheduleTaskId, oscParameters));
    }

    @Test
    public void testSwapTableWithInvalidManualSwapTableStatus() {
        Long scheduleTaskId = 1L;

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("scheduleId");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(result));
        scheduleTaskEntity.setJobName(scheduleEntity.getJobName());

        when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        assertThrows(IllegalStateException.class, () -> oscService.swapTable(scheduleTaskId, oscParameters));
    }

    @Test
    public void testGetLockUserIsRequiredWithDefaultConnection() {
        Long connectionId = 1L;

        ConnectionConfig decryptedConnConfig = new ConnectionConfig();

        assertTrue(oscService.getLockUserIsRequired(connectionId, decryptedConnConfig));
    }

    @Test
    public void testGetLockUserIsRequiredWithConnectionThatDoesNotSupportLockUser() {
        Long connectionId = 1L;

        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("NotSupportLockUser");

        assertFalse(oscService.getLockUserIsRequired(connectionId, decryptedConnConfig));
    }

}