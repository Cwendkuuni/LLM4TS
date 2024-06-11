package org.mockito.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentsMatcher;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.internal.invocation.RealMethod;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.array;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isInstanceOf;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InvocationTest {

    @Test
    public void testInvocation_shouldCreateNewInstance() {
        // given
        Object mock = Mockito.mock(String.class);
        Method method = Mockito.methodCalled("someMethod");
        Object[] args = new Object[]{1, "test"};

        // when
        Invocation invocation = new Invocation(mock, method, args, 1, new RealMethod(mock, method));

        // then
        assertEquals(mock, invocation.getMock());
        assertEquals(method, invocation.getMethod());
        assertEquals(args, invocation.getArguments());
        assertEquals(1, invocation.getSequenceNumber());
        assertEquals(method.getName(), invocation.getMethodName());
        assertTrue(invocation.isVerifiedInOrder());
        assertTrue(invocation.isVerified());
        assertFalse(invocation.isVoid());
        assertNull(invocation.getRawArguments());
        assertEquals(Arrays.toString(args), Arrays.toString(invocation.getArguments()));
        assertNotNull(invocation.getLocation());
        assertEquals(1, invocation.getArgumentsCount());
        assertFalse(invocation.isValidException(new RuntimeException()));
        assertFalse(invocation.isValidReturnType(int[].class));
        assertFalse(invocation.isValidReturnType(String.class));
        assertTrue(invocation.isValidReturnType(Integer.class));
        assertEquals("Integer", invocation.printMethodReturnType());
        assertEquals("mock.someMethod(1, test)", invocation.toString());
        assertEquals("mock.someMethod(1, test)", invocation.toString(new PrintSettings()));
        assertEquals("mock.someMethod(1, test)", invocation.toString(Arrays.<Matcher>asList(is(1)), new PrintSettings()));
        assertEquals("mock.someMethod(1, test)", invocation.toString(Arrays.<Matcher>asList(is(1), is("test")), new PrintSettings()));
        assertEquals("mock.someMethod(1, test)", invocation.toString(Arrays.<Matcher>asList(argThat(isA(Number.class)), argThat(isInstanceOf(String.class))), new PrintSettings()));
        assertEquals("mock.someMethod([1, test])", invocation.toString(Arrays.<Matcher>asList(array(hasItems(1), hasItems("test"))), new PrintSettings()));
    }

    @Test
    public void testInvocation_shouldVerifyMethods() {
        // given
        Object mock = Mockito.mock(String.class);
        Method method = Mockito.methodCalled("someMethod");
        Object[] args = new Object[]{1, "test"};

        // when
        Invocation invocation = new Invocation(mock, method, args, 1, new RealMethod(mock, method));
        invocation.markVerified();

        // then
        assertTrue(invocation.isVerified());
        assertFalse(invocation.isVerifiedInOrder());
        verify(mock).someMethod(1, "test");
    }

    @Test
    public void testInvocation_shouldVerifyMethodsInOrder() {
        // given
        Object mock = Mockito.mock(String.class);
        Method method = Mockito.methodCalled("someMethod");
        Object[] args = new Object[]{1, "test1"};
        Object[] args2 = new Object[]{1, "test2"};

        // when
        Invocation invocation1 = new Invocation(mock, method, args, 1, new RealMethod(mock, method));
        invocation1.markVerified();

        Invocation invocation2 = new Invocation(mock, method, args2, 2, new RealMethod(mock, method));
        invocation2.markVerifiedInOrder();

        // then
        assertTrue(invocation1.isVerified());
        assertFalse(invocation1.isVerifiedInOrder());
        assertTrue(invocation2.isVerified());
        assertTrue(invocation2.isVerifiedInOrder());
        verify(mock).someMethod(1, "test1");
        verify(mock).someMethod(1, "test2");
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testInvocation_shouldInvokeRealMethod() throws Exception {
        // given
        Object mock = Mockito.mock(String.class);
        Method method = Mockito.methodCalled("someMethod");
        Object[] args = new Object[]{1, "test"};

        // when
        Invocation invocation = new Invocation(mock, method, args, 1, new RealMethod(mock, method));
        String result = (String) invocation.callRealMethod();

        // then
        assertNotNull(result);
    }
}