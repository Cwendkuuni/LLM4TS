import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

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
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import com.oceanbase.odc.service.test.TestDataGenerator;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
@ContextConfiguration(classes = { OscService.class, TestDataGenerator.class })
@ActiveProfiles("test")
public class OscServiceTest {

    @Autowired
    private OscService oscService;

    @Autowired
    private DatabaseRepository databaseRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ConnectionService connectionService;

    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;

    @Autowired
    private DefaultConnectSessionFactory sessionFactory;

    @InjectMocks
    private AutowireCapableBeanFactory autowireBeanFactory;

    @BeforeAll
    public static void setup() {
        MockitoAnnotations.initMocks(OscServiceTest.class);
    }

    @Test
    public void testGetOscDatabaseInfo_ValidInput() {
        Long id = UUID.randomUUID().longValue();
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        databaseEntity.setConnectionId(id + 1);

        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getDatabaseId());
        verify(databaseRepository, times(1)).findById(id);
    }

    @Test
    public void testGetOscDatabaseInfo_InvalidInput() {
        Long id = UUID.randomUUID().longValue();

        when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertFalse(result.isPresent());
        verify(databaseRepository, times(1)).findById(id);
    }

    @Test
    public void testSwapTable_ValidInput() {
        Long scheduleTaskId = UUID.randomUUID().longValue();
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setDatabaseId(1L);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(getDefaultOnlineSchemaChangeParameters()));
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);

        assertNull(result);
        verify(scheduleRepository, times(1)).findById(1L);
        verify(scheduleTaskRepository, never()).findById(scheduleTaskId);
    }

    @Test
    public void testSwapTable_InvalidInput() {
        Long scheduleTaskId = UUID.randomUUID().longValue();

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setDatabaseId(1L);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(getDefaultOnlineSchemaChangeParameters()));
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setScheduleTaskId(scheduleTaskId);
        scheduleTask.setStatus("RUNNING");

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));

        assertThrows(IllegalStateException.class, () -> oscService.swapTable(scheduleTaskId));

        verify(scheduleRepository, times(1)).findById(1L);
        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
    }

    // Add test cases for other methods and potential edge cases and exceptions
    // ...

    private OnlineSchemaChangeParameters getDefaultOnlineSchemaChangeParameters() {
        return new OnlineSchemaChangeParameters() {{
            setSwapTableType(SwapTableType.MANUAL);
        }};
    }

}