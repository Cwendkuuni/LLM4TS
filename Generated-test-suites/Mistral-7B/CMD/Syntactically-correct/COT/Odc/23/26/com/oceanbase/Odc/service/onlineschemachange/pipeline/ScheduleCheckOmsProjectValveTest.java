import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.google.common.collect.Lists;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.oms.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValve;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValveTest;
import com.oceanbase.odc.service.onlineschemachange.pipeline.BaseValve;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ScheduleCheckOmsProjectValveTest {

    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;
    private ProjectOpenApiService projectOpenApiService;
    private BaseValve baseValve;
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    private ScheduleTaskEntity scheduleTask;
    private OnlineSchemaChangeScheduleTaskParameters taskParameter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        projectOpenApiService = mock(ProjectOpenApiService.class);
        baseValve = mock(BaseValve.class);
        onlineSchemaChangeProperties = mock(OnlineSchemaChangeProperties.class);
        scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        scheduleCheckOmsProjectValve.projectOpenApiService = projectOpenApiService;
        scheduleCheckOmsProjectValve.baseValve = baseValve;
        scheduleCheckOmsProjectValve.onlineSchemaChangeProperties = onlineSchemaChangeProperties;

        scheduleTask = new ScheduleTaskEntity();
        taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
    }

    @Test
    public void testInvoke() {
        given(projectOpenApiService.describeProjectSteps(any()))
                .willReturn(Lists.newArrayList(mock(ProjectStepVO.class)));

        given(projectOpenApiService.describeProjectProgress(any()))
                .willReturn(mock(ProjectProgressResponse.class));

        given(projectOpenApiService.listProjectFullVerifyResult(any()))
                .willReturn(mock(ProjectFullVerifyResultResponse.class));

        given(baseValve.invoke(any()))
                .willCallRealMethod();

        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        verify(baseValve, times(1)).invoke(any());
    }
}