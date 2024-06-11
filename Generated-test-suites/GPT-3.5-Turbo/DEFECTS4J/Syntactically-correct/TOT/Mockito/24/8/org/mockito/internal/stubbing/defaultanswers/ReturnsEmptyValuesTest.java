import org.junit.Test;
import static org.mockito.Mockito.*;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_toString() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getMethod("toString"));

        Object result = returnsEmptyValues.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof String);
        assertEquals("Mock for TestClass, hashCode: " + hashCode(), result);
    }

    @Test
    public void testAnswer_compareTo() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getMethod("compareTo", Object.class));

        Object result = returnsEmptyValues.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof Integer);
        assertEquals(1, result);
    }

    @Test
    public void testAnswer_primitiveReturnType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getMethod("primitiveReturnType"));

        Object result = returnsEmptyValues.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof Integer);
        assertEquals(0, result);
    }

    @Test
    public void testAnswer_wrapperClassReturnType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getMethod("wrapperClassReturnType"));

        Object result = returnsEmptyValues.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof Integer);
        assertEquals(0, result);
    }

    @Test
    public void testAnswer_collectionReturnType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getMethod("collectionReturnType"));

        Object result = returnsEmptyValues.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
        assertTrue(((LinkedList)result).isEmpty());
    }

    @Test
    public void testAnswer_setReturnType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getMethod("setReturnType"));

        Object result = returnsEmptyValues.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof HashSet);
        assertTrue(((HashSet)result).isEmpty());
    }

    @Test
    public void testAnswer_sortedSetReturnType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getMethod("sortedSetReturnType"));

        Object result = returnsEmptyValues.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof TreeSet);
        assertTrue(((TreeSet)result).isEmpty());
    }

    @Test
    public void testAnswer_linkedHashSetReturnType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getMethod("linkedHashSetReturnType"));

        Object result = returnsEmptyValues.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof LinkedHashSet);
        assertTrue(((LinkedHashSet)result).isEmpty());
    }

    @Test
    public void testAnswer_listReturnType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getMethod("listReturnType"));

        Object result = returnsEmptyValues.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
        assertTrue(((LinkedList)result).isEmpty());
    }

    @Test
    public void testAnswer_arrayListReturnType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getMethod("arrayListReturnType"));

        Object result = returnsEmptyValues.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof ArrayList);
        assertTrue(((ArrayList)result).isEmpty());
    }

    @Test
    public void testAnswer_mapReturnType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getMethod("mapReturnType"));

        Object result = returnsEmptyValues.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof HashMap);
        assertTrue(((HashMap)result).isEmpty());
    }

    @Test
    public void testAnswer_sortedMapReturnType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getMethod("sortedMapReturnType"));

        Object result = returnsEmptyValues.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof TreeMap);
        assertTrue(((TreeMap)result).isEmpty());
    }

    @Test
    public void testAnswer_linkedHashMapReturnType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getMethod("linkedHashMapReturnType"));

        Object result = returnsEmptyValues.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof LinkedHashMap);
        assertTrue(((LinkedHashMap)result).isEmpty());
    }

    private static class TestClass implements Comparable<Object> {
        @Override
        public String toString() {
            return "toString";
        }

        @Override
        public int compareTo(Object o) {
            return 0;
        }

        public int primitiveReturnType() {
            return 0;
        }

        public Integer wrapperClassReturnType() {
            return 0;
        }

        public Collection<Object> collectionReturnType() {
            return null;
        }

        public Set<Object> setReturnType() {
            return null;
        }

        public SortedSet<Object> sortedSetReturnType() {
            return null;
        }

        public LinkedHashSet<Object> linkedHashSetReturnType() {
            return null;
        }

        public List<Object> listReturnType() {
            return null;
        }

        public ArrayList<Object> arrayListReturnType() {
            return null;
        }

        public Map<Object, Object> mapReturnType() {
            return null;
        }

        public SortedMap<Object, Object> sortedMapReturnType() {
            return null;
        }

        public LinkedHashMap<Object, Object> linkedHashMapReturnType() {
            return null;
        }
    }
}