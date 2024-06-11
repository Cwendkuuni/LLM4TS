import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

@SuppressWarnings("unchecked")
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
    private DefaultConnectSessionFactory sessionFactory;

    @Mock
    private OscDBUserUtil oscDBUserUtil;

    @InjectMocks
    private OscService oscService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.ofNullable(createDatabaseEntity()));
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(createConnectionConfig());
        when(sessionFactory.generateSession()).thenReturn(createConnectionSession());
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertNull(result);
    }

    @Test
    public void testGetOscDatabaseInfo_DatabaseExists() {
        Long id = 1L;
        DatabaseEntity databaseEntity = createDatabaseEntity();
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertEquals(id, result.getDatabaseId());
        assertFalse(result.isLockDatabaseUserRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_InvalidScheduleTaskId() {
        Long scheduleTaskId = -1L;
        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testSwapTable_ValidScheduleTaskId() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = createScheduleTaskEntity();
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        oscService.swapTable(scheduleTaskId);
        verify(scheduleTaskRepository).updateTaskResult(scheduleTaskId, anyString());
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        assertTrue(oscService.getLockUserIsRequired(connectionId));
    }

    @Test
    public void testGetLockUserIsRequired_NoLockUserRequired() {
        Long connectionId = 1L;
        when(oscDBUserUtil.isLockUserRequired(anyString(), any()))
                .thenReturn(false);
        assertFalse(oscService.getLockUserIsRequired(connectionId));
    }

    private DatabaseEntity createDatabaseEntity() {
        return new DatabaseEntity();
    }

    private ConnectionConfig createConnectionConfig() {
        return new ConnectionConfig();
    }

    private ConnectionSession createConnectionSession() {
        return new ConnectionSession();
    }

    private ScheduleTaskEntity createScheduleTaskEntity() {
        return new ScheduleTaskEntity();
    }

    private OnlineSchemaChangeParameters createOnlineSchemaChangeParameters() {
        return new OnlineSchemaChangeParameters();
    }

    private OnlineSchemaChangeScheduleTaskResult createOnlineSchemaChangeScheduleTaskResult() {
        return new OnlineSchemaChangeScheduleTaskResult();
    }

    private OscSwapTableVO createOscSwapTableVO() {
        return new OscSwapTableVO();
    }

}