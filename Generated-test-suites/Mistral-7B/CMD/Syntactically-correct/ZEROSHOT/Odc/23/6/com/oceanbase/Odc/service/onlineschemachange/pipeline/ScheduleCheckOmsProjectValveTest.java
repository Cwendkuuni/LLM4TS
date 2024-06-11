package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.json.JsonUtils;
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
import com.oceanbase.odc.service.onlineschemachange.oms.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;

import lombok.extern.slf4j.Slf4j;

@Component
public class ScheduleCheckOmsProjectValveTest {

    @Autowired
    private ProjectOpenApiService projectOpenApiServiceMock;

    @Autowired
    private OscTaskCompleteHandler completeHandlerMock;

    @Autowired
    private ScheduleTaskRepository scheduleTaskRepositoryMock;

    @Autowired
    private OnlineSchemaChangeProperties onlineSchemaChangePropertiesMock;

    @Autowired
    private DefaultTableFactory tableFactoryMock;

    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    private ProjectFullVerifyResultResponse projectFullVerifyResultResponseMock;

    private ProjectStepVO projectStepVOMock;

    private ProjectProgressResponse projectProgressResponseMock;

    private ProjectControlRequest projectControlRequestMock;

    private OnlineSchemaChangeScheduleTaskParameters taskParameter;

    private OnlineSchemaChangeScheduleTaskResult result;

    private ScheduleTaskEntity scheduleTaskEntityMock;

    @Before
    public void setup() {
        scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();

        projectFullVerifyResultResponseMock = mock(ProjectFullVerifyResultResponse.class);
        projectStepVOMock = mock(ProjectStepVO.class);
        projectProgressResponseMock = mock(ProjectProgressResponse.class);
        projectControlRequestMock = mock(ProjectControlRequest.class);

        taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        scheduleTaskEntityMock = mock(ScheduleTaskEntity.class);

        when(projectOpenApiServiceMock.describeProjectSteps(projectControlRequestMock))
                .thenReturn(Lists.newArrayList(projectStepVOMock));
        when(projectOpenApiServiceMock.describeProjectProgress(projectControlRequestMock))
                .thenReturn(projectProgressResponseMock);
        when(projectOpenApiServiceMock.listProjectFullVerifyResult(any()))
                .thenReturn(projectFullVerifyResultResponseMock);
        when(projectOpenApiServiceMock.resumeProject(projectControlRequestMock)).thenReturn(null);

        when(scheduleTaskRepositoryMock.update(scheduleTaskEntityMock)).thenReturn(scheduleTaskEntityMock);
        when(scheduleTaskRepositoryMock.save(scheduleTaskEntityMock)).thenReturn(scheduleTaskEntityMock);
        when(completeHandlerMock.handleOscTaskComplete(result)).thenReturn(null);

        when(projectStepVOMock.getStepName()).thenReturn(OmsStepName.FULL_TRANSFER.name());
        when(projectStepVOMock.getProgress()).thenReturn(100);
        when(projectStepVOMock.getStatus()).thenReturn(TaskStatus.DONE.name());
        when(projectProgressResponseMock.getProjectId()).thenReturn("projectId");
        when(projectProgressResponseMock.getCurrentStep()).thenReturn(projectStepVOMock);

        when(projectControlRequestMock.getId()).thenReturn("projectId");
        when(projectControlRequestMock.getUid()).thenReturn("uid");

        when(onlineSchemaChangePropertiesMock.isEnableFullVerify()).thenReturn(true);

        when(tableFactoryMock.generateTable(anyInt(), anyList(), anyList()))
                .thenReturn(new Table("", "", "", ""));
    }

    @Test
    public void testInvoke() throws Exception {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(result));

        when(scheduleTaskEntityMock.getId()).thenReturn(scheduleTaskEntity.getId());
        when(scheduleTaskEntityMock.getStatus()).thenReturn(TaskStatus.SCHEDULED.name());
        when(scheduleTaskEntityMock.getResultJson()).thenReturn(JsonUtils.toJson(result));

