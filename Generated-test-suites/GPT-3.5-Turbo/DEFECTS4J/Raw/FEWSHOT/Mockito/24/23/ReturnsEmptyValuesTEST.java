import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.junit.Before;
import org.junit.Test;

public class ReturnsEmptyValuesTest {
    
    private ReturnsEmptyValues returnsEmptyValues;
    
    @Before
    public void setup() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }
    
    @Test
    public void testAnswer() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object result = returnsEmptyValues.answer(invocation);
        
        assertNull(result);
    }
    
    @Test
    public void testReturnValueFor() {
        Class<?> type = String.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertNull(result);
    }
    
    @Test
    public void testReturnValueFor_primitiveType() {
        Class<?> type = int.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testReturnValueFor_wrapperClass() {
        Class<?> type = Integer.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testReturnValueFor_Collection() {
        Class<?> type = Collection.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertEquals(new LinkedList<Object>(), result);
    }
    
    @Test
    public void testReturnValueFor_Set() {
        Class<?> type = Set.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertEquals(new HashSet<Object>(), result);
    }
    
    @Test
    public void testReturnValueFor_HashSet() {
        Class<?> type = HashSet.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertEquals(new HashSet<Object>(), result);
    }
    
    @Test
    public void testReturnValueFor_SortedSet() {
        Class<?> type = SortedSet.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertEquals(new TreeSet<Object>(), result);
    }
    
    @Test
    public void testReturnValueFor_TreeSet() {
        Class<?> type = TreeSet.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertEquals(new TreeSet<Object>(), result);
    }
    
    @Test
    public void testReturnValueFor_LinkedHashSet() {
        Class<?> type = LinkedHashSet.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertEquals(new LinkedHashSet<Object>(), result);
    }
    
    @Test
    public void testReturnValueFor_List() {
        Class<?> type = List.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertEquals(new LinkedList<Object>(), result);
    }
    
    @Test
    public void testReturnValueFor_LinkedList() {
        Class<?> type = LinkedList.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertEquals(new LinkedList<Object>(), result);
    }
    
    @Test
    public void testReturnValueFor_ArrayList() {
        Class<?> type = ArrayList.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertEquals(new ArrayList<Object>(), result);
    }
    
    @Test
    public void testReturnValueFor_Map() {
        Class<?> type = Map.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertEquals(new HashMap<Object, Object>(), result);
    }
    
    @Test
    public void testReturnValueFor_HashMap() {
        Class<?> type = HashMap.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertEquals(new HashMap<Object, Object>(), result);
    }
    
    @Test
    public void testReturnValueFor_SortedMap() {
        Class<?> type = SortedMap.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertEquals(new TreeMap<Object, Object>(), result);
    }
    
    @Test
    public void testReturnValueFor_TreeMap() {
        Class<?> type = TreeMap.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertEquals(new TreeMap<Object, Object>(), result);
    }
    
    @Test
    public void testReturnValueFor_LinkedHashMap() {
        Class<?> type = LinkedHashMap.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertEquals(new LinkedHashMap<Object, Object>(), result);
    }
  
}