import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

@RunWith(MockitoJUnitRunner.class)
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

    private DatabaseEntity mockDatabaseEntity;
    private ScheduleTaskEntity mockScheduleTaskEntity;
    private ScheduleEntity mockScheduleEntity;

    @Before
    public void setup() {
        mockDatabaseEntity = new DatabaseEntity();
        mockDatabaseEntity.setDatabaseId(1L);

        mockScheduleTaskEntity = new ScheduleTaskEntity();
        mockScheduleTaskEntity.setJobName("1");

        mockScheduleEntity = new ScheduleEntity();
        mockScheduleEntity.setJobParametersJson("{}");
        mockScheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        when(databaseRepository.findById(id)).thenReturn(Optional.of(mockDatabaseEntity));

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);

        assertNotNull(oscDatabase);
        assertEquals(mockDatabaseEntity.getDatabaseId(), oscDatabase.getDatabaseId());
        assertFalse(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(mockScheduleTaskEntity));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(mockScheduleEntity));

        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);

        assertNotNull(oscSwapTable);
        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        ConnectionSessionFactory mockFactory = mock(DefaultConnectSessionFactory.class);
        ConnectionSession mockConnSession = mock(ConnectionSession.class);

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(new ConnectionConfig());
        when(OscDBUserUtil.isLockUserRequired(any(), any())).thenReturn(true);
        when(mockFactory.generateSession()).thenReturn(mockConnSession);
        when(ConnectionSessionUtil.getVersion(mockConnSession)).thenReturn("1.0");

        boolean lockUserRequired = oscService.getLockUserIsRequired(connectionId);

        assertTrue(lockUserRequired);
    }
}