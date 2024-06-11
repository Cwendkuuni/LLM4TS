import org.junit.Assert;
import org.junit.Test;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_returnsDefaultValue() throws Throwable {
        // Setup
        ReturnsMoreEmptyValues delegate = Mockito.mock(ReturnsMoreEmptyValues.class);
        Answer<Object> answer = new ReturnsSmartNulls(delegate);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(delegate.answer(Mockito.any(InvocationOnMock.class))).thenReturn(null);
        
        // Execution
        Object result = answer.answer(invocation);
        
        // Verification
        Assert.assertEquals(null, result);
    }
    
    @Test
    public void testAnswer_returnsSmartNull() throws Throwable {
        // Setup
        ReturnsMoreEmptyValues delegate = Mockito.mock(ReturnsMoreEmptyValues.class);
        Answer<Object> answer = new ReturnsSmartNulls(delegate);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        
        Method toStringMethod = Object.class.getDeclaredMethod("toString");
        Mockito.when(invocation.getMethod()).thenReturn(toStringMethod);
        Mockito.when(delegate.answer(Mockito.any(InvocationOnMock.class))).thenReturn(null);
        
        // Execution
        Object result = answer.answer(invocation);
        
        // Verification
        Assert.assertEquals("SmartNull returned by unstubbed toString method on mock", result);
    }
    
    @Test
    public void testAnswer_RaisesException() throws Throwable {
        // Setup
        ReturnsMoreEmptyValues delegate = Mockito.mock(ReturnsMoreEmptyValues.class);
        Answer<Object> answer = new ReturnsSmartNulls(delegate);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        Method toStringMethod = Object.class.getDeclaredMethod("toString");
        Mockito.when(invocation.getMethod()).thenReturn(toStringMethod);
        Mockito.when(delegate.answer(Mockito.any(InvocationOnMock.class))).thenThrow(new NullPointerException());
        
        // Execution & Verification
        try {
            answer.answer(invocation);
            Assert.fail("Expected NullPointerException");
        } catch(NullPointerException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testAnswer_returnsNullForFinalTypes() throws Throwable {
        // Setup
        ReturnsMoreEmptyValues delegate = Mockito.mock(ReturnsMoreEmptyValues.class);
        Answer<Object> answer = new ReturnsSmartNulls(delegate);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        
        Method method = ReturnsSmartNulls.class.getDeclaredMethod("answer", InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(delegate.answer(Mockito.any(InvocationOnMock.class))).thenReturn(null);
        
        // Execution
        Object result = answer.answer(invocation);
        
        // Verification
        Assert.assertEquals(null, result);
    }
    
    @Test
    public void testAnswer_returnsImposterizedObject() throws Throwable {
        // Setup
        ReturnsMoreEmptyValues delegate = Mockito.mock(ReturnsMoreEmptyValues.class);
        Answer<Object> answer = new ReturnsSmartNulls(delegate);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        
        Method method = ReturnsSmartNulls.class.getDeclaredMethod("answer", InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        
        ClassImposterizer classImposterizer = Mockito.mock(ClassImposterizer.class);
        Class<?> returnType = Mockito.mock(Class.class);
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(returnType);
        Mockito.when(ClassImposterizer.INSTANCE.canImposterise(returnType)).thenReturn(true);
        Mockito.when(ClassImposterizer.INSTANCE.imposterise(Mockito.any(ThrowsInterceptor.class), Mockito.eq(returnType))).thenReturn(Mockito.mock(Object.class));
        
        // Execution
        Object result = answer.answer(invocation);
        
        // Verification
        Assert.assertNotNull(result);
        Mockito.verify(delegate).answer(Mockito.any(InvocationOnMock.class));
        Mockito.verify(ClassImposterizer.INSTANCE).canImposterise(returnType);
        Mockito.verify(ClassImposterizer.INSTANCE).imposterise(Mockito.any(ThrowsInterceptor.class), Mockito.eq(returnType));
    }
}