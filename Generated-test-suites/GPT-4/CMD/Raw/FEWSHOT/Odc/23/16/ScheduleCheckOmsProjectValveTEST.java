
package com.oceanbase.odc.service.onlineschemachange.pipeline;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.pipeline.valve.OscValveContext;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleCheckOmsProjectValveTest {
    
    @Mock
    private ProjectOpenApiService projectOpenApiService;
    
    @Mock
    private OscTaskCompleteHandler completeHandler;
    
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    
    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;
    

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testInvoke() {
        
        OscValveContext context = mock(OscValveContext.class);
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        when(context.getScheduleTask()).thenReturn(scheduleTask);

        OnlineSchemaChangeScheduleTaskParameters taskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        when(context.getTaskParameter()).thenReturn(taskParameters);

        ProjectControlRequest projectRequest = new ProjectControlRequest();
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(new ProjectProgressResponse());
      
        scheduleCheckOmsProjectValve.invoke(context);
        
        verify(completeHandler).complete(scheduleTask.getId(), TaskStatus.DONE);
    }
    
    // Continue with other test cases
}
