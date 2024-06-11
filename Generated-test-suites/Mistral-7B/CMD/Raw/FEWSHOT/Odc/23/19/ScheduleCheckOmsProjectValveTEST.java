package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValveTest.getScheduleTaskEntity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
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
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * Test class for ScheduleCheckOmsProjectValve
 */
@Component
public class ScheduleCheckOmsProjectValveTest {

    @Autowired
    private ProjectOpenApiService projectOpenApiService;
    @Autowired
    private OscTaskCompleteHandler completeHandler;
    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;

    @Autowired
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @Before
    public void setup() {
        projectOpenApiService = mock(ProjectOpenApiService.class);
        completeHandler = mock(OscTaskCompleteHandler.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        onlineSchemaChangeProperties = mock(OnlineSchemaChangeProperties.class);
    }

    @Test
    public void testInvoke() throws Exception {
        // given
        ScheduleTaskEntity scheduleTask = getScheduleTaskEntity();
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(new ProjectStepVO());
        ProjectProgressResponse progress = new ProjectProgressResponse();
        ProjectStepResult projectStepResult = new ProjectStepResultChecker(progress, projectSteps,
                onlineSchemaChangeProperties.isEnableFullVerify())
                        .withCheckerVerifyResult(() -> listProjectFullVerifyResult(taskParameter.getOmsProjectId(),
                                taskParameter.getDatabaseName(), taskParameter.getUid()))
                        .withResumeProject(() -> {
                            projectOpenApiService.resumeProject(projectRequest);
                            return null;
                        })
                        .getCheckerResult();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();

        // when
        valve.invoke(new ValveContext() {
            @Override
            public ScheduleTaskEntity getScheduleTask() {
                return scheduleTask;
            }

            @Override
            public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
                return taskParameter;
            }
        });

        // then
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(result.getFullVerificationResult(), projectStepResult.getFullVerificationResult());
        assertEquals(result.getFullVerificationResultDescription(), projectStepResult.getFullVerificationResultDescription());
        assertEquals(result.getFullTransferProgressPercentage(), projectStepResult.getFullTransferProgressPercentage());
        assertEquals(result.getFullTransferEstimatedCount(), projectStepResult.getFullTransferEstimatedCount());
        assertEquals(result.getFullTransferFinishedCount(), projectStepResult.getFullTransferFinishedCount());
    }

    private ProjectControlRequest getProjectRequest(OnlineSchemaChangeScheduleTaskParameters taskParam) {
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid(taskParam.getUid());
        projectRequest.setId(taskParam.getOmsProjectId());
        return projectRequest;
    }

    private ScheduleTaskEntity getScheduleTaskEntity() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(UUID.randomUUID().toString());
        scheduleTask.setStatus("SCHEDULED");
        scheduleTask.setResultJson("{}");
        return scheduleTask;
    }

    private ProjectFullVerifyResultResponse listProjectFullVerifyResult(String projectId, String databaseName,
            String uid) {
        ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();
        response.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        return response;
    }
}