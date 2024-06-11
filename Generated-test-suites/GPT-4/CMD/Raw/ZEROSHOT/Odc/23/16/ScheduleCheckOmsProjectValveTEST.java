
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve checkOmsProjectValve;

    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke() {
        //Given
        ValveContext valveContext = mock(ValveContext.class);
        OscValveContext context = mock(OscValveContext.class);
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);

        //When
        when(context.getScheduleTask()).thenReturn(scheduleTask);
        checkOmsProjectValve.invoke(valveContext);

        //Then
        verify(context, times(1)).getScheduleTask();
    }

    @Test
    public void testHandleOmsProjectStepResult() {
        // write test code here
    }

    @Test
    public void testContinueHandleProjectStepResult() {
         // write test code here
    }

    @Test
    public void testListProjectFullVerifyResult() {
         // write test code here
    }

    @Test
    public void testAdaptResult() {
         // write test code here
    }

    @Test
    public void testGetProjectRequest() {
         // write test code here
    }

    @Test
    public void testRecordCurrentProgress() {
         // write test code here
    }

    @Test
    public void testGetHeader() {
        // write test code here
    }
}
