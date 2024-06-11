package org.mockito.tests.internal.invocation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.matchers.ArgumentsMatchers.*;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.invocation.InvocationOnMock;

import java.util.Arrays;

import static org.mockito.internal.util.ArrayEquals.createObjectArray;
import static org.mockito.internal.util.MockUtil.getMockName;
import static org.mockito.internal.util.Primitives.primitiveTypeOf;

/**
 * This class contains tests to verify the functionality of Invocation class.
 */
public class InvocationTest {

    private Object mockObject;
    private Invocation invocation;

    @Before
    public void setup() {
        mockObject = mock(String.class);
    }

    @Test
    public void shouldCreateInvocationObject() {
        // given
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[]{1, "test"};
        RealMethod realMethod = mock(RealMethod.class);

        // when
        invocation = new Invocation(mockObject, mockitoMethod, arguments, 1, realMethod);

        // then
        assertInstanceOf(Invocation.class, invocation);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenInvokedWithNullMethod() {
        // given
        Object[] arguments = new Object[]{1, "test"};
        RealMethod realMethod = mock(RealMethod.class);

        // when
        Invocation invocation = new Invocation(mockObject, null, arguments, 1, realMethod);

        // then
        throw new RuntimeException("Method cannot be null");
    }

    @Test
    public void testGetMock() {
        // given
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[]{1, "test"};
        RealMethod realMethod = mock(RealMethod.class);

        // when
        Invocation invocation = new Invocation(mockObject, mockitoMethod, arguments, 1, realMethod);

        // then
        assertEquals(mockObject, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        // given
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[]{1, "test"};
        RealMethod realMethod = mock(RealMethod.class);

        // when
        Invocation invocation = new Invocation(mockObject, mockitoMethod, arguments, 1, realMethod);

        // then
        assertEquals(mockitoMethod, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        // given
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[]{1, "test"};
        RealMethod realMethod = mock(RealMethod.class);

        // when
        Invocation invocation = new Invocation(mockObject, mockitoMethod, arguments, 1, realMethod);

        // then
        assertArrayEquals(arguments, invocation.getArguments());
    }

    @Test
    public void testGetSequenceNumber() {
        // given
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[]{1, "test"};
        RealMethod realMethod = mock(RealMethod.class);

        // when
        Invocation invocation = new Invocation(mockObject, mockitoMethod, arguments, 1, realMethod);

        // then
        assertEquals(1, invocation.getSequenceNumber());
    }

    @Test
    public void testToString() {
        // given
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[]{1, "test"};
        RealMethod realMethod = mock(RealMethod.class);

        // when
        Invocation invocation = new Invocation(mockObject, mockitoMethod, arguments, 1, realMethod);

        // then
        String actual = invocation.toString();
        String expected = getMockName(mockObject) + "." + mockitoMethod.getName() + Arrays.toString(arguments);
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringWithPrintSettings() {
        // given
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[]{1, "test"};
        RealMethod realMethod = mock(RealMethod.class);

        // when
        Invocation invocation = new Invocation(mockObject, mockitoMethod, arguments, 1, realMethod);

        // then
        String actual = invocation.toString(new PrintSettings());
        String expected = getMockName(mockObject) + "." + mockitoMethod.getName() + "(" + Arrays.toString(arguments) + ")";
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        // given
        MockitoMethod mockitoMethod1 = mock(MockitoMethod.class);
        Object[] arguments1 = new Object[]{1, "test1"};
        RealMethod realMethod1 = mock(RealMethod.class);
        MockitoMethod mockitoMethod2 = mock(MockitoMethod.class);
        Object[] arguments2 = new Object[]{1, "test2"};
        RealMethod realMethod2 = mock(RealMethod.class);

        Invocation invocation1 = new Invocation(mockObject, mockitoMethod1, arguments1, 1, realMethod1);
        Invocation invocation2 = new Invocation(mockObject, mockitoMethod2, arguments2, 2, realMethod2);

        // when
        boolean actual = invocation1.equals(invocation2);

        // then
        assertFalse(actual);

        // given same mockitoMethod and different arguments
        Object[] arguments3 = new Object[]{1, "test1"};

        // when
        boolean actual2 = invocation1.equals(new Invocation(mockObject, mockitoMethod1, arguments3, 1, realMethod1));

        // then
        assertTrue(actual2);

        // given same mockitoMethod, arguments and different sequence number

        // when
        boolean actual3 = invocation1.equals(new Invocation(mockObject, mockitoMethod1, arguments1, 2, realMethod1));

        // then
        assertFalse(actual3);
    }

    @Test
    public void testIsVerified() {
        // given
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[]{1, "test"};
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mockObject, mockitoMethod, arguments, 1, realMethod);

        // when
        assertFalse(invocation.isVerified());

        // then
        verify(mockObject).someMethod();

        // when
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testIsVerifiedInOrder() {
        // given
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[]{1, "test1"};
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation1 = new Invocation(mockObject, mockitoMethod, arguments, 1, realMethod);
        Invocation invocation2 = new Invocation(mockObject, mockitoMethod, arguments, 2, realMethod);

        // when
        verify(mockObject).someMethod(anyInt(), anyString());

        // then
        assertFalse(invocation1.isVerifiedInOrder());
        assertFalse(invocation2.isVerifiedInOrder());

        // when
        verify(mockObject).someMethod(eq(1), eq("test1"));

        // then
        assertTrue(invocation1.isVerifiedInOrder());
        assertTrue(invocation2.isVerifiedInOrder());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        // given
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[]{1, "test"};
        RealMethod realMethod = mock(RealMethod.class);

        when(realMethod.invoke(mockObject, arguments)).thenReturn(42);

        Invocation invocation = new Invocation(mockObject, mockitoMethod, arguments, 1, realMethod);

        // when
        int actual = (int) invocation.callRealMethod();

        // then
        assertEquals(42, actual);
    }

    @Test(expected = MockitoException.class)
    public void testInvalidException() {
        // given
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[]{1, "test"};
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mockObject, mockitoMethod, arguments, 1, realMethod);

        // when
        throw new RuntimeException();

        // then
        throw new MockitoException("Unexpected exception thrown: org.mockito.exceptions.base.RuntimeExceptionException: java.lang.RuntimeException: Unexpected exception thrown");
    }

    @Test
    public void testIsToStringMethod() {
        // given
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);

        // when
        boolean actual = Invocation.isToString(mockitoMethod);

        // then
        assertFalse(actual);
    }

    @Test
    public void testIsValidException() {
        // given
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[]{1, "test"};
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mockObject, mockitoMethod, arguments, 1, realMethod);

        // when
        Throwable throwable = new RuntimeException();

        // then
        assertTrue(invocation.isValidException(throwable));

        // given
        Class<?> exceptionType = IllegalArgumentException.class;

        // then
        assertFalse(invocation.isValidException(throwable));

        // given
        when(mockitoMethod.getExceptionTypes()).thenReturn(new Class<?>[]{IllegalArgumentException.class});

        // when
        assertTrue(invocation.isValidException(throwable));
    }

    @Test
    public void testIsValidReturnType() {
        // given
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[]{1, "test"};
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mockObject, mockitoMethod, arguments, 1, realMethod);

        // when
        String expected = "String";
        Class<?> expectedClass = String.class;

        // then
        assertTrue(invocation.isValidReturnType(expectedClass));
        assertEquals(expected, invocation.printMethodReturnType());

        // given
        when(mockitoMethod.getReturnType()).thenReturn(new int[0].getClass());

        // then
        assertFalse(invocation.isValidReturnType(expectedClass));
        assertEquals("int[]", invocation.printMethodReturnType());

        // given
        when(mockitoMethod.getReturnType()).thenReturn(Integer.TYPE);

        // then
        assertTrue(invocation.isValidReturnType(Integer.TYPE));
        assertEquals("int", invocation.printMethodReturnType());
    }

