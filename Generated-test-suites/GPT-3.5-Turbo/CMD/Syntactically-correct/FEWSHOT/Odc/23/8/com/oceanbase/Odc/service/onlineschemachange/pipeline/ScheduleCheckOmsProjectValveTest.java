package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ListProjectFullVerifyResultRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.stage.OnlineSchemaChangeScheduleTaskResult;

public class ScheduleCheckOmsProjectValveTest {

    @Test
    public void testInvoke() {
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        ValveContext valveContext = mock(ValveContext.class);
        OscValveContext context = mock(OscValveContext.class);
        when(context.getScheduleTask()).thenReturn(new ScheduleTaskEntity());
        when(context.getTaskParameter()).thenReturn(new OnlineSchemaChangeScheduleTaskParameters());
        when(context.getParameter()).thenReturn(new OnlineSchemaChangeParameter());
        when(valveContext.getValveContext()).thenReturn(context);
        
        valve.invoke(valveContext);
    }

    @Test
    public void testHandleOmsProjectStepResult() {
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        ProjectStepResult projectStepResult = new ProjectStepResult();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        valve.handleOmsProjectStepResult(null, projectStepResult, result, SwapTableType.NONE, new ScheduleTaskEntity());
    }

    @Test
    public void testContinueHandleProjectStepResult() {
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setPreCheckResult(PrecheckResult.PASSED);
        valve.continueHandleProjectStepResult(projectStepResult);
    }

    @Test
    public void testListProjectFullVerifyResult() {
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        ProjectOpenApiService projectOpenApiService = mock(ProjectOpenApiService.class);
        when(projectOpenApiService.listProjectFullVerifyResult(mock(ListProjectFullVerifyResultRequest.class))).thenReturn(new ProjectFullVerifyResultResponse());
        valve.listProjectFullVerifyResult("projectId", "dbName", "uid");
    }

    @Test
    public void testAdaptResult() {
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        ProjectStepResult projectStepResult = new ProjectStepResult();
        valve.adaptResult(result, projectStepResult);
    }

    @Test
    public void testGetProjectRequest() {
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        OnlineSchemaChangeScheduleTaskParameters taskParam = new OnlineSchemaChangeScheduleTaskParameters();
        taskParam.setUid("uid");
        taskParam.setOmsProjectId("projectId");
        valve.getProjectRequest(taskParam);
    }

    @Test
    public void testRecordCurrentProgress() {
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setCurrentStep("PRE_CHECK");
        valve.recordCurrentProgress("projectId", result);
    }

    @Test
    public void testGetHeader() {
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        valve.getHeader();
    }
}