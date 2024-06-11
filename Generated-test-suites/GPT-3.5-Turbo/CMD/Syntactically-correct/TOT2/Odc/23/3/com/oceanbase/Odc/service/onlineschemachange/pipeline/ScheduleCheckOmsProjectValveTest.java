import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import java.util.List;

public class ScheduleCheckOmsProjectValveTest {

    @Mock
    ProjectOpenApiService projectOpenApiService;
    @Mock
    OscTaskCompleteHandler completeHandler;
    @Mock
    ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @Test
    public void testInvoke() {
        // Test basic functionality of invoke method
        ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        ValveContext valveContext = new ValveContext();

        OscValveContext context = new OscValveContext();
        context.setScheduleTask(new ScheduleTaskEntity());
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId("123");
        taskParameter.setDatabaseName("testDatabase");
        taskParameter.setUid("456");

        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(new ProjectProgressResponse());

        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(List.of(new ProjectStepVO()));

        when(scheduleTaskRepository.update(any(ScheduleTaskEntity.class))).thenReturn(true);

        scheduleCheckOmsProjectValve.invoke(valveContext);

        // Add more test cases as needed for edge cases and exception handling
    }

    // Add more test cases for other public methods in the ScheduleCheckOmsProjectValve class

}