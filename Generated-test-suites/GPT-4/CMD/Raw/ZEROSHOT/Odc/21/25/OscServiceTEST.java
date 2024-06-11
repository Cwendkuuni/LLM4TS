
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

import java.util.Optional;

import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;

public class OscServiceTest {
  
    @Mock
    private DatabaseRepository databaseRepository;
    
    @Mock
    private ConnectionService connectionService;
    
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    
    @Mock
    private ScheduleRepository scheduleRepository;
    
    @Mock
    private Optional<DatabaseEntity> database;

    @Mock
    private Optional<ScheduleTaskEntity> scheduleTaskOptional;

    @Mock
    private Optional<ScheduleEntity> scheduleEntity;

    @InjectMocks
    private OscService oscService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testGetOscDatabaseInfo() {
        when(databaseRepository.findById(anyLong())).thenReturn(database);
        when(database.isPresent()).thenReturn(true);
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
        assertNotNull(result);
    }
    
    @Test
    public void testSwapTable() {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(scheduleTaskOptional);
        when(scheduleTaskOptional.isPresent()).thenReturn(true);
        when(scheduleEntity.isPresent()).thenReturn(true);
        OscSwapTableVO result = oscService.swapTable(1L);
        assertNotNull(result);
    }
    
    @Test
    public void testGetLockUserIsRequired() {
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(mock(ConnectionConfig.class));
        boolean result = oscService.getLockUserIsRequired(1L);
        assertNotNull(result);
    }
}
