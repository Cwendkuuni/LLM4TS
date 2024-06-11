package com.oceanbase.odc.service.onlineschemachange.pipeline;

import com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValve;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ValveContext;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import com.oceanbase.odc.service.onlineschemachange.pipeline.OscValveContext;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import java.util.List;

public class ScheduleCheckOmsProjectValveTest {

    @Mock
    OscValveContext context;
    @Mock
    ScheduleTaskEntity scheduleTask;
    @Mock
    ProjectOpenApiService projectOpenApiService;
    @Mock
    ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    OnlineSchemaChangeScheduleTaskResult result;
    @Mock
    ProjectStepResult projectStepResult;
    @Mock
    ProjectProgressResponse progress;
    @Mock
    OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    @Mock
    ProjectStepResultChecker projectStepResultChecker;
    @Mock
    ValveContext valveContext;
    @Mock
    OscTaskCompleteHandler completeHandler;

    @InjectMocks
    ScheduleCheckOmsProjectValve valve;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvokeMethod() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        when(context.getTaskParameter()).thenReturn(taskParameter);
        when(taskParameter.getOmsProjectId()).thenReturn("projectId");
        when(taskParameter.getDatabaseName()).thenReturn("databaseName");
        when(taskParameter.getUid()).thenReturn("uid");

        List<ProjectStepVO> projectSteps = mock(List.class);
        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class))).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class))).thenReturn(progress);
        
        when(projectStepResultChecker.withCheckerVerifyResult(any()).withResumeProject(any()).getCheckerResult()).thenReturn(projectStepResult);

        valve.invoke(context);

        verify(scheduleTaskRepository, times(2)).update(scheduleTask);
        verify(context, times(1)).getTaskParameter();
    }

    @Test(expected = OscException.class)
    public void testHandleOmsProjectStepResultWithException() {
        when(projectStepResult.getPreCheckResult()).thenReturn(PrecheckResult.FAILED);
        valve.handleOmsProjectStepResult(valveContext, projectStepResult, result, SwapTableType.SWAP_ON_CUTOVER, scheduleTask);
    }
    
    @Test
    public void testHandleOmsProjectStepResultWithoutException() {
        boolean isEnableSwapTable = SwapTableUtil.isSwapTableEnable(SwapTableType.SWAP_ON_CUTOVER,
                    scheduleTask.getStatus(), result.getFullTransferProgressPercentage(),
                    result.getFullVerificationResult(), result.isManualSwapTableStarted());
        assertFalse(isEnableSwapTable);

        valve.invoke(context);

        verify(scheduleTaskRepository, times(2)).update(scheduleTask);
    }
}
