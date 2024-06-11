import org.hamcrest.*;
import org.junit.*;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.matchers.*;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.util.*;
import org.mockito.invocation.InvocationOnMock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class InvocationTest {

    private Invocation invocation;

    @Before
    public void setUp() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod();
        Object[] args = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);
    }

    @Test
    public void testGetMock() {
        Object expected = new Object();
        invocation.setMock(expected);
        Object actual = invocation.getMock();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMethod() {
        MockitoMethod expected = new MockitoMethod();
        invocation.setMethod(expected);
        MockitoMethod actual = invocation.getMethod();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetArguments() {
        Object[] expected = new Object[2];
        invocation.setArguments(expected);
        Object[] actual = invocation.getArguments();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testIsVerified_false() {
        assertFalse(invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        Integer expected = 1;
        Integer actual = invocation.getSequenceNumber();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsVerifiedInOrder_false() {
        assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals_true() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation other = new Invocation(mock, method, args, sequenceNumber, realMethod);
        assertTrue(invocation.equals(other));
    }

    @Test
    public void testHashCode() {
        assertThrows(RuntimeException.class, () -> invocation.hashCode());
    }

    @Test
    public void testToString() {
        String expected = "mock.toString";
        Object mock = new Object() {
            public String toString() {
                return "mock.toString";
            }
        };
        Invocation invocation = new Invocation(mock, new MockitoMethod(), new Object[0], 1, new RealMethod());
        String actual = invocation.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString_multiline() {
        String expected = "mock.toString\n" +
                "  (MatchersPrinter.getArgumentsBlock line)";
        Object mock = new Object() {
            public String toString() {
                return "mock.toString";
            }
        };
        Invocation invocation = new Invocation(mock, new MockitoMethod(), new Object[0], 1, new RealMethod()) {
            protected String toString(List<Matcher> matchers, PrintSettings printSettings) {
                return "mock.toString" + "\n" + "  (MatchersPrinter.getArgumentsBlock line)";
            }
        };
        String actual = invocation.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsValidException_true() {
        Throwable throwable = new Exception();
        MockitoMethod method = new MockitoMethod(Exception.class);
        invocation.setMethod(method);
        boolean actual = invocation.isValidException(throwable);
        assertTrue(actual);
    }

    @Test
    public void testIsValidException_false() {
        Throwable throwable = new IllegalArgumentException();
        MockitoMethod method = new MockitoMethod(Exception.class);
        invocation.setMethod(method);
        boolean actual = invocation.isValidException(throwable);
        assertFalse(actual);
    }

    @Test
    public void testIsValidReturnType_primitive_true() {
        Class clazz = int.class;
        MockitoMethod method = new MockitoMethod(int.class);
        invocation.setMethod(method);
        boolean actual = invocation.isValidReturnType(clazz);
        assertTrue(actual);
    }

    @Test
    public void testIsValidReturnType_primitive_false() {
        Class clazz = double.class;
        MockitoMethod method = new MockitoMethod(int.class);
        invocation.setMethod(method);
        boolean actual = invocation.isValidReturnType(clazz);
        assertFalse(actual);
    }

    @Test
    public void testIsValidReturnType_nonPrimitive_true() {
        Class clazz = Object.class;
        MockitoMethod method = new MockitoMethod(Object.class);
        invocation.setMethod(method);
        boolean actual = invocation.isValidReturnType(clazz);
        assertTrue(actual);
    }

    @Test
    public void testIsValidReturnType_nonPrimitive_false() {
        Class clazz = String.class;
        MockitoMethod method = new MockitoMethod(Object.class);
        invocation.setMethod(method);
        boolean actual = invocation.isValidReturnType(clazz);
        assertFalse(actual);
    }

    @Test
    public void testIsVoid_true() {
        MockitoMethod method = new MockitoMethod(void.class);
        invocation.setMethod(method);
        assertTrue(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        String expected = "String";
        MockitoMethod method = new MockitoMethod(String.class);
        invocation.setMethod(method);
        String actual = invocation.printMethodReturnType();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMethodName() {
        String expected = "methodName";
        MockitoMethod method = new MockitoMethod(expected);
        invocation.setMethod(method);
        String actual = invocation.getMethodName();
        assertEquals(expected, actual);
    }

    @Test
    public void testReturnsPrimitive_true() {
        MockitoMethod method = new MockitoMethod(int.class);
        invocation.setMethod(method);
        assertTrue(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Location expected = new Location();
        invocation.setLocation(expected);
        Location actual = invocation.getLocation();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetArgumentsCount() {
        int expected = 2;
        MockitoMethod method = new MockitoMethod();
        invocation.setMethod(method);
        invocation.setArguments(new Object[2]);
        int actual = invocation.getArgumentsCount();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetRawArguments() {
        Object[] expected = new Object[2];
        invocation.setRawArguments(expected);
        Object[] actual = invocation.getRawArguments();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Object mock = new Object();
        Object[] rawArguments = new Object[0];
        RealMethod realMethod = new RealMethod();
        MockitoMethod method = new MockitoMethod();
        Invocation invocation = new Invocation(mock, method, new Object[0], 1, realMethod);
        Object actual = invocation.callRealMethod();
        assertNotNull(actual);
    }

    @Test
    public void testToString_PrintSettings() {
        String expected = "mock.toString";
        Object mock = new Object() {
            public String toString() {
                return "mock.toString";
            }
        };
        Invocation invocation = new Invocation(mock, new MockitoMethod(), new Object[0], 1, new RealMethod()) {
            protected String toString(List<Matcher> matchers, PrintSettings printSettings) {
                return "mock.toString";
            }
        };
        String actual = invocation.toString(new PrintSettings());
        assertEquals(expected, actual);
    }

    @Test
    public void testMarkVerified() {
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerified());
        assertTrue(invocation.isVerifiedInOrder());
    }

    private class MockitoMethod {
        private Class<?>[] exceptionTypes;
        private Class<?> returnType;
        private String name;

        public MockitoMethod() {
            this.returnType = Object.class;
            this.exceptionTypes = new Class<?>[0];
            this.name = "methodName";
        }

        public MockitoMethod(Class<?> returnType) {
            this.returnType = returnType;
            this.exceptionTypes = new Class<?>[0];
            this.name = "methodName";
        }

        public MockitoMethod(Class<?>... exceptionTypes) {
            this.returnType = Object.class;
            this.exceptionTypes = exceptionTypes;
            this.name = "methodName";
        }

        public Class<?>[] getExceptionTypes() {
            return exceptionTypes;
        }

        public Class<?> getReturnType() {
            return returnType;
        }

        public String getName() {
            return name;
        }
    }
}