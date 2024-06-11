package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

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

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScheduleCheckOmsProjectValveTest {

    private ProjectOpenApiService projectOpenApiService;
    private ScheduleTaskRepository scheduleTaskRepository;
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    private OscTaskCompleteHandler completeHandler;
    private ScheduleCheckOmsProjectValve valve;

    @Before
    public void setUp() {
        projectOpenApiService = mock(ProjectOpenApiService.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        onlineSchemaChangeProperties = mock(OnlineSchemaChangeProperties.class);
        completeHandler = mock(OscTaskCompleteHandler.class);

        valve = new ScheduleCheckOmsProjectValve() {
            @Override
            public void invoke(ValveContext valveContext) {
                // no-op
            }
        };
        valve.setProjectOpenApiService(projectOpenApiService);
        valve.setScheduleTaskRepository(scheduleTaskRepository);
        valve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
        valve.setOscTaskCompleteHandler(completeHandler);
    }

    @Test
    public void testInvokeSuccess() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setStatus(TaskStatus.SCHEDULED.name());
        scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        ProjectStepVO stepVO = getStepVO();
        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(Lists.newArrayList(stepVO));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        ProjectStepResult projectStepResult = new ProjectStepResultChecker(progress, Lists.newArrayList(stepVO),
                onlineSchemaChangeProperties.isEnableFullVerify())
                .withCheckerVerifyResult(() -> getProjectFullVerifyResultResponse())
                .withResumeProject(() -> {
                    // no-op
                }).getCheckerResult();

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        adaptResult(result, projectStepResult);

        when(scheduleTaskRepository.update(any(ScheduleTaskEntity.class)))
                .thenReturn(scheduleTask);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.ofNullable(scheduleTask));

        // When
        valve.invoke(new OscValveContext() {
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
        assertEquals(TaskStatus.IN_PROGRESS.name(), scheduleTask.getStatus());
        assertTrue(scheduleTask.isResultJsonSet());
        assertEquals(1, JsonUtils.fromJson(scheduleTask.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class).getFullTransferEstimatedCount());
        assertFalse(scheduleTask.getManualSwapTableEnabled());
        assertFalse(scheduleTask.getManualSwapTableStarted());
    }

    @Test(expected = OscException.class)
    public void testInvokeProjectStepFailure() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setStatus(TaskStatus.SCHEDULED.name());
        scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenThrow(new RuntimeException());

        // When
        valve.invoke(new OscValveContext() {
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
        assertEquals(TaskStatus.FAILED.name(), scheduleTask.getStatus());
        assertTrue(scheduleTask.isResultJsonSet());
    }

    private ProjectControlRequest getProjectRequest(OnlineSchemaChangeScheduleTaskParameters taskParam) {
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid(taskParam.getUid());
        projectRequest.setId(taskParam.getOmsProjectId());
        return projectRequest;
    }

    private ProjectStepVO getStepVO() {
        ProjectStepVO projectStepVO = new ProjectStepVO();
        projectStepVO.setName("PreCheck");
        projectStepVO.setStatus(TaskStatus.DONE.name());
        projectStepVO.setCurrentProgressPercentage(100);
        return projectStepVO;
    }

    private ProjectFullVerifyResultResponse getProjectFullVerifyResultResponse() {
        ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();
        response.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        response.setFullVerificationResultDescription("Test data is consistent");
        return response;
    }

    private void adaptResult(OnlineSchemaChangeScheduleTaskResult result, ProjectStepResult projectStepResult) {
        // no-op
    }
}