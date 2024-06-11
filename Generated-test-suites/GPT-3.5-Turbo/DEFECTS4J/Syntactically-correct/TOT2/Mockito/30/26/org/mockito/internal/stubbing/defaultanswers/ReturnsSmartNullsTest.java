import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerReturnsSmartNulls() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        
        Answer<Object> answer = returnsSmartNulls;
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        Object result = answer.answer(invocation);
        
        assertNotNull("SmartNull should not be null", result);
        assertEquals("SmartNull returned by unstubbed method on mock", result);
    }
    
    @Test
    public void testAnswerReturnsSmartNullsException() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        
        Answer<Object> answer = returnsSmartNulls;
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        ClassImposterizer.INSTANCE = null;
        
        try {
            answer.answer(invocation);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            // Pass
        }
    }
    
    @Test
    public void testThrowingInterceptorIntercept() throws Throwable {
        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = new ReturnsSmartNulls().new ThrowingInterceptor(mock(InvocationOnMock.class));
        
        Object result = throwingInterceptor.intercept(new Object(), Object.class.getMethod("hashCode"), new Object[]{}, null);
        
        assertNotNull("SmartNull from ThrowingInterceptor should not be null", result);
        assertEquals("SmartNull returned by unstubbed hashCode() method on mock", result);
    }
}