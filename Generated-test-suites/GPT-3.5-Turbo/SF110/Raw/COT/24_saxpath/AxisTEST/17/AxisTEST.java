import org.junit.*;
import static org.junit.Assert.*;

public class AxisTest {

    @Test
    public void testLookup() {
        //Test valid axis numbers
        assertEquals("child", Axis.lookup(1));
        assertEquals("ancestor", Axis.lookup(4));
        assertEquals("following", Axis.lookup(7));
        
        //Test invalid axis number
        assertNull(Axis.lookup(0));
    }
    
    @Test
    public void testLookup() {
        //Test valid axis names
        assertEquals(1, Axis.lookup("child"));
        assertEquals(9, Axis.lookup("attribute"));
        assertEquals(11, Axis.lookup("self"));
        
        //Test invalid axis name
        assertEquals(0, Axis.lookup("invalid"));
    }
}