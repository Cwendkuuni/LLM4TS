import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.LenientDateTimeField;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LenientDateTimeFieldTest {

    @Test
    public void testGetInstance() {
        DateTimeField field = ISOChronology.getInstanceUTC().year();

        // Test case 1: When a lenient field is provided, it should return the same field
        DateTimeField result1 = LenientDateTimeField.getInstance(field, ISOChronology.getInstanceUTC());
        assertEquals(field, result1);

        // Test case 2: When a non-lenient field is provided, it should return a new LenientDateTimeField
        DateTimeField result2 = LenientDateTimeField.getInstance(field.withStrict(), ISOChronology.getInstanceUTC());
        assertEquals(true, result2 instanceof LenientDateTimeField);

    }

    @Test
    public void testSet() {
        Chronology chrono = ISOChronology.getInstanceUTC();
        DateTimeField baseField = chrono.minuteOfDay();

        // Test case 1: Setting a value within range, the new instant should be returned correctly
        DateTimeField field1 = LenientDateTimeField.getInstance(baseField, chrono);
        DateTime dateTime1 = new DateTime(2020, 1, 1, 0, 0, chrono);
        long result1 = field1.set(dateTime1.getMillis(), 300);
        assertEquals(dateTime1.getMillis() + 300 * 60 * 1000, result1);

        // Test case 2: Setting a value smaller than field range, the new instant should be returned correctly
        DateTimeField field2 = LenientDateTimeField.getInstance(baseField, chrono);
        DateTime dateTime2 = new DateTime(2020, 1, 1, 0, 0, chrono);
        long result2 = field2.set(dateTime2.getMillis(), -500);
        assertEquals(dateTime2.getMillis() + 860 * 60 * 1000 - 500 * 60 * 1000, result2);

        // Test case 3: Setting a value larger than field range, the new instant should be returned correctly
        DateTimeField field3 = LenientDateTimeField.getInstance(baseField, chrono);
        DateTime dateTime3 = new DateTime(2020, 1, 1, 0, 0, chrono);
        long result3 = field3.set(dateTime3.getMillis(), 1440);
        assertEquals(dateTime3.getMillis() + 1440 * 60 * 1000 - 24 * 60 * 60 * 1000, result3);

        // Test case 4: Setting a value far beyond field range, the new instant should be returned correctly
        DateTimeField field4 = LenientDateTimeField.getInstance(baseField, chrono);
        DateTime dateTime4 = new DateTime(2020, 1, 1, 0, 0, chrono);
        long result4 = field4.set(dateTime4.getMillis(), 10000);
        assertEquals(dateTime4.getMillis() + 10000 * 60 * 1000 - 24 * 60 * 60 * 1000, result4);

    }
}