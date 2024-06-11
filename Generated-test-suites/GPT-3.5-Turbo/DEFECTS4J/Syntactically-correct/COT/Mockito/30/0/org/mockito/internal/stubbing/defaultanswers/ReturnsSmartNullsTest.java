import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object result = answer.answer(invocation);
        
        Assert.assertNull(result);
    }
    
    @Test
    public void testAnswerWithDefaultReturnValue() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Answer<Object> delegate = Mockito.mock(Answer.class);
        Mockito.when(delegate.answer(invocation)).thenReturn("Test value");
        
        Object result = answer.answer(invocation);
        
        Assert.assertEquals("Test value", result);
    }
    
    @Test
    public void testAnswerWithMockableReturnType() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        
        Class<?> returnType = Mockito.mock(Class.class);
        Mockito.when(method.getReturnType()).thenReturn(returnType);
        Mockito.when(ClassImposterizer.INSTANCE.canImposterise(returnType)).thenReturn(true);
        
        Object result = answer.answer(invocation);
        
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testAnswerWithNonMockableReturnType() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        
        Class<?> returnType = Mockito.mock(Class.class);
        Mockito.when(method.getReturnType()).thenReturn(returnType);
        Mockito.when(ClassImposterizer.INSTANCE.canImposterise(returnType)).thenReturn(false);
        
        Object result = answer.answer(invocation);
        
        Assert.assertNull(result);
    }
    
}