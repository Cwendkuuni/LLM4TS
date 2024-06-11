import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.JobType;
import com.oceanbase.odc.metadb.schedule.OnlineSchemaChangeParameters;
import com.oceanbase.odc.metadb.schedule.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.SwapTableType;

import java.util.Optional;

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
    private ScheduleRepository scheduleRepository;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(new DatabaseEntity()));
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(new ConnectionConfig());
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(new ScheduleEntity()));
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(new ScheduleTaskEntity()));
    }

    @Test
    public void testGetOscDatabaseInfo() {
        // Given
        Long id = 1L;

        // When
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testSwapTable() {
        // Given
        Long scheduleTaskId = 1L;

        // When
        oscService.swapTable(scheduleTaskId);

        // Then
        // Verify that the methods in the service class are called in the expected order
        verify(databaseRepository, times(1)).findById(anyLong());
        verify(scheduleRepository, times(1)).findById(eq(Long.parseLong("scheduleId")));
        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(anyLong());
    }

    @Test
    public void testSwapTable_InvalidScheduleType() {
        // Given
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.SCHEDULE);
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        // When
        // Then
        assertThrows(IllegalStateException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });

        // Verify that the methods in the service class are not called
        verify(databaseRepository, never()).findById(anyLong());
    }

    @Test
    public void testSwapTable_InvalidSwapTableType() {
        // Given
        Long scheduleTaskId = 1L;
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.BACKUP);
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        // When
        // Then
        assertThrows(IllegalArgumentException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });

        // Verify that the methods in the service class are not called
        verify(databaseRepository, never()).findById(anyLong());
    }

    @Test
    public void testSwapTable_InvalidManualSwapTableStatus() {
        // Given
        Long scheduleTaskId = 1L;
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(result));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        // When
        // Then
        assertThrows(IllegalStateException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });

        // Verify that the methods in the service class are not called
        verify(databaseRepository, never()).findById(anyLong());
    }
}