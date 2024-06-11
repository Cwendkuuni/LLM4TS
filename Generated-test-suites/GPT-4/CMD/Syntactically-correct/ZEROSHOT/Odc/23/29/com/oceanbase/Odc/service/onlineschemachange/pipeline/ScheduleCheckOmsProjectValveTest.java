package com.oceanbase.odc.service.onlineschemachange.pipeline;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.errors.beanvalidation.SpringValidatorAdapter;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.pipeline.OscValveContext;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValve;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleCheckOmsProjectValveTest {

    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    private OscValveContext context;

    @Before
    public void setUp() {
        context = new OscValveContext();
    }

    @Test
    public void testInvoke() {
        Mockito.doNothing().when(projectOpenApiService).resumeProject(any(ProjectControlRequest.class));
        when(onlineSchemaChangeProperties.isEnableFullVerify()).thenReturn(true);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ProjectControlRequest.class)))
                .thenReturn(new ProjectFullVerifyResultResponse());

        try {
            scheduleCheckOmsProjectValve.invoke(context);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testHandleOmsProjectStepResult() {
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectStepResult.setTaskStatus(TaskStatus.DONE);
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();

        try {
            scheduleCheckOmsProjectValve.handleOmsProjectStepResult(context, projectStepResult, result, SwapTableType.AUTO, scheduleTask);
        } catch (Exception e) {
            Assert.fail("Unexpected Exception thrown: " + e.getMessage());
        }
    }

    // Continue for other methods...
}
