import static org.junit.Assert.*;
import org.junit.Test;

public class AxisTest {

    @Test
    public void testLookupInt() {
        // Typical use case
        assertEquals("child", Axis.lookup(Axis.CHILD));
        
        // Edge cases 
        assertEquals("ancestor-or-self", Axis.lookup(Axis.ANCESTOR_OR_SELF));
        assertEquals("self", Axis.lookup(Axis.SELF));
        
        // Error scenarios
        assertNull(Axis.lookup(15));
    }
    
    @Test
    public void testLookupString() {
        // Typical use case
        assertEquals(Axis.DESCENDANT, Axis.lookup("descendant"));
        
        // Edge cases 
        assertEquals(Axis.NAMESPACE, Axis.lookup("namespace"));
        assertEquals(Axis.PRECEDING_SIBLING, Axis.lookup("preceding-sibling"));
        
        // Error scenarios
        assertEquals(0, Axis.lookup("invalid axis"));
    }

}