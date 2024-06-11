import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDateTimeFieldVerifier;
import org.joda.time.MutableDateTime;
import org.joda.time.TimeZoneUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LenientDateTimeFieldTest {

    private DateTimeField field;
    private Chronology chronology;
    private LenientDateTimeField lenientField;

    @Before
    public void setUp() {
        field = DateTimeFieldType.dayOfMonth();
        chronology = TimeZoneUtils.getUTC();
        lenientField = LenientDateTimeField.getInstance(field, chronology);
    }

    @Test
    public void testGetInstance() {
        DateTimeField strictField = DateTimeFieldType.dayOfMonth();
        assertEquals(lenientField, LenientDateTimeField.getInstance(strictField, chronology));
        assertEquals(field, LenientDateTimeField.getInstance(lenientField, chronology));
        assertEquals(field, LenientDateTimeField.getInstance(lenientField, null));
        assertEquals(field, LenientDateTimeField.getInstance(null, chronology));
        assertEquals(field, LenientDateTimeField.getInstance(null, null));
    }

    @Test
    public void testIsLenient() {
        assertTrue(lenientField.isLenient());
        assertFalse(field.isLenient());
    }

    @Test
    public void testGetWrappedField() {
        assertEquals(field, lenientField.getWrappedField());
    }

    @Test
    public void testSetValidValue() {
        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 0, 0);
        MutableDateTime mutableDateTime = new MutableDateTime(dateTime);
        long instant = mutableDateTime.toInstant();

        lenientField.set(instant, 31);

        LocalDateTimeFieldVerifier verifier = new LocalDateTimeFieldVerifier(LocalDateTimeFieldVerifier.LENIENT, lenientField, LocalDateTimeFieldVerifier.DAY_OF_MONTH);
        assertEquals(new LocalDateTime(2022, 2, 1, 0, 0), verifier.verify(mutableDateTime));
        assertEquals(31, lenientField.get(instant));
    }

    @Test
    public void testSetOutOfBoundsValue() {
        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 0, 0);
        MutableDateTime mutableDateTime = new MutableDateTime(dateTime);
        long instant = mutableDateTime.toInstant();

        lenientField.set(instant, 32);

        LocalDateTimeFieldVerifier verifier = new LocalDateTimeFieldVerifier(LocalDateTimeFieldVerifier.LENIENT, lenientField, LocalDateTimeFieldVerifier.DAY_OF_MONTH);
        assertEquals(new LocalDateTime(2022, 2, 1, 0, 0), verifier.verify(mutableDateTime));
        assertEquals(1, lenientField.get(instant)); // LenientDateTimeField adds 1 to get the correct value
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithNullChronology() {
        lenientField.set(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithNullField() {
        lenientField.set(0, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWithInvalidValue() {
        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 0, 0);
        MutableDateTime mutableDateTime = new MutableDateTime(dateTime);
        long instant = mutableDateTime.toInstant();

        lenientField.set(instant, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWithValueGreaterThanMaximum() {
        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 0, 0);
        MutableDateTime mutableDateTime = new MutableDateTime(dateTime);
        long instant = mutableDateTime.toInstant();

        lenientField.set(instant, 33);
    }
}