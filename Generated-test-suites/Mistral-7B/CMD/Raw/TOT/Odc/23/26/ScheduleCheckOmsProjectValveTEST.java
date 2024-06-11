import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ListProjectFullVerifyResultRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepChecker;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @MockBean
    private ProjectOpenApiService projectOpenApiService;
    @MockBean
    private OscTaskCompleteHandler completeHandler;
    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;
    @MockBean
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    @MockBean
    private ProjectStepResultChecker projectStepResultChecker;
    @MockBean
    private SwapTableUtil swapTableUtil;

    @Test
    public void testInvoke_success() throws Exception {
        // arrange
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        ProjectFullVerifyResultResponse verifyResult = mock(ProjectFullVerifyResultResponse.class);
        ListProjectFullVerifyResultRequest verifyRequest = mock(ListProjectFullVerifyResultRequest.class);
        doAnswer(invocation -> {
            List<ProjectFullVerifyResultResponse> list = Lists.newArrayList(verifyResult);
            ProjectStepChecker checker = projectStepResultChecker.newChecker();
            ProjectStepVO projectStepVO = mock(ProjectStepVO.class);
            checker.setCheckerVerifyResult(() -> list);
            checker.setResumeProject(() -> {});
            projectStepResult.setCheckerResult(checker);
            return projectStepResult;
        }).when(projectOpenApiService).describeProjectSteps(any(ProjectControlRequest.class));
        doReturn(progress).when(projectOpenApiService).describeProjectProgress(any(ProjectControlRequest.class));
        doReturn(verifyResult).when(projectOpenApiService).listProjectFullVerifyResult(verifyRequest);
        doReturn(scheduleTask).when(scheduleTaskRepository).findById(Mockito.anyLong());
        doReturn(taskParameter).when(scheduleCheckOmsProjectValve).getTaskParameter(scheduleTask);

        // act
        scheduleCheckOmsProjectValve.invoke(Mockito.mock(ValveContext.class));

        // assert
        assertEquals(TaskStatus.DONE, projectStepResult.getTaskStatus());
        assertEquals("CONSISTENT", projectStepResult.getFullVerificationResult().name());
        assertTrue(scheduleCheckOmsProjectValve.handleOmsProjectStepResult(
                Mockito.mock(ValveContext.class), projectStepResult, null, SwapTableType.AUTO, scheduleTask));
    }

    @Test
    public void testInvoke_error() throws Exception {
        // arrange
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        doAnswer(invocation -> {
            throw new OscException("Test error");
        }).when(projectOpenApiService).describeProjectSteps(any(ProjectControlRequest.class));

        // act & assert
        assertThrows(OscException.class, () -> scheduleCheckOmsProjectValve.invoke(Mockito.mock(ValveContext.class)));
    }

    @Test
    public void testInvoke_precheck_error() throws Exception {
        // arrange
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        doAnswer(invocation -> {
            List<ProjectStepVO> projectSteps = Lists.newArrayList(mock(ProjectStepVO.class));
            ProjectStepChecker checker = projectStepResultChecker.newChecker();
            checker.setCheckerVerifyResult(() -> null);
            checker.setResumeProject(() -> {});
            projectStepResult.setCheckerResult(checker);
            return projectStepResult;
        }).when(projectOpenApiService).describeProjectSteps(any(ProjectControlRequest.class));
        doReturn(progress).when(projectOpenApiService).describeProjectProgress(any(ProjectControlRequest.class));
        doThrow(new OscException("Precheck error")).when(projectStepResultChecker)
                .withCheckerVerifyResult(() -> null);
        doReturn(scheduleTask).when(scheduleTaskRepository).findById(Mockito.anyLong());
        doReturn(taskParameter).when(scheduleCheckOmsProjectValve).getTaskParameter(scheduleTask);

        // act & assert
        assertThrows(OscException.class, () -> scheduleCheckOmsProjectValve.invoke(Mockito.mock(ValveContext.class)));
    }

    @Test
    public void testInvoke_full_verification_error_inconsistent() throws Exception {
        // arrange
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        ProjectFullVerifyResultResponse verifyResult = mock(ProjectFullVerifyResultResponse.class);
        doAnswer(invocation -> {
            List<ProjectStepVO> projectSteps = Lists.newArrayList(mock(ProjectStepVO.class));
            ProjectStepChecker checker = projectStepResultChecker.newChecker();
            checker.setCheckerVerifyResult(() -> null);
            checker.setResumeProject(() -> {});
            projectStepResult.setCheckerResult(checker);
            return projectStepResult;
        }).when(projectOpenApiService).describeProjectSteps(any(ProjectControlRequest.class));
        doReturn(progress).when(projectOpenApiService).describeProjectProgress(any(ProjectControlRequest.class));
        doReturn(verifyResult).when(projectOpenApiService).listProjectFullVerifyResult(verifyRequest);
        doReturn(scheduleTask).when(scheduleTaskRepository).findById(Mockito.anyLong());
        doReturn(taskParameter).when(scheduleCheckOmsProjectValve).getTaskParameter(scheduleTask);
        doThrow(new OscException("Full verification error: inconsistent")).when(projectStepResultChecker)
                .withCheckerVerifyResult(() -> null);

        // act & assert
        assertThrows(OscException.class,
                () -> scheduleCheckOmsProjectValve.handleOmsProjectStepResult(
                        Mockito.mock(ValveContext.class), projectStepResult, null, SwapTableType.AUTO, scheduleTask));
    }

    @Test
    public void testInvoke_full_verification_error_suspended() throws Exception {
        // arrange
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        ProjectFullVerifyResultResponse verifyResult = mock(ProjectFullVerifyResultResponse.class);
        doAnswer(invocation -> {
            List<ProjectStepVO> projectSteps = Lists.newArrayList(mock(ProjectStepVO.class));
            ProjectStepChecker checker = projectStepResultChecker.newChecker();
            checker.setCheckerVerifyResult(() -> null);
            checker.setResumeProject(() -> {});
            projectStepResult.setCheckerResult(checker);
            return projectStepResult;
        }).when(projectOpenApiService).describeProjectSteps(any(ProjectControlRequest.class));
        doReturn(progress).when(projectOpenApiService).describeProjectProgress(any(ProjectControlRequest.class));
        doReturn(verifyResult).when(projectOpenApiService).listProjectFullVerifyResult(verifyRequest);
        doReturn(scheduleTask).when(scheduleTaskRepository).findById(Mockito.anyLong());
        doReturn(taskParameter).when(scheduleCheckOmsProjectValve).getTaskParameter(scheduleTask);
        doThrow(new OscException("Full verification error: suspended")).when(projectStepResultChecker)
                .withCheckerVerifyResult(() -> null);

        // act & assert
        assertFalse(scheduleCheckOmsProjectValve.handleOmsProjectStepResult(
                Mockito.mock(ValveContext.class), projectStepResult, null, SwapTableType.AUTO, scheduleTask));
    }

    @Test
    public void testInvoke_full_verification_error_unknown() throws Exception {
        // arrange
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        ProjectFullVerifyResultResponse verifyResult = mock(ProjectFullVerifyResultResponse.class);
        doAnswer(invocation -> {
            List<ProjectStepVO> projectSteps = Lists.newArrayList(mock(ProjectStepVO.class));
            ProjectStepChecker checker = projectStepResultChecker.newChecker();
            checker.setCheckerVerifyResult(() -> null);
            checker.setResumeProject(() -> {});
            projectStepResult.setCheckerResult(checker);
            return projectStepResult;
        }).when(projectOpenApiService).describeProjectSteps(any(ProjectControlRequest.class));
        doReturn(progress).when(projectOpenApiService).describeProjectProgress(any(ProjectControlRequest.class));
        doReturn(verifyResult).when(projectOpenApiService).listProjectFullVerifyResult(verifyRequest);
        doReturn(scheduleTask).when(scheduleTaskRepository).findById(Mockito.anyLong());
        doReturn(taskParameter).when(scheduleCheckOmsProjectValve).getTaskParameter(scheduleTask);
        doThrow(new OscException("Full verification error: unknown")).when(projectStepResultChecker)
                .withCheckerVerifyResult(() -> null);

        // act & assert
        assertFalse(scheduleCheckOmsProjectValve.handleOmsProjectStepResult(
                Mockito.mock(ValveContext.class), projectStepResult, null, SwapTableType.AUTO, scheduleTask));
    }

    @Test
    public void testInvoke_full_verification_success() throws Exception {
        // arrange
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        ProjectFullVerifyResultResponse verifyResult = mock(ProjectFullVerifyResultResponse.class);
        doAnswer(invocation -> {
            List<ProjectStepVO> projectSteps = Lists.newArrayList(mock(ProjectStepVO.class));
            ProjectStepChecker checker = projectStepResultChecker.newChecker();
            checker.setCheckerVerifyResult(() -> null);
            checker.setResumeProject(() -> {});
            projectStepResult.setCheckerResult(checker);
            return projectStepResult;
        }).when(projectOpenApiService).describeProjectSteps(any(ProjectControlRequest.class));
        doReturn(progress).when(projectOpenApiService).describeProjectProgress(any(ProjectControlRequest.class));
        doReturn(verifyResult).when(projectOpenApiService).listProjectFullVerifyResult(verifyRequest);
        doReturn(scheduleTask).when(scheduleTaskRepository).findById(Mockito.anyLong());
        doReturn(taskParameter).when(scheduleCheckOmsProjectValve).getTaskParameter(scheduleTask);
        doReturn(projectStepResult).when(projectStepResultChecker)
                .withCheckerVerifyResult(() -> Lists.newArrayList(verifyResult));

        // act
        ProjectStepResult returnProjectStepResult = scheduleCheckOmsProjectValve.handleOmsProjectStepResult(
                Mockito.mock(ValveContext.class), projectStepResult, null, SwapTableType.AUTO, scheduleTask);

        // assert
        assertEquals(projectStepResult, returnProjectStepResult);
        assertTrue(returnProjectStepResult.isFullVerificationSuccess());
    }

    @Test
    public void testGetTaskParameter() throws Exception {
        // arrange
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);

        // act
        OnlineSchemaChangeScheduleTaskParameters result = scheduleCheckOmsProjectValve.getTaskParameter(scheduleTask);

        // assert
        assertNotNull(result);
    }
}