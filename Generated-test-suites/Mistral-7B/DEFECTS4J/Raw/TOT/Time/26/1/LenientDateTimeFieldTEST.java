import org.junit.Test;
import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ZoneUtils;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;

import static org.junit.Assert.assertEquals;

public class LenientDateTimeFieldTest {

    private static final Chronology UTC_CHRONOLOGY = Chronology.UTC;

    @Test
    public void testLenientDateTimeField() {
        DateTimeField baseField = new StrictDateTimeField(DateTimeFieldType.DAY_OF_YEAR, UTC_CHRONOLOGY);
        DateTimeField lenientField = LenientDateTimeField.getInstance(baseField, UTC_CHRONOLOGY);

        MutableDateTime dateTime = new MutableDateTime(ZoneUtils.toLocalMilliseconds(System.currentTimeMillis()), UTC_CHRONOLOGY);
        assertEquals(0, lenientField.get(dateTime.toLocalMilliseconds()));

        // Test setting a value within bounds
        lenientField.set(dateTime, 1);
        assertEquals(1, lenientField.get(dateTime.toLocalMilliseconds()));

        // Test setting a value out of bounds (below)
        lenientField.set(dateTime, -1);
        assertEquals(0, lenientField.get(dateTime.toLocalMilliseconds()));

        // Test setting a value out of bounds (above)
        lenientField.set(dateTime, 366);
        assertEquals(1, lenientField.get(dateTime.toLocalMilliseconds())); // Should roll over to next year

        // Test setting a value out of bounds (above, with a leap year)
        lenientField = LenientDateTimeField.getInstance(new StrictDateTimeField(DateTimeFieldType.DAY_OF_YEAR, new GregorianChronology()), UTC_CHRONOLOGY);
        dateTime = new LocalDateTime(2000, 1, 29, 0, 0, 0, 0, UTC_CHRONOLOGY);
        lenientField.set(dateTime, 366);
        assertEquals(1, lenientField.get(dateTime.toLocalMilliseconds())); // Should roll over to next year (with leap day)

        // Test setting a value with a negative instant
        MutableDateTime negativeDateTime = new MutableDateTime(-1, UTC_CHRONOLOGY);
        lenientField.set(negativeDateTime.toLocalMilliseconds(), 365);
        assertEquals(364, lenientField.get(negativeDateTime.toLocalMilliseconds())); // Should wrap around to previous year

        // Test setting a value with an invalid instant (NaN)
        MutableDateTime nanDateTime = new MutableDateTime(Double.NaN, UTC_CHRONOLOGY);
        lenientField.set(nanDateTime.toLocalMilliseconds(), 0);
        assertEquals(0, lenientField.get(nanDateTime.toLocalMilliseconds()));
    }
}