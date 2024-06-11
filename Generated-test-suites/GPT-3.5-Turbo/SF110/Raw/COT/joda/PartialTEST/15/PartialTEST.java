import org.joda.time.DateTimeFieldType;
import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class PartialTest {

    @Test
    public void testPartialDefaultConstructor() {
        Partial partial = new Partial();
        assertEquals(null, partial.getChronology());
        assertEquals(0, partial.size());
    }

    @Test
    public void testPartialChronologyConstructor() {
        Chronology chronology = DateTimeUtils.getChronology(null).withUTC();
        Partial partial = new Partial(chronology);
        assertEquals(chronology, partial.getChronology());
        assertEquals(0, partial.size());
    }

    @Test
    public void testPartialDateTimeFieldTypeConstructor() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        int value = 1;
        Partial partial = new Partial(fieldType, value);
        assertEquals(null, partial.getChronology());
        assertEquals(1, partial.size());
        assertEquals(fieldType, partial.getFieldType(0));
        assertEquals(value, partial.getValue(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPartialDateTimeFieldTypeNullConstructor() {
        DateTimeFieldType fieldType = null;
        int value = 1;
        Partial partial = new Partial(fieldType, value);
    }

    @Test
    public void testPartialDateTimeFieldTypeChronologyConstructor() {
        Chronology chronology = DateTimeUtils.getChronology(null).withUTC();
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        int value = 1;
        Partial partial = new Partial(fieldType, value, chronology);
        assertEquals(chronology, partial.getChronology());
        assertEquals(1, partial.size());
        assertEquals(fieldType, partial.getFieldType(0));
        assertEquals(value, partial.getValue(0));
    }

    // TODO: Add test cases for other constructors and methods

}