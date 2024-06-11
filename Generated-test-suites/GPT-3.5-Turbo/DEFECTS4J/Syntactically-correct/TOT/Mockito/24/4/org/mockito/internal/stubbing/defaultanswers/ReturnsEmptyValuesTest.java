import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import org.mockito.stubbing.Answer;

public class ReturnsEmptyValuesTest {

    @Mock
    private InvocationOnMock invocation;
    
    private ReturnsEmptyValues returnsEmptyValues;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        returnsEmptyValues = new ReturnsEmptyValues();
    }
    
    @Test
    public void testAnswer_methodIsToString_returnMockDescription() {
        when(invocation.getMethod()).thenReturn(String.class.getDeclaredMethods()[0]);
        Object mock = mock(Object.class);
        when(invocation.getMock()).thenReturn(mock);
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertEquals("Mock for Object, hashCode: " + mock.hashCode(), result);
    }
    
    @Test
    public void testAnswer_isCompareToMethod_return1() {
        when(invocation.getMethod()).thenReturn(Comparable.class.getDeclaredMethods()[0]);
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertEquals(1, result);
    }
    
    @Test
    public void testAnswer_returnTypeIsPrimitive_returnDefaultPrimitiveValue() {
        when(invocation.getMethod()).thenReturn(TestObject.class.getDeclaredMethods()[0]);
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testAnswer_returnTypeIsPrimitiveWrapper_returnDefaultPrimitiveValue() {
        when(invocation.getMethod()).thenReturn(TestObject.class.getDeclaredMethods()[1]);
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testAnswer_returnTypeIsCollection_returnEmptyLinkedList() {
        when(invocation.getMethod()).thenReturn(TestObject.class.getDeclaredMethods()[2]);
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertTrue(result instanceof LinkedList);
        assertTrue(((LinkedList) result).isEmpty());
    }
    
    @Test
    public void testAnswer_returnTypeIsSet_returnEmptyHashSet() {
        when(invocation.getMethod()).thenReturn(TestObject.class.getDeclaredMethods()[3]);
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertTrue(result instanceof HashSet);
        assertTrue(((HashSet) result).isEmpty());
    }
    
    @Test
    public void testAnswer_returnTypeIsHashSet_returnEmptyHashSet() {
        when(invocation.getMethod()).thenReturn(TestObject.class.getDeclaredMethods()[4]);
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertTrue(result instanceof HashSet);
        assertTrue(((HashSet) result).isEmpty());
    }
    
    @Test
    public void testAnswer_returnTypeIsSortedSet_returnEmptyTreeSet() {
        when(invocation.getMethod()).thenReturn(TestObject.class.getDeclaredMethods()[5]);
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertTrue(result instanceof TreeSet);
        assertTrue(((TreeSet) result).isEmpty());
    }
    
    @Test
    public void testAnswer_returnTypeIsTreeSet_returnEmptyTreeSet() {
        when(invocation.getMethod()).thenReturn(TestObject.class.getDeclaredMethods()[6]);
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertTrue(result instanceof TreeSet);
        assertTrue(((TreeSet) result).isEmpty());
    }
    
    @Test
    public void testAnswer_returnTypeIsLinkedHashSet_returnEmptyLinkedHashSet() {
        when(invocation.getMethod()).thenReturn(TestObject.class.getDeclaredMethods()[7]);
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertTrue(result instanceof LinkedHashSet);
        assertTrue(((LinkedHashSet) result).isEmpty());
    }
    
    @Test
    public void testAnswer_returnTypeIsList_returnEmptyLinkedList() {
        when(invocation.getMethod()).thenReturn(TestObject.class.getDeclaredMethods()[8]);
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertTrue(result instanceof LinkedList);
        assertTrue(((LinkedList) result).isEmpty());
    }
    
    @Test
    public void testAnswer_returnTypeIsLinkedList_returnEmptyLinkedList() {
        when(invocation.getMethod()).thenReturn(TestObject.class.getDeclaredMethods()[9]);
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertTrue(result instanceof LinkedList);
        assertTrue(((LinkedList) result).isEmpty());
    }
    
    @Test
    public void testAnswer_returnTypeIsArrayList_returnEmptyArrayList() {
        when(invocation.getMethod()).thenReturn(TestObject.class.getDeclaredMethods()[10]);
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertTrue(result instanceof ArrayList);
        assertTrue(((ArrayList) result).isEmpty());
    }
    
    @Test
    public void testAnswer_returnTypeIsMap_returnEmptyHashMap() {
        when(invocation.getMethod()).thenReturn(TestObject.class.getDeclaredMethods()[11]);
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertTrue(result instanceof HashMap);
        assertTrue(((HashMap) result).isEmpty());
    }
    
    @Test
    public void testAnswer_returnTypeIsHashMap_returnEmptyHashMap() {
        when(invocation.getMethod()).thenReturn(TestObject.class.getDeclaredMethods()[12]);
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertTrue(result instanceof HashMap);
        assertTrue(((HashMap) result).isEmpty());
    }
    
    @Test
    public void testAnswer_returnTypeIsSortedMap_returnEmptyTreeMap() {
        when(invocation.getMethod()).thenReturn(TestObject.class.getDeclaredMethods()[13]);
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertTrue(result instanceof TreeMap);
        assertTrue(((TreeMap) result).isEmpty());
    }
    
    @Test
    public void testAnswer_returnTypeIsTreeMap_returnEmptyTreeMap() {
        when(invocation.getMethod()).thenReturn(TestObject.class.getDeclaredMethods()[14]);
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertTrue(result instanceof TreeMap);
        assertTrue(((TreeMap) result).isEmpty());
    }
    
    @Test
    public void testAnswer_returnTypeIsLinkedHashMap_returnEmptyLinkedHashMap() {
        when(invocation.getMethod()).thenReturn(TestObject.class.getDeclaredMethods()[15]);
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertTrue(result instanceof LinkedHashMap);
        assertTrue(((LinkedHashMap) result).isEmpty());
    }
    
    @Test
    public void testAnswer_returnTypeIsOther_returnNull() {
        when(invocation.getMethod()).thenReturn(TestObject.class.getDeclaredMethods()[16]);
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertNull(result);
    }
    
    private static class TestObject {
        public int returnInt() {
            return 0;
        }
        
        public Integer returnInteger() {
            return 0;
        }
        
        public Collection<Object> returnCollection() {
            return null;
        }
        
        public Set<Object> returnSet() {
            return null;
        }
        
        public HashSet<Object> returnHashSet() {
            return null;
        }
        
        public SortedSet<Object> returnSortedSet() {
            return null;
        }
        
        public TreeSet<Object> returnTreeSet() {
            return null;
        }
        
        public LinkedHashSet<Object> returnLinkedHashSet() {
            return null;
        }
        
        public List<Object> returnList() {
            return null;
        }
        
        public LinkedList<Object> returnLinkedList() {
            return null;
        }
        
        public ArrayList<Object> returnArrayList() {
            return null;
        }
        
        public Map<Object, Object> returnMap() {
            return null;
        }
        
        public HashMap<Object, Object> returnHashMap() {
            return null;
        }
        
        public SortedMap<Object, Object> returnSortedMap() {
            return null;
        }
        
        public TreeMap<Object, Object> returnTreeMap() {
            return null;
        }
        
        public LinkedHashMap<Object, Object> returnLinkedHashMap() {
            return null;
        }
        
        public Object returnOther() {
            return null;
        }   
    }
}