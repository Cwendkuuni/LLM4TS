package com.oceanbase.odc.service.onlineschemachange.pipeline;

import com.google.common.collect.Lists;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ListProjectFullVerifyResultRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@Slf4j
public class ScheduleCheckOmsProjectValveTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    private ScheduleCheckOmsProjectValve valve;

    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @Before
    public void initMocks() {
        given(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .willReturn(Lists.newArrayList(createProjectStepVO()));
        given(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .willReturn(createProjectProgressResponse());
        given(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .willReturn(createProjectFullVerifyResultResponse());
        given(scheduleTaskRepository.update(any(ScheduleTaskEntity.class)))
                .willReturn(new ScheduleTaskEntity());
    }

    @Test
    public void testInvokeWithSuccess() throws Exception {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(UUID.randomUUID().toString());
        valve.invoke(createValveContext(taskParameter, scheduleTask));
        verify(projectOpenApiService).describeProjectSteps(any(ProjectControlRequest.class));
        verify(projectOpenApiService).describeProjectProgress(any(ProjectControlRequest.class));
        verify(projectOpenApiService).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
        verify(scheduleTaskRepository).update(eq(scheduleTask));
    }

    @Test(expected = OscException.class)
    public void testInvokeWithPreCheckFailed() throws Exception {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(UUID.randomUUID().toString());

        ProjectStepResult projectStepResult = new ProjectStepResult(
                OmsStepName.PRE_CHECK, PrecheckResult.FAILED, "Precheck failed.");
        given(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .willReturn(Lists.newArrayList(createProjectStepVO()));
        given(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .willReturn(createProjectProgressResponse());
        given(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .willReturn(createProjectFullVerifyResultResponse());

        valve.invoke(createValveContext(taskParameter, scheduleTask));
        verify(projectOpenApiService).describeProjectSteps(any(ProjectControlRequest.class));
        verify(projectOpenApiService).describeProjectProgress(any(ProjectControlRequest.class));
        verify(projectOpenApiService).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
        verify(scheduleTaskRepository).update(eq(scheduleTask));
        throw new OscException(ErrorCodes.OmsPreCheckFailed, projectStepResult.getErrorMsg());
    }

    @Test(expected = OscException.class)
    public void testInvokeWithDataInconsistent() throws Exception {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(UUID.randomUUID().toString());

        ProjectStepResult projectStepResult = new ProjectStepResult(
                OmsStepName.FULL_VERIFIER, FullVerificationResult.INCONSISTENT, "Data inconsistent.");
        given(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .willReturn(Lists.newArrayList(createProjectStepVO()));
        given(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .willReturn(createProjectProgressResponse());
        given(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .willReturn(createProjectFullVerifyResultResponseWithInconsistentData());

        valve.invoke(createValveContext(taskParameter, scheduleTask));
        verify(projectOpenApiService).describeProjectSteps(any(ProjectControlRequest.class));
        verify(projectOpenApiService).describeProjectProgress(any(ProjectControlRequest.class));
        verify(projectOpenApiService).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
        verify(scheduleTaskRepository).update(eq(scheduleTask));
        throw new OscException(ErrorCodes.OmsDataCheckInconsistent,
                "Task failed for origin table has inconsistent data with new table, result: "
                        + projectStepResult.getFullVerificationResultDescription());
    }

    private ProjectStepVO createProjectStepVO() {
        return new ProjectStepVO() {{
            setStepName(OmsStepName.PRE_CHECK.name());
            setStepStatus(TaskStatus.DONE.name());
            setStepProgress(100);
        }};
    }

    private ProjectProgressResponse createProjectProgressResponse() {
        return new ProjectProgressResponse() {{
            setProjectId("projectId");
            setProgress(100);
            setStatus(TaskStatus.DONE.name());
        }};
    }

    private ProjectFullVerifyResultResponse createProjectFullVerifyResultResponse() {
        return new ProjectFullVerifyResultResponse() {{
            setProjectId("projectId");
            setStatus("FINISHED");
        }};
    }

    private ProjectFullVerifyResultResponse createProjectFullVerifyResultResponseWithInconsistentData() {
        return new ProjectFullVerifyResultResponse() {{
            setProjectId("projectId");
            setStatus("FINISHED");
            setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        }};
    }

    private ValveContext createValveContext(OnlineSchemaChangeScheduleTaskParameters taskParameter,
            ScheduleTaskEntity scheduleTask) {
        return new ValveContext() {{
            setScheduleTask(scheduleTask);
            setTaskParameter(taskParameter);
        }};
    }
}