package com.oceanbase.odc.service.schedule.job;

 import static org.junit.Assert.*;
 import static org.mockito.Mockito.*;

 import java.util.ArrayList;
 import java.util.List;
 import java.util.Optional;

 import org.junit.After;
 import org.junit.Before;
 import org.junit.Test;
 import org.mockito.InjectMocks;
 import org.mockito.Mock;
 import org.quartz.JobExecutionContext;
 import org.quartz.JobExecutionException;

 import com.fasterxml.jackson.databind.ObjectMapper;
 import com.oceanbase.odc.common.json.JsonUtils;
 import com.oceanbase.odc.core.shared.constant.TaskStatus;
 import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
 import com.oceanbase.odc.service.dlm.model.DlmTask;
 import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
 import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
 import com.oceanbase.tools.migrator.common.enums.JobType;

 @Slf4j
 public class DataArchiveRollbackJobTest {

 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;
 @InjectMocks
 private DataArchiveRollbackJob dataArchiveRollbackJob;
 @Mock
 private Thread thread;
 @Mock
 private JobExecutionContext context;

 private ObjectMapper objectMapper;

 @Before
 public void setUp() {
 objectMapper = new ObjectMapper();
 dataArchiveRollbackJob.jobThread = thread;
 }

 @After
 public void tearDown() {
 dataArchiveRollbackJob = null;
 thread = null;
 context = null;
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("dataArchiveTaskId");
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 assertThrows(JobExecutionException.class, () -> {
 dataArchiveRollbackJob.execute(context);
 });

 verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskFound() throws Exception {
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("dataArchiveTaskId");
 rollbackParameters.setResultJson(JsonUtils.toJsonString(new ArrayList<>()));
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 List<DlmTask> taskUnits = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setId(1L);
 taskUnits.add(dlmTask);

 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));
 when(scheduleTaskRepository.updateStatusById(dataArchiveTask.getId(), eq(TaskStatus.FAILED)))
 .thenReturn(1);

 dataArchiveRollbackJob.execute(context);

 verify(scheduleTaskRepository, times(1)).updateStatusById(dataArchiveTask.getId(), eq(TaskStatus.DONE));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExecute_NullContext() {
 dataArchiveRollbackJob.execute(null);
 }

 @Test
 public void testGetTaskStatus() {
 List<DlmTask> taskUnits = new ArrayList<>();
 DlmTask dlmTask1 = new DlmTask();
 dlmTask1.setStatus(TaskStatus.DONE);
 DlmTask dlmTask2 = new DlmTask();
 dlmTask2.setStatus(TaskStatus.FAILED);
 taskUnits.add(dlmTask1);
 taskUnits.add(dlmTask2);

 assertEquals(TaskStatus.FAILED, dataArchiveRollbackJob.getTaskStatus(taskUnits));
 }

 @Test
 public void testGetTaskStatus_AllTasksSucceeded() {
 List<DlmTask> taskUnits = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setStatus(TaskStatus.DONE);
 taskUnits.add(dlmTask);

 assertEquals(TaskStatus.DONE, dataArchiveRollbackJob.getTaskStatus(taskUnits));
 }
 }