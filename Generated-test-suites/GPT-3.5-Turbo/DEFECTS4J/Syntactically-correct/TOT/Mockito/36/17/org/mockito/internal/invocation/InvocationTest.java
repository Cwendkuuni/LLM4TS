import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class InvocationTest {

    private Invocation invocation;

    @Before
    public void setUp() {
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod(getTestMethod());
        Object[] args = new Object[]{"arg1", "arg2"};
        int sequenceNumber = 1;
        RealMethod realMethod = null;
        invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);
    }

    @Test
    public void testGetMock() {
        Object mock = invocation.getMock();
        assertNotNull(mock);
        assertEquals(Object.class, mock.getClass());
    }

    @Test
    public void testGetMethod() {
        MockitoMethod method = invocation.getMethod();
        assertNotNull(method);
        assertEquals(getTestMethod(), method.getMethod());
    }

    @Test
    public void testGetArguments() {
        Object[] arguments = invocation.getArguments();
        assertNotNull(arguments);
        assertEquals(2, arguments.length);
        assertEquals("arg1", arguments[0]);
        assertEquals("arg2", arguments[1]);
    }

    @Test
    public void testIsVerified() {
        assertFalse(invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        Integer sequenceNumber = invocation.getSequenceNumber();
        assertNotNull(sequenceNumber);
        assertEquals(1, sequenceNumber.intValue());
    }

    @Test
    public void testIsVerifiedInOrder() {
        assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals_WithDifferentMock_ReturnsFalse() {
        Invocation other = new Invocation(mock(Object.class), new MockitoMethod(getTestMethod()), new Object[]{"arg1", "arg2"}, 1, null);
        assertFalse(invocation.equals(other));
    }

    @Test
    public void testEquals_WithDifferentMethod_ReturnsFalse() {
        Invocation other = new Invocation(mock(Object.class), new MockitoMethod(getOtherTestMethod()), new Object[]{"arg1", "arg2"}, 1, null);
        assertFalse(invocation.equals(other));
    }

    @Test
    public void testEquals_WithDifferentArguments_ReturnsFalse() {
        Invocation other = new Invocation(mock(Object.class), new MockitoMethod(getTestMethod()), new Object[]{"arg1", "arg3"}, 1, null);
        assertFalse(invocation.equals(other));
    }

    @Test
    public void testEqualArguments_WithSameArguments_ReturnsTrue() {
        Object[] arguments = new Object[]{"arg1", "arg2"};
        assertTrue(invocation.equalArguments(arguments));
    }

    @Test
    public void testEqualArguments_WithDifferentArguments_ReturnsFalse() {
        Object[] arguments = new Object[]{"arg1", "arg3"};
        assertFalse(invocation.equalArguments(arguments));
    }

    @Test(expected = RuntimeException.class)
    public void testHashCode_ThrowsRuntimeException() {
        invocation.hashCode();
    }

    @Test
    public void testToString() {
        String toString = invocation.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Invocation@"));
    }

    @Test
    public void testToString_WithMultilineSetting() {
        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(true);
        String toString = invocation.toString(printSettings);
        assertNotNull(toString);
        assertTrue(toString.contains("Invocation@"));
    }

    @Test
    public void testToString_WithLongInvocation() {
        List<Matcher> matchers = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            matchers.add(new Equals("arg" + i));
        }
        invocation.toString(matchers, new PrintSettings());
    }

    @Test
    public void testToString_WithLongInvocationAndMultilineSetting() {
        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(true);
        List<Matcher> matchers = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            matchers.add(new Equals("arg" + i));
        }
        invocation.toString(matchers, printSettings);
    }

    @Test
    public void testIsToString_WithToStringMethod_ReturnsTrue() throws NoSuchMethodException {
        Method method = invocation.getClass().getMethod("toString");
        assertTrue(invocation.isToString(new InvocationOnMock() {
            @Override
            public Object mock() {
                return null;
            }

            @Override
            public Method getMethod() {
                return method;
            }

            @Override
            public Object[] getArguments() {
                return null;
            }
        }));
    }

    @Test
    public void testIsValidException_WithValidException_ReturnsTrue() {
        assertTrue(invocation.isValidException(new Exception()));
    }

    @Test
    public void testIsValidException_WithInvalidException_ReturnsFalse() {
        assertFalse(invocation.isValidException(new RuntimeException()));
    }

    @Test
    public void testIsValidReturnType_WithValidReturnType_ReturnsTrue() {
        assertTrue(invocation.isValidReturnType(Object.class));
    }

    @Test
    public void testIsValidReturnType_WithInvalidReturnType_ReturnsFalse() {
        assertFalse(invocation.isValidReturnType(Integer.class));
    }

    @Test
    public void testIsVoid_WithVoidReturnType_ReturnsTrue() throws NoSuchMethodException {
        Method method = invocation.getClass().getMethod("callRealMethod");
        assertTrue(invocation.isVoid(new InvocationOnMock() {
            @Override
            public Object mock() {
                return null;
            }

            @Override
            public Method getMethod() {
                return method;
            }

            @Override
            public Object[] getArguments() {
                return null;
            }
        }));
    }

    @Test
    public void testPrintMethodReturnType() {
        String returnType = invocation.printMethodReturnType();
        assertNotNull(returnType);
        assertEquals("Object", returnType);
    }

    @Test
    public void testGetMethodName() {
        String methodName = invocation.getMethodName();
        assertNotNull(methodName);
        assertEquals("getTestMethod", methodName);
    }

    @Test
    public void testReturnsPrimitive_WithPrimitiveReturnType_ReturnsTrue() {
        assertTrue(invocation.returnsPrimitive());
    }

    @Test
    public void testReturnsPrimitive_WithNonPrimitiveReturnType_ReturnsFalse() {
        assertFalse(new Invocation(mock(Object.class), new MockitoMethod(getTestMethod()), new Object[]{"arg1", "arg2"}, 1, null).returnsPrimitive());
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
        assertNotNull(rawArguments);
        assertEquals(2, rawArguments.length);
        assertEquals("arg1", rawArguments[0]);
        assertEquals("arg2", rawArguments[1]);
    }

    @Test(expected = Throwable.class)
    public void testCallRealMethod_ThrowsThrowable() throws Throwable {
        invocation.callRealMethod();
    }

    @Test
    public void testToString_WithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(true);
        String toString = invocation.toString(printSettings);
        assertNotNull(toString);
        assertTrue(toString.contains("Invocation@"));
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

    private Method getTestMethod() {
        try {
            return InvocationTest.class.getMethod("testGetMethod");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private Method getOtherTestMethod() {
        try {
            return InvocationTest.class.getMethod("testIsValidReturnType_WithValidReturnType_ReturnsTrue");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}