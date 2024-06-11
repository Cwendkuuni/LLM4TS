package com.oceanbase.odc.service.onlineschemachange.pipeline;
import com.oceanbase.odc.service.onlineschemachange.pipeline.OscValveContext;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Before; 

public class ScheduleCheckOmsProjectValveTest {
    ScheduleCheckOmsProjectValve valveToTest;
    
    @Before
    public void setup(){
        valveToTest = spy(new ScheduleCheckOmsProjectValve());
    }

    @Test(expected=OscException.class)
    public void testHandleOmsProjectStepResult_FailedPrecheckResult_ExceptionThrown() { 
        ValveContext mockVContext = mock(ValveContext.class);
        ProjectStepResult mockPStepResult = mock(ProjectStepResult.class);
        doReturn(PrecheckResult.FAILED).when(mockPStepResult).getPreCheckResult();
        doReturn(TaskStatus.UNDEFINED).when(mockPStepResult).getTaskStatus(); 

        OnlineSchemaChangeScheduleTaskResult mockResult = mock(OnlineSchemaChangeScheduleTaskResult.class);
        SwapTableType mockSwapType = mock(SwapTableType.class);
        ScheduleTaskEntity mockScheduleTask = mock(ScheduleTaskEntity.class);

        valveToTest.handleOmsProjectStepResult(mockVContext, mockPStepResult, mockResult, mockSwapType, mockScheduleTask);
    }
    // write similar test cases for other scenarios covered by handleOmsProjectStepResult
}