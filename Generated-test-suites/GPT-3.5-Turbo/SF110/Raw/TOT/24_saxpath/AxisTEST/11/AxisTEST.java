import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.saxpath.Axis;

public class AxisTest {
    
    @Test
    public void testLookup_WithValidAxisNumber_ReturnsCorrectAxisName() {
        assertEquals("child", Axis.lookup(1));
        assertEquals("descendant", Axis.lookup(2));
        assertEquals("parent", Axis.lookup(3));
        assertEquals("ancestor", Axis.lookup(4));
        assertEquals("following-sibling", Axis.lookup(5));
        assertEquals("preceding-sibling", Axis.lookup(6));
        assertEquals("following", Axis.lookup(7));
        assertEquals("preceding", Axis.lookup(8));
        assertEquals("attribute", Axis.lookup(9));
        assertEquals("namespace", Axis.lookup(10));
        assertEquals("self", Axis.lookup(11));
        assertEquals("descendant-or-self", Axis.lookup(12));
        assertEquals("ancestor-or-self", Axis.lookup(13));
    }

    @Test
    public void testLookup_WithInvalidAxisNumber_ReturnsNull() {
        assertEquals(null, Axis.lookup(0));
        assertEquals(null, Axis.lookup(-1));
        assertEquals(null, Axis.lookup(14));
        assertEquals(null, Axis.lookup(Integer.MAX_VALUE));
    }
    
    @Test
    public void testLookup_WithValidAxisName_ReturnsCorrectAxisNumber() {
        assertEquals(1, Axis.lookup("child"));
        assertEquals(2, Axis.lookup("descendant"));
        assertEquals(3, Axis.lookup("parent"));
        assertEquals(4, Axis.lookup("ancestor"));
        assertEquals(5, Axis.lookup("following-sibling"));
        assertEquals(6, Axis.lookup("preceding-sibling"));
        assertEquals(7, Axis.lookup("following"));
        assertEquals(8, Axis.lookup("preceding"));
        assertEquals(9, Axis.lookup("attribute"));
        assertEquals(10, Axis.lookup("namespace"));
        assertEquals(11, Axis.lookup("self"));
        assertEquals(12, Axis.lookup("descendant-or-self"));
        assertEquals(13, Axis.lookup("ancestor-or-self"));
    }

    @Test
    public void testLookup_WithInvalidAxisName_ReturnsZero() {
        assertEquals(0, Axis.lookup("invalid"));
        assertEquals(0, Axis.lookup(""));
        assertEquals(0, Axis.lookup(null));
    }
}