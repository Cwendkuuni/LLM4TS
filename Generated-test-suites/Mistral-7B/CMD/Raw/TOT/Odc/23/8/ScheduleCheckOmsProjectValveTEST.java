package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.ProjectStepResult;
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

@SuppressWarnings("unchecked")
@InjectMocks
public class ScheduleCheckOmsProjectValveTest {

    @Mock
    private ProjectOpenApiService projectOpenApiService;
    @Mock
    private OscTaskCompleteHandler completeHandler;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    @Mock
    private DefaultTableFactory defaultTableFactory;

    private ScheduleCheckOmsProjectValve valveUnderTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        valveUnderTest = new ScheduleCheckOmsProjectValve();
        valveUnderTest.setProjectOpenApiService(projectOpenApiService);
        valveUnderTest.setCompleteHandler(completeHandler);
        valveUnderTest.setScheduleTaskRepository(scheduleTaskRepository);
        valveUnderTest.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
        valveUnderTest.setDefaultTableFactory(defaultTableFactory);
    }

    @Test
    public void testInvoke_success() throws Exception {
        // Arrange
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        ProjectStepVO stepVO = mock(ProjectStepVO.class);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(stepVO));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        ProjectStepResult projectStepResult = new ProjectStepResult(stepVO, progress, null, null, null);

        // Act
        valveUnderTest.invoke(mock(ValveContext.class));

        // Assert
        assertEquals(TaskStatus.DONE, projectStepResult.getTaskStatus());
        // Add more assertions if needed
    }

    // Add more test cases for different scenarios such as error handling, edge cases, etc.

    private ProjectControlRequest getProjectRequest(OnlineSchemaChangeScheduleTaskParameters taskParam) {
        // Implement this method as in the original class
    }
}