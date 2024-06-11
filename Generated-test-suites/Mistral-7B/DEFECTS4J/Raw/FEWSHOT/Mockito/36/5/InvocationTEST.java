package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.matchers.*;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.*;
import org.mockito.invocation.*;

public class InvocationTest {

    @Test
    public void testConstructor() {
        Object mock = mock(Object.class);
        MockitoMethod method = mockMethod(Object.class, "test", new Class[0]);
        Object[] arguments = new Object[0];
        Invocation invocation = new Invocation(mock, method, arguments, 1, new RealMethod(mock, method, arguments) {
            @Override
            public Object invoke(Object proxy, Object[] invocation) throws Throwable {
                fail("should not invoke real method");
            }
        });

        assertEquals(mock, invocation.getMock());
        assertEquals(method, invocation.getMethod());
        assertEquals(arguments, invocation.getArguments());
        assertEquals(1, invocation.getSequenceNumber());
        assertNull(invocation.getLocation());
        assertFalse(invocation.isVerified());
        assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testExpandVarArgs() {
        Object mock = mock(Object.class);
        Object[] args = new Object[] { null, new Object[] { 1, 2, 3 } };
        Object[] expandedArgs = Invocation.expandVarArgs(true, args);

        assertEquals(args.length + 1, expandedArgs.length);
        assertEquals(args[0], expandedArgs[0]);
        assertEquals(args[1], expandedArgs[args.length]);
    }

    @Test
    public void testEquals() {
        Object mock = mock(Object.class);
        MockitoMethod method = mockMethod(Object.class, "test", new Class[] { Integer.TYPE });
        Object[] args1 = new Object[] { 1 };
        Object[] args2 = new Object[] { 2 };

        Invocation invocation1 = new Invocation(mock, method, args1, 1, new RealMethod(mock, method, args1) {
            @Override
            public Object invoke(Object proxy, Object[] invocation) throws Throwable {
                return null;
            }
        });

        Invocation invocation2 = new Invocation(mock, method, args2, 2, new RealMethod(mock, method, args2) {
            @Override
            public Object invoke(Object proxy, Object[] invocation) throws Throwable {
                return null;
            }
        });

        Invocation invocation3 = new Invocation(mock, method, args1, 1, new RealMethod(mock, method, args1) {
            @Override
            public Object invoke(Object proxy, Object[] invocation) throws Throwable {
                return null;
            }
        });

        Invocation invocation4 = new Invocation(mock, method, args2, 2, new RealMethod(mock, method, args1) {
            @Override
            public Object invoke(Object proxy, Object[] invocation) throws Throwable {
                return null;
            }
        });

        Invocation invocation5 = new Invocation(mock, method, args1, 1, new RealMethod(mock, method, args2) {
            @Override
            public Object invoke(Object proxy, Object[] invocation) throws Throwable {
                return null;
            }
        });

        assertTrue(invocation1.equals(invocation1));
        assertTrue(invocation1.equals(invocation3));
        assertFalse(invocation1.equals(invocation2));
        assertFalse(invocation1.equals(invocation4));
        assertFalse(invocation1.equals(invocation5));
    }

    @Test
    public void testToString() {
        Object mock = mock(Object.class);
        MockitoMethod method = mockMethod(Object.class, "test", new Class[] { Integer.TYPE });
        Object arg = 1;

        Invocation invocation = new Invocation(mock, method, new Object[] { arg }, 1, new RealMethod(mock, method, new Object[] { arg }) {
            @Override
            public Object invoke(Object proxy, Object[] invocation) throws Throwable {
                return null;
            }
        });

        List<Matcher> matchers = invocation.argumentsToMatchers();
        Matcher argMatcher = matchers.get(0);

        assertEquals("arguments: [1]", invocation.toString());
        assertEquals("[1]", argMatcher.toString());
    }

    @Test
    public void testToStringWithMultilinePrintSettings() {
        Object mock = mock(Object.class);
        MockitoMethod method = mockMethod(Object.class, "test", new Class[] { Integer.TYPE, Integer.TYPE });
        Object arg1 = 1;
        Object arg2 = 2;

        Invocation invocation = new Invocation(mock, method, new Object[] { arg1, arg2 }, 1, new RealMethod(mock, method, new Object[] { arg1, arg2 }) {
            @Override
            public Object invoke(Object proxy, Object[] invocation) throws Throwable {
                return null;
            }
        });

        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(true);
        printSettings.setIndent(2);

        String invocationString = invocation.toString(invocation.argumentsToMatchers(), printSettings);

        String expected = "test(int arg1, int arg2)\n" +
                "    arguments:\n" +
                "        arg1: 1\n" +
                "        arg2: 2";

        assertEquals(expected, invocationString);
    }

    @Test
    public void testGetLocation() {
        Object mock = mock(Object.class);
        MockitoMethod method = mockMethod(Object.class, "test", new Class[0]);
        Object[] args = new Object[0];

        Invocation invocation = new Invocation(mock, method, args, 1, new RealMethod(mock, method, args) {
            @Override
            public Object invoke(Object proxy, Object[] invocation) throws Throwable {
                return null;
            }
        });

        Location location = invocation.getLocation();

        assertNotNull(location);
        assertNull(location.getClassName());
        assertNull(location.getFileName());
        assertNull(location.getLineNumber());
    }

    @Test(expected = RuntimeException.class)
    public void testHashCode() {
        Object mock = mock(Object.class);
        MockitoMethod method = mockMethod(Object.class, "test", new Class[0]);
        Object[] arguments = new Object[0];

        Invocation invocation = new Invocation(mock, method, arguments, 1, new RealMethod(mock, method, arguments) {
            @Override
            public Object invoke(Object proxy, Object[] invocation) throws Throwable {
                return null;
            }
        });

        invocation.hashCode();
    }

    @Test
    public void testIsToString() {
        Object mock = mock(Object.class);
        MockitoMethod method = mockMethod(Object.class, "toString", new Class[0]);

        assertTrue(Invocation.isToString(mock(InvocationOnMock.class).andStub(i -> i.getMethod()).usingMock(mock)));
        assertFalse(Invocation.isToString(mock(InvocationOnMock.class).andStub(i -> i.getMethod()).usingMock(mock).thenReturn(new Object() {
            public String toString() {
                return "";
            }
        })));
    }

    @Test
    public void testIsValidException() {
        Object mock = mock(Object.class);
        MockitoMethod method = mockMethod(Object.class, "test", new Class[] { Exception.class });
        Invocation invocation = new Invocation(mock, method, new Object[] { new Exception("test") }, 1, new RealMethod(mock, method, new Object[] { Exception.class }) {
            @Override
            public Object invoke(Object proxy, Object[] invocation) throws Throwable {
                throw new Exception("test");
            }
        });

        assertTrue(invocation.isValidException(invocation.getThrowable()));
    }

    @Test
    public void testIsValidReturnType() {
        Object mock = mock(Object.class);
        MockitoMethod method = mockMethod(Object.class, "test", new Class[] { Integer.TYPE });

        assertTrue(invocation(mock).method(method).returns(Integer.TYPE).invoke().isValidReturnType(Integer.TYPE));
        assertFalse(invocation(mock).method(method).returns(String.class).invoke().isValidReturnType(Integer.TYPE));
    }

    @Test
    public void testIsVoid() {
        Object mock = mock(Object.class);
        MockitoMethod method = mockMethod(Object.class, "test", new Class[0]);

        assertTrue(invocation(mock).method(method).andStub(i -> i.callRealMethod()).invoke().isVoid());
        assertFalse(invocation(mock).method(new MockitoMethod("test", new Class[0]) {
            @Override
            public Class<?> getReturnType() {
                return Integer.TYPE;
            }
        }).andStub(i -> i.callRealMethod()).invoke().isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Object mock = mock(Object.class);
        MockitoMethod method = mockMethod(Object.class, "test", new Class[] { Integer.TYPE });

        assertEquals("Integer", invocation(mock).method(method).invoke().printMethodReturnType());
    }

    @Test(expected = MockitoException.class)
    public void testMarkVerifiedWithVerifiedException() {
        Object mock = mock(Object.class);
        MockitoMethod method = mockMethod(Object.class, "test", new Class[0]);
        Object[] arguments = new Object[0];

        Invocation invocation = new Invocation(mock, method, arguments, 1, new RealMethod(mock, method, arguments) {
            @Override
            public Object invoke(Object proxy, Object[] invocation) throws Throwable {
                throw new MockitoException("Verification failed: test()");
            }
        });

        invocation.markVerified();
    }

    @Test
    public void testMarkVerifiedInOrder() {
        Object mock = mock(Object.class);
        MockitoMethod method = mockMethod(Object.class, "test", new Class[0]);
        Object[] arguments = new Object[0];

        Invocation invocation = new Invocation(mock, method, arguments, 1, new RealMethod(mock, method, arguments) {
            @Override
            public Object invoke(Object proxy, Object[] invocation) throws Throwable {
                return null;
            }
        });

        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerified());
        assertTrue(invocation.isVerifiedInOrder());
    }
}