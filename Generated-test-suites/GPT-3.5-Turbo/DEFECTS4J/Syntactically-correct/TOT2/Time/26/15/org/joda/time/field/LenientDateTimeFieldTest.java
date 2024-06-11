import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.joda.time.DateTimeField;
import org.joda.time.chrono.GregorianChronology;

public class LenientDateTimeFieldTest {

    private LenientDateTimeField lenientDateTimeField;

    @Before
    public void setUp() {
        DateTimeField baseField = GregorianChronology.getInstance().hourOfDay();
        lenientDateTimeField = LenientDateTimeField.getInstance(baseField, GregorianChronology.getInstance());
    }

    @Test
    public void testIsLenient() {
        assertTrue(lenientDateTimeField.isLenient());
    }

    @Test
    public void testSet() {
        long instant = 1609459200000L; // January 1, 2021 12:00:00 AM UTC
        int value = 25;

        long result = lenientDateTimeField.set(instant, value);
        assertEquals(1610070000000L, result); // January 8, 2021 12:00:00 AM UTC
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetIllegalArgumentException() {
        long instant = 1609459200000L; // January 1, 2021 12:00:00 AM UTC
        int value = -1;

        lenientDateTimeField.set(instant, value);
    }

    @Test
    public void testGetInstant() {
        long instant = 1609459200000L; // January 1, 2021 12:00:00 AM UTC

        assertEquals(0, lenientDateTimeField.get(instant));
    }

    @Test
    public void testGetDurationField() {
        assertNotNull(lenientDateTimeField.getDurationField());
    }

    @Test
    public void testGetRangeDurationField() {
        assertNotNull(lenientDateTimeField.getRangeDurationField());
    }

    @Test
    public void testGetMinimumValue() {
        assertEquals(0, lenientDateTimeField.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        assertEquals(23, lenientDateTimeField.getMaximumValue());
    }
}