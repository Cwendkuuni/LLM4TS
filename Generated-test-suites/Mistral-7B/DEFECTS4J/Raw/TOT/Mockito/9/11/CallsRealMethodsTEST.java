package org.mockito.internal.stubbing.answers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import static org.mockito.Mockito.*;

public class CallsRealMethodsTest {
    private Object realObject;
    private CallsRealMethods callsRealMethods;

    @Before
    public void setUp() {
        realObject = new ClassWithMethodsToTest();
        callsRealMethods = new CallsRealMethods();
    }

    @Test
    public void testCallsRealMethod() {
        Object result = callsRealMethods.answer(mock(InvocationOnMock.class).withRealMethod(realObject, "methodToTest"));
        verify(realObject).methodToTest();
        assertSame(result, realObject.methodToTest());
    }

    @Test
    public void testCallsRealMethodWithArguments() {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        realObject = mock(ClassWithMethodsToTest.class);
        callsRealMethods = new CallsRealMethods();

        callsRealMethods.answer(mock(InvocationOnMock.class).withRealMethod(realObject, "methodWithArgument", argumentCaptor.capture()));

        verify(realObject).methodWithArgument(argumentCaptor.getValue());
    }

    @Test
    public void testCallsRealMethodAndThrowsException() {
        realObject = mock(ClassWithMethodsToTest.class);
        callsRealMethods = new CallsRealMethods();

        doThrow(new RuntimeException("Test exception")).when(realObject).methodToTest();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setMethod(realObject.getClass().getMethod("methodToTest"));

        try {
            callsRealMethods.answer(invocation);
            fail("Expected exception not thrown");
        } catch (Throwable e) {
            verify(realObject).methodToTest();
            verifyNoMoreInteractions(realObject);
            assertEquals("Test exception", e.getMessage());
        }
    }
}