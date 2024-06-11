import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.JobType;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    @MockitoAnnotations.Mock
    private ConnectionService connectionService;

    @MockitoAnnotations.Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @MockitoAnnotations.Mock
    private ScheduleRepository scheduleRepository;

    @MockitoAnnotations.Mock
    private DatabaseRepository databaseRepository;

    @Before
    public void init() {
        Mockito.initMocks(this);
        ReflectionTestUtils.setFieldValue(oscService, "connectionService", connectionService);
        ReflectionTestUtils.setFieldValue(oscService, "scheduleTaskRepository", scheduleTaskRepository);
        ReflectionTestUtils.setFieldValue(oscService, "scheduleRepository", scheduleRepository);
        ReflectionTestUtils.setFieldValue(oscService, "databaseRepository", databaseRepository);
    }

    @Test
    public void testGetOscDatabaseInfo_whenDatabaseDoesntExist_thenNullOscDatabaseInfo() {
        Long id = 0L;
        when(databaseRepository.findById(id)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo actual = oscService.getOscDatabaseInfo(id);
        assertNull(actual);
    }

    @Test
    public void testGetOscDatabaseInfo_whenDatabaseExists_thenValidOscDatabaseInfo() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId("databaseId");
        when(databaseRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo expected = new OscLockDatabaseUserInfo();
        expected.setDatabaseId("databaseId");
        expected.setLockDatabaseUserRequired(true);

        OscLockDatabaseUserInfo actual = oscService.getOscDatabaseInfo(databaseEntity.getId());
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_whenScheduleTaskDoesntExist_thenIllegalArgumentException() {
        Long scheduleTaskId = 0L;

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testSwapTable_whenScheduleTaskExists_thenSwapTableVO() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("scheduleId");

        when(scheduleTaskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(scheduleTaskEntity));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        scheduleEntity.setJobParametersJson("{}");

        when(scheduleRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), OnlineSchemaChangeParameters.class)).thenReturn(oscParameters);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        when(JsonUtils.fromJson(scheduleTaskEntity.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class)).thenReturn(result);

        OscSwapTableVO expected = new OscSwapTableVO();
        expected.setScheduleTaskId(scheduleTaskEntity.getId());

        OscSwapTableVO actual = oscService.swapTable(scheduleTaskEntity.getId());

        assertEquals(expected, actual);
    }

    @Test
    public void testGetLockUserIsRequired_whenConnectionConfigDoesntSupportLockUser_thenFalse() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("obclient");

        assertFalse(oscService.getLockUserIsRequired(connectionConfig.getConnectionId()));
    }

    @Test
    public void testGetLockUserIsRequired_whenConnectionConfigSupportsLockUser_thenTrue() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mycat");

        assertTrue(oscService.getLockUserIsRequired(connectionConfig.getConnectionId()));
    }
}