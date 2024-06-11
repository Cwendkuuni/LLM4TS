
package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValve;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;

public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke() {

        OscValveContext context = mock(OscValveContext.class);
        
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        
        OnlineSchemaChangeScheduleTaskParameters params = new OnlineSchemaChangeScheduleTaskParameters();
        params.setUid("12345");
        params.setOmsProjectId("6789");
        
        when(context.getScheduleTask()).thenReturn(scheduleTaskEntity);
        when(context.getTaskParameter()).thenReturn(params);
        
        scheduleCheckOmsProjectValve.invoke(context);
        
        verify(context, times(1)).getScheduleTask();
        verify(context, times(1)).getTaskParameter();
        verify(scheduleTaskRepository, times(1)).update(any());
    }
}
