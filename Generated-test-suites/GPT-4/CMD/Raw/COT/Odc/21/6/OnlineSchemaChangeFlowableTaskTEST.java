
import org.junit.Test;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.core.shared.constant.TaskStatus;

import static org.mockito.ArgumentMatchers.*;

import java.util.List;
import java.util.ArrayList;

/**
 * Unit test for OnlineSchemaChangeFlowableTask class.
 *
 * @author Developer
 */
public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    private OnlineSchemaChangeFlowableTask task;
    
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private ScheduleService scheduleService;
    @Mock
    private ScheduleTaskService scheduleTaskService;
    @Mock
    private QuartzJobService quartzJobService;
    @Mock
    private OnlineSchemaChangeTaskHandler taskHandler;
    @Mock
    private OscTaskCompleteHandler completeHandler;
    @Mock
    private OrganizationService organizationService;

    @Before
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldStartSuccessfully() throws Exception {
        // preparatory steps (omitted)
        // Call the "start" method and assert a successful result
    }

    @Test
    public void IsSuccessfullTest() {
        task.status = TaskStatus.DONE;
        Assert.assertTrue(task.isSuccessful());
    }
    
    @Test
    public void IsFailureTest() {
        task.status = TaskStatus.FAILED;
        Assert.assertTrue(task.isFailure());
    }
    
    @Test
    public void IsCancelledTest() {
        task.status = TaskStatus.CANCELED;
        Assert.assertTrue(task.isCancelled());
    }
    
    @Test
    public void onTimeoutTest() {
        // preparatory steps (omitted)
        // Call the "onTimeout" method and assert correct behavior by verifying that dependent operations are called, such as log.warn
    }

    @Test
    public void OnProgressUpdateTest() throws Exception {
        // prepare mock list of tasks
        List<ScheduleTaskEntity> taskList = new ArrayList<>();
        ScheduleTaskEntity taskItem1 = new ScheduleTaskEntity();
        taskItem1.setProgressPercentage(75.0);
        taskList.add(taskItem1);
        ScheduleTaskEntity taskItem2 = new ScheduleTaskEntity();
        taskItem2.setProgressPercentage(25.0);
        taskList.add(taskItem2);
        
        // prepare mock Page<ScheduleTaskEntity>
        Page<ScheduleTaskEntity> page = Mockito.mock(Page.class);
        Mockito.when(page.getContent()).thenReturn(taskList);

        Mockito.when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(page);

        task.onProgressUpdate(101L, Mockito.mock(TaskService.class));

        Assert.assertEquals(50, task.percentage, 0);
    }
    
    @Test
    public void CancelTaskTest() throws Exception {
        // preparatory steps (omitted)
        // Call the "cancel" method and assert correct behavior by verifying that dependent operations are called like taskService.cancel
    }
    
    /* Other tests for remaining methods are omitted for the sake of brevity. Always remember to test happy path, edge cases, and exception handling. */
}
