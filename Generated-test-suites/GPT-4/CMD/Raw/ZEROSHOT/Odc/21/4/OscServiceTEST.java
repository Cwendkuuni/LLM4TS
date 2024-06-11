
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.function.Supplier;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

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
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        DatabaseEntity mockDatabaseEntity = new DatabaseEntity();
        mockDatabaseEntity.setDatabaseId(1L);
        Optional mockOptional = Optional.of(mockDatabaseEntity);
        
        when(databaseRepository.findById(1L)).
            thenReturn(mockOptional);
        assertNotNull(oscService.getOscDatabaseInfo(1L));
    }

    @Test
    public void testSwapTable() {
        ScheduleTaskEntity mockScheduleTask = new ScheduleTaskEntity();
        ScheduleEntity mockScheduleEntity = new ScheduleEntity();
        ConnectionConfig mockConnConfig = new ConnectionConfig();
        
        OnlineSchemaChangeScheduleTaskResult mockOscResult = new OnlineSchemaChangeScheduleTaskResult();
        SwapTableType mockSwapTableType = SwapTableType.MANUAL;
        JobType mockJobType = JobType.ONLINE_SCHEMA_CHANGE_COMPLETE;

        mockScheduleEntity.setJobType(mockJobType);
        mockScheduleEntity.setJobParametersJson(OnlineSchemaChangeParameters.class.toString());
        
        Optional mockOptional = Optional.of(mockScheduleTask);
        Optional mockOptionalSchedule = Optional.of(mockScheduleEntity);

        when(scheduleTaskRepository.findById(1L)).
            thenReturn(mockOptional);
        when(scheduleRepository.findById(1L)).
            thenReturn(mockOptionalSchedule);
        assertNotNull(oscService.swapTable(1L));
    }

    @Test
    public void testGetLockUserIsRequired() {
        ConnectionConfig mockConnConfig = new ConnectionConfig();
        DefaultConnectSessionFactory mockFactory = new DefaultConnectSessionFactory(mockConnConfig);
        String version = null;
        ConnectionSession mockConnSession = mock(ConnectionSession.class);
        
        Supplier<String> versionSupplier = () -> {
            try {
                version = ConnectionSessionUtil.getVersion(mockConnSession);
            } finally {
                if (mockConnSession != null) {
                    mockConnSession.expire();
                }
            }
            return version;
        };
        
        OscDBUserUtil.isLockUserRequired(mockConnConfig.getDialectType(), versionSupplier);
    }
}
