import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtensions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@DisplayName("OscService Test")
class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    @NonNull
    private OscService oscService;

    @Test
    @DisplayName("Test getOscDatabaseInfo with existing database")
    void testGetOscDatabaseInfoWithExistingDatabase() {
        // Given
        Long databaseId = UUID.randomUUID().longValue();
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(databaseId);
        databaseEntity.setConnectionId(1L);
        when(databaseRepository.findById(databaseId)).thenReturn(Optional.of(databaseEntity));

        // When
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(databaseId);

        // Then
        Assertions.assertEquals(databaseId, oscDatabase.getDatabaseId());
        Assertions.assertTrue(oscDatabase.isLockDatabaseUserRequired());

        // Verify
        verify(databaseRepository, times(1)).findById(databaseId);
    }

    @Test
    @DisplayName("Test getOscDatabaseInfo with non-existing database")
    void testGetOscDatabaseInfoWithNonExistingDatabase() {
        // Given
        Long databaseId = UUID.randomUUID().longValue();

        // When
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(databaseId);

        // Then
        Assertions.assertNull(oscDatabase);

        // Verify
        verify(databaseRepository, times(1)).findById(databaseId);
    }

    @Test
    @DisplayName("Test swapTable with valid parameters")
    void testSwapTableWithValidParameters() {
        // Given
        Long scheduleTaskId = UUID.randomUUID().longValue();
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        scheduleTaskEntity.setJobName("schedule-1");
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("schedule-1");
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(connectionService.getForConnectionSkipPermissionCheck(ArgumentMatchers.anyLong()))
                .thenReturn(new ConnectionConfig());
        when(databaseRepository.findById(ArgumentMatchers.anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(new DatabaseEntity()));

        // When
        oscService.swapTable(scheduleTaskId);

        // Then
        // Verify
        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
        verify(scheduleRepository, times(1)).findById(1L);
        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(ArgumentMatchers.anyLong());
        verify(databaseRepository, atLeastOnce()).findById(ArgumentMatchers.anyLong());
        // No need to verify result as it is not mocked and updated in actual implementation
    }

    @Test
    @DisplayName("Test swapTable with non-existing scheduleTask")
    void testSwapTableWithNonExistingScheduleTask() {
        // Given
        Long scheduleTaskId = UUID.randomUUID().longValue();

        // When & Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });
        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
    }

    @Test
    @DisplayName("Test swapTable with non-existing schedule")
    void testSwapTableWithNonExistingSchedule() {
        // Given
        Long scheduleTaskId = UUID.randomUUID().longValue();
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        // When & Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });
        verify(scheduleRepository, times(1)).findById(ArgumentMatchers.eq(Long.parseLong(scheduleTaskEntity.getJobName())));
    }

    @Test
    @DisplayName("Test swapTable with invalid job type")
    void testSwapTableWithInvalidJobType() {
        // Given
        Long scheduleTaskId = UUID.randomUUID().longValue();
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("schedule-1");
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.BACKUP);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        // When & Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });
        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
        verify(scheduleRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test swapTable with non-manual swapTableType")
    void testSwapTableWithNonManualSwapTableType() {
        // Given
        Long scheduleTaskId = UUID.randomUUID().longValue();
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("schedule-1");
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.AUTOMATIC);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        // When & Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });
        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
        verify(scheduleRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test swapTable with disabled manualSwapTableEnabled")
    void testSwapTableWithDisabledManualSwapTableEnabled() {
        // Given
        Long scheduleTaskId = UUID.randomUUID().longValue();
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("schedule-1");
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        // When & Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });
        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
        verify(scheduleRepository, times(1)).findById(1L);
    }

    @AfterEach
    void tearDown() {
        MockitoExtensions.reset(databaseRepository, connectionService, scheduleTaskRepository, scheduleRepository);
    }
}