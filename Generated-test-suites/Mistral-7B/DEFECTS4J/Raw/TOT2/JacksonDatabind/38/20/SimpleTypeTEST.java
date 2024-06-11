import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {
    @Test
    public void testSimpleTypeConstruction() {
        // Test default constructor
        SimpleType simpleType1 = new SimpleType(String.class);
        assertNotNull(simpleType1);
        
        // Test constructor with Class<?> cls
        SimpleType simpleType2 = new SimpleType(Integer.class);
        assertNotNull(simpleType2);
        
        // Test equals method
        assertEquals(simpleType1, simpleType1);
        assertEquals(simpleType1, simpleType1);
        assertNotEquals(simpleType1, simpleType2);
        assertNotEquals(simpleType2, simpleType1);
    }
}