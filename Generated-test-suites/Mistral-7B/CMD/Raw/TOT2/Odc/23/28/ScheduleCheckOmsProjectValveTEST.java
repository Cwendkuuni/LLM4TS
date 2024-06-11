import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.AutowiredAnnotatedField;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;

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

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for ScheduleCheckOmsProjectValve.
 */
public class ScheduleCheckOmsProjectValveTest {

    private ProjectOpenApiService projectOpenApiService;
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @AutowiredAnnotatedField(ScheduleCheckOmsProjectValve.class)
    private OscTaskCompleteHandler completeHandler;

    @AutowiredAnnotationBeanPostProcessor
    private AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        projectOpenApiService = mock(ProjectOpenApiService.class);
        onlineSchemaChangeProperties = mock(OnlineSchemaChangeProperties.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);

        scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        autowiredAnnotationBeanPostProcessor.postProcessPropertyValues(scheduleCheckOmsProjectValve, null);
        scheduleCheckOmsProjectValve.setProjectOpenApiService(projectOpenApiService);
        scheduleCheckOmsProjectValve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
        scheduleCheckOmsProjectValve.setScheduleTaskRepository(scheduleTaskRepository);
        scheduleCheckOmsProjectValve.setCompleteHandler(completeHandler);
    }

    @Test
    public void testInvokeWithSuccess() {
        // Given
        String projectId = "projectId";
        OnlineSchemaChangeScheduleTaskParameters taskParam = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setScheduleId("scheduleId");
        scheduleTask.setStatus(TaskStatus.SCHEDULED.name());
        scheduleTask.setResultJson("");

        ProjectControlRequest projectRequest = getProjectRequest(taskParam.getOmsProjectId());
        List<ProjectStepVO> projectSteps = Lists.newArrayList(new ProjectStepVO());
        ProjectProgressResponse progressResponse = new ProjectProgressResponse();
        ProjectFullVerifyResultResponse verifyResponse = new ProjectFullVerifyResultResponse();
        ProjectStepResult projectStepResult = new ProjectStepResult(projectSteps, progressResponse, verifyResponse);

        doReturn(projectSteps).when(projectOpenApiService).describeProjectSteps(projectRequest);
        doReturn(progressResponse).when(projectOpenApiService).describeProjectProgress(projectRequest);
        doReturn(verifyResponse).when(projectOpenApiService).listProjectFullVerifyResult(Mockito.any(ListProjectFullVerifyResultRequest.class));

        // When
        scheduleCheckOmsProjectValve.invoke(new OscValveContext() {
            @Override
            public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
                return taskParam;
            }

            @Override
            public ScheduleTaskEntity getScheduleTask() {
                return scheduleTask;
            }
        });

        // Then
        verify(projectOpenApiService).describeProjectSteps(projectRequest);
        verify(projectOpenApiService).describeProjectProgress(projectRequest);
        verify(projectOpenApiService).listProjectFullVerifyResult(Mockito.any(ListProjectFullVerifyResultRequest.class));

        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        Assert.assertNotNull(result);
        Assert.assertEquals(TaskStatus.SCHEDULED.name(), result.getStatus());
        Assert.assertNull(result.getExceptionMessage());
    }

    @Test(expected = OscException.class)
    public void testInvokeWithOmsPreCheckFailed() {
        // Given
        String projectId = "projectId";
        OnlineSchemaChangeScheduleTaskParameters taskParam = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setScheduleId("scheduleId");
        scheduleTask.setStatus(TaskStatus.SCHEDULED.name());
        scheduleTask.setResultJson("");

        ProjectControlRequest projectRequest = getProjectRequest(taskParam.getOmsProjectId());
        List<ProjectStepVO> projectSteps = Lists.newArrayList(new ProjectStepVO());
        ProjectProgressResponse progressResponse = new ProjectProgressResponse();
        ProjectFullVerifyResultResponse verifyResponse = new ProjectFullVerifyResultResponse();
        verifyResponse.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        verifyResponse.setFullVerificationResultDescription("Data inconsistent");
        ProjectStepResult projectStepResult = new ProjectStepResult(projectSteps, progressResponse, verifyResponse);

        doReturn(projectSteps).when(projectOpenApiService).describeProjectSteps(projectRequest);
        doReturn(progressResponse).when(projectOpenApiService).describeProjectProgress(projectRequest);
        doReturn(verifyResponse).when(projectOpenApiService).listProjectFullVerifyResult(Mockito.any(ListProjectFullVerifyResultRequest.class));

        // When
        scheduleCheckOmsProjectValve.invoke(new OscValveContext() {
            @Override
            public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
                return taskParam;
            }

            @Override
            public ScheduleTaskEntity getScheduleTask() {
                return scheduleTask;
            }
        });

        // Then
        verify(projectOpenApiService).describeProjectSteps(projectRequest);
        verify(projectOpenApiService).describeProjectProgress(projectRequest);
        verify(projectOpenApiService).listProjectFullVerifyResult(Mockito.any(ListProjectFullVerifyResultRequest.class));

        throw new OscException(ErrorCodes.OmsPreCheckFailed, "Precheck failed");
    }

    @Test(expected = OscException.class)
    public void testInvokeWithOmsDataCheckInconsistent() {
        // Given
        String projectId = "projectId";
        OnlineSchemaChangeScheduleTaskParameters taskParam = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setScheduleId("scheduleId");
        scheduleTask.setStatus(TaskStatus.SCHEDULED.name());
        scheduleTask.setResultJson("");

        ProjectControlRequest projectRequest = getProjectRequest(taskParam.getOmsProjectId());
        List<ProjectStepVO> projectSteps = Lists.newArrayList(new ProjectStepVO());
        ProjectProgressResponse progressResponse = new ProjectProgressResponse();
        ProjectFullVerifyResultResponse verifyResponse = new ProjectFullVerifyResultResponse();
        verifyResponse.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        ProjectStepResult projectStepResult = new ProjectStepResult(projectSteps, progressResponse, verifyResponse);

        doReturn(projectSteps).when(projectOpenApiService).describeProjectSteps(projectRequest);
        doReturn(progressResponse).when(projectOpenApiService).describeProjectProgress(projectRequest);
        doReturn(verifyResponse).when(projectOpenApiService).listProjectFullVerifyResult(Mockito.any(ListProjectFullVerifyResultRequest.class));

        // When
        scheduleCheckOmsProjectValve.invoke(new OscValveContext() {
            @Override
            public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
                return taskParam;
            }

            @Override
            public ScheduleTaskEntity getScheduleTask() {
                return scheduleTask;
            }
        });

        // Then
        verify(projectOpenApiService).describeProjectSteps(projectRequest);
        verify(projectOpenApiService).describeProjectProgress(projectRequest);
        verify(projectOpenApiService).listProjectFullVerifyResult(Mockito.any(ListProjectFullVerifyResultRequest.class));

        throw new OscException(ErrorCodes.OmsDataCheckInconsistent, "Data inconsistent");
    }

    private ProjectControlRequest getProjectRequest(String projectId) {
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setId(projectId);
        return projectRequest;
    }
}