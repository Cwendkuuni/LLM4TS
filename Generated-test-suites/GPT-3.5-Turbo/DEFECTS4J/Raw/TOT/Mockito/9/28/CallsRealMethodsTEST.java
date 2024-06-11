import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CallsRealMethodsTest {

    @Test
    public void answer_InvokesCallRealMethod() throws Throwable {
        // Arrange
        CallsRealMethods answer = new CallsRealMethods();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Act
        Object result = answer.answer(invocation);
        
        // Assert
        verify(invocation).callRealMethod();
    }
}