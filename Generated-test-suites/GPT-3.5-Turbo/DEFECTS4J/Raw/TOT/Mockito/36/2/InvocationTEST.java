import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationTest {

    private Invocation invocation;
    private Object mock;

    @Before
    public void setUp() {
        mock = mock(Object.class);
        MockitoMethod method = new MockitoMethod(mock, "testMethod");
        Object[] args = {1, "hello"};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);
        invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
    }

    @Test
    public void testGetMock() {
        assertSame(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        assertTrue(invocation.getMethod() instanceof MockitoMethod);
    }

    @Test
    public void testGetArguments() {
        Object[] arguments = invocation.getArguments();
        assertEquals(2, arguments.length);
        assertEquals(1, arguments[0]);
        assertEquals("hello", arguments[1]);
    }

    @Test
    public void testIsVerified() {
        assertFalse(invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        assertEquals(Integer.valueOf(1), invocation.getSequenceNumber());
    }

    @Test
    public void testIsVerifiedInOrder() {
        assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        Invocation invocation2 = new Invocation(mock, new MockitoMethod(mock, "testMethod"), new Object[]{1, "hello"}, 1, mock(RealMethod.class));
        assertTrue(invocation.equals(invocation2));
    }

    @Test
    public void testHashCode() {
        assertThrows(RuntimeException.class, () -> invocation.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("invocation.toString()", invocation.toString());
    }

    @Test
    public void testToStringWithMatchers() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new Equals(1));
        matchers.add(new Equals("hello"));
        String expected = "invocation.toString(matchers)";
        assertEquals(expected, invocation.toString(matchers, new PrintSettings()));
    }

    @Test
    public void testToStringWithMultiline() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new Equals(1));
        matchers.add(new Equals("hello"));
        String expected = "invocation.toString(matchers)";
        PrintSettings printSettings = new PrintSettings().multiline(true);
        assertEquals(expected, invocation.toString(matchers, printSettings));
    }

    @Test
    public void testToStringWithLongInvocation() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new Equals(1));
        matchers.add(new Equals("hello"));
        StringBuilder invocationBuilder = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            invocationBuilder.append("arg");
        }
        matchers.add(new Equals(invocationBuilder.toString()));
        String expected = "invocation.toString(matchers)";
        assertEquals(expected, invocation.toString(matchers, new PrintSettings()));
    }

    @Test
    public void testArgumentsToMatchers() {
        List<Matcher> matchers = invocation.argumentsToMatchers();
        assertEquals(2, matchers.size());
        assertTrue(matchers.get(0) instanceof Equals);
        assertTrue(matchers.get(1) instanceof Equals);
    }

    @Test
    public void testIsToString() {
        InvocationOnMock invocationOnMock = mock(InvocationOnMock.class);
        when(invocationOnMock.getMethod()).thenReturn(mock(Method.class));
        assertFalse(Invocation.isToString(invocationOnMock));
    }

    @Test
    public void testIsValidException() {
        Throwable throwable = new RuntimeException();
        assertTrue(invocation.isValidException(throwable));
    }

    @Test
    public void testIsValidExceptionWithInvalidThrowable() {
        Throwable throwable = new Exception();
        assertFalse(invocation.isValidException(throwable));
    }

    @Test
    public void testIsValidReturnType() {
        assertTrue(invocation.isValidReturnType(String.class));
    }

    @Test
    public void testIsValidReturnTypeWithInvalidType() {
        assertFalse(invocation.isValidReturnType(Integer.class));
    }

    @Test
    public void testIsVoid() {
        assertFalse(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        assertEquals("String", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        assertEquals("testMethod", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        assertFalse(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        assertNotNull(invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        assertEquals(2, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        Object[] rawArguments = invocation.getRawArguments();
        assertEquals(2, rawArguments.length);
        assertEquals(1, rawArguments[0]);
        assertEquals("hello", rawArguments[1]);
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Object result = new Object();
        RealMethod realMethod = mock(RealMethod.class);
        when(realMethod.invoke(mock, new Object[]{1, "hello"})).thenReturn(result);
        Invocation invocation = new Invocation(mock, new MockitoMethod(mock, "testMethod"), new Object[]{1, "hello"}, 1, realMethod);
        assertSame(result, invocation.callRealMethod());
    }

    @Test
    public void testToStringWithPrintSettings() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new Equals(1));
        matchers.add(new Equals("hello"));
        String expected = "invocation.toString(matchers)";
        PrintSettings printSettings = new PrintSettings().multiline(true).verbose();
        assertEquals(expected, invocation.toString(matchers, printSettings));
    }

    @Test
    public void testMarkVerified() {
        assertFalse(invocation.isVerified());
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        assertFalse(invocation.isVerifiedInOrder());
        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerified());
        assertTrue(invocation.isVerifiedInOrder());
    }

}