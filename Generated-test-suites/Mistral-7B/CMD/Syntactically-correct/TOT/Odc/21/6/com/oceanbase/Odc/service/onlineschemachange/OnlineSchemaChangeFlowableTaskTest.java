package com.oceanbase.odc.service.onlineschemachange;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.Assertions;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.test.FlowableTestRule;
import org.flowable.engine.test.Deployment;
import org.flowable.engine.test.PersistenceTestConfiguration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.quartz.QuartzJobBuilder;
import com.oceanbase.odc.service.quartz.QuartzTestUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.task.TaskService;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class OnlineSchemaChangeFlowableTaskTest {

    @Rule
    public FlowableTestRule flowableTestRule = new FlowableTestRule();

    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private TaskService taskService;

    @Before
    public void setup() {
        QuartzTestUtils.clearScheduler();
    }

    @Deployment
    public String testDeployment() {
        return "classpath:/test-online-schema-change-flowable-task.bpmn";
    }

    @Test
    public void testOnlineSchemaChangeFlowableTask() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(RandomUtils.nextLong());
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setId(RandomUtils.nextLong());
        parameters.setSchemaName("test_schema");
        parameters.setContinueOnError(true);

        // Start flowable task
        ExecutionEntity execution = flowableTestRule.executeJobByQuery("test_online_schema_change_flowable_task",
                new JobDataMap().put("connectionConfig", connectionConfig)
                        .put("onlineSchemaChangeParameters", parameters));
        BaseODCFlowTaskDelegate<Void> taskDelegate = (BaseODCFlowTaskDelegate<Void>) flowableTestRule.getFlowableServerConnection()
                .getEngineServices()
                .getExecutionService()
                .findExecutionById(execution.getId())
                .getBusinessKeyProperty("taskDelegateName");

        // Test successful scenario
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = (OnlineSchemaChangeFlowableTask) taskDelegate;
        OnlineSchemaChangeContextHolder.set(onlineSchemaChangeFlowableTask.getCreatorId(), execution.getId(),
                onlineSchemaChangeFlowableTask.getOrganizationId());
        OnlineSchemaChangeScheduleTaskParameters subTaskParameters = parameters.generateSubTaskParameters(connectionConfig,
                "test_schema").get(0);
        OnlineSchemaChangeScheduleTaskEntity subTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(onlineSchemaChangeFlowableTask.getScheduleId(),
                subTaskParameters);
        onlineSchemaChangeFlowableTask.taskHandler.start(onlineSchemaChangeFlowableTask.getScheduleId(),
                subTaskEntity.getId());
        Thread.sleep(2000); // Give some time for Quartz job to start

        // Check if Quartz job is running
        Scheduler scheduler = QuartzTestUtils.getScheduler();
        JobDetail jobDetail = JobBuilder.newJob(OnlineSchemaChangeTaskHandler.class).build();
        List<JobExecution> executions = scheduler.getJobExecutions(jobDetail.getKey(), false);
        Assertions.assertThat(executions).hasSize(1);
        Assertions.assertThat(executions.get(0).getStatus()).isEqualTo(JobExecution.STATUS_RUNNING);

        // Check if task status is DONE
        TaskEntity taskEntity = taskService.detail(execution.getId());
        Assertions.assertThat(taskEntity.getStatus()).isEqualTo(TaskStatus.DONE);

        // Test error scenario
        parameters.setContinueOnError(false);
        execution = flowableTestRule.executeJobByQuery("test_online_schema_change_flowable_task",
                new JobDataMap().put("connectionConfig", connectionConfig)
                        .put("onlineSchemaChangeParameters", parameters));
        taskDelegate = (BaseODCFlowTaskDelegate<Void>) flowableTestRule.getFlowableServerConnection()
                .getEngineServices()
                .getExecutionService()
                .findExecutionById(execution.getId())
                .getBusinessKeyProperty("taskDelegateName");

        OnlineSchemaChangeFlowableTask errorOnlineSchemaChangeFlowableTask = (OnlineSchemaChangeFlowableTask) taskDelegate;
        OnlineSchemaChangeContextHolder.set(errorOnlineSchemaChangeFlowableTask.getCreatorId(), execution.getId(),
                errorOnlineSchemaChangeFlowableTask.getOrganizationId());
        OnlineSchemaChangeScheduleTaskParameters errorSubTaskParameters = parameters.generateSubTaskParameters(connectionConfig,
                "test_schema").get(0);
        OnlineSchemaChangeScheduleTaskEntity errorSubTaskEntity = errorOnlineSchemaChangeFlowableTask.createScheduleTaskEntity(errorOnlineSchemaChangeFlowableTask.getScheduleId(),
                errorSubTaskParameters);
        onlineSchemaChangeFlowableTask.taskHandler.start(errorOnlineSchemaChangeFlowableTask.getScheduleId(),
                errorSubTaskEntity.getId());
        Thread.sleep(2000); // Give some time for Quartz job to start

        // Check if Quartz job is running
        scheduler = QuartzTestUtils.getScheduler();
        executions = scheduler.getJobExecutions(jobDetail.getKey(), false);
        Assertions.assertThat(executions).hasSize(2);

        // Check if task status is FAILED
        taskEntity = taskService.detail(execution.getId());
        Assertions.assertThat(taskEntity.getStatus()).isEqualTo(TaskStatus.FAILED);

        // Clean up
        scheduler.shutdown();
        OnlineSchemaChangeContextHolder.clear();
    }
}