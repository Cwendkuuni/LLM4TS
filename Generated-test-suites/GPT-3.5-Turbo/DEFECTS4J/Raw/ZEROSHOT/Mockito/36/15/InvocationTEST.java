import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;

public class InvocationTest {

    @Test
    public void testGetMock() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod method = new MockitoMethod("methodName", null, null);

        Invocation invocation = new Invocation(mock, method, null, 1, null);

        assertSame(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod method = new MockitoMethod("methodName", null, null);

        Invocation invocation = new Invocation(mock, method, null, 1, null);

        assertSame(method, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod method = new MockitoMethod("methodName", null, null);
        Object[] args = new Object[]{"arg1", "arg2"};

        Invocation invocation = new Invocation(mock, method, args, 1, null);

        assertArrayEquals(args, invocation.getArguments());
    }

    @Test
    public void testIsVerified() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod method = new MockitoMethod("methodName", null, null);

        Invocation invocation = new Invocation(mock, method, null, 1, null);

        assertFalse(invocation.isVerified());

        invocation.markVerified();

        assertTrue(invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod method = new MockitoMethod("methodName", null, null);

        Invocation invocation = new Invocation(mock, method, null, 1, null);

        assertEquals(Integer.valueOf(1), invocation.getSequenceNumber());
    }

    @Test
    public void testIsVerifiedInOrder() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod method = new MockitoMethod("methodName", null, null);

        Invocation invocation = new Invocation(mock, method, null, 1, null);

        assertFalse(invocation.isVerifiedInOrder());

        invocation.markVerifiedInOrder();

        assertTrue(invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        MockitoMethod method1 = new MockitoMethod("methodName", null, null);
        MockitoMethod method2 = new MockitoMethod("methodName", null, null);
        Object[] args1 = new Object[]{"arg1", "arg2"};
        Object[] args2 = new Object[]{"arg1", "arg2"};

        Invocation invocation1 = new Invocation(mock1, method1, args1, 1, null);
        Invocation invocation2 = new Invocation(mock1, method1, args1, 1, null);
        Invocation invocation3 = new Invocation(mock2, method1, args1, 1, null);
        Invocation invocation4 = new Invocation(mock1, method2, args1, 1, null);
        Invocation invocation5 = new Invocation(mock1, method1, args2, 1, null);

        assertTrue(invocation1.equals(invocation1));
        assertTrue(invocation1.equals(invocation2));
        assertFalse(invocation1.equals(invocation3));
        assertFalse(invocation1.equals(invocation4));
        assertFalse(invocation1.equals(invocation5));
    }

    @Test
    public void testHashCode() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod method = new MockitoMethod("methodName", null, null);
        Object[] args = new Object[]{"arg1", "arg2"};

        Invocation invocation = new Invocation(mock, method, args, 1, null);

        try {
            invocation.hashCode();
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e) {
            // expected
        }
    }

    @Test
    public void testToString() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod method = new MockitoMethod("methodName", null, null);
        Object[] args = new Object[]{"arg1", "arg2"};

        Invocation invocation = new Invocation(mock, method, args, 1, null);

        String expected = "Mock for java.lang.Object, hashCode: " + mock.hashCode() + ".methodName([arg1, arg2])";