        when(projectOpenApiServiceMock.getProjectRequest(taskParameter)).thenReturn(projectControlRequestMock);

        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        verify(scheduleTaskRepositoryMock).update(scheduleTaskEntityMock);
    }

    @Test(expected = OscException.class)
    public void testInvoke_OmsPreCheckFailed() throws Exception {
        PrecheckResult precheckResult = PrecheckResult.FAILED;
        when(projectStepVOMock.getPrecheckResult()).thenReturn(precheckResult);
        when(projectProgressResponseMock.getCurrentStep()).thenReturn(projectStepVOMock);

        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        verify(projectOpenApiServiceMock).resumeProject(projectControlRequestMock);
        throw new OscException(ErrorCodes.OmsPreCheckFailed,
                projectStepVOMock.getErrorMsg());
    }

    @Test(expected = OscException.class)
    public void testInvoke_OmsDataCheckInconsistent() throws Exception {
        FullVerificationResult fullVerificationResult = FullVerificationResult.INCONSISTENT;
        String fullVerificationResultDescription = "Task failed for origin table has inconsistent data with new table";
        when(projectProgressResponseMock.getFullVerificationResult()).thenReturn(fullVerificationResult);
        when(projectProgressResponseMock.getFullVerificationResultDescription())
                .thenReturn(fullVerificationResultDescription);
        when(projectStepVOMock.getFullVerificationResult()).thenReturn(fullVerificationResult);

        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        verify(projectOpenApiServiceMock).resumeProject(projectControlRequestMock);
        throw new OscException(ErrorCodes.OmsDataCheckInconsistent,
                "Task failed for origin table has inconsistent data with new table, result: "
                        + fullVerificationResultDescription);
    }

    @Test
    public void testInvoke_OmsProjectStepResult_DONE() throws Exception {
        PrecheckResult precheckResult = PrecheckResult.PASSED;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;

        when(projectStepVOMock.getPrecheckResult()).thenReturn(precheckResult);
        when(projectProgressResponseMock.getFullVerificationResult()).thenReturn(fullVerificationResult);
        when(projectProgressResponseMock.getCurrentStep()).thenReturn(projectStepVOMock);

        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        verify(projectOpenApiServiceMock).resumeProject(projectControlRequestMock);

        assertEquals(TaskStatus.SCHEDULED, result.getStatus());
        assertEquals(SwapTableType.FULL_VERIFICATION_COMPLETED, result.getSwapTableType());
        assertEquals(FullVerificationResult.CONSISTENT, result.getFullVerificationResult());
        assertEquals(100, result.getFullTransferProgressPercentage());
        assertEquals(1, result.getFullTransferFinishedCount());
        assertEquals(1, result.getFullTransferEstimatedCount());
    }

    @Test
    public void testInvoke_OmsProjectStepResult_RUNNING() throws Exception {
        PrecheckResult precheckResult = PrecheckResult.PASSED;
        FullVerificationResult fullVerificationResult = FullVerificationResult.RUNNING;

        when(projectStepVOMock.getPrecheckResult()).thenReturn(precheckResult);
        when(projectProgressResponseMock.getFullVerificationResult()).thenReturn(fullVerificationResult);
        when(projectProgressResponseMock.getCurrentStep()).thenReturn(projectStepVOMock);

        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        verify(projectOpenApiServiceMock).resumeProject(projectControlRequestMock);

        assertEquals(TaskStatus.RUNNING, result.getStatus());
        assertEquals(SwapTableType.FULL_VERIFICATION_RUNNING, result.getSwapTableType());
        assertEquals(FullVerificationResult.RUNNING, result.getFullVerificationResult());
        assertEquals(0, result.getFullTransferFinishedCount());
        assertEquals(1, result.getFullTransferEstimatedCount());
    }

    @Test
    public void testInvoke_OmsProjectStepResult_SUSPEND() throws Exception {
        PrecheckResult precheckResult = PrecheckResult.PASSED;
        FullVerificationResult fullVerificationResult = FullVerificationResult.SUSPEND;

        when(projectStepVOMock.getPrecheckResult()).thenReturn(precheckResult);
        when(projectProgressResponseMock.getFullVerificationResult()).thenReturn(fullVerificationResult);
        when(projectProgressResponseMock.getCurrentStep()).thenReturn(projectStepVOMock);

        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        verify(projectOpenApiServiceMock).resumeProject(projectControlRequestMock);

        assertEquals(TaskStatus.SCHEDULED, result.getStatus());
        assertEquals(SwapTableType.FULL_VERIFICATION_SUSPEND, result.getSwapTableType());
        assertEquals(FullVerificationResult.SUSPEND, result.getFullVerificationResult());
        assertEquals(0, result.getFullTransferFinishedCount());
        assertEquals(1, result.getFullTransferEstimatedCount());
    }
}