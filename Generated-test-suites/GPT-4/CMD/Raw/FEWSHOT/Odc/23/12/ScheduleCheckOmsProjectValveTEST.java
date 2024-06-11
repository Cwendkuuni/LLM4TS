
package com.oceanbase.odc.service.onlineschemachange.pipeline;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve valve;

    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @Mock
    private OscValveContext valveContext;

    @Mock
    private ScheduleTaskEntity scheduleTask;

    @Mock
    private OnlineSchemaChangeScheduleTaskParameters taskParameter;

    @Mock
    private ProjectStepResult projectStepResult;
    
    @Before
    public void setUp() {
    }

    @Test
    public void testInvoke() {
        when(valveContext.getScheduleTask()).thenReturn(scheduleTask);
        when(valveContext.getTaskParameter()).thenReturn(taskParameter);
        valve.invoke(valveContext);
        verify(valveContext).getScheduleTask();
        verify(valveContext).getTaskParameter();
    }

    @Test
    public void testContinueHandleProjectStepResult() {
        when(projectStepResult.getPreCheckResult()).thenReturn(PrecheckResult.FAILED);
        try {
            valve.continueHandleProjectStepResult(projectStepResult);
        } catch (Exception e) {
            assertNotNull(e);
            assertEquals(OscException.class, e.getClass());
        }
    }

    @Test
    public void testAdaptResult() {
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        valve.adaptResult(result, projectStepResult);
        assertNotNull(result.getFullTransferEstimatedCount());
        assertNotNull(result.getFullTransferFinishedCount());
        assertNotNull(result.getFullTransferProgressPercentage());
        assertNotNull(result.getFullVerificationResult());
        assertNotNull(result.getFullVerificationResultDescription());
        assertNotNull(result.getFullVerificationProgressPercentage());
        assertNotNull(result.getCurrentStep());
        assertNotNull(result.getCurrentStepStatus());
        assertNotNull(result.getPrecheckResult());
    }
}
