import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.json.JsonUtils;
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
import com.oceanbase.odc.service.onlineschemachange.valve.ScheduleCheckOmsProjectValve;
import com.oceanbase.odc.service.onlineschemachange.valve.ValveContext;

import static org.mockito.Mockito.*;

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

    @Captor
    private ArgumentCaptor<OnlineSchemaChangeScheduleTaskResult> resultCaptor;
    @Captor
    private ArgumentCaptor<ProjectControlRequest> projectControlRequestCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke_success() throws Exception {
        // given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        ProjectStepResult projectStepResult = new ProjectStepResult(TaskStatus.DONE,
                new FullVerificationResult("CONSISTENT"), "full verification result description");
        ProjectStepVO projectStepVO = new ProjectStepVO();
        projectStepVO.setCurrentStep("FULL_VERIFIER");
        projectStepVO.setCurrentStepStatus("SUCCEEDED");
        projectStepResult.setProjectStepVO(projectStepVO);

        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setProjectSteps(Lists.newArrayList(projectStepVO));

        ProjectFullVerifyResultResponse fullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        fullVerifyResultResponse.setFullVerificationResult(new FullVerificationResult("CONSISTENT"));

        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(Lists.newArrayList(projectStepVO));
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(fullVerifyResultResponse);

        // when
        ValveContext valveContext = new ValveContext(taskParameter, scheduleTask);
        valve.invoke(valveContext);

        // then
        verify(scheduleTaskRepository, times(1)).update(scheduleTask);
        verify(projectOpenApiService, times(1)).resumeProject(projectControlRequestCaptor.capture());
        OnlineSchemaChangeScheduleTaskResult actualResult = resultCaptor.getValue(valveContext.getResult());
        assertEquals(TaskStatus.DONE, actualResult.getStatus());
        assertEquals(FullVerificationResult.CONSISTENT, actualResult.getFullVerificationResult());
        assertTrue(actualResult.isManualSwapTableEnabled());
        assertEquals(0, actualResult.getManualSwapTableStarted());
        verifyNoMoreInteractions(projectOpenApiService);
    }

    @Test
    public void testInvoke_full_verification_failed() throws Exception {
        // given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        ProjectStepResult projectStepResult = new ProjectStepResult(TaskStatus.FAILED,
                new FullVerificationResult("INCONSISTENT"), "full verification result description");
        ProjectStepVO projectStepVO = new ProjectStepVO();
        projectStepVO.setCurrentStep("FULL_VERIFIER");
        projectStepVO.setCurrentStepStatus("FAILED");
        projectStepResult.setProjectStepVO(projectStepVO);

        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setProjectSteps(Lists.newArrayList(projectStepVO));

        ProjectFullVerifyResultResponse fullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        fullVerifyResultResponse.setFullVerificationResult(new FullVerificationResult("INCONSISTENT"));

        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(Lists.newArrayList(projectStepVO));
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(fullVerifyResultResponse);

        // when
        ValveContext valveContext = new ValveContext(taskParameter, scheduleTask);
        valve.invoke(valveContext);

        // then
        verify(scheduleTaskRepository, never()).update(scheduleTask);
        verify(projectOpenApiService, times(1)).resumeProject(projectControlRequestCaptor.capture());
        OnlineSchemaChangeScheduleTaskResult actualResult = resultCaptor.getValue(valveContext.getResult());
        assertEquals(TaskStatus.FAILED, actualResult.getStatus());
        assertEquals(FullVerificationResult.INCONSISTENT, actualResult.getFullVerificationResult());
        assertFalse(actualResult.isManualSwapTableEnabled());
        assertEquals(0, actualResult.getManualSwapTableStarted());
        verifyNoMoreInteractions(projectOpenApiService);
    }

    @Test(expected = OscException.class)
    public void testInvoke_precheck_failed() throws Exception {
        // given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        ProjectStepResult projectStepResult = new ProjectStepResult(TaskStatus.FAILED,
                new PrecheckResult("FAILED"), "precheck result description");
        ProjectStepVO projectStepVO = new ProjectStepVO();
        projectStepVO.setCurrentStep("PRE_CHECK");
        projectStepVO.setCurrentStepStatus("FAILED");
        projectStepResult.setProjectStepVO(projectStepVO);

        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setProjectSteps(Lists.newArrayList(projectStepVO));

        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(Lists.newArrayList(projectStepVO));
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(progress);

        // when
        ValveContext valveContext = new ValveContext(taskParameter, scheduleTask);
        valve.invoke(valveContext);

        // then
        verify(scheduleTaskRepository, never()).update(scheduleTask);
        verify(projectOpenApiService, never()).resumeProject(projectControlRequestCaptor.capture());
        OnlineSchemaChangeScheduleTaskResult actualResult = resultCaptor.getValue(valveContext.getResult());
        assertEquals(TaskStatus.FAILED, actualResult.getStatus());
        assertEquals(PrecheckResult.FAILED, actualResult.getPrecheckResult());
        assertFalse(actualResult.isManualSwapTableEnabled());
        assertEquals(0, actualResult.getManualSwapTableStarted());
        throw new OscException("Test case throw exception");
    }
}