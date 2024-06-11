import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.MutableLocalTime;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class LenientDateTimeFieldTest {

    @Test
    public void testGetInstance() {
        DateTimeField field = StrictDateTimeField.instance(DateTimeFieldType.dayOfMonth(), null);
        Chronology base = DateTimeConstants.UTC;
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
        assertNotNull(lenientField);
        assertEquals(LenientDateTimeField.class, lenientField.getClass());
        assertFalse(lenientField.isLenient());
        lenientField = LenientDateTimeField.getInstance(lenientField, base);
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testSet_validValues() {
        DateTimeField field = StrictDateTimeField.instance(DateTimeFieldType.dayOfMonth(), null);
        Chronology base = DateTimeConstants.UTC;
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        MutableDateTime dateTime = new MutableDateTime(0, base);
        long currentValue = lenientField.get(dateTime.toLocalMilliseconds());
        long newValue = currentValue + 1;
        long result = lenientField.set(dateTime.toLocalMilliseconds(), newValue);
        assertEquals(currentValue + 1, result);
    }

    @Test
    public void testSet_outOfBoundsValue() {
        DateTimeField field = StrictDateTimeField.instance(DateTimeFieldType.dayOfMonth(), null);
        Chronology base = DateTimeConstants.UTC;
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        MutableDateTime dateTime = new MutableDateTime(0, base);
        long currentValue = lenientField.get(dateTime.toLocalMilliseconds());
        long newValue = currentValue + DateTimeConstants.MAX_VALUE_DAYS;
        long result = lenientField.set(dateTime.toLocalMilliseconds(), newValue);
        assertEquals(currentValue + 1, result); // Addition occurs instead of setting
    }

    @Test
    public void testSet_nullField() {
        Chronology base = DateTimeConstants.UTC;
        assertThrows(NullPointerException.class, () -> LenientDateTimeField.getInstance(null, base));
    }

    @Test
    public void testSet_nullBase() {
        DateTimeField field = StrictDateTimeField.instance(DateTimeFieldType.dayOfMonth(), null);
        assertThrows(NullPointerException.class, () -> LenientDateTimeField.getInstance(field, null));
    }

    @Test
    public void testSet_incorrectBaseType() {
        DateTimeField field = StrictDateTimeField.instance(DateTimeFieldType.dayOfMonth(), LocalDate.class);
        Chronology base = DateTimeConstants.UTC;
        assertThrows(IllegalArgumentException.class, () -> LenientDateTimeField.getInstance(field, base));
    }
}