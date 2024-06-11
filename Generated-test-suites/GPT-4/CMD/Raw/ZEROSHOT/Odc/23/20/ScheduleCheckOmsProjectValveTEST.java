
import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.service.onlineschemachange.pipeline.*;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.*;
import com.oceanbase.odc.metadb.schedule.*;
import com.google.common.collect.Lists;
import com.oceanbase.odc.common.json.*;
import com.oceanbase.odc.service.onlineschemachange.model.*;
import com.oceanbase.odc.service.onlineschemachange.*;
import com.oceanbase.odc.service.onlineschemachange.configuration.*;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.*;
import com.oceanbase.odc.service.onlineschemachange.oms.response.*;
import com.oceanbase.odc.service.onlineschemachange.oms.request.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleCheckOmsProjectValveTest {

    @Mock
    private ProjectOpenApiService projectOpenApiService;
    @Mock
    private OscTaskCompleteHandler completeHandler;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private Valve valve;

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke() {
        // prepare test data
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        OscValveContext context = mock(OscValveContext.class);
        OnlineSchemaChangeScheduleTaskParameters taskParameter = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        List<ProjectStepVO> projectSteps = mock(List.class);
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        OnlineSchemaChangeScheduleTaskResult lastResult = mock(OnlineSchemaChangeScheduleTaskResult.class);
        SwapTableType swapTableType = SwapTableType.auto;

        // Proper definitions for the methods
        when(context.getScheduleTask()).thenReturn(scheduleTask);
        when(context.getTaskParameter()).thenReturn(taskParameter);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(JsonUtils.fromJson(scheduleTask.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class)).thenReturn(lastResult);

        // Invoke test method
        scheduleCheckOmsProjectValve.invoke(context);

        // Verify the results
        verify(scheduleTaskRepository, times(2)).update(scheduleTask);
        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
    }
}
