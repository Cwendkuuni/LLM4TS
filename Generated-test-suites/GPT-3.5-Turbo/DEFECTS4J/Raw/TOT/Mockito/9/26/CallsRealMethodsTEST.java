import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer() throws Throwable {
        // create a mock instance of InvocationOnMock
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // create an instance of CallsRealMethods
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        
        // call the answer method and check the return value
        Object returnValue = callsRealMethods.answer(invocation);
        assertEquals(invocation.callRealMethod(), returnValue);
    }
    
    @Test
    public void testAnswerWithNullInvocation() throws Throwable {
        // create an instance of CallsRealMethods
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        
        // call the answer method with null invocation
        Object returnValue = callsRealMethods.answer(null);
        
        // null invocation should result in null return value
        assertNull(returnValue);
    }
    
    @Test(expected = Throwable.class)
    public void testAnswerWithException() throws Throwable {
        // create a mock instance of InvocationOnMock
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // create an instance of CallsRealMethods
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        
        // throw an exception when callRealMethod is called
        doThrow(new Throwable()).when(invocation).callRealMethod();
        
        // call the answer method and expect an exception
        callsRealMethods.answer(invocation);
    }
}