    @Test
    public void testIsVoid() {
        // given
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);

        Invocation invocation = new Invocation(mockObject, mockitoMethod, new Object[0], 1, mock(RealMethod.class));

        // then
        assertFalse(invocation.isVoid());

        // given
        when(mockitoMethod.getReturnType()).thenReturn(Void.TYPE);

        // then
        assertTrue(invocation.isVoid());
    }

    @Test
    public void testMarkVerified() {
        // given
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[]{1, "test"};
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mockObject, mockitoMethod, arguments, 1, realMethod);

        // when
        invocation.markVerified();

        // then
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        // given
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments1 = new Object[]{1, "test1"};
        Object[] arguments2 = new Object[]{1, "test2"};
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation1 = new Invocation(mockObject, mockitoMethod, arguments1, 1, realMethod);
        Invocation invocation2 = new Invocation(mockObject, mockitoMethod, arguments2, 2, realMethod);

        // when
        verify(mockObject).someMethod(anyInt(), anyString());

        // then
        assertFalse(invocation1.isVerifiedInOrder());
        assertFalse(invocation2.isVerifiedInOrder());

        // when
        verify(mockObject).someMethod(eq(1), eq("test1"));
        verify(mockObject).someMethod(eq(1), eq("test2"));

        // then
        assertTrue(invocation1.isVerifiedInOrder());
        assertTrue(invocation2.isVerifiedInOrder());
    }

    @Test
    public void testGetLocation() {
        // given
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[]{1, "test"};
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mockObject, mockitoMethod, arguments, 1, realMethod);

        // when
        Location location = invocation.getLocation();

        // then
        assertNotNull(location);
    }

    @Test
    public void testGetArgumentsCount() {
        // given
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[]{1, "test"};
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mockObject, mockitoMethod, arguments, 1, realMethod);

        // when
        int argumentsCount = invocation.getArgumentsCount();

        // then
        assertEquals(arguments.length, argumentsCount);
    }

    @Test
    public void testGetRawArguments() {
        // given
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[]{1, "test"};
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mockObject, mockitoMethod, arguments, 1, realMethod);

        // when
        Object[] rawArguments = invocation.getRawArguments();

        // then
        assertArrayEquals(arguments, rawArguments);
    }
}