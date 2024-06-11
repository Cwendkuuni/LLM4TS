import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ReturnsEmptyValuesTest {
    @Test
    public void testAnswer() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        
        // Test toString() method
        assertEquals("Mock for Object, hashCode: 0", answer.answer(null));
        
        // Test compareTo() method
        assertEquals(1, answer.answer(null));
        
        // Test primitive and wrapper classes
        assertEquals(false, answer.answer(Boolean.class));
        assertEquals(0, answer.answer(Integer.class));
        assertEquals(0L, answer.answer(Long.class));
        assertEquals(0.0, answer.answer(Double.class), 0);
        assertEquals('\u0000', answer.answer(Character.class));
        
        // Test collections
        assertNull(answer.answer(Collection.class));
        assertEquals(new LinkedList<Object>(), answer.answer(LinkedList.class));
        assertEquals(new HashSet<Object>(), answer.answer(HashSet.class));
        assertEquals(new TreeSet<Object>(), answer.answer(TreeSet.class));
        assertEquals(new LinkedHashSet<Object>(), answer.answer(LinkedHashSet.class));
        assertEquals(new ArrayList<Object>(), answer.answer(ArrayList.class));
        
        assertNull(answer.answer(Map.class));
        assertEquals(new HashMap<Object, Object>(), answer.answer(HashMap.class));
        assertEquals(new TreeMap<Object, Object>(), answer.answer(TreeMap.class));
        assertEquals(new LinkedHashMap<Object, Object>(), answer.answer(LinkedHashMap.class));
    }
}