package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.exceptions.Reporter;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object returnValue = returnsSmartNulls.answer(invocation);

        assertNotNull(returnValue);
    }

    @Test
    public void testThrowingInterceptor() throws Throwable {
        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = new ReturnsSmartNulls().new ThrowingInterceptor(null);

        MethodInterceptor methodInterceptor = Mockito.mock(MethodInterceptor.class);
        Object returnValue = throwingInterceptor.intercept(null, null, null, methodInterceptor);

        assertNotNull(returnValue);
    }

    @Test
    public void testSmartNullException() {
        Location location = new Location();
        new Reporter().smartNullPointerException(location);

        // Check that no exception is thrown
    }

    @Test
    public void testCanImposterise() {
        ClassImposterizer.INSTANCE.canImposterise(Object.class);

        // Check that no exception is thrown
    }

    @Test
    public void testAnswerWithNonNullReturnValue() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Answer<Object> delegate = Mockito.mock(Answer.class);
        Mockito.when(delegate.answer(invocation)).thenReturn(new Object());
        
        Object returnValue = returnsSmartNulls.answer(invocation);
        
        assertNotNull(returnValue);
        assertNotEquals("SmartNull returned by unstubbed method on mock", returnValue);
    }
}