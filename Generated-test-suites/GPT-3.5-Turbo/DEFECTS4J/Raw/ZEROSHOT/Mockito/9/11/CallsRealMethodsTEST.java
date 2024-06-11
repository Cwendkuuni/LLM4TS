package org.mockito.internal.stubbing.answers;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class CallsRealMethodsTest {

    private CallsRealMethods callsRealMethods;

    @Before
    public void setUp() {
        callsRealMethods = new CallsRealMethods();
    }

    @Test
    public void testAnswer_invocationCallsRealMethod() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = callsRealMethods.answer(invocation);
        verify(invocation, times(1)).callRealMethod();
        // Add assertions for the result if required
    }

    @Test(expected = Throwable.class)
    public void testAnswer_invocationThrowsException() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.callRealMethod()).thenThrow(Throwable.class);
        callsRealMethods.answer(invocation);
    }
}