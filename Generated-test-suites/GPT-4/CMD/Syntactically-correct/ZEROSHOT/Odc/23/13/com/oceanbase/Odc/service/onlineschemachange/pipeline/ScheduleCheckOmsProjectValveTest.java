package com.oceanbase.odc.service.onlineschemachange.pipeline;

import com.oceanbase.odc.service.onlineschemachange.model.*;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ListProjectFullVerifyResultRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleCheckOmsProjectValveTest {

    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    private ValveContext valveContext;

    @Before
    public void setUp() {
        OscValveContext oscValveContext = new OscValveContext();
        valveContext = oscValveContext;
    }

    @Test
    public void testInvoke() {
        scheduleCheckOmsProjectValve.invoke(valveContext);
        verify(projectOpenApiService, times(1)).describeProjectSteps(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).describeProjectProgress(any(ProjectControlRequest.class));
    }

    @Test
    public void testGetProjectRequest() {
        OnlineSchemaChangeScheduleTaskParameters taskParams = new OnlineSchemaChangeScheduleTaskParameters();
        taskParams.setUid("123");
        taskParams.setOmsProjectId("456");
        ProjectControlRequest request = scheduleCheckOmsProjectValve.getProjectRequest(taskParams);
        assert("123".equals(request.getUid()));
        assert("456".equals(request.getId()));
    }

    @Test
    public void testListProjectFullVerifyResult() {
        ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();
        doReturn(response).when(projectOpenApiService).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));

        ProjectFullVerifyResultResponse actualResponse = scheduleCheckOmsProjectValve.listProjectFullVerifyResult("1", "testDB", "123");
        assert(actualResponse != null);
    }

    @Test
    public void testAdaptResult() {
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        ProjectStepResult projectStepResult = new ProjectStepResult();

        scheduleCheckOmsProjectValve.adaptResult(result, projectStepResult);

        assert(result.getPrecheckResult() == projectStepResult.getPreCheckResult());
        assert(result.getCurrentStepStatus() == projectStepResult.getCurrentStepStatus());
        assert(result.getFullVerificationResult() == projectStepResult.getFullVerificationResult());
    }

    @Test
    public void testGetHeader() {
        List<String> list = scheduleCheckOmsProjectValve.getHeader();
        assert(Arrays.equals(list.toArray(), new String[]{"ProjectId", "Step", "Status", "Progress", "Description"}));
    }
}
