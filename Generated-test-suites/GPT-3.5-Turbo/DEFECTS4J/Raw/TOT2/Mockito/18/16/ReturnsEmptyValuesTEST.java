import static org.junit.Assert.*;
import org.junit.Test;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        
        // Test toString() method
        assertEquals("Mock for ReturnsEmptyValues, hashCode: " + emptyValues.hashCode(), emptyValues.answer(null));
        
        // Test compareTo() method
        assertEquals(0, emptyValues.answer(null));
    }
    
    @Test
    public void testReturnValueFor() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        
        // Test primitive type
        assertEquals(0, emptyValues.returnValueFor(int.class));
        
        // Test Collection types
        assertEquals("LinkedList", emptyValues.returnValueFor(Collection.class).getClass().getSimpleName());
        assertEquals("HashSet", emptyValues.returnValueFor(HashSet.class).getClass().getSimpleName());
        assertEquals("TreeSet", emptyValues.returnValueFor(TreeSet.class).getClass().getSimpleName());
        assertEquals("LinkedHashSet", emptyValues.returnValueFor(LinkedHashSet.class).getClass().getSimpleName());
        assertEquals("ArrayList", emptyValues.returnValueFor(ArrayList.class).getClass().getSimpleName());
        
        // Test Map types
        assertEquals("HashMap", emptyValues.returnValueFor(Map.class).getClass().getSimpleName());
        assertEquals("TreeMap", emptyValues.returnValueFor(TreeMap.class).getClass().getSimpleName());
        assertEquals("LinkedHashMap", emptyValues.returnValueFor(LinkedHashMap.class).getClass().getSimpleName());
    }
}