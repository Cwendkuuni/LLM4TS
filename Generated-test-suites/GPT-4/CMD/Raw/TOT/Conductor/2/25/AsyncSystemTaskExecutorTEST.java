import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;
    
    @Before
    public void setUp() {
        // Initialize necessary objects
    }

    @Test
    public void testLoadTaskQuietly_seccess() {
        // given
        final TaskModel mockedTaskModel = new TaskModel();
        when(executionDAOFacade.getTaskModel(any())).thenReturn(mockedTaskModel);

        // when
        final TaskModel result = AsyncSystemTaskExecutor.loadTaskQuietly("testId");
        
        // then
        assertEquals(mockedTaskModel, result);
    }

    @Test
    public void testLoadTaskQuietly_noSuchTask() {
        // given
        when(executionDAOFacade.getTaskModel(any())).thenReturn(null);
        
        // when
        final TaskModel result = AsyncSystemTaskExecutor.loadTaskQuietly("testId");
        
        // then
        assertNull(result);
    }

    @Test
    public void testLoadTaskQuietly_exceptionOccurred() {
        // given
        when(executionDAOFacade.getTaskModel(any())).thenThrow(new RuntimeException());
        
        // when
        final TaskModel result = AsyncSystemTaskExecutor.loadTaskQuietly("testId");
        
        // then
        assertNull(result);
    }
}