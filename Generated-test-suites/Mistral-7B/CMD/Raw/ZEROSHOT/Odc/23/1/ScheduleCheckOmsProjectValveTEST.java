package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

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
import com.oceanbase.odc.service.onlineschemachange.testutils.ValveContextTestBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScheduleCheckOmsProjectValveTest {

    private ProjectOpenApiService projectOpenApiService;
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    private ScheduleTaskRepository scheduleTaskRepository;
    private OscTaskCompleteHandler completeHandler;
    private ScheduleCheckOmsProjectValve valve;

    @Before
    public void setup() {
        projectOpenApiService = mock(ProjectOpenApiService.class);
        onlineSchemaChangeProperties = mock(OnlineSchemaChangeProperties.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        completeHandler = mock(OscTaskCompleteHandler.class);
        valve = new ScheduleCheckOmsProjectValve();
        valve.setProjectOpenApiService(projectOpenApiService);
        valve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
        valve.setScheduleTaskRepository(scheduleTaskRepository);
        valve.setCompleteHandler(completeHandler);
    }

    @Test
    public void testInvoke() throws Exception {
        String projectId = "testProjectId";
        String databaseName = "testDatabaseName";
        String uid = "testUid";
        OnlineSchemaChangeScheduleTaskParameters taskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameters.setOmsProjectId(projectId);
        taskParameters.setDatabaseName(databaseName);
        taskParameters.setUid(uid);
        taskParameters.setSwapTableType(SwapTableType.MANUAL);

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

        ProjectControlRequest projectRequest = getProjectRequest(taskParameters);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(
                new ProjectStepVO(OmsStepName.PRE_CHECK.name(), TaskStatus.RUNNING, "PRE_CHECK_DESC"));
        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setCurrentStep(OmsStepName.PRE_CHECK.name());
        progress.setCurrentStepStatus(TaskStatus.RUNNING.name());

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(new ListProjectFullVerifyResultRequest()))
                .thenReturn(new ProjectFullVerifyResultResponse());
        when(scheduleTaskRepository.update(scheduleTask)).thenReturn(scheduleTask);

        valve.invoke(ValveContextTestBuilder.build(projectId, taskParameters, scheduleTask));

        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(TaskStatus.RUNNING, result.getCurrentStepStatus());
    }

    private ProjectControlRequest getProjectRequest(OnlineSchemaChangeScheduleTaskParameters taskParam) {
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid(taskParam.getUid());
        projectRequest.setId(taskParam.getOmsProjectId());
        return projectRequest;
    }
}