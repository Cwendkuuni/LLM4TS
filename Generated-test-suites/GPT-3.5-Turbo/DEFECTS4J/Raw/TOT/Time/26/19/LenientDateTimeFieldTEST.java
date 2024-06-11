import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.TimeZone;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    private Chronology chronology;
    private strictDateTimeField field;
    private DateTimeZone originalDateTimeZone;

    @Before
    public void setUp() {
        originalDateTimeZone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(DateTimeZone.forID("Etc/UTC"));
        chronology = DateTime.now().getChronology();
        field = new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), chronology);
    }

    @After
    public void tearDown() {
        DateTimeZone.setDefault(originalDateTimeZone);
    }

    @Test
    public void testGetInstanceWithNullField() {
        assertNull(LenientDateTimeField.getInstance(null, chronology));
    }

    @Test
    public void testGetInstanceWithLenientField() {
        DateTimeField lenientField = new LenientDateTimeField(field, chronology);
        assertEquals(lenientField, LenientDateTimeField.getInstance(lenientField, chronology));
    }

    @Test
    public void testGetInstanceWithStrictField() {
        assertEquals(field, LenientDateTimeField.getInstance(field, chronology));
    }

    @Test
    public void testGetInstanceWithNonLenientField() {
        DateTimeField nonLenientField = new NonLenientDateTimeField(field, chronology);
        DateTimeField lenientField = new LenientDateTimeField(nonLenientField, chronology);
        assertNotNull(LenientDateTimeField.getInstance(nonLenientField, chronology));
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testSetWithLenientField() {
        DateTimeField lenientField = new LenientDateTimeField(field, chronology);

        long instant = 1562066340000L; // 2nd July 2019, 01:59:00
        int value = 32;

        long newInstant = lenientField.set(instant, value);

        assertEquals(1564645140000L, newInstant); // 1st August 2019, 01:59:00
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWithStrictField() {
        field.set(1562066340000L, 32);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWithNonLenientField() {
        DateTimeField nonLenientField = new NonLenientDateTimeField(field, chronology);
        nonLenientField.set(1562066340000L, 32);
    }
}

class NonLenientDateTimeField extends DateTimeField {

    private static final long serialVersionUID = 1L;

    protected NonLenientDateTimeField(DateTimeFieldType fieldType, Chronology chronology) {
        super(fieldType, chronology);
    }

    @Override
    public long set(long instant, int value) {
        throw new IllegalArgumentException();
    }

    @Override
    public String getAsText(int fieldValue, Locale locale) {
        return null;
    }

    // other methods overridden as per requirements of DateTimeField
}