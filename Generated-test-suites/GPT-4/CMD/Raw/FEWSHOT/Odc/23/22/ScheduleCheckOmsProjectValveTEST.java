package com.oceanbase.odc.service.onlineschemachange.pipeline;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleCheckOmsProjectValveTest {

    @Mock
    private ValveContext valveContext;
    
    @Mock
    private OscValveContext oscValveContext;
    
    @Mock
    private ProjectStepResult projectStepResult;
    
    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    @Test
    public void test_invoke() {
        when(oscValveContext.getTaskParameter()).thenReturn(null);
        when(projectStepResult.getTaskStatus()).thenReturn(TaskStatus.DONE);
        when(oscValveContext.getParameter()).thenReturn(null);
        when(valveContext.getScheduleTask()).thenReturn(scheduleTaskEntity);

        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        valve.invoke(oscValveContext);

        verify(oscValveContext, atLeastOnce()).getProjectParameter();
        verify(projectStepResult, atLeastOnce()).getTaskStatus();
    }

    @Test
    public void test_handleOmsProjectStepResult() {
        when(valveContext.getScheduleTask()).thenReturn(scheduleTaskEntity);
        when(projectStepResult.getTaskStatus()).thenReturn(TaskStatus.DONE);
        when(projectStepResult.getFullVerificationResult()).thenReturn(null);
        
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        valve.handleOmsProjectStepResult(valveContext, projectStepResult, null, SwapTableType.ALWAYS, scheduleTaskEntity);
    
        verify(valveContext, atLeastOnce()).getScheduleTask();
        verify(projectStepResult, atLeastOnce()).getTaskStatus();
    }

    @Test
    public void test_continueHandleProjectStepResult() {
        when(projectStepResult.getPreCheckResult()).thenReturn(null);
        when(projectStepResult.getFullVerificationResult()).thenReturn(null);

        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        valve.continueHandleProjectStepResult(projectStepResult);

        verify(projectStepResult, atLeastOnce()).getPreCheckResult();
        verify(projectStepResult, atLeastOnce()).getFullVerificationResult();
    }

    @Test
    public void test_listProjectFullVerifyResult() {
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        ProjectFullVerifyResultResponse res = valve.listProjectFullVerifyResult("test_project", "test_database", "test_uid");

        assertNotNull(res);
    }

    @Test
    public void test_adaptResult() {
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        valve.adaptResult(null, projectStepResult);

        verify(projectStepResult, atLeastOnce()).getFullTransferEstimatedCount();
        verify(projectStepResult, atLeastOnce()).getFullTransferFinishedCount();
    }
}