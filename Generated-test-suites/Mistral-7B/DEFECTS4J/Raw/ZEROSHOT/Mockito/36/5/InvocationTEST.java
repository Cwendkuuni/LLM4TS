package org.mockito.internal.invocation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.array;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.matchers.ArgumentsMatchers.nullValue;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.matchers.ArgumentsMatchers;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;
import org.mockito.internal.util.PrintSettings;
import org.mockito.internal.util.PrintingFriendlyInvocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Stubber;

/**
 * Test class for {@link Invocation} class.
 */
public class InvocationTest {

    private Invocation invocation;
    private Object mock;
    private MockitoMethod mockitoMethod;
    private RealMethod realMethod;
    private Location location;

    @Before
    public void setUp() throws Exception {
        mock = mock(Object.class);
        mockitoMethod = mockMethodWithReturnType(String.class, "testMethod", new Class[0]);
        realMethod = mockMethodWithReturnType(String.class, "testMethod", new Class[0]);
        invocation = new Invocation(mock, mockitoMethod, new Object[0], 0, realMethod);
        location = new Location();
    }

    @Test
    public void testGetMock() {
        assertSame(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        assertSame(mockitoMethod, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        Object[] arguments = new Object[0];
        Invocation invocationWithArguments = new Invocation(mock, mockitoMethod, arguments, 0, realMethod);
        assertArrayEquals(arguments, invocationWithArguments.getArguments());
    }

    @Test
    public void testEquals() {
        Object mock2 = mock(Object.class);
        MockitoMethod mockitoMethod2 = mockMethodWithReturnType(String.class, "testMethod", new Class[0]);
        Invocation invocation2 = new Invocation(mock2, mockitoMethod2, new Object[0], 0, realMethod);

        assertTrue(invocation.equals(invocation));
        assertTrue(invocation.equals(invocation));
        assertFalse(invocation.equals(null));
        assertFalse(invocation.equals(""));
        assertFalse(invocation.equals(new Invocation(mock, mockitoMethod, new Object[0], 1, realMethod)));
        assertFalse(invocation.equals(invocation2));

        Invocation invocationWithSameMockAndMethod = new Invocation(mock, mockitoMethod, new Object[0], 0, realMethod);
        assertTrue(invocation.equals(invocationWithSameMockAndMethod));
        assertTrue(invocationWithSameMockAndMethod.equals(invocation));
    }

    @Test
    public void testHashCode() {
        // test is not implemented
        throw new RuntimeException("hashCode() is not implemented");
    }

    @Test(expected = RuntimeException.class)
    public void testToString() {
        invocation.toString();
    }

    @Test
    public void testToStringWithArgumentsMatchers() {
        List<Matcher> matchers = Arrays.asList(eq("expected"), any(Integer.class));
        String expectedToString = "testMethod(expected, <any int>)";
        assertEquals(expectedToString, invocation.toString(matchers, new PrintSettings(false)));
    }

    @Test
    public void testToStringWithPrintSettings() {
        String expectedToString = "testMethod()";
        PrintSettings printSettings = new PrintSettings(true);
        String actualToString = invocation.toString(printSettings);
        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testIsVerified() {
        assertFalse(invocation.isVerified());
    }

    @Test
    public void testIsVerifiedAfterVerification() {
        verify(mock).testMethod();
        assertTrue(invocation.isVerified());
    }

    @Test(expected = AssertionError.class)
    public void testIsVerifiedAfterNotVerifiedMethodCall() {
        verifyNoMoreInteractions(mock);
        verify(mock).testMethod();
    }

    @Test
    public void testIsVerifiedInOrder() {
        Invocation invocation1 = new Invocation(mock, mockitoMethod, new Object[0], 1, realMethod);
        Invocation invocation2 = new Invocation(mock, mockitoMethod, new Object[0], 2, realMethod);

        invocation1.markVerifiedInOrder();
        verify(mock).testMethod();
        assertFalse(invocation2.isVerifiedInOrder());
    }

    @Test
    public void testIsVerifiedInOrderAfterMultipleVerifications() {
        Invocation invocation1 = new Invocation(mock, mockitoMethod, new Object[0], 1, realMethod);
        Invocation invocation2 = new Invocation(mock, mockitoMethod, new Object[0], 2, realMethod);
        Invocation invocation3 = new Invocation(mock, mockitoMethod, new Object[0], 3, realMethod);

        invocation1.markVerifiedInOrder();
        invocation1.markVerifiedInOrder();
        invocation2.markVerifiedInOrder();
        invocation3.markVerifiedInOrder();

        verify(mock, inOrder(invocation1, invocation2, invocation3)).testMethod();
    }

    @Test
    public void testIsToString() {
        assertTrue(Invocation.isToString(mockitoMethod));
    }

    @Test
    public void testIsValidException() {
        Throwable exception = new RuntimeException("expected exception");
        assertTrue(invocation.isValidException(exception));
    }

    @Test
    public void testIsValidReturnType() {
        assertTrue(invocation.isValidReturnType(String.class));
    }

    @Test
    public void testIsVoid() {
        assertFalse(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        assertEquals("String", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        assertEquals("testMethod", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        assertFalse(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        assertEquals(location, invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        assertEquals(0, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        Object[] rawArguments = new Object[0];
        Invocation invocationWithRawArguments = new Invocation(mock, mockitoMethod, rawArguments, 0, realMethod);
        assertArrayEquals(rawArguments, invocationWithRawArguments.getRawArguments());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        String result = (String) invocation.callRealMethod();
        assertEquals("", result);
    }

    @Test(expected = MockitoException.class)
    public void testCallRealMethodWithVerifiedException() {
        verify(mock).testMethod();
        invocation.callRealMethod();
    }

    @Test(expected = MockitoException.class)
    public void testCallRealMethodWithNotVerifiedException() {
        verifyNoInteractions(mock);
        invocation.callRealMethod();
    }

    @Test
    public void testToStringWithNullArguments() {
        List<Matcher> matchers = Arrays.asList(nullValue());
        String expectedToString = "testMethod()";
        assertEquals(expectedToString, invocation.toString(matchers, new PrintSettings(false)));
    }

    @Test(expected = RuntimeException.class)
    public void testToStringWithInvalidPrintSettings() {
        invocation.toString(null);
    }

    @Test(expected = RuntimeException.class)
    public void testToStringWithInvalidPrintSettingsNull() {
        invocation.toString(null, null);
    }

    @Test
    public void testToStringWithEmptyPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        String expectedToString = "testMethod()";
        assertEquals(expectedToString, invocation.toString(printSettings));
    }

    private MockitoMethod mockMethodWithReturnType(Class<?> returnType, String name, Class<?>... parameterTypes)
            throws NoSuchMethodException {
        Method method = mock(Method.class);
        when(method.getName()).thenReturn(name);
        when(method.getReturnType()).thenReturn(returnType);
        when(method.getParameterTypes()).thenReturn(parameterTypes);
        return spy(new MockitoMethod(method));
    }
}