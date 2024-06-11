package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls.ThrowingInterceptor;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {
    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = null; // initialize with a valid invocation object

        assertNull(returnsSmartNulls.answer(invocation));
    }

    @Test
    public void testThrowingInterceptor() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = null; // initialize with a valid invocation object
        ThrowingInterceptor interceptor = returnsSmartNulls.new ThrowingInterceptor(invocation);

        assertEquals("SmartNull returned by unstubbed method() method on mock", interceptor.intercept(null, null, null, null));
    }

    @Test
    public void testFormatMethodCall() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = null; // initialize with a valid invocation object
        ThrowingInterceptor interceptor = returnsSmartNulls.new ThrowingInterceptor(invocation);

        assertEquals("method()", interceptor.formatMethodCall());
    }
}