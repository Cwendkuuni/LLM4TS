import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.LenientDateTimeField;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {
    private LenientDateTimeField lenientDateTimeField;

    @Before
    public void setUp() {
        DateTimeField baseField = ISOChronology.getInstance().secondOfMinute();
        lenientDateTimeField = new LenientDateTimeField(baseField, ISOChronology.getInstance());
    }

    @After
    public void tearDown() {
        lenientDateTimeField = null;
    }

    @Test
    public void testIsLenient() {
        assertTrue(lenientDateTimeField.isLenient());
    }

    @Test
    public void testSet() {
        long instant = DateTimeUtils.currentTimeMillis();
        int value = 10;
        long newInstant = lenientDateTimeField.set(instant, value);

        assertEquals(baseField.set(instant, value), lenientDateTimeField.get(instant));
        assertEquals(iBase.getZone().convertLocalToUTC(instant, false), newInstant);
    }

    @Test
    public void testSet_invalidValue() {
        long instant = DateTimeUtils.currentTimeMillis();
        int value = 60; // invalid value for secondOfMinute field
        long newInstant = lenientDateTimeField.set(instant, value);

        assertEquals(baseField.get(instant), lenientDateTimeField.get(instant));
        assertEquals(iBase.getZone().convertLocalToUTC(instant, false), newInstant);
    }

    @Test
    public void testSetMaxValue() {
        long instant = DateTimeUtils.currentTimeMillis();
        int maxValue = lenientDateTimeField.getMaximumValue();
        long newInstant = lenientDateTimeField.set(instant, maxValue);

        assertEquals(baseField.set(instant, maxValue), lenientDateTimeField.get(instant));
        assertEquals(iBase.getZone().convertLocalToUTC(instant, false), newInstant);
    }

    @Test
    public void testSetMinValue() {
        long instant = DateTimeUtils.currentTimeMillis();
        int minValue = lenientDateTimeField.getMinimumValue();
        long newInstant = lenientDateTimeField.set(instant, minValue);

        assertEquals(baseField.set(instant, minValue), lenientDateTimeField.get(instant));
        assertEquals(iBase.getZone().convertLocalToUTC(instant, false), newInstant);
    }

    @Test
    public void testSetWithOverflow() {
        long instant = DateTimeUtils.currentTimeMillis();
        int value = 70; // value will cause overflow in secondOfMinute field
        long newInstant = lenientDateTimeField.set(instant, value);

        assertEquals(baseField.add(instant, value), lenientDateTimeField.get(instant));
        assertEquals(iBase.getZone().convertLocalToUTC(instant, false), newInstant);
    }

    @Test
    public void testSetWithUnderflow() {
        long instant = DateTimeUtils.currentTimeMillis();
        int value = -10; // value will cause underflow in secondOfMinute field
        long newInstant = lenientDateTimeField.set(instant, value);

        assertEquals(baseField.add(instant, value), lenientDateTimeField.get(instant));
        assertEquals(iBase.getZone().convertLocalToUTC(instant, false), newInstant);
    }
}