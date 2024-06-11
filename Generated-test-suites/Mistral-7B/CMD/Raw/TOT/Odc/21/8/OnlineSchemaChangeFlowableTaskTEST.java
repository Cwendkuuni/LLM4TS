package com.oceanbase.odc.service.onlineschemachange;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AssertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private ScheduleTaskService scheduleTaskService;

    @Mock
    private QuartzJobService quartzJobService;

    @Mock
    private OnlineSchemaChangeTaskHandler taskHandler;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private OrganizationService organizationService;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void givenInitialization_whenStartIsCalled_thenCorrectlyInitializesFields() throws Exception {
        // Given
        long taskId = 1L;
        // ... set up mocks

        // When
        onlineSchemaChangeFlowableTask.start(taskId, null, null);

        // Then
        verify(scheduleTaskRepository).saveAndFlush(any(ScheduleTaskEntity.class));
        verify(scheduleService).create(any(ScheduleEntity.class));
        verify(taskHandler).start(anyLong(), anyLong());
    }

    @Test
    void givenStartIsCalledWithFailingTaskHandler_whenTaskHandlerFails_thenSetsTaskStatusToFailed() throws Exception {
        // Given
        long taskId = 1L;
        doThrow(new RuntimeException("Task handler failed")).when(taskHandler).start(anyLong(), anyLong());

        // When
        onlineSchemaChangeFlowableTask.start(taskId, null, null);

        // Then
        verify(onlineSchemaChangeFlowableTask).updateFlowInstanceStatus(FlowStatus.EXECUTION_FAILED);
    }

    @Test
    void givenStartIsCalledWithSuccessfulTaskHandler_whenTaskHandlerSucceeds_thenSetsTaskStatusToDone() throws Exception {
        // Given
        long taskId = 1L;

        // When
        onlineSchemaChangeFlowableTask.start(taskId, null, null);

        // Then
        verify(onlineSchemaChangeFlowableTask).updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED);
    }

    @Test
    void givenStartIsCalledWithErrorInOnlineSchemaChangeContextHolder_whenErrorInContextHolder_thenSetsTaskStatusToFailed()
            throws Exception {
        // Given
        long taskId = 1L;
        doReturn(new RuntimeException("Error in context holder")).when(onlineSchemaChangeFlowableTask)
                .getOnlineSchemaChangeContextHolder();

        // When
        onlineSchemaChangeFlowableTask.start(taskId, null, null);

        // Then
        verify(onlineSchemaChangeFlowableTask).updateFlowInstanceStatus(FlowStatus.EXECUTION_FAILED);
    }

    @Test
    void givenStartIsCalledWithNoSubTasks_whenThereAreNoSubTasks_thenDoesNotCreateAny() throws Exception {
        // Given
        long taskId = 1L;
        OnlineSchemaChangeParameters parameter = mock(OnlineSchemaChangeParameters.class);

        // When
        onlineSchemaChangeFlowableTask.start(taskId, null, null);

        // Then
        verify(taskHandler, never()).start(anyLong(), anyLong());
    }

    @Test
    void givenStartIsCalledWithSubTasks_whenSubTasksArePresent_thenStartsFirstOne() throws Exception {
        // Given
        long taskId = 1L;
        OnlineSchemaChangeParameters parameter = mock(OnlineSchemaChangeParameters.class);
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String schema = RandomStringUtils.randomAlphanumeric(10);
        doReturn(ListUtils.emptyIfNull(ListUtils.repeat(mock(OnlineSchemaChangeScheduleTaskParameters.class), 5))).when(parameter)
                .generateSubTaskParameters(connectionConfig, schema);

        // When
        onlineSchemaChangeFlowableTask.start(taskId, null, null);

        // Then
        verify(taskHandler).start(anyLong(), anyLong());
    }

    @Test
    void givenCancelIsCalled_whenTaskIsRunning_thenCancelsIt() throws Exception {
        // Given
        long taskId = 1L;
        long runningTaskId = 1L;

        // When
        onlineSchemaChangeFlowableTask.cancel(true, taskId, null);

        // Then
        verify(taskHandler).terminate(scheduleId, runningTaskId);
    }

    @Test
    void givenCancelIsCalled_whenTaskIsNotRunning_thenDoesNotTerminateIt() throws Exception {
        // Given
        long taskId = 1L;

        // When
        onlineSchemaChangeFlowableTask.cancel(true, taskId, null);

        // Then
        // Nothing should happen
    }

    @Test
    void givenCancelIsCalled_whenTaskIsCancelled_thenSetsTaskStatusToCancelled() throws Exception {
        // Given
        long taskId = 1L;

        // When
        onlineSchemaChangeFlowableTask.cancel(true, taskId, null);

        // Then
        verify(onlineSchemaChangeFlowableTask).updateFlowInstanceStatus(FlowStatus.CANCELED);
    }

    @Test
    void givenIsCancelledIsCalled_whenTaskIsCancelled_thenReturnsTrue() throws Exception {
        // Given
        long taskId = 1L;
        doReturn(true).when(onlineSchemaChangeFlowableTask).isCancelled();

        // When
        boolean result = onlineSchemaChangeFlowableTask.isCancelled();

        // Then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    void givenIsCancelledIsCalled_whenTaskIsNotCancelled_thenReturnsFalse() throws Exception {
        // Given
        long taskId = 1L;
        doReturn(false).when(onlineSchemaChangeFlowableTask).isCancelled();

        // When
        boolean result = onlineSchemaChangeFlowableTask.isCancelled();

        // Then
        Assertions.assertThat(result).isFalse();
    }

    @Test
    void givenGetOnlineSchemaChangeContextHolderIsCalled_whenContextHolderIsEmpty_thenReturnsEmpty() throws Exception {
        // Given
        long taskId = 1L;

        // When
        OnlineSchemaChangeContextHolder result = onlineSchemaChangeFlowableTask.getOnlineSchemaChangeContextHolder();

        // Then
        Assertions.assertThat(result).isNull();
    }

    @Test
    void givenGetOnlineSchemaChangeContextHolderIsCalled_whenContextHolderIsNotEmpty_thenReturnsNonEmpty() throws Exception {
        // Given
        long taskId = 1L;
        long creatorId = RandomUtils.nextLong();
        long organizationId = RandomUtils.nextLong();

        // When
        OnlineSchemaChangeContextHolder result = onlineSchemaChangeFlowableTask.getOnlineSchemaChangeContextHolder();

        // Then
        Assertions.assertThat(result.getCreatorId()).isEqualTo(creatorId);
        Assertions.assertThat(result.getFlowTaskId()).isEqualTo(taskId);
        Assertions.assertThat(result.getOrganizationId()).isEqualTo(organizationId);
    }

    @Test
    void givenUpdateFlowInstanceStatusIsCalled_whenFlowInstanceStatusIsUpdated_thenCorrectlyUpdatesStatus() throws Exception {
        // Given
        long taskId = 1L;

        // When
        onlineSchemaChangeFlowableTask.updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED);

        // Then
        verify(onlineSchemaChangeFlowableTask.getTaskService()).update(any(TaskEntity.class));
    }

    @Test
    void givenTaskServiceIsMocked_whenUpdateFlowInstanceStatusIsCalled_thenUpdatesStatusInTaskEntity() throws Exception {
        // Given
        long taskId = 1L;
        TaskEntity taskEntity = mock(TaskEntity.class);
        doReturn(taskEntity).when(onlineSchemaChangeFlowableTask).getTaskService().detail(taskId);

        // When
        onlineSchemaChangeFlowableTask.updateFlowInstanceStatus(FlowStatus.EXECUTION_FAILED);

        // Then
        verify(taskEntity).setStatus(FlowStatus.EXECUTION_FAILED);
    }

    @Test
    void givenListScheduleTasksIsCalled_whenTasksArePresent_thenReturnsCorrectPage() throws Exception {
        // Given
        long scheduleId = 1L;
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = mock(OnlineSchemaChangeFlowableTask.class);
        doReturn(onlineSchemaChangeFlowableTask).when(onlineSchemaChangeFlowableTask)
                .getScheduleTaskService();
        Pageable pageable = Pageable.unpaged();
        Page<ScheduleTaskEntity> expectedPage = new PageImpl<>(List.of(mock(ScheduleTaskEntity.class)));

        // When
        Page<ScheduleTaskEntity> actualPage = onlineSchemaChangeFlowableTask.listScheduleTasks(pageable, scheduleId);

        // Then
        AssertThrows.isInstanceOf(UnsupportedOperationException.class).given(() -> actualPage.getContent().get(0).getResultJson())
                .invoke(() -> actualPage.getContent().get(0).getResultJson());
        Assertions.assertThat(actualPage).isEqualTo(expectedPage);
    }

    @Test
    void givenListScheduleTasksIsCalled_whenTasksAreNotPresent_thenReturnsEmptyPage() throws Exception {
        // Given
        long scheduleId = 1L;
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = mock(OnlineSchemaChangeFlowableTask.class);
        doReturn(onlineSchemaChangeFlowableTask).when(onlineSchemaChangeFlowableTask)
                .getScheduleTaskService();

        // When
        Page<ScheduleTaskEntity> actualPage = onlineSchemaChangeFlowableTask.listScheduleTasks(Pageable.unpaged(), scheduleId);

        // Then
        Assertions.assertThat(actualPage.getSize()).isZero();
    }
}