package com.oceanbase.odc.service.onlineschemachange.pipeline;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve valve;

    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @Mock
    private OscTaskCompleteHandler completeHandler;
    
    //...other mock objects

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAdaptResult() {
        // Adjust to match your classes
        ProjectStepResult projectResult = new ProjectStepResult();
        OnlineSchemaChangeScheduleTaskResult scheduleResult = new OnlineSchemaChangeScheduleTaskResult();

        // Set projectResult specific values
        projectResult.setFullTransferEstimatedCount(10);
        //... other settings

        // Call method under test
        valve.adaptResult(scheduleResult, projectResult);

        // Assert that values were copied correctly from projectResult to scheduleResult
        assertEquals(10, scheduleResult.getFullTransferEstimatedCount());
        //... other assertions        
    }
  
  //... more unit tests
}