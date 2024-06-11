package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.AutowiredAnnotatedField;

import com.google.common.collect.Lists;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScheduleCheckOmsProjectValveTest {

    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;
    private ProjectOpenApiService projectOpenApiService;
    private ScheduleTaskRepository scheduleTaskRepository;
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    private OscTaskCompleteHandler completeHandler;

    @AutowiredAnnotatedField("projectOpenApiService")
    private void setProjectOpenApiService(ProjectOpenApiService projectOpenApiService) {
        this.projectOpenApiService = projectOpenApiService;
    }

    @AutowiredAnnotatedField("scheduleTaskRepository")
    private void setScheduleTaskRepository(ScheduleTaskRepository scheduleTaskRepository) {
        this.scheduleTaskRepository = scheduleTaskRepository;
    }

    @AutowiredAnnotatedField("onlineSchemaChangeProperties")
    private void setOnlineSchemaChangeProperties(OnlineSchemaChangeProperties onlineSchemaChangeProperties) {
        this.onlineSchemaChangeProperties = onlineSchemaChangeProperties;
    }

    @AutowiredAnnotatedField("completeHandler")
    private void setCompleteHandler(OscTaskCompleteHandler completeHandler) {
        this.completeHandler = completeHandler;
    }

    @Before
    public void setup() {
        scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        scheduleCheckOmsProjectValve.projectOpenApiService = mock(ProjectOpenApiService.class);
        scheduleCheckOmsProjectValve.scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        scheduleCheckOmsProjectValve.onlineSchemaChangeProperties = mock(OnlineSchemaChangeProperties.class);
        scheduleCheckOmsProjectValve.completeHandler = mock(OscTaskCompleteHandler.class);
    }

    @Test
    public void testInvoke_shouldReturnSuccess() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();

        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid("testUid");
        projectRequest.setId("testProjectId");

        ProjectStepVO stepVO = new ProjectStepVO();
        stepVO.setCurrentStep("PRE_CHECK");
        stepVO.setCurrentStepStatus("RUNNING");
        stepVO.setFullTransferEstimatedCount(10);
        stepVO.setFullTransferFinishedCount(5);
        stepVO.setFullTransferProgressPercentage(50);
        stepVO.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        stepVO.setFullVerificationResultDescription("Verify success");
        stepVO.setFullVerificationProgressPercentage(50);
        stepVO.setPreCheckResult(PrecheckResult.PASSED);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(stepVO));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(
                new ProjectProgressResponse());

        when(scheduleTaskRepository.update(any(ScheduleTaskEntity.class)))
                .thenReturn(new ScheduleTaskEntity());

        scheduleCheckOmsProjectValve.invoke(getValveContext(taskParameter));

        ArgumentCaptor<OnlineSchemaChangeScheduleTaskResult> resultArgumentCaptor = ArgumentCaptor.forClass(OnlineSchemaChangeScheduleTaskResult.class);
        verify(scheduleTaskRepository).update(eq(any(ScheduleTaskEntity.class))).andReturn(resultArgumentCaptor.capture());
        OnlineSchemaChangeScheduleTaskResult result = resultArgumentCaptor.getValue();
        assertEquals(TaskStatus.RUNNING, result.getStatus());
        assertEquals(SwapTableType.AUTO, result.getSwapTableType());
    }

    @Test
    public void testInvoke_shouldReturnExceptionWhenProjectStepIsFailed() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();

        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid("testUid");
        projectRequest.setId("testProjectId");

        ProjectStepVO stepVO = new ProjectStepVO();
        stepVO.setCurrentStep("FULL_TRANSFER");
        stepVO.setCurrentStepStatus("FAILED");
        stepVO.setFullTransferEstimatedCount(10);
        stepVO.setFullTransferFinishedCount(5);
        stepVO.setFullTransferProgressPercentage(50);
        stepVO.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        stepVO.setFullVerificationResultDescription("Verify success");
        stepVO.setFullVerificationProgressPercentage(50);
        stepVO.setPreCheckResult(PrecheckResult.PASSED);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(stepVO));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(
                new ProjectProgressResponse());

        when(scheduleTaskRepository.update(any(ScheduleTaskEntity.class)))
                .thenThrow(new OscException(ErrorCodes.OmsDataCheckInconsistent, ""));

        try {
            scheduleCheckOmsProjectValve.invoke(getValveContext(taskParameter));
            fail("Method must throw an exception");
        } catch (OscException e) {
            assertEquals(ErrorCodes.OmsDataCheckInconsistent, e.getErrorCode());
        }
    }

    private ValveContext getValveContext(OnlineSchemaChangeScheduleTaskParameters taskParameter) {
        ValveContext valveContext = mock(ValveContext.class);
        when(valveContext.getScheduleTask()).thenReturn(new ScheduleTaskEntity());
        when(valveContext.getTaskParameter()).thenReturn(taskParameter);
        return valveContext;
    }
}