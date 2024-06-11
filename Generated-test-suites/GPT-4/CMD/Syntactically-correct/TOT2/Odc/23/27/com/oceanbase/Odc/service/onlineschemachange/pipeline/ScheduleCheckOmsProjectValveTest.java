package com.oceanbase.odc.service.onlineschemachange.pipeline;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Mock
    OscValveContext oscValveContext;

    @Mock
    ScheduleTaskEntity scheduleTaskEntity;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Mockito.when(oscValveContext.getScheduleTask()).thenReturn(scheduleTaskEntity);
    }

    @Test
    public void invokeTest() {
        try {
            scheduleCheckOmsProjectValve.invoke(oscValveContext);
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }
}