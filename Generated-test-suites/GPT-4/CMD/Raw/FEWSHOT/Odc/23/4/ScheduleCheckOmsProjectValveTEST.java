
package com.oceanbase.odc.service.onlineschemachange.pipeline;

import java.util.Collections;

import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.*;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.oms.response.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleCheckOmsProjectValveTest {

    @Mock
    private ValveContext valveContext;
    @Mock
    private OscValveContext context;
    @Mock
    private ScheduleTaskEntity scheduleTask;
    @Mock
    private OnlineSchemaChangeProperties optionProperties;
    @Mock
    private OnlineSchemaChangeScheduleTaskResult result;
    @Mock
    private OnlineSchemaChangeScheduleTaskParameters taskParams;
    @Mock
    private OscTaskCompleteHandler oscTaskCompleteHandler;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    private ScheduleCheckOmsProjectValve valve;

    @Test
    public void testInvoke() {
        when(context.getScheduleTask()).thenReturn(scheduleTask);
        when(valveContext.getScheduleTask()).thenReturn(scheduleTask);
        when(context.getTaskParameter()).thenReturn(taskParams);
        when(scheduleTask.getResultJson()).thenReturn("{}");
        when(context.getParameter().getSwapTableType()).thenReturn(SwapTableType.AUTO);
        when(options.isEnableFullVerify()).thenReturn(true);

        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setTaskStatus(TaskStatus.DONE);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        result.setFullTransferProgressPercentage(90);
        result.setManualSwapTableEnabled(false);
        result.setManualSwapTableStarted(false);

        result.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        valve.invoke(valveContext);

        verify(scheduleTaskRepository).update(scheduleTask);
    }

}
