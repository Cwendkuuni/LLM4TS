import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    private LenientDateTimeField lenientDateTimeField;
    private Chronology baseChronology;

    @Before
    public void setUp() {
        baseChronology = DateTimeUtils.getChronology(DateTimeZone.UTC);
        lenientDateTimeField = new LenientDateTimeField(DateTimeFieldType.year(), baseChronology);
    }

    @After
    public void tearDown() {
        lenientDateTimeField = null;
        baseChronology = null;
    }

    @Test
    public void testIsLenient() {
        assertTrue(lenientDateTimeField.isLenient());
    }

    @Test
    public void testSet() {
        long instant = baseChronology.getDateTimeMillis(2021, 5, 15, 0, 0, 0, 0);
        int value = 10;
        long result = lenientDateTimeField.set(instant, value);
        assertEquals("Unexpected result", baseChronology.getDateTimeMillis(2021, 5, 25, 0, 0, 0, 0), result);

        // Edge cases
        try {
            lenientDateTimeField.set(instant, Integer.MAX_VALUE);
        } catch (IllegalFieldValueException e) {
            // Exception should be thrown for out of bounds value
            assertEquals("Exception message", "Value 2147483647 is too large for lenient field", e.getMessage());
        }

        try {
            lenientDateTimeField.set(instant, Integer.MIN_VALUE);
        } catch (IllegalFieldValueException e) {
            // Exception should be thrown for out of bounds value
            assertEquals("Exception message", "Value -2147483648 is too small for lenient field", e.getMessage());
        }
    }
}