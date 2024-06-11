import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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

    // Test method: getOscDatabaseInfo

    @Test
    public void testGetOscDatabaseInfoWithExistingDatabase() {
        Long databaseId = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(databaseId);
        databaseEntity.setConnectionId(1L);
        when(databaseRepository.findById(databaseId)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(databaseId);

        assertTrue(oscDatabase.isPresent());
        assertEquals(databaseId, oscDatabase.get().getDatabaseId());
        assertTrue(oscDatabase.get().isLockDatabaseUserRequired());

        verify(databaseRepository).findById(databaseId);
    }

    @Test
    public void testGetOscDatabaseInfoWithNonExistingDatabase() {
        Long databaseId = 1L;

        when(databaseRepository.findById(databaseId)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(databaseId);

        assertFalse(oscDatabase.isPresent());

        verify(databaseRepository).findById(databaseId);
    }

    // Test method: swapTable

    @Test
    public void testSwapTableWithValidScheduleTask() {
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("scheduleId1");
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setResultJson("{}");
        when(scheduleRepository.findById("scheduleId1")).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);

        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());

        verify(scheduleRepository).findById("scheduleId1");
        verify(scheduleTaskRepository).findById(scheduleTaskId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTableWithInvalidScheduleTask() {
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("scheduleId1");
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_IN_PROGRESS);
        when(scheduleRepository.findById("scheduleId1")).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testSwapTableWithExistingSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("scheduleId1");
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setResultJson("{\"manualSwapTableEnabled\":true,\"manualSwapTableStarted\":false}");
        when(scheduleRepository.findById("scheduleId1")).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class);

        assertFalse(result.isManualSwapTableEnabled());
        assertFalse(result.isManualSwapTableStarted());

        oscService.swapTable(scheduleTaskId);

        assertTrue(result.isManualSwapTableEnabled());
        assertFalse(result.isManualSwapTableStarted());

        verify(scheduleRepository).findById("scheduleId1");
        verify(scheduleTaskRepository).findById(scheduleTaskId);
    }

    @Test
    public void testSwapTableWithNotFoundDatabase() {
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("scheduleId1");
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        when(scheduleRepository.findById("scheduleId1")).thenReturn(Optional.of(scheduleEntity));

        assertThrows(IllegalArgumentException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });

        verify(scheduleRepository).findById("scheduleId1");
    }

    // Test method: getLockUserIsRequired

    @Test
    public void testGetLockUserIsRequiredWithConnectionRequiresLockUser() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);
        when(OscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType(), any())).thenReturn(true);

        assertTrue(oscService.getLockUserIsRequired(connectionId));

        verify(connectionService).getForConnectionSkipPermissionCheck(connectionId);
        verify(OscDBUserUtil).isLockUserRequired(eq(decryptedConnConfig.getDialectType()), any());
    }

    @Test
    public void testGetLockUserIsRequiredWithConnectionDoesNotRequireLockUser() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);
        when(OscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType(), any())).thenReturn(false);

        assertFalse(oscService.getLockUserIsRequired(connectionId));

        verify(connectionService).getForConnectionSkipPermissionCheck(connectionId);
        verify(OscDBUserUtil).isLockUserRequired(eq(decryptedConnConfig.getDialectType()), any());
    }
}