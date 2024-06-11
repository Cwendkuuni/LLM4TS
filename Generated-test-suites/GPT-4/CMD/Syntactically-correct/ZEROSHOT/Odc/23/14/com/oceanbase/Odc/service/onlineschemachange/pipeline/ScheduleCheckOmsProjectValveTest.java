
package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableType;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleCheckOmsProjectValveTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    @Mock
    OscValveContext oscValveContext;

    @Mock
    OnlineSchemaChangeScheduleTaskParameters oscTaskParameters;

    @Mock
    ProjectFullVerifyResultResponse verifyResultResponse;

    List<ProjectStepVO> projectSteps = new ArrayList<>();

    @Before
    public void setup() {
        when(oscValveContext.getScheduleTask()).thenReturn(scheduleTaskEntity);
        when(oscValveContext.getTaskParameter()).thenReturn(oscTaskParameters);
        when(scheduleTaskRepository.update(any(ScheduleTaskEntity.class))).thenReturn(true);
    }

    @Test
    public void testInvoke () {
        when(oscTaskParameters.getUid()).thenReturn("testUid");
        ProjectControlRequest request = scheduleCheckOmsProjectValve.getProjectRequest(oscTaskParameters);
        ProjectProgressResponse progressResponse = new ProjectProgressResponse();

        projectSteps = new ArrayList<>();
        when(projectOpenApiService.describeProjectSteps(any())).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(any())).thenReturn(progressResponse);
        when(oscTaskParameters.getOmsProjectId()).thenReturn("one");
        when(oscTaskParameters.getDatabaseName()).thenReturn("db");
        when(scheduleCheckOmsProjectValve.listProjectFullVerifyResult(any(), any(), any())).thenReturn(verifyResultResponse);
        scheduleCheckOmsProjectValve.invoke(oscValveContext);

        when(verifyResultResponse.getStatus()).thenReturn(Arrays.asList("FINISHED", "SUSPEND", "RUNNING"));
        scheduleCheckOmsProjectValve.listProjectFullVerifyResult("one", "db", "testUid");
    }

    @Test
    public void testGetProjectRequest() {
        when(oscTaskParameters.getUid()).thenReturn("testUid");
        when(oscTaskParameters.getOmsProjectId()).thenReturn("testProjectId");
        ProjectControlRequest request = scheduleCheckOmsProjectValve.getProjectRequest(oscTaskParameters);
        assertEquals("testUid", request.getUid());
        assertEquals("testProjectId", request.getId());
    }

    @Test
    public void testAdaptResult() {
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(oscTaskParameters);
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setFullTransferEstimatedCount(100L);
        projectStepResult.setFullTransferFinishedCount(50L);
        projectStepResult.setFullTransferProgressPercentage(50F);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectStepResult.setFullVerificationResultDescription("testDescription");
        projectStepResult.setFullVerificationProgressPercentage(70F);

        scheduleCheckOmsProjectValve.adaptResult(result, projectStepResult);

        assertEquals((Long) 100L, result.getFullTransferEstimatedCount());
        assertEquals((Long) 50L, result.getFullTransferFinishedCount());
        assertEquals((Float) 50F, result.getFullTransferProgressPercentage());
        assertEquals(FullVerificationResult.CONSISTENT, result.getFullVerificationResult());
        assertEquals("testDescription", result.getFullVerificationResultDescription());
        assertEquals((Float) 70F, result.getFullVerificationProgressPercentage());
    }
}
