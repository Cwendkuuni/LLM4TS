package com.oceanbase.odc.service.onlineschemachange.pipeline;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.oms.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@Component
public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @MockBean
    private ProjectOpenApiService projectOpenApiService;

    @MockBean
    private OscTaskCompleteHandler completeHandler;

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    @Autowired
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    private OnlineSchemaChangeScheduleTaskParameters taskParameter;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvokeWithSuccess() {
        // Given
        ProjectControlRequest projectRequest = mock(ProjectControlRequest.class);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenAnswer(new Answer<List<ProjectStepVO>>() {
            @Override
            public List<ProjectStepVO> answer(InvocationOnMock invocation) {
                return Lists.newArrayList();
            }
        });

        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(getProjectProgressResponse());

        when(projectOpenApiService.listProjectFullVerifyResult(any())).thenAnswer(
                invocation -> getProjectFullVerifyResultResponse());

        when(completeHandler.handle(any())).thenReturn(true);

        scheduleCheckOmsProjectValve.invoke(getContext());

        // Then
        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any());
        verify(completeHandler, times(1)).handle(any());
        verifyNoMoreInteractions(projectOpenApiService, completeHandler);

        ScheduleTaskEntity scheduleTask = getContext().getScheduleTask();
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        Assertions.assertEquals(TaskStatus.DONE, result.getStatus());
    }

    private ProjectProgressResponse getProjectProgressResponse() {
        ProjectProgressResponse response = new ProjectProgressResponse();
        response.setCurrentStep(new ProjectStepVO());
        response.setProjectId("projectId");
        return response;
    }

    private ProjectFullVerifyResultResponse getProjectFullVerifyResultResponse() {
        ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        response.setFullVerificationResult(fullVerificationResult);
        response.setFullVerificationResultDescription("desc");
        response.setFullVerificationProgressPercentage(100);
        return response;
    }

    private ValveContext getContext() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setStatus(TaskStatus.SCHEDULED);
        ValveContext context = new ValveContext() {
            @Override
            public ScheduleTaskEntity getScheduleTask() {
                return scheduleTask;
            }

            @Override
            public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
                return taskParameter;
            }
        };
        return context;
    }
}