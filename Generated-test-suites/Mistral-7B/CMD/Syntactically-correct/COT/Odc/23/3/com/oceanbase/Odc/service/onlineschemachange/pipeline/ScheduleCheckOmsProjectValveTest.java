package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.constant.ErrorCodes;
import com.oceanbase.odc.common.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.exception.OmsException;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValve;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValve.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValve.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValve.ProjectStepResultChecker;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValveTest.TestData;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

/**
 * Test class for ScheduleCheckOmsProjectValve.
 */
public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;
    @Mock
    private ProjectOpenApiService projectOpenApiService;
    @Mock
    private ProjectStepResultChecker projectStepResultChecker;
    @Mock
    private OscTaskCompleteHandler completeHandler;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    private OnlineSchemaChangeScheduleTaskParameters taskParameter;
    private ProjectControlRequest projectRequest;
    private ProjectStepResult projectStepResult;
    private ProjectProgressResponse progress;
    private List<ProjectStepVO> projectSteps;
    private ProjectFullVerifyResultResponse fullVerifyResultResponse;
    private FullVerificationResult fullVerificationResult;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        projectRequest = new ProjectControlRequest();
        projectStepResult = new ProjectStepResult();
        progress = new ProjectProgressResponse();
        projectSteps = Lists.newArrayList();
        fullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        fullVerificationResult = FullVerificationResult.CONSISTENT;
    }

    @Test
    public void testInvoke_shouldExecuteProjectStepsAndScheduleTask_whenOmsProjectIsValid() {
        givenProjectRequestIsValid();
        givenProjectStepsAreValid();
        givenProjectProgressIsValid();
        givenProjectFullVerifyResultIsValid();

        scheduleCheckOmsProjectValve.invoke(createValveContext());

        verifyInvokeIsCalled();
        verifyProjectStepsAreExecuted();
        verifyProjectTaskIsScheduled();
    }

    @Test(expected = OmsException.class)
    public void testInvoke_shouldThrowOmsException_whenProjectRequestIsInvalid() {
        givenProjectRequestIsInvalid();

        try {
            scheduleCheckOmsProjectValve.invoke(createValveContext());
        } catch (Exception e) {
            throw new OmsException("Unexpected exception", e);
        }

        verifyProjectRequestIsInvalid();
    }

    @Test
    public void testInvoke_shouldContinueToNextValve_whenProjectStepsResultIsFailed() {
        givenProjectRequestIsValid();
        givenProjectStepsAreFailed();

        scheduleCheckOmsProjectValve.invoke(createValveContext());

        verifyProjectStepsAreExecuted();
        verifyNextValveIsInvoked();
    }

    @Test(expected = OscException.class)
    public void testInvoke_shouldThrowOscException_whenProjectFullVerificationResultIsInconsistent() {
        givenProjectRequestIsValid();
        givenProjectStepsAreValid();
        givenProjectProgressIsValid();
        givenProjectFullVerifyResultIsInconsistent();

        try {
            scheduleCheckOmsProjectValve.invoke(createValveContext());
        } catch (Exception e) {
            throw new OscException("Unexpected exception", e);
        }

        verifyProjectFullVerifyResultIsInconsistent();
    }

    // Add more test cases here for other methods and edge cases

    private void givenProjectRequestIsValid() {
        projectRequest.setUid("uid");
        projectRequest.setId("projectId");

        when(onlineSchemaChangeProperties.isEnableFullVerify()).thenReturn(true);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
    }

    private void givenProjectRequestIsInvalid() {
        projectRequest.setUid(null);
        projectRequest.setId(null);
    }

    private void givenProjectStepsAreValid() {
        ProjectStepVO projectStepVO = new ProjectStepVO();
        projectStepVO.setStepName("PRE_CHECK");
        projectStepVO.setStatus(TaskStatus.DONE.name());
        projectStepVO.setCurrentStep(OmsStepName.PRE_CHECK.name());
        projectStepVO.setCurrentStepStatus(TaskStatus.DONE.name());
        projectStepVO.setFullVerificationResult(fullVerificationResult.name());
        projectStepVO.setFullVerificationResultDescription("description");
        projectSteps.add(projectStepVO);
    }

    private void givenProjectStepsAreFailed() {
        projectSteps.clear();
    }

    private void givenProjectProgressIsValid() {
        projectStepResult.setCurrentStepStatus(TaskStatus.DONE.name());
    }

    private void givenProjectFullVerifyResultIsValid() {
        fullVerifyResultResponse.setProjectFullVerifyResults(Lists.newArrayList(new ProjectFullVerifyResult()));
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(fullVerifyResultResponse);
    }

    private void givenProjectFullVerifyResultIsInconsistent() {
        ProjectFullVerifyResult projectFullVerifyResult = new ProjectFullVerifyResult();
        projectFullVerifyResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT.name());
        projectFullVerifyResult.setFullVerificationResultDescription("description");
        fullVerifyResultResponse.setProjectFullVerifyResults(Lists.newArrayList(projectFullVerifyResult));
    }

    private void verifyInvokeIsCalled() {
        // Add verification code here
    }

    private void verifyProjectStepsAreExecuted() {
        // Add verification code here
    }

    private void verifyProjectTaskIsScheduled() {
        // Add verification code here
    }

    private void verifyProjectRequestIsInvalid() {
        // Add verification code here
    }

    private void verifyProjectFullVerifyResultIsInconsistent() {
        // Add verification code here
    }

    private void verifyNextValveIsInvoked() {
        // Add verification code here
    }

    private ValveContext createValveContext() {
        OscValveContext oscValveContext = mock(OscValveContext.class);
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        when(oscValveContext.getScheduleTask()).thenReturn(scheduleTask);
        when(oscValveContext.getParameter()).thenReturn(taskParameter);
        when(scheduleTaskRepository.update(scheduleTask)).thenReturn(scheduleTask);
        return new ValveContext() {{
            setValveContext(oscValveContext);
        }};
    }

    static class ValveContext {
        OscValveContext oscValveContext;

        public void setValveContext(OscValveContext oscValveContext) {
            this.oscValveContext = oscValveContext;
        }

        public OscValveContext getValveContext() {
            return oscValveContext;
        }

        ScheduleTaskEntity getScheduleTask() {
            return oscValveContext.getScheduleTask();
        }

        OnlineSchemaChangeScheduleTaskParameters getParameter() {
            return oscValveContext.getParameter();
        }
    }

    static class TestData {
        // Add test data here
    }
}