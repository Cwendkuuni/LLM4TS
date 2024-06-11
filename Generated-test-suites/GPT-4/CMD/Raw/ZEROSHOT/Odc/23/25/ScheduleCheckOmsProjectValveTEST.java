
package com.oceanbase.odc.service.onlineschemachange.pipeline;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ListProjectFullVerifyResultRequest;

public class ScheduleCheckOmsProjectValveTest {

    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @Mock
    private OscTaskCompleteHandler completeHandler;
    
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke() {
        OscValveContext mockContext = mock(OscValveContext.class);
        ScheduleTaskEntity mockTask = mock(ScheduleTaskEntity.class);
        when(mockContext.getScheduleTask()).thenReturn(mockTask);

        OnlineSchemaChangeScheduleTaskParameters mockParameters = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        when(mockContext.getTaskParameter()).thenReturn(mockParameters);

        scheduleCheckOmsProjectValve.invoke(mockContext);

        verify(projectOpenApiService, times(1)).describeProjectProgress(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).describeProjectSteps(any(ProjectControlRequest.class));
    }

    @Test
    public void testGetProjectRequest() {
        OnlineSchemaChangeScheduleTaskParameters mockParameters = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        when(mockParameters.getUid()).thenReturn("testUid");
        when(mockParameters.getOmsProjectId()).thenReturn("testOmsProjectId");

        ProjectControlRequest result = (ProjectControlRequest) ReflectionTestUtils.invokeMethod(scheduleCheckOmsProjectValve, "getProjectRequest", mockParameters);

        assertEquals("testUid", result.getUid());
        assertEquals("testOmsProjectId", result.getId());
    }

    @Test
    public void testListProjectFullVerifyResult() {
        String projectId = "testProjectId";
        String databaseName = "testDatabaseName";
        String uid = "testUid";

        ListProjectFullVerifyResultRequest expectedRequest = new ListProjectFullVerifyResultRequest();
        expectedRequest.setProjectId(projectId);
        expectedRequest.setSourceSchemas(new String[]{databaseName});
        expectedRequest.setDestSchemas(new String[]{databaseName});
        expectedRequest.setStatus(new String[]{"FINISHED", "SUSPEND", "RUNNING"});
        expectedRequest.setPageSize(10);
        expectedRequest.setPageNumber(1);
        expectedRequest.setUid(uid);

        scheduleCheckOmsProjectValve.listProjectFullVerifyResult(projectId, databaseName, uid);

        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(eq(expectedRequest));
    }

    // Add more test methods as necessary
}
