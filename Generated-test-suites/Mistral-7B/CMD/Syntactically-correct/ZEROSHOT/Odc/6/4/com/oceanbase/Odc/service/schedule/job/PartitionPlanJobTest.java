import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.matchers.JobKeyMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
@Slf4j
public class PartitionPlanJobTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    private PartitionPlanService partitionPlanService;

    private PartitionPlanTaskService partitionPlanTaskService;

    private ScheduleService scheduleService;

    private Scheduler scheduler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        partitionPlanService = Mockito.mock(PartitionPlanService.class);
        partitionPlanTaskService = Mockito.mock(PartitionPlanTaskService.class);
        scheduleService = Mockito.mock(ScheduleService.class);
        partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.partitionPlanService = partitionPlanService;
        partitionPlanJob.partitionPlanTaskService = partitionPlanTaskService;
        partitionPlanJob.scheduleService = scheduleService;
        scheduler = new StdSchedulerFactory().getScheduler();
    }

    @Test
    public void testExecute() throws Exception {
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        Mockito.when(context.getMergedJobDataMap()).thenReturn(new JobDataMap());
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        Mockito.when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        Mockito.when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);
        List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity(), new TablePartitionPlanEntity());
        Mockito.when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        Mockito.when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(context);

        Mockito.verify(scheduleService, Mockito.times(1)).terminate(scheduleEntity);
    }

    @Test
    public void testExecuteWhenDatabasePartitionPlanIsDisabledOrNoTableToProcess() throws Exception {
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        Mockito.when(context.getMergedJobDataMap()).thenReturn(new JobDataMap());
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        Mockito.when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        Mockito.when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(false);
        List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList();
        Mockito.when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        Mockito.when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(context);

        Mockito.verify(scheduleService, Mockito.times(1)).terminate(scheduleEntity);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInterrupt() throws Exception {
        partitionPlanJob.interrupt();
    }

    @Test
    public void testGetBean() {
        PartitionPlanJob partitionPlanJob = SpringContextUtil.getBean(PartitionPlanJob.class);
        assertNotNull(partitionPlanJob);
    }

    @Test
    public void testGetPartitionPlanService() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        PartitionPlanService partitionPlanService = Mockito.mock(PartitionPlanService.class);
        partitionPlanJob.partitionPlanService = partitionPlanService;
        PartitionPlanService partitionPlanService1 = partitionPlanJob.getPartitionPlanService();
        assertEquals(partitionPlanService, partitionPlanService1);
    }

    @Test
    public void testGetPartitionPlanTaskService() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        PartitionPlanTaskService partitionPlanTaskService = Mockito.mock(PartitionPlanTaskService.class);
        partitionPlanJob.partitionPlanTaskService = partitionPlanTaskService;
        PartitionPlanTaskService partitionPlanTaskService1 = partitionPlanJob.getPartitionPlanTaskService();
        assertEquals(partitionPlanTaskService, partitionPlanTaskService1);
    }

    @Test
    public void testGetScheduleService() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        ScheduleService scheduleService = Mockito.mock(ScheduleService.class);
        partitionPlanJob.scheduleService = scheduleService;
        ScheduleService scheduleService1 = partitionPlanJob.getScheduleService();
        assertEquals(scheduleService, scheduleService1);
    }

    @Test
    public void testTestCreateJob() throws Exception {
        JobBuilder jobBuilder = JobBuilder.newJob(PartitionPlanJob.class);
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        jobBuilder.withIdentity("testJob", "testGroup");
        jobBuilder.usingJobData("scheduleId", 1L);
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("scheduleId", 1L);
        context.setMergedJobDataMap(jobDataMap);
        JobExecutionContext context1 = jobBuilder.build().execute(context);
        assertNotNull(context1);
        Mockito.verify(scheduleService, Mockito.times(0)).terminate(Mockito.any(ScheduleEntity.class));
    }

    @Test
    public void testTestCreateJobWhenDatabasePartitionPlanIsDisabledOrNoTableToProcess() throws Exception {
        JobBuilder jobBuilder = JobBuilder.newJob(PartitionPlanJob.class);
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        jobBuilder.withIdentity("testJob", "testGroup");
        jobBuilder.usingJobData("scheduleId", 1L);
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("scheduleId", 1L);
        context.setMergedJobDataMap(jobDataMap);
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(false);
        List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList();
        PartitionPlanService partitionPlanService = Mockito.mock(PartitionPlanService.class);
        Mockito.when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        Mockito.when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);
        PartitionPlanJob partitionPlanJob1 = new PartitionPlanJob();
        partitionPlanJob1.partitionPlanService = partitionPlanService;
        JobExecutionContext context1 = jobBuilder.build().execute(context);
        Mockito.verify(scheduleService, Mockito.times(1)).terminate(Mockito.argThat(scheduleEntity -> scheduleEntity.getId().equals(1L)));
    }

    @Test
    public void testTestScheduleJob() throws Exception {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "group1")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(10)
                        .repeatForever())
                .build();
        JobDetail jobDetail = JobBuilder.newJob(PartitionPlanJob.class)
                .withIdentity("job1", "group1")
                .build();
        scheduler.start();
        scheduler.scheduleJob(jobDetail, trigger);
        Thread.sleep(15000);
        Mockito.verify(scheduleService, Mockito.times(1)).saveOrUpdateSchedule(Mockito.any(ScheduleEntity.class));
        scheduler.shutdown();
    }

    @Test
    public void testTestScheduleJobWhenDatabasePartitionPlanIsDisabledOrNoTableToProcess() throws Exception {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(false);
        List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList();
        PartitionPlanService partitionPlanService = Mockito.mock(PartitionPlanService.class);
        Mockito.when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        Mockito.when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.partitionPlanService = partitionPlanService;
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "group1")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(10)
                        .repeatForever())
                .build();
        JobDetail jobDetail = JobBuilder.newJob(PartitionPlanJob.class)
                .withIdentity("job1", "group1")
                .build();
        scheduler.start();
        scheduler.scheduleJob(jobDetail, trigger);
        Mockito.verify(scheduleService, Mockito.times(1)).terminate(Mockito.argThat(scheduleEntity -> scheduleEntity.getId().equals(1L)));
        scheduler.shutdown();
    }

    @Test
    public void testTestGetRunningJobs() throws Exception {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        scheduler.start();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "group1")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(10)
                        .repeatForever())
                .build();
        JobDetail jobDetail = JobBuilder.newJob(PartitionPlanJob.class)
                .withIdentity("job1", "group1")
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
        List<JobExecutionContext> runningJobs = scheduler.getRunningJobs();
        assertEquals(1, runningJobs.size());
        scheduler.shutdown();
    }

    @Test
    public void testTestGetJobKeys() throws Exception {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        scheduler.start();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "group1")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(10)
                        .repeatForever())
                .build();
        JobDetail jobDetail = JobBuilder.newJob(PartitionPlanJob.class)
                .withIdentity("job1", "group1")
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
        List<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.anyJobGroup("group1"));
        assertEquals(1, jobKeys.size());
        scheduler.shutdown();
    }
}