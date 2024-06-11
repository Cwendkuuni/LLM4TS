import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ReturnsEmptyValuesTest {
    
    @Test
    public void testAnswer_String() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        assertEquals("", values.returnValueFor(String.class));
    }
    
    @Test
    public void testAnswer_Integer() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        assertEquals(0, values.returnValueFor(Integer.class));
    }
    
    @Test
    public void testAnswer_Collection() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        assertEquals(new LinkedList<Object>(), values.returnValueFor(Collection.class));
    }
    
    @Test
    public void testAnswer_Set() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        assertEquals(new HashSet<Object>(), values.returnValueFor(Set.class));
    }
    
    @Test
    public void testAnswer_HashSet() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        assertEquals(new HashSet<Object>(), values.returnValueFor(HashSet.class));
    }
    
    @Test
    public void testAnswer_SortedSet() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        assertEquals(new TreeSet<Object>(), values.returnValueFor(SortedSet.class));
    }
    
    @Test
    public void testAnswer_TreeSet() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        assertEquals(new TreeSet<Object>(), values.returnValueFor(TreeSet.class));
    }
    
    @Test
    public void testAnswer_LinkedHashSet() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        assertEquals(new LinkedHashSet<Object>(), values.returnValueFor(LinkedHashSet.class));
    }
    
    @Test
    public void testAnswer_List() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        assertEquals(new LinkedList<Object>(), values.returnValueFor(List.class));
    }
    
    @Test
    public void testAnswer_LinkedList() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        assertEquals(new LinkedList<Object>(), values.returnValueFor(LinkedList.class));
    }
    
    @Test
    public void testAnswer_ArrayList() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        assertEquals(new ArrayList<Object>(), values.returnValueFor(ArrayList.class));
    }
    
    @Test
    public void testAnswer_Map() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        assertEquals(new HashMap<Object, Object>(), values.returnValueFor(Map.class));
    }
    
    @Test
    public void testAnswer_HashMap() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        assertEquals(new HashMap<Object, Object>(), values.returnValueFor(HashMap.class));
    }
    
    @Test
    public void testAnswer_SortedMap() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        assertEquals(new TreeMap<Object, Object>(), values.returnValueFor(SortedMap.class));
    }
    
    @Test
    public void testAnswer_TreeMap() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        assertEquals(new TreeMap<Object, Object>(), values.returnValueFor(TreeMap.class));
    }
    
    @Test
    public void testAnswer_LinkedHashMap() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        assertEquals(new LinkedHashMap<Object, Object>(), values.returnValueFor(LinkedHashMap.class));
    }
    
    @Test
    public void testAnswer_OtherClass() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        assertEquals(null, values.returnValueFor(Object.class));
    }
}