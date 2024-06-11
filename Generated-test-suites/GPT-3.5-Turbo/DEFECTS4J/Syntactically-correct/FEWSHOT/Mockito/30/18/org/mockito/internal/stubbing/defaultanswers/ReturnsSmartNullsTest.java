
package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocationWithStringReturn = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocationWithStringReturn.getMethod()).thenReturn(String.class.getMethod("toString"));
        Mockito.when(invocationWithStringReturn.getArguments()).thenReturn(new Object[]{});
        assertEquals("SmartNull returned by unstubbed toString() method on mock", smartNulls.answer(invocationWithStringReturn));

        InvocationOnMock invocationWithNonStringReturn = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocationWithNonStringReturn.getMethod()).thenReturn(Object.class.getMethod("hashCode"));
        Mockito.when(invocationWithNonStringReturn.getArguments()).thenReturn(new Object[]{});
        assertNull(smartNulls.answer(invocationWithNonStringReturn));

        InvocationOnMock invocationWithToStringFail = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocationWithToStringFail.getMethod()).thenReturn(FailToStringClass.class.getMethod("toString"));
        Mockito.when(invocationWithToStringFail.getArguments()).thenReturn(new Object[]{});
        assertEquals("SmartNull returned by unstubbed toString() method on mock", smartNulls.answer(invocationWithToStringFail));

        InvocationOnMock invocationWithNPEFail = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocationWithNPEFail.getMethod()).thenReturn(FailNPEClass.class.getMethod("throwException"));
        Mockito.when(invocationWithNPEFail.getArguments()).thenReturn(new Object[]{});
        assertEquals(null, smartNulls.answer(invocationWithNPEFail));
    }

    public static class FailToStringClass {
        public String toString() {
            throw new NullPointerException();
        }
    }

    public static class FailNPEClass {
        public void throwException() {
            throw new NullPointerException();
        }
    }
}
