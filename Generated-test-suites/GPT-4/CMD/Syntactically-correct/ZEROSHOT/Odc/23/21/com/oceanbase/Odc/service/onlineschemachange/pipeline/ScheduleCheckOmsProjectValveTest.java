
package com.oceanbase.odc.service.onlineschemachange.pipeline;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.model.*;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.*;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;

public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke() {
        OscValveContext oscValveContext = new OscValveContext();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        oscValveContext.setScheduleTask(scheduleTask);
        
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setDatabaseName("databaseName");
        taskParameter.setOmsProjectId("omsProjectId");
        taskParameter.setUid("uid");
        oscValveContext.setTaskParameter(taskParameter);

        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid("uid");
        projectRequest.setId("omsProjectId");

        List<ProjectStepVO> projectSteps = new ArrayList<>();
        when(projectOpenApiService.describeProjectSteps(any())).thenReturn(projectSteps);

        ProjectProgressResponse progress = new ProjectProgressResponse();
        when(projectOpenApiService.describeProjectProgress(any())).thenReturn(progress);

        ProjectStepResult projectStepResult = new ProjectStepResult();
        when(new ProjectStepResultChecker(any(), any(), any()).getCheckerResult()).thenReturn(projectStepResult);
        
        scheduleCheckOmsProjectValve.invoke(oscValveContext);
        
        Assert.assertEquals(TaskStatus.UNKNOWN, oscValveContext.getScheduleTask().getStatus());
        verify(scheduleTaskRepository).update(scheduleTask);
    }

    // Add more test methods to understand the behavior of the class under different conditions and scenarios.
}
