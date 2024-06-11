package org.mockito.internal.invocation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import java.util.Arrays;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;

public class InvocationTest {

    private InvocationOnMock invocation;
    private MockitoMethod mockitoMethod;
    private Object mock;

    @Before
    public void setUp() {
        mock = Mockito.mock(String.class);
        mockitoMethod = Mockito.method(String.class, "length");
        invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object obj, Object[] objects) throws Throwable {
                return ((String) obj).length();
            }
        });
    }

    @Test
    public void testToString_noArguments() {
        String expectedResult = "mock.length()";
        String actualResult = invocation.toString();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testToString_withArguments() {
        Object[] arguments = new Object[]{"abc", 3};
        Invocation invocationWithArgs = new Invocation(mock, mockitoMethod, arguments, 2, new RealMethod() {
            @Override
            public Object invoke(Object obj, Object[] objects) throws Throwable {
                return ((String) obj).length() + Arrays.toString((String[]) objects[0]);
            }
        });

        String expectedResult = "mock.length(\"abc\", 3)";
        String actualResult = invocationWithArgs.toString();
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = RuntimeException.class)
    public void testIsValidException_throws_when_invalid_exception() {
        when(mock.length()).thenThrow(new IllegalArgumentException("Invalid length"));
        invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object obj, Object[] objects) throws Throwable {
                return ((String) obj).length();
            }
        });

        Throwable throwable = new RuntimeException("Unexpected exception");
        assertFalse(invocation.isValidException(throwable));
    }

    @Test
    public void testIsValidException_accepts_expected_exception() {
        when(mock.length()).thenThrow(new IllegalArgumentException("Invalid length"));
        invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object obj, Object[] objects) throws Throwable {
                return ((String) obj).length();
            }
        });

        assertTrue(invocation.isValidException(new IllegalArgumentException("Invalid length")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCallRealMethod_throws_expected_exception() {
        when(mock.length()).thenThrow(new IllegalArgumentException("Invalid length"));
        invocation.markVerified();
        invocation.callRealMethod();
    }

    @Test
    public void testIsVoid() {
        mockitoMethod = Mockito.method(Void.TYPE, "voidMethod");
        invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod() {
            @Override
            public void invoke(Object obj, Object[] objects) throws Throwable {
                // empty method
            }
        });

        assertTrue(invocation.isVoid());
    }

    @Test
    public void testIsVerified() {
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testIsVerifiedInOrder() {
        Invocation invocation2 = new Invocation(mock, mockitoMethod, new Object[0], 2, new RealMethod() {
            @Override
            public Object invoke(Object obj, Object[] objects) throws Throwable {
                return ((String) obj).length();
            }
        });
        invocation.markVerifiedInOrder();
        invocation2.markVerifiedInOrder();

        assertTrue(invocation.isVerifiedInOrder());
        assertTrue(invocation2.isVerifiedInOrder());
        assertTrue(invocation.isVerified());
        assertTrue(invocation2.isVerified());
    }

    @Test(expected = AssertionError.class)
    public void testEquals_different_methods() {
        Object[] args1 = new Object[]{1};
        Object[] args2 = new Object[]{2};
        Invocation invocation1 = new Invocation(mock, mockitoMethod, args1, 1, new RealMethod() {
            @Override
            public Object invoke(Object obj, Object[] objects) throws Throwable {
                return ((String) obj).length();
            }
        });
        Invocation invocation2 = new Invocation(mock, Mockito.method(Integer.TYPE, "toString"), args2, 2, new RealMethod() {
            @Override
            public Object invoke(Object obj, Object[] objects) throws Throwable {
                return ((Integer) obj).toString();
            }
        });

        assertFalse(invocation1.equals(invocation2));
    }

    @Test
    public void testEquals_same_arguments() {
        Object[] args1 = new Object[]{1};
        Object[] args2 = new Object[]{1};
        Invocation invocation1 = new Invocation(mock, mockitoMethod, args1, 1, new RealMethod() {
            @Override
            public Object invoke(Object obj, Object[] objects) throws Throwable {
                return ((String) obj).length();
            }
        });
        Invocation invocation2 = new Invocation(mock, mockitoMethod, args2, 2, new RealMethod() {
            @Override
            public Object invoke(Object obj, Object[] objects) throws Throwable {
                return ((String) obj).length();
            }
        });

        assertTrue(invocation1.equals(invocation2));
    }
}