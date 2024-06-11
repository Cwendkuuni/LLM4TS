import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.constant.ErrorCodes;
import com.oceanbase.odc.common.model.JsonResult;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.oms.ProjectOpenApiServiceImpl;
import com.oceanbase.odc.service.onlineschemachange.oms.ProjectStepResultChecker;
import com.oceanbase.odc.service.onlineschemachange.oms.model.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.model.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.model.ProjectStepVO;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValve;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValve.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResultCheckerImpl;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("unchecked")
public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;
    private ProjectOpenApiServiceImpl projectOpenApiServiceMock;
    private ProjectStepResultChecker projectStepResultCheckerMock;
    private OnlineSchemaChangeScheduleTaskParameters taskParameter;
    private ScheduleTaskEntity scheduleTask;
    private ProjectStepResult projectStepResult;
    private ProjectFullVerifyResultResponse projectFullVerifyResultResponseMock;
    private ProjectProgressResponse projectProgressResponseMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        projectOpenApiServiceMock = Mockito.mock(ProjectOpenApiServiceImpl.class);
        projectStepResultCheckerMock = new ProjectStepResultCheckerImpl();
        scheduleCheckOmsProjectValve.setProjectStepResultChecker(projectStepResultCheckerMock);
        scheduleCheckOmsProjectValve.setProjectOpenApiService(projectOpenApiServiceMock);
        scheduleCheckOmsProjectValve.setOscTaskCompleteHandler(Mockito.mock(OscTaskCompleteHandler.class));
        scheduleCheckOmsProjectValve.setScheduleTaskRepository(Mockito.mock(ScheduleTaskRepository.class));
        scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setStatus(TaskStatus.NOT_STARTED.name());
        taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setDatabaseName("testDatabaseName");
        taskParameter.setOmsProjectId("testProjectId");
        taskParameter.setSwapTableType(SwapTableType.AUTO);
        taskParameter.setUid("testUid");
    }

    @Test
    public void testInvoke_Success() throws Exception {
        ProjectControlRequest projectRequest = Mockito.mock(ProjectControlRequest.class);
        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setCurrentStep(new ProjectStepVO());
        projectProgressResponseMock = progress;
        ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();
        projectFullVerifyResultResponseMock = response;
        Mockito.when(projectOpenApiServiceMock.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectProgressResponseMock));
        Mockito.when(projectOpenApiServiceMock.describeProjectProgress(projectRequest)).thenAnswer(new Answer<ProjectProgressResponse>() {
            @Override
            public ProjectProgressResponse answer(InvocationOnMock invocation) throws Throwable {
                return projectProgressResponseMock;
            }
        });
        Mockito.when(projectOpenApiServiceMock.listProjectFullVerifyResult(Mockito.any()))
                .thenReturn(projectFullVerifyResultResponseMock);

        projectStepResult = new ProjectStepResult();
        projectStepResult.setCurrentStep(new ProjectStepVO());
        projectStepResult.setCurrentStepStatus(TaskStatus.DONE.name());
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectStepResult.setFullVerificationResultDescription("success");

        projectStepResultCheckerMock.setCheckerVerifyResult(() -> projectFullVerifyResultResponseMock);
        projectStepResultCheckerMock.setResumeProject(() -> {});

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        scheduleCheckOmsProjectValve.invoke(new OscValveContext() {
            @Override
            public ScheduleTaskEntity getScheduleTask() {
                return scheduleTask;
            }

            @Override
            public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
                return taskParameter;
            }
        });

        Assert.assertEquals(TaskStatus.DONE.name(), scheduleTask.getStatus());
    }

    @Test(expected = OscException.class)
    public void testInvoke_PreCheckFailed() throws Exception {
        projectStepResult = new ProjectStepResult();
        projectStepResult.setCurrentStep(new ProjectStepVO());
        projectStepResult.setCurrentStepStatus(TaskStatus.FAILED.name());
        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);
        projectStepResult.setErrorMsg("PreCheck Failed");

        projectStepResultCheckerMock.setCheckerVerifyResult(() -> projectFullVerifyResultResponseMock);
        projectStepResultCheckerMock.setResumeProject(() -> {});

        scheduleCheckOmsProjectValve.invoke(new OscValveContext() {
            @Override
            public ScheduleTaskEntity getScheduleTask() {
                return scheduleTask;
            }

            @Override
            public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
                return taskParameter;
            }
        });

        Mockito.verify(projectOpenApiServiceMock).resumeProject(Mockito.any(ProjectControlRequest.class));
        Throwable exception = Mockito.capture(projectStepResultCheckerMock.getCheckerException());
        Assert.assertEquals("PreCheck Failed", exception.getMessage());
    }

    @Test(expected = OscException.class)
    public void testInvoke_DataInconsistent() throws Exception {
        projectStepResult = new ProjectStepResult();
        projectStepResult.setCurrentStep(new ProjectStepVO());
        projectStepResult.setCurrentStepStatus(TaskStatus.FAILED.name());
        projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);

        projectStepResultCheckerMock.setCheckerVerifyResult(() -> projectFullVerifyResultResponseMock);
        projectStepResultCheckerMock.setResumeProject(() -> {});

        scheduleCheckOmsProjectValve.invoke(new OscValveContext() {
            @Override
            public ScheduleTaskEntity getScheduleTask() {
                return scheduleTask;
            }

            @Override
            public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
                return taskParameter;
            }
        });

        Mockito.verify(projectOpenApiServiceMock).resumeProject(Mockito.any(ProjectControlRequest.class));
        throw new OscException(ErrorCodes.OmsDataCheckInconsistent,
                "Task failed for origin table has inconsistent data with new table, result: "
                        + projectStepResult.getFullVerificationResultDescription());
    }

    @Test
    public void testInvoke_HandleOmsProjectStepResult() throws Exception {
        ProjectControlRequest projectRequest = Mockito.mock(ProjectControlRequest.class);
        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setCurrentStep(new ProjectStepVO());
        projectProgressResponseMock = progress;
        ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();
        projectFullVerifyResultResponseMock = response;

        projectStepResult = new ProjectStepResult();
        projectStepResult.setCurrentStep(new ProjectStepVO());
        projectStepResult.setCurrentStepStatus(TaskStatus.DONE.name());
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        projectStepResultCheckerMock.setCheckerVerifyResult(() -> projectFullVerifyResultResponseMock);
        projectStepResultCheckerMock.setResumeProject(() -> {});

        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(new ValveContext() {
            @Override
            public ScheduleTaskEntity getScheduleTask() {
                return scheduleTask;
            }

            @Override
            public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
                return taskParameter;
            }
        }, projectStepResult, new OnlineSchemaChangeScheduleTaskResult(taskParameter), SwapTableType.AUTO, scheduleTask);

        Mockito.verify(projectOpenApiServiceMock, Mockito.times(1))
                .resumeProject(projectRequest);

        Mockito.verify(projectOpenApiServiceMock, Mockito.never())
                .listProjectFullVerifyResult(Mockito.any());

        Assert.assertEquals(true, scheduleTask.isResultJsonPresent());

        String resultJson = scheduleTask.getResultJson();
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(resultJson, OnlineSchemaChangeScheduleTaskResult.class);
        Assert.assertTrue(result.isManualSwapTableEnabled());
        Assert.assertFalse(result.isManualSwapTableStarted());
    }

    @Test
    public void testInvoke_ContinueHandleProjectStepResult() throws Exception {
        projectStepResult = new ProjectStepResult();
        projectStepResult.setCurrentStep(new ProjectStepVO());
        projectStepResult.setCurrentStepStatus(TaskStatus.FAILED.name());
        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);

        projectStepResultCheckerMock.setCheckerVerifyResult(() -> projectFullVerifyResultResponseMock);
        projectStepResultCheckerMock.setResumeProject(() -> {});

        scheduleCheckOmsProjectValve.continueHandleProjectStepResult(projectStepResult);

        Mockito.verify(projectOpenApiServiceMock).resumeProject(projectRequest);
    }

    @Test
    public void testListProjectFullVerifyResult() throws Exception {
        String projectId = "testProjectId";
        String databaseName = "testDatabaseName";
        String uid = UUID.randomUUID().toString();

        ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest();
        request.setProjectId(projectId);
        request.setSourceSchemas(new String[] {databaseName});
        request.setDestSchemas(new String[] {databaseName});
        request.setStatus(new String[] {"FINISHED", "SUSPEND", "RUNNING"});
        request.setPageSize(10);
        request.setPageNumber(1);
        request.setUid(uid);

        ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();
        List<ProjectFullVerifyResultResponse.ProjectFullVerifyResultItem> items = Lists.newArrayList();
        ProjectFullVerifyResultResponse.ProjectFullVerifyResultItem item = new ProjectFullVerifyResultResponse.ProjectFullVerifyResultItem();
        item.setStatus("FINISHED");
        item.setDatabase("testDatabaseName");
        item.setSchema("testSchemaName");
        item.setTable("testTableName");
        item.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        items.add(item);
        response.setItems(items);

        Mockito.when(projectOpenApiServiceMock.listProjectFullVerifyResult(request)).thenReturn(response);

        List<ProjectFullVerifyResultResponse.ProjectFullVerifyResultItem> result = scheduleCheckOmsProjectValve
                .listProjectFullVerifyResult(projectId, databaseName, uid);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals(FullVerificationResult.CONSISTENT, result.get(0).getFullVerificationResult());
    }
}