
package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.OmsRequestException;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.OmsServiceException;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
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
    private ScheduleCheckOmsProjectValve valve;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke_ValidScenario_Success() throws OmsRequestException, OmsServiceException {
        OscValveContext mockContext = mock(OscValveContext.class);
        ScheduleTaskEntity mockTask = mock(ScheduleTaskEntity.class);
        when(mockContext.getScheduleTask()).thenReturn(mockTask);
        when(mockTask.getId()).thenReturn("123");
        OnlineSchemaChangeScheduleTaskParameters parameters = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        when(parameters.getOmsProjectId()).thenReturn("456");
        when(parameters.getDatabaseName()).thenReturn("myDb");
        when(parameters.getUid()).thenReturn("789");
        when(parameters.getSwapTableType()).thenReturn(SwapTableType.MANUAL);
        when(mockContext.getTaskParameter()).thenReturn(parameters);

        ProjectControlRequest request = valve.getProjectRequest(parameters);
        List<ProjectStepVO> steps = new ArrayList<ProjectStepVO>();
        when(projectOpenApiService.describeProjectSteps(request)).thenReturn(steps);

        ProjectProgressResponse progress = new ProjectProgressResponse();
        ProjectStepResult result = new ProjectStepResult();
        when(new ProjectStepResultChecker(progress, steps, true)
                .withCheckerVerifyResult(() -> valve.listProjectFullVerifyResult(mockArticle.getOmsProjectId(),
                        mockArticle.getDatabaseName(), mockArticle.getUid()))
                .withResumeProject(() -> {
                    projectOpenApiService.resumeProject(projectRequest);
                    return null;
                })
                .getCheckerResult()).thenReturn(result);

        valve.invoke(mockContext);
    }

    @Test(expected = OscException.class)
    public void testContinueHandleProjectStepResult_PreCheckFailed_ThrowsOscException() {
        ProjectStepResult result = new ProjectStepResult();
        result.setPreCheckResult(PrecheckResult.FAILED);
        valve.continueHandleProjectStepResult(result);
    }

    @Test(expected = OscException.class)
    public void testContinueHandleProjectStepResult_InconsistentData_ThrowsOscException() {
        ProjectStepResult result = new ProjectStepResult();
        result.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        valve.continueHandleProjectStepResult(result);
    }

    @Test
    public void testGetProjectRequest_NotNull() {
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();
        param.setUid("112233");
        param.setOmsProjectId("445566");
        assertNotNull(valve.getProjectRequest(param));
    }
}
