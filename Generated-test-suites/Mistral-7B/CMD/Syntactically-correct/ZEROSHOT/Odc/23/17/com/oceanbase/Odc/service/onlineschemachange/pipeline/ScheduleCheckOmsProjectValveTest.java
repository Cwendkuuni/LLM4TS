package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.UUID;

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
import com.oceanbase.odc.service.onlineschemachange.oms.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ListProjectFullVerifyResultRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;

@Slf4j
public class ScheduleCheckOmsProjectValveTest {

    @Mock
    private ProjectOpenApiService projectOpenApiService;
    @Mock
    private OscTaskCompleteHandler completeHandler;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    @Mock
    private DefaultTableFactory defaultTableFactory;

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    private ScheduleTaskEntity testScheduleTask;
    private OnlineSchemaChangeScheduleTaskParameters testTaskParameter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testScheduleTask = new ScheduleTaskEntity();
        testTaskParameter = new OnlineSchemaChangeScheduleTaskParameters();
    }

    @Test
    public void testInvoke() throws Exception {
        // Arrange
        String projectId = "projectId";
        String databaseName = "databaseName";
        String uid = UUID.randomUUID().toString();
        String taskId = "taskId";
        ProjectStepVO projectStepVO = new ProjectStepVO();
        ProjectProgressResponse progressResponse = new ProjectProgressResponse();
        ProjectFullVerifyResultResponse fullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        List<ProjectStepVO> projectSteps = Lists.newArrayList(projectStepVO);
        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(progressResponse);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(fullVerifyResultResponse);
        when(scheduleTaskRepository.update(any(ScheduleTaskEntity.class)))
                .thenReturn(testScheduleTask);

        testTaskParameter.setOmsProjectId(projectId);
        testTaskParameter.setDatabaseName(databaseName);
        testTaskParameter.setUid(uid);

        // Act
        scheduleCheckOmsProjectValve.invoke(new OscValveContext() {
            @Override
            public ScheduleTaskEntity getScheduleTask() {
                return testScheduleTask;
            }

            @Override
            public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
                return testTaskParameter;
            }
        });

        // Assert
        verify(projectOpenApiService, times(1)).describeProjectSteps(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).describeProjectProgress(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
        verify(scheduleTaskRepository, times(1)).update(any(ScheduleTaskEntity.class));
    }

    @Test(expected = OscException.class)
    public void testInvokeWithError() throws Exception {
        // Arrange
        String projectId = "projectId";
        String databaseName = "databaseName";
        String uid = UUID.randomUUID().toString();
        String taskId = "taskId";
        ProjectStepVO projectStepVO = new ProjectStepVO();
        ProjectProgressResponse progressResponse = new ProjectProgressResponse();
        ProjectFullVerifyResultResponse fullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        List<ProjectStepVO> projectSteps = Lists.newArrayList(projectStepVO);
        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenThrow(new Exception("Error occurred"));

        testTaskParameter.setOmsProjectId(projectId);
        testTaskParameter.setDatabaseName(databaseName);
        testTaskParameter.setUid(uid);

        // Act & Assert
        scheduleCheckOmsProjectValve.invoke(new OscValveContext() {
            @Override
            public ScheduleTaskEntity getScheduleTask() {
                return testScheduleTask;
            }

            @Override
            public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
                return testTaskParameter;
            }
        });
    }

    @Test
    public void testHandleOmsProjectStepResult() throws Exception {
        // Arrange
        String projectId = "projectId";
        String databaseName = "databaseName";
        String uid = UUID.randomUUID().toString();
        SwapTableType swapTableType = SwapTableType.DEFAULT;
        OnlineSchemaChangeScheduleTaskResult testResult = new OnlineSchemaChangeScheduleTaskResult();
        ProjectStepResult projectStepResult = new ProjectStepResult();
        ProjectStepVO projectStepVO = new ProjectStepVO();

        testResult.setFullTransferProgressPercentage(100);
        testResult.setFullVerificationProgressPercentage(100);
        projectStepResult.setFullTransferProgressPercentage(100);
        projectStepResult.setFullVerificationProgressPercentage(100);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(Lists.newArrayList(projectStepVO));
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(new ProjectProgressResponse());

        // Act
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(null, projectStepResult, testResult, swapTableType,
                testScheduleTask);

        // Assert
        assertEquals(true, testResult.isManualSwapTableEnabled());
        assertEquals(true, testResult.isManualSwapTableStarted());
        verify(projectOpenApiService, times(1)).resumeProject(any(ProjectControlRequest.class));
    }

    @Test
    public void testContinueHandleProjectStepResult() throws Exception {
        // Arrange
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);

        // Act & Assert
        scheduleCheckOmsProjectValve.continueHandleProjectStepResult(projectStepResult);
        fail("Expected OscException to be thrown");
    }

    @Test
    public void testListProjectFullVerifyResult() throws Exception {
        // Arrange
        String projectId = "projectId";
        String databaseName = "databaseName";
        String uid = UUID.randomUUID().toString();
        ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest();
        ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();

        when(projectOpenApiService.listProjectFullVerifyResult(request))
                .thenReturn(response);

        // Act
        List<OnlineSchemaChangeScheduleTaskResult> results = scheduleCheckOmsProjectValve
                .listProjectFullVerifyResult(projectId, databaseName, uid);

        // Assert
        assertEquals(0, results.size());
    }
}