import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Stub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
import com.oceanbase.odc.service.onlineschemachange.utils.TestHelper;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@Component
public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Mock
    private ProjectOpenApiService projectOpenApiService;
    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private OscTaskCompleteHandler completeHandler;
    @Mock
    private SwapTableUtil swapTableUtil;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvokeWithSuccess() {
        String projectId = UUID.randomUUID().toString();
        OnlineSchemaChangeScheduleTaskParameters taskParameters = TestHelper.getTestTaskParameters();
        ScheduleTaskEntity scheduleTaskEntity = TestHelper.getTestScheduleTaskEntity();

        ProjectControlRequest projectControlRequest = getProjectControlRequest(taskParameters);
        ProjectStepVO projectStepVO = TestHelper.getTestProjectStepVO();
        ProjectProgressResponse projectProgressResponse = TestHelper.getTestProjectProgressResponse();
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = TestHelper.getTestProjectFullVerifyResultResponse();

        when(projectOpenApiService.describeProjectSteps(projectControlRequest)).thenReturn(Lists.newArrayList(projectStepVO));
        when(projectOpenApiService.describeProjectProgress(projectControlRequest)).thenReturn(projectProgressResponse);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);

        ProjectStepResult projectStepResult = new ProjectStepResultChecker(projectProgressResponse, List.of(projectStepVO),
                onlineSchemaChangeProperties.isEnableFullVerify())
                .withCheckerVerifyResult(() -> projectFullVerifyResultResponse)
                .withResumeProject(() -> {})
                .getCheckerResult();

        OnlineSchemaChangeScheduleTaskResult onlineSchemaChangeScheduleTaskResult = new OnlineSchemaChangeScheduleTaskResult(taskParameters);
        adaptResult(onlineSchemaChangeScheduleTaskResult, projectStepResult);

        verify(scheduleTaskRepository, times(1)).update(scheduleTaskEntity);
        Assertions.assertNotNull(onlineSchemaChangeScheduleTaskResult.getResultJson());

        scheduleCheckOmsProjectValve.invoke(null);
    }

    @Test
    public void testInvokeWithError() {
        OnlineSchemaChangeScheduleTaskParameters taskParameters = TestHelper.getTestTaskParameters();

        ProjectControlRequest projectControlRequest = getProjectControlRequest(taskParameters);
        ProjectProgressResponse projectProgressResponse = TestHelper.getTestProjectProgressResponseWithError();

        when(projectOpenApiService.describeProjectProgress(projectControlRequest)).thenReturn(projectProgressResponse);

        ProjectStepResult projectStepResult = new ProjectStepResultChecker(projectProgressResponse, null, null)
                .getCheckerResult();

        OnlineSchemaChangeScheduleTaskResult onlineSchemaChangeScheduleTaskResult = new OnlineSchemaChangeScheduleTaskResult(taskParameters);

        verify(scheduleTaskRepository, never()).update(any(ScheduleTaskEntity.class));
        Assertions.assertNull(onlineSchemaChangeScheduleTaskResult.getResultJson());

        scheduleCheckOmsProjectValve.invoke(null);
    }

    @Test
    public void testInvokeWithSwapTableEnabled() {
        String projectId = UUID.randomUUID().toString();
        OnlineSchemaChangeScheduleTaskParameters taskParameters = TestHelper.getTestTaskParameters();
        ScheduleTaskEntity scheduleTaskEntity = TestHelper.getTestScheduleTaskEntity();

        ProjectControlRequest projectControlRequest = getProjectControlRequest(taskParameters);
        ProjectStepVO projectStepVO = TestHelper.getTestProjectStepVO();
        ProjectProgressResponse projectProgressResponse = TestHelper.getTestProjectProgressResponse();
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = TestHelper.getTestProjectFullVerifyResultResponse();

        when(projectOpenApiService.describeProjectSteps(projectControlRequest)).thenReturn(Lists.newArrayList(projectStepVO));
        when(projectOpenApiService.describeProjectProgress(projectControlRequest)).thenReturn(projectProgressResponse);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);

        ProjectStepResult projectStepResult = new ProjectStepResultChecker(projectProgressResponse, List.of(projectStepVO),
                onlineSchemaChangeProperties.isEnableFullVerify())
                .withCheckerVerifyResult(() -> projectFullVerifyResultResponse)
                .withResumeProject(() -> {})
                .getCheckerResult();

        OnlineSchemaChangeScheduleTaskResult onlineSchemaChangeScheduleTaskResult = new OnlineSchemaChangeScheduleTaskResult(taskParameters);
        SwapTableType swapTableType = SwapTableType.SCHEMA;
        adaptResult(onlineSchemaChangeScheduleTaskResult, projectStepResult);

        verify(scheduleTaskRepository, times(1)).update(scheduleTaskEntity);
        Assertions.assertNotNull(onlineSchemaChangeScheduleTaskResult.getResultJson());
        Assertions.assertTrue(onlineSchemaChangeScheduleTaskResult.isManualSwapTableEnabled());

        scheduleCheckOmsProjectValve.invoke(null);
    }

    @Test
    public void testInvokeWithSwapTableDisabled() {
        String projectId = UUID.randomUUID().toString();
        OnlineSchemaChangeScheduleTaskParameters taskParameters = TestHelper.getTestTaskParameters();
        ScheduleTaskEntity scheduleTaskEntity = TestHelper.getTestScheduleTaskEntity();

        ProjectControlRequest projectControlRequest = getProjectControlRequest(taskParameters);
        ProjectStepVO projectStepVO = TestHelper.getTestProjectStepVO();
        ProjectProgressResponse projectProgressResponse = TestHelper.getTestProjectProgressResponse();
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = TestHelper.getTestProjectFullVerifyResultResponse();

        when(projectOpenApiService.describeProjectSteps(projectControlRequest)).thenReturn(Lists.newArrayList(projectStepVO));
        when(projectOpenApiService.describeProjectProgress(projectControlRequest)).thenReturn(projectProgressResponse);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);

        ProjectStepResult projectStepResult = new ProjectStepResultChecker(projectProgressResponse, List.of(projectStepVO),
                onlineSchemaChangeProperties.isEnableFullVerify())
                .withCheckerVerifyResult(() -> projectFullVerifyResultResponse)
                .withResumeProject(() -> {})
                .getCheckerResult();

        OnlineSchemaChangeScheduleTaskResult onlineSchemaChangeScheduleTaskResult = new OnlineSchemaChangeScheduleTaskResult(taskParameters);
        SwapTableType swapTableType = SwapTableType.SCHEMA;
        adaptResult(onlineSchemaChangeScheduleTaskResult, projectStepResult);

        verify(scheduleTaskRepository, times(1)).update(scheduleTaskEntity);
        Assertions.assertNotNull(onlineSchemaChangeScheduleTaskResult.getResultJson());
        Assertions.assertFalse(onlineSchemaChangeScheduleTaskResult.isManualSwapTableEnabled());

        scheduleCheckOmsProjectValve.invoke(null);
    }

    private ProjectControlRequest getProjectControlRequest(OnlineSchemaChangeScheduleTaskParameters taskParameters) {
        ProjectControlRequest projectControlRequest = new ProjectControlRequest();
        projectControlRequest.setUid(taskParameters.getUid());
        projectControlRequest.setId(taskParameters.getOmsProjectId());
        return projectControlRequest;
    }
}