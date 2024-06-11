
package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.PrecheckResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    @Mock
    private ProjectStepResult projectStepResult;

    @Mock
    private OnlineSchemaChangeScheduleTaskResult result;

    @Before
    public void setUp() {
        when(scheduleTaskEntity.getId()).thenReturn(1L);
    }

    @Test
    public void testHandleOmsProjectStepResultTaskDoneAndConsistent() {
        when(projectStepResult.getTaskStatus()).thenReturn(TaskStatus.DONE);
        when(projectStepResult.getFullVerificationResult()).thenReturn(FullVerificationResult.CONSISTENT);
        try {
            scheduleCheckOmsProjectValve.handleOmsProjectStepResult(null, projectStepResult, result, SwapTableType.AUTO, scheduleTaskEntity);
        } catch (OscException ex) {
            fail("OscException thrown");
        }
    }

    @Test
    public void testHandleOmsProjectStepResultTaskDoneAndUncheck() {
        when(projectStepResult.getTaskStatus()).thenReturn(TaskStatus.DONE);
        when(projectStepResult.getFullVerificationResult()).thenReturn(FullVerificationResult.UNCHECK);
        try {
            scheduleCheckOmsProjectValve.handleOmsProjectStepResult(null, projectStepResult, result, SwapTableType.AUTO, scheduleTaskEntity);
        } catch (OscException ex) {
            fail("OscException thrown");
        }
    }

    @Test(expected = OscException.class)
    public void testContinueHandleProjectStepResultPrecheckFailed() {
        when(projectStepResult.getPreCheckResult()).thenReturn(PrecheckResult.FAILED);
        scheduleCheckOmsProjectValve.continueHandleProjectStepResult(projectStepResult);
    }

    @Test(expected = OscException.class)
    public void testContinueHandleProjectStepResultFullverifyInconsistent() {
        when(projectStepResult.getFullVerificationResult()).thenReturn(FullVerificationResult.INCONSISTENT);
        scheduleCheckOmsProjectValve.continueHandleProjectStepResult(projectStepResult);
    }
}
