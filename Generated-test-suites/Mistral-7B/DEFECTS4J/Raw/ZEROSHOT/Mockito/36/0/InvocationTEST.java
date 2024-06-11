package org.mockito.tests.internal.invocation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.RealMethod;
import org.mockito.internal.invocation.mockitomethod.MockitoMethod;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;
import org.mockito.internal.util.PrintSettings;
import org.mockito.internal.util.PrintingFriendlyInvocation;
import org.mockito.internal.util.PrintingFriendlyInvocationFactory;
import org.mockito.internal.util.StubbingContext;
import org.mockito.internal.util.StubbingContextWithSettings;
import org.mockito.internal.util.StubbingContextWithSettingsAndVarargs;
import org.mockito.internal.util.StubbingContextWithSettingsAndVarargsAndArray;
import org.mockito.internal.util.StubbingContextWithSettingsAndVarargsAndArrayAndException;
import org.mockito.internal.util.StubbingContextWithSettingsAndVarargsAndArrayAndExceptionAndReturnType;
import org.mockito.internal.util.StubbingContextWithSettingsAndVarargsAndArrayAndExceptionAndReturnTypeAndArgMatcher;
import org.mockito.internal.util.StubbingContextWithSettingsAndVarargsAndArrayAndExceptionAndReturnTypeAndArgMatcherAndPrintSettings;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.array;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isInstanceOf;
import static org.mockito.Mockito.*;

public class InvocationTest {

    private static final int MAX_LINE_LENGTH = 45;
    private static final PrintSettings DEFAULT_PRINT_SETTINGS = new PrintSettings();
    private static final PrintSettings MULTI_LINE_PRINT_SETTINGS = new PrintSettings(true);

    private Invocation invocation;
    private Object mock;
    private MockitoMethod method;
    private RealMethod realMethod;
    private Object[] arguments;
    private Object[] rawArguments;
    private Location location;

    @Before
    public void setUp() {
        mock = mock(Object.class);
        method = mockMethod(Object.class, "testMethod", int.class, int.class, int[].class, Exception.class);
        realMethod = new RealMethod(method, new Object[]{}, new Class[]{int[].class});
        arguments = new Object[]{1, 2, new int[]{1, 2, 3}};
        rawArguments = new Object[]{1, 2, new int[]{1, 2, 3}};
        invocation = new Invocation(mock, method, arguments, 1, realMethod);
        location = new Location();
    }

    @Test
    public void testGetMock() {
        assertEquals(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        assertEquals(method, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        assertArrayEquals(arguments, invocation.getArguments());
    }

    @Test
    public void testGetRawArguments() {
        assertArrayEquals(rawArguments, invocation.getRawArguments());
    }

    @Test
    public void testGetSequenceNumber() {
        assertEquals(1, invocation.getSequenceNumber());
    }

    @Test
    public void testIsVerified() {
        assertFalse(invocation.isVerified());
    }

    @Test
    public void testIsVerifiedInOrder() {
        assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testToString() {
        String expected = "testMethod(int int, int[] int[])";
        assertEquals(expected, invocation.toString());
    }

    @Test
    public void testToStringWithPrintSettings() {
        String expected = "testMethod(int int, int[] int[])\narguments:\nint int = 1\nint[] int[] = {1, 2, 3}";
        assertEquals(expected, invocation.toString(DEFAULT_PRINT_SETTINGS));
    }

    @Test
    public void testToStringWithMultilinePrintSettings() {
        String expected = "testMethod(int int, int[] int[])\narguments:\nint int = 1\nint[] int[] = {1, 2, 3}\n";
        assertEquals(expected, invocation.toString(MULTI_LINE_PRINT_SETTINGS));
    }

    @Test
    public void testIsEquals() {
        Invocation invocation1 = new Invocation(mock, method, arguments, 1, realMethod);
        Invocation invocation2 = new Invocation(mock, method, arguments, 1, realMethod);

        assertTrue(invocation1.equals(invocation2));
        assertTrue(invocation2.equals(invocation1));
    }

    @Test
    public void testIsNotEqualsWithDifferentArguments() {
        Object[] arguments1 = new Object[]{1, 2, new int[]{1, 2, 3}};
        Object[] arguments2 = new Object[]{1, 2, new int[]{1, 2, 4}};

        Invocation invocation1 = new Invocation(mock, method, arguments1, 1, realMethod);
        Invocation invocation2 = new Invocation(mock, method, arguments2, 1, realMethod);

        assertFalse(invocation1.equals(invocation2));
        assertFalse(invocation2.equals(invocation1));
    }

    @Test
    public void testIsNotEqualsWithDifferentMocks() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        Invocation invocation1 = new Invocation(mock1, method, arguments, 1, realMethod);
        Invocation invocation2 = new Invocation(mock2, method, arguments, 1, realMethod);

        assertFalse(invocation1.equals(invocation2));
        assertFalse(invocation2.equals(invocation1));
    }

    @Test
    public void testIsNotEqualsWithDifferentMethods() {
        MockitoMethod method1 = mockMethod(Object.class, "testMethod1", int.class, int.class);
        MockitoMethod method2 = mockMethod(Object.class, "testMethod2", int.class, int.class);

        Invocation invocation1 = new Invocation(mock, method1, arguments, 1, realMethod);
        Invocation invocation2 = new Invocation(mock, method2, arguments, 1, realMethod);

        assertFalse(invocation1.equals(invocation2));
        assertFalse(invocation2.equals(invocation1));
    }

    @Test
    public void testHashCode() {
        Invocation invocation1 = new Invocation(mock, method, arguments, 1, realMethod);
        Invocation invocation2 = new Invocation(mock, method, arguments, 1, realMethod);

        int hashCode1 = invocation1.hashCode();
        int hashCode2 = invocation2.hashCode();

        assertNotSame(hashCode1, hashCode2);
    }

    @Test
    public void testIsVerifiedAfterVerification() {
        verify(mock).testMethod(anyInt(), anyIntArray(), any(Exception.class));
        assertTrue(invocation.isVerified());
    }

    @Test(expected = MockitoException.class)
    public void testIsVerifiedBeforeVerification() {
        assertFalse(invocation.isVerified());
        verify(mock).testMethod(anyInt(), anyIntArray(), any(Exception.class));
    }

    @Test
    public void testMarkVerified() {
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerified());
        assertTrue(invocation.isVerifiedInOrder());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        int expected = 5;
        when(mock.testMethod(anyInt(), anyIntArray(), isA(Exception.class))).thenReturn(expected);

        int result = invocation.callRealMethod();

        assertEquals(expected, result);
    }

    @Test(expected = MockitoException.class)
    public void testCallRealMethodWithUnmatchedExceptionType() throws Throwable {
        when(mock.testMethod(anyInt(), anyIntArray())).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> invocation.callRealMethod());
    }

