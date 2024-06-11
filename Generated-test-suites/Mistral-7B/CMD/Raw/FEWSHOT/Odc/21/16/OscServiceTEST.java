package com.oceanbase.odc.service.onlineschemachange;

import static com.oceanbase.odc.core.shared.PreConditions.validExists;
import static com.oceanbase.odc.core.shared.PreConditions.validArgumentState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
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

    @InjectMocks
    private OscService oscService;

    @Mock
    private DatabaseRepository databaseRepository;
    @Mock
    private ConnectionService connectionService;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private ScheduleRepository scheduleRepository;

    private static final Log log = LogFactory.getLog(OscService.class);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void cleanup() {
    }

    @Test
    public void testGetOscDatabaseInfo_DatabaseIdNotFound_ReturnsEmptyOscDatabaseInfo() {
        when(databaseRepository.findById(1L)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(1L);

        assertTrue(oscDatabaseInfo.isEmpty());
    }

    @Test
    public void testGetOscDatabaseInfo_DatabaseFound_ReturnsOscDatabaseInfo() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        databaseEntity.setConnectionId(1L);

        when(databaseRepository.findById(1L)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(1L);

        assertFalse(oscDatabaseInfo.isEmpty());
        assertEquals(1L, oscDatabaseInfo.getDatabaseId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLockUserIsRequired_ConnectionIdIsNull_ThrowsException() {
        oscService.getLockUserIsRequired(null);
    }

    @Test
    public void testGetLockUserIsRequired_LockUserRequired_ReturnsTrue() {
        ConnectionConfig decryptedConnConfig = mock(ConnectionConfig.class);
        when(decryptedConnConfig.getDialectType()).thenReturn("MySQL");
        when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(decryptedConnConfig);

        assertTrue(oscService.getLockUserIsRequired(1L));
    }

    @Test
    public void testGetLockUserIsRequired_LockUserNotRequired_ReturnsFalse() {
        ConnectionConfig decryptedConnConfig = mock(ConnectionConfig.class);
        when(decryptedConnConfig.getDialectType()).thenReturn("Oracle");
        when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(decryptedConnConfig);

        assertFalse(oscService.getLockUserIsRequired(1L));
    }

    @Test
    public void testSwapTable_ScheduleTaskNotFound_ReturnsEmptyOscSwapTableVO() {
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);

        assertTrue(oscSwapTable.isEmpty());
    }

    @Test
    public void testSwapTable_ScheduleTaskFound_ReturnsOscSwapTableVO() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setResultJson("{}");

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);

        assertFalse(oscSwapTable.isEmpty());
        assertEquals(1L, oscSwapTable.getScheduleTaskId());
    }
}