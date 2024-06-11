package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Type;
import java.util.Arrays;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoBaseException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.RealMethod;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;
import org.mockito.internal.util.PrintingFriendlyInvocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.Method;
import org.mockito.stub.Stubber;

public class InvocationTest {

    @Test
    public void testConstructor() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = mockMethod(String.class, "toString");
        Object[] args = new Object[0];
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod() {
            @Override
            public Object invoke(Object target, Object[] args) throws Throwable {
                return null;
            }
        });

        assertEquals(mock, invocation.getMock());
        assertEquals(mockitoMethod, invocation.getMethod());
        assertEquals(args, invocation.getArguments());
        assertEquals(1, invocation.getSequenceNumber());
    }

    @Test
    public void testGetMethodName() {
        MockitoMethod mockitoMethod = mockMethod(String.class, "toString");
        Invocation invocation = new Invocation(mock(String.class), mockitoMethod, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object target, Object[] args) throws Throwable {
                return null;
            }
        });
        assertEquals("toString", invocation.getMethodName());
    }

    @Test
    public void testToString() {
        MockitoMethod mockitoMethod = mockMethod(String.class, "toString");
        Object[] args = new Object[1];
        args[0] = "test";
        Invocation invocation = new Invocation(mock(String.class), mockitoMethod, args, 1, new RealMethod() {
            @Override
            public Object invoke(Object target, Object[] args) throws Throwable {
                return null;
            }
        });
        assertEquals("test.toString()", invocation.toString());
    }

    @Test
    public void testToStringWithMatchers() {
        MockitoMethod mockitoMethod = mockMethod(String.class, "toString");
        Object[] args = new Object[1];
        args[0] = "test";
        Invocation invocation = new Invocation(mock(String.class), mockitoMethod, args, 1, new RealMethod() {
            @Override
            public Object invoke(Object target, Object[] args) throws Throwable {
                return null;
            }
        });

        List<Matcher> matchers = invocation.argumentsToMatchers();
        assertEquals(1, matchers.size());
        Matcher argMatcher = matchers.get(0);
        assertEquals(CoreMatchers.isInstanceOf(String.class), argMatcher);
        assertEquals("test", argMatcher.getValue());

        PrintSettings printSettings = new PrintSettings();
        assertEquals("test.toString()\n" +
                "    arguments:\n" +
                "    test", invocation.toString(printSettings));
    }

    @Test
    public void testIsToString() {
        MockitoMethod mockitoMethod = mockMethod(String.class, "toString");
        Invocation invocation = new Invocation(mock(String.class), mockitoMethod, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object target, Object[] args) throws Throwable {
                return null;
            }
        });
        assertTrue(Invocation.isToString(invocation));
    }

    @Test(expected = RuntimeException.class)
    public void testHashCode() {
        Invocation invocation1 = new Invocation(mock(String.class), mockMethod(String.class, "toString"), new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object target, Object[] args) throws Throwable {
                return null;
            }
        });
        Invocation invocation2 = new Invocation(mock(String.class), mockMethod(String.class, "toString"), new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object target, Object[] args) throws Throwable {
                return null;
            }
        });
        invocation1.hashCode();
    }

    @Test
    public void testIsVerified() {
        Invocation invocation = new Invocation(mock(String.class), mockMethod(String.class, "toString"), new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object target, Object[] args) throws Throwable {
                return null;
            }
        });
        assertFalse(invocation.isVerified());
    }

    @Test
    public void testMarkVerified() {
        Invocation invocation = new Invocation(mock(String.class), mockMethod(String.class, "toString"), new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object target, Object[] args) throws Throwable {
                return null;
            }
        });
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testIsVerifiedInOrder() {
        Invocation invocation1 = new Invocation(mock(String.class), mockMethod(String.class, "toString"), new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object target, Object[] args) throws Throwable {
                return null;
            }
        });
        invocation1.markVerifiedInOrder();
        assertTrue(invocation1.isVerifiedInOrder());

        Invocation invocation2 = new Invocation(mock(String.class), mockMethod(String.class, "toString"), new Object[0], 2, new RealMethod() {
            @Override
            public Object invoke(Object target, Object[] args) throws Throwable {
                return null;
            }
        });
        invocation2.markVerifiedInOrder();
        assertFalse(invocation2.isVerifiedInOrder());
    }

    @Test
    public void testGetArgumentsCount() {
        Invocation invocation = new Invocation(mock(String.class), mockMethod(String.class, "toString"), new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object target, Object[] args) throws Throwable {
                return null;
            }
        });
        assertEquals(0, invocation.getArgumentsCount());

        Object[] args = new Object[1];
        args[0] = "test";
        Invocation invocationWithArgs = new Invocation(mock(String.class), mockMethod(String.class, "toString"), args, 1, new RealMethod() {
            @Override
            public Object invoke(Object target, Object[] args) throws Throwable {
                return null;
            }
        });
        assertEquals(1, invocationWithArgs.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        Object[] args = new Object[1];
        args[0] = "test";
        Invocation invocation = new Invocation(mock(String.class), mockMethod(String.class, "toString"), args, 1, new RealMethod() {
            @Override
            public Object invoke(Object target, Object[] args) throws Throwable {
                return null;
            }
        });
        assertEquals(args, invocation.getRawArguments());
    }

    @Test
    public void testEquals() {
        Invocation invocation1 = new Invocation(mock(String.class), mockMethod(String.class, "toString"), new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object target, Object[] args) throws Throwable {
                return null;
            }
        });
        Invocation invocation2 = new Invocation(mock(String.class), mockMethod(String.class, "toString"), new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object target, Object[] args) throws Throwable {
                return null;
            }
        });
        assertTrue(invocation1.equals(invocation2));

        MockitoMethod mockitoMethod2 = mockMethod(String.class, "toString");
        Invocation invocation3 = new Invocation(mock(String.class), mockitoMethod2, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object target, Object[] args) throws Throwable {
                return null;
            }
        });
        assertFalse(invocation1.equals(invocation3));

        Object[] args = new Object[1];
        args[0] = "test";
        Invocation invocation4 = new Invocation(mock(String.class), mockMethod(String.class, "toString"), args, 1, new RealMethod() {
            @Override
            public Object invoke(Object target, Object[] args) throws Throwable {
                return null;
            }
        });
        assertFalse(invocation1.equals(invocation4));
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = mockMethod(String.class, "length");
        Object[] args = new Object[0];
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod() {
            @Override
            public Object invoke(Object target, Object[] args) throws Throwable {
                return ((String) target).length();
            }
        });
        assertEquals(5, invocation.callRealMethod());
    }

    @Test(expected = MockitoBaseException.class)
    public void testInvalidReturnType() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = mockMethod(String.class, "toString");
        Object[] args = new Object[0];
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod() {
            @Override
            public Integer invoke(Object target, Object[] args) throws Throwable {
                return 5;
            }
        });
        invocation.callRealMethod();
    }

    @Test(expected = MockitoBaseException.class)
    public void testInvalidException() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = mockMethod(String.class, "toString", new Class[0]);
        Object[] args = new Object[0];
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod() {
            @Override
            public void invoke(Object target, Object[] args) throws Throwable {
                throw new RuntimeException("Test exception");
            }
        });
        invocation.callRealMethod();
    }

    @Test(expected = MockitoBaseException.class)
    public void testInvalidVoidMethod() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = mockMethod(Void.TYPE, "toString");
        Object[] args = new Object[0];
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod() {
            @Override
            public void invoke(Object target, Object[] args) throws Throwable {
                // empty method
            }
        });
        invocation.callRealMethod();
    }

    private MockitoMethod mockMethod(Class<?> returnType, String methodName, Class<? extends Throwable>... exceptionTypes) {
        try {
            Method mockitoMethod = Class.forName("org.mockito.internal.invocation.realmethod.RealMethodStubImpl")
                    .getDeclaredMethod(methodName, new Class[0]);
            mockitoMethod.setAccessible(true);
            return new MockitoMethod(returnType, methodName, exceptionTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}