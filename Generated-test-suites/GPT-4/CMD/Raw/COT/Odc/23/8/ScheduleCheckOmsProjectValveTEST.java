
package com.oceanbase.odc.service.onlineschemachange.tests.pipeline;

import com.oceanbase.odc.service.onlineschemachange.pipeline.*;
import com.oceanbase.odc.service.onlineschemachange.model.*;
import com.oceanbase.odc.service.onlineschemachange.oms.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ScheduleCheckOmsProjectValveTest {
    private ScheduleCheckOmsProjectValve checkOmsProjectValve;
    private ProjectOpenApiService projectOpenApiService;
    private OscTaskCompleteHandler oscTaskCompleteHandler;
    private ScheduleTaskRepository scheduleTaskRepository;
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    private OscValveContext oscValveContext;
    private ScheduleTaskEntity scheduleTaskEntity;
   
    @Before
    public void setUp() {
        projectOpenApiService = mock(ProjectOpenApiService.class);
        oscTaskCompleteHandler = mock(OscTaskCompleteHandler.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        onlineSchemaChangeProperties = mock(OnlineSchemaChangeProperties.class);
        oscValveContext = mock(OscValveContext.class);
        scheduleTaskEntity = mock(ScheduleTaskEntity.class);

        checkOmsProjectValve = new ScheduleCheckOmsProjectValve();
        checkOmsProjectValve.projectOpenApiService = projectOpenApiService;
        checkOmsProjectValve.oscTaskCompleteHandler = oscTaskCompleteHandler;
        checkOmsProjectValve.scheduleTaskRepository = scheduleTaskRepository;
        checkOmsProjectValve.onlineSchemaChangeProperties = onlineSchemaChangeProperties;
    }

    //1. Testing invoke method
    @Test()
    public void testInvoke() {
        when(oscValveContext.getScheduleTask()).thenReturn(scheduleTaskEntity);
        checkOmsProjectValve.invoke(oscValveContext);
        verify(oscValveContext, times(1)).getScheduleTask();
    }
}
