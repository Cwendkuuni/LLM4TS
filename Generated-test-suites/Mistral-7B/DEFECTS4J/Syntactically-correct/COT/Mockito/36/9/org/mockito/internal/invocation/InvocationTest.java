import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.util.MockUtil;

public class InvocationTest {

    @Test
    public void testConstructor() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{}, "methodName", new Class[0]);
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 0, new RealMethod(){});

        assertEquals(mockitoMethod.getName(), invocation.getMethod().getName());
        assertEquals(mock, invocation.getMock());
        assertArrayEquals(arguments, invocation.getArguments());
        assertEquals(0, invocation.getSequenceNumber());
        assertNull(invocation.getLocation());
        assertFalse(invocation.isVerified());
        assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testGetMock() {
        Object mock = new Object();
        Invocation invocation = new Invocation(mock, new MockitoMethod(new Class[]{}, "methodName", new Class[0]), new Object[0], 0, new RealMethod(){});
        assertEquals(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{}, "methodName", new Class[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 0, new RealMethod(){});
        assertEquals(mockitoMethod, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{}, "methodName", new Class[0]);
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 0, new RealMethod(){});
        assertArrayEquals(arguments, invocation.getArguments());
    }

    @Test
    public void testIsVerified() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{}, "methodName", new Class[0]);
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 0, new RealMethod(){});
        assertFalse(invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{}, "methodName", new Class[0]);
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 0, new RealMethod(){});
        assertEquals(0, invocation.getSequenceNumber());
    }

    @Test
    public void testIsVerifiedInOrder() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{}, "methodName", new Class[0]);
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 0, new RealMethod(){});
        assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{}, "methodName", new Class[0]);
        Object[] arguments1 = new Object[]{1, "test"};
        Object[] arguments2 = new Object[]{1, "test"};
        Invocation invocation1 = new Invocation(mock, mockitoMethod, arguments1, 0, new RealMethod(){});
        Invocation invocation2 = new Invocation(mock, mockitoMethod, arguments2, 0, new RealMethod(){});
        assertTrue(invocation1.equals(invocation2));
    }

    @Test
    public void testToString() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{}, "methodName", new Class[0]);
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 0, new RealMethod(){});
        String expected = "methodName(1, test)";
        String actual = invocation.toString();
        assertEquals(expected, actual);
    }

    @Test(expected = RuntimeException.class)
    public void testHashCode() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{}, "methodName", new Class[0]);
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 0, new RealMethod(){});
        invocation.hashCode();
    }

    @Test
    public void testExpandVarArgs() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{String[].class}, "methodName", new Class[0]);
        Object[] arguments = new Object[]{"test1", "test2", new String[]{"test3", "test4"}};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 0, new RealMethod(){});
        Object[] expected = new Object[]{"test1", "test2", "test3", "test4"};
        Object[] actual = invocation.getArguments();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testIsValidException() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{String.class}, "methodName", new Class[]{Exception.class});
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 0, new RealMethod(){});

        Throwable throwable = new Exception("Test exception");
        assertTrue(invocation.isValidException(throwable));

        Class<?> unexpectedExceptionType = Integer.class;
        assertFalse(invocation.isValidException(new Integer(1)));
        assertFalse(invocation.isValidException(throwable));
    }

    @Test
    public void testIsValidReturnType() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{String.class}, "methodName", new Class[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 0, new RealMethod(){});

        Class<String> expectedStringType = String.class;
        assertTrue(invocation.isValidReturnType(expectedStringType));

        Class<Integer> unexpectedIntegerType = Integer.class;
        assertFalse(invocation.isValidReturnType(unexpectedIntegerType));
    }

    @Test
    public void testIsVoid() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{String.class}, "methodName", new Class[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 0, new RealMethod(){});

        assertFalse(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{String.class}, "methodName", new Class[]{Integer.class});
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 0, new RealMethod(){});

        String expectedReturnType = "Integer";
        String actualReturnType = invocation.printMethodReturnType();
        assertEquals(expectedReturnType, actualReturnType);
    }

    @Test
    public void testGetMethodName() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{String.class}, "methodName", new Class[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 0, new RealMethod(){});

        String expectedMethodName = "methodName";
        String actualMethodName = invocation.getMethodName();
        assertEquals(expectedMethodName, actualMethodName);
    }

    @Test
    public void testReturnsPrimitive() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{Boolean.TYPE}, "methodName", new Class[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 0, new RealMethod(){});

        assertTrue(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{String.class}, "methodName", new Class[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 0, new RealMethod(){});

        Location expectedLocation = new Location();
        Location actualLocation = invocation.getLocation();
        assertEquals(expectedLocation, actualLocation);
    }

    @Test
    public void testGetArgumentsCount() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{String.class}, "methodName", new Class[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 0, new RealMethod(){});

        int expectedArgumentsCount = 0;
        int actualArgumentsCount = invocation.getArgumentsCount();
        assertEquals(expectedArgumentsCount, actualArgumentsCount);
    }

    @Test
    public void testCallRealMethod() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{String.class}, "methodName", new Class[0]);
        Object[] rawArguments = new Object[]{"test"};
        Invocation invocation = new Invocation(mock, mockitoMethod, rawArguments, 0, new RealMethod(){
            @SuppressWarnings("unchecked")
            public String invoke(Object object, Object[] objects) throws Throwable {
                return (String) objects[0];
            }
        });

        String expected = "test";
        String actual = (String) invocation.callRealMethod();
        assertEquals(expected, actual);
    }

    @Test(expected = Throwable.class)
    public void testCallRealMethodWithException() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{String.class}, "methodName", new Class[]{Exception.class});
        Object[] rawArguments = new Object[]{"test"};
        Invocation invocation = new Invocation(mock, mockitoMethod, rawArguments, 0, new RealMethod(){
            @SuppressWarnings("unchecked")
            public String invoke(Object object, Object[] objects) throws Throwable {
                throw new Exception("Test exception");
            }
        });

        try {
            invocation.callRealMethod();
            fail("Expected exception");
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testMarkVerified() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{String.class}, "methodName", new Class[0]);
        Object[] arguments = new Object[]{"test"};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 0, new RealMethod(){});

        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{String.class}, "methodName1", new Class[0]);
        Object[] arguments1 = new Object[]{"test1"};
        Invocation invocation1 = new Invocation(mock, mockitoMethod, arguments1, 0, new RealMethod(){});

        MockitoMethod mockitoMethod2 = new MockitoMethod(new Class[]{String.class}, "methodName2", new Class[0]);
        Object[] arguments2 = new Object[]{"test2"};
        Invocation invocation2 = new Invocation(mock, mockitoMethod2, arguments2, 1, new RealMethod(){});

        invocation1.markVerified();
        invocation1.markVerifiedInOrder();
        invocation2.markVerified();
        invocation2.markVerifiedInOrder();

        assertTrue(invocation1.isVerifiedInOrder());
        assertTrue(invocation2.isVerifiedInOrder());
    }
}