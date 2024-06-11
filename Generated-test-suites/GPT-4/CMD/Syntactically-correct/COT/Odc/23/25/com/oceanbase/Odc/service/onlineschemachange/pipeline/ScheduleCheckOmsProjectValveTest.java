package com.oceanbase.odc.service.onlineschemachange.pipeline;
import org.junit.Test;
import com.oceanbase.odc.service.onlineschemachange.pipeline.OscValveContext;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValve;
import static org.mockito.Mockito.*;

public class ScheduleCheckOmsProjectValveTest {
    
    @Test
    public void testInvoke() {
        OscValveContext context = mock(OscValveContext.class);
        ScheduleCheckOmsProjectValve scheduleCheck = new ScheduleCheckOmsProjectValve();
        scheduleCheck.invoke(context);
    }
    
    @Test(expected = NullPointerException.class)
    public void testInvoke_NullValveContext() {
        ScheduleCheckOmsProjectValve scheduleCheck = new ScheduleCheckOmsProjectValve();
        scheduleCheck.invoke(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoke_NullScheduleTask() {
        OscValveContext context = mock(OscValveContext.class);
        when(context.getScheduleTask()).thenReturn(null);
        ScheduleCheckOmsProjectValve scheduleCheck = new ScheduleCheckOmsProjectValve();
        scheduleCheck.invoke(context);
    }

    @Test(expected = ClassCastException.class)
    public void testInvoke_InvalidScheduleTask() {
        OscValveContext context = mock(OscValveContext.class);
        Object scheduleTask = new Object();
        when(context.getScheduleTask()).thenReturn(scheduleTask);;
        ScheduleCheckOmsProjectValve scheduleCheck = new ScheduleCheckOmsProjectValve();
        scheduleCheck.invoke(context);
    }
}