package com.oceanbase.odc.service.onlineschemachange.pipeline;

import com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValve;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Mock
    private ValveContext valveContext;

    @Mock
    private OscValveContext context;
    
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testInvoke() {
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        when(context.getScheduleTask()).thenReturn(scheduleTask);
        scheduleCheckOmsProjectValve.invoke(valveContext);
        verify(context).getScheduleTask();
    }
}

