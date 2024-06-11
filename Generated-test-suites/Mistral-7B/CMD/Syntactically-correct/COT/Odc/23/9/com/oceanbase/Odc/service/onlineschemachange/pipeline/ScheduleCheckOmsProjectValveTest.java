package com.example.junit4.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertEquals;

import com.google.common.collect.Lists;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.oms.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectCheckOmsProjectValve;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

public class ScheduleCheckOmsProjectValveTest {
    private static final String PROJECT_ID = "projectId";
    private static final String DATABASE_NAME = "databaseName";
    private static final String UID = "uid";
    private static final SwapTableType SWAP_TABLE_TYPE = SwapTableType.SCHEMA;

    private ProjectOpenApiService projectOpenApiService;
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    private OscTaskCompleteHandler completeHandler;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ProjectCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Before
    public void setUp() {
        projectOpenApiService = mock(ProjectOpenApiService.class);
        onlineSchemaChangeProperties = mock(OnlineSchemaChangeProperties.class);
        completeHandler = mock(OscTaskCompleteHandler.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        scheduleCheckOmsProjectValve = new ProjectCheckOmsProjectValve() {
            @Override
            protected ProjectOpenApiService getProjectOpenApiService() {
                return projectOpenApiService;
            }

            @Override
            protected OnlineSchemaChangeProperties getOnlineSchemaChangeProperties() {
                return onlineSchemaChangeProperties;
            }

            @Override
            protected OscTaskCompleteHandler getOscTaskCompleteHandler() {
                return completeHandler;
            }

            @Override
            protected ScheduleTaskRepository getScheduleTaskRepository() {
                return scheduleTaskRepository;
            }
        };
    }

    @Test
    public void testInvoke() throws Exception {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId(PROJECT_ID);
        taskParameter.setDatabaseName(DATABASE_NAME);
        taskParameter.setUid(UID);
        taskParameter.setSwapTableType(SWAP_TABLE_TYPE);

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(UUID.randomUUID().toString());
        scheduleTask.setResultJson("");

        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
        ProjectFullVerifyResultResponse fullVerifyResultResponse = mock(ProjectFullVerifyResultResponse.class);
        doReturn(fullVerifyResultResponse).when(projectOpenApiService).listProjectFullVerifyResult(any());
        doReturn(projectStepResult).when(projectOpenApiService).describeProjectSteps(any());
        doReturn(projectStepResult).when(projectOpenApiService).describeProjectProgress(any());

        OnlineSchemaChangeScheduleTaskResult result = scheduleCheckOmsProjectValve.invoke(getValveContext(taskParameter));

        verify(projectOpenApiService).describeProjectSteps(taskParameter.getProjectControlRequest());
        verify(projectOpenApiService).describeProjectProgress(taskParameter.getProjectControlRequest());
        verify(projectOpenApiService).listProjectFullVerifyResult(PROJECT_ID, DATABASE_NAME, UID);

        assertEquals(result.getFullTransferEstimatedCount(), 0L);
        assertEquals(result.getFullVerificationResult(), FullVerificationResult.UNCHECK);
        assertNull(result.getResultJson());
        assertEquals(result.getSwapTableType(), SWAP_TABLE_TYPE);
    }

    private ValveContext getValveContext(OnlineSchemaChangeScheduleTaskParameters taskParameter) {
        OscValveContext oscValveContext = new OscValveContext();
        oscValveContext.setScheduleTask(scheduleTask);
        oscValveContext.setTaskParameter(taskParameter);
        return oscValveContext;
    }

    // Add test cases for other methods: handleOmsProjectStepResult, continueHandleProjectStepResult, listProjectFullVerifyResult, adaptResult, getProjectRequest, and recordCurrentProgress.
}