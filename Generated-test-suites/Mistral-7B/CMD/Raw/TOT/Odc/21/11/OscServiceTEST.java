import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.OptionalInt;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@Transactional
public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private DatabaseRepository databaseRepository;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setId(id);
        databaseEntity.setConnectionId(1L);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));
        when(connectionService.getForConnectionSkipPermissionCheck(databaseEntity.getConnectionId()))
                .thenReturn(new ConnectionConfig());

        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(id);
        assertNotNull(oscDatabaseInfo);
        assertTrue(oscDatabaseInfo.isLockDatabaseUserRequired());

        verify(databaseRepository).findById(id);
        verifyNoMoreInteractions(databaseRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOscDatabaseInfoNoDatabaseFound() {
        Long id = 1L;
        when(databaseRepository.findById(id)).thenReturn(Optional.empty());
        oscService.getOscDatabaseInfo(id);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(oscParameters));

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(false);
        when(scheduleTaskEntity.getResultJson()).thenReturn(JsonUtils.toJson(result));

        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);
        assertNotNull(oscSwapTable);
        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());

        verify(scheduleTaskRepository).findById(scheduleTaskId);
        verify(scheduleRepository).findById(1L);
        verify(scheduleTaskEntity).setResultJson(anyString());
        verify(scheduleTaskRepository).save(scheduleTaskEntity);
        verifyNoMoreInteractions(scheduleTaskRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTableNoScheduleTaskFound() {
        Long scheduleTaskId = 1L;
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(OptionalInt.empty());
        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testSwapTableInvalidJobType() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.BACKUP);
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(scheduleTaskId));

        verify(scheduleTaskRepository).findById(scheduleTaskId);
        verify(scheduleRepository).findById(1L);
        verifyNoInteractions(scheduleTaskEntity);
    }

    @Test
    public void testSwapTableInvalidSwapTableType() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.BACKUP);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(oscParameters));

        assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(scheduleTaskId));

        verify(scheduleTaskRepository).findById(scheduleTaskId);
        verify(scheduleRepository).findById(1L);
        verifyNoMoreInteractions(scheduleTaskEntity);
    }

    @Test
    public void testSwapTableInvalidManualSwapTableEnabled() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(oscParameters));

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);
        result.setManualSwapTableStarted(false);
        when(scheduleTaskEntity.getResultJson()).thenReturn(JsonUtils.toJson(result));

        assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(scheduleTaskId));

        verify(scheduleTaskRepository).findById(scheduleTaskId);
        verify(scheduleRepository).findById(1L);
        verifyNoMoreInteractions(scheduleTaskEntity);
    }

    @Test
    public void testSwapTableSuccess() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(oscParameters));

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(false);

        doNothing().when(scheduleTaskEntity).setResultJson(anyString());

        oscService.swapTable(scheduleTaskId);

        assertTrue(result.isManualSwapTableEnabled());
        assertFalse(result.isManualSwapTableStarted());

        verify(scheduleTaskEntity).setResultJson(JsonUtils.toJson(result));
        verify(scheduleTaskRepository).save(scheduleTaskEntity);
        verifyNoMoreInteractions(scheduleTaskRepository);
    }
}