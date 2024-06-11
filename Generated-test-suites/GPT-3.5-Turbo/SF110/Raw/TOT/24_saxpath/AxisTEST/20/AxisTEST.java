import org.junit.Test;
import static org.junit.Assert.*;

public class AxisTest {
    
    @Test
    public void testLookupWithValidAxisNum() {
        // Typical use case
        assertEquals("child", Axis.lookup(1));
        
        // Typical use case
        assertEquals("descendant", Axis.lookup(2));
        
        // Typical use case
        assertEquals("parent", Axis.lookup(3));
        
        // Typical use case
        assertEquals("ancestor", Axis.lookup(4));
        
        // Typical use case
        assertEquals("following-sibling", Axis.lookup(5));
        
        // Typical use case
        assertEquals("preceding-sibling", Axis.lookup(6));
        
        // Typical use case
        assertEquals("following", Axis.lookup(7));
        
        // Typical use case
        assertEquals("preceding", Axis.lookup(8));
        
        // Typical use case
        assertEquals("attribute", Axis.lookup(9));
        
        // Typical use case
        assertEquals("namespace", Axis.lookup(10));
        
        // Typical use case
        assertEquals("self", Axis.lookup(11));
        
        // Typical use case
        assertEquals("descendant-or-self", Axis.lookup(12));
        
        // Typical use case
        assertEquals("ancestor-or-self", Axis.lookup(13));
    }
    
    @Test
    public void testLookupWithInvalidAxisNum() {
        // Error scenario
        assertNull(Axis.lookup(0));
    }
    
    @Test
    public void testLookupWithValidAxisName() {
        // Typical use case
        assertEquals(1, Axis.lookup("child"));
        
        // Typical use case
        assertEquals(2, Axis.lookup("descendant"));
        
        // Typical use case
        assertEquals(3, Axis.lookup("parent"));
        
        // Typical use case
        assertEquals(4, Axis.lookup("ancestor"));
        
        // Typical use case
        assertEquals(5, Axis.lookup("following-sibling"));
        
        // Typical use case
        assertEquals(6, Axis.lookup("preceding-sibling"));
        
        // Typical use case
        assertEquals(7, Axis.lookup("following"));
        
        // Typical use case
        assertEquals(8, Axis.lookup("preceding"));
        
        // Typical use case
        assertEquals(9, Axis.lookup("attribute"));
        
        // Typical use case
        assertEquals(10, Axis.lookup("namespace"));
        
        // Typical use case
        assertEquals(11, Axis.lookup("self"));
        
        // Typical use case
        assertEquals(12, Axis.lookup("descendant-or-self"));
        
        // Typical use case
        assertEquals(13, Axis.lookup("ancestor-or-self"));
    }
    
    @Test
    public void testLookupWithInvalidAxisName() {
        // Error scenario
        assertEquals(0, Axis.lookup("invalid"));
    }
}