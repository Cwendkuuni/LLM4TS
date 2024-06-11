import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

import com.google.common.collect.Lists;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
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

import java.util.List;

class ScheduleCheckOmsProjectValveTest {

 @InjectMocks
 private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

 @BeforeEach
 void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 void testInvoke_success() {
 // Given
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 ProjectControlRequest projectRequest = Mockito.spy(new ProjectControlRequest());
 ProjectOpenApiService projectOpenApiService = Mockito.mock(ProjectOpenApiService.class);
 OscTaskCompleteHandler completeHandler = Mockito.mock(OscTaskCompleteHandler.class);
 ScheduleTaskRepository scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
 ScheduleTaskEntity scheduleTask = Mockito.mock(ScheduleTaskEntity.class);
 OnlineSchemaChangeProperties onlineSchemaChangeProperties = Mockito.mock(OnlineSchemaChangeProperties.class);

 scheduleCheckOmsProjectValve.setProjectOpenApiService(projectOpenApiService);
 scheduleCheckOmsProjectValve.setCompleteHandler(completeHandler);
 scheduleCheckOmsProjectValve.setScheduleTaskRepository(scheduleTaskRepository);
 scheduleCheckOmsProjectValve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);

 Mockito.when(scheduleTaskRepository.findById(anyLong()))
 .thenReturn(scheduleTask);
 Mockito.when(projectOpenApiService.describeProjectSteps(projectRequest))
 .thenReturn(Lists.newArrayList());
 Mockito.when(projectOpenApiService.describeProjectProgress(projectRequest))
 .thenReturn(new ProjectProgressResponse());
 Mockito.when(projectOpenApiService.resumeProject(projectRequest))
 .thenReturn(null);
 Mockito.when(projectOpenApiService.listProjectFullVerifyResult(any())).thenReturn(new ProjectFullVerifyResultResponse());

 // When
 scheduleCheckOmsProjectValve.invoke(null);

 // Then
 Mockito.verify(projectOpenApiService, times(2)).describeProjectSteps(projectRequest);
 Mockito.verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
 Mockito.verify(projectOpenApiService, times(1)).resumeProject(projectRequest);
 Mockito.verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any());
 Mockito.verifyNoMoreInteractions(projectOpenApiService);
 Mockito.verifyNoMoreInteractions(completeHandler);
 Mockito.verifyNoMoreInteractions(scheduleTaskRepository);

 assertTrue(scheduleTask.isResultJsonSet());
 assertEquals(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()), scheduleTask.getResultJson());
 }

 @Test
 void testInvoke_preCheckFailed() {
 // Given
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 ProjectControlRequest projectRequest = Mockito.spy(new ProjectControlRequest());
 ProjectOpenApiService projectOpenApiService = Mockito.mock(ProjectOpenApiService.class);
 OscTaskCompleteHandler completeHandler = Mockito.mock(OscTaskCompleteHandler.class);
 ScheduleTaskRepository scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
 ScheduleTaskEntity scheduleTask = Mockito.mock(ScheduleTaskEntity.class);
 OnlineSchemaChangeProperties onlineSchemaChangeProperties = Mockito.mock(OnlineSchemaChangeProperties.class);

 scheduleCheckOmsProjectValve.setProjectOpenApiService(projectOpenApiService);
 scheduleCheckOmsProjectValve.setCompleteHandler(completeHandler);
 scheduleCheckOmsProjectValve.setScheduleTaskRepository(scheduleTaskRepository);
 scheduleCheckOmsProjectValve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);

 Mockito.when(scheduleTaskRepository.findById(anyLong()))
 .thenReturn(scheduleTask);
 Mockito.when(projectOpenApiService.describeProjectSteps(projectRequest))
 .thenReturn(Lists.newArrayList());
 Mockito.when(projectOpenApiService.describeProjectProgress(projectRequest))
 .thenReturn(new ProjectProgressResponse());
 Mockito.when(projectOpenApiService.resumeProject(projectRequest))
 .thenReturn(null);
 Mockito.when(projectOpenApiService.listProjectFullVerifyResult(any()))
 .thenThrow(new OscException(ErrorCodes.OmsPreCheckFailed, "PreCheck Failed"));

 // When
 assertThrows(OscException.class, () -> scheduleCheckOmsProjectValve.invoke(null));

 // Then
 Mockito.verify(projectOpenApiService, times(2)).describeProjectSteps(projectRequest);
 Mockito.verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
 Mockito.verify(projectOpenApiService, times(1)).resumeProject(projectRequest);
 Mockito.verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any());
 Mockito.verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any());
 Mockito.verify(projectOpenApiService, times(1)).resumeProject(projectRequest);
 Mockito.verify(projectOpenApiService, times(1)).resumeProject(projectRequest);
 Mockito.verify(projectOpenApiService, times(1)).resumeProject(projectRequest);
 Mockito.verify(projectOpenApiService, times(1)).resumeProject(projectRequest);
 Mockito.verifyNoMoreInteractions(completeHandler);
 Mockito.verifyNoMoreInteractions(scheduleTaskRepository);
 }

 // Add more test cases for other scenarios here
}