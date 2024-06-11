package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.MockitoJobExecutionListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.JobThread;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.tools.migrator.common.utils.DlmJobIdUtil;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ObjectMapper objectMapper;
    private Scheduler scheduler;

    @Before
    public void setup() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        scheduleTaskRepository = new ScheduleTaskRepository();
        objectMapper = new ObjectMapper();
        scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() throws Exception {
        JobExecutionContext context = new MockContext();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(-1L);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setJobName("testJobName");
        taskEntity.setJobGroup("testJobGroup");
        taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
        scheduleTaskRepository.save(taskEntity);

        dataArchiveRollbackJob.execute(context);

        List<ScheduleTaskEntity> taskEntities = scheduleTaskRepository.findAll();
        ScheduleTaskEntity taskEntityAfterExecution = taskEntities.get(0);
        assertEquals(TaskStatus.FAILED, taskEntityAfterExecution.getStatus());
    }

    @Test
    public void testExecute_RollbackTasksPreparation() throws Exception {
        JobExecutionContext context = new MockContext();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setJobName("testJobName");
        dataArchiveTask.setJobGroup("testJobGroup");
        dataArchiveTask.setResultJson(JsonUtils.toJsonString( Arrays.asList(new DlmTask(), new DlmTask())));
        scheduleTaskRepository.save(dataArchiveTask);

        dataArchiveRollbackJob.execute(context);

        List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());
        for (int i = 0; i < taskUnits.size(); i++) {
            DlmTask taskUnit = taskUnits.get(i);
            assertEquals(JobType.ROLLBACK, taskUnit.getJobType());
            assertEquals(TaskStatus.PREPARING, taskUnit.getStatus());
        }
    }

    @Test(expected = JobExecutionException.class)
    @SneakyThrows
    public void testExecute_TaskExecutionException() throws Exception {
        JobExecutionContext context = new MockContext();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setJobName("testJobName");
        dataArchiveTask.setJobGroup("testJobGroup");
        dataArchiveTask.setResultJson(JsonUtils.toJsonString(Arrays.asList(new DlmTask())));
        scheduleTaskRepository.save(dataArchiveTask);

        ScheduleTaskEntity exceptionTaskEntity = new ScheduleTaskEntity();
        exceptionTaskEntity.setId(2L);
        exceptionTaskEntity.setJobName("testJobNameException");
        exceptionTaskEntity.setJobGroup("testJobGroupException");
        exceptionTaskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
        scheduleTaskRepository.save(exceptionTaskEntity);

        dataArchiveRollbackJob.execute(context);

        List<ScheduleTaskEntity> taskEntities = scheduleTaskRepository.findAll();
        ScheduleTaskEntity taskEntityAfterExecution = taskEntities.get(1);
        assertEquals(TaskStatus.FAILED, taskEntityAfterExecution.getStatus());
    }

    @Test
    public void testGetTaskStatus() throws Exception {
        List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());
        assertEquals(TaskStatus.DONE, dataArchiveRollbackJob.getTaskStatus(taskUnits));

        taskUnits.get(0).setStatus(TaskStatus.FAILED);
        assertEquals(TaskStatus.FAILED, dataArchiveRollbackJob.getTaskStatus(taskUnits));
    }

    private static class MockContext implements JobExecutionContext {
        private static final long serialVersionUID = -6978152130550267374L;

        @Override
        public String getName() {
            return "testJobName";
        }

        @Override
        public String getJobDetailKey() {
            return "testJobName";
        }

        @Override
        public JobExecutionContext getPreviousContext() {
            return null;
        }

        @Override
        public JobDataMap getJobDataMap() {
            return null;
        }

        @Override
        public JobDataMap getMergedJobDataMap() {
            return null;
        }

        @Override
        public String getJobGroup() {
            return "testJobGroup";
        }

        @Override
        public TriggerKey getTriggerKey() {
            return null;
        }

        @Override
        public Trigger getTrigger() {
            return null;
        }

        @Override
        public JobDataMap getExtendedJobDataMap() {
            return null;
        }

        @Override
        public JobExecutionContext getFireContext() {
            return null;
        }

        @Override
        public JobExecutionException getPreviousExecutionException() {
            return null;
        }

        @Override
        public String getMergedJobDataMapKey() {
            return "testJobName";
        }

        @Override
        public Scheduler scheduler() {
            return scheduler;
        }

        @Override
        public List<Trigger> triggers() {
            return Arrays.asList(new Trigger());
        }

        @Override
        public List<Trigger> getTriggers() {
            return Arrays.asList(new Trigger());
        }

        @Override
        public String getJobName() {
            return "testJobName";
        }

        @Override
        public String getJobClassName() {
            return DataArchiveRollbackJob.class.getName();
        }

        @Override
        public String getInstanceId() {
            return "testInstanceId";
        }

        @Override
        public JobExecutionException getException() {
            return null;
        }

        @Override
        public Long getNextFireTime() {
            return 1L;
        }

        @Override
        public Long getPreviousFireTime() {
            return 1L;
        }

        @Override
        public Long getMisfireInstruction() {
            return 1L;
        }

        @Override
        public Long getStartTime() {
            return 1L;
        }

        @Override
        public Long getEndTime() {
            return 1L;
        }

        @Override
        public Long getCreatedTime() {
            return 1L;
        }

        @Override
        public Long getLastModifiedTime() {
            return 1L;
        }

        @Override
        public Long getPriority() {
            return 1L;
        }

        @Override
        public String getDescription() {
            return "testJobDescription";
        }

        @Override
        public Map<String, Object> getJobDataMapAsMap() {
            return null;
        }

        @Override
        public String getId() {
            return "testJobId";
        }

        @Override
        public String getFireInstanceId() {
            return "testFireInstanceId";
        }

        @Override
        public int getVersion() {
            return 1;
        }

        @Override
        public void setFireInstanceId(String fireInstanceId) {
        }

        @Override
        public void setInstanceId(String instanceId) {
        }

        @Override
        public void setDescription(String description) {
        }

        @Override
        public void setJobName(String jobName) {
        }

        @Override
        public void setJobGroup(String jobGroup) {
        }

        @Override
        public void setTrigger(Trigger trigger) {
        }

        @Override
        public void setJobDataMap(JobDataMap jobDataMap) {
        }

        @Override
        public void setMergedJobDataMap(JobDataMap mergedJobDataMap) {
        }

        @Override
        public void setJobDataMapKeys(List<String> keys) {
        }

        @Override
        public void setExtendedJobDataMap(JobDataMap extendedJobDataMap) {
        }

        @Override
        public void setScheduler(Scheduler scheduler) {
        }

        @Override
        public void setListeners(List<JobListener> listeners) {
            scheduler.getListenerRegistry().addJobListener(new MockitoJobExecutionListener(),
                    GroupMatcher.jobGroupEquals("testJobGroup"));
        }

        @Override
        public void setMergedJobDataMapKeys(List<String> keys) {
        }

        @Override
        public void setJobDetail(JobDetail jobDetail) {
        }

        @Override
        public void setJobExecutionContext(JobExecutionContext jobExecutionContext) {
        }

        @Override
        public void setFireTime(Long fireTime) {
        }

        @Override
        public void setMisfireInstruction(Long misfireInstruction) {
        }

        @Override
        public void setNextFireTime(Long nextFireTime) {
        }

        @Override
        public void setPreviousFireTime(Long previousFireTime) {
        }

        @Override
        public void setCreatedTime(Long createdTime) {
        }

        @Override
        public void setLastModifiedTime(Long lastModifiedTime) {
        }

        @Override
        public void setPriority(int priority) {
        }

        @Override
        public void setDescriptionKey(String descriptionKey) {
        }

        @Override
        public void setJobDataMap(Map<String, Object> jobDataMap) {
        }

        @Override
        public void setJobExecutionException(JobExecutionException jobExecutionException) {
        }

        @Override
        public void setMergeable(boolean mergeable) {
        }

        @Override
        public void setLocked(boolean locked) {
        }

        @Override
        public void setDurability(boolean durability) {
        }

        @Override
        public void setIdentity(boolean identity) {
        }

        @Override
        public void setIsUpdateRequired(boolean isUpdateRequired) {
        }

        @Override
        public void setTenantId(Long tenantId) {
        }
    }
}