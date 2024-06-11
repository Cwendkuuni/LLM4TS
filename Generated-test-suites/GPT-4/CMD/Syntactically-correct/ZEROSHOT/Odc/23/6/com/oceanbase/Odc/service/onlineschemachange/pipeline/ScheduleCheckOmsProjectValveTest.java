
package com.oceanbase.odc.service.onlineschemachange.pipeline;
 
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.PrecheckResult;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectValveContext;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectValveContext.ValveContext;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;


@RunWith(MockitoJUnitRunner.class)
public class ScheduleCheckOmsProjectValveTest {

    @Mock
    private ProjectOpenApiService projectOpenApiService;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    
    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    private ProjectValveContext context;
    private OnlineSchemaChangeScheduleTaskParameters taskParameter;
    private ScheduleTaskEntity scheduleTask;
    private ProjectStepVO projectStep;

    @Before
    public void setUp() {
        context = mock(ProjectValveContext.class);
        taskParameter = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        scheduleTask = mock(ScheduleTaskEntity.class);
        projectStep = mock(ProjectStepVO.class);

        when(context.getScheduleTask()).thenReturn(scheduleTask);
        when(context.getTaskParameter()).thenReturn(taskParameter);
    }
    
    @Test
    public void invokeTest() {
        //mock entities
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
        OnlineSchemaChangeScheduleTaskResult lastResult = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        
        //mock returnd data
        when(projectOpenApiService.describeProjectSteps(any())).thenReturn(Arrays.asList(projectStep));
        when(projectOpenApiService.describeProjectProgress(any())).thenReturn(progress);
        when(onlineSchemaChangeProperties.isEnableFullVerify()).thenReturn(true);
        when(taskParameter.getUid()).thenReturn("uid");
        when(projectStepResult.getTaskStatus()).thenReturn(TaskStatus.DONE);
        when(scheduleTask.getResultJson()).thenReturn("");
        
        //invoke the method
        scheduleCheckOmsProjectValve.invoke(context);
        
        //assert and verify
        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verify(onlineSchemaChangeProperties, times(1)).isEnableFullVerify();
        verify(taskParameter, times(1)).getUid();
    }
}

