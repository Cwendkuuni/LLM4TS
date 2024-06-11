import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.MutableDateTime;
import org.joda.time.TestBase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class LenientDateTimeFieldTest extends TestBase {

    private DateTimeField field;
    private Chronology chronology;
    private LenientDateTimeField lenientField;

    @Before
    public void setUp() {
        field = new DateTimeField(DateTimeFieldType.SECOND_OF_MINUTE, 30, Chronology.UTC);
        chronology = Chronology.UTC;
        lenientField = LenientDateTimeField.getInstance(field, chronology);
    }

    // Test method: public static DateTimeField getInstance(DateTimeField field, Chronology base)
    @Test
    public void testGetInstance() {
        DateTimeField lenientFieldFromNull = LenientDateTimeField.getInstance(null, chronology);
        assertNull(lenientFieldFromNull);

        DateTimeField lenientFieldFromLenient = LenientDateTimeField.getInstance(lenientField, chronology);
        assertSame(lenientField, lenientFieldFromLenient);

        DateTimeField lenientFieldFromStrict = LenientDateTimeField.getInstance(new StrictDateTimeField(3, Chronology.UTC), chronology);
        assertNotNull(lenientFieldFromStrict);
        assertFalse(lenientFieldFromStrict.isLenient());
    }

    // Test method: protected LenientDateTimeField(DateTimeField field, Chronology base)
    @Test
    public void testConstructor() {
        assertNotNull(lenientField);
        assertSame(field, lenientField.getDelegate());
        assertSame(chronology, lenientField.iBase);
        assertTrue(lenientField.isLenient());
    }

    // Test method: public boolean isLenient()
    @Test
    public void testIsLenient() {
        assertTrue(lenientField.isLenient());
    }

    // Test method: public long set(long instant, int value)
    @Test
    public void testSet() {
        MutableDateTime dateTime = new MutableDateTime(1631966400000L, chronology);
        long newInstant = lenientField.set(dateTime.getMillis(), 100);
        assertEquals(1631966410000L, newInstant);

        // Test out of bounds
        MutableDateTime outOfBoundsDateTime = new MutableDateTime(-1, chronology);
        long outOfBoundsNewInstant = lenientField.set(outOfBoundsDateTime.getMillis(), 100);
        assertEquals(-999999999900L, outOfBoundsNewInstant);

        // Test null input
        assertThrows(NullPointerException.class, () -> lenientField.set(null, 100));

        // Test invalid input
        assertThrows(IllegalArgumentException.class, () -> lenientField.set(-1, 100));
    }
}