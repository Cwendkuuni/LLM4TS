package org.joda.time.field.test;

import org.joda.time.BaseTestCase;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@RunWith(Parameterized.class)
public class UnsupportedDurationFieldTest extends BaseTestCase {

    private DurationFieldType type;
    private UnsupportedDurationField field;

    public UnsupportedDurationFieldTest(DurationFieldType type) {
        this.type = type;
    }

    @Parameterized.Parameters(name = "{index}: UnsupportedDurationField for type={0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {DurationFieldType.dayOfMonth()},
                {DurationFieldType.hourOfDay()},
                {DurationFieldType.minuteOfHour()},
                {DurationFieldType.secondOfMinute()},
                {DurationFieldType.millisOfSecond()},
                {DurationFieldType.years()},
                {DurationFieldType.weeks()},
                {DurationFieldType.days()},
                {DurationFieldType.months()},
                {DurationFieldType.weeksPerYear()},
                {DurationFieldType.daysFraction()}
        });
    }

    @Test
    public void testGetType() {
        field = UnsupportedDurationField.getInstance(type);
        assertEquals(type, field.getType());
    }

    @Test
    public void testGetName() {
        field = UnsupportedDurationField.getInstance(type);
        assertEquals(type.getName(), field.getName());
    }

    @Test
    public void testIsSupported() {
        field = UnsupportedDurationField.getInstance(type);
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        field = UnsupportedDurationField.getInstance(type);
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        field = UnsupportedDurationField.getInstance(type);
        field.getValue(0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueInt() {
        field = UnsupportedDurationField.getInstance(type);
        field.getValue(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueLong() {
        field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisInt() {
        field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLong() {
        field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisIntLong() {
        field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLongLong() {
        field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddLong() {
        field = UnsupportedDurationField.getInstance(type);
        field.add(1L, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddLongLong() {
        field = UnsupportedDurationField.getInstance(type);
        field.add(1L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceInt() {
        field = UnsupportedDurationField.getInstance(type);
        field.getDifference(0L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceLong() {
        field = UnsupportedDurationField.getInstance(type);
        field.getDifferenceAsLong(0L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetUnitMillis() {
        field = UnsupportedDurationField.getInstance(type);
        field.getUnitMillis();
    }

    @Test
    public void testCompareTo() {
        field = UnsupportedDurationField.getInstance(type);
        DurationField durationField = DurationFieldType.millisOfSecond();
        assertEquals(0, field.compareTo(durationField));
    }

    @Test
    public void testEquals() {
        field = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField otherField = UnsupportedDurationField.getInstance(type);
        assertTrue(field.equals(otherField));
    }

    @Test
    public void testHashCode() {
        field = UnsupportedDurationField.getInstance(type);
        assertEquals(type.hashCode(), field.hashCode());
    }

    @Test
    public void testToString() {
        field = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[" + type.getName() + ']', field.toString());
    }

    @Test
    public void testResolve() {
        field = UnsupportedDurationField.getInstance(type);
        assertEquals(field, field.readResolve());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupported() {
        field = new UnsupportedDurationField(type);
        field.unsupported();
    }
}