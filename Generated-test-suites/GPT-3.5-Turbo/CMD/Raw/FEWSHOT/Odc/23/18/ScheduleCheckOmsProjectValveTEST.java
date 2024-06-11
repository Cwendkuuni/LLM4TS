package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import com.oceanbase.odc.common.json.JsonUtils;
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
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectCheckOmsProjectValve.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;

public class ScheduleCheckOmsProjectValveTest {

    private ScheduleCheckOmsProjectValve valve;
    private ProjectOpenApiService projectOpenApiService;
    private OscTaskCompleteHandler completeHandler;
    private ScheduleTaskRepository scheduleTaskRepository;
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    private ScheduleTaskEntity scheduleTask;

    @Before
    public void setUp() {
        valve = new ScheduleCheckOmsProjectValve();
        projectOpenApiService = mock(ProjectOpenApiService.class);
        completeHandler = mock(OscTaskCompleteHandler.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        onlineSchemaChangeProperties = mock(OnlineSchemaChangeProperties.class);
        scheduleTask = new ScheduleTaskEntity();
        valve.projectOpenApiService = projectOpenApiService;
        valve.completeHandler = completeHandler;
        valve.scheduleTaskRepository = scheduleTaskRepository;
        valve.onlineSchemaChangeProperties = onlineSchemaChangeProperties;
    }

    @Test
    public void testHandleOmsProjectStepResult() {
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setTaskStatus(TaskStatus.DONE);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        SwapTableType swapTableType = SwapTableType.AUTO;
        when(scheduleTaskRepository.update(any())).thenReturn(null);
        
        valve.handleOmsProjectStepResult(null, projectStepResult, result, swapTableType, scheduleTask);
        
        assertFalse(result.isManualSwapTableEnabled());
    }

    @Test
    public void testContinueHandleProjectStepResult_PreCheckFailed() {
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);
        assertThrows(OscException.class, () -> valve.continueHandleProjectStepResult(projectStepResult));
    }

    @Test
    public void testContinueHandleProjectStepResult_DataCheckInconsistent() {
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        assertThrows(OscException.class, () -> valve.continueHandleProjectStepResult(projectStepResult));
    }

    @Test
    public void testListProjectFullVerifyResult() {
        String projectId = "123";
        String databaseName = "test";
        String uid = "456";
        ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest();
        request.setProjectId(projectId);
        request.setSourceSchemas(new String[] { databaseName });
        request.setDestSchemas(new String[] { databaseName });
        request.setStatus(new String[] { "FINISHED", "SUSPEND", "RUNNING" });
        request.setPageSize(10);
        request.setPageNumber(1);
        request.setUid(uid);
        ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();

        when(projectOpenApiService.listProjectFullVerifyResult(request)).thenReturn(response);

        assertEquals(response, valve.listProjectFullVerifyResult(projectId, databaseName, uid));
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(request);
    }
}