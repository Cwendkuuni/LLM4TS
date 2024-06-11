
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;


public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;
    @Mock
    private ConnectionService connectionService;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    private OscService oscServiceUnderTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 123L;                   

        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(123);
        databaseEntity.setConnectionId(456L);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscServiceUnderTest.getOscDatabaseInfo(id);

        verify(databaseRepository).findById(id);
        assertEquals(result.getDatabaseId(), new Long(123));
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 123L;

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("Dialect");

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(connectionConfig);

        boolean result = oscServiceUnderTest.getLockUserIsRequired(connectionId);

        assertTrue(result);
    }

    @Test(expected = PreConditions.ResourceNotFoundException.class)
    public void testSwapTable_ThrowsException() {
          Long scheduleTaskId = 1L; 

          when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());

          OscSwapTableVO result = oscServiceUnderTest.swapTable(scheduleTaskId);

          verify(scheduleTaskRepository).findById(scheduleTaskId);
    }
}
