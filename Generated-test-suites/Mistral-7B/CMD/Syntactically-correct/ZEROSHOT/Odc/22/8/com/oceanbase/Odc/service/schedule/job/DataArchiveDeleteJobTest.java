package com.oceanbase.odc.service.schedule.job;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.MockObject;
import org.quartz.impl.JobExecutionContextImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.StdSchedulerImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@Slf4j
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskEntity scheduleTaskEntity;
 private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 private ObjectMapper objectMapper = new ObjectMapper();
 private StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();
 private StdSchedulerImpl scheduler;

 @Before
 public void setUp() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson("{}");
 scheduleTaskEntity.setJobName("testJobName");
 scheduleTaskEntity.setJobGroup("testJobGroup");
 scheduleTaskEntity.setStatus(TaskStatus.DONE);

 scheduler = (StdSchedulerImpl) schedulerFactory.getScheduler();
 scheduler.start();
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 doReturn(Optional.empty()).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

 JobExecutionContext context = new JobExecutionContextImpl();
 context.setResult(scheduleTaskEntity);
 dataArchiveDeleteJob.execute(context);

 verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 scheduleTaskEntity.setStatus(TaskStatus.RUNNING);
 doReturn(Optional.of(scheduleTaskEntity)).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

 JobExecutionContext context = new JobExecutionContextImpl();
 context.setResult(scheduleTaskEntity);
 dataArchiveDeleteJob.execute(context);

 verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_Success() throws Exception {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 List<DlmTask> dlmTasks = new ArrayList<>();
 dlmTasks.add(new DlmTask());
 scheduleTaskEntity.setStatus(TaskStatus.DONE);
 doReturn(Optional.of(scheduleTaskEntity)).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

 JobExecutionContext context = new JobExecutionContextImpl();
 context.setResult(scheduleTaskEntity);
 dataArchiveDeleteJob.execute(context);

 // verify and check the status update
 verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.PREPARING);
 List<DlmTask> tasks = (List<DlmTask>) JsonUtils.fromJson(scheduleTaskEntity.getResultJson(), new TypeReference<List<DlmTask>>() {});
 assertEquals(1, tasks.size());
 DlmTask task = tasks.get(0);
 assertEquals(TaskStatus.PREPARING, task.getStatus());
 // verify and check the execute task
 verify(scheduler, times(1)).startJob(eq(dataArchiveDeleteJob), eq(dataArchiveTaskEntity(1L, dataArchiveClearParameters.getDataArchiveTaskId(), dlmTasks)));
 }

 private JobExecutionContext dataArchiveTaskEntity(long id, long dataArchiveTaskId, List<DlmTask> dlmTasks) {
 JobExecutionContext context = new JobExecutionContextImpl();
 context.setResult(scheduleTaskEntity);
 context.setMergedJobDataMap(map("dataArchiveTaskId", dataArchiveTaskId, "dlmTasks", dlmTasks));
 return context;
 }
}