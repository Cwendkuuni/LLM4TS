
package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;

public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Mock
    private OscValveContext oscValveContext;
    
    @Mock
    private ProjectOpenApiService projectOpenApiService;
    
    @Mock
    private OscTaskCompleteHandler oscTaskCompleteHandler;
    
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        // cleanup after each test case
    }

    @Test
    public void testInvoke() {
        ProjectControlRequest projectControlRequest = new ProjectControlRequest();
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        List<ProjectStepVO> projectStepVOList = new ArrayList<>();
        ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest();

        when(oscValveContext.getScheduleTask()).thenReturn(scheduleTaskEntity);        
        when(oscValveContext.getTaskParameter()).thenReturn(taskParameter);
        when(projectOpenApiService.describeProjectSteps(projectControlRequest)).thenReturn(projectStepVOList);
        when(projectOpenApiService.describeProjectProgress(projectControlRequest)).thenReturn(new ProjectProgressResponse());
        when(projectOpenApiService.listProjectFullVerifyResult(request)).thenReturn(new ProjectFullVerifyResultResponse());
        when(scheduleTaskRepository.update(scheduleTaskEntity)).thenReturn(null);

        scheduleCheckOmsProjectValve.invoke(oscValveContext);

        verify(oscValveContext, times(1)).getScheduleTask();
        verify(projectOpenApiService, times(1)).describeProjectSteps(projectControlRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectControlRequest);
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(request);
        verify(scheduleTaskRepository, times(1)).update(scheduleTaskEntity);
    }

    @Test(expected = OscException.class)
    public void testInvokeException() {
        ProjectStepResult projectStepResult = new ProjectStepResult(TaskStatus.FAILED, null, null, null, null, PrecheckResult.FAILED, null);
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(oscValveContext, projectStepResult, new OnlineSchemaChangeScheduleTaskResult(), SwapTableType.AUTO_SWAP);
    }

    @Test
    public void testAdaptResult() {
        ProjectStepResult projectStepResult = new ProjectStepResult(TaskStatus.SUCCESS, null, 5000,
            10000, 50.0, PrecheckResult.PASSED, null);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        scheduleCheckOmsProjectValve.adaptResult(result, projectStepResult);
        assertEquals(result.getFullTransferEstimatedCount(), projectStepResult.getFullTransferEstimatedCount());
        assertEquals(result.getFullTransferFinishedCount(),projectStepResult.getFullTransferFinishedCount());
        assertEquals(result.getFullTransferProgressPercentage(), projectStepResult.getFullTransferProgressPercentage(), 0.0);
        assertEquals(result.getPrecheckResult(), projectStepResult.getPreCheckResult());
    }
}
