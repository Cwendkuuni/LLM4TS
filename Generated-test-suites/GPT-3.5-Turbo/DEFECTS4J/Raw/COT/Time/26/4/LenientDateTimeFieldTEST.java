import static org.junit.Assert.*;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    private LenientDateTimeField field;
    private Chronology base;
    private DateTimeField wrappedField;

    @Before
    public void setUp() {
        base = DateTimeZone.getDefault().withZone(UTCChronology.getInstance());
        wrappedField = base.year();
        field = new LenientDateTimeField(wrappedField, base);
    }

    @Test
    public void testIsLenient() {
        assertTrue(field.isLenient());
    }

    @Test
    public void testSet() {
        long instant = base.getDateTimeMillis(2021, 5, 15, 0, 0, 0, 0);
        int value = 10;

        long updatedInstant = field.set(instant, value);
        assertEquals(base.getDateTimeMillis(2021, 10, 15, 0, 0, 0, 0), updatedInstant);
    }

    @Test
    public void testGetInstanceWithBase() {
        DateTimeField strictField = new StrictDateTimeField(wrappedField, base);

        DateTimeField returnedField = LenientDateTimeField.getInstance(strictField, base);

        assertTrue(returnedField instanceof LenientDateTimeField);
        assertEquals(wrappedField, ((LenientDateTimeField)returnedField).getWrappedField());
    }

    @Test
    public void testGetInstanceWithNullField() {
        DateTimeField returnedField = LenientDateTimeField.getInstance(null, base);

        assertNull(returnedField);
    }

    @Test
    public void testGetInstanceWithLenientField() {
        DateTimeField returnedField = LenientDateTimeField.getInstance(wrappedField, base);

        assertEquals(wrappedField, returnedField);
    }

    @Test
    public void testGetInstanceWithStrictField() {
        DateTimeField strictField = new StrictDateTimeField(wrappedField, base);

        DateTimeField returnedField = LenientDateTimeField.getInstance(strictField, base);

        assertTrue(returnedField instanceof LenientDateTimeField);
        assertEquals(wrappedField, ((LenientDateTimeField)returnedField).getWrappedField());
    }

}