import static org.junit.Assert.*;
import org.junit.Test;

public class InvocationTest {

    @Test
    public void testGetMock() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Invocation inv = new Invocation(mock, method, new Object[]{}, 1, null);
        assertEquals(mock, inv.getMock());
    }

    @Test
    public void testGetMethod() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Invocation inv = new Invocation(mock, method, new Object[]{}, 1, null);
        assertEquals(method, inv.getMethod());
    }

    @Test
    public void testGetArguments() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{1, "test"};
        Invocation inv = new Invocation(mock, method, args, 1, null);
        assertArrayEquals(args, inv.getArguments());
    }

    @Test
    public void testIsVerified() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Invocation inv = new Invocation(mock, method, new Object[]{}, 1, null);
        assertFalse(inv.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Invocation inv = new Invocation(mock, method, new Object[]{}, 1, null);
        assertEquals(Integer.valueOf(1), inv.getSequenceNumber());
    }

    @Test
    public void testIsVerifiedInOrder() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Invocation inv = new Invocation(mock, method, new Object[]{}, 1, null);
        assertFalse(inv.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        Object mock1 = new Object();
        Object mock2 = new Object();
        MockitoMethod methodA = new MockitoMethod();
        MockitoMethod methodB = new MockitoMethod();
        Object[] args1 = new Object[]{1, "test"};
        Object[] args2 = new Object[]{2, "test2"};
        Invocation inv1 = new Invocation(mock1, methodA, args1, 1, null);
        Invocation inv2 = new Invocation(mock1, methodA, args1, 1, null);
        Invocation inv3 = new Invocation(mock2, methodA, args1, 1, null);
        Invocation inv4 = new Invocation(mock1, methodB, args1, 1, null);
        Invocation inv5 = new Invocation(mock1, methodA, args2, 1, null);
        assertTrue(inv1.equals(inv2));
        assertFalse(inv1.equals(inv3));
        assertFalse(inv1.equals(inv4));
        assertFalse(inv1.equals(inv5));
    }

    @Test
    public void testToString() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{1, "test"};
        Invocation inv = new Invocation(mock, method, args, 1, null);
        assertEquals("testMethod(1, test)", inv.toString());
    }

    @Test
    public void testIsToString() {
        MockitoMethod toStrMethod = new MockitoMethod("toString", Object.class, new Class<?>[]{});
        MockitoMethod otherMethod = new MockitoMethod("other", Object.class, new Class<?>[]{});
        Invocation toStrInv = new Invocation(new Object(), toStrMethod, new Object[]{}, 1, null);
        Invocation otherInv = new Invocation(new Object(), otherMethod, new Object[]{}, 1, null);
        assertTrue(Invocation.isToString(toStrInv));
        assertFalse(Invocation.isToString(otherInv));
    }

    @Test
    public void testIsValidException() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Invocation inv = new Invocation(mock, method, new Object[]{}, 1, null);
        assertTrue(inv.isValidException(new Exception()));
        assertFalse(inv.isValidException(new RuntimeException()));
    }

    @Test
    public void testIsValidReturnType() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Invocation inv = new Invocation(mock, method, new Object[]{}, 1, null);
        assertTrue(inv.isValidReturnType(Object.class));
        assertFalse(inv.isValidReturnType(Integer.class));
    }

    @Test
    public void testIsVoid() {
        Object mock = new Object();
        MockitoMethod voidMethod = new MockitoMethod("voidMethod", void.class, new Class<?>[]{});
        MockitoMethod otherMethod = new MockitoMethod("otherMethod", Object.class, new Class<?>[]{});
        Invocation voidInv = new Invocation(mock, voidMethod, new Object[]{}, 1, null);
        Invocation otherInv = new Invocation(mock, otherMethod, new Object[]{}, 1, null);
        assertTrue(voidInv.isVoid());
        assertFalse(otherInv.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod("methodName", String.class, new Class<?>[]{});
        Invocation inv = new Invocation(mock, method, new Object[]{}, 1, null);
        assertEquals("String", inv.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod("methodName", String.class, new Class<?>[]{});
        Invocation inv = new Invocation(mock, method, new Object[]{}, 1, null);
        assertEquals("methodName", inv.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        Object mock = new Object();
        MockitoMethod primitiveMethod = new MockitoMethod("primitiveMethod", int.class, new Class<?>[]{});
        MockitoMethod objectMethod = new MockitoMethod("objectMethod", Object.class, new Class<?>[]{});
        Invocation primitiveInv = new Invocation(mock, primitiveMethod, new Object[]{}, 1, null);
        Invocation objectInv = new Invocation(mock, objectMethod, new Object[]{}, 1, null);
        assertTrue(primitiveInv.returnsPrimitive());
        assertFalse(objectInv.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Invocation inv = new Invocation(mock, method, new Object[]{}, 1, null);
        assertNotNull(inv.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{1, "test", new Object()};
        Invocation inv = new Invocation(mock, method, args, 1, null);
        assertEquals(3, inv.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{1, "test", new Object()};
        Invocation inv = new Invocation(mock, method, args, 1, null);
        assertArrayEquals(args, inv.getRawArguments());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCallRealMethod() throws Throwable {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Invocation inv = new Invocation(mock, method, new Object[]{}, 1, null);
        inv.callRealMethod();
    }

    @Test
    public void testToStringWithPrintSettings() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{1, "test", new Object()};
        Invocation inv = new Invocation(mock, method, args, 1, null);
        assertEquals("testMethod(\n    1,\n    test,\n    [Ljava.lang.Object;@hash\n)", inv.toString(new PrintSettings().multiline()));
    }

    @Test
    public void testMarkVerified() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Invocation inv = new Invocation(mock, method, new Object[]{}, 1, null);
        assertFalse(inv.isVerified());
        inv.markVerified();
        assertTrue(inv.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Invocation inv = new Invocation(mock, method, new Object[]{}, 1, null);
        assertFalse(inv.isVerifiedInOrder());
        assertFalse(inv.isVerified());
        inv.markVerifiedInOrder();
        assertTrue(inv.isVerifiedInOrder());
        assertTrue(inv.isVerified());
    }

}