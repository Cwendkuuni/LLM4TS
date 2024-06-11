// Corresponding JUnit 4 Test Cases:

package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class ScheduleCheckOmsProjectValveTest {

    @Test
    public void testInvoke() {
        // Setup
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        OscValveContext valveContext = mock(OscValveContext.class);
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        when(valveContext.getScheduleTask()).thenReturn(scheduleTask);
        when(valveContext.getTaskParameter()).thenReturn(taskParameter);

        // Test
        valve.invoke(valveContext);

        // Verify
        // Add verification steps here
    }

    // Add more test cases for other methods in ScheduleCheckOmsProjectValve class

}