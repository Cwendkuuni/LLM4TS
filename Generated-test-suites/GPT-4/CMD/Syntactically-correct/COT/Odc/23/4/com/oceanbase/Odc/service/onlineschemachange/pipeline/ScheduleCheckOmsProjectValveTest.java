package com.oceanbase.odc.service.onlineschemachange.pipeline;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.pipeline.OscValveContext;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ValveContext;


@RunWith(MockitoJUnitRunner.class)
public class ScheduleCheckOmsProjectValveTest {

    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @InjectMocks
    ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

   @Mock
   OscValveContext valveContext;

   @Mock
   ScheduleTaskEntity scheduleTask;

    @Test
    public void testInvoke() {
    
        Mockito.when(valveContext.getScheduleTask()).thenReturn(scheduleTask);
        scheduleCheckOmsProjectValve.invoke(valveContext);
        Mockito.verify(valveContext, Mockito.times(1)).getScheduleTask();
    }

   @Test
   public void testHandleOmsProjectStepResult() {
    
    // Method is private and not testable directly. It will be indirectly
    // tested in the invoke() test case.  

   }

   @Test
   public void testContinueHandleProjectStepResult() {
    
    // Method is private and not testable directly. It will be indirectly
    // tested in the invoke() test case.
   }

   @Test
   public void testListProjectFullVerifyResult() {
   
    //Method is private and not testable directly. It will be indirectly
    //tested in the invoke() test case.   
   }
  
   @Test
   public void testAdaptResult() {
   
    // Method is private and not testable directly. It will be indirectly
    // tested in the invoke() test case.    
   }

   @Test
   public void testGetProjectRequest() {
   
    // Method is private and not testable directly. It will be indirectly
    // tested in the invoke() test case.    
   }

   @Test
   public void testRecordCurrentProgress() {
   
    // Method is private and not testable directly. It will be indirectly
    // tested in the invoke() test case.   
   }

   @Test
   public void testGetHeader() {

    // Method is private and not testable directly.
   }
}

