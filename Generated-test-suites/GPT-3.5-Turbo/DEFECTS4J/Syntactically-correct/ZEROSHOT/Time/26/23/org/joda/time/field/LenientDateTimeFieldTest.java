import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.LocalDate;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.field.LenientDateTimeField;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {

    private LenientDateTimeField lenientField;
    private DateTimeField baseField;
    private Chronology baseChronology;

    @Before
    public void setUp() {
        baseField = new BaseDateTimeField(DateTimeFieldType.dayOfMonth(), 1);
        baseChronology = new BaseChronology();
        lenientField = new LenientDateTimeField(baseField, baseChronology);
    }

    @Test
    public void testIsLenient() {
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testSet() {
        long instant = new LocalDate(2022, 1, 1).toDateTimeAtStartOfDay().getMillis();
        int value = 40;
        long result = lenientField.set(instant, value);
        assertEquals(instant + value * 24 * 60 * 60 * 1000, result);
    }

    @Test
    public void testSetBoundaryValue() {
        long instant = DateTimeZone.UTC.convertLocalToUTC(0, false);
        int value = 1;
        long result = lenientField.set(instant, value);
        assertEquals(24 * 60 * 60 * 1000, result);
    }

    @Test
    public void testSetOutOfBoundsValue() {
        long instant = DateTimeZone.UTC.convertLocalToUTC(0, false);
        int value = 40;
        long result = lenientField.set(instant, value);
        assertEquals(40 * 24 * 60 * 60 * 1000, result);
    }

    @Test
    public void testSetNegativeValue() {
        long instant = new LocalDate(2022, 1, 1).toDateTimeAtStartOfDay().getMillis();
        int value = -10;
        long result = lenientField.set(instant, value);
        assertEquals(instant + value * 24 * 60 * 60 * 1000, result);
    }

    @Test
    public void testSetZeroValue() {
        long instant = new LocalDate(2022, 1, 1).toDateTimeAtStartOfDay().getMillis();
        int value = 0;
        long result = lenientField.set(instant, value);
        assertEquals(instant, result);
    }
}