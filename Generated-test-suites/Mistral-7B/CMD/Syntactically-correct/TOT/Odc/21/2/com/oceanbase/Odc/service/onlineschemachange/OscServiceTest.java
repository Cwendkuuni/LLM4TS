package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import com.oceanbase.odc.service.onlineschemachange.OscService;

@SuppressWarnings("unchecked")
public class OscServiceTest {

    private OscService oscService;
    private ConnectionService connectionServiceMock;
    private ScheduleTaskRepository scheduleTaskRepositoryMock;

    @Before
    public void setUp() {
        connectionServiceMock = mock(ConnectionService.class);
        scheduleTaskRepositoryMock = mock(ScheduleTaskRepository.class);
        oscService = new OscService() {
            {
                this.connectionService = connectionServiceMock;
                this.scheduleTaskRepository = scheduleTaskRepositoryMock;
            }
        };
    }

    @Test
    public void testGetOscDatabaseInfo_DatabaseNotFound() {
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
        assertTrue(result == null);
    }

    @Test
    public void testGetOscDatabaseInfo_DatabaseFound() {
        Long databaseId = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(databaseId);
        when(databaseRepository.findById(databaseId)).thenReturn(Optional.of(databaseEntity));
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(databaseId);
        assertEquals(databaseId, result.getDatabaseId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_InvalidJobType() {
        Long scheduleTaskId = 1L;
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(new ScheduleEntity()));
        oscService.swapTable(scheduleTaskId);
        fail("IllegalArgumentException expected but not thrown");
    }

    @Test
    public void testSwapTable_Success() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setJobName("1");
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        doReturn(oscParameters).when(oscService).getSwapTableParametersFromSchedule(scheduleEntity);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        doReturn(result).when(oscService).swapTableInternal(anyLong(), anyLong(), anyBoolean(), anyBoolean());
        oscService.swapTable(scheduleTaskId);
        verify(scheduleTaskRepository).updateTaskResult(scheduleTaskId, anyString());
    }

    @Test
    public void testGetLockUserIsRequired_ConnectionOk() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("OBDC");
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);
        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
        ConnectionSession connSession = factory.generateSession();
        String version = "v4.2.3";
        when(ConnectionSessionUtil.getVersion(connSession)).thenReturn(version);
        assertTrue(oscService.getLockUserIsRequired(connectionId));
    }

    @Test
    public void testGetLockUserIsRequired_ConnectionFailed() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenThrow(new RuntimeException());
        assertFalse(oscService.getLockUserIsRequired(connectionId));
    }

    @Test
    public void testGetLockUserIsRequired_NoUserRequired() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);
        assertFalse(OscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType(), () -> {
            return null;
        }));
    }
}