import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

public class CallsRealMethodsTest {

    private CallsRealMethods callsRealMethods;

    @Before
    public void setUp() {
        callsRealMethods = new CallsRealMethods();
    }

    @After
    public void tearDown() {
        callsRealMethods = null;
    }

    @Test
    public void testAnswer_withInvocation_shouldCallRealMethod() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.callRealMethod()).thenReturn("Mockito");
        
        Object result = callsRealMethods.answer(invocation);
        
        verify(invocation).callRealMethod();
        assertEquals("Mockito", result);
    }
    
    @Test
    public void testAnswer_withNullInvocation_shouldThrowException() throws Throwable {
        InvocationOnMock invocation = null;
        
        try {
            callsRealMethods.answer(invocation);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected exception thrown
        }
    }
    
    @Test
    public void testAnswer_withRealMethodThrowingException_shouldThrowSameException() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.callRealMethod()).thenThrow(new IllegalArgumentException());
        
        try {
            callsRealMethods.answer(invocation);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception thrown
        }
        
        verify(invocation).callRealMethod();
    }

}