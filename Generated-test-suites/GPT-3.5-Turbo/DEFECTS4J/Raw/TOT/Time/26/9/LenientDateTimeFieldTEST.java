import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import static org.junit.Assert.*;
import org.junit.*;

public class LenientDateTimeFieldTest {

    private static Chronology chronology;
    private static DateTimeField dateTimeField;

    @BeforeClass
    public static void setup() {
        chronology = LenientChronology.getInstance(ISOChronology.getInstanceUTC());
        dateTimeField = chronology.dayOfMonth();
    }

    @AfterClass
    public static void tearDown() {
        chronology = null;
        dateTimeField = null;
    }

    @Test
    public void testGetInstanceForNullField() {
        assertNull(LenientDateTimeField.getInstance(null, chronology));
    }
    
    @Test
    public void testGetInstanceForLenientField() {
        assertEquals(dateTimeField, LenientDateTimeField.getInstance(dateTimeField, chronology));
    }
    
    @Test
    public void testGetInstanceForStrictField() {
        StrictDateTimeField strictDateTimeField = new StrictDateTimeField(dateTimeField);
        assertEquals(dateTimeField, LenientDateTimeField.getInstance(strictDateTimeField, chronology));
    }

    @Test
    public void testIsLenient() {
        assertTrue(dateTimeField.isLenient());
    }

    @Test
    public void testSetWithNormalValue() {
        long instant = chronology.set(2022, 12, 25, 0, 0, 0, 0);
        instant = dateTimeField.set(instant, 10);
        assertEquals(chronology.set(2022, 12, 10, 0, 0, 0, 0), instant);
    }

    @Test
    public void testSetWithOutOfBoundsValue() {
        long instant = chronology.set(2022, 12, 25, 0, 0, 0, 0);
        instant = dateTimeField.set(instant, 35);  // Maximum day in month is 31
        assertEquals(chronology.set(2023, 1, 4, 0, 0, 0, 0), instant);  // Overflow to next month
    }

    @Test
    public void testSetWithNegativeValue() {
        long instant = chronology.set(2022, 12, 25, 0, 0, 0, 0);
        instant = dateTimeField.set(instant, -5);  // Minimum day in month is 1
        assertEquals(chronology.set(2022, 12, 1, 0, 0, 0, 0), instant);  // Underflow to previous month
    }

    @Test
    public void testSetWithZeroValue() {
        long instant = chronology.set(2022, 12, 25, 0, 0, 0, 0);
        instant = dateTimeField.set(instant, 0);  // Minimum day in month is 1
        assertEquals(chronology.set(2022, 12, 1, 0, 0, 0, 0), instant);  // Underflow to previous month
    }
}