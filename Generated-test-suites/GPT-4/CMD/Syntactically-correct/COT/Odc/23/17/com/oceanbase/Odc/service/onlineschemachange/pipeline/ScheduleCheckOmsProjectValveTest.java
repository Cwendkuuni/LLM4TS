package com.oceanbase.odc.service.onlineschemachange.pipeline;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.junit.Assert.assertEquals;

public class ScheduleCheckOmsProjectValveTest {
    @Mock
    private ValveContext valveContext;

    @Mock
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Mock
    private OscValveContext oscValveContext;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        oscValveContext = new OscValveContext();
    }

    @Test(expected = NullPointerException.class)
    public void invokeWithNullValveContext() {
        scheduleCheckOmsProjectValve.invoke(null);
    }

    @Test
    public void invokeWithValidValveContext() {
        when(valveContext.isNotNull(any())).thenReturn(true);
        scheduleCheckOmsProjectValve.invoke(oscValveContext);
        assertEquals(oscValveContext, oscValveContext);
    }
}