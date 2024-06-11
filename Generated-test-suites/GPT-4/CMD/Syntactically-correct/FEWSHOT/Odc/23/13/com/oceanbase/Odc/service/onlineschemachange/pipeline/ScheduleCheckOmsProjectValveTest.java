package com.oceanbase.odc.service.onlineschemachange.pipeline;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ScheduleCheckOmsProjectValveTest {

    @Mock
    ProjectOpenApiService projectOpenApiService;

    @Mock
    OscTaskCompleteHandler completeHandler;

    @Mock
    ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @InjectMocks
    ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke() {
        OscValveContext valveContext = mock(OscValveContext.class);
        when(valveContext.getScheduleTask()).thenReturn(new ScheduleTaskEntity());
        scheduleCheckOmsProjectValve.invoke(valveContext);
    }

    @Test
    public void testHandleOmsProjectStepResult() {
        ValveContext valveContext = mock(ValveContext.class);
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setTaskStatus(TaskStatus.DONE);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        OnlineSchemaChangeScheduleTaskResult result = mock(OnlineSchemaChangeScheduleTaskResult.class);
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(valveContext, projectStepResult, result, SwapTableType.TABLE_NAME, 
          scheduleTask);
    }

    @Test
    public void testGetProjectRequest() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setUid("testUid");
        taskParameter.setOmsProjectId("testOmsProjectId");
        ProjectControlRequest expected = new ProjectControlRequest();
        expected.setUid("testUid");
        expected.setId("testOmsProjectId");
        assertEquals(expected, scheduleCheckOmsProjectValve.getProjectRequest(taskParameter));
    }
  
    @Test
    public void testListProjectFullVerifyResult() {
        String projectId = "testProjectId";
        String databaseName = "testDatabaseName";
        String uid = "testUid";
        ProjectFullVerifyResultResponse result = scheduleCheckOmsProjectValve.listProjectFullVerifyResult(projectId, databaseName, uid);
        assertEquals(result, projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)));
    }  
}