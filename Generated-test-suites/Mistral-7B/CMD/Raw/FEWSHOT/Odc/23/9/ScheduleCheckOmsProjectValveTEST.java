package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValveTest.createProjectControlRequest;
import static com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValveTest.createProjectFullVerifyResultResponse;
import static com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValveTest.createProjectOpenApiService;
import static com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValveTest.createProjectStepResult;
import static com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValveTest.createScheduleTaskEntity;
import static com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValveTest.createScheduleTaskRepository;
import static com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValveTest.createSwapTableType;
import static com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValveTest.createOnlineSchemaChangeScheduleTaskParameters;
import static com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValveTest.createOnlineSchemaChangeScheduleTaskResult;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.commons.cli.util.Table;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.PrecheckResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ListProjectFullVerifyResultRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * @author yaobin
 */
@Component
public class ScheduleCheckOmsProjectValveTest {

    private ProjectOpenApiService projectOpenApiService;
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ScheduleCheckOmsProjectValve valve;

    @Before
    public void setup() {
        projectOpenApiService = createProjectOpenApiService();
        onlineSchemaChangeProperties = mock(OnlineSchemaChangeProperties.class);
        scheduleTaskRepository = createScheduleTaskRepository();
        valve = new ScheduleCheckOmsProjectValve();
        valve.setProjectOpenApiService(projectOpenApiService);
        valve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
        valve.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testInvoke() {
        String projectId = "projectId";
        String databaseName = "databaseName";
        OnlineSchemaChangeScheduleTaskParameters taskParameter = createOnlineSchemaChangeScheduleTaskParameters(projectId,
                databaseName);
        ScheduleTaskEntity scheduleTask = createScheduleTaskEntity(projectId, taskParameter);

        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(createProjectStepVO());
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        ProjectProgressResponse progress = createProjectProgressResponse();
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        ProjectStepResult projectStepResult = new ProjectStepResultChecker(progress, projectSteps,
                true).getCheckerResult();

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        adaptResult(result, projectStepResult);
        ScheduleTaskEntity expectedScheduleTask = new ScheduleTaskEntity(scheduleTask);
        expectedScheduleTask.setResultJson(JsonUtils.toJson(result));
        expectedScheduleTask.setProgressPercentage(projectStepResult.getTaskPercentage());
        scheduleTaskRepository.update(expectedScheduleTask);

        valve.invoke(createValveContext(scheduleTask));

        ScheduleTaskEntity actualScheduleTask = scheduleTaskRepository.findById(scheduleTask.getId());
        assertThat(actualScheduleTask, is(notNullValue()));
        assertThat(actualScheduleTask.getResultJson(), is(notNullValue()));
        OnlineSchemaChangeScheduleTaskResult actualResult = JsonUtils.fromJson(actualScheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertThat(actualResult, is(notNullValue()));
        assertThat(actualResult.getFullVerificationResult(), is(notNullValue()));
        assertThat(actualResult.getFullVerificationResult(), is(projectStepResult.getFullVerificationResult()));
        assertThat(actualScheduleTask.getProgressPercentage(), is(projectStepResult.getTaskPercentage()));
    }

    private ProjectStepVO createProjectStepVO() {
        ProjectStepVO projectStepVO = new ProjectStepVO();
        projectStepVO.setStepName("StepName");
        projectStepVO.setTaskPercentage(50);
        projectStepVO.setStatus(TaskStatus.DONE.name());
        projectStepVO.setFullTransferProgressPercentage(50);
        projectStepVO.setFullTransferFinishedCount(10);
        projectStepVO.setFullTransferEstimatedCount(20);
        projectStepVO.setFullVerificationProgressPercentage(50);
        projectStepVO.setFullVerificationResultDescription("Description");
        projectStepVO.setFullVerificationResult(FullVerificationResult.CONSISTENT.name());
        projectStepVO.setPreCheckResultDescription("PrecheckDescription");
        projectStepVO.setPreCheckResult(PrecheckResult.SUCCEED.name());
        return projectStepVO;
    }

    private ProjectProgressResponse createProjectProgressResponse() {
        ProjectProgressResponse projectProgressResponse = new ProjectProgressResponse();
        projectProgressResponse.setProjectId("projectId");
        projectProgressResponse.setProjectName("projectName");
        projectProgressResponse.setTotalStepCount(1);
        projectProgressResponse.setTotalTransferedDataSize(1024L);
        projectProgressResponse.setTotalFinishedStepCount(1);
        projectProgressResponse.setTotalFailedStepCount(0);
        projectProgressResponse.setRunningStepName("StepName");
        return projectProgressResponse;
    }

    private ProjectControlRequest getProjectRequest(OnlineSchemaChangeScheduleTaskParameters taskParam) {
        return createProjectControlRequest(taskParam);
    }

    private ValveContext createValveContext(ScheduleTaskEntity scheduleTask) {
        OscValveContext context = new OscValveContext();
        context.setScheduleTask(scheduleTask);
        return context;
    }

    private void adaptResult(OnlineSchemaChangeScheduleTaskResult result, ProjectStepResult projectStepResult) {
        result.setFullTransferEstimatedCount(projectStepResult.getFullTransferEstimatedCount());
        result.setFullTransferFinishedCount(projectStepResult.getFullTransferFinishedCount());
        result.setFullTransferProgressPercentage(projectStepResult.getFullTransferProgressPercentage());
        result.setFullVerificationResult(projectStepResult.getFullVerificationResult());
        result.setFullVerificationResultDescription(projectStepResult.getFullVerificationResultDescription());
        result.setFullVerificationProgressPercentage(projectStepResult.getFullVerificationProgressPercentage());
        result.setCurrentStep(projectStepResult.getCurrentStep());
        result.setCurrentStepStatus(projectStepResult.getCurrentStepStatus());
        result.setPrecheckResult(projectStepResult.getPrecheckResult());
    }
}