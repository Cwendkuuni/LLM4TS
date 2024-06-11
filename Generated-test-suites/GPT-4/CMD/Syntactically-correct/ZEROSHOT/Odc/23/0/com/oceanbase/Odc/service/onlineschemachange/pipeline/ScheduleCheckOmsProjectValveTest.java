package com.oceanbase.odc.service.onlineschemachange.pipeline;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.PrecheckResult;
import com.oceanbase.odc.service.onlineschemachange.pipeline.OscValveContext;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleCheckOmsProjectValveTest {
    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;
    @Mock
    private OscValveContext oscValveContext;
    @Mock
    private OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeTaskParameters;
    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;
    @Mock
    private ProjectStepResult projectStepResult;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke() {
        Mockito.when(oscValveContext.getScheduleTask()).thenReturn(scheduleTaskEntity);
        Mockito.when(oscValveContext.getTaskParameter()).thenReturn(onlineSchemaChangeTaskParameters);
        Mockito.when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.DONE.name());
        Mockito.when(scheduleTaskEntity.getId()).thenReturn(1L);
        Mockito.when(projectStepResult.getTaskStatus()).thenReturn(TaskStatus.DONE);

        scheduleCheckOmsProjectValve.invoke(oscValveContext);

        Mockito.verify(scheduleTaskEntity, Mockito.times(1)).getResultJson();
        Mockito.verify(scheduleTaskEntity, Mockito.times(1)).setResultJson(any(String.class));
    }

    @Test(expected = RuntimeException.class)
    public void testInvokePrecheckFailed() {
        Mockito.when(oscValveContext.getScheduleTask()).thenReturn(scheduleTaskEntity);
        Mockito.when(oscValveContext.getTaskParameter()).thenReturn(onlineSchemaChangeTaskParameters);
        Mockito.when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.DONE.name());
        Mockito.when(scheduleTaskEntity.getId()).thenReturn(1L);
        Mockito.when(projectStepResult.getTaskStatus()).thenReturn(TaskStatus.DONE);
        Mockito.when(projectStepResult.getPreCheckResult()).thenReturn(PrecheckResult.FAILED);
        
        scheduleCheckOmsProjectValve.invoke(oscValveContext);
    }

    @Test(expected = RuntimeException.class)
    public void testInvokeInconsistentData() {
        Mockito.when(oscValveContext.getScheduleTask()).thenReturn(scheduleTaskEntity);
        Mockito.when(oscValveContext.getTaskParameter()).thenReturn(onlineSchemaChangeTaskParameters);
        Mockito.when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.DONE.name());
        Mockito.when(scheduleTaskEntity.getId()).thenReturn(1L);
        Mockito.when(projectStepResult.getTaskStatus()).thenReturn(TaskStatus.DONE);
        Mockito.when(projectStepResult.getFullVerificationResult()).thenReturn(FullVerificationResult.INCONSISTENT);
        
        scheduleCheckOmsProjectValve.invoke(oscValveContext);
    }
}
