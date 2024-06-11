import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
    private ArrowType arrowType;
    
    @Before
    public void setUp() {
        arrowType = new ArrowType(
            new JSTypeRegistry(),
            new Node(null),
            new JSType(JSTypeNative.BOOLEAN_TYPE));
    }
    
    @Test
    public void testIsSubtype() {
        ArrowType other = new ArrowType(
            new JSTypeRegistry(),
            new Node(null),
            new JSType(JSTypeNative.UNKNOWN_TYPE));
        
        assertFalse(arrowType.isSubtype(null));
        assertFalse(arrowType.isSubtype(new JSType(JSTypeNative.UNKNOWN_TYPE)));
        assertFalse(arrowType.isSubtype(other));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(arrowType.hashCode(), arrowType.hashCode());
    }
}