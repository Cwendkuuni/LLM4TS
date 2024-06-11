package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {
    
    @Test
    public void testAnswer() throws Throwable {
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls(delegate);

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        MethodProxy proxy = mock(MethodProxy.class);
        Method method = Object.class.getMethod("toString");
        Object[] args = {};

        assertNull(returnsSmartNulls.answer(invocation));
        assertEquals("SmartNull returned by unstubbed toString() method on mock", returnsSmartNulls.answer(invocation, method, args, proxy));

        method = Object.class.getMethod("hashCode");
        assertEquals(null, returnsSmartNulls.answer(invocation, method, args, proxy));
    }
}