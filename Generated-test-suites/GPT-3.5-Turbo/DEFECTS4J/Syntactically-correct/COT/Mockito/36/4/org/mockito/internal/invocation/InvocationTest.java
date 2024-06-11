import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.MatchersPrinter;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class InvocationTest {

    @Test
    public void testGetMock() {
        Object mock = new Object();
        Invocation invocation = new Invocation(mock, null, null, 0, null);
        assertEquals(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        MockitoMethod method = MockitoMethod.mock();
        Invocation invocation = new Invocation(null, method, null, 0, null);
        assertEquals(method, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        Object[] arguments = new Object[]{"arg1", "arg2"};
        Invocation invocation = new Invocation(null, null, arguments, 0, null);
        assertArrayEquals(arguments, invocation.getArguments());
    }

    @Test
    public void testIsVerified() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        assertFalse(invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        int sequenceNumber = 1;
        Invocation invocation = new Invocation(null, null, null, sequenceNumber, null);
        assertEquals(Integer.valueOf(sequenceNumber), invocation.getSequenceNumber());
    }

    @Test
    public void testIsVerifiedInOrder() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        Object mock = new Object();
        MockitoMethod method = MockitoMethod.mock();
        Object[] arguments = new Object[]{"arg1", "arg2"};
        Invocation invocation1 = new Invocation(mock, method, arguments, 0, null);
        Invocation invocation2 = new Invocation(mock, method, arguments, 0, null);
        Invocation invocation3 = new Invocation(mock, method, arguments, 1, null);

        assertTrue(invocation1.equals(invocation2));
        assertFalse(invocation1.equals(invocation3));
    }

    @Test
    public void testHashCode() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        try {
            invocation.hashCode();
            fail("hashCode() should throw an exception");
        } catch (RuntimeException e) {
            // Expected exception, ignore
        }
    }

    @Test
    public void testToString() {
        MockitoMethod method = MockitoMethod.mock();
        Object[] arguments = new Object[]{"arg1", "arg2"};
        Invocation invocation = new Invocation(null, method, arguments, 0, null);
        String expected = new MockUtil().getMockName(null) + "." + method.getName() + "(arg1, arg2)";
        assertEquals(expected, invocation.toString());
    }

    @Test
    public void testArgumentsToMatchers() {
        Object[] arguments = new Object[]{"arg1", "arg2"};
        Invocation invocation = new Invocation(null, null, arguments, 0, null);
        List<Matcher> matchers = invocation.argumentsToMatchers();
        assertEquals(2, matchers.size());
        assertTrue(matchers.get(0) instanceof Equals);
        assertEquals("arg1", ((Equals) matchers.get(0)).getExpected());
        assertTrue(matchers.get(1) instanceof Equals);
        assertEquals("arg2", ((Equals) matchers.get(1)).getExpected());
    }

    @Test
    public void testIsToString() {
        MockitoMethod toStringMethod = MockitoMethod.mock();
        MockitoMethod nonToStringMethod = MockitoMethod.mock();
        MockitoMethod[] methods = new MockitoMethod[]{toStringMethod, nonToStringMethod};
        MockitoMethod mockedToStringMethod = MockitoMethod.mock();
        InvocationOnMock invocation1 = new Invocation(null, toStringMethod, null);
        InvocationOnMock invocation2 = new Invocation(null, nonToStringMethod, null);
        InvocationOnMock invocation3 = new Invocation(null, mockedToStringMethod, null);

        assertTrue(Invocation.isToString(invocation1));
        assertFalse(Invocation.isToString(invocation2));
        assertFalse(Invocation.isToString(invocation3));
    }

    @Test
    public void testIsValidException() {
        Class<?> exception1 = RuntimeException.class;
        Class<?> exception2 = NullPointerException.class;
        Class<?>[] exceptions = new Class<?>[]{exception1, exception2};
        Invocation invocation = new Invocation(null, null, null, 0, null);

        assertTrue(invocation.isValidException(new RuntimeException()));
        assertTrue(invocation.isValidException(new NullPointerException()));
        assertFalse(invocation.isValidException(new Exception()));
    }

    @Test
    public void testIsValidReturnType() {
        MockitoMethod method = MockitoMethod.mock();
        Class<?> returnType = Integer.TYPE;
        MockitoMethod mockedMethod = MockitoMethod.mock();
        Invocation invocation = new Invocation(null, method, null, 0, null);

        assertTrue(invocation.isValidReturnType(returnType));
        assertFalse(invocation.isValidReturnType(String.class));
    }

    @Test
    public void testIsVoid() {
        MockitoMethod voidMethod = MockitoMethod.mock();
        MockitoMethod nonVoidMethod = MockitoMethod.mock();
        MockitoMethod[] methods = new MockitoMethod[]{voidMethod, nonVoidMethod};
        MockitoMethod mockedVoidMethod = MockitoMethod.mock();
        Invocation invocation1 = new Invocation(null, voidMethod, null);
        Invocation invocation2 = new Invocation(null, nonVoidMethod, null);
        Invocation invocation3 = new Invocation(null, mockedVoidMethod, null);

        assertTrue(invocation1.isVoid());
        assertFalse(invocation2.isVoid());
        assertTrue(invocation3.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        MockitoMethod method = MockitoMethod.mock();
        Class<?> returnType = Integer.TYPE;
        MockitoMethod mockedMethod = MockitoMethod.mock();
        Invocation invocation = new Invocation(null, method, null, 0, null);

        assertEquals("int", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        MockitoMethod method = MockitoMethod.mock();
        Invocation invocation = new Invocation(null, method, null, 0, null);

        assertEquals(method.getName(), invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        MockitoMethod primitiveMethod = MockitoMethod.mock();
        MockitoMethod nonPrimitiveMethod = MockitoMethod.mock();
        MockitoMethod[] methods = new MockitoMethod[]{primitiveMethod, nonPrimitiveMethod};
        MockitoMethod mockedPrimitiveMethod = MockitoMethod.mock();
        Invocation invocation1 = new Invocation(null, primitiveMethod, null);
        Invocation invocation2 = new Invocation(null, nonPrimitiveMethod, null);
        Invocation invocation3 = new Invocation(null, mockedPrimitiveMethod, null);

        assertTrue(invocation1.returnsPrimitive());
        assertFalse(invocation2.returnsPrimitive());
        assertTrue(invocation3.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        assertNotNull(invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        Object[] arguments = new Object[]{"arg1", "arg2"};
        Invocation invocation = new Invocation(null, null, arguments, 0, null);
        assertEquals(arguments.length, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        Object[] rawArguments = new Object[]{"arg1", "arg2"};
        Invocation invocation = new Invocation(null, null, null, 0, null);
        invocation.getRawArguments();
        assertArrayEquals(rawArguments, invocation.getRawArguments());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Object mock = new Object();
        Object[] rawArguments = new Object[]{"arg1", "arg2"};
        RealMethod realMethod = new RealMethod(mock, "methodName");
        Invocation invocation = new Invocation(mock, null, rawArguments, 0, realMethod);
        assertNull(invocation.callRealMethod());
    }

    @Test
    public void testToStringWithPrintSettings() {
        MockitoMethod method = MockitoMethod.mock();
        Object[] arguments = new Object[]{"arg1", "arg2"};
        Invocation invocation = new Invocation(null, method, arguments, 0, null);
        PrintSettings printSettings = new PrintSettings();
        String expected = new MockUtil().getMockName(null) + "." + method.getName() + "(arg1, arg2)";

        assertEquals(expected, invocation.toString(printSettings));
    }

    @Test
    public void testMarkVerified() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        assertFalse(invocation.isVerified());

        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        assertFalse(invocation.isVerifiedInOrder());

        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerifiedInOrder());
    }
}