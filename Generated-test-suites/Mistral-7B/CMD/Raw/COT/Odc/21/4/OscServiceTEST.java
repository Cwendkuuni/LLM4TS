package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
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
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.core.shared.constant.SwapTableType;
import com.oceanbase.odc.core.shared.exception.PreconditionsException;

/**
 * OscServiceTest.java
 */
@SuppressWarnings("unchecked")
public class OscServiceTest {

    private DatabaseRepository databaseRepository;
    private ScheduleRepository scheduleRepository;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ConnectionService connectionService;
    private OscService oscService;

    @Before
    public void setup() {
        databaseRepository = mock(DatabaseRepository.class);
        scheduleRepository = mock(ScheduleRepository.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        connectionService = mock(ConnectionService.class);
        oscService = new OscService();
        oscService.setDatabaseRepository(databaseRepository);
        oscService.setConnectionService(connectionService);
        oscService.setScheduleRepository(scheduleRepository);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testGetOscDatabaseInfo_ValidDatabaseId() {
        long databaseId = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(databaseId);
        when(databaseRepository.findById(databaseId)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(databaseId);
        assertEquals(databaseId, result.getDatabaseId());
        verify(databaseRepository, times(1)).findById(databaseId);
    }

    @Test
    public void testGetOscDatabaseInfo_InvalidDatabaseId() {
        long databaseId = -1L;

        when(databaseRepository.findById(databaseId)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(databaseId);
        assertNull(result);
        verify(databaseRepository, times(1)).findById(databaseId);
    }

    @Test
    public void testSwapTable_ValidParameters() {
        long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        scheduleEntity.setJobParametersJson("{}");

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(jsonUtils().fromJson(scheduleEntity.getJobParametersJson(), OnlineSchemaChangeParameters.class))
                .thenReturn(oscParameters);
        when(jsonUtils().fromJson(scheduleTask.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class))
                .thenReturn(result);

        OscSwapTableVO swapTable = oscService.swapTable(scheduleTaskId);
        assertEquals(scheduleTaskId, swapTable.getScheduleTaskId());
        verify(scheduleRepository, times(1)).findById(1L);
        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
        verifyNoMoreInteractions(databaseRepository, connectionService, jsonUtils());
    }

    @Test(expected = PreconditionsException.class)
    public void testSwapTable_InvalidJobType() {
        long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.BACKUP);
        scheduleEntity.setJobParametersJson("{}");

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(jsonUtils().fromJson(scheduleEntity.getJobParametersJson(), OnlineSchemaChangeParameters.class))
                .thenReturn(oscParameters);

        oscService.swapTable(scheduleTaskId);
    }

    @Test(expected = PreconditionsException.class)
    public void testSwapTable_InvalidSwapTableType() {
        long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        scheduleEntity.setJobParametersJson("{}");

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.EXPORT);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(jsonUtils().fromJson(scheduleEntity.getJobParametersJson(), OnlineSchemaChangeParameters.class))
                .thenReturn(oscParameters);

        oscService.swapTable(scheduleTaskId);
    }

    @Test(expected = PreconditionsException.class)
    public void testSwapTable_InvalidManualSwapTableEnabled() {
        long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        scheduleEntity.setJobParametersJson("{}");

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(jsonUtils().fromJson(scheduleEntity.getJobParametersJson(), OnlineSchemaChangeParameters.class))
                .thenReturn(oscParameters);
        when(jsonUtils().fromJson(scheduleTask.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class))
                .thenReturn(result);

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testGetLockUserIsRequired() {
        long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("OBServer");

        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
        String version = "3.0.1";
        ConnectionSession connSession = Mockito.mock(ConnectionSession.class);

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);
        when(factory.generateSession()).thenReturn(connSession);
        when(ConnectionSessionUtil.getVersion(connSession)).thenReturn(version);

        boolean result = oscService.getLockUserIsRequired(connectionId);
        assertTrue(result);
        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(connectionId);
        verify(factory, times(1)).generateSession();
        verify(connSession, times(1)).expire();
    }

    @Test
    public void testGetLockUserIsRequired_NoConnection() {
        long connectionId = 1L;

        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("OBServer");

        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);

        boolean result = oscService.getLockUserIsRequired(connectionId);
        assertFalse(result);
        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(connectionId);
        verifyNoMoreInteractions(factory);
    }

}