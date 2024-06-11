import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class CallsRealMethodsTest {

    @Test
    public void testAnswerMethodWithNullInvocation() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        Assert.assertNull(callRealMethods.answer(null));
    }

    @Test
    public void testAnswerMethodWithValidInvocation() {
        Object expectedResult = new Object();
        CallsRealMethods callRealMethods = new CallsRealMethods();
        InvocationOnMock invocationMock = Mockito.mock(InvocationOnMock.class);
        ArgumentCaptor<InvocationOnMock> invocationCaptor = ArgumentCaptor.forClass(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(new Method("testMethod", new Class[0]));
        when(invocationMock.getArguments()).thenReturn(new Object[0]);
        when(invocationMock.getMock()).thenReturn(new Object());
        Object result = callRealMethods.answer(invocationMock);
        Mockito.verify(invocationMock).callRealMethod();
        Assert.assertEquals(expectedResult, result);
    }
}