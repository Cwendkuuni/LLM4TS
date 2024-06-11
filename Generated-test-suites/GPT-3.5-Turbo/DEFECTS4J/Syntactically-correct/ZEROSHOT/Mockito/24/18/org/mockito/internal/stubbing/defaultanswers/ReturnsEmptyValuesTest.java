import static org.junit.Assert.*;
import org.junit.Test;

public class ReturnsEmptyValuesTest {
    
    @Test
    public void testAnswer() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        
        // Test that toString() method returns description of mock
        assertEquals("Mock for Object, hashCode: 0", emptyValues.answer(null));
        
        // Test that compareTo() method returns 1
        assertEquals(1, emptyValues.answer(null));
        
        // Test primitive and wrapper class return values
        assertEquals(false, emptyValues.answer(null));
        assertEquals(new Boolean(false), emptyValues.answer(null));
        assertEquals((byte) 0, emptyValues.answer(null));
        assertEquals(new Byte((byte) 0), emptyValues.answer(null));
        assertEquals((short) 0, emptyValues.answer(null));
        assertEquals(new Short((short) 0), emptyValues.answer(null));
        assertEquals(0, emptyValues.answer(null));
        assertEquals(new Integer(0), emptyValues.answer(null));
        assertEquals(0L, emptyValues.answer(null));
        assertEquals(new Long(0L), emptyValues.answer(null));
        assertEquals(0.0f, emptyValues.answer(null), 0.0001);
        assertEquals(new Float(0.0f), emptyValues.answer(null));
        assertEquals(0.0, emptyValues.answer(null), 0.0001);
        assertEquals(new Double(0.0), emptyValues.answer(null));
        assertEquals('\u0000', emptyValues.answer(null));
        assertEquals(new Character('\u0000'), emptyValues.answer(null));
        
        // Test collection return values
        assertEquals(new LinkedList<Object>(), emptyValues.answer(null));
        assertEquals(new HashSet<Object>(), emptyValues.answer(null));
        assertEquals(new TreeSet<Object>(), emptyValues.answer(null));
        assertEquals(new LinkedHashSet<Object>(), emptyValues.answer(null));
        assertEquals(new ArrayList<Object>(), emptyValues.answer(null));
        
        // Test map return values
        assertEquals(new HashMap<Object, Object>(), emptyValues.answer(null));
        assertEquals(new TreeMap<Object, Object>(), emptyValues.answer(null));
        assertEquals(new LinkedHashMap<Object, Object>(), emptyValues.answer(null));
        
        // Test other return values
        assertNull(emptyValues.answer(null));
    }
}