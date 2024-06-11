
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.when;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValve;
import com.oceanbase.odc.service.onlineschemachange.pipeline.OscValveContext;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ValveContext;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Mock
    private OscValveContext oscValveContext;

    @Mock
    private OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters;

    @Before
    public void setUp() {
        oscValveContext = Mockito.mock(OscValveContext.class);
        onlineSchemaChangeScheduleTaskParameters = Mockito.mock(OnlineSchemaChangeScheduleTaskParameters.class);
    }

    @Test
    public void testInvoke() {
        String uid = "123456";
        String omsProjectId = "654321";

        when(oscValveContext.getTaskParameter()).thenReturn(onlineSchemaChangeScheduleTaskParameters);
        when(onlineSchemaChangeScheduleTaskParameters.getUid()).thenReturn(uid);
        when(onlineSchemaChangeScheduleTaskParameters.getOmsProjectId()).thenReturn(omsProjectId);

        scheduleCheckOmsProjectValve.invoke(oscValveContext);
       
    }

    @Test
    public void testAdaptResult() {
        ProjectStepResultChecker.ProjectStepResult projectStepResult = Mockito.mock(ProjectStepResultChecker.ProjectStepResult.class);
        when(projectStepResult.getFullTransferFinishedCount()).thenReturn(100);
        when(projectStepResult.getFullTransferProgressPercentage()).thenReturn(50.0);
        scheduleCheckOmsProjectValve.adaptResult(new OnlineSchemaChangeScheduleTaskResult(onlineSchemaChangeScheduleTaskParameters), projectStepResult);
      
    }

    @Test
    public void testGetProjectRequest() {
        String uid = "123456";
        String omsProjectId = "654321";

        when(onlineSchemaChangeScheduleTaskParameters.getUid()).thenReturn(uid);
        when(onlineSchemaChangeScheduleTaskParameters.getOmsProjectId()).thenReturn(omsProjectId); 

        ProjectControlRequest projectControlRequest = scheduleCheckOmsProjectValve.getProjectRequest(onlineSchemaChangeScheduleTaskParameters);

        assertEquals(uid, projectControlRequest.getUid());
        assertEquals(omsProjectId, projectControlRequest.getId());
    }

}
