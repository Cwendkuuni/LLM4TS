
package com.oceanbase.odc.service.onlineschemachange.pipeline;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleCheckOmsProjectValveTest {

    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @Mock
    private OscTaskCompleteHandler completeHandler;
    
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;
    
    private OscValveContext context;

    @Before
    public void setup() {
        context = Mockito.mock(OscValveContext.class);
    }
    
    @Test
    public void testInvoke() {
        ScheduleTaskEntity scheduleTask = Mockito.mock(ScheduleTaskEntity.class);
        Mockito.when(context.getScheduleTask()).thenReturn(scheduleTask);
        
        OnlineSchemaChangeScheduleTaskParameters taskParameter = Mockito.mock(OnlineSchemaChangeScheduleTaskParameters.class);
        Mockito.when(context.getTaskParameter()).thenReturn(taskParameter);

        scheduleCheckOmsProjectValve.invoke(context);

        Mockito.verify(context, times(1)).getScheduleTask();
        Mockito.verify(context, times(1)).getTaskParameter();
        Mockito.verify(projectOpenApiService, times(1)).describeProjectSteps(any(ProjectControlRequest.class));
        Mockito.verify(projectOpenApiService, times(1)).describeProjectProgress(any(ProjectControlRequest.class));
        Mockito.verify(projectOpenApiService, times(1)).resumeProject(any(ProjectControlRequest.class));
        Mockito.verify(scheduleTaskRepository, times(2)).update(any(ScheduleTaskEntity.class));
    }
}
