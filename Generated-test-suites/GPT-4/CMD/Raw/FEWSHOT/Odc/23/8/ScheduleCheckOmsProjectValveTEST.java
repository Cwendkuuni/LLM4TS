package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;

public class ScheduleCheckOmsProjectValveTest {
    
    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    
    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;
    
    private ScheduleTaskEntity scheduleTask;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        scheduleTask = new ScheduleTaskEntity();
    }

    @Test
    public void testInvoke() {
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid("1");
        projectRequest.setId("1");
        
        // Create mock data
        List<ProjectStepVO> steps = new ArrayList<>();
        steps.add(new ProjectStepVO());
       
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(steps);
        
        // Run the method and check results
        scheduleCheckOmsProjectValve.invoke(new OscValveContext(scheduleTask));
        
        // Add your assertions here to validate the result
    }

    @Test
    public void testListProjectFullVerifyResult() {
        ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest();
        request.setProjectId("1");
        request.setSourceSchemas(new String[] {"database1"});
        request.setDestSchemas(new String[] {"database2"});
        request.setStatus(new String[] {"FINISHED", "SUSPEND", "RUNNING"});
        request.setPageSize(10);
        request.setPageNumber(1);
        request.setUid("1");

        ProjectFullVerifyResultResponse response = scheduleCheckOmsProjectValve.listProjectFullVerifyResult("1", "database1", "1");

        assertNotNull(response);
    }

    // Add more test cases as needed to fully test ScheduleCheckOmsProjectValve
}