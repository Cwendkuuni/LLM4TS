import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.model.PreConditions;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    @Test
    public void testGetOscDatabaseInfo() {
        DatabaseEntity database = new DatabaseEntity();
        database.setDatabaseId(1L);
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(database));
        OscLockDatabaseUserInfo expectedOscDatabase = new OscLockDatabaseUserInfo();
        expectedOscDatabase.setDatabaseId(database.getDatabaseId());
        PreConditions preConditions = PreConditions.instance();
        expectedOscDatabase.setLockDatabaseUserRequired(preConditions.getLockDatabaseUserIsRequired(1L));
        OscLockDatabaseUserInfo actualOscDatabase = oscService.getOscDatabaseInfo(1L);
        assertNotNull(actualOscDatabase);
        assertEquals(expectedOscDatabase, actualOscDatabase);
    }

    @Test
    public void testGetOscDatabaseInfo_DatabaseDoesNotExist() {
        when(databaseRepository.findById(1L)).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo actualOscDatabase = oscService.getOscDatabaseInfo(1L);
        assertNull(actualOscDatabase);
    }

    @Test(expected = RuntimeException.class)
    public void testSwapTable_InvalidScheduleTaskId() {
        Long scheduleTaskId = -1L;
        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testSwapTable_GetDatabaseEntity_ThrowException() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTask));
        DatabaseEntity database = new DatabaseEntity();
        database.setDatabaseId(1L);
        doThrow(new RuntimeException("Test Exception")).when(databaseRepository).findById(1L);

        assertThrows(RuntimeException.class, () -> {
            oscService.swapTable(1L);
        });

        verify(databaseRepository, times(1)).findById(1L);
    }

    @Test
    public void testSwapTable_GetDatabaseEntity_ReturnNull() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTask));
        when(databaseRepository.findById(1L)).thenReturn(Optional.empty());

        oscService.swapTable(1L);
    }

    @Test
    public void testSwapTable_GetDatabaseEntity_ReturnDatabaseEntity() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        DatabaseEntity database = new DatabaseEntity();
        database.setDatabaseId(1L);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTask));
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(database));

        oscService.swapTable(1L);

        verify(databaseRepository, times(1)).findById(1L);
        verify(databaseRepository, times(0)).save(any(DatabaseEntity.class));
        verify(scheduleTaskRepository, times(1)).updateTaskResult(1L, anyString());
    }

    @Test
    public void testSwapTable_GetDatabaseEntity_SaveDatabaseEntity() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        DatabaseEntity database = new DatabaseEntity();
        database.setDatabaseId(1L);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTask));
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(database));
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("OBServer");
        when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(connectionConfig);
        when(databaseRepository.save(any(DatabaseEntity.class))).thenReturn(database);

        oscService.swapTable(1L);

        verify(databaseRepository, times(1)).findById(1L);
        verify(databaseRepository, times(1)).save(database);
        verify(scheduleTaskRepository, times(1)).updateTaskResult(1L, anyString());
    }

    @Test
    public void testSwapTable_GetDatabaseEntity_SaveDatabaseEntity_ThrowException() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        DatabaseEntity database = new DatabaseEntity();
        database.setDatabaseId(1L);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTask));
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(database));
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("OBServer");
        when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(connectionConfig);
        doThrow(new RuntimeException("Test Exception")).when(databaseRepository).save(database);

        assertThrows(RuntimeException.class, () -> {
            oscService.swapTable(1L);
        });

        verify(databaseRepository, times(1)).findById(1L);
        verify(databaseRepository, times(1)).save(database);
        verify(scheduleTaskRepository, times(1)).updateTaskResult(1L, anyString());
    }

    @Test
    public void testGetLockUserIsRequired() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("OBServer");
        when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(connectionConfig);
        ConnectionSessionFactory factory = mock(ConnectionSessionFactory.class);
        when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(connectionConfig);
        ConnectionSession connSession = mock(ConnectionSession.class);
        when(factory.generateSession()).thenReturn(connSession);
        String version = "1.0.0";
        ConnectionSessionUtil.whenGetVersion(connSession).thenReturn(version);

        boolean actualLockUserIsRequired = oscService.getLockUserIsRequired(1L);
        boolean expectedLockUserIsRequired = OscDBUserUtil.isLockUserRequired(connectionConfig.getDialectType(), () -> {
            ConnectionSessionFactory factory = new DefaultConnectSessionFactory(connectionConfig);
            ConnectionSession connSession = factory.generateSession();
            String version = ConnectionSessionUtil.getVersion(connSession);
            return version;
        });

        assertEquals(expectedLockUserIsRequired, actualLockUserIsRequired);
    }

    @Test
    public void testGetLockUserIsRequired_ConnectionServiceGetForConnectionSkipPermissionCheck_ThrowException() {
        doThrow(new RuntimeException("Test Exception")).when(connectionService).getForConnectionSkipPermissionCheck(1L);

        boolean actualLockUserIsRequired = oscService.getLockUserIsRequired(1L);
        assertFalse(actualLockUserIsRequired);

        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(1L);
    }
}