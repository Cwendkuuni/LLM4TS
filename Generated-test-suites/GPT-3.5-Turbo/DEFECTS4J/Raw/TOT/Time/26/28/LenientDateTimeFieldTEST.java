import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.DateTimeZoneConverter;
import org.joda.time.convert.InstantConverter;
import org.joda.time.convert.UnsupportedConverter;

import static org.junit.Assert.*;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    @Test
    public void testGetInstance() {
        DateTimeField field = new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), new MockLenientChronology());
        Chronology base = new MockLenientChronology();
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
        assertNotNull("LenientDateTimeField.getInstance returned null", lenientField);
        assertTrue("LenientDateTimeField.getInstance returned non-lenient field", lenientField.isLenient());
    }

    @Test
    public void testIsLenient() {
        DateTimeField field = new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), new MockLenientChronology());
        Chronology base = new MockLenientChronology();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
        assertTrue("LenientDateTimeField.isLenient returned false", lenientField.isLenient());
    }

    @Test
    public void testSet() {
        DateTimeField field = new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), new MockLenientChronology());
        Chronology base = new MockLenientChronology();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        long instant = 1000L;
        int value = 20;
        long result = lenientField.set(instant, value);
        assertEquals("LenientDateTimeField.set returned unexpected result", instant + value, result);
    }

    @Test
    public void testSetWithOutOfBoundsValue() {
        DateTimeField field = new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), new MockLenientChronology());
        Chronology base = new MockLenientChronology();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        long instant = 1000L;
        int value = 50;
        long result = lenientField.set(instant, value);
        assertEquals("LenientDateTimeField.set did not perform the expected addition for an out-of-bounds value", instant + value, result);
    }

    @Test
    public void testSetWithNegativeValue() {
        DateTimeField field = new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), new MockLenientChronology());
        Chronology base = new MockLenientChronology();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        long instant = 1000L;
        int value = -10;
        long result = lenientField.set(instant, value);
        assertEquals("LenientDateTimeField.set did not perform the expected addition for a negative value", instant + value, result);
    }

    @Test
    public void testSetWithZeroValue() {
        DateTimeField field = new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), new MockLenientChronology());
        Chronology base = new MockLenientChronology();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        long instant = 1000L;
        int value = 0;
        long result = lenientField.set(instant, value);
        assertEquals("LenientDateTimeField.set did not perform the expected addition for a zero value", instant, result);
    }

    @Test
    public void testSetWithMaxValue() {
        DateTimeField field = new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), new MockLenientChronology());
        Chronology base = new MockLenientChronology();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        long instant = 1000L;
        int value = Integer.MAX_VALUE;
        long result = lenientField.set(instant, value);
        assertEquals("LenientDateTimeField.set returned unexpected result for max value", instant + value, result);
    }

    @Test
    public void testSetWithMinValue() {
        DateTimeField field = new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), new MockLenientChronology());
        Chronology base = new MockLenientChronology();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        long instant = 1000L;
        int value = Integer.MIN_VALUE;
        long result = lenientField.set(instant, value);
        assertEquals("LenientDateTimeField.set returned unexpected result for min value", instant + value, result);
    }    

}

class MockLenientChronology implements Chronology {

    @Override
    public DateTimeField getField(DateTimeFieldType type) {
        return null;
    }

    @Override
    public DateTimeZone getZone() {
        return DateTimeZone.UTC;
    }

    @Override
    public Chronology withUTC() {
        return null;
    }

    @Override
    public Chronology withZone(DateTimeZone zone) {
        return null;
    }

    @Override
    public InstantConverter getInstantConverter() {
        return null;
    }

    @Override
    public ConverterManager getConverterManager() {
        return null;
    }

    @Override
    public DateTimeZoneConverter getDateTimeZoneConverter() {
        return null;
    }

    @Override
    public UnsupportedConverter getUnsupportedDateTimeFieldAny() {
        return null;
    }

    @Override
    public UnsupportedConverter getUnsupportedDurationAny() {
        return null;
    }

    @Override
    public UnsupportedConverter getUnsupportedPeriodAny() {
        return null;
    }

}