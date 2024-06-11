
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValve;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;

public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetProjectRequest() {
        String uid = "test_uid";
        String projectId = "1234";

        OnlineSchemaChangeScheduleTaskParameters taskParam = new OnlineSchemaChangeScheduleTaskParameters();
        taskParam.setUid(uid);
        taskParam.setOmsProjectId(projectId);

        ProjectControlRequest actual = scheduleCheckOmsProjectValve.getProjectRequest(taskParam);
        Assert.assertEquals(uid, actual.getUid());
        Assert.assertEquals(projectId, actual.getId());
    }

    @Test
    public void testAdaptResult() {
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);

        when(projectStepResult.getFullTransferEstimatedCount()).thenReturn(100L);
        when(projectStepResult.getFullTransferFinishedCount()).thenReturn(50L);
        when(projectStepResult.getFullTransferProgressPercentage()).thenReturn(50.0);
        when(projectStepResult.getFullVerificationResult()).thenReturn(FullVerificationResult.CONSISTENT);
        when(projectStepResult.getFullVerificationResultDescription()).thenReturn("description");
        when(projectStepResult.getFullVerificationProgressPercentage()).thenReturn(70.0);
        when(projectStepResult.getCurrentStep()).thenReturn("step");
        when(projectStepResult.getCurrentStepStatus()).thenReturn(TaskStatus.RUNNING);
        when(projectStepResult.getPreCheckResult()).thenReturn(PrecheckResult.PASSED);

        scheduleCheckOmsProjectValve.adaptResult(result, projectStepResult);

        Assert.assertEquals(result.getFullTransferEstimatedCount(), new Long(100L));
        Assert.assertEquals(result.getFullTransferFinishedCount(), new Long(50L));
        Assert.assertEquals(result.getFullTransferProgressPercentage(), new Double(50.0));
        Assert.assertEquals(result.getFullVerificationResult(), FullVerificationResult.CONSISTENT);
        Assert.assertEquals(result.getFullVerificationResultDescription(), "description");
        Assert.assertEquals(result.getFullVerificationProgressPercentage(), new Double(70.0));
        Assert.assertEquals(result.getCurrentStep(), "step");
        Assert.assertEquals(result.getCurrentStepStatus(), TaskStatus.RUNNING);
        Assert.assertEquals(result.getPrecheckResult(), PrecheckResult.PASSED);
    }

    @Test
    public void testRecordCurrentProgress() {
        String projectId = "1234";
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();

        result.setCurrentStep("PRE_CHECK");
        result.setCurrentStepStatus(TaskStatus.DONE);
        result.setPrecheckResult(PrecheckResult.PASSED);

        scheduleCheckOmsProjectValve.recordCurrentProgress(projectId, result);

        result.setCurrentStep("FULL_TRANSFER");
        result.setCurrentStepStatus(TaskStatus.DONE);
        result.setFullTransferEstimatedCount(100L);
        result.setFullTransferFinishedCount(50L);
        result.setFullTransferProgressPercentage(50.0);

        scheduleCheckOmsProjectValve.recordCurrentProgress(projectId, result);

        result.setCurrentStep("FULL_VERIFIER");
        result.setCurrentStepStatus(TaskStatus.DONE);
        result.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        result.setFullVerificationResultDescription("description");
        result.setFullVerificationProgressPercentage(70.0);

        scheduleCheckOmsProjectValve.recordCurrentProgress(projectId, result);
    }
}
