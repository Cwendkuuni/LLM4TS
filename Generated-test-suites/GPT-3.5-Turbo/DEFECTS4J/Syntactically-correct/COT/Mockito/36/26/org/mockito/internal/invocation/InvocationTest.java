import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;

public class InvocationTest {

    // Test getMock()
    @Test
    public void testGetMock() {
        Object mock = new Object();
        Invocation invocation = new Invocation(mock, null, null, 0, null);
        assertEquals(mock, invocation.getMock());
    }

    // Test getMethod()
    @Test
    public void testGetMethod() {
        MockitoMethod method = new MockitoMethod(null, null, null);
        Invocation invocation = new Invocation(null, method, null, 0, null);
        assertEquals(method, invocation.getMethod());
    }

    // Test getArguments()
    @Test
    public void testGetArguments() {
        Object[] arguments = new Object[]{1, 2, 3};
        Invocation invocation = new Invocation(null, null, arguments, 0, null);
        assertArrayEquals(arguments, invocation.getArguments());
    }

    // Test isVerified()
    @Test
    public void testIsVerified() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        assertFalse(invocation.isVerified());
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    // Test getSequenceNumber()
    @Test
    public void testGetSequenceNumber() {
        int sequenceNumber = 1;
        Invocation invocation = new Invocation(null, null, null, sequenceNumber, null);
        assertEquals(Integer.valueOf(sequenceNumber), invocation.getSequenceNumber());
    }

    // Test isVerifiedInOrder()
    @Test
    public void testIsVerifiedInOrder() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        assertFalse(invocation.isVerifiedInOrder());
        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerifiedInOrder());
    }

    // Test equals()
    @Test
    public void testEquals() {
        Invocation invocation1 = new Invocation(null, null, null, 0, null);
        Invocation invocation2 = new Invocation(null, null, null, 0, null);
        Invocation invocation3 = new Invocation(null, null, null, 1, null);

        assertTrue(invocation1.equals(invocation2));
        assertFalse(invocation1.equals(invocation3));
    }

    // Test hashCode()
    @Test(expected = RuntimeException.class)
    public void testHashCode() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        invocation.hashCode();
    }

    // Test toString()
    @Test
    public void testToString() {
        MockitoMethod method = new MockitoMethod("methodName", null, null);
        ArrayList<Matcher> matchers = new ArrayList<>();
        Invocation invocation = new Invocation(null, method, null, 0, null);

        String expected = "null.methodName";
        assertEquals(expected, invocation.toString(matchers, new PrintSettings()));
    }

    // Test toString(PrintSettings)
    @Test
    public void testToStringWithPrintSettings() {
        MockitoMethod method = new MockitoMethod("methodName", null, null);
        ArrayList<Matcher> matchers = new ArrayList<>();
        PrintSettings printSettings = new PrintSettings();
        Invocation invocation = new Invocation(null, method, null, 0, null);

        String expected = "null.methodName";
        assertEquals(expected, invocation.toString(matchers, printSettings));
    }

    // Test isValidException()
    @Test
    public void testIsValidException() {
        Invocation invocation = new Invocation(null, null, null, 0, null);

        assertTrue(invocation.isValidException(new Throwable()));
        assertFalse(invocation.isValidException(new Exception()));
    }

    // Test isValidReturnType()
    @Test
    public void testIsValidReturnType() {
        Invocation invocation = new Invocation(null, null, null, 0, null);

        assertTrue(invocation.isValidReturnType(Object.class));
        assertFalse(invocation.isValidReturnType(String.class));
    }

    // Test isVoid()
    @Test
    public void testIsVoid() {
        MockitoMethod method = new MockitoMethod(null, Void.TYPE, null);
        Invocation invocation1 = new Invocation(null, null, null, 0, null);
        Invocation invocation2 = new Invocation(null, method, null, 0, null);

        assertFalse(invocation1.isVoid());
        assertTrue(invocation2.isVoid());
    }

    // Test printMethodReturnType()
    @Test
    public void testPrintMethodReturnType() {
        MockitoMethod method = new MockitoMethod(null, Object.class, null);
        Invocation invocation = new Invocation(null, method, null, 0, null);

        assertEquals("Object", invocation.printMethodReturnType());
    }

    // Test getMethodName()
    @Test
    public void testGetMethodName() {
        MockitoMethod method = new MockitoMethod("methodName", null, null);
        Invocation invocation = new Invocation(null, method, null, 0, null);

        assertEquals("methodName", invocation.getMethodName());
    }

    // Test returnsPrimitive()
    @Test
    public void testReturnsPrimitive() {
        MockitoMethod method1 = new MockitoMethod(null, int.class, null);
        MockitoMethod method2 = new MockitoMethod(null, Object.class, null);
        Invocation invocation1 = new Invocation(null, method1, null, 0, null);
        Invocation invocation2 = new Invocation(null, method2, null, 0, null);

        assertTrue(invocation1.returnsPrimitive());
        assertFalse(invocation2.returnsPrimitive());
    }

    // Test getLocation()
    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        assertEquals(new Location(), invocation.getLocation());
    }

    // Test getArgumentsCount()
    @Test
    public void testGetArgumentsCount() {
        Invocation invocation = new Invocation(null, null, new Object[]{1,2,3}, 0, null);
        assertEquals(3, invocation.getArgumentsCount());
    }

    // Test getRawArguments()
    @Test
    public void testGetRawArguments() {
        Object[] rawArguments = new Object[]{1,2,3};
        Invocation invocation = new Invocation(null, null, null, 0, null);
        invocation.getRawArguments();
        assertArrayEquals(rawArguments, invocation.getRawArguments());
    }

    // Test callRealMethod()
    @Test(expected = Throwable.class)
    public void testCallRealMethod() throws Throwable {
        Object mock = new Object();
        Invocation invocation = new Invocation(mock, null, null, 0, null);
        invocation.callRealMethod();
    }

    // Test markVerified()
    @Test
    public void testMarkVerified() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        assertFalse(invocation.isVerified());
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    // Test markVerifiedInOrder()
    @Test
    public void testMarkVerifiedInOrder() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        assertFalse(invocation.isVerifiedInOrder());
        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerifiedInOrder());
    }
}