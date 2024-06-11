
package com.oceanbase.odc.service.onlineschemachange.pipeline;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        valve = new ScheduleCheckOmsProjectValve();
        valve.projectOpenApiService = projectOpenApiService;
        valve.completeHandler = completeHandler;
        valve.scheduleTaskRepository = scheduleTaskRepository;
        valve.onlineSchemaChangeProperties = onlineSchemaChangeProperties;
    }

    @Test
    public void testInvoke() {
        OscValveContext context = mock(OscValveContext.class);
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        OnlineSchemaChangeScheduleTaskParameters parameters = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
        OnlineSchemaChangeScheduleTaskResult taskResult = mock(OnlineSchemaChangeScheduleTaskResult.class);

        when(context.getScheduleTask()).thenReturn(scheduleTask);
        when(context.getTaskParameter()).thenReturn(parameters);
        when(scheduleTask.getResultJson()).thenReturn(JsonUtils.toJson(taskResult));
        when(projectStepResult.getTaskStatus()).thenReturn(TaskStatus.DONE);
        when(projectStepResult.getFullVerificationResult()).thenReturn(FullVerificationResult.CONSISTENT);

        valve.invoke(context);

        verify(scheduleTaskRepository).update(scheduleTask);
    }

    // Add more test methods to cover all methods, edge cases and exceptions.
}
