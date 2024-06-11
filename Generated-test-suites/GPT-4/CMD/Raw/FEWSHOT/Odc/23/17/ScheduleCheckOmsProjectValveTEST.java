
package com.oceanbase.odc.service.onlineschemachange.pipeline;

import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ListProjectFullVerifyResultRequest;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;

public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Mock
    private ProjectOpenApiService projectOpenApiService;
    
    @Mock
    private OscTaskCompleteHandler completeHandler;
    
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke() {
        OscValveContext context = mock(OscValveContext.class);
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        OnlineSchemaChangeScheduleTaskParameters taskParameter = mock(OnlineSchemaChangeScheduleTaskParameters.class);

        when(context.getScheduleTask()).thenReturn(scheduleTask);
        when(context.getTaskParameter()).thenReturn(taskParameter);
        
        scheduleCheckOmsProjectValve.invoke(context);

        verify(context, times(1)).getScheduleTask();
        verify(context, times(1)).getTaskParameter();
    }
    
    @Test
    public void testProjectControlRequest() {
        OnlineSchemaChangeScheduleTaskParameters taskParam = mock(OnlineSchemaChangeScheduleTaskParameters.class);

        when(taskParam.getUid()).thenReturn("testUid");
        when(taskParam.getOmsProjectId()).thenReturn("testId");

        ProjectControlRequest pcr = scheduleCheckOmsProjectValve.getProjectRequest(taskParam);

        assertEquals("testUid", pcr.getUid());
        assertEquals("testId", pcr.getId());
    }

    @Test
    public void testListProjectFullVerifyResult() {
        String projectId = "projectId";
        String databaseName = "databaseName";
        String uid = "uid";

        ListProjectFullVerifyResultRequest lpvrr = scheduleCheckOmsProjectValve.listProjectFullVerifyResult(projectId, databaseName, uid);

        assertEquals(projectId, lpvrr.getProjectId());
        assertEquals(databaseName, lpvrr.getSourceSchemas()[0]);
        assertEquals(databaseName, lpvrr.getDestSchemas()[0]);
        assertEquals(uid, lpvrr.getUid());
    }
}
