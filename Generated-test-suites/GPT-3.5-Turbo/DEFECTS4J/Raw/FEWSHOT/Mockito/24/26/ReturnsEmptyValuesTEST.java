import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ReturnsEmptyValuesTest {
    
    @Test
    public void testAnswer() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        
        // Test toString() method
        assertEquals("Mock for Object, hashCode: 0", emptyValues.answer(null));
        
        // Test compareTo() method
        assertEquals(1, emptyValues.answer(null));
        
        // Test primitive and wrapper classes
        assertEquals(0, emptyValues.answer(int.class));
        assertEquals(0, emptyValues.answer(Integer.class));
        assertEquals(0L, emptyValues.answer(long.class));
        assertEquals(0L, emptyValues.answer(Long.class));
        assertEquals(0.0f, emptyValues.answer(float.class));
        assertEquals(0.0f, emptyValues.answer(Float.class));
        assertEquals(0.0, emptyValues.answer(double.class));
        assertEquals(0.0, emptyValues.answer(Double.class));
        assertEquals(false, emptyValues.answer(boolean.class));
        assertEquals(false, emptyValues.answer(Boolean.class));
        assertEquals('\u0000', emptyValues.answer(char.class));
        assertEquals('\u0000', emptyValues.answer(Character.class));
        assertNull(emptyValues.answer(void.class));
        
        // Test collection types
        assertTrue(emptyValues.answer(Collection.class) instanceof LinkedList);
        assertTrue(emptyValues.answer(Set.class) instanceof HashSet);
        assertTrue(emptyValues.answer(HashSet.class) instanceof HashSet);
        assertTrue(emptyValues.answer(SortedSet.class) instanceof TreeSet);
        assertTrue(emptyValues.answer(TreeSet.class) instanceof TreeSet);
        assertTrue(emptyValues.answer(LinkedHashSet.class) instanceof LinkedHashSet);
        assertTrue(emptyValues.answer(List.class) instanceof LinkedList);
        assertTrue(emptyValues.answer(LinkedList.class) instanceof LinkedList);
        assertTrue(emptyValues.answer(ArrayList.class) instanceof ArrayList);
        
        // Test map types
        assertTrue(emptyValues.answer(Map.class) instanceof HashMap);
        assertTrue(emptyValues.answer(HashMap.class) instanceof HashMap);
        assertTrue(emptyValues.answer(SortedMap.class) instanceof TreeMap);
        assertTrue(emptyValues.answer(TreeMap.class) instanceof TreeMap);
        assertTrue(emptyValues.answer(LinkedHashMap.class) instanceof LinkedHashMap);
    }
}