package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.AutowiredMock;
import org.springframework.stereotype.ComponentMock;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.oceanbase.odc.common.exception.OscException;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscExceptionFactory;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScheduleCheckOmsProjectValveTest {

    @ComponentMock
    private ProjectOpenApiService projectOpenApiService;
    @AutowiredMock
    private OscTaskCompleteHandler completeHandler;
    @AutowiredMock
    private ScheduleTaskRepository scheduleTaskRepository;
    @AutowiredMock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    private ScheduleCheckOmsProjectValve valve;

    @Before
    public void setup() {
        valve = new ScheduleCheckOmsProjectValve();
        valve.setProjectOpenApiService(projectOpenApiService);
        valve.setCompleteHandler(completeHandler);
        valve.setScheduleTaskRepository(scheduleTaskRepository);
        valve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(Lists.newArrayList(createProjectStepVO()));
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(createProjectProgressResponse());
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(createProjectFullVerifyResultResponse());
        when(completeHandler.getNext()).thenReturn(null);
    }

    @Test
    public void testInvoke_success() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = createTaskParameter();
        ScheduleTaskEntity scheduleTask = createScheduleTaskEntity();
        valve.invoke(createValveContext(taskParameter, scheduleTask));

        verify(projectOpenApiService, times(1)).describeProjectSteps(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).describeProjectProgress(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(
                eq(new ListProjectFullVerifyResultRequest().setProjectId("projectId")
                        .setSourceSchemas(new String[] {"databaseName"})
                        .setDestSchemas(new String[] {"databaseName"})
                        .setStatus(new String[] {"FINISHED", "SUSPEND", "RUNNING"})
                        .setPageSize(10)
                        .setPageNumber(1)
                        .setUid(taskParameter.getUid())));

        OnlineSchemaChangeScheduleTaskResult result = new Gson().fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(TaskStatus.SUCCESS, result.getStatus());
    }

    @Test
    public void testInvoke_preCheckFailed() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = createTaskParameter();
        ScheduleTaskEntity scheduleTask = createScheduleTaskEntity();
        ProjectStepResult projectStepResult = new ProjectStepResult(PrecheckResult.FAILED, "error mssage",
                OmsStepName.PRE_CHECK);
        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(Lists.newArrayList(projectStepResult));

        valve.invoke(createValveContext(taskParameter, scheduleTask));

        verify(projectOpenApiService, times(1)).describeProjectSteps(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(0)).describeProjectProgress(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(0)).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));

        assertThrows(OscException.class, () -> {
            new Gson().fromJson(scheduleTask.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class);
        });
        assertEquals("error mssage", scheduleTask.getLastErrorMessage());
    }

    @Test
    public void testInvoke_dataInconsistent() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = createTaskParameter();
        ScheduleTaskEntity scheduleTask = createScheduleTaskEntity();
        ProjectStepResult projectStepResult = new ProjectStepResult(FullVerificationResult.INCONSISTENT,
                "error mssage", OmsStepName.FULL_VERIFIER);
        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(Lists.newArrayList(projectStepResult));

        valve.invoke(createValveContext(taskParameter, scheduleTask));

        verify(projectOpenApiService, times(1)).describeProjectSteps(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(0)).describeProjectProgress(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(0)).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));

        assertThrows(OscException.class, () -> {
            new Gson().fromJson(scheduleTask.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class);
        });
        assertEquals("Task failed for origin table has inconsistent data with new table, result: "
                + projectStepResult.getFullVerificationResultDescription(),
                scheduleTask.getLastErrorMessage());
    }

    private ProjectControlRequest createProjectControlRequest() {
        return new ProjectControlRequest().setUid(UUID.randomUUID().toString())
                .setId("projectId");
    }

    private ProjectStepVO createProjectStepVO() {
        return new ProjectStepVO();
    }

    private ProjectProgressResponse createProjectProgressResponse() {
        return new ProjectProgressResponse();
    }

    private ProjectFullVerifyResultResponse createProjectFullVerifyResultResponse() {
        return new ProjectFullVerifyResultResponse();
    }

    private OnlineSchemaChangeScheduleTaskParameters createTaskParameter() {
        return new OnlineSchemaChangeScheduleTaskParameters();
    }

    private ScheduleTaskEntity createScheduleTaskEntity() {
        return new ScheduleTaskEntity();
    }

    private OscValveContext createValveContext(OnlineSchemaChangeScheduleTaskParameters taskParameter,
            ScheduleTaskEntity scheduleTask) {
        return new OscValveContext().setScheduleTask(scheduleTask).setTaskParameter(taskParameter);
    }
}