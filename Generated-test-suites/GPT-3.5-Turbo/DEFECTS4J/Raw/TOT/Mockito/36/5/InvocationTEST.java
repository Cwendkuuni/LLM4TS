import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.*;
import org.mockito.internal.matchers.*;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class InvocationTest {

    private Invocation invocation;
    private Object mock;
    private MockitoMethod method;
    private Object[] arguments;
    private int sequenceNumber;
    private RealMethod realMethod;

    @Before
    public void setUp() {
        mock = new Object();
        method = new MockitoMethod("methodName", null);
        arguments = new Object[]{1, 2, 3};
        sequenceNumber = 1;
        realMethod = new RealMethod();
        invocation = new Invocation(mock, method, arguments, sequenceNumber, realMethod);
    }

    @Test
    public void testGetMock() {
        assertEquals(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        assertEquals(method, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        assertArrayEquals(arguments, invocation.getArguments());
    }

    @Test
    public void testIsVerified() {
        assertFalse(invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        assertEquals(Integer.valueOf(sequenceNumber), invocation.getSequenceNumber());
    }

    @Test
    public void testIsVerifiedInOrder() {
        assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        Invocation otherInvocation = new Invocation(mock, method, arguments, sequenceNumber, realMethod);
        assertTrue(invocation.equals(otherInvocation));
    }

    @Test
    public void testEquals_Null() {
        assertFalse(invocation.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        assertFalse(invocation.equals(new Object()));
    }

    @Test
    public void testEquals_DifferentMock() {
        Invocation otherInvocation = new Invocation(new Object(), method, arguments, sequenceNumber, realMethod);
        assertFalse(invocation.equals(otherInvocation));
    }

    @Test
    public void testEquals_DifferentMethod() {
        MockitoMethod otherMethod = new MockitoMethod("differentMethod", null);
        Invocation otherInvocation = new Invocation(mock, otherMethod, arguments, sequenceNumber, realMethod);
        assertFalse(invocation.equals(otherInvocation));
    }

    @Test
    public void testHashCode() {
        assertThrows(RuntimeException.class, () -> invocation.hashCode());
    }

    @Test
    public void testToString() {
        String expected = "";
        expected += new MockUtil().getMockName(mock) + "." + method.getName() + "(1, 2, 3)";
        assertEquals(expected, invocation.toString());
    }

    @Test
    public void testToString_Multiline() {
        PrintSettings printSettings = new PrintSettings().multiline(true);
        String expected = "";
        expected += new MockUtil().getMockName(mock) + "." + method.getName() + "(";
        expected += "1,\n";
        expected += "2,\n";
        expected += "3";
        expected += ")";
        assertEquals(expected, invocation.toString(printSettings));
    }

    @Test
    public void testToString_LongInvocation() {
        method = new MockitoMethod("longMethodNameWithThirtyCharacters", null);
        invocation = new Invocation(mock, method, arguments, sequenceNumber, realMethod);
        String expected = "";
        expected += new MockUtil().getMockName(mock) + "." + method.getName() + "(1, 2, 3)";
        assertEquals(expected, invocation.toString());
    }

    @Test
    public void testToString_LongInvocationMultiline() {
        method = new MockitoMethod("longMethodNameWithThirtyCharacters", null);
        invocation = new Invocation(mock, method, arguments, sequenceNumber, realMethod);
        PrintSettings printSettings = new PrintSettings().multiline(true);
        String expected = "";
        expected += new MockUtil().getMockName(mock) + "." + method.getName() + "(";
        expected += "1,\n";
        expected += "2,\n";
        expected += "3";
        expected += ")";
        assertEquals(expected, invocation.toString(printSettings));
    }

    @Test
    public void testArgumentsToMatchers() {
        List<Matcher> matchers = invocation.argumentsToMatchers();
        assertEquals(3, matchers.size());
        assertTrue(matchers.get(0) instanceof Equals);
        assertTrue(matchers.get(1) instanceof Equals);
        assertTrue(matchers.get(2) instanceof Equals);
    }

    @Test
    public void testIsToString() {
        InvocationOnMock invocationOnMock = (InvocationOnMock) invocation;
        assertFalse(Invocation.isToString(invocationOnMock));
    }

    @Test
    public void testIsValidException_InvalidException() {
        Throwable throwable = new RuntimeException();
        assertFalse(invocation.isValidException(throwable));
    }

    @Test
    public void testIsValidException_ValidException() {
        method = new MockitoMethod("methodName", new Class[]{RuntimeException.class});
        invocation = new Invocation(mock, method, arguments, sequenceNumber, realMethod);
        Throwable throwable = new RuntimeException();
        assertTrue(invocation.isValidException(throwable));
    }

    @Test
    public void testIsValidReturnType_Primitive_ReturnsTrue() {
        assertTrue(invocation.isValidReturnType(int.class));
    }

    @Test
    public void testIsValidReturnType_Primitive_ReturnsFalse() {
        assertFalse(invocation.isValidReturnType(String.class));
    }

    @Test
    public void testIsValidReturnType_NonPrimitive_ReturnsTrue() {
        assertTrue(invocation.isValidReturnType(Object.class));
    }

    @Test
    public void testIsValidReturnType_NonPrimitive_ReturnsFalse() {
        method = new MockitoMethod("methodName", new Class[]{String.class});
        invocation = new Invocation(mock, method, arguments, sequenceNumber, realMethod);
        assertFalse(invocation.isValidReturnType(Integer.class));
    }

    @Test
    public void testIsVoid() {
        assertFalse(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        assertEquals("void", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        assertEquals("methodName", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        assertFalse(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        assertNotNull(invocation.getLocation());
        assertEquals(Location.class, invocation.getLocation().getClass());
    }

    @Test
    public void testGetArgumentsCount() {
        assertEquals(3, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        assertArrayEquals(arguments, invocation.getRawArguments());
    }

    @Test
    public void testCallRealMethod() {
        assertThrows(Throwable.class, () -> invocation.callRealMethod());
    }

    @Test
    public void testToString_PrintSettings() {
        PrintSettings printSettings = new PrintSettings().multiline(true);
        String expected = "";
        expected += new MockUtil().getMockName(mock) + "." + method.getName() + "(";
        expected += "1,\n";
        expected += "2,\n";
        expected += "3";
        expected += ")";
        assertEquals(expected, invocation.toString(printSettings));
    }

    @Test
    public void testMarkVerified() {
        assertFalse(invocation.isVerified());
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        assertFalse(invocation.isVerified());
        assertFalse(invocation.isVerifiedInOrder());
        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerified());
        assertTrue(invocation.isVerifiedInOrder());
    }
}