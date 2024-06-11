import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.json.JsonUtils;
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
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
public class ScheduleCheckOmsProjectValveTest {

 private ProjectOpenApiService projectOpenApiService;
 private ScheduleTaskRepository scheduleTaskRepository;
 private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
 private OscTaskCompleteHandler completeHandler;
 private DefaultTableFactory defaultTableFactory;

 @BeforeEach
 public void setup() {
 projectOpenApiService = mock(ProjectOpenApiService.class);
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 onlineSchemaChangeProperties = mock(OnlineSchemaChangeProperties.class);
 completeHandler = mock(OscTaskCompleteHandler.class);
 defaultTableFactory = mock(DefaultTableFactory.class);
 }

 @Test
 public void testInvoke_Success() {
 // Given
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 taskParameter.setOmsProjectId("projectId");
 taskParameter.setDatabaseName("databaseName");
 taskParameter.setUid(UUID.randomUUID().toString());
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId("1");
 scheduleTask.setResultJson("");

 when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
 .thenReturn(getTestProjectSteps());
 when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
 .thenAnswer(new Answer<ProjectProgressResponse>() {
 @Override
 public ProjectProgressResponse answer(InvocationOnMock invocation) throws Throwable {
 return new ProjectProgressResponse();
 }
 });
 when(projectOpenApiService.resumeProject(any(ProjectControlRequest.class)))
 .thenReturn(null);

 doAnswer(invocation -> {
 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
 adaptResult(result, new ProjectStepResult(new ProjectStepVO(), FullVerificationResult.CONSISTENT, null));
 return result;
 })
 .when(projectOpenApiService)
 .listProjectFullVerifyResult(ArgumentMatchers.eq(taskParameter.getOmsProjectId())
 , ArgumentMatchers.eq(taskParameter.getDatabaseName())
 , ArgumentMatchers.eq(taskParameter.getUid()));

 when(scheduleTaskRepository.update(ArgumentMatchers.eq(scheduleTask)))
 .thenReturn(scheduleTask);

 ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
 valve.setProjectOpenApiService(projectOpenApiService);
 valve.setScheduleTaskRepository(scheduleTaskRepository);
 valve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
 valve.setCompleteHandler(completeHandler);
 valve.setDefaultTableFactory(defaultTableFactory);

 OscValveContext valveContext = new OscValveContext() {{
 setScheduleTask(scheduleTask);
 setTaskParameter(taskParameter);
 }};

 // When
 valve.invoke(valveContext);

 // Then
 Assertions.assertEquals(TaskStatus.DONE, valve.getLastTaskStatus());
 }

 @Test
 public void testInvoke_Exception() {
 // Given
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 taskParameter.setOmsProjectId("projectId");
 taskParameter.setDatabaseName("databaseName");
 taskParameter.setUid(UUID.randomUUID().toString());
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId("1");
 scheduleTask.setResultJson("");

 when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
 .thenReturn(getTestProjectSteps());
 when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
 .thenThrow(new OscException("test error"));

 doAnswer(invocation -> {
 throw new OscException("test error");
 })
 .when(projectOpenApiService)
 .listProjectFullVerifyResult(ArgumentMatchers.eq(taskParameter.getOmsProjectId())
 , ArgumentMatchers.eq(taskParameter.getDatabaseName())
 , ArgumentMatchers.eq(taskParameter.getUid()));

 ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
 valve.setProjectOpenApiService(projectOpenApiService);
 valve.setScheduleTaskRepository(scheduleTaskRepository);
 valve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
 valve.setCompleteHandler(completeHandler);
 valve.setDefaultTableFactory(defaultTableFactory);

 OscValveContext valveContext = new OscValveContext() {{
 setScheduleTask(scheduleTask);
 setTaskParameter(taskParameter);
 }};

 // When & Then
 Assertions.assertThrows(OscException.class, () -> valve.invoke(valveContext));
 }

 private List<ProjectStepVO> getTestProjectSteps() {
 List<ProjectStepVO> projectSteps = Lists.newArrayList();
 ProjectStepVO projectStepVO = new ProjectStepVO();
 projectStepVO.setName("PRE_CHECK");
 projectStepVO.setStatus("RUNNING");
 projectStepVO.setCurrentStep(1);
 projectStepVO.setFullTransferProgressPercentage(100);
 projectStepVO.setFullVerificationProgressPercentage(50);
 projectStepVO.setFullVerificationResult(FullVerificationResult.CONSISTENT.toString());
 projectSteps.add(projectStepVO);
 return projectSteps;
 }
}