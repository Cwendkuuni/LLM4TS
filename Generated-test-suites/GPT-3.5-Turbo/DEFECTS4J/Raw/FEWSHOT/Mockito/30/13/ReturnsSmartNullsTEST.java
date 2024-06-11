package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {
    private ReturnsSmartNulls returnsSmartNulls;

    @Before
    public void setUp() {
        returnsSmartNulls = new ReturnsSmartNulls();
    }

    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        // Test when defaultReturnValue is not null
        Answer<Object> delegate = Mockito.mock(Answer.class);
        Mockito.when(delegate.answer(invocation)).thenReturn("default");
        Object result = returnsSmartNulls.answer(invocation);
        assertNotNull(result);
        assertEquals("default", result);

        // Test when defaultReturnValue is null and return type is mockable
        delegate = Mockito.mock(Answer.class);
        Mockito.when(delegate.answer(invocation)).thenReturn(null);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsSmartNulls.class.getDeclaredMethods()[0]);
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(Location.class);
        result = returnsSmartNulls.answer(invocation);
        assertNotNull(result);
        assertEquals("SmartNull returned by unstubbed formatMethodCall method on mock", result);

        // Test when defaultReturnValue is null, return type is not mockable, and is toString method
        delegate = Mockito.mock(Answer.class);
        Mockito.when(delegate.answer(invocation)).thenReturn(null);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsSmartNulls.class.getDeclaredMethods()[0]);
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        ObjectMethodsGuru objectMethodsGuru = Mockito.mock(ObjectMethodsGuru.class);
        Mockito.when(objectMethodsGuru.isToString(invocation.getMethod())).thenReturn(true);
        ReturnsSmartNulls returnsSmartNullsSpy = Mockito.spy(returnsSmartNulls);
        Mockito.doReturn(objectMethodsGuru).when(returnsSmartNullsSpy).getObjectMethodsGuru();
        result = returnsSmartNullsSpy.answer(invocation);
        assertNotNull(result);
        assertEquals("SmartNull returned by unstubbed formatMethodCall method on mock", result);

        // Test when defaultReturnValue is null, return type is not mockable, and is not toString method
        delegate = Mockito.mock(Answer.class);
        Mockito.when(delegate.answer(invocation)).thenReturn(null);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsSmartNulls.class.getDeclaredMethods()[0]);
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        objectMethodsGuru = Mockito.mock(ObjectMethodsGuru.class);
        Mockito.when(objectMethodsGuru.isToString(invocation.getMethod())).thenReturn(false);
        returnsSmartNullsSpy = Mockito.spy(returnsSmartNulls);
        Mockito.doReturn(objectMethodsGuru).when(returnsSmartNullsSpy).getObjectMethodsGuru();
        Location location = Mockito.mock(Location.class);
        Reporter reporter = Mockito.mock(Reporter.class);
        Mockito.when(returnSmartNullsSpy.getLocation()).thenReturn(location);
        Mockito.when(returnSmartNullsSpy.getReporter()).thenReturn(reporter);
        result = returnsSmartNullsSpy.answer(invocation);
        assertNull(result);
    }
}