package com.oceanbase.odc.service.onlineschemachange.pipeline;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.exception.OscException;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.common.util.tableformat.Table;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ListProjectFullVerifyResultRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResultChecker;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @Mock
    private ProjectStepResultChecker projectStepResultChecker;

    @Mock
    private ProjectStepVO projectStepVO;

    @Mock
    private ProjectProgressResponse projectProgressResponse;

    @Mock
    private ListProjectFullVerifyResultRequest listProjectFullVerifyResultRequest;

    @Mock
    private ProjectFullVerifyResultResponse projectFullVerifyResultResponse;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    @Mock
    private OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters;

    @Mock
    private OnlineSchemaChangeScheduleTaskResult onlineSchemaChangeScheduleTaskResult;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testInvoke() {
        // Given
        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(Lists.newArrayList(projectStepVO));
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(projectProgressResponse);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);
        when(projectStepResultChecker.getCheckerResult(any(), any(), any()))
                .thenReturn(new ProjectStepResult(TaskStatus.DONE, FullVerificationResult.CONSISTENT));

        UUID uuid = UUID.randomUUID();
        OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        onlineSchemaChangeScheduleTaskParameters.setUid(uuid.toString());
        onlineSchemaChangeScheduleTaskParameters.setOmsProjectId("testProjectId");
        OnlineSchemaChangeScheduleTaskResult onlineSchemaChangeScheduleTaskResult = new OnlineSchemaChangeScheduleTaskResult();
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(onlineSchemaChangeScheduleTaskResult));

        // When
        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        // Then
        Assert.assertNull(scheduleCheckOmsProjectValve.getNext());
    }

    // Add more test cases for different scenarios here

    @Test(expected = OscException.class)
    public void testInvoke_preCheckFailed() {
        // Given
        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(Lists.newArrayList(projectStepVO));
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(projectProgressResponse);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);
        when(projectStepResultChecker.getCheckerResult(any(), any(), any()))
                .thenReturn(new ProjectStepResult(TaskStatus.FAILED, PrecheckResult.FAILED, "Error Message"));

        UUID uuid = UUID.randomUUID();
        OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        onlineSchemaChangeScheduleTaskParameters.setUid(uuid.toString());
        onlineSchemaChangeScheduleTaskParameters.setOmsProjectId("testProjectId");
        OnlineSchemaChangeScheduleTaskResult onlineSchemaChangeScheduleTaskResult = new OnlineSchemaChangeScheduleTaskResult();
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(onlineSchemaChangeScheduleTaskResult));

        // When & Then
        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));
        Assert.fail("Precheck failed but no exception thrown");
    }

    // Add more test cases for different scenarios here
}