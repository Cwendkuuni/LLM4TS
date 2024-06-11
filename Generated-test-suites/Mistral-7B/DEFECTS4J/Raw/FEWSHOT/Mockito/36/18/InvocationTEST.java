package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.internal.matchers.ArgumentsMatchers.*;
import static org.mockito.internal.reporting.PrintSettings.printSettings;

import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.Arguments;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.MockitoMethod;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;
import org.mockito.internal.util.PrintingFriendlyInvocation;

public class InvocationTest {

    @Test
    public void testGetters() {
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "foo", new Class[]{String.class, int.class});
        Object mock = new Object();
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[]{"hello", 42}, 1, new RealMethod(mockitoMethod, mock));

        assertEquals(mock, invocation.getMock());
        assertEquals(mockitoMethod, invocation.getMethod());
        assertArrayEquals(new Object[]{"hello", 42}, invocation.getArguments());
        assertEquals(1, invocation.getSequenceNumber());
        assertFalse(invocation.isVerified());
        assertFalse(invocation.isVerifiedInOrder());

        Object[] rawArguments = invocation.getRawArguments();
        assertEquals(2, rawArguments.length);
        assertEquals("hello", rawArguments[0]);
        assertEquals(42, rawArguments[1]);
    }

    @Test
    public void testToString() {
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "foo", new Class[]{String.class, int.class});
        Object mock = new Object();
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[]{"hello", 42}, 1, new RealMethod(mockitoMethod, mock));

        PrintSettings printSettings = printSettings();
        printSettings.setMultiline(true);
        String expected = String.format("%s(%s)", new MockUtil().getMockName(mock), mockitoMethod.getArgumentsString(printSettings));
        assertEquals(expected, invocation.toString());

        printSettings.setMultiline(false);
        assertEquals("foo(String hello, int 42)", invocation.toString(printSettings));

        List<Matcher> matchers = invocation.argumentsToMatchers();
        assertEquals(2, matchers.size());
        assertTrue(matchers.get(0) instanceof Equals);
        assertTrue(matchers.get(1) instanceof ArrayEquals);
    }

    @Test
    public void testIsToString() {
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "toString", new Class[0]);
        Invocation invocation = new Invocation(mock(String.class), mockitoMethod, null, 1, new RealMethod(mockitoMethod, mock(String.class)));
        assertTrue(Invocation.isToString(invocation));
    }

    @Test
    public void testIsValidException() {
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "foo", new Class[]{String.class, Exception.class});
        Object mock = new Object();
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[]{"hello"}, 1, new RealMethod(mockitoMethod, mock));

        RuntimeException runtimeException = new RuntimeException("Oops");
        assertFalse(invocation.isValidException(runtimeException));

        Exception exception = new Exception("Oops");
        assertTrue(invocation.isValidException(exception));
    }

    @Test
    public void testIsValidReturnType() {
        Invocation invocation = new Invocation(mock(Object.class), new MockitoMethod(Integer.class, "foo", new Class[0]), null, 1, new RealMethod(new MockitoMethod(Integer.class, "foo", new Class[0]), mock(Object.class)));
        assertFalse(invocation.isValidReturnType(String.class));
        assertTrue(invocation.isValidReturnType(Integer.class));
    }

    @Test
    public void testIsVoid() {
        MockitoMethod mockitoMethod = new MockitoMethod(Void.TYPE, "foo", new Class[0]);
        Invocation invocation = new Invocation(mock(Object.class), mockitoMethod, null, 1, new RealMethod(mockitoMethod, mock(Object.class)));
        assertTrue(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "foo", new Class[]{String.class, int.class});
        Invocation invocation = new Invocation(mock(Object.class), mockitoMethod, new Object[]{"hello", 42}, 1, new RealMethod(mockitoMethod, mock(Object.class)));
        assertEquals("String", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "foo", new Class[]{String.class, int.class});
        Invocation invocation = new Invocation(mock(Object.class), mockitoMethod, new Object[]{"hello", 42}, 1, new RealMethod(mockitoMethod, mock(Object.class)));
        assertEquals("foo", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        MockitoMethod mockitoMethod = new MockitoMethod(boolean.class, "foo", new Class[0]);
        Invocation invocation = new Invocation(mock(Object.class), mockitoMethod, null, 1, new RealMethod(mockitoMethod, mock(Object.class)));
        assertTrue(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation(mock(Object.class), new MockitoMethod(String.class, "foo", new Class[0]), null, 1, new RealMethod(new MockitoMethod(String.class, "foo", new Class[0]), mock(Object.class)));
        assertNull(invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "foo", new Class[]{String.class, int.class});
        Invocation invocation = new Invocation(mock(Object.class), mockitoMethod, new Object[]{"hello", 42}, 1, new RealMethod(mockitoMethod, mock(Object.class)));
        assertEquals(2, invocation.getArgumentsCount());
    }

    @Test
    public void testCallRealMethod() throws Exception {
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "foo", new Class[]{String.class, int.class});
        Object mock = mock(String.class);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[]{"hello", 42}, 1, new RealMethod(mockitoMethod, mock));

        String result = (String) invocation.callRealMethod();
        assertEquals("hello42", result);
    }
}