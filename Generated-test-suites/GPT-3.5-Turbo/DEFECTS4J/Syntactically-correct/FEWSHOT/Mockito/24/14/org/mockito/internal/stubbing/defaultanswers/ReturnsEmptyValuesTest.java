import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ReturnsEmptyValuesTest {
    
    @Test
    public void testAnswer() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        
        assertEquals("Mock for Object, hashCode: 123", emptyValues.answer(null));
        assertEquals(1, emptyValues.answer(null));
        assertNull(emptyValues.answer(null));
        assertEquals(0, emptyValues.answer(null));
        assertEquals(new LinkedList<Object>(), emptyValues.answer(Collection.class));
        assertEquals(new HashSet<Object>(), emptyValues.answer(Set.class));
        assertEquals(new HashSet<Object>(), emptyValues.answer(HashSet.class));
        assertEquals(new TreeSet<Object>(), emptyValues.answer(SortedSet.class));
        assertEquals(new TreeSet<Object>(), emptyValues.answer(TreeSet.class));
        assertEquals(new LinkedHashSet<Object>(), emptyValues.answer(LinkedHashSet.class));
        assertEquals(new LinkedList<Object>(), emptyValues.answer(List.class));
        assertEquals(new LinkedList<Object>(), emptyValues.answer(LinkedList.class));
        assertEquals(new ArrayList<Object>(), emptyValues.answer(ArrayList.class));
        assertEquals(new HashMap<Object, Object>(), emptyValues.answer(Map.class));
        assertEquals(new HashMap<Object, Object>(), emptyValues.answer(HashMap.class));
        assertEquals(new TreeMap<Object, Object>(), emptyValues.answer(SortedMap.class));
        assertEquals(new TreeMap<Object, Object>(), emptyValues.answer(TreeMap.class));
        assertEquals(new LinkedHashMap<Object, Object>(), emptyValues.answer(LinkedHashMap.class));
    }
}