        assertEquals(expected, invocation.toString());
    }

    @Test
    public void testToStringWithPrintSettings() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod method = new MockitoMethod("methodName", null, null);
        Object[] args = new Object[]{"arg1", "arg2"};
        PrintSettings printSettings = new PrintSettings();

        Invocation invocation = new Invocation(mock, method, args, 1, null);

        String expected = "Mock for java.lang.Object, hashCode: " + mock.hashCode() + ".methodName([arg1,\narg2])";

        assertEquals(expected, invocation.toString(printSettings));
    }

    @Test
    public void testIsToString() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod method = new MockitoMethod("toString", null, null);

        Invocation invocation = new Invocation(mock, method, null, 1, null);

        assertTrue(Invocation.isToString(invocation));
    }

    @Test
    public void testIsValidException() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod method = new MockitoMethod("methodName", new Class[]{IllegalArgumentException.class}, null);
        Throwable throwable = new IllegalArgumentException();

        Invocation invocation = new Invocation(mock, method, null, 1, null);

        assertTrue(invocation.isValidException(throwable));
    }

    @Test
    public void testIsNotValidException() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod method = new MockitoMethod("methodName", new Class[]{IllegalArgumentException.class}, null);
        Throwable throwable = new NullPointerException();

        Invocation invocation = new Invocation(mock, method, null, 1, null);

        assertFalse(invocation.isValidException(throwable));
    }

    @Test
    public void testIsValidReturnType() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod method = new MockitoMethod("methodName", null, Integer.class);

        Invocation invocation = new Invocation(mock, method, null, 1, null);

        assertTrue(invocation.isValidReturnType(Integer.class));
    }

    @Test
    public void testIsNotValidReturnType() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod method = new MockitoMethod("methodName", null, Integer.class);

        Invocation invocation = new Invocation(mock, method, null, 1, null);

        assertFalse(invocation.isValidReturnType(String.class));
    }

    @Test
    public void testIsVoid() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod method = new MockitoMethod("methodName", null, Void.TYPE);

        Invocation invocation1 = new Invocation(mock, method, null, 1, null);
        Invocation invocation2 = new Invocation(mock, method, null, 1, null);

        assertTrue(invocation1.isVoid());
        assertFalse(invocation2.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod method = new MockitoMethod("methodName", null, Integer.class);

        Invocation invocation = new Invocation(mock, method, null, 1, null);

        assertEquals("Integer", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod method = new MockitoMethod("methodName", null, null);

        Invocation invocation = new Invocation(mock, method, null, 1, null);

        assertEquals("methodName", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod method1 = new MockitoMethod("methodName", null, Integer.class);
        MockitoMethod method2 = new MockitoMethod("methodName", null, int.class);

        Invocation invocation1 = new Invocation(mock, method1, null, 1, null);
        Invocation invocation2 = new Invocation(mock, method2, null, 1, null);

        assertFalse(invocation1.returnsPrimitive());
        assertTrue(invocation2.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod method = new MockitoMethod("methodName", null, null);

        Invocation invocation = new Invocation(mock, method, null, 1, null);

        assertNotNull(invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod method = new MockitoMethod("methodName", null, null);
        Object[] args = new Object[]{"arg1", "arg2"};

        Invocation invocation = new Invocation(mock, method, args, 1, null);

        assertEquals(2, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod method = new MockitoMethod("methodName", null, null);
        Object[] args = new Object[]{"arg1", "arg2"};

        Invocation invocation = new Invocation(mock, method, args, 1, null);

        assertArrayEquals(args, invocation.getRawArguments());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod method = new MockitoMethod("methodName", null, null);
        Object[] args = new Object[]{"arg1", "arg2"};
        RealMethod realMethod = Mockito.mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, 1, realMethod);

        Mockito.when(realMethod.invoke(mock, args)).thenReturn("result");

        Object result = invocation.callRealMethod();

        assertEquals("result", result);
    }

    @Test
    public void testToStringWithMatchers() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod method = new MockitoMethod("methodName", null, null);
        Object[] args = new Object[]{"arg1", "arg2"};

        Invocation invocation = new Invocation(mock, method, args, 1, null);

        String expected = "Mock for java.lang.Object, hashCode: " + mock.hashCode() + ".methodName([arg1, arg2])";

        assertEquals(expected, invocation.toString());
    }

    @Test
    public void testMarkVerified() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod method = new MockitoMethod("methodName", null, null);

        Invocation invocation = new Invocation(mock, method, null, 1, null);

        assertFalse(invocation.isVerified());

        invocation.markVerified();

        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod method = new MockitoMethod("methodName", null, null);

        Invocation invocation = new Invocation(mock, method, null, 1, null);

        assertFalse(invocation.isVerifiedInOrder());

        invocation.markVerifiedInOrder();

        assertTrue(invocation.isVerifiedInOrder());
        assertTrue(invocation.isVerified());
    }
}