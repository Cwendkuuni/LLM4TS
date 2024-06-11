package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.DurationFieldType;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * JUnit test file for UnsupportedDurationField class.
 *
 * @author John Doe
 */
public class UnsupportedDurationFieldTest {

    private static UnsupportedDurationField field;

    @BeforeClass
    public static void setup() {
        field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    }

    @Test
    public void testGetInstance() {
        UnsupportedDurationField testField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertSame(field.getClass(), testField.getClass());
        assertNotSame(field, testField);
    }

    @Test
    public void testGetType() {
        DurationFieldType expectedType = DurationFieldType.SECONDS;
        DurationFieldType actualType = field.getType();
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testIsSupported() {
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        long duration = 3600000;
        field.getValue(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        long duration = 3600000;
        field.getValueAsLong(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAtInstant() {
        long duration = 3600000;
        long instant = System.currentTimeMillis();
        field.getValue(duration, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongAtInstant() {
        long duration = 3600000;
        long instant = System.currentTimeMillis();
        field.getValueAsLong(duration, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        int value = 3600;
        field.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAtInstant() {
        int value = 3600;
        long instant = System.currentTimeMillis();
        field.getMillis(value, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        long instant = System.currentTimeMillis();
        field.add(instant, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddLong() {
        long instant = System.currentTimeMillis();
        field.add(instant, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        long minuendInstant = System.currentTimeMillis() - 3600000;
        long subtrahendInstant = System.currentTimeMillis();
        field.getDifference(minuendInstant, subtrahendInstant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        long minuendInstant = System.currentTimeMillis() - 3600000;
        long subtrahendInstant = System.currentTimeMillis();
        field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    @Test
    public void testGetUnitMillis() {
        long expectedUnitMillis = 0;
        long actualUnitMillis = field.getUnitMillis();
        assertEquals(expectedUnitMillis, actualUnitMillis);
    }

    @Test
    public void testCompareTo() {
        DurationField durationField = DurationFieldType.MILLISECONDS;
        int expectedComparisonResult = 0;
        int actualComparisonResult = field.compareTo(durationField);
        assertEquals(expectedComparisonResult, actualComparisonResult);
    }

    @Test
    public void testEquals() {
        Object obj = new Object();
        assertFalse(field.equals(obj));

        UnsupportedDurationField anotherField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        String anotherName = "anotherName";
        UnsupportedDurationField anotherFieldWithName = new UnsupportedDurationField(DurationFieldType.MILLISECONDS, anotherName);

        assertFalse(field.equals(null));
        assertFalse(field.equals(anotherField));
        assertFalse(field.equals(anotherFieldWithName));

        assertFalse(anotherField.equals(field));
        assertFalse(anotherField.equals(anotherFieldWithName));

        assertFalse(anotherFieldWithName.equals(field));
        assertFalse(anotherFieldWithName.equals(anotherField));

        UnsupportedDurationField sameField = field;
        assertTrue(field.equals(sameField));
        assertTrue(sameField.equals(field));

        UnsupportedDurationField sameFieldWithName = new UnsupportedDurationField(DurationFieldType.SECONDS, getClass().getName());
        assertTrue(field.equals(sameFieldWithName));
        assertTrue(sameFieldWithName.equals(field));
    }

    @Test
    public void testHashCode() {
        int expectedHashCode = "UnsupportedDurationField[SECONDS]".hashCode();
        int actualHashCode = field.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testToString() {
        String expectedString = "UnsupportedDurationField[SECONDS]";
        String actualString = field.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField resolvedField = (UnsupportedDurationField) field.readResolve();
        assertSame(field, resolvedField);
    }

    @Test
    public void testUnsupportedOperationException() {
        UnsupportedOperationException expectedException = new UnsupportedOperationException("SECONDS field is unsupported");
        UnsupportedOperationException actualException = field.unsupported();
        assertSame(expectedException.getClass(), actualException.getClass());
        assertEquals(expectedException.getMessage(), actualException.getMessage());
    }
}