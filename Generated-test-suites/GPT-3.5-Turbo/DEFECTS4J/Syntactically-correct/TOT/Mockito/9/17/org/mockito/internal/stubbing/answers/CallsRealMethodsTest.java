import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class CallsRealMethodsTest {
    
    private CallsRealMethods callsRealMethods;
    private InvocationOnMock invocation;
    
    @Before
    public void setUp() {
        callsRealMethods = new CallsRealMethods();
        invocation = mock(InvocationOnMock.class);
    }
    
    @Test
    public void testAnswer_Success() throws Throwable {
        // Arrange
        doCallRealMethod().when(invocation).callRealMethod();
        
        // Act
        Object result = callsRealMethods.answer(invocation);
        
        // Assert
        assertNull(result);
    }
    
    @Test(expected = Throwable.class)
    public void testAnswer_Throwable() throws Throwable {
        // Arrange
        doThrow(new Throwable()).when(invocation).callRealMethod();
        
        // Act
        callsRealMethods.answer(invocation);
    }
    
    @Test(expected = NullPointerException.class)
    public void testAnswer_NullPointerException() throws Throwable {
        // Arrange
        doThrow(new NullPointerException()).when(invocation).callRealMethod();
        
        // Act
        callsRealMethods.answer(invocation);
    }
}