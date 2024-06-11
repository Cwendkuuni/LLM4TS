
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;

public class ScheduleCheckOmsProjectValveTest {
    @InjectMocks
    ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Mock
    ProjectOpenApiService projectOpenApiService;

    @Mock
    OscTaskCompleteHandler completeHandler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke() {
        OscValveContext context = new OscValveContext();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ArrayList<ProjectStepVO> projectSteps = new ArrayList<>();
        ProjectProgressResponse progress = new ProjectProgressResponse();
        projectSteps.add(new ProjectStepVO());

        when(projectOpenApiService.describeProjectSteps(new ProjectControlRequest())).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(new ProjectControlRequest())).thenReturn(progress);
        
        scheduleCheckOmsProjectValve.invoke(context);

        verify(projectOpenApiService, times(1)).describeProjectSteps(new ProjectControlRequest());
        verify(projectOpenApiService, times(1)).describeProjectProgress(new ProjectControlRequest());
    }

    @Test
    public void testHandleOmsProjectStepResult() {
        ValveContext valveContext = new ValveContext();
        ProjectStepResult projectStepResult = new ProjectStepResult();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();

        projectStepResult.setTaskStatus(TaskStatus.DONE);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(valveContext, projectStepResult, result, SwapTableType.EXCHANGE, scheduleTask);

        assertEquals(true, result.isManualSwapTableEnabled());
    }

    @Test
    public void testAdaptResult() {
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        ProjectStepResult projectStepResult = new ProjectStepResult();

        scheduleCheckOmsProjectValve.adaptResult(result, projectStepResult);

        assertEquals(projectStepResult.getFullTransferEstimatedCount(), result.getFullTransferEstimatedCount());
        assertEquals(projectStepResult.getFullTransferFinishedCount(), result.getFullTransferFinishedCount());
    }
}
