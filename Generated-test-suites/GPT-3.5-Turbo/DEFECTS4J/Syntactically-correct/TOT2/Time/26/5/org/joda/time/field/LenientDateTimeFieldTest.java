import static org.junit.Assert.*;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    @Test
    public void testGetInstance_nullField() {
        assertNull(LenientDateTimeField.getInstance(null, null));
    }

    @Test
    public void testGetInstance_alreadyLenientField() {
        DateTimeField field = new BaseDateTimeField();
        assertTrue(LenientDateTimeField.getInstance(field, null).isLenient());
    }

    @Test
    public void testGetInstance_strictField() {
        DateTimeField field = new StrictDateTimeField();
        assertFalse(LenientDateTimeField.getInstance(field, null).isLenient());
    }

    @Test
    public void testIsLenient() {
        DateTimeField field = new BaseDateTimeField();
        assertTrue(new LenientDateTimeField(field, null).isLenient());
    }

    @Test
    public void testSet() {
        DateTimeField field = new BaseDateTimeField();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, null);
        long instant = 1000;
        int value = 5;
        assertEquals(1005, lenientField.set(instant, value));
    }

    @Test
    public void testSet_edgeCase() {
        DateTimeField field = new BaseDateTimeField();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, null);
        long instant = Long.MAX_VALUE;
        int value = 1;
        assertEquals(Long.MAX_VALUE, lenientField.set(instant, value));
    }

    @Test
    public void testSet_exceptionHandling() {
        DateTimeField field = new BaseDateTimeField();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, null);
        long instant = 1000;
        int value = 2000;
        assertEquals(0, lenientField.set(instant, value));
    }
}