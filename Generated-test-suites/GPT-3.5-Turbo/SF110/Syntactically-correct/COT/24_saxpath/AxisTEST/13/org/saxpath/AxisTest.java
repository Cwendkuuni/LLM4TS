
import org.junit.*;
import static org.junit.Assert.*;

public class AxisTest {

    @Test
    public void testLookup_validAxisNumber_returnsAxisName() {
        String expected = "child";
        String actual = Axis.lookup(1);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLookup_invalidAxisNumber_returnsNull() {
        String expected = null;
        String actual = Axis.lookup(0);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLookup_validAxisName_returnsAxisNumber() {
        int expected = 3;
        int actual = Axis.lookup("parent");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLookup_invalidAxisName_returnsZero() {
        int expected = 0;
        int actual = Axis.lookup("invalid");
        assertEquals(expected, actual);
    }

}
