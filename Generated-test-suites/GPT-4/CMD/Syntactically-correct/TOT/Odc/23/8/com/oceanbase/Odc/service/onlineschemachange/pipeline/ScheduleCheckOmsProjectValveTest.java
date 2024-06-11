package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.mockito.Mockito.*;

import com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValve;
import com.oceanbase.odc.service.onlineschemachange.pipeline.OscValveContext;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleCheckOmsProjectValveTest {
    @Mock
    OscValveContext oscValveContext;
    @Mock
    ScheduleTaskEntity scheduleTaskEntity;

    @InjectMocks
    ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    //One method test case
    @Test
    public void testInvoke() {
        when(oscValveContext.getScheduleTask()).thenReturn(scheduleTaskEntity);
       
        try{
           scheduleCheckOmsProjectValve.invoke(oscValveContext);
        }catch(Exception e){
          // Exception handling/Assertion code
        }
        verify(oscValveContext, times(1)).getScheduleTask();
    }
}
