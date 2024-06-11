package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectCheckOmsProjectValve;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.google.common.collect.Lists;

public class ScheduleCheckOmsProjectValveTest {

    @Mock
    private ProjectOpenApiService projectOpenApiService;
    @Mock
    private OscTaskCompleteHandler completeHandler;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    private ScheduleCheckOmsProjectValve valve;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        valve = new ScheduleCheckOmsProjectValve();
        valve.setProjectOpenApiService(projectOpenApiService);
        valve.setCompleteHandler(completeHandler);
        valve.setScheduleTaskRepository(scheduleTaskRepository);
        valve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
    }

    @Test
    public void testHandleOmsProjectStepResult() {
        // Test case for handling project step result
        // Implement your test scenario here
    }

    @Test
    public void testContinueHandleProjectStepResult() {
        // Test case for continuing to handle project step result
        // Implement your test scenario here
    }

    @Test
    public void testListProjectFullVerifyResult() {
        ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest();
        request.setProjectId("project1");
        request.setSourceSchemas(new String[] {"source"});
        request.setDestSchemas(new String[] {"dest"});
        request.setStatus(new String[] {"FINISHED"});
        request.setPageSize(10);
        request.setPageNumber(1);
        request.setUid("uid");

        // Mock the response
        ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();
        // Setup mock behavior

        // Call the method to test
        ProjectFullVerifyResultResponse result = valve.listProjectFullVerifyResult("project1", "source", "uid");

        // Assertions
        assertNotNull(result);
        // Add more assertions based on the expected behavior and response
    }

    @Test
    public void testAdaptResult() {
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        ProjectStepResult projectStepResult = new ProjectStepResult();

        // Setup projectStepResult with required data

        valve.adaptResult(result, projectStepResult);

        // Assertions
        // Implement assertions to verify the adaptation of result from projectStepResult
    }

    @Test
    public void testGetProjectRequest() {
        OnlineSchemaChangeScheduleTaskParameters taskParam = new OnlineSchemaChangeScheduleTaskParameters();
        taskParam.setUid("uid");
        taskParam.setOmsProjectId("project1");

        ProjectControlRequest expectedRequest = new ProjectControlRequest();
        expectedRequest.setUid("uid");
        expectedRequest.setId("project1");

        ProjectControlRequest result = valve.getProjectRequest(taskParam);

        // Assertions
        assertNotNull(result);
        // Add more assertions based on the expected behavior and response
    }

    @Test
    public void testRecordCurrentProgress() {
        // Test case for recording current progress
        // Implement your test scenario here
    }
}