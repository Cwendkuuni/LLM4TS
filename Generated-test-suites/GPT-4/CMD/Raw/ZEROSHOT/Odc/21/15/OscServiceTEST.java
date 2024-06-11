
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;

import java.util.Optional;

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
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getOscDatabaseInfoTest() {
        Long id = 1L;
        Optional<DatabaseEntity> dbEntityOption = Optional.of(new DatabaseEntity());
        dbEntityOption.get().setDatabaseId(1L);
        dbEntityOption.get().setConnectionId(2L);

        when(databaseRepository.findById(id)).thenReturn(dbEntityOption);
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);

        assertNotNull(oscDatabase);
        assertEquals(id, oscDatabase.getDatabaseId());
    }

    @Test
    public void swapTableTest() {
        Long scheduleTaskId = 1L;

        Optional<ScheduleTaskEntity> scheduleTaskEntityOptional = Optional.of(new ScheduleTaskEntity());
        scheduleTaskEntityOptional.get().setJobName("1");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        scheduleTaskEntityOptional.get().setResultJson(JsonUtils.toJson(result));

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(scheduleTaskEntityOptional);
        when(scheduleRepository.findById(Long.parseLong(scheduleTaskEntityOptional.get().getJobName()))).thenReturn(Optional.of(scheduleEntity));
        assertEquals(scheduleTaskId, oscService.swapTable(scheduleTaskId).getScheduleTaskId());
    }

    @Test
    public void getLockUserIsRequiredTest() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");

        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(connectionConfig);
        assertEquals(OscDBUserUtil.isLockUserRequired(connectionConfig.getDialectType(), () -> "8.0.20"), 
        oscService.getLockUserIsRequired(1L));
    }
}
