package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.util.tableformat.Table;
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

/**
 * Test for ScheduleCheckOmsProjectValve class
 */
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

    private OnlineSchemaChangeScheduleTaskParameters taskParameter;
    private ScheduleTaskEntity scheduleTask;
    private ProjectControlRequest projectRequest;
    private ProjectStepResult projectStepResult;
    private ProjectProgressResponse progress;
    private ProjectFullVerifyResultResponse fullVerifyResponse;
    private List<ProjectStepVO> projectSteps;
    private OnlineSchemaChangeScheduleTaskResult result;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        scheduleTask = new ScheduleTaskEntity();
        projectRequest = new ProjectControlRequest();
        projectStepResult = new ProjectStepResult();
        progress = new ProjectProgressResponse();
        fullVerifyResponse = new ProjectFullVerifyResultResponse();
        projectSteps = Lists.newArrayList();
        result = new OnlineSchemaChangeScheduleTaskResult();
    }

    @After
    public void tearDown() {
        reset(projectOpenApiService, completeHandler, scheduleTaskRepository, onlineSchemaChangeProperties,
                defaultTableFactory);
    }

    @Test
    public void testInvokeMethod_success() {
        // given
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        // when
        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        // then
        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
    }

    @Test(expected = OscException.class)
    public void testInvokeMethod_precheckFailed() {
        // given
        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);
        projectStepResult.setErrorMsg("Error message");
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.resumeProject(projectRequest))
                .thenThrow(new OscException(ErrorCodes.OmsPreCheckFailed, "Error message"));

        // when
        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        // then
        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verify(projectOpenApiService, times(1)).resumeProject(projectRequest);
    }

    @Test(expected = OscException.class)
    public void testInvokeMethod_dataInconsistent() {
        // given
        projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        projectStepResult.setFullVerificationResultDescription("Error message");
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        // when
        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        // then
        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
    }

    @Test
    public void testListProjectFullVerifyResult_success() {
        // given
        String projectId = UUID.randomUUID().toString();
        String databaseName = "database_name";
        String uid = UUID.randomUUID().toString();

        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(fullVerifyResponse);

        // when
        OnlineSchemaChangeScheduleTaskParameters taskParam = new OnlineSchemaChangeScheduleTaskParameters();
        taskParam.setOmsProjectId(projectId);
        taskParam.setDatabaseName(databaseName);
        taskParam.setUid(uid);
        ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        List<ProjectFullVerifyResultResponse> fullVerifyResults = scheduleCheckOmsProjectValve
                .listProjectFullVerifyResult(taskParam);

        // then
        verify(projectOpenApiService, times(1))
                .listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
        assertNotNull(fullVerifyResults);
        assertEquals(1, fullVerifyResults.size());
        assertEquals(fullVerifyResponse, fullVerifyResults.get(0));
    }

    @Test
    public void testGetProjectRequest_success() {
        // given
        OnlineSchemaChangeScheduleTaskParameters taskParam = new OnlineSchemaChangeScheduleTaskParameters();
        taskParam.setOmsProjectId("ProjectId");
        taskParam.setUid("Uid");

        // when
        ProjectControlRequest projectRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParam);

        // then
        assertEquals("ProjectId", projectRequest.getId());
        assertEquals("Uid", projectRequest.getUid());
    }
}