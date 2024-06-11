
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.common.util.tableformat.Table;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.pipeline.OscValveContext;
import com.oceanbase.odc.service.onlineschemachange.pipepline.ValveContext;
import com.oceanbase.odc.service.onlineschemachange.model.*;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve valve;

    @Mock
    private OscValveContext mockOscValveContext;
    
    @Mock
    private ScheduleTaskEntity mockScheduleTask;
    
    @Mock
    OnlineSchemaChangeScheduleTaskParameters mockTaskParameters;

    @Mock
    Table mockTable;

    @Test
    public void test_invoke() {
        Mockito.when(mockOscValveContext.getScheduleTask()).thenReturn(mockScheduleTask);
        Mockito.when(mockScheduleTask.getStatus()).thenReturn(TaskStatus.IN_PROGRESS);

        valve.invoke(mockOscValveContext);
        verify(mockOscValveContext, times(1)).getScheduleTask();
    }

    @Test(expected = OscException.class)
    public void test_continueHandleProjectStepResult_withPrecheckFailed() {
        ProjectStepResult mockResult = new ProjectStepResult();
        mockResult.setPreCheckResult(PrecheckResult.FAILED);

        valve.continueHandleProjectStepResult(mockResult);
    }

    @Test(expected = OscException.class)
    public void test_continueHandleProjectStepResult_withFullVerificationFailed() {
        ProjectStepResult mockResult = new ProjectStepResult();
        mockResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);

        valve.continueHandleProjectStepResult(mockResult);
    }

    @Test
    public void test_recordCurrentProgress() {
        OnlineSchemaChangeScheduleTaskResult mockResult = Mockito.mock(OnlineSchemaChangeScheduleTaskResult.class);

        Mockito.when(mockResult.getCurrentStep()).thenReturn("PRE_CHECK");
        Mockito.when(mockResult.getPrecheckResultDescription()).thenReturn("Test description");
        
        valve.recordCurrentProgress("123", mockResult);

        assertEquals(OnlineSchemaChangeScheduleTaskResult.getCurrentStep(), mockResult.getCurrentStep());
        assertEquals(OnlineSchemaChangeScheduleTaskResult.getPrecheckResultDescription(), mockResult.getPrecheckResultDescription());
    }
}
