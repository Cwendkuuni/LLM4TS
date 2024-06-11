
package com.oceanbase.odc.service.onlineschemachange.pipeline;

import com.oceanbase.odc.common.json.JsonUtils; 
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;

import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleCheckOmsProjectValveTest {

    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Before
    public void setUp() {
        scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        scheduleCheckOmsProjectValve.projectOpenApiService = Mockito.mock(ProjectOpenApiService.class);
        scheduleCheckOmsProjectValve.completeHandler = Mockito.mock(OscTaskCompleteHandler.class);
        scheduleCheckOmsProjectValve.scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        scheduleCheckOmsProjectValve.onlineSchemaChangeProperties = Mockito.mock(OnlineSchemaChangeProperties.class);

    }

    @Test 
    public void testInvoke() {
        ValveContext valveContext = new OscValveContext();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId("123");
        projectRequest = new ProjectControlRequest();
        OnlineSchemaChangeScheduleTaskParameters parameters = new OnlineSchemaChangeScheduleTaskParameters();
        parameters.setUid("123");
        parameters.setOmsProjectId("123");
        valveContext.setScheduleTask(scheduleTask);
        valveContext.setTaskParameter(parameters);
        //Configure mock to return an appropriate response
        Mockito.when(scheduleCheckOmsProjectValve.projectOpenApiService.describeProjectSteps(Mockito.any(ProjectControlRequest.class))).thenReturn(null);
        
        Mockito.when(scheduleCheckOmsProjectValve.projectOpenApiService.describeProjectProgress(Mockito.any(ProjectControlRequest.class))).thenReturn(null);

        Mockito.when(scheduleCheckOmsProjectValve.projectOpenApiService.listProjectFullVerifyResult(Mockito.any(ListProjectFullVerifyResultRequest.class))).thenReturn(new ProjectFullVerifyResultResponse());

        Mockito.when(scheduleCheckOmsProjectValve.scheduleTaskRepository.update(Mockito.any(ScheduleTaskEntity.class))).thenReturn(null);
        
        //Run the method
        scheduleCheckOmsProjectValve.invoke(valveContext);      
   
    }
}

