import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ScheduleCheckOmsProjectValveTest {

    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Before
    public void setUp() {
        scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
    }

    @Test
    public void testInvoke() {
        // Arrange

        // Act
        scheduleCheckOmsProjectValve.invoke(valveContext);

        // Assert
        // Add your assertions here
    }

    @Test
    public void testHandleOmsProjectStepResult() {
        // Arrange

        // Act
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(valveContext, projectStepResult, result, swapTableType, scheduleTask);

        // Assert
        // Add your assertions here
    }

    @Test
    public void testContinueHandleProjectStepResult() {
        // Arrange

        // Act
        scheduleCheckOmsProjectValve.continueHandleProjectStepResult(projectStepResult);

        // Assert
        // Add your assertions here
    }

    @Test
    public void testAdaptResult() {
        // Arrange

        // Act
        scheduleCheckOmsProjectValve.adaptResult(result, projectStepResult);

        // Assert
        // Add your assertions here
    }

    @Test
    public void testGetProjectRequest() {
        // Arrange

        // Act
        ProjectControlRequest projectRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParam);

        // Assert
        // Add your assertions here
    }

    @Test
    public void testRecordCurrentProgress() {
        // Arrange

        // Act
        scheduleCheckOmsProjectValve.recordCurrentProgress(projectId, result);

        // Assert
        // Add your assertions here
    }

    @Test
    public void testGetHeader() {
        // Arrange

        // Act
        List<String> header = scheduleCheckOmsProjectValve.getHeader();

        // Assert
        assertNotNull(header);
        assertEquals(5, header.size());
        assertEquals("ProjectId", header.get(0));
        assertEquals("Step", header.get(1));
        assertEquals("Status", header.get(2));
        assertEquals("Progress", header.get(3));
        assertEquals("Description", header.get(4));
    }
}