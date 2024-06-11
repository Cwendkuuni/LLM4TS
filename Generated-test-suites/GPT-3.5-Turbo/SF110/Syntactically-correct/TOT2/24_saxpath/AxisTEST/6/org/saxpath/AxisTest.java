package saxpath;
import static org.junit.Assert.*;
import org.junit.Test;

public class AxisTest {
    
    @Test
    public void testLookupByAxisNumChild() {
        assertEquals("child", Axis.lookup(1));
    }
    
    @Test
    public void testLookupByAxisNumInvalid() {
        assertNull(Axis.lookup(0));
    }
    
    @Test
    public void testLookupByAxisNameDescendant() {
        assertEquals(2, Axis.lookup("descendant"));
    }
    
    @Test
    public void testLookupByAxisNameInvalid() {
        assertEquals(0, Axis.lookup("invalid"));
    }
}