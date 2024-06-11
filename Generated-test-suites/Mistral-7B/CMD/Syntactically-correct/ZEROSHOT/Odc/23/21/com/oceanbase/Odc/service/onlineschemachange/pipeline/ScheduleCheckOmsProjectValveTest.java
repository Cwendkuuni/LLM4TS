package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

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
import com.oceanbase.odc.service.onlineschemachange.test.BaseValveTest;
import com.oceanbase.odc.service.onlineschemachange.test.TestComponent;

@TestComponent
public class ScheduleCheckOmsProjectValveTest extends BaseValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    private ProjectOpenApiService projectOpenApiService;
    private OscTaskCompleteHandler completeHandler;
    private ScheduleTaskRepository scheduleTaskRepository;
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        projectOpenApiService = mock(ProjectOpenApiService.class);
        completeHandler = mock(OscTaskCompleteHandler.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        onlineSchemaChangeProperties = mock(OnlineSchemaChangeProperties.class);

        when(projectOpenApiService.describeProjectSteps(any()))
                .thenReturn(getTestProjectSteps());
        when(projectOpenApiService.describeProjectProgress(any()))
                .thenReturn(getTestProjectProgress());
        when(projectOpenApiService.listProjectFullVerifyResult(any()))
                .thenReturn(getTestProjectFullVerifyResult());

        when(scheduleTaskRepository.update(any()))
                .thenReturn(getTestScheduleTask());
    }

    @Test
    public void testInvoke() throws Exception {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = getTestTaskParameter();
        ScheduleTaskEntity scheduleTask = getTestScheduleTask();
        when(scheduleTaskRepository.getOne(scheduleTask.getId()))
                .thenReturn(scheduleTask);

        OscValveContext oscValveContext = getTestValveContext(taskParameter, scheduleTask);
        scheduleCheckOmsProjectValve.invoke(oscValveContext);

        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(TaskStatus.DONE, result.getStatus());
    }

    @Test(expected = OscException.class)
    public void testInvokeWithError() throws Exception {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = getTestTaskParameter();
        ScheduleTaskEntity scheduleTask = getTestScheduleTask();
        when(scheduleTaskRepository.getOne(scheduleTask.getId()))
                .thenReturn(scheduleTask);

        OscValveContext oscValveContext = getTestValveContext(taskParameter, scheduleTask);

        // Simulate error from ProjectOpenApiService.describeProjectProgress
        when(projectOpenApiService.describeProjectProgress(any()))
                .thenThrow(new RuntimeException("Test error"));

        scheduleCheckOmsProjectValve.invoke(oscValveContext);

        fail("Expected OscException to be thrown");
    }

    private List<ProjectStepVO> getTestProjectSteps() {
        ProjectStepVO step1 = new ProjectStepVO();
        step1.setStepId("1");
        step1.setStepName("PreCheck");
        step1.setStatus("RUNNING");
        step1.setProgress(0);
        step1.setErrorMsg("");

        ProjectStepVO step2 = new ProjectStepVO();
        step2.setStepId("2");
        step2.setStepName("FullTransfer");
        step2.setStatus("RUNNING");
        step2.setProgress(50);
        step2.setErrorMsg("");

        ProjectStepVO step3 = new ProjectStepVO();
        step3.setStepId("3");
        step3.setStepName("FullVerifier");
        step3.setStatus("FAILED");
        step3.setProgress(0);
        step3.setErrorMsg("Test error");

        return Lists.newArrayList(step1, step2, step3);
    }

    private ProjectProgressResponse getTestProjectProgress() {
        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setCurrentStep("3");
        progress.setCurrentStepStatus("FAILED");
        progress.setCurrentStepProgress(0);
        progress.setCurrentStepErrorMsg("Test error");
        return progress;
    }

    private ProjectFullVerifyResultResponse getTestProjectFullVerifyResult() {
        ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();
        response.setResult(Lists.newArrayList(getTestFullVerifyResult()));
        return response;
    }

    private FullVerificationResult getTestFullVerifyResult() {
        FullVerificationResult result = new FullVerificationResult();
        result.setFullVerificationResult(PrecheckResult.SUCCESS);
        result.setFullVerificationResultDescription("Test success");
        return result;
    }

    private ScheduleTaskEntity getTestScheduleTask() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setResultJson("");
        scheduleTask.setStatus(TaskStatus.RUNNING.toString());
        return scheduleTask;
    }

    private OnlineSchemaChangeScheduleTaskParameters getTestTaskParameter() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId("test_project_id");
        taskParameter.setDatabaseName("test_database_name");
        taskParameter.setUid("test_uid");
        taskParameter.setSwapTableType(SwapTableType.AUTO);
        return taskParameter;
    }
}