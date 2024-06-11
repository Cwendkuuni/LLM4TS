
package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.mockito.Mockito.*;

import com.oceanbase.odc.common.util.tableformat.Table;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class ScheduleCheckOmsProjectValveTest {
    ScheduleCheckOmsProjectValve valve;
    ProjectOpenApiService service;
    OscTaskCompleteHandler handler;
    ScheduleTaskRepository repository;

    @Before
    public void init() {
        valve = new ScheduleCheckOmsProjectValve();
        service = mock(ProjectOpenApiService.class);
        handler = mock(OscTaskCompleteHandler.class);
        repository = mock(ScheduleTaskRepository.class);
        valve.setProjectOpenApiService(service);
        valve.setOscTaskCompleteHandler(handler);
        valve.setScheduleTaskRepository(repository);
    }

    @Test
    public void testHandleOmsProjectStepResult() {
        // create some fake outputs
        ProjectStepResult fakeStepResult = new ProjectStepResult();
        fakeStepResult.setTaskStatus(TaskStatus.DONE);
        fakeStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        ValveContext fakeContext = mock(ValveContext.class);
        ScheduleTaskEntity fakeTask = new ScheduleTaskEntity();

        valve.handleOmsProjectStepResult(fakeContext, fakeStepResult, null, SwapTableType.ALL, fakeTask);
        verify(service, times(1)).describeProjectProgress(any());
        verify(handler, times(1)).handleTaskComplete(any(), anyBoolean());
    }

    @Test
    public void testContinueHandleProjectStepResult() {
        ProjectStepResult result = new ProjectStepResult();
        result.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        result.setPreCheckResult(PrecheckResult.FAILED);
        try {
            valve.continueHandleProjectStepResult(result);
        } catch (OscException e) {
            assertNotNull(e);
            assertEquals(e.getCode(), ErrorCodes.OmsPreCheckFailed);
        }
    }

    @Test
    public void testListProjectFullVerifyResult() {
        ProjectFullVerifyResultResponse response = valve.listProjectFullVerifyResult("projectId", "databaseName", "uid");
        assertNull(response);
    }

    @Test
    public void testAdaptResult() {
        OnlineSchemaChangeScheduleTaskResult scheduleTaskResult = new OnlineSchemaChangeScheduleTaskResult();
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setCurrentStepStatus("Status");
        projectStepResult.setFullVerificationResult(FullVerificationResult.NOT_YET_RUN);
        projectStepResult.setFullTransferFinishedCount(100L);
        projectStepResult.setFullTransferEstimatedCount(200L);
        projectStepResult.setFullTransferProgressPercentage(0.5F);

        valve.adaptResult(scheduleTaskResult, projectStepResult);

        assertEquals(scheduleTaskResult.getFullTransferFinishedCount(), Long.valueOf(100L));
        assertEquals(scheduleTaskResult.getFullTransferEstimatedCount(), Long.valueOf(200L));
        assertEquals(scheduleTaskResult.getFullTransferProgressPercentage(), Float.valueOf(0.5F));
    }

    @Test
    public void testRecordCurrentProgress() {
        OnlineSchemaChangeScheduleTaskResult taskResult = new OnlineSchemaChangeScheduleTaskResult();
        taskResult.setFullTransferProgressPercentage(0.5F);
        taskResult.setCurrentStep("FULL_TRANSFER");
        taskResult.setFullTransferFinishedCount(100L);
        taskResult.setFullTransferEstimatedCount(200L);

        valve.recordCurrentProgress("projectId", taskResult);
    }
}
