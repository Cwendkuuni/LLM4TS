import org.junit.Test;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        LenientDateTimeField field = new LenientDateTimeField(null, null);
        assertTrue(field.isLenient());
    }

    @Test
    public void testSet() {
        LenientDateTimeField field = new LenientDateTimeField(null, null);
        long instant = 0;
        int value = 10;
        assertEquals(10, field.set(instant, value));
    }

    @Test
    public void testGetInstance() {
        LenientDateTimeField field = new LenientDateTimeField(null, null);
        assertNull(LenientDateTimeField.getInstance(null, null));
    }

    @Test
    public void testSetOutOfBounds() {
        LenientDateTimeField field = new LenientDateTimeField(null, null);
        long instant = 0;
        int value = 100; // Out of bounds value
        assertEquals(100, field.set(instant, value));
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullTimeZone() {
        LenientDateTimeField field = new LenientDateTimeField(null, null);
        long instant = 0;
        int value = 10;
        field.set(instant, value);
    }

}