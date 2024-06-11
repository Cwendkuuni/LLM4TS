import org.junit.*;
import org.mockito.*;

import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest{
   
   @Mock
   ExecutionDAOFacade executionDAOFacadeMock;

   @Mock
   QueueDAO queueDAOMock;

   @Mock
   MetadataDAO metadataDAOMock;
   
   @Mock
   ConductorProperties conductorPropertiesMock;

   @Mock
   WorkflowExecutor workflowExecutorMock;
   
   @Mock
   WorkflowSystemTask systemTaskMock;

   @Mock
   TaskModel taskModelMock;
   
   @Mock
   WorkflowModel workflowModelMock;

   @InjectMocks
   AsyncSystemTaskExecutor asyncSystemTaskExecutorTest;

   @Before
   public void setUp() throws Exception{
       MockitoAnnotations.initMocks(this);
   }
   
   @Test
   public void testExecuteTaskIsNull() {
       when(executionDAOFacadeMock.getTaskModel(anyString())).thenReturn(null);
       asyncSystemTaskExecutorTest.execute(systemTaskMock, "fakeTaskId");
       verify(queueDAOMock, never()).remove(anyString(), anyString());
   }
   
   @Test
   public void testExceptionInLoadTaskQuietly() {
       when(executionDAOFacadeMock.getTaskModel(anyString())).thenThrow(new RuntimeException());
       asyncSystemTaskExecutorTest.execute(systemTaskMock, "fakeTaskId");
       verify(queueDAOMock, never()).remove(anyString(), anyString());
   }
   
   @Test
   public void testExecuteTaskStatusTerminal() {
       when(executionDAOFacadeMock.getTaskModel(anyString())).thenReturn(taskModelMock);
       when(taskModelMock.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
       asyncSystemTaskExecutorTest.execute(systemTaskMock, "fakeTaskId");
       verify(queueDAOMock).remove(anyString(), anyString());
   }
   
   //... similar test methods for other edge and exception cases

   @After
   public void tearDown() throws Exception{
       asyncSystemTaskExecutorTest = null;
   }
}