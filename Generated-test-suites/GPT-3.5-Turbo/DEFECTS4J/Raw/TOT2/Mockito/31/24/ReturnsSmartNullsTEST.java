import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_OrdinaryReturnValue() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsSmartNulls.answer(invocation);
        
        // Mock an ordinary method call
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        Object result = answer.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof String);
    }

    @Test
    public void testAnswer_NullReturnValue() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsSmartNulls.answer(invocation);
        
        // Mock a method call with null return value
        when(invocation.getMethod().getReturnType()).thenReturn(Void.class);
        Object result = answer.answer(invocation);
        
        assertNull(result);
    }

    @Test
    public void testAnswer_SmartNullReturnValue() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsSmartNulls.answer(invocation);
        
         // Mock a method call that returns smart null
        when(invocation.getMethod().getReturnType()).thenReturn(ClassImposterizer.INSTANCE.getClass());
        Object result = answer.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof MethodProxy);
    }

    @Test
    public void testAnswer_ExceptionHandling() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsSmartNulls.answer(invocation);
        
        // Mock a method call that throws an exception
        when(invocation.getMethod().getReturnType()).thenThrow(new Exception("Error"));
        
        try {
            Object result = answer.answer(invocation);
        } catch (Exception e) {
            assertNotNull(e);
            assertTrue(e instanceof Exception);
            assertEquals("Error", e.getMessage());
        }
    }
}