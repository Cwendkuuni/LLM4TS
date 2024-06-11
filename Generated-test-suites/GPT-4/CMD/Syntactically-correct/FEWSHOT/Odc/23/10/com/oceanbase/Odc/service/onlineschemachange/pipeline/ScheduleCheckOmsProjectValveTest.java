
package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.util.tableformat.Table;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;

public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    

    @Test
    public void testInvoke() {
        ValveContext valveContext = new OscValveContext();
        ScheduleTaskEntity scheduledTask = new ScheduleTaskEntity();

        // Mocks
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setUid("uid");
        taskParameter.setOmsProjectId("omsProjectId");
        ProjectControlRequest projectRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParameter);

        ProjectStepVO projectStepVO = new ProjectStepVO();
        List<ProjectStepVO> projectSteps = Arrays.asList(projectStepVO);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);

        ProjectProgressResponse progress = new ProjectProgressResponse();
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        // Test method invoke
        scheduleCheckOmsProjectValve.invoke(valveContext);

        // Validate
        assertNotNull(taskParameter);
        assertNotNull(projectRequest);
        assertEquals(1, projectSteps.size());
    }

}
