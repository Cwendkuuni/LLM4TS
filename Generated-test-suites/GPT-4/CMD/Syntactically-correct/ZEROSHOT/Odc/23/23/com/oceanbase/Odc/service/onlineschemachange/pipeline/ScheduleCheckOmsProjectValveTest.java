package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ListProjectFullVerifyResultRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @Test
    public void testInvoke() {
        ValveContext valveContext = new OscValveContext();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
       
        when(context.getScheduleTask()).thenReturn(scheduleTask);
        
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        List<ProjectStepVO> projectSteps = new ArrayList<>();
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setId("1");
        projectRequest.setUid("1");
        
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        
        ProjectProgressResponse progress = new ProjectProgressResponse();
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        
        scheduleCheckOmsProjectValve.invoke(valveContext);
    }

    @Test
    public void testListProjectFullVerifyResult() {
        String projectId="xyz", databaseName="db_name", uid="uid123";
        
        ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest();
        request.setProjectId(projectId);
        request.setSourceSchemas(new String[] {databaseName});
        request.setDestSchemas(new String[] {databaseName});
        request.setStatus(new String[] {"FINISHED", "SUSPEND", "RUNNING"});
        request.setPageSize(10);
        request.setPageNumber(1);
        request.setUid(uid);
        
        ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();
        when(projectOpenApiService.listProjectFullVerifyResult(request)).thenReturn(response);
        
        assertEquals(response, scheduleCheckOmsProjectValve.listProjectFullVerifyResult(projectId, databaseName, uid));
    }
    
    // Similarly you can add test cases for other methods as well..
}
