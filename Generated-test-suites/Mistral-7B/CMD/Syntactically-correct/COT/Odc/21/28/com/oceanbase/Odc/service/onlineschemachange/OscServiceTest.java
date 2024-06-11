import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.core.shared.PreConditions;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
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

    @Mock
    private OscDBUserUtil oscDBUserUtil;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(new DatabaseEntity()));
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong()))
                .thenAnswer(invocation -> new ConnectionConfig());
        when(oscDBUserUtil.isLockUserRequired(anyString(), any()))
                .thenAnswer(invocation -> true);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        OscLockDatabaseUserInfo expectedResult = new OscLockDatabaseUserInfo();
        when(databaseRepository.findById(id)).thenReturn(Optional.of(new DatabaseEntity()));
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        verify(databaseRepository).findById(id);
        PreConditions.assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOscDatabaseInfoWithNoDatabase() {
        Long id = 1L;
        when(databaseRepository.findById(id)).thenReturn(Optional.empty());
        oscService.getOscDatabaseInfo(id);
    }

    @Test
    @Transactional
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleEntity.getJobType()).thenReturn(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        when(scheduleEntity.getJobParametersJson()).thenReturn("{}");
        when(oscDBUserUtil.isLockUserRequired(anyString(), any()))
                .thenAnswer(invocation -> true);

        oscService.swapTable(scheduleTaskId);

        verify(scheduleTaskRepository).findById(scheduleTaskId);
        verify(scheduleRepository).findById(1L);
        verify(scheduleTask).setResultJson(anyString());
        verify(oscDBUserUtil).isLockUserRequired(anyString(), any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTableWithInvalidJobType() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleEntity.getJobType()).thenReturn(1L); // invalid job type

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    @Transactional
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        boolean expectedResult = true;

        oscService.getLockUserIsRequired(connectionId);

        verify(connectionService).getForConnectionSkipPermissionCheck(connectionId);
        verify(oscDBUserUtil).isLockUserRequired(anyString(), any());
        PreConditions.assertEquals(expectedResult, true);
    }

    @Test
    public void testGetLockUserIsRequiredWithNoLockUserRequired() {
        Long connectionId = 1L;
        when(oscDBUserUtil.isLockUserRequired(anyString(), any()))
                .thenAnswer(invocation -> false);

        boolean result = oscService.getLockUserIsRequired(connectionId);

        PreConditions.assertEquals(false, result);
    }
}