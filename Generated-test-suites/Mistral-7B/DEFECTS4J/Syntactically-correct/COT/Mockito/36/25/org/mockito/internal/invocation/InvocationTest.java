import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.RealMethod;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.exceptions.base.MockitoException;

public class InvocationTest {

    @Test
    public void testConstructor() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{}, "testMethod", new Class[]{String.class});
        Object[] arguments = new Object[]{"test"};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod(mockitoMethod, "testMethod", new Class[0], new Class[]{String.class}, mock);

        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, sequenceNumber, realMethod);

        assertEquals(mock, invocation.getMock());
        assertEquals(mockitoMethod, invocation.getMethod());
        assertArrayEquals(arguments, invocation.getArguments());
        assertEquals(sequenceNumber, invocation.getSequenceNumber());
        assertEquals(realMethod, invocation.realMethod);
    }

    @Test
    public void testGetMock() {
        Object mock = new Object();
        Invocation invocation = new Invocation(mock, null, null, 1, null);
        assertEquals(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{}, "testMethod", new Class[]{String.class});
        Invocation invocation = new Invocation(null, mockitoMethod, null, 1, null);
        assertEquals(mockitoMethod, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        Object[] arguments = new Object[]{"test"};
        Invocation invocation = new Invocation(null, null, arguments, 1, null);
        assertArrayEquals(arguments, invocation.getArguments());
    }

    @Test
    public void testIsVerified() {
        Invocation invocation = new Invocation(null, null, null, 1, null);
        assertFalse(invocation.isVerified());

        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        Invocation invocation = new Invocation(null, null, null, 1, null);
        assertEquals(1, invocation.getSequenceNumber());
    }

    @Test
    public void testIsVerifiedInOrder() {
        Invocation invocation = new Invocation(null, null, null, 1, null);
        assertFalse(invocation.isVerifiedInOrder());

        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{}, "testMethod", new Class[]{String.class});
        Object[] arguments1 = new Object[]{"test1"};
        Object[] arguments2 = new Object[]{"test2"};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod(mockitoMethod, "testMethod", new Class[0], new Class[]{String.class}, mock);

        Invocation invocation1 = new Invocation(mock, mockitoMethod, arguments1, sequenceNumber, realMethod);
        Invocation invocation2 = new Invocation(mock, mockitoMethod, arguments2, sequenceNumber, realMethod);

        assertFalse(invocation1.equals(invocation2));

        Object[] arguments3 = new Object[]{"test1"};
        Invocation invocation3 = new Invocation(mock, mockitoMethod, arguments3, sequenceNumber, realMethod);

        assertTrue(invocation1.equals(invocation3));

        MockitoMethod mockitoMethod2 = new MockitoMethod(new Class[]{}, "testMethod2", new Class[]{String.class});
        Invocation invocation4 = new Invocation(mock, mockitoMethod2, arguments1, sequenceNumber, realMethod);

        assertFalse(invocation1.equals(invocation4));

        Invocation invocation5 = new Invocation(mock, mockitoMethod, arguments1, sequenceNumber + 1, realMethod);

        assertFalse(invocation1.equals(invocation5));
    }

    @Test
    public void testToString() {
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{}, "testMethod", new Class[]{String.class});
        Object[] arguments = new Object[]{"test"};
        Invocation invocation = new Invocation(null, mockitoMethod, arguments, 1, null);

        List<Matcher> matchers = invocation.argumentsToMatchers();
        PrintSettings printSettings = new PrintSettings();

        String result = invocation.toString(matchers, printSettings);

        String expected = "testMethod(test)";
        assertEquals(expected, result);
    }

    @Test
    public void testIsToString() {
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{}, "toString", new Class[0]);
        Invocation invocation = new Invocation(null, mockitoMethod, null, 1, null);

        assertTrue(Invocation.isToString(invocation));
    }

    @Test
    public void testIsValidException() {
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{String.class}, "testMethod", new Class[0]);
        Invocation invocation = new Invocation(null, mockitoMethod, null, 1, null);

        Throwable throwable = new MockitoException("MockitoException message");

        assertFalse(invocation.isValidException(throwable));

        try {
            throw new RuntimeException();
        } catch (RuntimeException e) {
            assertTrue(invocation.isValidException(e));
        }

        try {
            throw new NullPointerException();
        } catch (NullPointerException e) {
            assertTrue(invocation.isValidException(e));
        }

        try {
            throw new ClassCastException();
        } catch (ClassCastException e) {
            assertTrue(invocation.isValidException(e));
        }

        try {
            throw new IllegalArgumentException();
        } catch (IllegalArgumentException e) {
            assertTrue(invocation.isValidException(e));
        }

        try {
            throw new InstantiationException();
        } catch (InstantiationException e) {
            assertTrue(invocation.isValidException(e));
        }

        try {
            throw new IllegalAccessException();
        } catch (IllegalAccessException e) {
            assertTrue(invocation.isValidException(e));
        }

        try {
            throw new InvocationTargetException(new RuntimeException("RuntimeException message"));
        } catch (InvocationTargetException e) {
            assertTrue(invocation.isValidException(e.getTargetException()));
        }

        try {
            throw new InvocationTargetException(new NullPointerException());
        } catch (InvocationTargetException e) {
            assertTrue(invocation.isValidException(e.getTargetException()));
        }

        try {
            throw new InvocationTargetException(new ClassCastException());
        } catch (InvocationTargetException e) {
            assertTrue(invocation.isValidException(e.getTargetException()));
        }

        try {
            throw new InvocationTargetException(new IllegalArgumentException());
        } catch (InvocationTargetException e) {
            assertTrue(invocation.isValidException(e.getTargetException()));
        }

        try {
            throw new InvocationTargetException(new InstantiationException());
        } catch (InvocationTargetException e) {
            assertTrue(invocation.isValidException(e.getTargetException()));
        }

        try {
            throw new InvocationTargetException(new IllegalAccessException());
        } catch (InvocationTargetException e) {
            assertTrue(invocation.isValidException(e.getTargetException()));
        }

        try {
            throw new InvocationTargetException(new NumberFormatException("NumberFormatException message"));
        } catch (InvocationTargetException e) {
            assertTrue(invocation.isValidException(e.getTargetException()));
        }

        try {
            throw new InvocationTargetException(new ArrayIndexOutOfBoundsException(1));
        } catch (InvocationTargetException e) {
            assertTrue(invocation.isValidException(e.getTargetException()));
        }

        try {
            throw new InvocationTargetException(new ArrayStoreException(new String[1], new Integer(1)));
        } catch (InvocationTargetException e) {
            assertTrue(invocation.isValidException(e.getTargetException()));
        }
    }

    @Test
    public void testIsValidReturnType() {
        Invocation invocation = new Invocation(null, null, null, 1, null);

        assertTrue(invocation.isValidReturnType(int.class));
        assertTrue(invocation.isValidReturnType(String.class));
        assertTrue(invocation.isValidReturnType(List.class));
        assertTrue(invocation.isValidReturnType(ArrayList.class));
        assertTrue(invocation.isValidReturnType(HashMap.class));
        assertTrue(invocation.isValidReturnType(Boolean.class));
        assertTrue(invocation.isValidReturnType(double.class));
        assertTrue(invocation.isValidReturnType(void.class));

        assertFalse(invocation.isValidReturnType(float.class));
        assertFalse(invocation.isValidReturnType(char.class));
        assertFalse(invocation.isValidReturnType(short.class));
        assertFalse(invocation.isValidReturnType(byte.class));
        assertFalse(invocation.isValidReturnType(long.class));
        assertFalse(invocation.isValidReturnType(Object.class));

        Invocation invocation2 = new Invocation(null, new MockitoMethod(new Class[]{String.class}, "testMethod", new Class[]{String[].class}), null, 1, null);

        assertTrue(invocation2.isValidReturnType(String[].class));
    }

    @Test
    public void testIsVoid() {
        Invocation invocation = new Invocation(null, null, null, 1, null);

        assertFalse(invocation.isVoid());

        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{}, "voidMethod", new Class[0]);
        Invocation invocation2 = new Invocation(null, mockitoMethod, null, 1, null);

        assertTrue(invocation2.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Invocation invocation = new Invocation(null, null, null, 1, null);

        assertEquals("void", invocation.printMethodReturnType());

        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{String.class}, "testMethod", new Class[]{String.class});
        Invocation invocation2 = new Invocation(null, mockitoMethod, null, 1, null);

        assertEquals("String", invocation2.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{String.class}, "testMethod", new Class[]{String.class});
        Invocation invocation = new Invocation(null, mockitoMethod, null, 1, null);

        assertEquals("testMethod", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        Invocation invocation = new Invocation(null, null, null, 1, null);

        assertFalse(invocation.returnsPrimitive());

        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{String.class}, "testMethod", new Class[]{boolean.class});
        Invocation invocation2 = new Invocation(null, mockitoMethod, null, 1, null);

        assertTrue(invocation2.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation(null, null, null, 1, null);

        assertNull(invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        Invocation invocation = new Invocation(null, null, new Object[]{"test"}, 1, null);

        assertEquals(1, invocation.getArgumentsCount());

        Invocation invocation2 = new Invocation(null, null, new Object[]{new Object[2][], new Object[]{}}
                , 1, null);

        assertEquals(2, invocation2.getArgumentsCount());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{String.class}, "testMethod", new Class[]{String.class});
        Object mock = new Object();
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[]{"test"}, 1, new RealMethod(mockitoMethod, "testMethod", new Class[0], new Class[]{String.class}, mock));

        assertEquals("test", invocation.callRealMethod());
    }
}