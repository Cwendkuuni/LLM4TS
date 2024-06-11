import static org.junit.Assert.*;
import org.junit.Test;

public class InvocationTest {
    private Invocation createInvocation() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod("testMethod", null, new Class[0], false, false);
        Object[] args = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        return new Invocation(mock, method, args, sequenceNumber, realMethod);
    }

    @Test
    public void testGetMock() {
        Invocation invocation = createInvocation();
        assertNotNull(invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = createInvocation();
        assertNotNull(invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        Invocation invocation = createInvocation();
        assertNotNull(invocation.getArguments());
        assertEquals(0, invocation.getArguments().length);
    }

    @Test
    public void testIsVerified() {
        Invocation invocation = createInvocation();
        assertFalse(invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        Invocation invocation = createInvocation();
        assertEquals(Integer.valueOf(1), invocation.getSequenceNumber());
    }

    @Test
    public void testIsVerifiedInOrder() {
        Invocation invocation = createInvocation();
        assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        Invocation invocation1 = createInvocation();
        Invocation invocation2 = createInvocation();
        assertTrue(invocation1.equals(invocation2));
    }

    @Test
    public void testHashCode() {
        Invocation invocation = createInvocation();
        assertThrows(RuntimeException.class, () -> invocation.hashCode());
    }

    @Test
    public void testToString() {
        Invocation invocation = createInvocation();
        assertNotNull(invocation.toString());
    }

    @Test
    public void testIsToString() {
        Invocation invocation = createInvocation();
        assertFalse(invocation.isToString(invocation));
    }

    @Test
    public void testIsValidException() {
        Invocation invocation = createInvocation();
        Throwable throwable = new Throwable();
        assertFalse(invocation.isValidException(throwable));
    }

    @Test
    public void testIsValidReturnType() {
        Invocation invocation = createInvocation();
        assertTrue(invocation.isValidReturnType(Object.class));
    }

    @Test
    public void testIsVoid() {
        Invocation invocation = createInvocation();
        assertFalse(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Invocation invocation = createInvocation();
        assertNotNull(invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        Invocation invocation = createInvocation();
        assertNotNull(invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        Invocation invocation = createInvocation();
        assertFalse(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = createInvocation();
        assertNotNull(invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        Invocation invocation = createInvocation();
        assertEquals(0, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        Invocation invocation = createInvocation();
        assertNotNull(invocation.getRawArguments());
        assertEquals(0, invocation.getRawArguments().length);
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Invocation invocation = createInvocation();
        assertNotNull(invocation.callRealMethod());
    }

    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = createInvocation();
        assertNotNull(invocation.toString(new PrintSettings()));
    }

    @Test
    public void testMarkVerified() {
        Invocation invocation = createInvocation();
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        Invocation invocation = createInvocation();
        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerifiedInOrder());
    }
}