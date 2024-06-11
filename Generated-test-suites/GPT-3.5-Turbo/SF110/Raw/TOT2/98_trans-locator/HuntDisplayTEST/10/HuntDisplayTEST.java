
import org.junit.*;
import static org.junit.Assert.*;

public class HuntDisplayTest {

    @Test
    public void testGetPreferredSize() {
        HuntDisplay display = new HuntDisplay();
        Dimension expectedSize = new Dimension(200, 200);
        assertEquals(expectedSize, display.getPreferredSize());
    }

    @Test(expected = NullPointerException.class)
    public void testAddPointWithNull() {
        HuntDisplay display = new HuntDisplay();
        display.addPoint(null);
    }

    // Continue with other test cases for remaining methods and edge cases

}
