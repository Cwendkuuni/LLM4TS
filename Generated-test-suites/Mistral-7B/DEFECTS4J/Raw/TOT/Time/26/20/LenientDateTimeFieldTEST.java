import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ZoneIdent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class LenientDateTimeFieldTest {

    @Test
    public void testLenientDateTimeField() {
        // Test with BaseChronology (UTC)
        Chronology baseChrono = ChronologyUtils.UTC();
        DateTimeField field = DateTimeFieldType.dayOfWeek();
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, baseChrono);

        MutableDateTime datetime = new MutableDateTime();
        long currentValue = datetime.get(lenientField);

        // Test setting valid value
        datetime.set(lenientField, 1);
        assertEquals(1, datetime.get(lenientField));

        // Test setting out of bounds value
        datetime.set(lenientField, 8);
        assertEquals(1, datetime.get(lenientField)); // should wrap around to next week

        // Test setting negative value
        datetime.set(lenientField, -1);
        assertEquals(7, datetime.get(lenientField)); // should wrap around to previous week

        // Test setting out of bounds value with negative offset
        datetime.set(lenientField, -2);
        assertEquals(6, datetime.get(lenientField)); // should wrap around to previous week

        // Test with CustomChronology (America/New_York)
        Chronology customChrono = ChronologyUtils.forZone(ZoneIdent.forID("America/New_York"));
        DateTimeField lenientCustomField = LenientDateTimeField.getInstance(field, customChrono);

        MutableDateTime customDatetime = new MutableDateTime();
        long customCurrentValue = customDatetime.get(lenientCustomField);

        // Test setting valid value
        customDatetime.set(lenientCustomField, 1);
        assertEquals(1, customDatetime.get(lenientCustomField));

        // Test setting out of bounds value
        customDatetime.set(lenientCustomField, 8);
        assertEquals(1, customDatetime.get(lenientCustomField)); // should wrap around to next week

        // Test setting negative value
        customDatetime.set(lenientCustomField, -1);
        assertEquals(6, customDatetime.get(lenientCustomField)); // should wrap around to previous week

        // Test setting out of bounds value with negative offset in custom chronology
        customDatetime.set(lenientCustomField, -2);
        assertEquals(5, customDatetime.get(lenientCustomField)); // should wrap around to previous week
    }
}