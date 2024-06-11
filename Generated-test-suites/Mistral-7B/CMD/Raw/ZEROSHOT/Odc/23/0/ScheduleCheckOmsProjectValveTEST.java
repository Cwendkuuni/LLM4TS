package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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
import com.oceanbase.odc.service.onlineschemachange.testutils.ValveContextBuilder;

public class ScheduleCheckOmsProjectValveTest {

    private static final String OMS_PROJECT_ID = "omsProjectId";
    private static final String DATABASE_NAME = "databaseName";
    private static final String SCHEDULE_TASK_ID = "scheduleTaskId";
    private static final String UID = "uid";

    private ProjectOpenApiService projectOpenApiService;
    private OscTaskCompleteHandler completeHandler;
    private ScheduleTaskRepository scheduleTaskRepository;
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    private DefaultTableFactory tableFactory;

    private ProjectOpenApiService projectOpenApiServiceSpy;
    private ScheduleTaskEntity scheduleTaskEntity;
    private OnlineSchemaChangeScheduleTaskParameters taskParameters;
    private OnlineSchemaChangeScheduleTaskResult taskResult;

    @Before
    public void setUp() {
        projectOpenApiService = Mockito.mock(ProjectOpenApiService.class);
        completeHandler = Mockito.mock(OscTaskCompleteHandler.class);
        scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        onlineSchemaChangeProperties = Mockito.mock(OnlineSchemaChangeProperties.class);
        tableFactory = Mockito.mock(DefaultTableFactory.class);

        projectOpenApiServiceSpy = spy(projectOpenApiService);
        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(SCHEDULE_TASK_ID);
        taskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameters.setUid(UID);
        taskParameters.setOmsProjectId(OMS_PROJECT_ID);
        taskParameters.setDatabaseName(DATABASE_NAME);
        taskParameters.setSwapTableType(SwapTableType.AUTO);
        taskResult = new OnlineSchemaChangeScheduleTaskResult(taskParameters);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(projectOpenApiServiceSpy);
        verifyNoMoreInteractions(completeHandler);
        verifyNoMoreInteractions(scheduleTaskRepository);
    }

