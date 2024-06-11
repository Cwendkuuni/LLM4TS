import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LenientDateTimeFieldTest {

    @Test
    public void testSet() {
        DateTime date = new DateTime(2021, 10, 15, 0, 0, ISOChronology.getInstance());
        LenientDateTimeField lenientField = new LenientDateTimeField(DateTimeFieldType.dayOfMonth(), date.getChronology());

        // Typical case
        long result = lenientField.set(date.getMillis(), 20);
        assertEquals(20, lenientField.get(result));

        // Edge case: out of bounds value
        result = lenientField.set(date.getMillis(), 40);
        assertEquals(40, lenientField.get(result));

        // Error scenario: null field
        lenientField = new LenientDateTimeField(null, date.getChronology());
        result = lenientField.set(date.getMillis(), 5);
        assertEquals(5, lenientField.get(result));
    }
}