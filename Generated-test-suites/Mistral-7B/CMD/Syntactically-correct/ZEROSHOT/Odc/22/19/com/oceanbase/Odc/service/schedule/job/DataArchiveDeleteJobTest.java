import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.JobType;
import com.oceanbase.odc.service.schedule.model.JobExecutionContext;
import com.oceanbase.tools.migrator.common.enums.JobStatus;

@SuppressWarnings("unchecked")
public class DataArchiveDeleteJobTest {

 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;
 @Mock
 private ObjectMapper objectMapper;
 @InjectMocks
 private DataArchiveDeleteJob dataArchiveDeleteJob;

 private ScheduleTaskEntity scheduleTaskEntity;
 private DlmTask dlmTask;
 private List<DlmTask> dlmTaskList;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);

 scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("dataArchiveJob");
 scheduleTaskEntity.setJobGroup("dataArchiveGroup");
 scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\":1}");
 scheduleTaskEntity.setResultJson("[]");
 scheduleTaskEntity.setStatus(TaskStatus.DONE);

 dlmTask = new DlmTask();
 dlmTask.setId(1L);
 dlmTask.setJobId(1L);
 dlmTask.setStatus(TaskStatus.DONE);

 dlmTaskList = new ArrayList<>();
 dlmTaskList.add(dlmTask);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // Given
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

 // When
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 // Then
 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_DataArchiveTaskAlreadyFinished() {
 // Given
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));

 // When
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 // Then
 verify(scheduleTaskRepository, times(1)).updateStatusById(eq(1L), eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() {
 // Given
 scheduleTaskEntity.setStatus(TaskStatus.RUNNING);
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));

 // When
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 // Then
 verify(scheduleTaskRepository, times(1)).updateStatusById(eq(1L), eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_PrepareTasksForClear() {
 // Given
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));

 // When
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 // Then
 ArgumentCaptor<List<DlmTask>> dlmTaskArgumentCaptor = ArgumentCaptor.forClass(List.class);
 verify(dataArchiveDeleteJob).executeTask(1L, dlmTaskArgumentCaptor.capture());

 List<DlmTask> actualDlmTaskList = dlmTaskArgumentCaptor.getValue();
 assertEquals(1, actualDlmTaskList.size());
 assertEquals(1L, actualDlmTaskList.get(0).getId());
 assertEquals(JobType.DELETE, actualDlmTaskList.get(0).getJobType());
 assertEquals(TaskStatus.PREPARING, actualDlmTaskList.get(0).getStatus());
 }

 @Test
 public void testGetTaskStatus() {
 // Given
 when(objectMapper.convertValue(dlmTaskList, List.class)).thenReturn(dlmTaskList);

 // When
 TaskStatus actualTaskStatus = dataArchiveDeleteJob.getTaskStatus(dlmTaskList);

 // Then
 assertEquals(JobStatus.SUCCESS, actualTaskStatus);
 }
}