package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

/**
 * @author yaobin
 */
public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve valve;

    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvokeWithSuccess() throws Exception {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = createTaskParameter();
        ScheduleTaskEntity scheduleTask = createScheduleTask(taskParameter);
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        ProjectProgressResponse progress = createProjectProgressResponse();
        ProjectStepVO step = createProjectStepVO();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(step));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        valve.invoke(createValveContext(scheduleTask, taskParameter));

        String resultJson = scheduleTask.getResultJson();
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(resultJson, OnlineSchemaChangeScheduleTaskResult.class);

        assertThat(result.getCurrentStepStatus()).isEqualTo(TaskStatus.DONE.toString());
        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
    }

    @Test
    public void testInvokeWithPrecheckFailed() throws Exception {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = createTaskParameter();
        ScheduleTaskEntity scheduleTask = createScheduleTask(taskParameter);
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        ProjectStepVO step = createProjectStepVO();
        ProjectProgressResponse progress = createProjectProgressResponse();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(step));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.resumeProject(projectRequest)).thenReturn(null);

        valve.invoke(createValveContext(scheduleTask, taskParameter));

        String resultJson = scheduleTask.getResultJson();
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(resultJson, OnlineSchemaChangeScheduleTaskResult.class);

        assertThat(result.getCurrentStepStatus()).isEqualTo(TaskStatus.FAILED.toString());
        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verify(projectOpenApiService, times(1)).resumeProject(projectRequest);
    }

    @Test
    public void testInvokeWithInconsistentData() throws Exception {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = createTaskParameter();
        ScheduleTaskEntity scheduleTask = createScheduleTask(taskParameter);
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        ProjectProgressResponse progress = createProjectProgressResponse();
        ProjectStepVO step = createProjectStepVO();
        ProjectFullVerifyResultResponse fullVerifyResponse = createFullVerifyResponse(FullVerificationResult.INCONSISTENT);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(step));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(new ListProjectFullVerifyResultRequest()))
                .thenReturn(fullVerifyResponse);

        valve.invoke(createValveContext(scheduleTask, taskParameter));

        String resultJson = scheduleTask.getResultJson();
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(resultJson, OnlineSchemaChangeScheduleTaskResult.class);

        assertThat(result.getCurrentStepStatus()).isEqualTo(TaskStatus.FAILED.toString());
        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(new ListProjectFullVerifyResultRequest());
    }

    private OnlineSchemaChangeScheduleTaskParameters createTaskParameter() {
        return new OnlineSchemaChangeScheduleTaskParameters();
    }

    private ScheduleTaskEntity createScheduleTask(OnlineSchemaChangeScheduleTaskParameters taskParameter) {
        return new ScheduleTaskEntity();
    }

    private ProjectControlRequest getProjectRequest(OnlineSchemaChangeScheduleTaskParameters taskParameter) {
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid(taskParameter.getUid());
        projectRequest.setId(taskParameter.getOmsProjectId());
        return projectRequest;
    }

    private ProjectStepVO createProjectStepVO() {
        return new ProjectStepVO();
    }

    private ProjectProgressResponse createProjectProgressResponse() {
        return new ProjectProgressResponse();
    }

    private void recordCurrentProgress(ScheduleTaskEntity scheduleTask, OnlineSchemaChangeScheduleTaskResult result) {
        // not used in test
    }

    private List<ProjectStepVO> createProjectStepsForResponse(List<ProjectStepVO> steps) {
        return steps;
    }

    private OnlineSchemaChangeScheduleTaskResult createTaskResult() {
        return new OnlineSchemaChangeScheduleTaskResult();
    }

    private ProjectFullVerifyResultResponse createFullVerifyResponse(FullVerificationResult result) {
        ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();
        response.setFullVerificationResults(Lists.newArrayList(new ProjectFullVerifyResultResponse.FullVerificationResult()
                .setTableName("testTable").setResult(result)));
        return response;
    }

    private OscValveContext createValveContext(ScheduleTaskEntity scheduleTask, OnlineSchemaChangeScheduleTaskParameters taskParameter) {
        return new OscValveContext() {
            @Override
            public ScheduleTaskEntity getScheduleTask() {
                return scheduleTask;
            }

            @Override
            public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
                return taskParameter;
            }
        };
    }

}