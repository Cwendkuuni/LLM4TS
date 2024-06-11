import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDateTimeFieldTestBase;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class LenientDateTimeFieldTest extends LocalDateTimeFieldTestBase {

    @Test
    public void testLenientDateTimeField() {
        Chronology base = ISOChronology.getInstance();
        DateTimeFieldType type = DateTimeFieldType.dayOfMonth();
        DateTimeField strictField = new StrictDateTimeField(type, base);
        DateTimeField lenientField = LenientDateTimeField.getInstance(strictField, base);

        // Test setting valid values
        LocalDateTime dateTime = new LocalDateTime(base, new MutableDateTime(2022, 1, 1, 12, 0, 0));
        assertEquals(1, lenientField.get(dateTime.toLocalMilliseconds()));
        lenientField.set(dateTime.toLocalMilliseconds(), 31);
        dateTime = new LocalDateTime(base, lenientField.get(new MutableDateTime(2022, 1, 1, 12, 0, 0)));
        assertEquals(31, lenientField.get(dateTime.toLocalMilliseconds()));

        // Test setting out of bounds values
        lenientField.set(dateTime.toLocalMilliseconds(), 32);
        dateTime = new LocalDateTime(base, lenientField.get(new MutableDateTime(2022, 1, 1, 12, 0, 0)));
        assertEquals(31, lenientField.get(dateTime.toLocalMilliseconds()));
        dateTime = new LocalDateTime(base, lenientField.get(new MutableDateTime(2022, 1, 1, 12, 0, 0).withFieldAdded(lenientField, 1)));
        assertEquals(32, lenientField.get(dateTime.toLocalMilliseconds()));

        // Test setting negative values
        lenientField.set(dateTime.toLocalMilliseconds(), -1);
        dateTime = new LocalDateTime(base, lenientField.get(new MutableDateTime(2022, 1, 1, 12, 0, 0)).withFieldAdded(lenientField, -1));
        assertEquals(30, lenientField.get(dateTime.toLocalMilliseconds()));

        // Test setting large values
        lenientField.set(dateTime.toLocalMilliseconds(), 365 * 2 + 1);
        dateTime = new LocalDateTime(base, lenientField.get(new MutableDateTime(2022, 1, 1, 12, 0, 0).withFieldAdded(lenientField, 365 * 2 + 1)));
        assertEquals(365 * 2 + 2, lenientField.get(dateTime.toLocalMilliseconds()));

        // Test equals and hashCode
        DateTimeField lenientField1 = LenientDateTimeField.getInstance(strictField, base);
        assertTrue(lenientField.equals(lenientField1));
        assertEquals(lenientField.hashCode(), lenientField1.hashCode());

        // Test adding and subtracting
        DateTimeField lenientField2 = LenientDateTimeField.getInstance(new StrictDateTimeField(DateTimeFieldType.hourOfDay(), base), base);
        assertEquals(3600, lenientField.getAddRemoveAmount(lenientField2, 1));
        assertEquals(-3600, lenientField.getAddRemoveAmount(lenientField2, -1));

        // Test getInstance
        DateTimeField lenientField3 = LenientDateTimeField.getInstance(lenientField, base);
        assertEquals(lenientField, lenientField3);

        // Test with different base
        Chronology base2 = JDKChronology.instance();
        DateTimeField lenientField4 = LenientDateTimeField.getInstance(strictField, base2);
        assertEquals(1, lenientField.get(new MutableDateTime(base, 2022, 1, 1, 12, 0, 0).toLocalMilliseconds()), 0.1);
        assertEquals(31, lenientField4.get(new MutableDateTime(base2, 2022, 1, 1, 12, 0, 0).toLocalMilliseconds()), 0.1);
        assertNotSame(lenientField, lenientField4);
    }

    @Test
    public void testSerialization() {
        Chronology base = ISOChronology.getInstance();
        DateTimeFieldType type = DateTimeFieldType.dayOfMonth();
        DateTimeField field = new StrictDateTimeField(type, base);
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);

        String json = JSON.toString(lenientField);
        DateTimeField lenientField2 = JSON.parse(json, DateTimeField.class);
        assertEquals(lenientField, lenientField2);
    }

    @Test
    public void testToString() {
        Chronology base = ISOChronology.getInstance();
        DateTimeFieldType type = DateTimeFieldType.dayOfMonth();
        DateTimeField field = new StrictDateTimeField(type, base);
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
        String expected = "LenientDateTimeField[type=" + type + ", base=" + base + "]";
        assertEquals(expected, lenientField.toString());
    }
}