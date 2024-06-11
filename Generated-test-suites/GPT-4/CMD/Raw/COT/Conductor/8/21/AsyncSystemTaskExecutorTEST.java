import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AsyncSystemTaskExecutorTest {
    private AsyncSystemTaskExecutor executor;

    @Mock
    private ExecutionDAOFacade executionDAOFacade;
    @Mock
    private QueueDAO queueDAO;
    @Mock
    private MetadataDAO metadataDAO;
    @Mock
    private ConductorProperties conductorProperties;
    @Mock
    private WorkflowExecutor workflowExecutor;
    @Mock
    private WorkflowSystemTask systemTask;
    private final String taskId = "taskId";

    @Before
    public void setUp() {
        executor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testConstructor() {
        assertNotNull(executor);
    }

    @Test
    public void testExecuteTaskNotExistence() {
        when(executionDAOFacade.getTaskModel(anyString())).thenThrow(new RuntimeException());
        executor.execute(systemTask, taskId);
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
    }

    @Test
    public void testExecuteTaskTerminalStatus() {
        TaskModel task = mock(TaskModel.class);
        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        executor.execute(systemTask, taskId);
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    // Similar tests can be conducted for all possible scenarios and edge cases.
}