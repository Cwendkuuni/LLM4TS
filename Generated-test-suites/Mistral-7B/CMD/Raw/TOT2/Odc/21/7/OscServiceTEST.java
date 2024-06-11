import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.DialectType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.SessionFactory;
import com.oceanbase.odc.service.session.model.ConnectionSession;

@SpringBootTest
public class OscServiceTest {

    @Autowired
    private DatabaseRepository databaseRepository;

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    @MockBean
    private ScheduleRepository scheduleRepository;

    @MockBean
    private ConnectionService connectionService;

    @Autowired
    private SessionFactory sessionFactory;

    @Before
    public void init() {
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(new DatabaseEntity()));
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(new ScheduleEntity()));
        when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(new ConnectionConfig());
        OscDBUserUtil.setLockUserIsRequired = mock(OscDBUserUtil.class).andReturn(false);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        OscService oscService = new OscService();
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertTrue(result.isPresent());
        assertEquals(0L, result.get().getDatabaseId());
        assertEquals(false, result.get().isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_databaseNotFound() {
        Long id = 1L;
        OscService oscService = new OscService();
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertFalse(result.isPresent());
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        OscService oscService = new OscService();
        oscService.swapTable(scheduleTaskId);
        verify(scheduleTaskRepository, times(1)).updateTaskResult(scheduleTaskId, anyString());
    }

    @Test(expected = Exception.class)
    public void testSwapTable_invalidTaskType() {
        Long scheduleTaskId = 1L;
        OscService oscService = new OscService();
        oscService.swapTable(scheduleTaskId);
        verify(scheduleTaskRepository, times(1)).updateTaskResult(scheduleTaskId, anyString());
        // this test case should throw an exception
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.ofNullable(new ScheduleTaskEntity()));
        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testSwapTable_invalidSwapTableType() {
        Long scheduleTaskId = 1L;
        OscService oscService = new OscService();
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.AUTOMATIC);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleRepository.findById(Long.parseLong(scheduleTaskRepository.findById(scheduleTaskId).get().getJobName()))).thenReturn(Optional.of(new ScheduleEntity()));
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(new DatabaseEntity()));
        when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(new ConnectionConfig());
        oscService.swapTable(scheduleTaskId, oscParameters);
        verify(scheduleTaskRepository, times(1)).updateTaskResult(scheduleTaskId, anyString());
        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
        verify(scheduleRepository, times(1)).findById(Long.parseLong(scheduleTaskRepository.findById(scheduleTaskId).get().getJobName()));
        verify(databaseRepository, times(1)).findById(1L);
        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(1L);
    }

    @Test(expected = Exception.class)
    public void testSwapTable_invalidManualSwapTableState() {
        Long scheduleTaskId = 1L;
        OscService oscService = new OscService();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);
        result.setManualSwapTableStarted(false);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleRepository.findById(Long.parseLong(scheduleTaskRepository.findById(scheduleTaskId).get().getJobName()))).thenReturn(Optional.of(new ScheduleEntity()));
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(new DatabaseEntity()));
        when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(new ConnectionConfig());
        oscService.swapTable(scheduleTaskId, null);
        verify(scheduleTaskRepository, times(1)).updateTaskResult(scheduleTaskId, anyString());
        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
        verify(scheduleRepository, times(1)).findById(Long.parseLong(scheduleTaskRepository.findById(scheduleTaskId).get().getJobName()));
        verify(databaseRepository, times(1)).findById(1L);
        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(1L);
    }

    @Test
    public void testSwapTable_validSwapTableState() {
        Long scheduleTaskId = 1L;
        OscService oscService = new OscService();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(true);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleRepository.findById(Long.parseLong(scheduleTaskRepository.findById(scheduleTaskId).get().getJobName()))).thenReturn(Optional.of(new ScheduleEntity()));
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(new DatabaseEntity()));
        when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(new ConnectionConfig());
        oscService.swapTable(scheduleTaskId, null);
        verify(scheduleTaskRepository, times(1)).updateTaskResult(scheduleTaskId, anyString());
        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
        verify(scheduleRepository, times(1)).findById(Long.parseLong(scheduleTaskRepository.findById(scheduleTaskId).get().getJobName()));
        verify(databaseRepository, times(1)).findById(1L);
        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(1L);
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        OscService oscService = new OscService();
        boolean result = oscService.getLockUserIsRequired(connectionId);
        assertTrue(result);
    }

    @Test
    public void testGetLockUserIsRequired_false() {
        Long connectionId = 1L;
        OscService oscService = new OscService();
        OscDBUserUtil.setLockUserIsRequired = mock(OscDBUserUtil.class).andReturn(false);
        boolean result = oscService.getLockUserIsRequired(connectionId);
        assertFalse(result);
    }
}