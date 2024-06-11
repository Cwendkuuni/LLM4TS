package com.oceanbase.odc.service.onlineschemachange.pipeline;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
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
import com.oceanbase.odc.service.onlineschemachange.testutils.ProjectOpenApiServiceTestUtils;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@Slf4j
public class ScheduleCheckOmsProjectValveTest {
    @InjectMocks
    private ScheduleCheckOmsProjectValve valve;
    private ProjectOpenApiService projectOpenApiServiceMock;
    private OnlineSchemaChangeProperties onlineSchemaChangePropertiesMock;
    private ScheduleTaskRepository scheduleTaskRepositoryMock;
    private OscTaskCompleteHandler completeHandlerMock;

    private OnlineSchemaChangeScheduleTaskParameters taskParameter;
    private ScheduleTaskEntity scheduleTask;
    private ProjectControlRequest projectRequest;
    private ProjectFullVerifyResultResponse projectFullVerifyResultResponse;
    private ProjectProgressResponse projectProgressResponse;
    private ListProjectFullVerifyResultRequest listProjectFullVerifyResultRequest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        projectOpenApiServiceMock = Mockito.mock(ProjectOpenApiService.class);
        onlineSchemaChangePropertiesMock = Mockito.mock(OnlineSchemaChangeProperties.class);
        scheduleTaskRepositoryMock = Mockito.mock(ScheduleTaskRepository.class);
        completeHandlerMock = Mockito.mock(OscTaskCompleteHandler.class);
        valve.setProjectOpenApiService(projectOpenApiServiceMock);
        valve.setOnlineSchemaChangeProperties(onlineSchemaChangePropertiesMock);
        valve.setScheduleTaskRepository(scheduleTaskRepositoryMock);
        valve.setCompleteHandler(completeHandlerMock);

        taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId("testProjectId");
        taskParameter.setDatabaseName("testDatabaseName");
        taskParameter.setUid(UUID.randomUUID().toString());

        scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setResultJson("");
        scheduleTask.setStatus(TaskStatus.INIT.name());

        projectRequest = new ProjectControlRequest();
        projectRequest.setId("testProjectId");
        projectRequest.setUid(taskParameter.getUid());

        projectFullVerifyResultResponse = ProjectOpenApiServiceTestUtils.createProjectFullVerifyResultResponse();
        projectProgressResponse = ProjectOpenApiServiceTestUtils.createProjectProgressResponse();
        listProjectFullVerifyResultRequest = ProjectOpenApiServiceTestUtils.createListProjectFullVerifyResultRequest();

