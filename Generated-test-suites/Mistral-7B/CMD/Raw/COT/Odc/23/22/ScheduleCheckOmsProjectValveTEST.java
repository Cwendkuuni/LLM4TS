import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.oms.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.ProjectStepVO;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.onlineschemachange.utils.JsonUtils;

/**
 * ScheduleCheckOmsProjectValveTest.java
 */
@Slf4j
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
  private ProjectStepResultChecker projectStepResultChecker;

  @Mock
  private SwapTableUtil swapTableUtil;

  @InjectMocks
  private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testInvoke_success() throws Exception {
    // Given
    OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
    ProjectControlRequest projectRequest = mock(ProjectControlRequest.class);
    List<ProjectStepVO> projectSteps = Lists.newArrayList();
    ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
    ProjectFullVerifyResultResponse fullVerifyResponse = mock(ProjectFullVerifyResultResponse.class);

    when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
    when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
    when(projectOpenApiService.listProjectFullVerifyResult(any())).thenReturn(fullVerifyResponse);
    when(scheduleTaskRepository.update(any())).thenReturn(true);

    // When
    ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
    scheduleTask.setId(1L);
    scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

    scheduleCheckOmsProjectValve.invoke(createValveContext(scheduleTask, taskParameter));

    // Then
    verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
    verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
    verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any());
    verify(scheduleTaskRepository, times(1)).update(scheduleTask);
  }

  @Test(expected = OscException.class)
  public void testInvoke_projectStepFailure() throws Exception {
    // Given
    OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
    ProjectControlRequest projectRequest = mock(ProjectControlRequest.class);
    List<ProjectStepVO> projectSteps = Lists.newArrayList();
    ProjectProgressResponse progress = new ProjectProgressResponse();
    progress.setCurrentStep(new ProjectStepVO());
    progress.getCurrentStep().setStatus(TaskStatus.FAILED.name());

    when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
    when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

    // When & Then
    assertThrows(OscException.class, () -> scheduleCheckOmsProjectValve.invoke(createValveContext(null, taskParameter)));

    verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
    verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
  }

  // Add more test cases for other methods in the class, such as handleOmsProjectStepResult(), listProjectFullVerifyResult(), adaptResult(), getProjectRequest(), recordCurrentProgress(), and getHeader()

  private ValveContext createValveContext(ScheduleTaskEntity scheduleTask, OnlineSchemaChangeScheduleTaskParameters taskParameter) {
    OscValveContext oscValveContext = mock(OscValveContext.class);
    when(oscValveContext.getScheduleTask()).thenReturn(scheduleTask);
    when(oscValveContext.getTaskParameter()).thenReturn(taskParameter);
    return oscValveContext;
  }
}