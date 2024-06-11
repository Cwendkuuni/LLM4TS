
package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;

import java.util.ArrayList;

public class ScheduleCheckOmsProjectValveTest {

    @Autowired
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;
  
    @Before
    public void setUp() {
      scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
    }

    @Test
    public void testInvoke() {

        OscValveContext context = mock(OscValveContext.class);
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);

        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId("1");
        taskParameter.setDatabaseName("TestDB");
        taskParameter.setUid("2");

        when(context.getScheduleTask()).thenReturn(scheduleTask);
        when(context.getTaskParameter()).thenReturn(taskParameter);
        
        ArrayList<ProjectStepVO> projectSteps = new ArrayList<>();
        ProjectStepVO step = new ProjectStepVO();
        step.setStep("step1");
        projectSteps.add(step);
        
        ProjectControlRequest projectRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParameter);
        
        ProjectFullVerifyResultResponse response = listProjectFullVerifyResult(
            taskParameter.getOmsProjectId(), 
            taskParameter.getDatabaseName(),
            taskParameter.getUid());
     
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        OnlineSchemaChangeScheduleTaskResult lastResult = mock(OnlineSchemaChangeScheduleTaskResult.class);
        
        OnlineSchemaChangeScheduleTaskResult result = scheduleCheckOmsProjectValve.invoke(result, projectStepResult, step);
        assertNotNull(result);
        assertEquals(TaskStatus.DONE, result.getTaskStatus());
        assertEquals(FullVerificationResult.CONSISTENT, result.getFullVerificationResult());
        assertEquals(FullVerificationResult.UNCHECK, result.getFullVerificationResult());
    }

    @Test
    public void testHandleOmsProjectStepResult() {
        ValveContext valveContext = mock(ValveContext.class);
        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
        OnlineSchemaChangeScheduleTaskResult result = mock(OnlineSchemaChangeScheduleTaskResult.class);
        SwapTableType swapTableType = SwapTableType.NEW;
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);

        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(valveContext, projectStepResult, result, swapTableType, scheduleTask);

        assertEquals(TaskStatus.DONE, projectStepResult.getTaskStatus());
    }
}
