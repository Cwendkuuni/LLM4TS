import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

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
import com.oceanbase.odc.service.onlineschemachange.OscService;

import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    private DatabaseRepository databaseRepository;
    private ScheduleRepository scheduleRepository;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ConnectionService connectionService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        databaseRepository = mock(DatabaseRepository.class);
        scheduleRepository = mock(ScheduleRepository.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        connectionService = mock(ConnectionService.class);
        oscService.setDatabaseRepository(databaseRepository);
        oscService.setScheduleRepository(scheduleRepository);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
        oscService.setConnectionService(connectionService);
    }

    // Test for getOscDatabaseInfo method
    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);

        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(id);
        verify(databaseRepository, times(1)).findById(id);
        assertEquals(id, oscDatabaseInfo.getDatabaseId());
        assertEquals(false, oscDatabaseInfo.isLockDatabaseUserRequired());

        verifyNoMoreInteractions(databaseRepository);
    }

    // Test for swapTable method with valid input
    @Test
    public void testSwapTableWithValidInput() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("schedule1");
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(new DatabaseEntity()));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);

        verify(scheduleRepository, times(1)).findById(1L);
        verify(databaseRepository, atLeastOnce()).findById(anyLong());
        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
        verify(connectionService, never()).getForConnectionSkipPermissionCheck(anyLong());

        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());

        verifyNoMoreInteractions(databaseRepository, scheduleRepository, scheduleTaskRepository, connectionService);
    }

    // Test for swapTable method with invalid input - non-existent schedule task
    @Test(expected = RuntimeException.class)
    public void testSwapTableWithInvalidInput1() {
        Long scheduleTaskId = 1L;
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());

        oscService.swapTable(scheduleTaskId);
    }

    // Test for swapTable method with invalid input - non-existent schedule
    @Test(expected = RuntimeException.class)
    public void testSwapTableWithInvalidInput2() {
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("schedule1");
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());

        oscService.swapTable(scheduleTaskId);
    }

    // Test for swapTable method with invalid input - non-ONLINE_SCHEMA_CHANGE_COMPLETE schedule
    @Test(expected = RuntimeException.class)
    public void testSwapTableWithInvalidInput3() {
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("schedule1");
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.SCHEDULE);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        oscService.swapTable(scheduleTaskId);
    }

    // Test for getLockUserIsRequired method
    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("dialectType");

        OngoingStubbing<Boolean> returnValue = doReturn(true).when(connectionService)
                .getForConnectionSkipPermissionCheck(connectionId);

        boolean result = oscService.getLockUserIsRequired(connectionId);
        assertTrue(result);

        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(connectionId);
        returnValue.verify();

        verifyNoMoreInteractions(connectionService);
    }

    // Test for getLockUserIsRequired method with outdated connection config
    @Test
    public void testGetLockUserIsRequiredWithOutdatedConnectionConfig() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("dialectType");

        OngoingStubbing<Boolean> connectionCheckResult = doReturn(false).when(connectionService)
                .getForConnectionSkipPermissionCheck(connectionId);

        PreConditions.expectException(RuntimeException.class);
        oscService.getLockUserIsRequired(connectionId);

        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(connectionId);
        connectionCheckResult.verify();

        verifyNoMoreInteractions(connectionService);
    }

    // Test for getLockUserIsRequired method with invalid connection config
    @Test
    public void testGetLockUserIsRequiredWithInvalidConnectionConfig() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("dialectType");

        OngoingStubbing<ConnectionConfig> connectionConfig = doReturn(decryptedConnConfig)
                .when(connectionService).getForConnectionSkipPermissionCheck(connectionId);

        PreConditions.expectException(RuntimeException.class);
        oscService.getLockUserIsRequired(connectionId);

        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(connectionId);
        connectionConfig.verify();

        verifyNoMoreInteractions(connectionService);
    }
}