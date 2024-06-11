import static org.junit.Assert.*;
import org.junit.Test;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_ToString() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        Object mock = new Object();
        String expected = "Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode();
        Object result = emptyValues.answer(new InvocationOnMock() {
            public Object getMock() {
                return mock;
            }
            public Object[] getArguments() {
                return null;
            }
            public Object callRealMethod() throws Throwable {
                return null;
            }
            public boolean equals(Object obj) {
                return false;
            }
            public int hashCode() {
                return 0;
            }
            public Object mockito$getMockCreationSettings() {
                return null;
            }
            public Class<?> getMethodReturnType() {
                return null;
            }
            public Object getMockitoInterceptor() {
                return null;
            }
            public Object handleMethodCall() throws Throwable {
                return null;
            }
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return null;
            }
            public String toString() {
                return null;
            }
        });
        assertEquals(expected, result);
    }

    @Test
    public void testAnswer_CompareTo() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        Object mock = new Object();
        Object argument = new Object();
        int expected = 1;
        Object result = emptyValues.answer(new InvocationOnMock() {
            public Object getMock() {
                return mock;
            }
            public Object[] getArguments() {
                return new Object[] { argument };
            }
            public Object callRealMethod() throws Throwable {
                return null;
            }
            public boolean equals(Object obj) {
                return false;
            }
            public int hashCode() {
                return 0;
            }
            public Object mockito$getMockCreationSettings() {
                return null;
            }
            public Class<?> getMethodReturnType() {
                return null;
            }
            public Object getMockitoInterceptor() {
                return null;
            }
            public Object handleMethodCall() throws Throwable {
                return null;
            }
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return null;
            }
            public String toString() {
                return null;
            }
        });
        assertEquals(expected, result);
    }

    @Test
    public void testAnswer_ReturnsPrimitive() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        Class<?>[] primitiveTypes = { byte.class, short.class, int.class, long.class, float.class, double.class, char.class, boolean.class };
        for (Class<?> type : primitiveTypes) {
            Object expected = Primitives.defaultValueForPrimitiveOrWrapper(type);
            Object result = emptyValues.answer(new InvocationOnMock() {
                public Object getMock() {
                    return null;
                }
                public Object[] getArguments() {
                    return null;
                }
                public Object callRealMethod() throws Throwable {
                    return null;
                }
                public boolean equals(Object obj) {
                    return false;
                }
                public int hashCode() {
                    return 0;
                }
                public Object mockito$getMockCreationSettings() {
                    return null;
                }
                public Class<?> getMethodReturnType() {
                    return type;
                }
                public Object getMockitoInterceptor() {
                    return null;
                }
                public Object handleMethodCall() throws Throwable {
                    return null;
                }
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return null;
                }
                public String toString() {
                    return null;
                }
            });
            assertEquals(expected, result);
        }
    }

    @Test
    public void testAnswer_ReturnsEmptyCollection() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        Class<?>[] collectionTypes = { Collection.class, Set.class, HashSet.class, SortedSet.class, TreeSet.class, LinkedHashSet.class, List.class, LinkedList.class, ArrayList.class, Map.class, HashMap.class, SortedMap.class, TreeMap.class, LinkedHashMap.class };
        for (Class<?> type : collectionTypes) {
            Object expected = newInstance(type);
            Object result = emptyValues.answer(new InvocationOnMock() {
                public Object getMock() {
                    return null;
                }
                public Object[] getArguments() {
                    return null;
                }
                public Object callRealMethod() throws Throwable {
                    return null;
                }
                public boolean equals(Object obj) {
                    return false;
                }
                public int hashCode() {
                    return 0;
                }
                public Object mockito$getMockCreationSettings() {
                    return null;
                }
                public Class<?> getMethodReturnType() {
                    return type;
                }
                public Object getMockitoInterceptor() {
                    return null;
                }
                public Object handleMethodCall() throws Throwable {
                    return null;
                }
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return null;
                }
                public String toString() {
                    return null;
                }
            });
            assertEquals(expected, result);
        }
    }

    private static Object newInstance(Class<?> type) {
        try {
            return type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to create instance of " + type.getSimpleName(), e);
        }
    }
}