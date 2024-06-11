import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;

import static org.mockito.Mockito.when;

/**
 * Tests for the OscService class.
 */
public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    @MockBean
    private ConnectionService connectionService;

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);

        Optional<DatabaseEntity> databaseOptional = Mockito.mock(Optional.class);
        when(databaseOptional.isPresent()).thenReturn(true);
        when(databaseRepository.findById(id)).thenReturn(databaseOptional);

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);
        Assert.assertNotNull(oscDatabase);
        Assert.assertEquals(id, oscDatabase.getDatabaseId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOscDatabaseInfoWithInvalidDatabaseId() {
        Long id = -1L;

        Optional<DatabaseEntity> databaseOptional = Mockito.mock(Optional.class);
        when(databaseOptional.isPresent()).thenReturn(false);
        when(databaseRepository.findById(id)).thenReturn(databaseOptional);

        oscService.getOscDatabaseInfo(id);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Mockito.mock(Optional.class));
        when(scheduleRepository.findById(1L)).thenReturn(Mockito.mock(Optional.class));
        when(scheduleRepository.findById(1L).get()).thenReturn(scheduleEntity);

        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);
        Assert.assertNotNull(result);
        Assert.assertEquals(scheduleTaskId, result.getScheduleTaskId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTableWithInvalidScheduleTaskId() {
        Long scheduleTaskId = -1L;

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Mockito.mock(Optional.class));

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);

        boolean lockUserIsRequired = oscService.getLockUserIsRequired(connectionId);
        Assert.assertTrue(lockUserIsRequired);
    }

    @Test
    public void testGetLockUserIsRequiredWithNoLockUserRequired() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        OscDBUserUtil.isLockUserRequired = (dialectType, getVersion) -> false;

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);

        boolean lockUserIsRequired = oscService.getLockUserIsRequired(connectionId);
        Assert.assertFalse(lockUserIsRequired);
    }
}