
package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ScheduleCheckOmsProjectValveTest {

    @Mock
    private ProjectOpenApiService projectOpenApiService;
    
    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
  
    @Test
    public void testInvoke() {
       OscValveContext context = mock(OscValveContext.class);
        when(context.getTaskParameter()).thenReturn(new OnlineSchemaChangeScheduleTaskParameters());
        scheduleCheckOmsProjectValve.invoke(context);
    }

    @Test(expected = OscException.class)
    public void testContinueHandleProjectStepResult() {
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);
        // OscException should be thrown
        scheduleCheckOmsProjectValve.continueHandleProjectStepResult(projectStepResult);
    }
    
    @Test
    public void testAdaptResult() {
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        ProjectStepResult projectStepResult = new ProjectStepResult();
        scheduleCheckOmsProjectValve.adaptResult(result, projectStepResult);

        assertEquals(0L, result.getFullTransferEstimatedCount(), 0);
        assertEquals(0L, result.getFullTransferFinishedCount(), 0);
        assertEquals(result.getFullTransferProgressPercentage(), projectStepResult.getFullTransferProgressPercentage());
        assertEquals(result.getFullVerificationResult(), projectStepResult.getFullVerificationResult());
        assertEquals(result.getFullVerificationResultDescription(), projectStepResult.getFullVerificationResultDescription());
        assertEquals(result.getFullVerificationProgressPercentage(), projectStepResult.getFullVerificationProgressPercentage());
        assertEquals(result.getCurrentStep(), projectStepResult.getCurrentStep());
        assertEquals(result.getCurrentStepStatus(), projectStepResult.getCurrentStepStatus());
        assertEquals(result.getPrecheckResult(), projectStepResult.getPreCheckResult());
    }
    
    @Test
    public void testGetProjectRequest() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setUid("TestUid");
        taskParameter.setOmsProjectId("TestProjectId");

        ProjectControlRequest request = this.scheduleCheckOmsProjectValve.getProjectRequest(taskParameter);
        
        assertEquals("TestUid", request.getUid());
        assertEquals("TestProjectId", request.getId());
    }
}
