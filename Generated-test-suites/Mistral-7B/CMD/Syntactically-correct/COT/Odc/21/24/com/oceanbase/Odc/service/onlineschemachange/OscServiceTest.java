package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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

@SuppressWarnings("unchecked")
public class OscServiceTest {

    private DatabaseRepository databaseRepository;
    private ScheduleRepository scheduleRepository;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ConnectionService connectionService;

    @Before
    public void setup() {
        databaseRepository = mock(DatabaseRepository.class);
        scheduleRepository = mock(ScheduleRepository.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        connectionService = mock(ConnectionService.class);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        when(databaseRepository.findById(id)).thenReturn(Optional.of(new DatabaseEntity()));

        OscService oscService = new OscService();
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertTrue(result.getDatabaseId() == 1);
        assertFalse(result.isLockDatabaseUserRequired());

        verify(databaseRepository, times(1)).findById(id);
    }

    @Test
    public void testGetOscDatabaseInfo_DatabaseNotFound() {
        Long id = 1L;
        when(databaseRepository.findById(id)).thenReturn(Optional.empty());

        OscService oscService = new OscService();
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertNull(result.getDatabaseId());

        verify(databaseRepository, times(1)).findById(id);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleRepository.findById(Long.parseLong(scheduleTaskEntity.getJobName()))).thenReturn(Optional.of(new ScheduleEntity()));

        OscService oscService = new OscService();
        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);

        assertEquals(scheduleTaskId, result.getScheduleTaskId());

        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
        verify(scheduleRepository, times(1)).findById(Long.parseLong(scheduleTaskEntity.getJobName()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_JobTypeNotOnlineSchemaChange() {
        Long scheduleTaskId = 1L;
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleRepository.findById(Long.parseLong(scheduleTaskEntity.getJobName()))).thenReturn(Optional.of(new ScheduleEntity()));

        OscService oscService = new OscService();
        oscService.swapTable(scheduleTaskId);

        fail("IllegalArgumentException expected but not thrown");

        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
        verify(scheduleRepository, times(1)).findById(Long.parseLong(scheduleTaskEntity.getJobName()));
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);

        boolean result = OscService.getInstance().getLockUserIsRequired(connectionId);

        assertTrue(result);

        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(connectionId);
    }

    @Test
    public void testGetLockUserIsRequired_DecryptedConnectionConfigNull() {
        Long connectionId = 1L;
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(null);

        boolean result = OscService.getInstance().getLockUserIsRequired(connectionId);

        assertFalse(result);

        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(connectionId);
    }

    @Test
    public void testGetLockUserIsRequired_ConnectionSessionFactoryThrowException() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        ConnectionSessionFactory factory = Mockito.mock(ConnectionSessionFactory.class);
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);
        when(factory.generateSession()).thenThrow(new RuntimeException());

        boolean result = OscService.getInstance().getLockUserIsRequired(connectionId);

        assertFalse(result);

        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(connectionId);
        verify(factory, times(1)).generateSession();
    }

    @Test
    public void testGetLockUserIsRequired_ConnectionSessionVersionNull() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
        ConnectionSession connSession = Mockito.mock(ConnectionSession.class);
        when(factory.generateSession()).thenReturn(connSession);
        when(ConnectionSessionUtil.getVersion(connSession)).thenReturn(null);

        boolean result = OscService.getInstance().getLockUserIsRequired(connectionId);

        assertFalse(result);

        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(connectionId);
        verify(factory, times(1)).generateSession();
        verify(connSession, times(1)).expire();
    }

    @Test
    public void testGetLockUserIsRequired_ConnectionSessionVersionObsolete() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
        ConnectionSession connSession = Mockito.mock(ConnectionSession.class);
        when(factory.generateSession()).thenReturn(connSession);
        when(ConnectionSessionUtil.getVersion(connSession)).thenReturn("3.0.1");

        boolean result = OscService.getInstance().getLockUserIsRequired(connectionId);

        assertFalse(result);

        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(connectionId);
        verify(factory, times(1)).generateSession();
        verify(connSession, times(1)).expire();
    }
}