package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.invoke;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_methodReturnsNotNull() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Answer<Object> answer = returnsSmartNulls;
        Object mock = mock(String.class);
        String result = (String) answer.answer(new InvocationOnMock(mock, new MethodStub(), new Object[0], 0));
        assertNotNull(result);
    }

    @Test
    public void testAnswer_methodReturnsNullAndIsMockable() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Answer<Object> answer = returnsSmartNulls;
        Object mock = mock(String.class);
        String result = (String) answer.answer(new InvocationOnMock(mock, new MethodStub(), new Object[0], 0));
        assertNull(result);

        verify(mock).answer(new InvocationOnMock(mock, new MethodStub(), new Object[0], 0));
    }

    @Test(expected = org.mockito.exceptions.SmartNullPointerException.class)
    public void testAnswer_methodReturnsNullAndIsNotMockable() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Answer<Object> answer = returnsSmartNulls;
        Object nonMockableType = new Object() {
            @Override
            public String toString() {
                return "NonMockableType";
            }
        };
        answer.answer(new InvocationOnMock(spy(nonMockableType), new MethodStub(), new Object[0], 0));
    }

    @Test
    public void testAnswer_methodThrowsException() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Answer<Object> answer = returnsSmartNulls;
        Object mock = mock(String.class);
        try {
            answer.answer(new InvocationOnMock(mock, new MethodStub() {
                @Override
                public Object invoke(Object obj, Object[] args) {
                    throw new RuntimeException("Test Exception");
                }
            }, new Object[0], 0));
            fail("Method should have thrown an exception");
        } catch (RuntimeException e) {
            // expected
        }
    }

    @Test
    public void testThrowingInterceptor_methodReturnsToString() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = new InvocationOnMock(null, new MethodStub() {
            @Override
            public String toString() {
                return "toString()";
            }
        }, new Object[0], 0);
        Object result = returnsSmartNulls.answer(invocation);
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result.toString());
    }

    @Test(expected = org.mockito.exceptions.SmartNullPointerException.class)
    public void testThrowingInterceptor_methodReturnsNull() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = new InvocationOnMock(null, new MethodStub() {
            @Override
            public Object invoke(Object obj, Object[] args) {
                return null;
            }
        }, new Object[0], 0);
        returnsSmartNulls.answer(invocation);
    }

    private static class MethodStub implements Method {
        @Override
        public Class<?>[] getParameterTypes() {
            return new Class[0];
        }

        @Override
        public int getModifiers() {
            return 0;
        }

        @Override
        public String getName() {
            return "";
        }

        @Override
        public Class<?> getReturnType() {
            return Object.class;
        }

        @Override
        public Annotation[][] getParameterAnnotations() {
            return new Annotation[0][];
        }

        @Override
        public Annotation[][] getDeclaredAnnotations() {
            return new Annotation[0][];
        }

        @Override
        public Type[] getGenericParameterTypes() {
            return new Type[0];
        }

        @Override
        public Type getGenericReturnType() {
            return null;
        }

        @Override
        public int getAnnotationModifiers() {
            return 0;
        }

        @Override
        public Annotation getAnnotation(Class<?> annotationType) {
            return null;
        }

        @Override
        public boolean isAnnotationPresent(Class<?> annotationType) {
            return false;
        }

        @Override
        public boolean isSynthetic() {
            return false;
        }
    }
}