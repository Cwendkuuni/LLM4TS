package com.oceanbase.odc.service.onlineschemachange.pipeline;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class ScheduleCheckOmsProjectValveTest {
    @Mock
    ProjectOpenApiService mockProjectOpenApiService;
    @Mock
    OscTaskCompleteHandler mockCompleteHandler;
    @Mock
    ScheduleTaskRepository mockScheduleTaskRepository;
    @Mock
    OnlineSchemaChangeProperties mockOnlineSchemaChangeProperties;
    @InjectMocks
    ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    // This checks the main functionality of the method.
    @Test
    public void testInvoke() {
       ValveContext mockValveContext = mock(ValveContext.class);
       doNothing().when(mockValveContext).overrideParam(anyString(), any());
       scheduleCheckOmsProjectValve.invoke(mockValveContext);
       verify(mockValveContext, times(1)).overrideParam(anyString(), any());
    }

    // This tests the edge case where invoke throws an exception.
    @Test(expected = RuntimeException.class)
    public void testInvokeWithException() {
       ValveContext mockValveContext = mock(ValveContext.class);
       doThrow(new RuntimeException()).when(mockValveContext).overrideParam(anyString(), any());
       scheduleCheckOmsProjectValve.invoke(mockValveContext);
    }

    // This checks the main functionality of the method.
    @Test
    public void testListProjectFullVerifyResult() {
       String projectId = "testProjectId";
       String databaseName = "testDBName";
       String uid = "testUID";
       scheduleCheckOmsProjectValve.listProjectFullVerifyResult(projectId, databaseName, uid);
       verify(mockProjectOpenApiService, times(1)).
           listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
    }

    // This tests the edge case where listProjectFullVerifyResult throws an exception.
    @Test(expected = RuntimeException.class)
    public void testListProjectFullVerifyResultWithException() {
       String projectId = "testProjectId";
       String databaseName = "testDBName";
       String uid = "testUID";
       doThrow(new RuntimeException()).when(mockProjectOpenApiService).
           listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
       scheduleCheckOmsProjectValve.listProjectFullVerifyResult(projectId, databaseName, uid);
    }
}
