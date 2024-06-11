package com.oceanbase.odc.service.onlineschemachange.pipeline;

import com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValve;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.pipeline.OscValveContext;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Mock
    OscValveContext valveContext;
    
    @Mock
    ScheduleTaskEntity scheduleTask;

    @Mock
    OnlineSchemaChangeScheduleTaskParameters taskParameter;
    
    @Mock
    ProjectControlRequest projectRequest;
    
    @Mock
    List<ProjectStepVO> projectSteps;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testInvoke() {
        when(valveContext.getScheduleTask()).thenReturn(scheduleTask);
        when(valveContext.getTaskParameter()).thenReturn(taskParameter);
        when(scheduleCheckOmsProjectValve.getProjectRequest(taskParameter)).thenReturn(projectRequest);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(scheduleTask.getResultJson()).thenReturn("json");
        doNothing().when(scheduleTaskRepository).update(scheduleTask);
        scheduleCheckOmsProjectValve.invoke(valveContext);
        verify(scheduleTask, times(1)).setId(ID);
    }

    @Test
    public void testGetProjectRequest() {
        OnlineSchemaChangeScheduleTaskParameters taskParam = new OnlineSchemaChangeScheduleTaskParameters();
        taskParam.setUid("uid");
        taskParam.setOmsProjectId("id");
        ProjectControlRequest result = scheduleCheckOmsProjectValve.getProjectRequest(taskParam);
        assertEquals("uid", result.getUid());
        assertEquals("id", result.getId());
    }
    
    // Ideally, there should be more test methods here to cover all the other methods in your source class...
}

