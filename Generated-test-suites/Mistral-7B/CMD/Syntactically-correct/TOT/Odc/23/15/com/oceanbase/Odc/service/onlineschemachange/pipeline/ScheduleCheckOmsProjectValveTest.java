package com.oceanbase.odc.service.onlineschemachange.pipeline;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.exception.OscException;
import com.oceanbase.odc.common.util.tableformat.Table;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscExceptionEnum;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ListProjectFullVerifyResultRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("ScheduleCheckOmsProjectValve Test")
@MockitoJUnitRunner
public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("testInvoke when project steps and progress are successful")
    public void testInvoke_projectStepsAndProgressSuccessful() {
        // Given
        String projectId = "projectId";
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setId(projectId);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(new ProjectStepVO()));
        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setPercentage(100);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        // When
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setResultJson("");
        scheduleCheckOmsProjectValve.invoke(new ValveContext() {
            @Override
            public ScheduleTaskEntity getScheduleTask() {
                return scheduleTask;
            }

            @Override
            public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
                return taskParameter;
            }
        });

        // Then
        verify(scheduleTaskRepository, times(1)).update(any(ScheduleTaskEntity.class));
        verifyNoMoreInteractions(projectOpenApiService);
    }

    @Test
    @DisplayName("testInvoke when project steps have error and progress is successful")
    public void testInvoke_projectStepsHaveErrorAndProgressSuccessful() {
        // Given
        String projectId = "projectId";
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setId(projectId);
        ProjectStepVO projectStepVO = new ProjectStepVO();
        projectStepVO.setCurrentStep("Step1");
        projectStepVO.setCurrentStepStatus("FAILED");
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepVO));
        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setPercentage(100);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        // When
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setResultJson("");
        scheduleCheckOmsProjectValve.invoke(new ValveContext() {
            @Override
            public ScheduleTaskEntity getScheduleTask() {
                return scheduleTask;
            }

            @Override
            public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
                return taskParameter;
            }
        });

        // Then
        Assertions.assertEquals(projectStepVO.getCurrentStepStatus(), scheduleTask.getStatus());
        verify(scheduleTaskRepository, times(1)).update(any(ScheduleTaskEntity.class));
        verifyNoMoreInteractions(projectOpenApiService);
    }

    @Test
    @DisplayName("testInvoke when project steps are successful and progress have error")
    public void testInvoke_projectStepsSuccessfulAndProgressHaveError() {
        // Given
        String projectId = "projectId";
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setId(projectId);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(new ProjectStepVO());
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setPercentage(-1);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenThrow(new OscException(OscExceptionEnum.PROJECT_PROGRESS_FAILED));

        // When
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setResultJson("");
        try {
            scheduleCheckOmsProjectValve.invoke(new ValveContext() {
                @Override
                public ScheduleTaskEntity getScheduleTask() {
                    return scheduleTask;
                }

                @Override
                public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
                    return taskParameter;
                }
            });
            fail("Should throw exception");
        } catch (OscException e) {
            // Then
            verify(scheduleTaskRepository, never()).update(any(ScheduleTaskEntity.class));
            verify(projectOpenApiService, times(2)).describeProjectSteps(projectRequest);
            verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        }
    }

    @Test
    @DisplayName("testInvoke when project steps and progress both have error")
    public void testInvoke_projectStepsAndProgressBothHaveError() {
        // Given
        String projectId = "projectId";
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setId(projectId);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(new ProjectStepVO());
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenThrow(new OscException(OscExceptionEnum.PROJECT_STEP_FAILED));
        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setPercentage(-1);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenThrow(new OscException(OscExceptionEnum.PROJECT_PROGRESS_FAILED));

        // When
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setResultJson("");
        try {
            scheduleCheckOmsProjectValve.invoke(new ValveContext() {
                @Override
                public ScheduleTaskEntity getScheduleTask() {
                    return scheduleTask;
                }

                @Override
                public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
                    return taskParameter;
                }
            });
            fail("Should throw exception");
        } catch (OscException e) {
            // Then
            verify(scheduleTaskRepository, never()).update(any(ScheduleTaskEntity.class));
            verify(projectOpenApiService, times(2)).describeProjectSteps(projectRequest);
            verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        }
    }

    @Test
    @DisplayName("testAdaptResult when project steps and progress both are successful")
    public void testAdaptResult_projectStepsAndProgressBothSuccessful() {
        // Given
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setCurrentStep("Step1");
        projectStepResult.setCurrentStepStatus(TaskStatus.DONE);
        projectStepResult.setFullTransferEstimatedCount(10);
        projectStepResult.setFullTransferFinishedCount(5);
        projectStepResult.setFullTransferProgressPercentage(50);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class))).thenReturn(Lists.newArrayList(projectStepResult));
        ProjectStepVO projectStepVO = new ProjectStepVO();
        projectStepVO.setCurrentStep("Step2");
        projectStepVO.setCurrentStepStatus(TaskStatus.DONE);
        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class))).thenReturn(Lists.newArrayList(projectStepVO));
        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setPercentage(100);

        // When
        ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        OnlineSchemaChangeScheduleTaskResult adaptedResult = scheduleCheckOmsProjectValve.adaptResult(result, null);

        // Then
        Assertions.assertEquals(projectStepResult.getFullTransferEstimatedCount(), adaptedResult.getFullTransferEstimatedCount());
        Assertions.assertEquals(projectStepResult.getFullTransferFinishedCount(), adaptedResult.getFullTransferFinishedCount());
        Assertions.assertEquals(projectStepResult.getFullTransferProgressPercentage(), adaptedResult.getFullTransferProgressPercentage());
        Assertions.assertEquals(projectStepResult.getFullVerificationResult(), adaptedResult.getFullVerificationResult());
        Assertions.assertEquals(projectStepVO.getCurrentStep(), adaptedResult.getCurrentStep());
        Assertions.assertEquals(projectStepVO.getCurrentStepStatus(), adaptedResult.getCurrentStepStatus());
        Assertions.assertNull(adaptedResult.getManualSwapTableEnabled());
        Assertions.assertNull(adaptedResult.getManualSwapTableStarted());
    }

    @Test
    @DisplayName("testAdaptResult when project steps have error and progress is successful")
    public void testAdaptResult_projectStepsHaveErrorAndProgressSuccessful() {
        // Given
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setCurrentStep("Step1");
        projectStepResult.setCurrentStepStatus(TaskStatus.FAILED);
        projectStepResult.setFullTransferEstimatedCount(10);
        projectStepResult.setFullTransferFinishedCount(5);
        projectStepResult.setFullTransferProgressPercentage(50);
        projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        ProjectStepVO projectStepVO = new ProjectStepVO();
        projectStepVO.setCurrentStep("Step2");
        projectStepVO.setCurrentStepStatus(TaskStatus.DONE);
        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setPercentage(100);

        // When
        ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        OnlineSchemaChangeScheduleTaskResult adaptedResult = scheduleCheckOmsProjectValve.adaptResult(result, projectStepVO);

        // Then
        Assertions.assertEquals(projectStepResult.getFullTransferEstimatedCount(), adaptedResult.getFullTransferEstimatedCount());
        Assertions.assertEquals(projectStepResult.getFullTransferFinishedCount(), adaptedResult.getFullTransferFinishedCount());
        Assertions.assertEquals(projectStepResult.getFullTransferProgressPercentage(), adaptedResult.getFullTransferProgressPercentage());
        Assertions.assertEquals(projectStepResult.getFullVerificationResult(), adaptedResult.getFullVerificationResult());
        Assertions.assertEquals(projectStepVO.getCurrentStep(), adaptedResult.getCurrentStep());
        Assertions.assertEquals(projectStepVO.getCurrentStepStatus(), adaptedResult.getCurrentStepStatus());
        Assertions.assertEquals(projectStepResult.getFullVerificationResult(), adaptedResult.getFullVerificationResult());
        Assertions.assertEquals(projectStepResult.getFullVerificationResultDescription(), adaptedResult.getFullVerificationResultDescription());
        Assertions.assertNull(adaptedResult.getManualSwapTableEnabled());
        Assertions.assertNull(adaptedResult.getManualSwapTableStarted());
    }

    @Test
    @DisplayName("testListProjectFullVerifyResult when get project full verify result from projectOpenApiService is successful")
    public void testListProjectFullVerifyResult_projectOpenApiServiceSuccessful() {
        // Given
        String projectId = "projectId";
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class))).thenReturn(projectFullVerifyResultResponse);

        // When
        ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        FullVerificationResult fullVerificationResult = scheduleCheckOmsProjectValve.listProjectFullVerifyResult(projectId, "databaseName", taskParameter.getUid());

        // Then
        Assertions.assertEquals(projectFullVerifyResultResponse.getResult().getFullVerificationResult(), fullVerificationResult);
    }

    @Test
    @DisplayName("testListProjectFullVerifyResult when get project full verify result from projectOpenApiService is failed")
    public void testListProjectFullVerifyResult_projectOpenApiServiceFailed() {
        // Given
        String projectId = "projectId";
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyRequest.class))).thenThrow(new OscException(OscExceptionEnum.PROJECT_FULL_VERIFY_FAILED));

        // When
        ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        FullVerificationResult fullVerificationResult = scheduleCheckOmsProjectValve.listProjectFullVerifyResult(projectId, "databaseName", taskParameter.getUid());

        // Then
        Assertions.assertNull(fullVerificationResult);
    }
}