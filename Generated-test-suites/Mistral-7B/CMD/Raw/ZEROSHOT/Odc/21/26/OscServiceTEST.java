import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SuppressWarnings("ResultOfMethodCallIgnored")
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
        Long id = 0L;
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);
        assertNull(oscDatabase);
    }

    @Test
    public void testGetOscDatabaseInfoWithValidDatabaseId() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);
        assertEquals(id, oscDatabase.getDatabaseId());
        assertFalse(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTableWithInvalidScheduleTaskId() {
        Long scheduleTaskId = 0L;
        oscService.swapTable(scheduleTaskId);
        // no exception thrown is a successful test
    }

    @Test
    public void testSwapTableWithValidScheduleTaskId() {
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("scheduleId");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson("{\"SwapTableType\":\"MANUAL\"}");

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        scheduleTaskEntity.setResultJson("{\"ManualSwapTableEnabled\":true}");
        scheduleTaskEntity.setScheduleId(1L);
        scheduleTaskEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_TASK);

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));

        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);
        assertEquals(scheduleTaskId, result.getScheduleTaskId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTableWithInvalidJobTypeInSchedule() {
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("scheduleId");
        scheduleEntity.setJobType(JobType.DATA_SYNC);
        scheduleEntity.setJobParametersJson("{\"SwapTableType\":\"MANUAL\"}");

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        oscService.swapTable(scheduleTaskId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTableWithInvalidSwapTableType() {
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("scheduleId");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson("{\"SwapTableType\":\"INVALID\"}");

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        oscService.swapTable(scheduleTaskId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTableWithInvalidManualSwapTableStatus() {
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("scheduleId");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson("{\"SwapTableType\":\"MANUAL\"}");

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        scheduleTaskEntity.setResultJson("{\"ManualSwapTableEnabled\":false}");
        scheduleTaskEntity.setScheduleId(1L);
        scheduleTaskEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_TASK);

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testGetLockUserIsRequiredWithConnectionIdNotSupportLockUser() {
        Long connectionId = 1L;
        assertFalse(oscService.getLockUserIsRequired(connectionId));
    }

    @Test
    public void testGetLockUserIsRequiredWithConnectionIdSupportLockUser() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("OBServer");

        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(connectionConfig);
        String version = "3.0.0";

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .thenReturn(connectionConfig);

        ConnectionSession session = mock(ConnectionSession.class);
        when(factory.generateSession()).thenReturn(session);
        when(ConnectionSessionUtil.getVersion(session)).thenReturn(version);

        ConnectionSessionUtil.clearConnectionSessionFactory();
        ConnectionSessionUtil.addConnectionSessionFactory(factory);

        assertTrue(oscService.getLockUserIsRequired(connectionId));

        ConnectionSessionUtil.clearConnectionSessionFactory();
    }
}