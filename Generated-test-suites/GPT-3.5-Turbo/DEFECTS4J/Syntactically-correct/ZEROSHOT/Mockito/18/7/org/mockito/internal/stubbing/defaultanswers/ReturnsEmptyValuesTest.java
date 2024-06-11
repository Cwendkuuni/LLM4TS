import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;

public class ReturnsEmptyValuesTest {
    
    private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    
    @Test
    public void testAnswer_toString() {
        Object mock = new Object();
        assertEquals("Mock for Object, hashCode: " + mock.hashCode(), returnsEmptyValues.answer(InvocationOnMock));
    }
    
    @Test
    public void testAnswer_compareTo() {
        Object mock = new Object();
        Object[] arguments = {mock};
        assertEquals(0, returnsEmptyValues.answer(InvocationOnMock));
    }
    
    @Test
    public void testAnswer_primitive() {
        assertEquals(0, returnsEmptyValues.answer(InvocationOnMock));
    }
    
    @Test
    public void testAnswer_collection() {
        assertEquals(new LinkedList<Object>(), returnsEmptyValues.answer(InvocationOnMock));
    }
    
    @Test
    public void testAnswer_set() {
        assertEquals(new HashSet<Object>(), returnsEmptyValues.answer(InvocationOnMock));
    }
    
    @Test
    public void testAnswer_hashSet() {
        assertEquals(new HashSet<Object>(), returnsEmptyValues.answer(InvocationOnMock));
    }
    
    @Test
    public void testAnswer_sortedSet() {
        assertEquals(new TreeSet<Object>(), returnsEmptyValues.answer(InvocationOnMock));
    }
    
    @Test
    public void testAnswer_treeSet() {
        assertEquals(new TreeSet<Object>(), returnsEmptyValues.answer(InvocationOnMock));
    }
    
    @Test
    public void testAnswer_linkedHashSet() {
        assertEquals(new LinkedHashSet<Object>(), returnsEmptyValues.answer(InvocationOnMock));
    }
    
    @Test
    public void testAnswer_list() {
        assertEquals(new LinkedList<Object>(), returnsEmptyValues.answer(InvocationOnMock));
    }
    
    @Test
    public void testAnswer_linkedList() {
        assertEquals(new LinkedList<Object>(), returnsEmptyValues.answer(InvocationOnMock));
    }
    
    @Test
    public void testAnswer_arrayList() {
        assertEquals(new ArrayList<Object>(), returnsEmptyValues.answer(InvocationOnMock));
    }
    
    @Test
    public void testAnswer_map() {
        assertEquals(new HashMap<Object, Object>(), returnsEmptyValues.answer(InvocationOnMock));
    }
    
    @Test
    public void testAnswer_hashMap() {
        assertEquals(new HashMap<Object, Object>(), returnsEmptyValues.answer(InvocationOnMock));
    }
    
    @Test
    public void testAnswer_sortedMap() {
        assertEquals(new TreeMap<Object, Object>(), returnsEmptyValues.answer(InvocationOnMock));
    }
    
    @Test
    public void testAnswer_treeMap() {
        assertEquals(new TreeMap<Object, Object>(), returnsEmptyValues.answer(InvocationOnMock));
    }
    
    @Test
    public void testAnswer_linkedHashMap() {
        assertEquals(new LinkedHashMap<Object, Object>(), returnsEmptyValues.answer(InvocationOnMock));
    }
}