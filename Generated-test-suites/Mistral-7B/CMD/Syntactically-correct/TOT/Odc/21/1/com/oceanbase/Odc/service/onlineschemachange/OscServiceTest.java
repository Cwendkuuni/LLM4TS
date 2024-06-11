import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.OscDBUserUtil;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Transactional
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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(databaseRepository.findById(1L)).thenReturn(Optional.of(new DatabaseEntity()));
        when(connectionService.getForConnectionSkipPermissionCheck(1L))
                .thenReturn(new ConnectionConfig());
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(new ScheduleEntity()));
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(new ScheduleTaskEntity()));
    }

    @Test
    public void testGetOscDatabaseInfo() {
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
        assertNotNull(result);
        assertFalse(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable_invalidJobType() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.BACKUP);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        try {
            oscService.swapTable(1L);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains(ErrorCodes.BadArgument.getMessage()));
        }
    }

    @Test
    public void testSwapTable_manualSwapTableAlreadyStarted() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleTaskEntity.getResultJson()).thenReturn(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

        assertTrue(oscService.swapTable(1L).isManualSwapTableEnabled());

        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTaskEntity.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);

        result.setManualSwapTableEnabled(true);
        scheduleTaskRepository.updateTaskResult(1L, JsonUtils.toJson(result));

        try {
            oscService.swapTable(1L);
            fail("Exception expected");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains(ErrorCodes.BadState.getMessage()));
        }
    }

    @Test
    public void testSwapTable_noDatabaseConnection() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setConnectionId(1L);
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(databaseEntity));
        when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(null);

        try {
            oscService.swapTable(1L);
            fail("Exception expected");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("Failed to connect to database"));
        }
    }
}