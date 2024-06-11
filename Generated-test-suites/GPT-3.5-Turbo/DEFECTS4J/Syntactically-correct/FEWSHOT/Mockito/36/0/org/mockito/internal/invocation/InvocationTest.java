package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.matchers.*;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;
import org.mockito.invocation.InvocationOnMock;

public class InvocationTest {
    // Test data
    private Object mockObject = new Object();
    private MockitoMethod mockitoMethod = new MockitoMethod("methodName", null);
    private Object[] args = new Object[] {"arg1", "arg2"};
    private int sequenceNumber = 1;
    private RealMethod realMethod = null;

    // Test objects
    private Invocation invocationUnderTest = new Invocation(mockObject, mockitoMethod, args, sequenceNumber, realMethod);

    @Test
    public void testGetMock() {
        assertEquals(mockObject, invocationUnderTest.getMock());
    }

    @Test
    public void testGetMethod() {
        assertEquals(mockitoMethod, invocationUnderTest.getMethod());
    }

    @Test
    public void testGetArguments() {
        assertEquals(args, invocationUnderTest.getArguments());
    }

    @Test
    public void testIsVerified() {
        assertFalse(invocationUnderTest.isVerified());
        invocationUnderTest.markVerified();
        assertTrue(invocationUnderTest.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        assertEquals(Integer.valueOf(sequenceNumber), invocationUnderTest.getSequenceNumber());
    }

    @Test
    public void testIsVerifiedInOrder() {
        assertFalse(invocationUnderTest.isVerifiedInOrder());
        invocationUnderTest.markVerifiedInOrder();
        assertTrue(invocationUnderTest.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        Invocation invocation1 = new Invocation(mockObject, mockitoMethod, args, sequenceNumber, realMethod);
        Invocation invocation2 = new Invocation(mockObject, mockitoMethod, args, sequenceNumber, realMethod);
        Invocation invocation3 = new Invocation(new Object(), new MockitoMethod("otherMethod", null), new Object[]{"otherArg1"}, 2, null);

        assertTrue(invocation1.equals(invocation2));
        assertFalse(invocation1.equals(invocation3));
    }

    @Test
    public void testHashCode() {
        try {
            invocationUnderTest.hashCode();
        } catch (RuntimeException e) {
            assertEquals("hashCode() is not implemented", e.getMessage());
        }
    }

    @Test
    public void testToString() {
        String expectedToString = new MockUtil().getMockName(mockObject) + ".methodName(arg1, arg2)";
        assertEquals(expectedToString, invocationUnderTest.toString());
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings().multiline(true);
        String expectedToString = new MockUtil().getMockName(mockObject) + ".methodName(\narg1,\narg2\n)";
        assertEquals(expectedToString, invocationUnderTest.toString(printSettings));
    }

    @Test
    public void testToStringWithMatchers() {
        List<Matcher> matchers = Arrays.asList(new Equals("arg1"), new Equals("arg2"));
        String expectedToString = new MockUtil().getMockName(mockObject) + ".methodName(arg1, arg2)";
        assertEquals(expectedToString, invocationUnderTest.toString(matchers, new PrintSettings()));
    }

    @Test
    public void testToStringWithMatchersAndPrintSettings() {
        List<Matcher> matchers = Arrays.asList(new Equals("arg1"), new Equals("arg2"));
        PrintSettings printSettings = new PrintSettings().multiline(true);
        String expectedToString = new MockUtil().getMockName(mockObject) + ".methodName(\narg1,\narg2\n)";
        assertEquals(expectedToString, invocationUnderTest.toString(matchers, printSettings));
    }

    @Test
    public void testArgumentsToMatchers() {
        List<Matcher> matchers = invocationUnderTest.argumentsToMatchers();
        assertEquals(2, matchers.size());
        
        assertTrue(matchers.get(0) instanceof Equals);
        assertEquals("arg1", ((Equals) matchers.get(0)).getExpected());

        assertTrue(matchers.get(1) instanceof Equals);
        assertEquals("arg2", ((Equals) matchers.get(1)).getExpected());
    }

    @Test
    public void testIsToString() {
        InvocationOnMock invocation = new InvocationOnMock() {
            @Override
            public Object getMock() {
                return null;
            }

            @Override
            public MockitoMethod getMethod() {
                return new MockitoMethod("toString", null);
            }

            @Override
            public Object[] getArguments() {
                return null;
            }

            @Override
            public boolean isVerified() {
                return false;
            }

            @Override
            public Integer getSequenceNumber() {
                return null;
            }

            @Override
            public boolean isVerifiedInOrder() {
                return false;
            }
        };

        assertTrue(Invocation.isToString(invocation));
    }

    @Test
    public void testIsValidException() {
        Throwable throwable = new RuntimeException();
        assertFalse(invocationUnderTest.isValidException(throwable));

        mockitoMethod = new MockitoMethod("methodName", new Class[]{RuntimeException.class});
        invocationUnderTest = new Invocation(mockObject, mockitoMethod, args, sequenceNumber, realMethod);
        assertTrue(invocationUnderTest.isValidException(throwable));
    }

    @Test
    public void testIsValidReturnType() {
        Class<?> clazz = String.class;
        assertTrue(invocationUnderTest.isValidReturnType(clazz));

        mockitoMethod = new MockitoMethod("methodName", new Class[]{Integer.class});
        invocationUnderTest = new Invocation(mockObject, mockitoMethod, args, sequenceNumber, realMethod);
        assertFalse(invocationUnderTest.isValidReturnType(clazz));
    }

    @Test
    public void testIsVoid() {
        assertTrue(invocationUnderTest.isVoid());

        mockitoMethod = new MockitoMethod("methodName", Integer.class);
        invocationUnderTest = new Invocation(mockObject, mockitoMethod, args, sequenceNumber, realMethod);
        assertFalse(invocationUnderTest.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        assertEquals("void", invocationUnderTest.printMethodReturnType());

        mockitoMethod = new MockitoMethod("methodName", String.class);
        invocationUnderTest = new Invocation(mockObject, mockitoMethod, args, sequenceNumber, realMethod);
        assertEquals("String", invocationUnderTest.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        assertEquals("methodName", invocationUnderTest.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        assertFalse(invocationUnderTest.returnsPrimitive());

        mockitoMethod = new MockitoMethod("methodName", int.class);
        invocationUnderTest = new Invocation(mockObject, mockitoMethod, args, sequenceNumber, realMethod);
        assertTrue(invocationUnderTest.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Location location = invocationUnderTest.getLocation();
        assertEquals(invocationUnderTest, location.getInvocation());
    }

    @Test
    public void testGetArgumentsCount() {
        assertEquals(2, invocationUnderTest.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        assertEquals(args, invocationUnderTest.getRawArguments());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Object result = invocationUnderTest.callRealMethod();
        assertNull(result);
    }

    @Test
    public void testToStringWithPrintSettingsArg() {
        PrintSettings printSettings = new PrintSettings().multiline(true);
        String expectedToString = new MockUtil().getMockName(mockObject) + ".methodName(\narg1,\narg2\n)";
        assertEquals(expectedToString, invocationUnderTest.toString(printSettings));
    }

    @Test
    public void testMarkVerified() {
        assertFalse(invocationUnderTest.isVerified());
        invocationUnderTest.markVerified();
        assertTrue(invocationUnderTest.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        assertFalse(invocationUnderTest.isVerified());
        assertFalse(invocationUnderTest.isVerifiedInOrder());
        invocationUnderTest.markVerifiedInOrder();
        assertTrue(invocationUnderTest.isVerified());
        assertTrue(invocationUnderTest.isVerifiedInOrder());
    }
}