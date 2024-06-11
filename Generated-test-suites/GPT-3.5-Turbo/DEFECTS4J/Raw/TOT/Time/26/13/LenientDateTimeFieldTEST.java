import org.joda.time.*;
import org.joda.time.chrono.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {

    private LenientDateTimeField lenientDateTimeField;
    private Chronology chronology;

    @Before
    public void setUp() {
        chronology = ISOChronology.getInstanceUTC();
        lenientDateTimeField = new LenientDateTimeField(chronology.year(), chronology);
    }

    @Test
    public void testIsLenient() {
        assertTrue(lenientDateTimeField.isLenient());
    }

    @Test
    public void testGetters() {
        assertEquals(Chronology.year(), lenientDateTimeField.getWrappedField());
        assertEquals(chronology, lenientDateTimeField.getChronology());
        assertEquals(chronology.year(), lenientDateTimeField.getType());
    }

    @Test
    public void testSet_ValidValue() {
        long instant = 1557801600000L; // Wednesday, May 15, 2019 12:00:00 AM GMT
        int newValue = 2020;

        long result = lenientDateTimeField.set(instant, newValue);

        long expected = 1589932800000L; // Sunday, May 19, 2020 12:00:00 AM GMT
        assertEquals(expected, result);
    }

    @Test
    public void testSet_OutOfBoundsValue() {
        long instant = 1557801600000L; // Wednesday, May 15, 2019 12:00:00 AM GMT
        int outOfBoundsValue = 3000;

        long result = lenientDateTimeField.set(instant, outOfBoundsValue);

        long expected = 1589932800000L; // Sunday, May 19, 2020 12:00:00 AM GMT
        assertEquals(expected, result);
    }

    @Test
    public void testSet_InvalidInstant() {
        long invalidInstant = 1557801600000L; // Wednesday, May 15, 2019 12:00:00 AM GMT
        int newValue = 2020;

        try {
            lenientDateTimeField.set(invalidInstant, newValue);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid instant", e.getMessage());
        }
    }

    @Test
    public void testSet_NullField() {
        long instant = 1557801600000L; // Wednesday, May 15, 2019 12:00:00 AM GMT
        int newValue = 2020;
        lenientDateTimeField = new LenientDateTimeField(null, chronology);

        try {
            lenientDateTimeField.set(instant, newValue);
            fail("Expected a NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals("DateTimeField must not be null", e.getMessage());
        }
    }

    @Test
    public void testGetInstance_LenientField() {
        DateTimeField field = chronology.year();

        DateTimeField result = LenientDateTimeField.getInstance(field, chronology);

        assertSame(field, result);
    }

    @Test
    public void testGetInstance_StrictField() {
        DateTimeField field = chronology.yearOfEra();

        DateTimeField result = LenientDateTimeField.getInstance(field, chronology);

        assertNotSame(field, result);
        assertTrue(result instanceof LenientDateTimeField);
    }

}