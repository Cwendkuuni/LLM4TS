package org.mockito.internal.invocation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.mockito.internal.reporting.PrintSettings.printSettings;

import java.util.Arrays;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.matchers.*;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.util.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class InvocationTest {

    @Test
    public void testConstructor() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{}, "testMethod", new Class[]{String.class, int.class}, null);
        Object[] args = new Object[]{null, 1};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod(mockitoMethod, mock) {
            @Override
            public Object invoke(Object mock, Object[] rawArguments) throws Throwable {
                return null;
            }
        });

        assertEquals("mock", invocation.getMock());
        assertEquals("testMethod", invocation.getMethod().getName());
        assertArrayEquals(new Object[]{null, 1}, invocation.getArguments());
        assertEquals(1, invocation.getSequenceNumber());
        assertNull(invocation.getLocation());
        assertFalse(invocation.isVerified());
        assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testToString() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{}, "testMethod", new Class[]{String.class, int.class}, null);
        Object[] args = new Object[]{null, 1};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod(mockitoMethod, mock) {
            @Override
            public Object invoke(Object mock, Object[] rawArguments) throws Throwable {
                return null;
            }
        });

        String expected = "mock.testMethod(null, 1)";
        assertEquals(expected, invocation.toString());

        List<Matcher> matchers = Arrays.asList(new Equals("null"), new Equals(1));
        assertEquals(expected + " -> [" + matchers.toString() + "]", invocation.toString(matchers, printSettings()));
    }

    @Test
    public void testToStringMultiline() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{}, "testMethod", new Class[]{String[].class, int[].class, String.class}, null);
        Object[] args = new Object[]{new Object[]{null}, new Object[]{1}, "foo"};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod(mockitoMethod, mock) {
            @Override
            public Object invoke(Object mock, Object[] rawArguments) throws Throwable {
                return null;
            }
        });

        List<Matcher> matchers = Arrays.asList(ArgThat.arrayContaining(Arrays.asList(new Equals(null))),
                ArgThat.arrayContaining(Arrays.asList(new Equals(1))),
                new Equals("foo"));

        String expected = "mock.testMethod([null], [1], \"foo\")";
        assertEquals(expected, invocation.toString(matchers, printSettings()));

        String result = invocation.toString(matchers, printSettings(true));
        String[] lines = result.split("\n");
        assertEquals(3, lines.length);
        assertEquals(expected, lines[0]);
        assertTrue(lines[1].startsWith("Arguments:"));
        assertTrue(lines[2].startsWith("-> ["));
    }

    @Test(expected = RuntimeException.class)
    public void testHashCode() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{}, "testMethod", new Class[]{String.class, int.class}, null);
        Object[] args = new Object[]{null, 1};
        Invocation invocation1 = new Invocation(mock, mockitoMethod, args, 1, new RealMethod(mockitoMethod, mock) {
            @Override
            public Object invoke(Object mock, Object[] rawArguments) throws Throwable {
                return null;
            }
        });

        Invocation invocation2 = new Invocation(mock, mockitoMethod, args, 1, new RealMethod(mockitoMethod, mock) {
            @Override
            public Object invoke(Object mock, Object[] rawArguments) throws Throwable {
                return null;
            }
        });

        invocation1.hashCode();
        invocation2.hashCode();
    }

    @Test
    public void testEquals() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{}, "testMethod", new Class[]{String.class, int.class}, null);
        Object[] args1 = new Object[]{null, 1};
        Object[] args2 = new Object[]{null, 2};

        Invocation invocation1 = new Invocation(mock, mockitoMethod, args1, 1, new RealMethod(mockitoMethod, mock) {
            @Override
            public Object invoke(Object mock, Object[] rawArguments) throws Throwable {
                return null;
            }
        });

        Invocation invocation2 = new Invocation(mock, mockitoMethod, args1, 1, new RealMethod(mockitoMethod, mock) {
            @Override
            public Object invoke(Object mock, Object[] rawArguments) throws Throwable {
                return null;
            }
        });

        Invocation invocation3 = new Invocation(mock, mockitoMethod, args2, 1, new RealMethod(mockitoMethod, mock) {
            @Override
            public Object invoke(Object mock, Object[] rawArguments) throws Throwable {
                return null;
            }
        });

        assertTrue(invocation1.equals(invocation1));
        assertTrue(invocation1.equals(invocation2));
        assertFalse(invocation1.equals(invocation3));
    }

    @Test
    public void testIsToString() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{}, "toString", new Class[0], null);
        Object[] args = new Object[0];

        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod(mockitoMethod, mock) {
            @Override
            public Object invoke(Object mock, Object[] rawArguments) throws Throwable {
                return mock.toString();
            }
        });

        assertTrue(Invocation.isToString(invocation));
    }

    @Test
    public void testIsValidException() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{}, "testMethod", new Class[]{String.class, int.class}, null);
        Object[] args = new Object[]{null, 1};

        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod(mockitoMethod, mock) {
            @Override
            public Object invoke(Object mock, Object[] rawArguments) throws Exception {
                throw new RuntimeException();
            }
        });

        assertTrue(invocation.isValidException(new RuntimeException()));
        assertFalse(invocation.isValidException(new IOException()));
    }

    @Test
    public void testIsValidReturnType() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{}, "testMethod", new Class[]{String.class}, new Class[]{String.class});
        Object[] args = new Object[]{null};

        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod(mockitoMethod, mock) {
            @Override
            public Object invoke(Object mock, Object[] rawArguments) throws Throwable {
                return null;
            }
        });

        assertTrue(invocation.isValidReturnType(String.class));
        assertFalse(invocation.isValidReturnType(Integer.class));
    }

    @Test
    public void testIsVoid() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{Void.TYPE}, "testMethod", new Class[0], null);

        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod(mockitoMethod, mock) {
            @Override
            public Object invoke(Object mock, Object[] rawArguments) throws Throwable {
                return null;
            }
        });

        assertTrue(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{String.class}, "testMethod", new Class[0], null);

        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod(mockitoMethod, mock) {
            @Override
            public Object invoke(Object mock, Object[] rawArguments) throws Throwable {
                return null;
            }
        });

        assertEquals("String", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{}, "testMethod", new Class[]{String.class}, null);

        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod(mockitoMethod, mock) {
            @Override
            public Object invoke(Object mock, Object[] rawArguments) throws Throwable {
                return null;
            }
        });

        assertEquals("testMethod", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{boolean.class}, "testMethod", new Class[0], null);

        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod(mockitoMethod, mock) {
            @Override
            public Object invoke(Object mock, Object[] rawArguments) throws Throwable {
                return true;
            }
        });

        assertTrue(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{String.class}, "testMethod", new Class[0], null);

        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod(mockitoMethod, mock) {
            @Override
            public Object invoke(Object mock, Object[] rawArguments) throws Throwable {
                return null;
            }
        });

        Location location = new Location();
        invocation.setLocation(location);
        assertEquals(location, invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{String.class}, "testMethod", new Class[]{String.class}, null);

        Object[] args = new Object[]{null};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod(mockitoMethod, mock) {
            @Override
            public Object invoke(Object mock, Object[] rawArguments) throws Throwable {
                return null;
            }
        });

        assertEquals(1, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{String.class}, "testMethod", new Class[]{String.class}, null);

        Object[] args = new Object[]{null};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod(mockitoMethod, mock) {
            @Override
            public Object invoke(Object mock, Object[] rawArguments) throws Throwable {
                return null;
            }
        });

        assertEquals(args, invocation.getRawArguments());
    }

    @Test
    public void testCallRealMethod() throws Exception {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{String.class}, "testMethod", new Class[]{String.class}, null);

        Object[] args = new Object[]{null};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod(mockitoMethod, mock) {
            @Override
            public Object invoke(Object mock, Object[] rawArguments) throws Throwable {
                return "foo";
            }
        });

        assertEquals("foo", invocation.callRealMethod());
    }

    @Test
    public void testMarkVerified() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{String.class}, "testMethod", new Class[]{String.class}, null);
        Object[] args = new Object[]{null};

        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod(mockitoMethod, mock) {
            @Override
            public Object invoke(Object mock, Object[] rawArguments) throws Throwable {
                return null;
            }
        });

        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{String.class}, "testMethod", new Class[]{String.class}, null);
        Object[] args = new Object[]{null};

        Invocation invocation1 = new Invocation(mock, mockitoMethod, args, 1, new RealMethod(mockitoMethod, mock) {
            @Override
            public Object invoke(Object mock, Object[] rawArguments) throws Throwable {
                return null;
            }
        });
        Invocation invocation2 = new Invocation(mock, mockitoMethod, args, 2, new RealMethod(mockitoMethod, mock) {
            @Override
            public Object invoke(Object mock, Object[] rawArguments) throws Throwable {
                return null;
            }
        });

        invocation1.markVerified();
        invocation1.markVerifiedInOrder();
        invocation2.markVerified();
        invocation2.markVerifiedInOrder();

        assertTrue(invocation1.isVerified());
        assertTrue(invocation1.isVerifiedInOrder());
        assertTrue(invocation2.isVerified());
        assertTrue(invocation2.isVerifiedInOrder());
    }

    @Test
    public void testToStringAnswer() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{}, "testMethod", new Class[]{String.class}, null);

        Answer answer = invocation -> "answer";
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod(mockitoMethod, mock) {
            @Override
            public Object invoke(Object mock, Object[] rawArguments) throws Throwable {
                return null;
            }
        }, answer);

        assertEquals("answer", invocation.callRealMethod());

        assertEquals("testMethod() -> \"answer\"", invocation.toString());
    }
}