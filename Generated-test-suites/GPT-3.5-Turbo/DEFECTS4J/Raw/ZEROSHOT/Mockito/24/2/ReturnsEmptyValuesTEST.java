import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;

import java.util.*;

public class ReturnsEmptyValuesTest {

    ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

    @Test
    public void testAnswerToString() {
        String expected = "Mock for Object, hashCode: " + new Object().hashCode();
        assertEquals(expected, returnsEmptyValues.answer(invocationToString()));
    }

    @Test
    public void testAnswerToStringWithName() {
        String expected = "testMockName";
        assertEquals(expected, returnsEmptyValues.answer(invocationToStringWithName()));
    }

    @Test
    public void testAnswerCompareTo() {
        assertEquals(1, returnsEmptyValues.answer(invocationCompareTo()));
    }

    @Test
    public void testReturnValueForPrimitive() {
        assertEquals(0, returnsEmptyValues.returnValueFor(int.class));
        assertEquals(0L, returnsEmptyValues.returnValueFor(long.class));
        assertEquals((short) 0, returnsEmptyValues.returnValueFor(short.class));
        assertEquals((byte) 0, returnsEmptyValues.returnValueFor(byte.class));
        assertEquals(0.0f, returnsEmptyValues.returnValueFor(float.class), 0.0);
        assertEquals(0.0, returnsEmptyValues.returnValueFor(double.class), 0.0);
        assertEquals('\u0000', returnsEmptyValues.returnValueFor(char.class));
        assertEquals(false, returnsEmptyValues.returnValueFor(boolean.class));
    }

    @Test
    public void testReturnValueForPrimitiveWrapper() {
        assertEquals(0, returnsEmptyValues.returnValueFor(Integer.class));
        assertEquals(0L, returnsEmptyValues.returnValueFor(Long.class));
        assertEquals((short) 0, returnsEmptyValues.returnValueFor(Short.class));
        assertEquals((byte) 0, returnsEmptyValues.returnValueFor(Byte.class));
        assertEquals(0.0f, returnsEmptyValues.returnValueFor(Float.class), 0.0);
        assertEquals(0.0, returnsEmptyValues.returnValueFor(Double.class), 0.0);
        assertEquals('\u0000', returnsEmptyValues.returnValueFor(Character.class));
        assertEquals(false, returnsEmptyValues.returnValueFor(Boolean.class));
    }

    @Test
    public void testReturnValueForCollection() {
        assertTrue(returnsEmptyValues.returnValueFor(Collection.class) instanceof LinkedList);
        assertTrue(returnsEmptyValues.returnValueFor(Set.class) instanceof HashSet);
        assertTrue(returnsEmptyValues.returnValueFor(HashSet.class) instanceof HashSet);
        assertTrue(returnsEmptyValues.returnValueFor(SortedSet.class) instanceof TreeSet);
        assertTrue(returnsEmptyValues.returnValueFor(TreeSet.class) instanceof TreeSet);
        assertTrue(returnsEmptyValues.returnValueFor(LinkedHashSet.class) instanceof LinkedHashSet);
        assertTrue(returnsEmptyValues.returnValueFor(List.class) instanceof LinkedList);
        assertTrue(returnsEmptyValues.returnValueFor(LinkedList.class) instanceof LinkedList);
        assertTrue(returnsEmptyValues.returnValueFor(ArrayList.class) instanceof ArrayList);
    }

    @Test
    public void testReturnValueForMap() {
        assertTrue(returnsEmptyValues.returnValueFor(Map.class) instanceof HashMap);
        assertTrue(returnsEmptyValues.returnValueFor(HashMap.class) instanceof HashMap);
        assertTrue(returnsEmptyValues.returnValueFor(SortedMap.class) instanceof TreeMap);
        assertTrue(returnsEmptyValues.returnValueFor(TreeMap.class) instanceof TreeMap);
        assertTrue(returnsEmptyValues.returnValueFor(LinkedHashMap.class) instanceof LinkedHashMap);
    }

    private InvocationOnMock invocationToString() {
        Object mock = new Object();
        return new InvocationOnMock() {
            @Override
            public Object getMock() {
                return mock;
            }

            @Override
            public java.lang.reflect.Method getMethod() {
                try {
                    return mock.getClass().getMethod("toString");
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public Object[] getArguments() {
                return null;
            }

            @Override
            public Object callRealMethod() throws Throwable {
                return null;
            }

            @Override
            public boolean isIgnoredForVerification() {
                return false;
            }

            @Override
            public MockHandler getMockHandler() {
                return null;
            }

            @Override
            public Object getMockitoInterceptor() {
                return null;
            }

            @Override
            public void markStubbingAsUsed(Answer<?> answer) {

            }
        };
    }

    private InvocationOnMock invocationToStringWithName() {
        Object mock = new Object();
        return new InvocationOnMock() {
            @Override
            public Object getMock() {
                return mock;
            }

            @Override
            public java.lang.reflect.Method getMethod() {
                try {
                    return mock.getClass().getMethod("toString");
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public Object[] getArguments() {
                return null;
            }

            @Override
            public Object callRealMethod() throws Throwable {
                return null;
            }

            @Override
            public boolean isIgnoredForVerification() {
                return false;
            }

            @Override
            public MockHandler getMockHandler() {
                return null;
            }

            @Override
            public Object getMockitoInterceptor() {
                return null;
            }

            @Override
            public void markStubbingAsUsed(Answer<?> answer) {

            }
        };
    }

    private InvocationOnMock invocationCompareTo() {
        Object mock = new Object();
        return new InvocationOnMock() {
            @Override
            public Object getMock() {
                return mock;
            }

            @Override
            public java.lang.reflect.Method getMethod() {
                try {
                    return Comparable.class.getMethod("compareTo", Object.class);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public Object[] getArguments() {
                return null;
            }

            @Override
            public Object callRealMethod() throws Throwable {
                return null;
            }

            @Override
            public boolean isIgnoredForVerification() {
                return false;
            }

            @Override
            public MockHandler getMockHandler() {
                return null;
            }

            @Override
            public Object getMockitoInterceptor() {
                return null;
            }

            @Override
            public void markStubbingAsUsed(Answer<?> answer) {

            }
        };
    }
}