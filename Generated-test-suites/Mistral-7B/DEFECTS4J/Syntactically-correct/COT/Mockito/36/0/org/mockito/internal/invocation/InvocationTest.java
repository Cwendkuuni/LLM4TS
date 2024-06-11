import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.RealMethod;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.exceptions.base.MockitoException;

public class InvocationTest {

    @Test
    public void testConstructor() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(Mockito.class, "testMethod", new Class[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod(mock, mockitoMethod, new Object[0]));

        assertEquals(mock, invocation.getMock());
        assertEquals(mockitoMethod, invocation.getMethod());
        assertArrayEquals(new Object[0], invocation.getArguments());
        assertEquals(1, invocation.getSequenceNumber());
    }

    @Test
    public void testExpandVarArgs() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(Mockito.class, "testMethod", new Class[]{Integer[].class, Integer.class});
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[]{1, new Integer[]{2, 3}}, 1, new RealMethod(mock, mockitoMethod, new Object[]{1, new Integer[]{2, 3}}));

        Object[] expectedArguments = new Object[]{1, 2, 3};
        assertEquals(expectedArguments, invocation.getArguments());
    }

    @Test(expected = MockitoException.class)
    public void testEqualsNull() {
        Object mock = new Object();
        Invocation invocation = new Invocation(mock, new MockitoMethod(Mockito.class, "testMethod", new Class[0]), new Object[0], 1, new RealMethod(mock, new MockitoMethod(Mockito.class, "testMethod", new Class[0]), new Object[0]));
        Invocation other = null;
        assertFalse(invocation.equals(other));
    }

    @Test
    public void testEqualsSelf() {
        Object mock = new Object();
        Invocation invocation = new Invocation(mock, new MockitoMethod(Mockito.class, "testMethod", new Class[0]), new Object[0], 1, new RealMethod(mock, new MockitoMethod(Mockito.class, "testMethod", new Class[0]), new Object[0]));
        assertTrue(invocation.equals(invocation));
    }

    @Test
    public void testEqualsDifferentMock() {
        Object mock1 = new Object();
        Object mock2 = new Object();
        Invocation invocation1 = new Invocation(mock1, new MockitoMethod(Mockito.class, "testMethod", new Class[0]), new Object[0], 1, new RealMethod(mock1, new MockitoMethod(Mockito.class, "testMethod", new Class[0]), new Object[0]));
        Invocation invocation2 = new Invocation(mock2, new MockitoMethod(Mockito.class, "testMethod", new Class[0]), new Object[0], 1, new RealMethod(mock2, new MockitoMethod(Mockito.class, "testMethod", new Class[0]), new Object[0]));
        assertFalse(invocation1.equals(invocation2));
    }

    @Test
    public void testEqualsSameMethodDifferentArguments() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(Mockito.class, "testMethod", new Class[0]);
        Invocation invocation1 = new Invocation(mock, mockitoMethod, new Object[]{1}, 1, new RealMethod(mock, mockitoMethod, new Object[]{1}));
        Invocation invocation2 = new Invocation(mock, mockitoMethod, new Object[]{2}, 2, new RealMethod(mock, mockitoMethod, new Object[]{2}));
        assertFalse(invocation1.equals(invocation2));
    }

    @Test
    public void testToString() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(Mockito.class, "testMethod", new Class[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod(mock, mockitoMethod, new Object[0]));

        List<Matcher> matchers = invocation.argumentsToMatchers();
        assertEquals("testMethod([])", invocation.toString());
        assertEquals("testMethod([])", invocation.toString(new PrintSettings()));

        Object[] arguments = new Object[]{1, 2};
        Invocation invocationWithArguments = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod(mock, mockitoMethod, arguments));
        assertEquals("testMethod([1, 2])", invocationWithArguments.toString());
        assertEquals("testMethod([1, 2])", invocationWithArguments.toString(new PrintSettings()));

        Object[] varArgs = new Object[]{1, 2, 3};
        Invocation invocationWithVarArgs = new Invocation(mock, mockitoMethod, varArgs, 1, new RealMethod(mock, mockitoMethod, varArgs));
        assertEquals("testMethod([1, 2, 3])", invocationWithVarArgs.toString());
        assertEquals("testMethod([1, 2, 3])", invocationWithVarArgs.toString(new PrintSettings()));
    }

    @Test(expected = RuntimeException.class)
    public void testHashCode() {
        Object mock = new Object();
        Invocation invocation = new Invocation(mock, new MockitoMethod(Mockito.class, "testMethod", new Class[0]), new Object[0], 1, new RealMethod(mock, new MockitoMethod(Mockito.class, "testMethod", new Class[0]), new Object[0]));
        invocation.hashCode();
    }

    @Test
    public void testIsToString() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "toString", new Class[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod(mock, mockitoMethod, new Object[0]));
        assertTrue(Invocation.isToString(invocation));
    }

    @Test
    public void testIsValidException() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(Mockito.class, "testMethod", new Class[]{"Exception"});
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod(mock, mockitoMethod, new Object[0]));

        try {
            throw new Exception();
        } catch (Exception e) {
            assertTrue(invocation.isValidException(e));
        }
    }

    @Test
    public void testIsValidReturnTypePrimitive() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(Mockito.class, "testMethod", new Class[]{boolean.class});
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod(mock, mockitoMethod, new Object[0]));
        assertTrue(invocation.isValidReturnType(boolean.class));
    }

    @Test
    public void testIsValidReturnTypeObject() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(Mockito.class, "testMethod", new Class[]{String.class});
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod(mock, mockitoMethod, new Object[0]));
        assertTrue(invocation.isValidReturnType(String.class));
    }

    @Test
    public void testIsVoid() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(Mockito.class, "testMethod", new Class[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod(mock, mockitoMethod, new Object[0]));
        assertFalse(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(Mockito.class, "testMethod", new Class[]{boolean.class});
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod(mock, mockitoMethod, new Object[0]));
        assertEquals("boolean", invocation.printMethodReturnType());
    }

    @Test(expected = MockitoException.class)
    public void testInvokeWithInvalidException() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(Mockito.class, "testMethod", new Class[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod(mock, mockitoMethod, new Object[0]));

        try {
            invocation.callRealMethod();
            fail();
        } catch (MockitoException e) {
            //Expected
        }
    }

    @Test(expected = MockitoException.class)
    public void testInvokeWithInvalidReturnType() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(Mockito.class, "testMethod", new Class[]{String.class});
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod(mock, mockitoMethod, new Object[0]));

        try {
            invocation.callRealMethod();
            fail();
        } catch (MockitoException e) {
            //Expected
        }
    }

    @Test
    public void testInvokeWithValidReturnType() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(Mockito.class, "testMethod", new Class[]{Integer.class});
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod(mock, mockitoMethod, new Object[0]));

        Integer result = (Integer) invocation.callRealMethod();
        assertEquals(1, result.intValue());
    }

    @Test
    public void testMarkVerified() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(Mockito.class, "testMethod", new Class[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod(mock, mockitoMethod, new Object[0]));
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(Mockito.class, "testMethod", new Class[0]);
        Invocation invocation1 = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod(mock, mockitoMethod, new Object[0]));
        Invocation invocation2 = new Invocation(mock, mockitoMethod, new Object[0], 2, new RealMethod(mock, mockitoMethod, new Object[0]));

        invocation1.markVerified();
        invocation1.markVerifiedInOrder();
        assertFalse(invocation1.isVerifiedInOrder());
        assertFalse(invocation2.isVerified());

        invocation2.markVerified();
        invocation1.markVerifiedInOrder();
        assertTrue(invocation1.isVerifiedInOrder());
        assertTrue(invocation2.isVerified());
    }
}