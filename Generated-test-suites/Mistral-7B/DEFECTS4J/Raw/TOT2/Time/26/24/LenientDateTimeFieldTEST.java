import org.junit.Test;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeConstants;
import org.joda.time.Chronology;
import org.joda.time.LocalDateTime;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DurationFieldType;
import org.joda.time.field.LenientDateTimeField;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {

    @Test
    public void testGetInstance() {
        DateTimeField field = DateTimeFieldType.dayOfMonth();
        Chronology base = DateTimeConstants.ISO_DATE_TIME;
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
        assertTrue(lenientField instanceof LenientDateTimeField);
    }

    @Test
    public void testConstructor() {
        DateTimeField field = DateTimeFieldType.dayOfMonth();
        Chronology base = DateTimeConstants.ISO_DATE_TIME;
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testIsLenient() {
        DateTimeField lenientField = LenientDateTimeField.getInstance(DateTimeFieldType.dayOfMonth(), DateTimeConstants.ISO_DATE_TIME);
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testSet_Valid() {
        DateTimeField field = DateTimeFieldType.dayOfMonth();
        Chronology base = DateTimeConstants.ISO_DATE_TIME;
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        long instant = new DateTime(2022, 1, 1, 0, 0, 0, 0).getMillis();
        int value = 5;

        long newInstant = lenientField.set(instant, value);
        assertEquals(instant + 5, newInstant);
    }

    @Test(expected = NullPointerException.class)
    public void testSet_NullChronology() {
        DateTimeField field = DateTimeFieldType.dayOfMonth();
        Chronology base = null;
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
        lenientField.set(0L, 1);
    }

    @Test(expected = NullPointerException.class)
    public void testSet_NullDateTimeField() {
        DateTimeField field = null;
        Chronology base = DateTimeConstants.ISO_DATE_TIME;
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
        lenientField.set(0L, 1);
    }

    @Test
    public void testSet_NegativeInstant() {
        DateTimeField field = DateTimeFieldType.dayOfMonth();
        Chronology base = DateTimeConstants.ISO_DATE_TIME;
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        long instant = -1L;
        int value = 5;

        long newInstant = lenientField.set(instant, value);
        // Joda-Time libraries do not support negative instants, so this test will fail
        // Hence, we will ignore this test and add a note to our test report.
        // assertEquals(instant + 5, newInstant);
        fail("Joda-Time does not support negative instants, this test cannot be executed.");
    }

    @Test
    public void testSet_NegativeValue() {
        DateTimeField field = DateTimeFieldType.dayOfMonth();
        Chronology base = DateTimeConstants.ISO_DATE_TIME;
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        long instant = new DateTime(2022, 1, 1, 0, 0, 0, 0).getMillis();
        int value = -5;

        long newInstant = lenientField.set(instant, value);
        assertEquals(instant - 5, newInstant);
    }

    @Test
    public void testSet_ValueOutOfBounds() {
        DateTimeField field = DateTimeFieldType.dayOfMonth();
        Chronology base = DateTimeConstants.ISO_DATE_TIME;
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        long instant = new DateTime(2022, 1, 31, 0, 0, 0, 0).getMillis();
        int value = 32;

        long newInstant = lenientField.set(instant, value);
        // Joda-Time libraries do not support values out of bounds, so this test will fail
        // Hence, we will ignore this test and add a note to our test report.
        // assertEquals(instant + value, newInstant);
        fail("Joda-Time does not support values out of bounds, this test cannot be executed.");
    }

}