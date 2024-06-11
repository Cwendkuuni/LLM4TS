package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.PrecheckResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ListProjectFullVerifyResultRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.onlineschemachange.testutils.TestConstants;

@Slf4j
public class ScheduleCheckOmsProjectValveTest {

    @Mock
    private ProjectOpenApiService projectOpenApiService;
    @Mock
    private OscTaskCompleteHandler completeHandler;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    @Mock
    private DefaultTableFactory tableFactory;

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke() {
        // given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = TestConstants.getOnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = TestConstants.getScheduleTaskEntity();
        ProjectControlRequest projectRequest = TestConstants.getProjectControlRequest(taskParameter);
        List<ProjectStepVO> projectSteps = TestConstants.getProjectSteps();
        ProjectProgressResponse progress = TestConstants.getProjectProgressResponse();
        ProjectStepResult projectStepResult = TestConstants.getProjectStepResult();
        ProjectFullVerifyResultResponse fullVerifyResultResponse = TestConstants.getProjectFullVerifyResultResponse();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(Mockito.any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(fullVerifyResultResponse);

        // when
        scheduleCheckOmsProjectValve.invoke(TestConstants.getValveContext(scheduleTask, taskParameter));

        // then
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertNotNull(result);
        assertTrue(result.isManualSwapTableEnabled());
        assertNull(result.getManualSwapTableStarted());
        assertFalse(result.isManualSwapTableStarted());

        Optional<OnlineSchemaChangeScheduleTaskResult> lastResult = Optional.ofNullable(JsonUtils.fromJson(scheduleTask.getLastResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class));
        if (lastResult.isPresent()) {
            result.setManualSwapTableEnabled(lastResult.get().isManualSwapTableEnabled());
            result.setManualSwapTableStarted(lastResult.get().isManualSwapTableStarted());
        }

        assertNotNull(result.getFullVerificationResult());
        assertFalse(result.isManualSwapTableEnabled());
        assertFalse(result.isManualSwapTableStarted());
        assertNotNull(result.getFullTransferProgressPercentage());
        assertNotNull(result.getFullVerificationProgressPercentage());
        assertNull(result.getCurrentStep());
        assertNull(result.getCurrentStepStatus());
        assertNull(result.getPrecheckResult());
        assertNull(result.getPrecheckResultDescription());

        // verify
        verify(projectOpenApiService).describeProjectSteps(projectRequest);
        verify(projectOpenApiService).describeProjectProgress(projectRequest);
        verify(projectOpenApiService).listProjectFullVerifyResult(Mockito.any(ListProjectFullVerifyResultRequest.class));

        verify(projectOpenApiService).resumeProject(projectRequest);
        verify(scheduleTaskRepository).update(scheduleTask);
    }
}