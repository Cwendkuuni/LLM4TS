import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class CallsRealMethodsTest {

    @Test
    public void testAnswerMethod() {
        CallsRealMethods callsRealMethods = Mockito.mock(CallsRealMethods.class);
        Object input = new Object();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.callRealMethod()).thenReturn(input);

        Object result = callsRealMethods.answer(invocation);

        verify(invocation).callRealMethod();
        assertSame(input, result);
    }

    @Test
    public void testAnswerMethodWithNullInvocation() {
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        InvocationOnMock invocation = null;

        try {
            callsRealMethods.answer(invocation);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            //Expected
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAnswerMethodWithUnsupportedMethodSignature() {
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.getMethod().setParameterTypes(new Class[]{int.class});

        callsRealMethods.answer(invocation);
    }

}