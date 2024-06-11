import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.google.common.collect.Lists;
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

@Slf4j
@ActiveProfiles("test")
public class ScheduleCheckOmsProjectValveTest {

    @MockBean
    private ProjectOpenApiService projectOpenApiService;
    @MockBean
    private OscTaskCompleteHandler completeHandler;
    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;
    @MockBean
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    @Autowired
    private DefaultTableFactory defaultTableFactory;

    @Value("${test.projectId}")
    private String projectId;
    @Value("${test.databaseName}")
    private String databaseName;
    @Value("${test.uid}")
    private String uid;

    private ScheduleCheckOmsProjectValve valve;
    private OnlineSchemaChangeScheduleTaskParameters taskParameter;
    private ScheduleTaskEntity scheduleTask;
    private ProjectControlRequest projectRequest;
    private ProjectStepVO projectStepVo;
    private ProjectProgressResponse projectProgressResponse;
    private ProjectFullVerifyResultResponse projectFullVerifyResultResponse;

    @BeforeEach
    public void setup() {
        valve = new ScheduleCheckOmsProjectValve();
        valve.setProjectOpenApiService(projectOpenApiService);
        valve.setCompleteHandler(completeHandler);
        valve.setScheduleTaskRepository(scheduleTaskRepository);
        valve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);

        taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId(projectId);
        taskParameter.setDatabaseName(databaseName);
        taskParameter.setUid(uid);

        projectRequest = new ProjectControlRequest();
        projectRequest.setUid(uid);
        projectRequest.setId(projectId);

        projectStepVo = new ProjectStepVO();
        projectStepVo.setId("1");
        projectStepVo.setStatus(TaskStatus.DONE.name());
        projectStepVo.setName(OmsStepName.PRE_CHECK.name());
        projectStepVo.setProgressPercentage(100);

        projectProgressResponse = new ProjectProgressResponse();
        projectProgressResponse.setSteps(Lists.newArrayList(projectStepVo));

        projectFullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        projectFullVerifyResultResponse.setProjectId(projectId);
        projectFullVerifyResultResponse.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectFullVerifyResultResponse.setFullVerificationResultDescription("Success");

        Mockito.when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepVo));
        Mockito.when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(projectProgressResponse);
        Mockito.when(projectOpenApiService.listProjectFullVerifyResult(Mockito.any(ListProjectFullVerifyResultRequest.class))).thenReturn(projectFullVerifyResultResponse);
    }

    @Test
    public void testInvokeWithConsistentData() {
        OnlineSchemaChangeScheduleTaskResult result = valve.invoke(new OscValveContext(taskParameter, null, null, null, scheduleTask));
        Assertions.assertEquals(TaskStatus.DONE.name(), result.getCurrentStepStatus());
        Assertions.assertEquals(100, result.getProgressPercentage());
        Assertions.assertEquals(FullVerificationResult.CONSISTENT, result.getFullVerificationResult());
    }

    @Test
    public void testInvokeWithInconsistentData() {
        projectStepVo.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        projectStepVo.setFullVerificationResultDescription("Data inconsistent");

        projectProgressResponse.setSteps(Lists.newArrayList(projectStepVo));

        OnlineSchemaChangeScheduleTaskResult result = valve.invoke(new OscValveContext(taskParameter, null, null, null, scheduleTask));
        Assertions.assertEquals(TaskStatus.FAILED.name(), result.getCurrentStepStatus());
        Assertions.assertNull(result.getProgressPercentage());
        Assertions.assertEquals(FullVerificationResult.INCONSISTENT, result.getFullVerificationResult());
        Assertions.assertEquals("Data inconsistent", result.getFullVerificationResultDescription());
    }

    @Test
    public void testInvokeWithFailedPreCheck() {
        projectStepVo.setPreCheckResult(PrecheckResult.FAILED);
        projectStepVo.setErrorMsg("Precheck failed");

        projectProgressResponse.setSteps(Lists.newArrayList(projectStepVo));

        OnlineSchemaChangeScheduleTaskResult result = valve.invoke(new OscValveContext(taskParameter, null, null, null, scheduleTask));
        Assertions.assertEquals(TaskStatus.FAILED.name(), result.getCurrentStepStatus());
        Assertions.assertNull(result.getProgressPercentage());
        Assertions.assertEquals(PrecheckResult.FAILED, result.getPrecheckResult());
        Assertions.assertEquals("Precheck failed", result.getPrecheckResultDescription());
    }

    @Test
    public void testInvokeWithSuspendedProject() {
        projectStepVo.setStatus(TaskStatus.SUSPENDED.name());

        projectProgressResponse.setSteps(Lists.newArrayList(projectStepVo));

        OnlineSchemaChangeScheduleTaskResult result = valve.invoke(new OscValveContext(taskParameter, null, null, null, scheduleTask));
        Assertions.assertEquals(TaskStatus.SUSPENDED.name(), result.getCurrentStepStatus());
        Assertions.assertNull(result.getProgressPercentage());
    }

    @Test
    public void testInvokeWithRunningProject() {
        projectStepVo.setStatus(TaskStatus.RUNNING.name());

        projectProgressResponse.setSteps(Lists.newArrayList(projectStepVo));

        OnlineSchemaChangeScheduleTaskResult result = valve.invoke(new OscValveContext(taskParameter, null, null, null, scheduleTask));
        Assertions.assertEquals(TaskStatus.RUNNING.name(), result.getCurrentStepStatus());
        Assertions.assertNull(result.getProgressPercentage());
    }

    @Test
    public void testInvokeWithErrorInProjectOpenApiService() {
        Mockito.when(projectOpenApiService.describeProjectSteps(projectRequest)).thenThrow(new RuntimeException("Error in describeProjectSteps"));

        OnlineSchemaChangeScheduleTaskResult result = valve.invoke(new OscValveContext(taskParameter, null, null, null, scheduleTask));
        Assertions.assertEquals(TaskStatus.FAILED.name(), result.getCurrentStepStatus());
        Assertions.assertNull(result.getProgressPercentage());
        Assertions.assertNull(result.getFullVerificationResult());
        Assertions.assertNull(result.getFullVerificationResultDescription());
    }
}