        when(projectOpenApiServiceMock.describeProjectSteps(projectRequest)).thenReturn(
                ProjectOpenApiServiceTestUtils.createProjectStepVOList());
        when(projectOpenApiServiceMock.describeProjectProgress(projectRequest)).thenReturn(projectProgressResponse);
        when(projectOpenApiServiceMock.resumeProject(projectRequest)).thenReturn(null);
        when(projectOpenApiServiceMock.listProjectFullVerifyResult(listProjectFullVerifyResultRequest)).thenReturn(
                projectFullVerifyResultResponse);
    }

    @Test
    public void testInvoke_preCheckFailed_shouldThrowOscException() {
        // given
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setCurrentStep("PRE_CHECK");
        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);

        // when
        Mockito.doReturn(projectStepResult).when(valve).new ProjectStepResultChecker(projectProgressResponse,
                ProjectOpenApiServiceTestUtils.createProjectStepVOList(),
                onlineSchemaChangePropertiesMock.isEnableFullVerify())
                        .getCheckerResult();

        // then
        Assert.assertThrows(OscException.class, () -> valve.invoke(Mockito.mock(ValveContext.class)));
    }

    @Test
    public void testInvoke_fullVerificationInconsistent_shouldThrowOscException() {
        // given
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setCurrentStep("FULL_VERIFIER");
        projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);

        // when
        Mockito.doReturn(projectStepResult).when(valve).new ProjectStepResultChecker(projectProgressResponse,
                ProjectOpenApiServiceTestUtils.createProjectStepVOList(),
                onlineSchemaChangePropertiesMock.isEnableFullVerify())
                        .getCheckerResult();

        // then
        Assert.assertThrows(OscException.class, () -> valve.invoke(Mockito.mock(ValveContext.class)));
    }

    @Test
    public void testInvoke_fullTransferCompleted_shouldUpdateScheduleTask() {
        // given
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setCurrentStep("FULL_TRANSFER");
        projectStepResult.setFullTransferEstimatedCount(100);
        projectStepResult.setFullTransferFinishedCount(100);
        projectStepResult.setFullTransferProgressPercentage(100);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        // when
        Mockito.doReturn(projectStepResult).when(valve).new ProjectStepResultChecker(projectProgressResponse,
                ProjectOpenApiServiceTestUtils.createProjectStepVOList(),
                onlineSchemaChangePropertiesMock.isEnableFullVerify())
                        .getCheckerResult();

        OnlineSchemaChangeScheduleTaskResult taskResult = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        OnlineSchemaChangeScheduleTaskResult lastResult = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        lastResult.setManualSwapTableEnabled(false);
        lastResult.setManualSwapTableStarted(false);
        when(scheduleTaskRepositoryMock.update(Mockito.any(ScheduleTaskEntity.class))).then(invocation -> {
            ScheduleTaskEntity scheduledTask = invocation.getArgument(0);
            scheduledTask.setResultJson(JsonUtils.toJson(taskResult));
            scheduledTask.setProgressPercentage(projectStepResult.getTaskPercentage());
            return scheduledTask;
        });

        // then
        valve.invoke(Mockito.mock(ValveContext.class));

        ScheduleTaskEntity updatedScheduleTask = scheduleTaskRepositoryMock.findById(1L);
        OnlineSchemaChangeScheduleTaskResult updatedScheduleTaskResult = JsonUtils.fromJson(updatedScheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        Assert.assertEquals(taskResult.getManualSwapTableEnabled(), updatedScheduleTaskResult.getManualSwapTableEnabled());
        Assert.assertEquals(taskResult.getManualSwapTableStarted(), updatedScheduleTaskResult.getManualSwapTableStarted());
        Assert.assertEquals(projectStepResult.getFullTransferEstimatedCount(), updatedScheduleTaskResult.getFullTransferEstimatedCount());
        Assert.assertEquals(projectStepResult.getFullTransferFinishedCount(), updatedScheduleTaskResult.getFullTransferFinishedCount());
        Assert.assertEquals(projectStepResult.getFullTransferProgressPercentage(), updatedScheduleTaskResult.getFullTransferProgressPercentage());
        Assert.assertEquals(projectStepResult.getFullVerificationResult(), updatedScheduleTaskResult.getFullVerificationResult());
    }

    @Test
    public void testInvoke_fullTransferInProgress_shouldContinueHandleProjectStepResult() {
        // given
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setCurrentStep("FULL_TRANSFER");
        projectStepResult.setFullTransferEstimatedCount(100);
        projectStepResult.setFullTransferFinishedCount(50);
        projectStepResult.setFullTransferProgressPercentage(50);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        // when
        Mockito.doReturn(projectStepResult).when(valve).new ProjectStepResultChecker(projectProgressResponse,
                ProjectOpenApiServiceTestUtils.createProjectStepVOList(),
                onlineSchemaChangePropertiesMock.isEnableFullVerify())
                        .getCheckerResult();

        // then
        valve.invoke(Mockito.mock(ValveContext.class));

        // verify
        Mockito.verify(valve).continueHandleProjectStepResult(projectStepResult);
    }

    @Test
    public void testInvoke_noProjectStep_shouldInvokeNextValve() {
        // given
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setCurrentStep("UNKNOWN");

        // when
        Mockito.doReturn(projectStepResult).when(valve).new ProjectStepResultChecker(projectProgressResponse,
                ProjectOpenApiServiceTestUtils.createProjectStepVOList(),
                onlineSchemaChangePropertiesMock.isEnableFullVerify())
                        .getCheckerResult();

        // then
        valve.invoke(Mockito.mock(ValveContext.class));

        // verify
        Mockito.verify(valve).getNext().invoke(Mockito.any(ValveContext.class));
    }
}