package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

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

@Slf4j
public class ScheduleCheckOmsProjectValveTest {

    private static final String OMS_PROJECT_ID = "1";
    private static final String DATABASE_NAME = "testdb";
    private static final String UID = "testuid";
    private static final ProjectOpenApiService projectOpenApiService = mock(ProjectOpenApiService.class);
    private static final ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
    private static final OnlineSchemaChangeProperties onlineSchemaChangeProperties = mock(OnlineSchemaChangeProperties.class);
    private static final SwapTableUtil swapTableUtil = mock(SwapTableUtil.class);
    private static final OscTaskCompleteHandler completeHandler = mock(OscTaskCompleteHandler.class);
    private static final ProjectStepResultChecker projectStepResultChecker = new ProjectStepResultChecker();
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Before
    public void setup() {
        scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        scheduleCheckOmsProjectValve.setProjectOpenApiService(projectOpenApiService);
        scheduleCheckOmsProjectValve.setScheduleTaskRepository(scheduleTaskRepository);
        scheduleCheckOmsProjectValve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
        scheduleCheckOmsProjectValve.setSwapTableUtil(swapTableUtil);
        scheduleCheckOmsProjectValve.setCompleteHandler(completeHandler);
    }

    @Test
    public void testInvoke() {
        // Given
        final OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        final ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();

        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(Lists.newArrayList(new ProjectStepVO()));

        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(new ProjectProgressResponse());

        when(listProjectFullVerifyResult(OMS_PROJECT_ID, DATABASE_NAME, UID)).thenReturn(
                new ProjectFullVerifyResultResponse());

        when(scheduleTaskRepository.update(any(ScheduleTaskEntity.class))).thenReturn(scheduleTask);

        when(swapTableUtil.isSwapTableEnable(any(SwapTableType.class), anyInt(), anyInt(),
                eq(FullVerificationResult.CONSISTENT), anyBoolean())).thenReturn(true);

        when(completeHandler.handle(any(OscValveContext.class), any(ProjectStepResult.class), any(
                OnlineSchemaChangeScheduleTaskResult.class), anyString(), any(ScheduleTaskEntity.class)))
                .thenReturn(null);

        when(onlineSchemaChangeProperties.isEnableFullVerify()).thenReturn(true);

        // When
        scheduleCheckOmsProjectValve.invoke(new ValveContext() {
            @Override
            public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
                return taskParameter;
            }

            @Override
            public ScheduleTaskEntity getScheduleTask() {
                return scheduleTask;
            }
        });

        // Then
        verify(scheduleTaskRepository).update(scheduleTask);
    }

    @Test
    public void testHandleOmsProjectStepResultWithConsistentData() {
        // Given
        final OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        final ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        final ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(Lists.newArrayList(new ProjectStepVO()));

        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(new ProjectProgressResponse());

        when(listProjectFullVerifyResult(OMS_PROJECT_ID, DATABASE_NAME, UID)).thenReturn(
                new ProjectFullVerifyResultResponse());

        when(scheduleTaskRepository.update(any(ScheduleTaskEntity.class))).thenReturn(scheduleTask);

        when(swapTableUtil.isSwapTableEnable(any(SwapTableType.class), anyInt(), anyInt(),
                eq(FullVerificationResult.CONSISTENT), anyBoolean())).thenReturn(true);

        when(completeHandler.handle(any(OscValveContext.class), any(ProjectStepResult.class), any(
                OnlineSchemaChangeScheduleTaskResult.class), anyString(), any(ScheduleTaskEntity.class)))
                .thenReturn(null);

        when(onlineSchemaChangeProperties.isEnableFullVerify()).thenReturn(true);

        // When
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(
                new ValveContext() {
                    @Override
                    public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
                        return taskParameter;
                    }

                    @Override
                    public ScheduleTaskEntity getScheduleTask() {
                        return scheduleTask;
                    }
                }, projectStepResult, new OnlineSchemaChangeScheduleTaskResult(), SwapTableType.MYSQL, scheduleTask);

        // Then
        verify(scheduleTaskRepository).update(scheduleTask);
    }

    @Test
    public void testHandleOmsProjectStepResultWithInconsistentData() {
        // Given
        final OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        final ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        final ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);

        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(Lists.newArrayList(new ProjectStepVO()));

        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(new ProjectProgressResponse());

        when(listProjectFullVerifyResult(OMS_PROJECT_ID, DATABASE_NAME, UID)).thenReturn(
                new ProjectFullVerifyResultResponse());

        when(scheduleTaskRepository.update(any(ScheduleTaskEntity.class))).thenReturn(scheduleTask);

        // When
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(
                new ValveContext() {
                    @Override
                    public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
                        return taskParameter;
                    }

                    @Override
                    public ScheduleTaskEntity getScheduleTask() {
                        return scheduleTask;
                    }
                }, projectStepResult, new OnlineSchemaChangeScheduleTaskResult(), SwapTableType.MYSQL, scheduleTask);

        // Then
        verify(projectOpenApiService).resumeProject(any(ProjectControlRequest.class));
    }

    @Test(expected = OscException.class)
    public void testHandleOmsProjectStepResultWithPrecheckFailed() {
        // Given
        final OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        final ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        final ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);

        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(Lists.newArrayList(new ProjectStepVO()));

        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(new ProjectProgressResponse());

        when(listProjectFullVerifyResult(OMS_PROJECT_ID, DATABASE_NAME, UID)).thenReturn(
                new ProjectFullVerifyResultResponse());

        when(scheduleTaskRepository.update(any(ScheduleTaskEntity.class))).thenReturn(scheduleTask);

        // When
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(
                new ValveContext() {
                    @Override
                    public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
                        return taskParameter;
                    }

                    @Override
                    public ScheduleTaskEntity getScheduleTask() {
                        return scheduleTask;
                    }
                }, projectStepResult, new OnlineSchemaChangeScheduleTaskResult(), SwapTableType.MYSQL, scheduleTask);

        // Then
        // OscException should be thrown
    }

    private ProjectFullVerifyResultResponse listProjectFullVerifyResult(String projectId, String databaseName,
            String uid) {
        ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest();
        request.setProjectId(projectId);
        request.setSourceSchemas(new String[] {databaseName});
        request.setDestSchemas(new String[] {databaseName});
        request.setStatus(new String[] {"FINISHED", "SUSPEND", "RUNNING"});
        request.setPageSize(10);
        request.setPageNumber(1);
        request.setUid(uid);

        ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();
        response.setCode(0);
        response.setMessage("success");

        return response;
    }
}