package org.mockito.internal.invocation;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.MatchersPrinter;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.MockitoMethod;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

public class InvocationTest {
    private Invocation invocation;

    @Before
    public void setup() {
        Object mock = mock(Object.class);
        MockitoMethod method = new MockitoMethod("method", Object.class, new Class[]{}, false, false);
        Object[] args = new Object[]{};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);
        invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
    }

    @Test
    public void testGetMock() {
        Object mock = mock(Object.class);
        invocation = new Invocation(mock, null, null, 0, mock(RealMethod.class));
        assertEquals(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        MockitoMethod method = new MockitoMethod("method", Object.class, new Class[]{}, false, false);
        invocation = new Invocation(null, method, null, 0, mock(RealMethod.class));
        assertEquals(method, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        Object[] args = new Object[]{};
        invocation = new Invocation(null, null, args, 0, mock(RealMethod.class));
        assertArrayEquals(args, invocation.getArguments());
    }

    @Test
    public void testIsVerified() {
        assertFalse(invocation.isVerified());
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        int sequenceNumber = 1;
        invocation = new Invocation(null, null, null, sequenceNumber, mock(RealMethod.class));
        assertEquals(sequenceNumber, invocation.getSequenceNumber().intValue());
    }

    @Test
    public void testIsVerifiedInOrder() {
        assertFalse(invocation.isVerifiedInOrder());
        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        Invocation other = new Invocation(null, null, null, 0, mock(RealMethod.class));
        assertTrue(invocation.equals(other));
        other = new Invocation(null, null, new Object[]{}, 0, mock(RealMethod.class));
        assertFalse(invocation.equals(other));
        other = new Invocation(null, null, null, 1, mock(RealMethod.class));
        assertFalse(invocation.equals(other));
        other = new Invocation(null, new MockitoMethod("method", Object.class, new Class[]{}, false, false), null, 0, mock(RealMethod.class));
        assertFalse(invocation.equals(other));
    }

    @Test
    public void testHashCode() {
        try {
            invocation.hashCode();
        } catch (RuntimeException e) {
            fail();
        }
    }

    @Test
    public void testToString() {
        try {
            invocation.toString();
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testToStringPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        try {
            invocation.toString(printSettings);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testArgumentsToMatchers() {
        Object[] args = new Object[]{};
        invocation = new Invocation(null, null, args, 0, mock(RealMethod.class));
        List<Matcher> matchers = new ArrayList<Matcher>();
        assertEquals(matchers, invocation.argumentsToMatchers());
    }

    @Test
    public void testIsToString() {
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        MockitoMethod method = new MockitoMethod("toString", Object.class, new Class[]{}, false, false);
        when(invocationMock.getMethod()).thenReturn(method);
        assertTrue(Invocation.isToString(invocationMock));
    }

    @Test
    public void testIsValidException() {
        Throwable throwable = new Throwable();
        MockitoMethod method = new MockitoMethod("method", Object.class, new Class[]{Throwable.class}, false, false);
        invocation = new Invocation(null, method, new Object[]{throwable}, 0, mock(RealMethod.class));
        assertTrue(invocation.isValidException(throwable));
    }

    @Test
    public void testIsValidReturnType() {
        Class<?> clazz = Object.class;
        MockitoMethod method = new MockitoMethod("method", clazz, new Class[]{}, false, false);
        invocation = new Invocation(null, method, null, 0, mock(RealMethod.class));
        assertTrue(invocation.isValidReturnType(clazz));
    }

    @Test
    public void testIsVoid() {
        MockitoMethod method = new MockitoMethod("method", Void.TYPE, new Class[]{}, false, false);
        invocation = new Invocation(null, method, null, 0, mock(RealMethod.class));
        assertTrue(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        MockitoMethod method = new MockitoMethod("method", Object.class, new Class[]{}, false, false);
        invocation = new Invocation(null, method, null, 0, mock(RealMethod.class));
        assertEquals("Object", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        MockitoMethod method = new MockitoMethod("method", Object.class, new Class[]{}, false, false);
        invocation = new Invocation(null, method, null, 0, mock(RealMethod.class));
        assertEquals("method", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        MockitoMethod method = new MockitoMethod("method", int.class, new Class[]{}, false, false);
        invocation = new Invocation(null, method, null, 0, mock(RealMethod.class));
        assertTrue(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Location location = invocation.getLocation();
        assertNotNull(location);
    }

    @Test
    public void testGetArgumentsCount() {
        Object[] args = new Object[]{};
        invocation = new Invocation(null, null, args, 0, mock(RealMethod.class));
        assertEquals(0, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        Object[] args = new Object[]{};
        invocation = new Invocation(null, null, args, 0, mock(RealMethod.class));
        assertEquals(args, invocation.getRawArguments());
    }

    @Test
    public void testCallRealMethod() {
        try {
            invocation.callRealMethod();
        } catch (Throwable e) {
            fail();
        }
    }

    @Test
    public void testMarkVerified() {
        assertFalse(invocation.isVerified());
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        assertFalse(invocation.isVerifiedInOrder());
        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerifiedInOrder());
    }
}