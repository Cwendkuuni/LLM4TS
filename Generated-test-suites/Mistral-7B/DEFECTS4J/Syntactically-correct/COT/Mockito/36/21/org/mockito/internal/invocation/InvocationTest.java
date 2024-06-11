import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.ArgumentsMatchers;
import org.mockito.internal.mock.MockitoMock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.exceptions.base.MockitoException;

public class InvocationTest {

    @Test
    public void testConstructor() {
        MockitoMock mock = new MockitoMock();
        MockitoMethod method = MockitoMethods.method(String.class, "method", new Class[0]);
        Invocation invocation = new Invocation(mock, method, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object mock, Object[] args) throws Throwable {
                return null;
            }
        });

        assertEquals(mock, invocation.getMock());
        assertEquals(method, invocation.getMethod());
        assertArrayEquals(new Object[0], invocation.getArguments());
        assertEquals(1, invocation.getSequenceNumber());
        assertFalse(invocation.isVerified());
        assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testExpandVarArgs() {
        MockitoMock mock = new MockitoMock();
        MockitoMethod method = MockitoMethods.method(String.class, "method", new Class[] { int[].class });

        Object[] args = new Object[] { new int[] { 1, 2 }, null };
        Object[] expandedArgs = Invocation.expandVarArgs(method.isVarArgs(), args);

        assertEquals(args.length, expandedArgs.length);
        assertEquals(args[0], expandedArgs[0]);
        assertNull(expandedArgs[expandedArgs.length - 1]);
    }

    @Test
    public void testIsToString() {
        MockitoMock mock = new MockitoMock();
        MockitoMethod toStringMethod = MockitoMethods.method(String.class, "toString", new Class[0]);
        Invocation invocation = new Invocation(mock, toStringMethod, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object mock, Object[] args) throws Throwable {
                return "mock object";
            }
        });

        assertTrue(Invocation.isToString(invocation));
    }

    @Test
    public void testIsVerified() {
        MockitoMock mock = new MockitoMock();
        MockitoMethod method = MockitoMethods.method(String.class, "method", new Class[0]);
        Invocation invocation = new Invocation(mock, method, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object mock, Object[] args) throws Throwable {
                return null;
            }
        });

        assertFalse(invocation.isVerified());

        invocation.markVerified();

        assertTrue(invocation.isVerified());
    }

    @Test
    public void testIsVerifiedInOrder() {
        MockitoMock mock = new MockitoMock();
        MockitoMethod method1 = MockitoMethods.method(String.class, "method1", new Class[0]);
        MockitoMethod method2 = MockitoMethods.method(String.class, "method2", new Class[0]);
        Invocation invocation1 = new Invocation(mock, method1, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object mock, Object[] args) throws Throwable {
                return null;
            }
        });
        Invocation invocation2 = new Invocation(mock, method2, new Object[0], 2, new RealMethod() {
            @Override
            public Object invoke(Object mock, Object[] args) throws Throwable {
                return null;
            }
        });

        assertFalse(invocation1.isVerifiedInOrder());
        assertFalse(invocation2.isVerifiedInOrder());

        invocation1.markVerified();
        invocation1.markVerifiedInOrder();
        invocation2.markVerified();

        assertTrue(invocation1.isVerifiedInOrder());
        assertTrue(invocation2.isVerifiedInOrder());
    }

    @Test(expected = MockitoException.class)
    public void testInvalidReturnType() {
        MockitoMock mock = new MockitoMock();
        MockitoMethod method = MockitoMethods.method(String.class, "method", new Class[] { Integer.class });
        Invocation invocation = new Invocation(mock, method, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object mock, Object[] args) throws Throwable {
                return 1;
            }
        });

        invocation.markVerified();

        try {
            invocation.isValidReturnType(String.class);
            fail("Exception expected");
        } catch (MockitoException e) {
            // expected
        }
    }

    @Test
    public void testValidReturnType() {
        MockitoMock mock = new MockitoMock();
        MockitoMethod method = MockitoMethods.method(String.class, "method", new Class[0]);
        Invocation invocation = new Invocation(mock, method, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object mock, Object[] args) throws Throwable {
                return "return value";
            }
        });

        assertTrue(invocation.isValidReturnType(String.class));
    }

    @Test(expected = MockitoException.class)
    public void testInvalidException() {
        MockitoMock mock = new MockitoMock();
        MockitoMethod method = MockitoMethods.method(String.class, "method", new Class[0]);
        Invocation invocation = new Invocation(mock, method, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object mock, Object[] args) throws Throwable {
                throw new RuntimeException("expected exception");
            }
        });

        invocation.markVerified();

        try {
            invocation.isValidException(new IllegalArgumentException("another exception"));
            fail("Exception expected");
        } catch (MockitoException e) {
            // expected
        }
    }

    @Test
    public void testValidException() {
        MockitoMock mock = new MockitoMock();
        MockitoMethod method = MockitoMethods.method(String.class, "method", new Class[0]);
        Invocation invocation = new Invocation(mock, method, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object mock, Object[] args) throws Throwable {
                throw new IllegalArgumentException("expected exception");
            }
        });

        invocation.markVerified();

        assertTrue(invocation.isValidException(IllegalArgumentException.class));
    }

    @Test
    public void testToString() {
        MockitoMock mock = new MockitoMock();
        MockitoMethod method = MockitoMethods.method(String.class, "method", new Class[0]);
        Invocation invocation = new Invocation(mock, method, new Object[] { "arg1" }, 1, new RealMethod() {
            @Override
            public Object invoke(Object mock, Object[] args) throws Throwable {
                return null;
            }
        });

        List<Matcher> matchers = invocation.argumentsToMatchers();
        PrintSettings printSettings = new PrintSettings();

        String invocationString = invocation.toString(matchers, printSettings);

        String expectedInvocationString = "mock." + method.getName() + ArgumentsMatchers.argumentsToString(matchers, printSettings);
        assertEquals(expectedInvocationString, invocationString);
    }
}