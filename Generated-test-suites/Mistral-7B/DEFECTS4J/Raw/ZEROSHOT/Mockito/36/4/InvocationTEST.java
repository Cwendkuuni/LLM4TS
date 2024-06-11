package org.mockito.tests.internal.invocation;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.BaseMockitoException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockitoMock;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;
import org.mockito.internal.util.PrintSettings;
import org.mockito.internal.util.PrintingFriendlyInvocation;
import org.mockito.internal.util.Printables;
import org.mockito.internal.util.MockitoMethod;
import org.mockito.internal.util.MatchersPrinter;
import org.mockito.internal.util.ArrayEquals;

import java.lang.reflect.Method;
import java.util.Arrays;

public class InvocationTest {

    @Test
    public void testCreateInvocationWithNoArguments() {
        // given
        MockitoMock mock = new MockitoMock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod(Object.class, "someMethod", new Class[0]);
        RealMethod realMethod = new RealMethod(mock, mockitoMethod, null);
        int sequenceNumber = 1;

        // when
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], sequenceNumber, realMethod);

        // then
        Assert.assertEquals(mock, invocation.getMock());
        Assert.assertEquals(mockitoMethod, invocation.getMethod());
        Assert.assertArrayEquals(new Object[0], invocation.getArguments());
        Assert.assertEquals(sequenceNumber, invocation.getSequenceNumber());
        Assert.assertFalse(invocation.isVerified());
        Assert.assertFalse(invocation.isVerifiedInOrder());
        Assert.assertNull(invocation.location);
        Assert.assertEquals(1, invocation.getArgumentsCount());
        Assert.assertNull(invocation.getRawArguments());
        Assert.assertFalse(invocation.isVoid());
        Assert.assertEquals("someMethod()", invocation.printMethodReturnType());
        Assert.assertEquals(0, invocation.getLocation().getLineNumber());
        Assert.assertFalse(invocation.equals(null));
        Assert.assertFalse(invocation.equals(""));
        Assert.assertTrue(invocation.equals(invocation));
        Assert.assertTrue(invocation.isValidReturnType(int.class));
        Assert.assertFalse(invocation.isValidReturnType(String.class));
        Assert.assertFalse(invocation.isValidException(new RuntimeException("Test exception")));
        Assert.assertFalse(invocation.isValidException(new IllegalArgumentException("Test exception")));
        Assert.assertEquals("MockitoMock$1.someMethod()", invocation.toString(new PrintSettings()));
        Assert.assertTrue(invocation.callRealMethod().equals(mockitoMethod.getReturnValue()));
    }

    @Test
    public void testCreateInvocationWithSingleArgument() {
        // given
        MockitoMock mock = new MockitoMock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod(Object.class, "someMethod", new Class[]{Integer.class});
        Object arg = 1;
        RealMethod realMethod = new RealMethod(mock, mockitoMethod, arg);
        int sequenceNumber = 1;

        // when
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[]{arg}, sequenceNumber, realMethod);

        // then
        Assert.assertEquals(mock, invocation.getMock());
        Assert.assertEquals(mockitoMethod, invocation.getMethod());
        Assert.assertArrayEquals(new Object[]{arg}, invocation.getArguments());
        Assert.assertEquals(sequenceNumber, invocation.getSequenceNumber());
        Assert.assertFalse(invocation.isVerified());
        Assert.assertFalse(invocation.isVerifiedInOrder());
        Assert.assertNull(invocation.location);
        Assert.assertEquals(1, invocation.getArgumentsCount());
        Assert.assertArrayEquals(new Object[]{arg}, invocation.getRawArguments());
        Assert.assertFalse(invocation.isVoid());
        Assert.assertEquals("someMethod(int)", invocation.printMethodReturnType());
        Assert.assertEquals(0, invocation.getLocation().getLineNumber());
        Assert.assertFalse(invocation.equals(null));
        Assert.assertFalse(invocation.equals(""));
        Assert.assertTrue(invocation.equals(invocation));
        Assert.assertTrue(invocation.isValidReturnType(int.class));
        Assert.assertFalse(invocation.isValidReturnType(String.class));
        Assert.assertFalse(invocation.isValidException(new RuntimeException("Test exception")));
        Assert.assertFalse(invocation.isValidException(new IllegalArgumentException("Test exception")));
        Assert.assertEquals("MockitoMock$1.someMethod(1)", invocation.toString(new PrintSettings()));
        Assert.assertTrue(invocation.callRealMethod().equals(mockitoMethod.getReturnValue()));
    }

    @Test
    public void testCreateInvocationWithVarArgs() {
        // given
        MockitoMock mock = new MockitoMock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod(Object.class, "someMethod", new Class[]{Integer[].class});
        Object arg1 = 1;
        Object arg2 = 2;
        Object[] varArgs = new Object[]{arg1, arg2};
        RealMethod realMethod = new RealMethod(mock, mockitoMethod, varArgs);
        int sequenceNumber = 1;

        // when
        Invocation invocation = new Invocation(mock, mockitoMethod, varArgs, sequenceNumber, realMethod);

        // then
        Assert.assertEquals(mock, invocation.getMock());
        Assert.assertEquals(mockitoMethod, invocation.getMethod());
        Assert.assertArrayEquals(varArgs, invocation.getArguments());
        Assert.assertEquals(sequenceNumber, invocation.getSequenceNumber());
        Assert.assertFalse(invocation.isVerified());
        Assert.assertFalse(invocation.isVerifiedInOrder());
        Assert.assertNull(invocation.location);
        Assert.assertEquals(2, invocation.getArgumentsCount());
        Assert.assertArrayEquals(varArgs, invocation.getRawArguments());
        Assert.assertFalse(invocation.isVoid());
        Assert.assertEquals("someMethod([I)", invocation.printMethodReturnType());
        Assert.assertEquals(0, invocation.getLocation().getLineNumber());
        Assert.assertFalse(invocation.equals(null));
        Assert.assertFalse(invocation.equals(""));
        Assert.assertTrue(invocation.equals(invocation));
        Assert.assertTrue(invocation.isValidReturnType(int[].class));
        Assert.assertFalse(invocation.isValidReturnType(String.class));
        Assert.assertFalse(invocation.isValidException(new RuntimeException("Test exception")));
        Assert.assertFalse(invocation.isValidException(new IllegalArgumentException("Test exception")));
        Assert.assertEquals("MockitoMock$1.someMethod([1, 2])", invocation.toString(new PrintSettings()));
        Assert.assertTrue(invocation.callRealMethod().equals(mockitoMethod.getReturnValue()));
    }

    @Test(expected = RuntimeException.class)
    public void testCreateInvocationWithInvalidReturnType() {
        // given
        MockitoMock mock = new MockitoMock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod(Object.class, "someMethod", new Class[]{});
        Object[] rawArguments = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod(mock, mockitoMethod, rawArguments);

        // when, then
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], sequenceNumber, realMethod);
        Assert.assertThrows(RuntimeException.class, () -> invocation.isValidReturnType(String.class));
    }

    @Test
    public void testIsToStringMethod() {
        // given
        MockitoMethod mockitoMethod = new MockitoMethod(Object.class, "toString", new Class[0]);

        // when
        boolean isToStringMethod = Invocation.isToStringMethod(mockitoMethod);

        // then
        Assert.assertTrue(isToStringMethod);
    }

    @Test
    public void testIsValidExceptionType() {
        // given
        MockitoMethod mockitoMethod = new MockitoMethod(Object.class, "someMethod", new Class[]{});
        Throwable throwable = new RuntimeException("Test exception");

        // when
        boolean isValidExceptionType = Invocation.isValidExceptionType(mockitoMethod, throwable);

        // then
        Assert.assertFalse(isValidExceptionType);
    }

    @Test
    public void testIsValidExceptionTypeWithMatchedExceptionType() {
        // given
        MockitoMethod mockitoMethod = new MockitoMethod(Object.class, "someMethod", new Class[]{});
        Throwable throwable = new IllegalArgumentException("Test exception");

        // when
        boolean isValidExceptionType = Invocation.isValidExceptionType(mockitoMethod, throwable);

        // then
        Assert.assertTrue(isValidExceptionType);
    }

    @Test(expected = BaseMockitoException.class)
    public void testCallRealMethodWithMismatchedReturnType() throws Throwable {
        // given
        MockitoMock mock = new MockitoMock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod(Object.class, "someMethod", new Class[]{Integer.class});
        Object arg = 1;
        RealMethod realMethod = new RealMethod(mock, mockitoMethod, arg);
        int sequenceNumber = 1;
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[]{arg}, sequenceNumber, realMethod);
        Class<String> clazz = String.class;

        // when, then
        Assert.assertThrows(BaseMockitoException.class, () -> invocation.callRealMethod().getClass().equals(clazz));
    }

    @Test
    public void testCallRealMethodWithMatchedReturnType() throws Throwable {
        // given
        MockitoMock mock = new MockitoMock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod(Object.class, "someMethod", new Class[]{Integer.class});
        Object arg = 1;
        RealMethod realMethod = new RealMethod(mock, mockitoMethod, arg);
        int sequenceNumber = 1;
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[]{arg}, sequenceNumber, realMethod);
        Class<Integer> clazz = Integer.class;

        // when
        Object result = invocation.callRealMethod();

        // then
        Assert.assertEquals(mockitoMethod.getReturnValue(), result);
        Assert.assertEquals(clazz, result.getClass());
    }

    @Test
    public void testPrintableInvocation() {
        // given
        MockitoMock mock = new MockitoMock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod(Object.class, "someMethod", new Class[]{Integer.class});
        Object arg = 1;
        RealMethod realMethod = new RealMethod(mock, mockitoMethod, arg);
        int sequenceNumber = 1;
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[]{arg}, sequenceNumber, realMethod);

        // when
        String printableInvocation = invocation.print(new PrintSettings());

        // then
        Assert.assertNotNull(printableInvocation);
        Assert.assertEquals("someMethod(1)", printableInvocation);
    }

    @Test
    public void testPrintingFriendlyInvocation() {
        // given
        MockitoMock mock = new MockitoMock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod(Object.class, "someMethod", new Class[]{Integer.class});
        Object arg = 1;
        RealMethod realMethod = new RealMethod(mock, mockitoMethod, arg);
        int sequenceNumber = 1;
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[]{arg}, sequenceNumber, realMethod);

        // when
        String printingFriendlyInvocation = invocation.printingFriendly();

        // then
        Assert.assertEquals("Invocation on mock: someMethod(1)", printingFriendlyInvocation);
    }
}