import org.junit.Test;
import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.joda.time.field.ZoneLength;

import static org.junit.Assert.assertEquals;

public class LenientDateTimeFieldTest {

    @Test
    public void testLenientDateTimeField() {
        Chronology chrono = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSSS").getChronology();
        DateTimeField field = DateTimeFieldType.millisOfDay();
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, chrono);

        // Test valid value
        MutableDateTime dt = new MutableDateTime(1590928000000L, chrono);
        assertEquals(1, lenientField.get(dt.toLocalMilliseconds()));
        lenientField.set(dt, 2);
        assertEquals(2, lenientField.get(dt.toLocalMilliseconds()));

        // Test minimum value
        MutableDateTime minValue = new MutableDateTime(Long.MIN_VALUE, chrono);
        lenientField.set(minValue, Integer.MIN_VALUE);
        assertEquals(Long.MIN_VALUE + lenientField.getPast(minValue, ZoneLength.ONE), minValue.getMilliseconds());

        // Test maximum value
        MutableDateTime maxValue = new MutableDateTime(Long.MAX_VALUE, chrono);
        lenientField.set(maxValue, Integer.MAX_VALUE);
        assertEquals(Long.MAX_VALUE + lenientField.getPast(maxValue, ZoneLength.ONE), maxValue.getMilliseconds());

        // Test out of bounds value
        MutableDateTime outOfBoundsValue = new MutableDateTime(1234567890123L, chrono);
        lenientField.set(outOfBoundsValue, 10000);
        assertEquals(1234567890123L + lenientField.getPast(outOfBoundsValue, ZoneLength.ONE), outOfBoundsValue.getMilliseconds());

        // Test negative value
        MutableDateTime negativeValue = new MutableDateTime(Long.MIN_VALUE + 1000, chrono);
        lenientField.set(negativeValue, -100);
        assertEquals(Long.MIN_VALUE + lenientField.getPast(negativeValue, ZoneLength.ONE), negativeValue.getMilliseconds());

        // Test wrap around value
        MutableDateTime wrapAroundValue = new MutableDateTime(Long.MIN_VALUE - 1000, chrono);
        lenientField.set(wrapAroundValue, Long.MAX_VALUE);
        assertEquals(Long.MIN_VALUE + lenientField.getPast(wrapAroundValue, ZoneLength.ONE), wrapAroundValue.getMilliseconds());

        // Test get and set with StrictDateTimeField
        DateTimeField strictField = StrictDateTimeField.getInstance(DateTimeFieldType.millisOfDay(), chrono);
        LenientDateTimeField lenientStrictField = LenientDateTimeField.getInstance(strictField, chrono);
        MutableDateTime strictDt = new MutableDateTime(1590928000000L, chrono);
        lenientStrictField.set(strictDt, 3);
        assertEquals(3, lenientStrictField.get(strictDt.toLocalMilliseconds()));
    }
}