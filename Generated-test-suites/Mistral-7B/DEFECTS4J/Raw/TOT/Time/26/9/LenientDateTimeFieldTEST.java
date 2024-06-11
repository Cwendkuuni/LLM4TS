import org.junit.Before;
import org.junit.Test;
import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.Minutes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LenientDateTimeFieldTest {

    private DateTimeZone utcZone;
    private DateTimeZone parisZone;
    private ISOChronology isoChronology;
    private LenientDateTimeField lenientMinutes;

    @Before
    public void setUp() {
        utcZone = DateTimeZone.UTC;
        parisZone = DateTimeZone.forID("Europe/Paris");
        isoChronology = new ISOChronology();
        lenientMinutes = LenientDateTimeField.getInstance(Minutes.getInstance(), isoChronology);
    }

    @Test
    public void testIsLenient() {
        assertTrue("LenientDateTimeField is lenient", lenientMinutes.isLenient());
    }

    @Test
    public void testGetInstanceWithNullField() {
        assertNull("LenientDateTimeField returns null for null field", LenientDateTimeField.getInstance(null, isoChronology));
    }

    @Test
    public void testGetInstanceWithStrictDateTimeField() {
        DateTimeField strictMinutes = Minutes.getInstance();
        DateTimeField lenientMinutes = LenientDateTimeField.getInstance(strictMinutes, isoChronology);
        assertNotNull("LenientDateTimeField is not null", lenientMinutes);
        assertTrue("New LenientDateTimeField is an instance of LenientDateTimeField", lenientMinutes instanceof LenientDateTimeField);
        assertEquals("LenientDateTimeField has the same ID as the original field", lenientMinutes.getID(), strictMinutes.getID());
        assertFalse("LenientDateTimeField is not the same instance as the original field", lenientMinutes == strictMinutes);
    }

    @Test
    public void testGetInstanceWithLenientDateTimeField() {
        DateTimeField lenientMinutes = LenientDateTimeField.getInstance(lenientMinutes, isoChronology);
        assertNotNull("LenientDateTimeField is not null", lenientMinutes);
        assertTrue("New LenientDateTimeField is an instance of LenientDateTimeField", lenientMinutes instanceof LenientDateTimeField);
        assertEquals("LenientDateTimeField has the same ID as the original field", lenientMinutes.getID(), lenientMinutes.getID());
        assertFalse("LenientDateTimeField is not the same instance as the original field", lenientMinutes == lenientMinutes);
    }

    @Test
    public void testSetWithValidValue() {
        LocalDateTime dateTime = new LocalDateTime(isoChronology, utcZone, 2022, 1, 1, 1, 0);
        long instant = dateTime.toInstance().getMillis();
        int value = 10;

        long newInstant = lenientMinutes.set(instant, value);
        LocalDateTime newDateTime = new LocalDateTime(isoChronology, utcZone, 2022, 1, 1, 11, 0);
        assertEquals("New instant matches the expected value", newInstant, newDateTime.toInstance().getMillis());
    }

    @Test
    public void testSetWithOutOfBoundsValue() {
        LocalDateTime dateTime = new LocalDateTime(isoChronology, utcZone, 2022, 1, 1, 1, 0);
        long instant = dateTime.toInstance().getMillis();
        int value = 120; // 2 hours out of bounds

        long newInstant = lenientMinutes.set(instant, value);
        LocalDateTime newDateTime = new LocalDateTime(isoChronology, utcZone, 2022, 1, 1, 3, 0);
        assertEquals("New instant matches the expected value", newInstant, newDateTime.toInstance().getMillis());
    }

    @Test
    public void testSetWithValidValueInParis() {
        LocalDateTime dateTime = new LocalDateTime(isoChronology, parisZone, 2022, 1, 1, 1, 0);
        long instant = dateTime.toInstance().getMillis();
        int value = 10;

        long newInstant = lenientMinutes.set(instant, value);
        LocalDateTime newDateTime = new LocalDateTime(isoChronology, parisZone, 2022, 1, 1, 11, 0);
        assertEquals("New instant matches the expected value in Paris timezone", newInstant, newDateTime.toInstance().getMillis());
    }

    @Test
    public void testSetWithOutOfBoundsValueInParis() {
        LocalDateTime dateTime = new LocalDateTime(isoChronology, parisZone, 2022, 1, 1, 1, 0);
        long instant = dateTime.toInstance().getMillis();
        int value = 120; // 2 hours out of bounds

        long newInstant = lenientMinutes.set(instant, value);
        LocalDateTime newDateTime = new LocalDateTime(isoChronology, parisZone, 2022, 1, 1, 3, 0);
        assertEquals("New instant matches the expected value in Paris timezone", newInstant, newDateTime.toInstance().getMillis());
    }
}