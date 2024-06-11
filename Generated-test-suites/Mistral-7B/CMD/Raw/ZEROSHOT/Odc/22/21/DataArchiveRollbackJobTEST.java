import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.model.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.model.AbstractDlmJobRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

@SuppressWarnings("unchecked")
public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob job;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 objectMapper = new ObjectMapper();
 job = new DataArchiveRollbackJob() {
 @Override
 AbstractDlmJobRepository getDlmJobRepository() {
 return scheduleTaskRepository;
 }
 };
 }

 @Test
 public void testExecute_Success() throws Exception {
 // given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
 scheduleTaskEntity.setJobName("testJobName");
 scheduleTaskEntity.setJobGroup("testJobGroup");
 scheduleTaskEntity.setStatus(TaskStatus.SCHEDULED);

 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());

 // when
 job.execute(Mockito.mock(JobExecutionContext.class));

 // then
 verify(scheduleTaskRepository).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.DONE);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() throws Exception {
 // given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setStatus(TaskStatus.SCHEDULED);

 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 // when & then
 assertFalse(job.execute(Mockito.mock(JobExecutionContext.class)));
 verify(scheduleTaskRepository).updateStatusById(ArgumentMatchers.anyLong(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_RollbackTasksPrepared() throws Exception {
 // given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
 scheduleTaskEntity.setJobName("testJobName");
 scheduleTaskEntity.setJobGroup("testJobGroup");
 scheduleTaskEntity.setStatus(TaskStatus.SCHEDULED);
 scheduleTaskEntity.setResultJson(JsonUtils.toJson(new TypeReference<List<DlmTask>>() {}
 .getType()));

 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());
 taskUnits.get(0).setStatus(TaskStatus.DONE);
 taskUnits.get(1).setStatus(TaskStatus.DONE);

 when(scheduleTaskRepository.updateStatusById(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(TaskStatus.DONE)))
 .thenReturn(1);

 // when
 job.execute(Mockito.mock(JobExecutionContext.class));

 // then
 verify(scheduleTaskRepository, times(2)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.DONE);
 }

}