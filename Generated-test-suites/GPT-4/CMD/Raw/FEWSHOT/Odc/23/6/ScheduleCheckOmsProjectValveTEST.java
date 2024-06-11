
package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import com.oceanbase.odc.service.onlineschemachange.pipeline.valveContext.OscValveContext;

public class ScheduleCheckOmsProjectValveTest {
    @Mock
    ProjectOpenApiService projectOpenApiService;
    @InjectMocks
    ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void checkInvokeMethodForNonNullValveContext() {
        OscValveContext context = new OscValveContext();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1);

        context.setScheduleTask(scheduleTask);
        scheduleCheckOmsProjectValve.invoke(context);

        // assert result here: preferably through spy or some mock behaviour
    }

    @Test
    public void checkAdaptResultNotNull() {
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setFullTransferEstimatedCount(25);
        projectStepResult.setFullTransferFinishedCount(15);
        projectStepResult.setFullTransferProgressPercentage(100.00);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectStepResult.setFullVerificationResultDescription("description");
        projectStepResult.setFullVerificationProgressPercentage(100.00);
        
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        
        scheduleCheckOmsProjectValve.adaptResult(result, projectStepResult);
        
        //assertions that the result has been updated accordingly
        assertEquals(result.getFullTransferEstimatedCount(), new Integer(25));
        assertEquals(result.getFullTransferFinishedCount(), new Integer(15));
        assertEquals(result.getFullTransferProgressPercentage(), new Double(100.00));
    }

    // continue to add all other test methods
}
