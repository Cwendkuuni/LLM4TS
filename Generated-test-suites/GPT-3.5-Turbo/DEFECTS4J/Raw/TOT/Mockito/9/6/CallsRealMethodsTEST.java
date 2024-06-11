import org.junit.Assert;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer_callsRealMethod() throws Throwable {
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object expectedResult = new Object();
        
        Mockito.when(invocation.callRealMethod()).thenReturn(expectedResult);
        
        Object actualResult = callsRealMethods.answer(invocation);
        
        Assert.assertEquals(expectedResult, actualResult);
        Mockito.verify(invocation, Mockito.times(1)).callRealMethod();
    }
    
    @Test
    public void testAnswer_callsRealMethodForDifferentArguments() throws Throwable {
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object expectedResult1 = new Object();
        Object expectedResult2 = new Object();
        
        Mockito.when(invocation.callRealMethod())
               .thenReturn(expectedResult1)
               .thenReturn(expectedResult2);
        
        Object actualResult1 = callsRealMethods.answer(invocation);
        Object actualResult2 = callsRealMethods.answer(invocation);
        
        Assert.assertEquals(expectedResult1, actualResult1);
        Assert.assertEquals(expectedResult2, actualResult2);
        Mockito.verify(invocation, Mockito.times(2)).callRealMethod();
    }
}