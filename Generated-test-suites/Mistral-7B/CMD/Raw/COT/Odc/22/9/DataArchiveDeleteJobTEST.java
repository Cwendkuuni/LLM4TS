package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.job.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskRepository scheduleTaskRepository;

 @Before
 public void setUp() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 }

 @Test
 public void testExecute_TaskNotFound() throws Exception {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("non-existing-task-id");
 taskEntity.setId("task-id");
 taskEntity.setJobName("job-name");
 taskEntity.setJobGroup("job-group");
 taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
 taskEntity.setResultJson(JsonUtils.toJson(new ArrayList<>()));
 taskEntity.setStatus(TaskStatus.SCHEDULED);

 JobExecutionContext context = new JobExecutionContext();
 context.setMergedJobDataMap(new JobDataMap());
 context.setResult(taskEntity);

 // when
 dataArchiveDeleteJob.execute(context);

 // then
 Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById("task-id", TaskStatus.FAILED);
 }

 @Test
 public void testExecute_TaskNotFinished() throws Exception {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("task-id");
 taskEntity.setId("task-id");
 taskEntity.setJobName("job-name");
 taskEntity.setJobGroup("job-group");
 taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
 taskEntity.setResultJson(JsonUtils.toJson(Arrays.asList(new DlmTask())));
 taskEntity.setStatus(TaskStatus.RUNNING);

 JobExecutionContext context = new JobExecutionContext();
 context.setMergedJobDataMap(new JobDataMap());
 context.setResult(taskEntity);

 // when
 dataArchiveDeleteJob.execute(context);

 // then
 Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById("task-id", TaskStatus.FAILED);
 }

 @Test(expected = JobExecutionException.class)
 public void testExecute_TaskAlreadyFinished() throws Exception {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("task-id");
 taskEntity.setId("task-id");
 taskEntity.setJobName("job-name");
 taskEntity.setJobGroup("job-group");
 taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
 taskEntity.setResultJson(JsonUtils.toJson(Arrays.asList(new DlmTask())));
 taskEntity.setStatus(TaskStatus.DONE);

 JobExecutionContext context = new JobExecutionContext();
 context.setMergedJobDataMap(new JobDataMap());
 context.setResult(taskEntity);

 // when
 dataArchiveDeleteJob.execute(context);

 // then
 assertFalse(true); // this test case should not reach this point
 }

 @Test
 public void testExecute_TasksClearSuccessfully() throws Exception {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("task-id");
 taskEntity.setId("task-id");
 taskEntity.setJobName("job-name");
 taskEntity.setJobGroup("job-group");
 taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
 taskEntity.setResultJson(JsonUtils.toJson(Arrays.asList(new DlmTask(), new DlmTask())));
 taskEntity.setStatus(TaskStatus.DONE);

 DlmTask dlmTask1 = new DlmTask();
 dlmTask1.setId(DlmJobIdUtil.generateHistoryJobId("job-name", "job-group", "task-id", 0));
 dlmTask1.setStatus(TaskStatus.FAILED);
 DlmTask dlmTask2 = new DlmTask();
 dlmTask2.setId(DlmJobIdUtil.generateHistoryJobId("job-name", "job-group", "task-id", 1));
 dlmTask2.setStatus(TaskStatus.DONE);
 List<DlmTask> taskUnits = Arrays.asList(dlmTask1, dlmTask2);

 JobExecutionContext context = new JobExecutionContext();
 context.setMergedJobDataMap(new JobDataMap());
 context.setResult(taskEntity);

 // when
 dataArchiveDeleteJob.execute(context);

 // then
 Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById("task-id", TaskStatus.DONE);
 }
}