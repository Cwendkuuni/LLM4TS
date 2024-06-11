package org.joda.time.field; // Import necessary packages

import org.junit.Assert;
import org.junit.Test;

import static org.joda.time.DurationFieldType.*;

/**
 * Test class for UnsupportedDurationField.
 */
public class UnsupportedDurationFieldTest {

    

    /**
     * Test method for {@link UnsupportedDurationField#getInstance(DurationFieldType)}
     */
    @Test
    public void testGetInstance() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(MILLISECONDS);
        Assert.assertNotNull(field);
        Assert.assertEquals(MILLISECONDS, field.getType());
    }

    /**
     * Test method for {@link UnsupportedDurationField#getType()}
     */
    @Test
    public void testGetType() {
        UnsupportedDurationField field = new UnsupportedDurationField(MILLISECONDS);
        Assert.assertEquals(MILLISECONDS, field.getType());
    }

    /**
     * Test method for {@link UnsupportedDurationField#getName()}
     */
    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(HOURS);
        Assert.assertEquals("hours", field.getName());
    }

    /**
     * Test method for {@link UnsupportedDurationField#isSupported()}
     */
    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = new UnsupportedDurationField(MINUTES);
        Assert.assertFalse(field.isSupported());
    }

    /**
     * Test method for {@link UnsupportedDurationField#isPrecise()}
     */
    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = new UnsupportedDurationField(SECONDS);
        Assert.assertTrue(field.isPrecise());
    }

    /**
     * Test method for {@link UnsupportedDurationField#getValue(long)} and {@link UnsupportedOperationException}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testValue() {
        UnsupportedDurationField field = new UnsupportedDurationField(MICROS);
        field.getValue(1L);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getValueAsLong(long)} and {@link UnsupportedOperationException}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testValueAsLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DAYS);
        field.getValueAsLong(1L);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getValue(long, long)} and {@link UnsupportedOperationException}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testValueWithInstant() {
        UnsupportedDurationField field = new UnsupportedDurationField(WEEKS);
        field.getValue(1L, 1L);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getValueAsLong(long, long)} and {@link UnsupportedOperationException}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testValueAsLongWithInstant() {
        UnsupportedDurationField field = new UnsupportedDurationField(YEARS);
        field.getValueAsLong(1L, 1L);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getMillis(int)} and {@link UnsupportedOperationException}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField field = new UnsupportedDurationField(MONTHS);
        field.getMillis(1);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getMillis(long)} and {@link UnsupportedOperationException}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DAY_OF_WEEK);
        field.getMillis(1L);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getMillis(int, long)} and {@link UnsupportedOperationException}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstant() {
        UnsupportedDurationField field = new UnsupportedDurationField(DAY_OF_YEAR);
        field.getMillis(1, 1L);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getMillis(long, long)} and {@link UnsupportedOperationException}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLongWithInstant() {
        UnsupportedDurationField field = new UnsupportedDurationField(MIDNIGHTS);
        field.getMillis(1L, 1L);
    }

    /**
     * Test method for {@link UnsupportedDurationField#add(long, int)} and {@link UnsupportedOperationException}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField field = new UnsupportedDurationField(INSTANT);
        field.add(1L, 1);
    }

    /**
     * Test method for {@link UnsupportedDurationField#add(long, long)} and {@link UnsupportedOperationException}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(MILLISECONDS_PER_DAY);
        field.add(1L, 1L);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getDifference(long, long)} and {@link UnsupportedOperationException}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = new UnsupportedDurationField(MILLISECONDS_PER_MINUTE);
        field.getDifference(1L, 1L);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getDifferenceAsLong(long, long)} and {@link UnsupportedOperationException}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(MILLISECONDS_PER_HOUR);
        field.getDifferenceAsLong(1L, 1L);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getUnitMillis()}
     */
    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = new UnsupportedDurationField(CENTISECONDS);
        Assert.assertEquals(0, field.getUnitMillis());
    }

    /**
     * Test method for {@link UnsupportedDurationField#compareTo(DurationField)}
     */
    @Test
    public void testCompareTo() {
        UnsupportedDurationField f1 = new UnsupportedDurationField(MILLISECONDS);
        UnsupportedDurationField f2 = new UnsupportedDurationField(MILLISECONDS);
        Assert.assertEquals(0, f1.compareTo(f2));
    }

    /**
     * Test method for {@link UnsupportedDurationField#equals(Object)}
     */
    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(MILLISECONDS);
        UnsupportedDurationField field2 = new UnsupportedDurationField(MILLISECONDS);
        Assert.assertTrue(field1.equals(field2));

        Object nullObject = null;
        Assert.assertFalse(field1.equals(nullObject));

        Object anotherObject = new Object();
        Assert.assertFalse(field1.equals(anotherObject));

        UnsupportedDurationField field3 = new UnsupportedDurationField(SECONDS);
        Assert.assertFalse(field1.equals(field3));
    }

    /**
     * Test method for {@link UnsupportedDurationField#hashCode()}
     */
    @Test
    public void testHashCode() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(MILLISECONDS);
        UnsupportedDurationField field2 = new UnsupportedDurationField(MILLISECONDS);
        Assert.assertEquals(field1.hashCode(), field2.hashCode());
    }

    /**
     * Test method for {@link UnsupportedDurationField#toString()}
     */
    @Test
    public void testToString() {
        UnsupportedDurationField field = new UnsupportedDurationField(MILLISECONDS);
        String expected = "UnsupportedDurationField[milliseconds]";
        Assert.assertEquals(expected, field.toString());
    }

    /**
     * Test method for {@link UnsupportedDurationField#readResolve()}
     */
    @Test
    public void testReadResolve() {
        UnsupportedDurationField field = new UnsupportedDurationField(MILLISECONDS);
        UnsupportedDurationField resolvedField = (UnsupportedDurationField) field.readResolve();
        Assert.assertEquals(field.getType(), resolvedField.getType());
    }

    
}