    @Test
    public void testInvoke_success() throws Exception {
        // Given
        String expectedFullVerificationResultDescription = "Full verification result description";
        String expectedCurrentStep = OmsStepName.FULL_VERIFIER.name();
        FullVerificationResult expectedFullVerificationResult = FullVerificationResult.CONSISTENT;

        ProjectStepVO stepVO1 = new ProjectStepVO();
        stepVO1.setName(OmsStepName.PRE_CHECK.name());
        stepVO1.setStatus(TaskStatus.DONE.name());
        ProjectStepVO stepVO2 = new ProjectStepVO();
        stepVO2.setName(OmsStepName.FULL_TRANSFER.name());
        stepVO2.setStatus(TaskStatus.DONE.name());
        ProjectStepVO stepVO3 = new ProjectStepVO();
        stepVO3.setName(OmsStepName.FULL_VERIFIER.name());
        stepVO3.setStatus(TaskStatus.RUNNING.name());
        when(projectOpenApiServiceSpy.describeProjectSteps(any()))
                .thenReturn(Lists.newArrayList(stepVO1, stepVO2, stepVO3));

        ProjectProgressResponse progressResponse = new ProjectProgressResponse();
        progressResponse.setStatus(TaskStatus.RUNNING.name());
        when(projectOpenApiServiceSpy.describeProjectProgress(any()))
                .thenReturn(progressResponse);

        when(projectOpenApiServiceSpy.resumeProject(any()))
                .thenReturn(null);

        when(projectOpenApiServiceSpy.listProjectFullVerifyResult(any()))
                .thenReturn(new ProjectFullVerifyResultResponse());

        when(scheduleTaskRepository.update(any()))
                .thenReturn(scheduleTaskEntity);

        // When
        ValveContext valveContext = ValveContextBuilder.create().withScheduleTask(scheduleTaskEntity)
                .withTaskParameter(taskParameters).build();
        doCallRealMethod().when(new ScheduleCheckOmsProjectValve())
                .invoke(valveContext);

        // Then
        verify(projectOpenApiServiceSpy).describeProjectSteps(eq(getProjectControlRequest()));
        verify(projectOpenApiServiceSpy).describeProjectProgress(eq(getProjectControlRequest()));
        verify(projectOpenApiServiceSpy, times(2)).resumeProject(eq(getProjectControlRequest()));
        verify(projectOpenApiServiceSpy).listProjectFullVerifyResult(eq(getListProjectFullVerifyResultRequest()));
        verify(scheduleTaskRepository).update(eq(scheduleTaskEntity));

        assertEquals(expectedFullVerificationResultDescription, taskResult.getFullVerificationResultDescription());
        assertEquals(expectedCurrentStep, taskResult.getCurrentStep());
        assertEquals(expectedFullVerificationResult, taskResult.getFullVerificationResult());

        ProjectStepResult projectStepResult = new ProjectStepResultChecker(progressResponse, Lists.newArrayList(stepVO1,
                stepVO2, stepVO3), true)
                .withCheckerVerifyResult(() -> new ProjectFullVerifyResultResponse())
                .withResumeProject(() -> null)
                .getCheckerResult();

        assertEquals(TaskStatus.RUNNING.name(), projectStepResult.getCurrentStepStatus());
        assertEquals(expectedCurrentStep, projectStepResult.getCurrentStep());
        assertEquals(expectedFullVerificationResult, projectStepResult.getFullVerificationResult());

        assertEquals(FullVerificationResult.CONSISTENT, projectStepResult.getFullVerificationResult());
        assertEquals(TaskStatus.RUNNING.name(), projectStepResult.getCurrentStepStatus());
        assertEquals(OmsStepName.FULL_VERIFIER.name(), projectStepResult.getCurrentStep());
        assertEquals(expectedFullVerificationResultDescription, projectStepResult.getFullVerificationResultDescription());

        when(projectOpenApiServiceSpy.listProjectFullVerifyResult(any()))
                .thenThrow(new OscException(ErrorCodes.OmsExceptionCode.OmsPreCheckFailed,
                        "Precheck failed error message"));

        // When
        doCallRealMethod().when(new ScheduleCheckOmsProjectValve())
                .invoke(valveContext);

        // Then
        verify(projectOpenApiServiceSpy).describeProjectSteps(eq(getProjectControlRequest()));
        verify(projectOpenApiServiceSpy).describeProjectProgress(eq(getProjectControlRequest()));
        verify(projectOpenApiServiceSpy, atLeast(3)).resumeProject(eq(getProjectControlRequest()));
        verify(projectOpenApiServiceSpy, never()).listProjectFullVerifyResult(eq(getListProjectFullVerifyResultRequest()));
        verify(scheduleTaskRepository, never()).update(eq(scheduleTaskEntity));

        assertEquals(expectedCurrentStep, taskResult.getCurrentStep());
        assertEquals(FullVerificationResult.UNCHECK, taskResult.getFullVerificationResult());

        projectStepResult = new ProjectStepResultChecker(progressResponse, Lists.newArrayList(stepVO1, stepVO2, stepVO3),
                true)
                .withCheckerVerifyResult(() -> new ProjectFullVerifyResultResponse())
                .withResumeProject(() -> null)
                .getCheckerResult();

        assertEquals(TaskStatus.RUNNING.name(), projectStepResult.getCurrentStepStatus());
        assertEquals(expectedCurrentStep, projectStepResult.getCurrentStep());
        assertEquals(FullVerificationResult.UNCHECK, projectStepResult.getFullVerificationResult());

        assertEquals(FullVerificationResult.UNCHECK, projectStepResult.getFullVerificationResult());
        assertEquals(TaskStatus.RUNNING.name(), projectStepResult.getCurrentStepStatus());
        assertEquals(OmsStepName.FULL_VERIFIER.name(), projectStepResult.getCurrentStep());
    }

    private ProjectControlRequest getProjectControlRequest() {
        return new ProjectControlRequest().setUid(UID).setId(OMS_PROJECT_ID);
    }

    private ListProjectFullVerifyResultRequest getListProjectFullVerifyResultRequest() {
        ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest();
        request.setProjectId(OMS_PROJECT_ID);
        request.setSourceSchemas(new String[] {DATABASE_NAME});
        request.setDestSchemas(new String[] {DATABASE_NAME});
        request.setStatus(new String[] {"FINISHED", "SUSPEND", "RUNNING"});
        request.setPageSize(10);
        request.setPageNumber(1);
        request.setUid(UID);
        return request;
    }
}