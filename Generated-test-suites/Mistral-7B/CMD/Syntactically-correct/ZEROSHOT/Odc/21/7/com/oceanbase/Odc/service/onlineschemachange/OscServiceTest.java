import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.ConnectionService;
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
    private ConnectionService connectionService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    private DatabaseEntity databaseEntity;
    private ScheduleEntity scheduleEntity;
    private ScheduleTaskEntity scheduleTaskEntity;
    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters oscParameters;
    private OnlineSchemaChangeScheduleTaskResult result;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setScheduleTaskId(1L);
        scheduleTaskEntity.setResultJson("{}");
        connectionConfig = new ConnectionConfig();
        oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        result = new OnlineSchemaChangeScheduleTaskResult();
    }

    @Test
    public void testGetOscDatabaseInfo() {
        when(databaseRepository.findById(databaseEntity.getDatabaseId())).thenReturn(Optional.of(databaseEntity));
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(databaseEntity.getDatabaseId());
        assertEquals(databaseEntity.getDatabaseId(), oscDatabase.getDatabaseId());
        assertFalse(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfoWithLockUserRequired() {
        databaseEntity.setConnectionId(1L);
        when(databaseRepository.findById(databaseEntity.getDatabaseId())).thenReturn(Optional.of(databaseEntity));
        when(connectionService.getForConnectionSkipPermissionCheck(databaseEntity.getConnectionId()))
                .thenReturn(connectionConfig);
        when(connectionConfig.getDialectType()).thenReturn("OBServer");
        when(connectionService.getForConnectionSkipPermissionCheck(databaseEntity.getConnectionId())).thenReturn(connectionConfig);
        oscService.getOscDatabaseInfo(databaseEntity.getDatabaseId());
        assertTrue(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOscDatabaseInfoWithNullDatabaseEntity() {
        when(databaseRepository.findById(databaseEntity.getDatabaseId()))
                .thenReturn(Optional.empty());
        oscService.getOscDatabaseInfo(databaseEntity.getDatabaseId());
    }

    @Test
    public void testSwapTable() {
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        when(databaseRepository.findById(databaseEntity.getDatabaseId())).thenReturn(Optional.of(databaseEntity));
        when(scheduleRepository.findById(Long.parseLong(scheduleTaskEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        oscService.swapTable(scheduleTaskId);
        assertNull(result.getManualSwapTableEnabled());
        assertNull(result.getManualSwapTableStarted());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTableWithNullScheduleTaskEntity() {
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());
        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testSwapTableWithInvalidJobType() {
        scheduleEntity.setJobType(JobType.BACKUP_COMPLETE);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        when(databaseRepository.findById(databaseEntity.getDatabaseId()))
                .thenReturn(Optional.of(databaseEntity));
        when(scheduleRepository.findById(Long.parseLong(scheduleTaskEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        assertThrows(IllegalArgumentException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });
    }

    @Test
    public void testSwapTableWithInvalidSwapTableType() {
        oscParameters.setSwapTableType(SwapTableType.BACKUP);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        when(databaseRepository.findById(databaseEntity.getDatabaseId()))
                .thenReturn(Optional.of(databaseEntity));
        when(scheduleRepository.findById(Long.parseLong(scheduleTaskEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskEntity.getResultJson()).thenReturn("{}");
        result = JsonUtils.fromJson(scheduleTaskEntity.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class);
        assertThrows(IllegalArgumentException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });
    }

    @Test
    public void testSwapTableWithSwapTableDisabled() {
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        result.setManualSwapTableEnabled(false);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        when(databaseRepository.findById(databaseEntity.getDatabaseId()))
                .thenReturn(Optional.of(databaseEntity));
        when(scheduleRepository.findById(Long.parseLong(scheduleTaskEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskEntity.getResultJson()).thenReturn("{}");
        result = JsonUtils.fromJson(scheduleTaskEntity.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class);
        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testSwapTableWithSwapTableEnabled() {
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        result.setManualSwapTableEnabled(true);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        when(databaseRepository.findById(databaseEntity.getDatabaseId()))
                .thenReturn(Optional.of(databaseEntity));
        when(scheduleRepository.findById(Long.parseLong(scheduleTaskEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskEntity.getResultJson()).thenReturn("{}");
        result = JsonUtils.fromJson(scheduleTaskEntity.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class);
        oscService.swapTable(scheduleTaskId);
        assertFalse(result.isManualSwapTableEnabled());
        assertTrue(result.isManualSwapTableStarted());
    }

}