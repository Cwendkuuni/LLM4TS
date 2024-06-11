import static org.junit.Assert.*;

import org.junit.Test;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.chrono.LenientChronology;

public class LenientDateTimeFieldTest {

    @Test
    public void testGetInstance_withNullField_returnsNull() {
        DateTimeField result = LenientDateTimeField.getInstance(null, null);
        assertNull(result);
    }

    @Test
    public void testGetInstance_withLenientField_returnsSameField() {
        Chronology base = new LenientChronology();
        DateTimeField field = base.secondOfMinute();
        DateTimeField result = LenientDateTimeField.getInstance(field, base);
        assertEquals(field, result);
    }

    @Test
    public void testGetInstance_withStrictField_returnsLenientField() {
        Chronology base = new LenientChronology();
        DateTimeField field = base.dayOfMonth();
        DateTimeField result = LenientDateTimeField.getInstance(new StrictDateTimeField(field), base);
        assertTrue(result instanceof LenientDateTimeField);
    }

    @Test
    public void testSet_withValidValue_returnsUpdatedValue() {
        Chronology base = new LenientChronology();
        DateTimeField field = base.secondOfMinute();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
        DateTime dateTime = new DateTime(2021, 1, 1, 0, 0); // Midnight on Jan 1, 2021
        long initialInstant = dateTime.getMillis();
        long updatedInstant = lenientField.set(initialInstant, 30); // Set to 30 seconds
        DateTime updatedDateTime = new DateTime(updatedInstant);
        assertEquals(30, updatedDateTime.getSecondOfMinute());
    }

    @Test
    public void testSet_withOutOfBoundsValue_returnsAdjustedValue() {
        Chronology base = new LenientChronology();
        DateTimeField field = base.secondOfMinute();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
        DateTime dateTime = new DateTime(2021, 1, 1, 0, 0); // Midnight on Jan 1, 2021
        long initialInstant = dateTime.getMillis();
        long updatedInstant = lenientField.set(initialInstant, 70); // Set to 70 seconds (out of bounds)
        DateTime updatedDateTime = new DateTime(updatedInstant);
        assertEquals(10, updatedDateTime.getSecondOfMinute()); // 70 % 60 = 10
    }

    @Test
    public void testIsLenient_returnsTrue() {
        Chronology base = new LenientChronology();
        DateTimeField field = base.secondOfMinute();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
        assertTrue(lenientField.isLenient());
    }

}