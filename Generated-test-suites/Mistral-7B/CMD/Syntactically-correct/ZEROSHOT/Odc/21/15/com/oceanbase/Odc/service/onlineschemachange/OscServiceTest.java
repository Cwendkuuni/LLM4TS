import static org.junit.Assert. assertFalse;
import static org.junit.Assert. assertNull;
import static org.junit.Assert. assertSame;
import static org.junit.Assert. assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.JobType;
import com.oceanbase.odc.metadb.schedule.OnlineSchemaChangeParameters;
import com.oceanbase.odc.metadb.schedule.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SuppressWarnings({"unchecked", "rawtypes"})
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
    public void testGetOscDatabaseInfoWithInvalidDatabaseId() {
        Long id = 1L;
        when(databaseRepository.findById(id)).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(id);
        assertNull(oscDatabaseInfo);
    }

    @Test
    public void testGetOscDatabaseInfoWithValidDatabaseId() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(id);
        assertSame(id, oscDatabaseInfo.getDatabaseId());
        assertFalse(oscDatabaseInfo.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTableWithInvalidScheduleTaskId() {
        Long scheduleTaskId = 1L;
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());
        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testSwapTableWithInvalidJobType() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("schedule_1");
        scheduleTaskEntity.setJobType(JobType.BACKUP);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        assertThrows(IllegalStateException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });
    }

    @Test
    public void testSwapTableWithInvalidSwapTableType() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("schedule_1");
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
        when(scheduleRepository.findById(Long.parseLong(scheduleTaskEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        assertThrows(IllegalArgumentException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });
    }

    @Test
    public void testSwapTableWithInvalidManualSwapTableEnabled() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("schedule_1");
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
        scheduleEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));
        when(scheduleRepository.findById(Long.parseLong(scheduleTaskEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleTaskRepository.updateTaskResult(scheduleTaskId, (String) anyObject())).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });
    }

    @Test
    public void testSwapTableWithSuccess() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("schedule_1");
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
        scheduleEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));
        when(scheduleRepository.findById(Long.parseLong(scheduleTaskEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleTaskRepository.updateTaskResult(scheduleTaskId, (String) anyObject())).thenReturn(true);

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testGetLockUserIsRequiredWithValidConnectionConfig() {
        Long connectionId = 1L;
        ConnectionConfig connectionConfig = new ConnectionConfig();
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(connectionConfig);
        OscLockDatabaseUserInfo oscLockDatabaseUserInfo = oscService.getLockUserIsRequired(connectionId);
        assertFalse(oscLockDatabaseUserInfo.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetLockUserIsRequiredWithInvalidConnectionConfig() {
        Long connectionId = 1L;
        ConnectionConfig connectionConfig = new ConnectionConfig();
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenThrow(new RuntimeException());
        OscLockDatabaseUserInfo oscLockDatabaseUserInfo = oscService.getLockUserIsRequired(connectionId);
        assertTrue(oscLockDatabaseUserInfo.isLockDatabaseUserRequired());
    }
}