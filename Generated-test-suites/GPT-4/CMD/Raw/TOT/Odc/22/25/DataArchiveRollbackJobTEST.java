import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doNothing;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;

import java.util.Arrays;
import java.util.Optional;


public class DataArchiveRollbackJobTest {
    
    @InjectMocks
    private DataArchiveRollbackJob testDataArchiveRollbackJob;
    
    @Mock
    private JobExecutionContext mockContext;
    
    @Mock
    private ScheduleTaskEntity mockTaskEntity;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testExecute_whenDataArchiveTaskIsPresent_thenShouldUpdateStatus() {
        // Prepare test data
        DlmTask dlmTaskToExecute = new DlmTask();
        dlmTaskToExecute.setSourceDatabaseId(1L);
        
        Optional<ScheduleTaskEntity> optionalTask = Optional.of(new ScheduleTaskEntity());
        optionalTask.get().setResultJson(JsonUtils.toJson(Arrays.asList(dlmTaskToExecute)));

        // Configure mocks
        doReturn(mockTaskEntity).when(mockContext).getResult();
        doReturn("foo").when(mockTaskEntity).getParametersJson();
        doReturn(optionalTask).when(scheduleTaskRepository).findById(anyString());
        
        // Execute method and verify result
        testDataArchiveRollbackJob.execute(mockContext);
        
        verify(scheduleTaskRepository, atLeastOnce()).updateStatusById(anyLong(), any(TaskStatus.class));
    }
    
    @Test
    public void testExecute_whenDataArchiveTaskIsNotPresent_thenShouldUpdateStatusToFailed() {
        // Configure mocks
        doReturn(mockTaskEntity).when(mockContext).getResult();
        doReturn("foo").when(mockTaskEntity).getParametersJson();
        doReturn(Optional.empty()).when(scheduleTaskRepository).findById(anyString());
        
        // Execute method and verify result
        testDataArchiveRollbackJob.execute(mockContext);
        
        verify(scheduleTaskRepository, atLeastOnce()).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }
}
