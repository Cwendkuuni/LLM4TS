package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;

import org.junit.Test;

public class ScheduleCheckOmsProjectValveTest {
 
    @Test
    public void testInvoke() {
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        ValveContext mockContext = mock(OscValveContext.class);
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        // TODO: Mock the returned object and actions of methods in the OscValveContext
        // including getScheduleTask()

        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        // TODO: Set the task parameters

        valve.invoke(mockContext);
        // TODO: Verify the result or the state of objects
    }
    
    @Test
    public void testHandleOmsProjectStepResult() {
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        ValveContext mockContext = mock(OscValveContext.class);
        ProjectStepResult projectStepResult = new ProjectStepResult();
        // TODO: Set the project step result
        
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        // TODO: Set the task result
        
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        // TODO: Set the schedule task

        valve.handleOmsProjectStepResult(mockContext, projectStepResult, result, SwapTableType.MANUAL, scheduleTask);
        // TODO: Verify the result or the state of objects
    }
    
    @Test
    public void testContinueHandleProjectStepResult() {
        try {
            ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
            ProjectStepResult projectStepResult = new ProjectStepResult();
            projectStepResult.setPreCheckResult(PrecheckResult.FAILED);
            valve.continueHandleProjectStepResult(projectStepResult);
        } catch (OscException e) {
            assertEquals(ErrorCodes.OmsPreCheckFailed, e.getCode());
        }

        try {
            ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
            ProjectStepResult projectStepResult = new ProjectStepResult();
            projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
            valve.continueHandleProjectStepResult(projectStepResult);
        } catch (OscException e) {
            assertEquals(ErrorCodes.OmsDataCheckInconsistent, e.getCode());
        }
    }

    // TODO: Generate JUnit test cases for the remaining methods in the ScheduleCheckOmsProjectValve class
}