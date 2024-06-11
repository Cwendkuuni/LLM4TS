package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

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

@InjectMocks
public class ScheduleCheckOmsProjectValveTest {

    @Mock
    private ProjectOpenApiService projectOpenApiService;
    @Mock
    private OscTaskCompleteHandler completeHandler;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    private ScheduleCheckOmsProjectValve valve;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        valve = new ScheduleCheckOmsProjectValve();
        valve.setProjectOpenApiService(projectOpenApiService);
        valve.setOscTaskCompleteHandler(completeHandler);
        valve.setScheduleTaskRepository(scheduleTaskRepository);
        valve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
    }

    @Test
    public void testInvokeWithSuccess() {
        // given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();

        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        ProjectStepVO projectStep = new ProjectStepVO();
        projectStep.setCurrentStep(OmsStepName.FULL_TRANSFER.name());
        projectStep.setCurrentStepStatus(TaskStatus.DONE.name());
        List<ProjectStepVO> projectSteps = Lists.newArrayList(projectStep);

        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setProgress("50");

        ProjectFullVerifyResultResponse fullVerifyResult = new ProjectFullVerifyResultResponse();
        fullVerifyResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(fullVerifyResult);

        // when
        valve.invoke(null);

        // then
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(TaskStatus.DONE, result.getCurrentStepStatus());
        assertEquals(OmsStepName.FULL_TRANSFER, OmsStepName.valueOf(result.getCurrentStep()));
    }

    @Test
    public void testInvokeWithError() {
        // given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();

        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        ProjectStepVO projectStep = new ProjectStepVO();
        projectStep.setCurrentStep(OmsStepName.FULL_TRANSFER.name());
        projectStep.setCurrentStepStatus(TaskStatus.FAILED.name());
        List<ProjectStepVO> projectSteps = Lists.newArrayList(projectStep);

        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setProgress("50");

        ProjectFullVerifyResultResponse fullVerifyResult = new ProjectFullVerifyResultResponse();
        fullVerifyResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(fullVerifyResult);

        // when
        try {
            valve.invoke(null);
            fail();
        } catch (OscException e) {
            assertEquals(ErrorCodes.OmsDataCheckInconsistent, e.getErrorCode());
        }
    }

    private ProjectControlRequest getProjectRequest(OnlineSchemaChangeScheduleTaskParameters taskParam) {
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid(taskParam.getUid());
        projectRequest.setId(taskParam.getOmsProjectId());
        return projectRequest;
    }
}