package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfigDto;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;

/**
 * Test class for OscService
 */
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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test for getOscDatabaseInfo method
     */
    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);

        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        verify(databaseRepository).findById(id);
        assertTrue(result.getDatabaseId() == id);
        assertFalse(result.isLockDatabaseUserRequired());
    }

    /**
     * Test for getOscDatabaseInfo method when database is not present
     */
    @Test
    public void testGetOscDatabaseInfoWhenDatabaseNotFound() {
        Long id = 1L;

        when(databaseRepository.findById(id)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        verify(databaseRepository).findById(id);
        assertNull(result);
    }

    /**
     * Test for swapTable method
     */
    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        scheduleEntity.setJobParametersJson("{}");

        when(databaseRepository.findById(anyLong())).thenReturn(Optional.ofNullable(new DatabaseEntity()));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);

        verify(databaseRepository).findById(anyLong());
        verify(scheduleRepository).findById(1L);
        verify(scheduleTaskRepository).findById(scheduleTaskId);
        assertTrue(result.getScheduleTaskId() == scheduleTaskId);
    }

    /**
     * Test for swapTable method when scheduleTask is not present
     */
    @Test(expected = RuntimeException.class)
    public void testSwapTableWhenScheduleTaskNotFound() {
        Long scheduleTaskId = 1L;

        when(databaseRepository.findById(anyLong())).thenReturn(Optional.ofNullable(new DatabaseEntity()));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(new ScheduleEntity()));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());

        oscService.swapTable(scheduleTaskId);
    }

    /**
     * Test for getLockUserIsRequired method
     */
    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        ConnectionConfig connectionConfig = new ConnectionConfigDto();

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .thenReturn(connectionConfig);

        boolean result = oscService.getLockUserIsRequired(connectionId);

        verify(connectionService).getForConnectionSkipPermissionCheck(connectionId);
        assertFalse(result);
    }

    /**
     * Test for getLockUserIsRequired method when connectionConfig is null
     */
    @Test
    public void testGetLockUserIsRequiredWhenConnectionConfigIsNull() {
        Long connectionId = 1L;

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .thenReturn(null);

        boolean result = oscService.getLockUserIsRequired(connectionId);

        verify(connectionService).getForConnectionSkipPermissionCheck(connectionId);
        assertFalse(result);
    }

    /**
     * Test for getLockUserIsRequired method when dialectType is unsupported
     */
    @Test
    public void testGetLockUserIsRequiredWhenDialectTypeUnsupported() {
        Long connectionId = 1L;
        ConnectionConfig connectionConfig = new ConnectionConfigDto();
        connectionConfig.setDialectType("unsupported");

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .thenReturn(connectionConfig);

        boolean result = oscService.getLockUserIsRequired(connectionId);

        verify(connectionService).getForConnectionSkipPermissionCheck(connectionId);
        assertFalse(result);
    }

    /**
     * Test for getLockUserIsRequired method when getVersion fails
     */
    @Test
    public void testGetLockUserIsRequiredWhenGetVersionFails() {
        Long connectionId = 1L;
        ConnectionConfig connectionConfig = new ConnectionConfigDto();

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(connectionConfig);
        when(new DefaultConnectSessionFactory(connectionConfig).generateSession()).thenThrow(new RuntimeException("Error"));

        boolean result = oscService.getLockUserIsRequired(connectionId);

        verify(connectionService).getForConnectionSkipPermissionCheck(connectionId);
        verify(new DefaultConnectSessionFactory(connectionConfig)).generateSession();
        assertFalse(result);
    }

    /**
     * Test for getLockUserIsRequired method when getVersion returns null
     */
    @Test
    public void testGetLockUserIsRequiredWhenVersionIsNull() {
        Long connectionId = 1L;
        ConnectionConfig connectionConfig = new ConnectionConfigDto();
        String version = null;

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(connectionConfig);
        when(new DefaultConnectSessionFactory(connectionConfig).generateSession())
                .thenReturn(mock(ConnectionSession.class));
        when(ConnectionSessionUtil.getVersion(mock(ConnectionSession.class))).thenReturn(version);

        boolean result = oscService.getLockUserIsRequired(connectionId);

        verify(connectionService).getForConnectionSkipPermissionCheck(connectionId);
        verify(new DefaultConnectSessionFactory(connectionConfig)).generateSession();
        verify(ConnectionSessionUtil.getVersion(mock(ConnectionSession.class))).times(2);
        assertFalse(result);
    }

    /**
     * Test for getLockUserIsRequired method when getVersion returns a version
     */
    @Test
    public void testGetLockUserIsRequiredWhenVersionIsPresent() {
        Long connectionId = 1L;
        ConnectionConfig connectionConfig = new ConnectionConfigDto();
        String version = "5.5.21";

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(connectionConfig);
        when(new DefaultConnectSessionFactory(connectionConfig).generateSession())
                .thenReturn(mock(ConnectionSession.class));
        when(ConnectionSessionUtil.getVersion(mock(ConnectionSession.class))).thenReturn(version);

        boolean result = oscService.getLockUserIsRequired(connectionId);

        verify(connectionService).getForConnectionSkipPermissionCheck(connectionId);
        verify(new DefaultConnectSessionFactory(connectionConfig)).generateSession();
        verify(ConnectionSessionUtil.getVersion(mock(ConnectionSession.class))).times(2);
        assertTrue(result);
    }
}