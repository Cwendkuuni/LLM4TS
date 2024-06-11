import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private ScheduleTaskService scheduleTaskService;
    
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    
    @Mock
    private TaskService taskService;
    
    @Mock
    private DelegateExecution execution;
    
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    
    @Before
    public void setup() {
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskService);
        onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepository);
        onlineSchemaChangeFlowableTask.setTaskService(taskService);
    }
    
    @Test
    public void testStart() {
        Long taskId = 1L;
        
        // Mocking the necessary objects
        User creator = new User();
        Mockito.when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(creator);
        
        ConnectionConfig connectionConfig = new ConnectionConfig();
        Mockito.when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        Mockito.when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(parameter);
        
        String schema = "test_schema";
        Mockito.when(FlowTaskUtil.getSchemaName(execution)).thenReturn(schema);
        
        List<ScheduleTaskEntity> tasks = new ArrayList<>();
        Mockito.when(scheduleTaskService.listTask(Pageable.unpaged(), 1L)).thenReturn(new Page<ScheduleTaskEntity>() {
            @Override
            public int getTotalPages() {
                return 1;
            }
            
            @Override
            public long getTotalElements() {
                return tasks.size();
            }
            
            @Override
            public <U> Page<U> map(Function<? super ScheduleTaskEntity, ? extends U> converter) {
                return null;
            }
            
            @Override
            public int getNumber() {
                return 1;
            }
            
            @Override
            public int getSize() {
                return tasks.size();
            }
            
            @Override
            public int getNumberOfElements() {
                return tasks.size();
            }
            
            @Override
            public List<ScheduleTaskEntity> getContent() {
                return tasks;
            }
            
            @Override
            public boolean hasContent() {
                return !tasks.isEmpty();
            }
            
            @Override
            public Sort getSort() {
                return null;
            }
            
            @Override
            public boolean hasPrevious() {
                return false;
            }
            
            @Override
            public Pageable nextPageable() {
                return null;
            }
            
            @Override
            public Pageable previousPageable() {
                return null;
            }
            
            @Override
            public boolean isFirst() {
                return false;
            }
            
            @Override
            public boolean isLast() {
                return false;
            }
            
            @Override
            public boolean hasNext() {
                return false;
            }
        });
        
        OnlineSchemaChangeTaskResult taskResult = new OnlineSchemaChangeTaskResult(tasks);
        
        // Executing the method under test
        try {
            onlineSchemaChangeFlowableTask.start(taskId, taskService, execution);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Verifying the results
        assertEquals(1, onlineSchemaChangeFlowableTask.getCreatorId());
        assertEquals(1, onlineSchemaChangeFlowableTask.getOrganizationId());
        assertEquals(TaskStatus.RUNNING, onlineSchemaChangeFlowableTask.getStatus());
        assertEquals(1L, onlineSchemaChangeFlowableTask.getScheduleId());
    }

    @Test
    public void testIsSuccessful() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);
        
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);
        
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnProgressUpdate() {
        // Mocking the necessary objects
        ScheduleTaskEntity task = new ScheduleTaskEntity();
        task.setStatus(TaskStatus.RUNNING);
        task.setProgressPercentage(50.0);
        List<ScheduleTaskEntity> tasks = new ArrayList<>();
        tasks.add(task);
        
        Mockito.when(scheduleTaskService.listTask(Pageable.unpaged(), 1L)).thenReturn(new Page<ScheduleTaskEntity>() {
            @Override
            public int getTotalPages() {
                return 1;
            }
            
            // Implement other methods as needed for the test
            
        });
        
        // Executing the method under test
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);
        
        // Verifying the results
        assertEquals(50.0, onlineSchemaChangeFlowableTask.getPercentage(), 0.1);
    }

    @Test
    public void testCancel() {
        Long taskId = 1L;
        
        ScheduleTaskEntity runningEntity = new ScheduleTaskEntity();
        runningEntity.setStatus(TaskStatus.RUNNING);
        List<ScheduleTaskEntity> tasks = new ArrayList<>();
        tasks.add(runningEntity);
        
        Mockito.when(scheduleTaskService.listTask(Pageable.unpaged(), 1L)).thenReturn(new Page<ScheduleTaskEntity>() {
            @Override
            public List<ScheduleTaskEntity> getContent() {
                return tasks;
            }
            
            // Implement other methods as needed for the test
            
        });
        
        assertTrue(onlineSchemaChangeFlowableTask.cancel(true, taskId, taskService));
        assertEquals(TaskStatus.CANCELED, onlineSchemaChangeFlowableTask.getStatus());
    }

    @Test
    public void testIsCancelled() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.CANCELED);
        
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }
}