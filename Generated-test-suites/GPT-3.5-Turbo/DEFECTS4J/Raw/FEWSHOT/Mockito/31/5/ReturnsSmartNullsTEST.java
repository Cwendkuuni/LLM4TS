
package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerReturnsNullForUnmockableReturnType() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        ClassImposterizer mockImposterizer = new ClassImposterizer();

        // Mock a method with a final return type
        MethodProxy methodProxy = MethodProxy.create(Object.class, "finalMethod", "()I", null, null);
        InvocationOnMock invocationOnMock = new InvocationOnMock(null, methodProxy, null);
        Object returnValue = returnsSmartNulls.answer(invocationOnMock);

        assertNull(returnValue);
    }

    @Test
    public void testAnswerReturnsSmartNullForMockableReturnType() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        // Mock a method with a mockable return type
        MethodProxy methodProxy = MethodProxy.create(Object.class, "getMockableMethod", "()Ljava/lang/Object;", null, null);
        InvocationOnMock invocationOnMock = new InvocationOnMock(null, methodProxy, null);
        Object returnValue = returnsSmartNulls.answer(invocationOnMock);

        assertNotNull(returnValue);
    }

    @Test
    public void testAnswerHandlesToStringMethod() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        MethodProxy methodProxy = MethodProxy.create(Object.class, "toString", "()Ljava/lang/String;", null, null);
        InvocationOnMock invocationOnMock = new InvocationOnMock(null, methodProxy, null);
        Object returnValue = returnsSmartNulls.answer(invocationOnMock);

        assertEquals("SmartNull returned by unstubbed toString() method on mock", returnValue);
    }

    @Test(expected = Reporter.SmartNullPointerException.class)
    public void testAnswerThrowsSmartNullPointerException() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Location location = new Location();
        InvocationOnMock invocationOnMock = new InvocationOnMock(location, null, null);
        returnsSmartNulls.answer(invocationOnMock);
    }

    // Additional tests for ReturnsMoreEmptyValues can be added here

    // Additional tests for ObjectMethodsGuru can be added here

    // Additional tests for ClassImposterizer can be added here
}
