package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.AutowiredAnnotatedFieldUtils;
import org.springframework.stereotype.Component;

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

@Component
public class ScheduleCheckOmsProjectValveTest {

    private ScheduleCheckOmsProjectValve valveUnderTest;
    private ProjectOpenApiService projectOpenApiServiceMock;
    private ScheduleTaskRepository scheduleTaskRepositoryMock;
    private OnlineSchemaChangeProperties onlineSchemaChangePropertiesMock;
    private OscTaskCompleteHandler completeHandlerMock;
    private DefaultTableFactory tableFactoryMock;

    @Before
    public void setup() {
        projectOpenApiServiceMock = mock(ProjectOpenApiService.class);
        scheduleTaskRepositoryMock = mock(ScheduleTaskRepository.class);
        onlineSchemaChangePropertiesMock = mock(OnlineSchemaChangeProperties.class);
        completeHandlerMock = mock(OscTaskCompleteHandler.class);
        tableFactoryMock = mock(DefaultTableFactory.class);

        AutowedScheduleCheckOmsProjectValve valve = new AutowedScheduleCheckOmsProjectValve();
        AutowedFieldUtils.inject(valve, projectOpenApiServiceMock, scheduleTaskRepositoryMock,
                onlineSchemaChangePropertiesMock, completeHandlerMock, tableFactoryMock);
        valveUnderTest = valve;
    }

    @Test
    public void testInvoke_noError() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();

        ProjectControlRequest projectRequest = new ProjectControlRequest();
        ProjectStepVO[] projectSteps = new ProjectStepVO[0];
        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setProgressPercentage(50);

        when(projectOpenApiServiceMock.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiServiceMock.describeProjectProgress(projectRequest)).thenReturn(progress);

        ProjectStepResult projectStepResult = new ProjectStepResult(projectSteps, progress,
                FullVerificationResult.CONSISTENT);

        when(projectOpenApiServiceMock.listProjectFullVerifyResult(any()))
                .thenReturn(new ProjectFullVerifyResultResponse());

        // When
        valveUnderTest.invoke(null);

        // Then
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(
                valveUnderTest.getScheduleTask().getResultJson(), OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(TaskStatus.DONE, result.getStatus());
        assertEquals(50, result.getProgressPercentage());
    }

    @Test(expected = OscException.class)
    public void testInvoke_preCheckFailed() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();

        ProjectControlRequest projectRequest = new ProjectControlRequest();
        ProjectStepVO[] projectSteps = new ProjectStepVO[0];
        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setProgressPercentage(50);

        when(projectOpenApiServiceMock.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiServiceMock.describeProjectProgress(projectRequest)).thenReturn(progress);

        ProjectStepResult projectStepResult = new ProjectStepResult(projectSteps, progress, PrecheckResult.FAILED);

        when(projectOpenApiServiceMock.listProjectFullVerifyResult(any()))
                .thenReturn(new ProjectFullVerifyResultResponse());

        // When & Then
        valveUnderTest.invoke(null);
        throw new OscException(ErrorCodes.OmsPreCheckFailed, "Precheck failed");
    }

    @Test(expected = OscException.class)
    public void testInvoke_dataInconsistent() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();

        ProjectControlRequest projectRequest = new ProjectControlRequest();
        ProjectStepVO[] projectSteps = new ProjectStepVO[0];
        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setProgressPercentage(50);

        when(projectOpenApiServiceMock.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiServiceMock.describeProjectProgress(projectRequest)).thenReturn(progress);

        ProjectStepResult projectStepResult = new ProjectStepResult(projectSteps, progress,
                FullVerificationResult.INCONSISTENT);

        when(projectOpenApiServiceMock.listProjectFullVerifyResult(any()))
                .thenReturn(new ProjectFullVerifyResultResponse());

        // When & Then
        valveUnderTest.invoke(null);
        throw new OscException(ErrorCodes.OmsDataCheckInconsistent,
                "Task failed for origin table has inconsistent data with new table");
    }

    @Test
    public void testGetProjectRequest() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = valveUnderTest.getProjectRequest(taskParameter);
        assertEquals(taskParameter.getOmsProjectId(), projectRequest.getId());
        assertEquals(taskParameter.getUid(), projectRequest.getUid());
    }

    @Test
    public void testListProjectFullVerifyResult() throws Exception {
        String projectId = "projectId";
        String databaseName = "databaseName";
        String uid = "uid";

        ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest();
        request.setProjectId(projectId);
        request.setSourceSchemas(new String[] {databaseName});
        request.setDestSchemas(new String[] {databaseName});
        request.setStatus(new String[] {"FINISHED", "SUSPEND", "RUNNING"});
        request.setPageSize(10);
        request.setPageNumber(1);
        request.setUid(uid);

        ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();
        when(projectOpenApiServiceMock.listProjectFullVerifyResult(request)).thenReturn(response);

        List<FullVerificationResult> result = valveUnderTest.listProjectFullVerifyResult(projectId, databaseName, uid);
        assertEquals(0, result.size());
    }
}