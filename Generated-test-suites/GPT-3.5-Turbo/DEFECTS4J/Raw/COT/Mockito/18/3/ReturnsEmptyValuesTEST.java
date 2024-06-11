import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsEmptyValuesTest {

    ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

    @Test
    public void testAnswer_toString() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethod("toString"));
        Object result = returnsEmptyValues.answer(invocation);
        assertNotNull(result);
        assertTrue(result instanceof String);
        assertEquals("Mock for Object, hashCode: 0", result);
    }

    @Test
    public void testAnswer_compareTo() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Comparable.class.getDeclaredMethod("compareTo", Object.class));
        when(invocation.getMock()).thenReturn(new Comparable<Object>() {
            public int compareTo(Object other) {
                return 0;
            }
        });
        Object result = returnsEmptyValues.answer(invocation);
        assertNotNull(result);
        assertTrue(result instanceof Integer);
        assertEquals(0, result);
    }

    @Test
    public void testAnswer_primitiveReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ClassWithPrimitiveReturn.class.getDeclaredMethod("getInt"));
        Object result = returnsEmptyValues.answer(invocation);
       assertNotNull(result);
       assertTrue(result instanceof Integer);
       assertEquals(0, result);
    }

    @Test
    public void testAnswer_primitiveWrapperReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ClassWithWrapperReturn.class.getDeclaredMethod("getInteger"));
        Object result = returnsEmptyValues.answer(invocation);
       assertNotNull(result);
       assertTrue(result instanceof Integer);
       assertEquals(0, result);
    }

    @Test
    public void testAnswer_collectionReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ClassWithCollectionReturn.class.getDeclaredMethod("getCollection"));
        Object result = returnsEmptyValues.answer(invocation);
       assertNotNull(result);
       assertTrue(result instanceof LinkedList);
       assertTrue(((LinkedList) result).isEmpty());
    }

    @Test
    public void testAnswer_setReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ClassWithSetReturn.class.getDeclaredMethod("getSet"));
        Object result = returnsEmptyValues.answer(invocation);
       assertNotNull(result);
       assertTrue(result instanceof HashSet);
       assertTrue(((HashSet) result).isEmpty());
    }

    @Test
    public void testAnswer_sortedSetReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ClassWithSortedSetReturn.class.getDeclaredMethod("getSortedSet"));
        Object result = returnsEmptyValues.answer(invocation);
       assertNotNull(result);
       assertTrue(result instanceof TreeSet);
       assertTrue(((TreeSet) result).isEmpty());
    }

    @Test
    public void testAnswer_linkedHashSetReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ClassWithLinkedHashSetReturn.class.getDeclaredMethod("getLinkedHashSet"));
        Object result = returnsEmptyValues.answer(invocation);
       assertNotNull(result);
       assertTrue(result instanceof LinkedHashSet);
       assertTrue(((LinkedHashSet) result).isEmpty());
    }

    @Test
    public void testAnswer_listReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ClassWithListReturn.class.getDeclaredMethod("getList"));
        Object result = returnsEmptyValues.answer(invocation);
       assertNotNull(result);
       assertTrue(result instanceof LinkedList);
       assertTrue(((LinkedList) result).isEmpty());
    }

    @Test
    public void testAnswer_linkedListReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ClassWithLinkedListReturn.class.getDeclaredMethod("getLinkedList"));
        Object result = returnsEmptyValues.answer(invocation);
       assertNotNull(result);
       assertTrue(result instanceof LinkedList);
       assertTrue(((LinkedList) result).isEmpty());
    }

    @Test
    public void testAnswer_arrayListReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ClassWithArrayListReturn.class.getDeclaredMethod("getArrayList"));
        Object result = returnsEmptyValues.answer(invocation);
       assertNotNull(result);
       assertTrue(result instanceof ArrayList);
       assertTrue(((ArrayList) result).isEmpty());
    }

    @Test
    public void testAnswer_mapReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ClassWithMapReturn.class.getDeclaredMethod("getMap"));
        Object result = returnsEmptyValues.answer(invocation);
       assertNotNull(result);
       assertTrue(result instanceof HashMap);
       assertTrue(((HashMap) result).isEmpty());
    }

    @Test
    public void testAnswer_hashMapReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ClassWithHashMapReturn.class.getDeclaredMethod("getHashMap"));
        Object result = returnsEmptyValues.answer(invocation);
       assertNotNull(result);
       assertTrue(result instanceof HashMap);
       assertTrue(((HashMap) result).isEmpty());
    }

    @Test
    public void testAnswer_sortedMapReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ClassWithSortedMapReturn.class.getDeclaredMethod("getSortedMap"));
        Object result = returnsEmptyValues.answer(invocation);
       assertNotNull(result);
       assertTrue(result instanceof TreeMap);
       assertTrue(((TreeMap) result).isEmpty());
    }

    @Test
    public void testAnswer_treeMapReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ClassWithTreeMapReturn.class.getDeclaredMethod("getTreeMap"));
        Object result = returnsEmptyValues.answer(invocation);
       assertNotNull(result);
       assertTrue(result instanceof TreeMap);
       assertTrue(((TreeMap) result).isEmpty());
    }

    @Test
    public void testAnswer_linkedHashMapReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ClassWithLinkedHashMapReturn.class.getDeclaredMethod("getLinkedHashMap"));
        Object result = returnsEmptyValues.answer(invocation);
       assertNotNull(result);
       assertTrue(result instanceof LinkedHashMap);
       assertTrue(((LinkedHashMap) result).isEmpty());
    }

    private static class ClassWithPrimitiveReturn {
        public int getInt() {
            return 0;
        }
    }

    private static class ClassWithWrapperReturn {
        public Integer getInteger() {
            return 0;
        }
    }

    private static class ClassWithCollectionReturn {
        public Collection<Object> getCollection() {
            return new LinkedList<Object>();
        }
    }

    private static class ClassWithSetReturn {
        public Set<Object> getSet() {
            return new HashSet<Object>();
        }
    }

    private static class ClassWithSortedSetReturn {
        public SortedSet<Object> getSortedSet() {
            return new TreeSet<Object>();
        }
    }

    private static class ClassWithLinkedHashSetReturn {
        public LinkedHashSet<Object> getLinkedHashSet() {
            return new LinkedHashSet<Object>();
        }
    }

    private static class ClassWithListReturn {
        public List<Object> getList() {
            return new LinkedList<Object>();
        }
    }

    private static class ClassWithLinkedListReturn {
        public LinkedList<Object> getLinkedList() {
            return new LinkedList<Object>();
        }
    }

    private static class ClassWithArrayListReturn {
        public ArrayList<Object> getArrayList() {
            return new ArrayList<Object>();
        }
    }

    private static class ClassWithMapReturn {
        public Map<Object, Object> getMap() {
            return new HashMap<Object, Object>();
        }
    }

    private static class ClassWithHashMapReturn {
        public HashMap<Object, Object> getHashMap() {
            return new HashMap<Object, Object>();
        }
    }

    private static class ClassWithSortedMapReturn {
        public SortedMap<Object, Object> getSortedMap() {
            return new TreeMap<Object, Object>();
        }
    }

    private static class ClassWithTreeMapReturn {
        public TreeMap<Object, Object> getTreeMap() {
            return new TreeMap<Object, Object>();
        }
    }

    private static class ClassWithLinkedHashMapReturn {
        public LinkedHashMap<Object, Object> getLinkedHashMap() {
            return new LinkedHashMap<Object, Object>();
        }
    }
}