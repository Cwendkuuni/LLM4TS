import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.RealMethod;
import org.mockito.internal.invocation.MockitoMethod;
import org.mockito.internal.matchers.ArgumentsMatcher;

public class InvocationTest {

    @Test
    public void testConstructor_ValidArguments() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "testMethod", new Class[0]);
        Object[] rawArguments = new Object[0];
        RealMethod realMethod = new RealMethod(mockitoMethod, new Object[0]);

        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, realMethod);

        assertEquals(mock, invocation.getMock());
        assertEquals(mockitoMethod, invocation.getMethod());
        assertArrayEquals(new Object[0], invocation.getArguments());
        assertArrayEquals(rawArguments, invocation.getRawArguments());
        assertEquals(1, invocation.getSequenceNumber());
        assertFalse(invocation.isVerified());
        assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testConstructor_VarArgs() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "testMethod", new Class[]{String[].class});
        Object[] rawArguments = new Object[]{new String[]{"a", "b"}};
        RealMethod realMethod = new RealMethod(mockitoMethod, new Object[0]);

        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, realMethod);

        assertEquals(mock, invocation.getMock());
        assertEquals(mockitoMethod, invocation.getMethod());
        assertArrayEquals(new Object[0], invocation.getArguments());
        assertArrayEquals(rawArguments, invocation.getRawArguments());
        assertEquals(1, invocation.getSequenceNumber());
        assertFalse(invocation.isVerified());
        assertFalse(invocation.isVerifiedInOrder());
    }

    @Test(expected = RuntimeException.class)
    public void testConstructor_InvalidArguments() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "testMethod", new Class[0]);
        Object[] rawArguments = new Object[]{new Object()};
        RealMethod realMethod = new RealMethod(mockitoMethod, new Object[0]);

        Invocation invocation = new Invocation(mock, mockitoMethod, rawArguments, 1, realMethod);

        fail("Invocation constructor should fail with RuntimeException when rawArguments is not the same as arguments");
    }

    @Test
    public void testVerify() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "testMethod", new Class[0]);
        Object[] rawArguments = new Object[0];
        RealMethod realMethod = new RealMethod(mockitoMethod, new Object[0]);

        Invocation invocation = new Invocation(mock, mockitoMethod, rawArguments, 1, realMethod);

        invocation.markVerified();

        assertTrue(invocation.isVerified());
    }

    @Test
    public void testVerifyInOrder() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "testMethod", new Class[0]);
        Object[] rawArguments = new Object[0];
        RealMethod realMethod = new RealMethod(mockitoMethod, new Object[0]);

        Invocation invocation1 = new Invocation(mock, mockitoMethod, rawArguments, 1, realMethod);
        Invocation invocation2 = new Invocation(mock, mockitoMethod, rawArguments, 2, realMethod);

        invocation1.markVerifiedInOrder();

        assertTrue(invocation1.isVerifiedInOrder());
        assertFalse(invocation2.isVerifiedInOrder());
    }

    @Test(expected = RuntimeException.class)
    public void testVerifyInOrder_WrongSequenceNumber() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "testMethod", new Class[0]);
        Object[] rawArguments = new Object[0];
        RealMethod realMethod = new RealMethod(mockitoMethod, new Object[0]);

        Invocation invocation1 = new Invocation(mock, mockitoMethod, rawArguments, 1, realMethod);
        Invocation invocation2 = new Invocation(mock, mockitoMethod, rawArguments, 3, realMethod);

        invocation1.markVerifiedInOrder();

        fail("Invocation verification in order should fail with RuntimeException when sequence numbers are not in order");
    }

    @Test
    public void testIsToString() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "toString", new Class[0]);

        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod(mockitoMethod, new Object[0]));

        assertTrue(Invocation.isToString(invocation));
    }

    @Test
    public void testIsToString_NonToStringMethod() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "testMethod", new Class[0]);

        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod(mockitoMethod, new Object[0]));

        assertFalse(Invocation.isToString(invocation));
    }

    @Test(expected = RuntimeException.class)
    public void testCallRealMethod_InvalidException() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "testMethod", new Class[0]);
        Object[] rawArguments = new Object[0];
        RealMethod realMethod = new RealMethod(mockitoMethod, new Object[0]);

        Invocation invocation = new Invocation(mock, mockitoMethod, rawArguments, 1, realMethod);

        Throwable throwable = new RuntimeException();

        try {
            invocation.callRealMethod();
            fail("callRealMethod should throw an exception when isValidException returns false");
        } catch (Throwable e) {
            // expected
        }

        assertNotSame(throwable, invocation.getMethod().getExceptionTypes()[0]);
        assertThrows(RuntimeException.class, () -> invocation.callRealMethod());
    }

    @Test
    public void testCallRealMethod_ValidException() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "testMethod", new Class[]{Integer.class});
        Object[] rawArguments = new Object[]{1};
        RealMethod realMethod = new RealMethod(mockitoMethod, new Object[0]);

        Invocation invocation = new Invocation(mock, mockitoMethod, rawArguments, 1, realMethod);

        Throwable throwable = new IllegalArgumentException();

        try {
            invocation.callRealMethod();
            fail("callRealMethod should throw an exception when isValidException returns true");
        } catch (Throwable e) {
            assertEquals(throwable.getClass(), invocation.getMethod().getExceptionTypes()[0]);
            assertEquals(throwable, invocation.callRealMethod());
        }
    }

    @Test
    public void testIsValidReturnType() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(Integer.class, "testMethod", new Class[]{Integer.class});

        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod(mockitoMethod, new Object[0]));

        assertTrue(invocation.isValidReturnType(int.class));
        assertTrue(invocation.isValidReturnType(Integer.class));
        assertTrue(invocation.isValidReturnType(int[].class));
        assertTrue(invocation.isValidReturnType(Integer[].class));
        assertFalse(invocation.isValidReturnType(String.class));
    }

    @Test
    public void testIsVoid() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(Void.TYPE, "testMethod", new Class[0]);

        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod(mockitoMethod, new Object[0]));

        assertTrue(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "testMethod", new Class[]{Integer.class});

        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod(mockitoMethod, new Object[0]));

        assertEquals("String", invocation.printMethodReturnType());
    }

    @Test
    public void testToString() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "testMethod", new Class[]{Integer.class});
        Object[] rawArguments = new Object[]{1};

        Invocation invocation = new Invocation(mock, mockitoMethod, rawArguments, 1, new RealMethod(mockitoMethod, new Object[0]));

        List<Matcher> matchers = invocation.argumentsToMatchers();
        PrintSettings printSettings = new PrintSettings();

        String expected = "testMethod(int)";
        String result = invocation.toString(printSettings);

        assertEquals(expected, result);
    }

    @Test
    public void testToString_WithMatchers() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "testMethod", new Class[]{Integer.class, String[].class});
        Object[] rawArguments = new Object[]{1, new String[]{"a"}};

        Invocation invocation = new Invocation(mock, mockitoMethod, rawArguments, 1, new RealMethod(mockitoMethod, new Object[0]));

        List<Matcher> matchers = invocation.argumentsToMatchers();
        PrintSettings printSettings = new PrintSettings();

        String expected = "testMethod(int, [a])";
        String result = invocation.toString(printSettings);

        assertEquals(expected, result);
    }
}