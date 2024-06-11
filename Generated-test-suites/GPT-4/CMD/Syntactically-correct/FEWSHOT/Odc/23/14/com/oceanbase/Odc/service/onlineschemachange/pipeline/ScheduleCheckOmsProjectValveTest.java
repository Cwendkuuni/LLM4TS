package com.oceanbase.odc.service.onlineschemachange.pipeline;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.model.*;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.*;
import com.oceanbase.odc.service.onlineschemachange.oms.response.*;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;

@SpringBootTest
public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Mock
    private ProjectOpenApiService projectOpenApiService;
    
    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    
    @Before
    public void setUp() throws Exception {
        // Setup the projectOpenApiService mock
        ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest();
        when(projectOpenApiService.listProjectFullVerifyResult(request)).thenReturn(new ProjectFullVerifyResultResponse());
    }

    @Test
    public void testInvoke() {
        OscValveContext context = new OscValveContext();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleCheckOmsProjectValve.invoke(context);
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
    }

    @Test(expected = OscException.class)
    public void testInvokeWithException() {
        OscValveContext context = new OscValveContext();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        when(onlineSchemaChangeProperties.isEnableFullVerify()).thenReturn(false);
        scheduleCheckOmsProjectValve.invoke(context);
    }
    // Additional test methods here...
}