import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.RealMethod;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.exceptions.base.MockitoException;

public class InvocationTest {

    @Test
    public void testGetMock() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(new Class[]{}, new Class[]{String.class}), new Object[]{"hello"}, 1, new RealMethod(new Object(), new Class[0], new Class[0]));
        assertEquals("expected mock object", new Object(), invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(new Class[]{}, new Class[]{String.class}), new Object[]{"hello"}, 1, new RealMethod(new Object(), new Class[0], new Class[0]));
        assertEquals("expected method object", new MockitoMethod(new Class[]{}, new Class[]{String.class}), invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(new Class[]{}, new Class[]{String.class}), new Object[]{"hello"}, 1, new RealMethod(new Object(), new Class[0], new Class[0]));
        Object[] expectedArguments = new Object[]{"hello"};
        assertArrayEquals("expected arguments", expectedArguments, invocation.getArguments());
    }

    @Test
    public void testIsVerified() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(new Class[]{}, new Class[]{String.class}), new Object[]{"hello"}, 1, new RealMethod(new Object(), new Class[0], new Class[0]));
        assertFalse("invocation not verified yet", invocation.isVerified());

        invocation.markVerified();
        assertTrue("invocation verified now", invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        Invocation invocation1 = new Invocation(new Object(), new MockitoMethod(new Class[]{}, new Class[]{String.class}), new Object[]{"hello"}, 1, new RealMethod(new Object(), new Class[0], new Class[0]));
        Invocation invocation2 = new Invocation(new Object(), new MockitoMethod(new Class[]{}, new Class[]{String.class}), new Object[]{"world"}, 2, new RealMethod(new Object(), new Class[0], new Class[0]));
        assertEquals("expected sequence number of first invocation", 1, invocation1.getSequenceNumber());
        assertEquals("expected sequence number of second invocation", 2, invocation2.getSequenceNumber());
    }

    @Test
    public void testIsVerifiedInOrder() {
        Invocation invocation1 = new Invocation(new Object(), new MockitoMethod(new Class[]{}, new Class[]{String.class}), new Object[]{"hello"}, 1, new RealMethod(new Object(), new Class[0], new Class[0]));
        Invocation invocation2 = new Invocation(new Object(), new MockitoMethod(new Class[]{}, new Class[]{String.class}), new Object[]{"world"}, 2, new RealMethod(new Object(), new Class[0], new Class[0]));
        assertFalse("invocations not verified in order yet", invocation1.isVerifiedInOrder());

        invocation1.markVerifiedInOrder();
        invocation1.markVerified();
        invocation2.markVerifiedInOrder();
        invocation2.markVerified();

        assertTrue("invocations verified in order now", invocation1.isVerifiedInOrder());
        assertTrue("invocations verified in order now", invocation2.isVerifiedInOrder());
    }

    @Test(expected = AssertionError.class)
    public void testEqualsWithDifferentMock() {
        Invocation invocation1 = new Invocation(new Object(), new MockitoMethod(new Class[]{}, new Class[]{String.class}), new Object[]{"hello"}, 1, new RealMethod(new Object(), new Class[0], new Class[0]));
        Invocation invocation2 = new Invocation(new Object(), new MockitoMethod(new Class[]{}, new Class[]{String.class}), new Object[]{"world"}, 2, new RealMethod(new Object(), new Class[0], new Class[0]));
        assertNotEquals("different mocks", invocation1, invocation2);
    }

    @Test
    public void testEqualsWithSameArguments() {
        Invocation invocation1 = new Invocation(new Object(), new MockitoMethod(new Class[]{}, new Class[]{String.class}), new Object[]{"hello"}, 1, new RealMethod(new Object(), new Class[0], new Class[0]));
        Invocation invocation2 = new Invocation(new Object(), new MockitoMethod(new Class[]{}, new Class[]{String.class}), new Object[]{"hello"}, 1, new RealMethod(new Object(), new Class[0], new Class[0]));
        assertEquals("same invocations", invocation1, invocation2);
    }

    @Test(expected = RuntimeException.class)
    public void testHashCode() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(new Class[]{}, new Class[]{String.class}), new Object[]{"hello"}, 1, new RealMethod(new Object(), new Class[0], new Class[0]));
        throw new RuntimeException("hashCode() is not implemented");
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(new Class[]{}, new Class[]{String.class}), new Object[]{"hello"}, 1, new RealMethod(new Object(), new Class[0], new Class[0]));
        List<Matcher> matchers = invocation.argumentsToMatchers();
        String expectedToString = "Invocation on mock <mock> with method: public java.lang.String foo(java.lang.String)" +
                " arguments: [hello]";
        assertEquals("expected toString", expectedToString, invocation.toString());
    }

    @Test
    public void testIsToString() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(Void.TYPE, new Class[]{}, new Class[0]), null, 1, new RealMethod(new Object(), new Class[0], new Class[0]));
        assertTrue("invocation is a method call to toString()", Invocation.isToString(invocation));
    }

    @Test(expected = MockitoException.class)
    public void testInvalidException() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(new Class[]{}, new Class[]{String.class}), new Object[]{"hello"}, 1, new RealMethod(new Object(), new Class[0], new Class[]{RuntimeException.class}));
        Throwable throwable = new RuntimeException();
        assertFalse("expected exception is not a valid exception for invocation", invocation.isValidException(throwable));
        invocation.markVerified();
        try {
            invocation.isValidException(throwable);
        } catch (MockitoException e) {
            assertEquals("expected exception message", "Unexpected exception type: " + throwable.getClass().getName(), e.getMessage());
            throw e;
        }
    }

    @Test
    public void testInvalidReturnType() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(new Class[]{Integer.TYPE}, new Class[]{String.class}), new Object[]{"hello"}, 1, new RealMethod(new Object(), new Class[]{String.class}, new Class[0]));
        assertFalse("expected return type is not valid for invocation", invocation.isValidReturnType(Integer.TYPE));
    }

    @Test
    public void testValidReturnType() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(new Class[]{String.class}, new Class[]{String.class}), new Object[]{"hello"}, 1, new RealMethod(new Object(), new Class[]{String.class}, new Class[0]));
        assertTrue("expected return type is valid for invocation", invocation.isValidReturnType(String.class));
    }

    @Test
    public void testIsVoid() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(Void.TYPE, new Class[]{}, new Class[0]), null, 1, new RealMethod(new Object(), new Class[0], new Class[0]));
        assertTrue("expected invocation to be void", invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(new Class[]{String.class}, new Class[]{String.class}), new Object[]{"hello"}, 1, new RealMethod(new Object(), new Class[]{String.class}, new Class[0]));
        assertEquals("expected return type String", "String", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(new Class[]{String.class}, new Class[]{String.class}), new Object[]{"hello"}, 1, new RealMethod(new Object(), new Class[]{String.class}, new Class[0]));
        assertEquals("expected method name", "foo", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(Boolean.TYPE, new Class[]{}, new Class[0]), null, 1, new RealMethod(new Object(), new Class[]{Boolean.TYPE}, new Class[0]));
        assertTrue("expected invocation to return primitive boolean", invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(new Class[]{String.class}, new Class[]{String.class}), new Object[]{"hello"}, 1, new RealMethod(new Object(), new Class[]{String.class}, new Class[0]));
        assertNotNull("expected location to be set", invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(new Class[]{String.class}, new Class[]{String.class, Integer.TYPE}), new Object[]{"hello", 1}, 1, new RealMethod(new Object(), new Class[]{String.class, Integer.TYPE}, new Class[0]));
        assertEquals("expected number of arguments", 2, invocation.getArgumentsCount());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(new Class[]{String.class}, new Class[]{String.class}), new Object[]{"hello"}, 1, new RealMethod(new Object(), new Class[]{String.class}, new Class[0]));
        assertEquals("expected callRealMethod to return 'hello'", "hello", invocation.callRealMethod());
    }
}