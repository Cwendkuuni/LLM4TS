package com.oceanbase.odc.service.onlineschemachange;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@DisplayName("OscService Test")
public class OscServiceTest {

    @MockBean
    private DatabaseRepository databaseRepository;

    @MockBean
    private ConnectionService connectionService;

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    @MockBean
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private OscService oscService;

    @Autowired
    private JsonUtils jsonUtils;

    private Long databaseId;
    private Long scheduleTaskId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        databaseId = 1L;
        scheduleTaskId = 2L;
    }

    @Test
    @DisplayName("Test getOscDatabaseInfo method with valid input")
    public void testGetOscDatabaseInfo() {
        // Given
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(databaseId);

        when(databaseRepository.findById(databaseId)).thenReturn(Optional.of(databaseEntity));

        // When
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(databaseId);

        // Then
        verify(databaseRepository, times(1)).findById(databaseId);
        assertThat(result.getDatabaseId(), is(databaseId));
        verifyNoMoreInteractions(databaseRepository);
    }

    @Test
    @DisplayName("Test getOscDatabaseInfo method with invalid input")
    public void testGetOscDatabaseInfoWithInvalidInput() {
        // Given
        when(databaseRepository.findById(databaseId)).thenReturn(Optional.empty());

        // When & Then
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(databaseId);
        verify(databaseRepository, times(1)).findById(databaseId);
        assertThat(result, is(sameInstance(new OscLockDatabaseUserInfo())));
        verifyNoMoreInteractions(databaseRepository);
    }

    @Test
    @DisplayName("Test swapTable method with valid input")
    public void testSwapTableWithValidInput() {
        // Given
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName("1");
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson(jsonUtils.toJson(new OnlineSchemaChangeParameters()));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        when(databaseRepository.findById(databaseId)).thenReturn(Optional.of(new DatabaseEntity()));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        // When
        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);

        // Then
        verify(databaseRepository, times(1)).findById(databaseId);
        verify(scheduleRepository, times(1)).findById(1L);
        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
        verify(scheduleTaskRepository, times(1)).updateTaskResult(scheduleTaskId, anyString());
        assertThat(result, is(notNullValue()));
        verifyNoMoreInteractions(databaseRepository, scheduleRepository, scheduleTaskRepository);
    }

    @Test
    @DisplayName("Test swapTable method with invalid input (non-existent scheduleTask)")
    public void testSwapTableWithInvalidInputNonExistentScheduleTask() {
        // Given
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName("1");
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson(jsonUtils.toJson(new OnlineSchemaChangeParameters()));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        when(databaseRepository.findById(databaseId)).thenReturn(Optional.of(new DatabaseEntity()));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);
        verify(databaseRepository, times(1)).findById(databaseId);
        verify(scheduleRepository, times(1)).findById(1L);
        verifyNoInteractions(scheduleTaskRepository);
        assertThat(result, is(nullValue()));
        verifyNoMoreInteractions(databaseRepository, scheduleRepository, scheduleTaskRepository);
    }

    @Test
    @DisplayName("Test swapTable method with invalid input (non-ONLINE_SCHEMA_CHANGE_COMPLETE schedule)")
    public void testSwapTableWithInvalidInputNonOnlineSchemaChangeCompleteSchedule() {
        // Given
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName("NonOnlineSchemaChangeJobName");
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson(jsonUtils.toJson(new OnlineSchemaChangeParameters()));
        scheduleEntity.setJobType(JobType.BACKUP_DATABASE);

        when(databaseRepository.findById(databaseId)).thenReturn(Optional.of(new DatabaseEntity()));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        // When & Then
        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);
        verify(databaseRepository, times(1)).findById(databaseId);
        verify(scheduleRepository, times(1)).findById(1L);
        verifyNoInteractions(scheduleTaskRepository);
        assertThat(result, is(nullValue()));
        verifyNoMoreInteractions(databaseRepository, scheduleRepository, scheduleTaskRepository);
    }

    @Test
    @DisplayName("Test getLockUserIsRequired method with valid input")
    public void testGetLockUserIsRequiredWithValidInput() {
        // Given
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("MySQL");

        // When
        boolean result = oscService.getLockUserIsRequired(decryptedConnConfig.getConnectionId());

        // Then
        verifyNoInteractions(connectionService);
        assertThat(result, is(true));
        verifyNoMoreInteractions(connectionService);
    }

    @Test
    @DisplayName("Test getLockUserIsRequired method with invalid input (no ConnectionService)")
    public void testGetLockUserIsRequiredWithInvalidInputNoConnectionService() {
        // Given
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("MySQL");

        // When
        OscService oscService = new OscService();

        // When & Then
        boolean result = oscService.getLockUserIsRequired(decryptedConnConfig.getConnectionId());
        verifyNoInteractions(connectionService);
        assertThat(result, is(false));
        verifyNoMoreInteractions(connectionService);
    }

    @Test
    @DisplayName("Test getLockUserIsRequired method with invalid input (null ConnectionConfig)")
    public void testGetLockUserIsRequiredWithInvalidInputNullConnectionConfig() {
        // Given
        ConnectionConfig decryptedConnConfig = null;

        // When
        OscService oscService = new OscService();

        // When & Then
        boolean result = oscService.getLockUserIsRequired(decryptedConnConfig.getConnectionId());
        verifyNoInteractions(connectionService);
        assertThat(result, is(false));
        verifyNoMoreInteractions(connectionService);
    }
}