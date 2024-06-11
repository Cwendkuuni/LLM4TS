import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.ZoneInfo;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link LenientDateTimeField}.
 */
@RunWith(JUnit4.class)
public class LenientDateTimeFieldTest {

    private static final Chronology ISO_CHRONO = Chronology.getInstance("ISO");

    /**
     * Tests set method with valid values and different chronologies.
     */
    @Test
    public void testSet_validValue_differentChronologies() {
        DateTimeField field = LenientDateTimeField.getInstance(DateTimeFieldType.DAY_OF_MONTH, ISO_CHRONO);

        // Test with UTC
        MutableDateTime datetime = new MutableDateTime();
        datetime.setDateTime(2023, 1, 1, 0, 0, 0, 0);
        long utcInstant = datetime.getLong();
        int value = 5;
        long expected = utcInstant + 4L;
        long actual = ((LenientDateTimeField) field).set(utcInstant, value);
        assertEquals(expected, actual);

        // Test with America/New_York
        ZoneInfo zone = ZoneInfo.getSystemDefault();
        MutableLocalDateTime localDateTime = new MutableLocalDateTime();
        localDateTime.set(2023, 1, 1, 0, 0, 0, 0);
        long localInstant = localDateTime.getLong();
        long actualZoneInstant = ((LenientDateTimeField) field).set(localInstant, value, zone.getUTC());
        Instant expectedZoneInstant = new BaseDateTime(expected, zone).toInstant();
        assertEquals(expectedZoneInstant.getMillis(), actualZoneInstant);

        // Test with Europe/London
        MutableLocalDateTime europeDateTime = new MutableLocalDateTime();
        europeDateTime.set(2023, 1, 1, 0, 0, 0, 0);
        localInstant = europeDateTime.getLong();
        long actualEuropeInstant = ((LenientDateTimeField) field).set(localInstant, value, zone);
        Instant expectedEuropeInstant = new BaseDateTime(expected + (zone.getOffset(2023, 1, 1, 0, 0, 0) - zone.getOffset(2023, 1, 1, 0, 0, 0)), zone).toInstant();
        assertEquals(expectedEuropeInstant.getMillis(), actualEuropeInstant);
    }

    /**
     * Tests set method with valid values and the same chronology.
     */
    @Test
    public void testSet_validValue_sameChronology() {
        DateTimeField field = LenientDateTimeField.getInstance(DateTimeFieldType.DAY_OF_MONTH, ISO_CHRONO);

        MutableDateTime datetime = new MutableDateTime();
        datetime.setDateTime(2023, 1, 1, 0, 0, 0, 0);
        long utcInstant = datetime.getLong();
        int value = 5;
        long expected = utcInstant + 4L;
        long actual = ((LenientDateTimeField) field).set(utcInstant, value);
        assertEquals(expected, actual);
    }

    /**
     * Tests set method with negative values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSet_negativeValue() {
        DateTimeField field = LenientDateTimeField.getInstance(DateTimeFieldType.DAY_OF_MONTH, ISO_CHRONO);

        MutableDateTime datetime = new MutableDateTime();
        datetime.setDateTime(2023, 1, 1, 0, 0, 0, 0);
        long utcInstant = datetime.getLong();
        int value = -1;
        ((LenientDateTimeField) field).set(utcInstant, value);
    }

    /**
     * Tests set method with out of bounds values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSet_outOfBoundsValue() {
        DateTimeField field = LenientDateTimeField.getInstance(DateTimeFieldType.DAY_OF_MONTH, ISO_CHRONO);

        MutableDateTime datetime = new MutableDateTime();
        datetime.setDateTime(2023, 1, 31, 0, 0, 0, 0);
        long utcInstant = datetime.getLong();
        int value = 32;
        ((LenientDateTimeField) field).set(utcInstant, value);
    }

    /**
     * Tests set method with valid values and the same field.
     */
    @Test
    public void testSet_validValue_sameField() {
        DateTimeField field = LenientDateTimeField.getInstance(DateTimeFieldType.DAY_OF_MONTH, ISO_CHRONO);
        DateTimeField fieldCopy = LenientDateTimeField.getInstance(DateTimeFieldType.DAY_OF_MONTH, ISO_CHRONO);

        MutableDateTime datetime = new MutableDateTime();
        datetime.setDateTime(2023, 1, 1, 0, 0, 0, 0);
        long utcInstant = datetime.getLong();
        int value = 5;
        long expected = utcInstant + 4L;
        long actual = ((LenientDateTimeField) field).set(utcInstant, value);
        assertEquals(expected, actual);

        // Verify that the field and the copied field have the same behavior
        long actualFieldCopyInstant = ((LenientDateTimeField) fieldCopy).set(actual, 0);
        assertEquals(expected, actualFieldCopyInstant);
        assertNotSame(field, fieldCopy);
    }

    /**
     * Tests isLenient method.
     */
    @Test
    public void testIsLenient() {
        DateTimeField field = LenientDateTimeField.getInstance(DateTimeFieldType.DAY_OF_MONTH, ISO_CHRONO);
        assertTrue(field.isLenient());
    }

    /**
     * Tests getInstance method with different chronologies.
     */
    @Test
    public void testGetInstance_differentChronologies() {
        DateTimeField fieldUTC = LenientDateTimeField.getInstance(DateTimeFieldType.DAY_OF_MONTH, ISO_CHRONO);
        DateTimeField fieldAmerica = LenientDateTimeField.getInstance(DateTimeFieldType.DAY_OF_MONTH, Chronology.getAmericaNewYorkUTC());

        assertNotSame(fieldUTC, fieldAmerica);
    }

    /**
     * Tests getInstance method with the same chronology.
     */
    @Test
    public void testGetInstance_sameChronology() {
        DateTimeField field1 = LenientDateTimeField.getInstance(DateTimeFieldType.DAY_OF_MONTH, ISO_CHRONO);
        DateTimeField field2 = LenientDateTimeField.getInstance(DateTimeFieldType.DAY_OF_MONTH, ISO_CHRONO);

        assertSame(field1, field2);
    }

    /**
     * Tests getInstance method with a null field.
     */
    @Test(expected = NullPointerException.class)
    public void testGetInstance_nullField() {
        LenientDateTimeField.getInstance(null, ISO_CHRONO);
    }

    /**
     * Tests getInstance method with a null base.
     */
    @Test(expected = NullPointerException.class)
    public void testGetInstance_nullBase() {
        LenientDateTimeField.getInstance(DateTimeFieldType.DAY_OF_MONTH, null);
    }

    /**
     * Tests getWrappedField method.
     */
    @Test
    public void testGetWrappedField() {
        DateTimeField lenientField = LenientDateTimeField.getInstance(DateTimeFieldType.DAY_OF_MONTH, ISO_CHRONO);
        StrictDateTimeField strictField = new StrictDateTimeField(DateTimeFieldType.DAY_OF_MONTH, ISO_CHRONO);

        assertEquals(strictField, lenientField.getWrappedField());
    }
}