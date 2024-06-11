import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class ScheduleCheckOmsProjectValveTest {

    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Before
    public void setUp() {
        scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        scheduleCheckOmsProjectValve.projectOpenApiService = mock(ProjectOpenApiService.class);
        scheduleCheckOmsProjectValve.completeHandler = mock(OscTaskCompleteHandler.class);
        scheduleCheckOmsProjectValve.scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        scheduleCheckOmsProjectValve.onlineSchemaChangeProperties = mock(OnlineSchemaChangeProperties.class);
    }

    @Test
    public void testInvoke() {
        // Test setup
        ValveContext valveContext = mock(ValveContext.class);
        OscValveContext context = mock(OscValveContext.class);
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId("123");
        taskParameter.setDatabaseName("testDB");
        taskParameter.setUid("testUid");
        scheduleTask.setId(1L);
        scheduleTask.setResultJson("{}");
        context.setScheduleTask(scheduleTask);
        context.setTaskParameter(taskParameter);
        context.setParameter(new Parameter());
        context.getParameter.setSwapTableType(SwapTableType.AUTO);

        when(scheduleCheckOmsProjectValve.projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(new ArrayList<>());
        when(scheduleCheckOmsProjectValve.projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(new ProjectProgressResponse());

        // Execute the method
        scheduleCheckOmsProjectValve.invoke(valveContext);

        // Verify the result
        verify(scheduleCheckOmsProjectValve.projectOpenApiService).describeProjectSteps(any(ProjectControlRequest.class));
        verify(scheduleCheckOmsProjectValve.projectOpenApiService).describeProjectProgress(any(ProjectControlRequest.class));
        verify(scheduleCheckOmsProjectValve.scheduleTaskRepository).update(scheduleTask);
    }

    // Test other public methods with different scenarios

}