    @Test
    public void testIsValidException() {
        Class<?> exceptionType = Exception.class;

        assertTrue(invocation.isValidException(new Exception(exceptionType)));
        assertTrue(invocation.isValidException(new Exception(exceptionType, "message")));
        assertTrue(invocation.isValidException(new RuntimeException(exceptionType)));
        assertTrue(invocation.isValidException(new RuntimeException(exceptionType, "message")));
        assertFalse(invocation.isValidException(new IllegalArgumentException()));
        assertFalse(invocation.isValidException(new IllegalStateException()));
        assertFalse(invocation.isValidException(new AssertionError()));
    }

    @Test
    public void testIsValidReturnType() {
        Class<Integer> intClass = Integer.class;

        assertTrue(invocation.isValidReturnType(intClass));
        assertTrue(invocation.isValidReturnType(int[].class));
        assertFalse(invocation.isValidReturnType(String.class));
        assertFalse(invocation.isValidReturnType(Double.TYPE));
    }

    @Test
    public void testIsVoid() {
        MockitoMethod voidMethod = mockMethod(Object.class, "voidMethod", Void.TYPE);

        assertTrue(new Invocation(mock, voidMethod, null, 1, new RealMethod(voidMethod, null, null)).isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        assertEquals("Integer", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        assertEquals("testMethod", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        assertTrue(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        assertEquals(location, invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        assertEquals(3, invocation.getArgumentsCount());
    }

    @Test
    public void testToStringWithPrintingFriendlyInvocation() {
        PrintingFriendlyInvocation printingFriendlyInvocation = PrintingFriendlyInvocationFactory.createPrintingFriendlyInvocation(invocation);
        String expected = new MockUtil().getMockName(mock) + ".testMethod(int, int[], Exception)";

        assertEquals(expected, printingFriendlyInvocation.toString());
    }

    @Test
    public void testToStringWithStubbingContext() {
        StubbingContext stubbingContext = new StubbingContextWithSettingsAndVarargsAndArrayAndExceptionAndReturnTypeAndArgMatcher(mock, method, arguments, MULTI_LINE_PRINT_SETTINGS, new Object[]{1}, new int[]{1}, new Exception("message"), new Integer(5), new Equals(new Integer(5)));
        String expected = "testMethod(int int, int[] int[], Exception)\narguments:\nint int = 1\nint[] int[] = {1}\nException = org.mockito.exceptions.base.DefaultException:\nmessage = message\nreturn: 5";

        assertEquals(expected, invocation.toString(stubbingContext));
    }

    @Test
    public void testToStringWithStubbingContextWithSettingsAndVarargsAndArrayAndExceptionAndReturnType() {
        StubbingContext stubbingContext = new StubbingContextWithSettingsAndVarargsAndArrayAndExceptionAndReturnTypeAndArgMatcher(mock, method, arguments, MULTI_LINE_PRINT_SETTINGS, new Object[]{1}, new int[]{1}, new Exception("message"), new Integer(5), new Equals(new Integer(5)));
        String expected = "testMethod(int int, int[] int[], Exception)\narguments:\nint int = 1\nint[] int[] = {1}\nException = org.mockito.exceptions.base.DefaultException:\nmessage = message\nreturn: 5";

        assertEquals(expected, invocation.toString(stubbingContext));
    }

    @Test
    public void testToStringWithStubbingContextWithSettingsAndVarargsAndArrayAndExceptionAndReturnTypeAndArgMatcherAndPrintSettings() {
        StubbingContextWithSettingsAndVarargsAndArrayAndExceptionAndReturnTypeAndArgMatcherAndPrintSettings stubbingContext = new StubbingContextWithSettingsAndVarargsAndArrayAndExceptionAndReturnTypeAndArgMatcherAndPrintSettings(mock, method, arguments, MULTI_LINE_PRINT_SETTINGS, new Object[]{1}, new int[]{1}, new Exception("message"), new Integer(5), new Equals(new Integer(5)), DEFAULT_PRINT_SETTINGS);
        String expected = "testMethod(int int, int[] int[], Exception)\narguments:\nint int = 1\nint[] int[] = {1}\nException = org.mockito.exceptions.base.DefaultException:\nmessage = message\nreturn: 5";

        assertEquals(expected, invocation.toString(stubbingContext));
    }

    private Method mockMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        try {
            return clazz.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}