package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {
    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        InvocationOnMock mockInvocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(mockInvocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        Object result = returnsSmartNulls.answer(mockInvocation);
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);

        ObjectMethodsGuru objectMethodsGuru = Mockito.mock(ObjectMethodsGuru.class);
        Method method = Object.class.getMethod("toString");
        Mockito.when(objectMethodsGuru.isToString(method)).thenReturn(true);
        new Reporter().smartNullPointerException(new Location());
        Mockito.when(objectMethodsGuru.isToString(method)).thenReturn(false);
        result = returnsSmartNulls.answer(mockInvocation);
        assertNull(result);
    }
}