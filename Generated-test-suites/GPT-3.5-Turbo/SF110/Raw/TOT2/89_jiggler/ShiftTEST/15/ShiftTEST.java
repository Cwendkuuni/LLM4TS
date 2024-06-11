import org.junit.Test;
import static org.junit.Assert.*;

public class ShiftTest {

    @Test
    public void testShiftConstructor() {
        Shift shift = new Shift(3, 5, 1);
        assertEquals(3, shift.xoffset);
        assertEquals(5, shift.yoffset);
        assertTrue(shift.wrap);
    }

    @Test
    public void testEdgeCaseWrapNoWrap() {
        Shift shift = new Shift(2, 2, Shift.NOWRAP);
        assertFalse(shift.wrap);
    }

    @Test
    public void testEdgeCaseNegativeOffsets() {
        Shift shift = new Shift(-3, -4, Shift.WRAP);
        assertEquals(-3, shift.xoffset);
        assertEquals(-4, shift.yoffset);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeOffsets() {
        Shift shift = new Shift(-1, -1, Shift.WRAP);
    }

    // Add more test cases for other methods as needed

}