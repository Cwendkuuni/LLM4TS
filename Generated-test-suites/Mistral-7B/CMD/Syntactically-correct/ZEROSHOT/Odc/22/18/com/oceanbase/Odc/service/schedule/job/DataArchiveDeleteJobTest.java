package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParametersTestData;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveDeleteJobTest {

 private static final Long DATA_ARCHIVE_TASK_ID = 1L;
 private static final String JOB_NAME = "DataArchiveDeleteJob";
 private static final String JOB_GROUP = "DataArchiveDeleteJobGroup";
 private static final LocalDateTime CURRENT_TIME = LocalDateTime.now();
 private static final TaskStatus INITIAL_TASK_STATUS = TaskStatus.CREATED;
 private static final TaskStatus FINISHED_TASK_STATUS = TaskStatus.DONE;
 private static final TaskStatus FAILED_TASK_STATUS = TaskStatus.FAILED;

 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;

 @InjectMocks
 private DataArchiveDeleteJob dataArchiveDeleteJob;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 }

 @After
 public void tearDown() {
 clearMocks(scheduleTaskRepository);
 }

 @Test
 public void testExecute_WhenDataArchiveTaskNotExist_ThenRollbackAndFailed() {
 // Given
 when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID))
 .thenReturn(Optional.empty());

 // When
 dataArchiveDeleteJob.execute(null);

 // Then
 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(FAILED_TASK_STATUS));
 }

 @Test
 public void testExecute_WhenDataArchiveTaskAlreadyFinished_ThenRollbackAndFailed() {
 // Given
 ScheduleTaskEntity dataArchiveTaskEntity = new ScheduleTaskEntity();
 dataArchiveTaskEntity.setId(DATA_ARCHIVE_TASK_ID);
 dataArchiveTaskEntity.setStatus(FINISHED_TASK_STATUS);
 dataArchiveTaskEntity.setResultJson(JsonUtils.toJson( Arrays.asList( new DlmTask() ) ));
 when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(dataArchiveTaskEntity));

 // When
 dataArchiveDeleteJob.execute(null);

 // Then
 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(FAILED_TASK_STATUS));
 }

 @Test
 public void testExecute_WhenDataArchiveTaskNotFinished_ThenExecuteTasksAndUpdateTaskStatus() {
 // Given
 ScheduleTaskEntity dataArchiveTaskEntity = new ScheduleTaskEntity();
 dataArchiveTaskEntity.setId(DATA_ARCHIVE_TASK_ID);
 dataArchiveTaskEntity.setStatus(INITIAL_TASK_STATUS);
 dataArchiveTaskEntity.setParametersJson(JsonUtils.toJson(new DataArchiveClearParametersTestData()));
 when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(dataArchiveTaskEntity));

 DlmTask dlmTask = new DlmTask();
 when(scheduleTaskRepository.save(any(DlmTask.class))).thenReturn(dlmTask);

 // When
 dataArchiveDeleteJob.execute(null);

 // Then
 verify(scheduleTaskRepository, times(1)).updateStatusById(eq(DATA_ARCHIVE_TASK_ID), eq(TaskStatus.PREPARING));
 verify(scheduleTaskRepository, times(1)).save(any(DlmTask.class));
 verify(scheduleTaskRepository, times(1)).updateStatusById(eq(DATA_ARCHIVE_TASK_ID), eq(TaskStatus.PREPARING));
 }
}