package com.oceanbase.odc.service.onlineschemachange.pipeline;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.PrecheckResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ListProjectFullVerifyResultRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleCheckOmsProjectValveTest {

    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @Mock
    private ScheduleTaskEntity scheduleTask;

    @Mock
    private List<ProjectStepVO> projectSteps;

    @Mock
    private ProjectProgressResponse progress;

    @Mock
    private ProjectStepResult projectStepResult;

    @Mock
    private ProjectControlRequest projectRequest;

    @Mock
    private OnlineSchemaChangeScheduleTaskResult result;

    @Mock
    private ListProjectFullVerifyResultRequest request;

    @Mock
    private ProjectFullVerifyResultResponse response;
    
    private ScheduleCheckOmsProjectValve valve;
    
    @Before
    public void setUp() {
        valve = new ScheduleCheckOmsProjectValve() {
            @Override
            protected void getNext() {
                super.getNext();
            }
        };
        valve.setProjectOpenApiService(projectOpenApiService);
        valve.setCompleteHandler(completeHandler);
        valve.setScheduleTaskRepository(scheduleTaskRepository);
        valve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
        
    }

    @Test
    public void testInvoke() {
        // Prepare
        when(onlineSchemaChangeProperties.isEnableFullVerify()).thenReturn(true);
        when(projectStepResult.getTaskStatus()).thenReturn(TaskStatus.UNKNOWN);
        when(projectStepResult.getErrorMsg()).thenReturn("");
        when(scheduleTask.getResultJson()).thenReturn("");
        when(scheduleTask.getResultJson()).thenReturn("");
        when(scheduleTask.getResultJson()).thenReturn("");
        when(response.getSuccess()).thenReturn(true);
        when(response.getErrMsg()).thenReturn("");
        when(response.getCode()).thenReturn(rcode.SUCCESS);
        when(response.getData()).thenReturn(null);

        // Execute
        valve.invoke(new OscValveContext());

        // Verify
        Mockito.verify(onlineSchemaChangeProperties).isEnableFullVerify();
        Mockito.verify(projectOpenApiService).describeProjectProgress(any(ProjectControlRequest.class));
        Mockito.verify(scheduleTaskRepository).update(any(ScheduleTaskEntity.class));
    }
}