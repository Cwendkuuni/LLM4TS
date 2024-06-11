package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupported, other);
    }

    @Test
    public void testGetInstanceNullCache() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertTrue(UnsupportedDurationField.cCache.containsKey(type));
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupported, other);
    }

    @Test
    public void testGetInstanceSameTypeDifferentName() {
        DurationFieldType type1 = new DurationFieldType() {
            @Override
            public String getName() {
                return "days";
            }
        };
        DurationFieldType type2 = new DurationFieldType() {
            @Override
            public String getName() {
                return "day";
            }
        };
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type2);
        assertFalse(unsupported1.equals(unsupported2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 1L;
        unsupported.getValue(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 1L;
        unsupported.getValueAsLong(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueLongInstant() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 1L;
        long instant = 1L;
        unsupported.getValue(duration, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongLongInstant() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 1L;
        long instant = 1L;
        unsupported.getValueAsLong(duration, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        int value = 1;
        unsupported.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long value = 1L;
        unsupported.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisIntInstant() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        int value = 1;
        long instant = 1L;
        unsupported.getMillis(value, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLongInstant() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long value = 1L;
        long instant = 1L;
        unsupported.getMillis(value, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long instant = 1L;
        int value = 1;
        unsupported.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long instant = 1L;
        long value = 1L;
        unsupported.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 1L;
        long subtrahendInstant = 1L;
        unsupported.getDifference(minuendInstant, subtrahendInstant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 1L;
        long subtrahendInstant = 1L;
        unsupported.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals(0, unsupported.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.DAYS;
        DurationFieldType type2 = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type2);
        assertEquals(1, unsupported1.compareTo(unsupported2));
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals(type.hashCode(), unsupported.hashCode());
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[days]", unsupported.toString());
    }

    @Test
    public void testEqualsNull() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertFalse(unsupported.equals(null));
    }

    @Test
    public void testEqualsSameInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertTrue(unsupported.equals(unsupported));
    }

    @Test
    public void testEqualsSameClassDifferentName() {
        DurationFieldType type1 = new DurationFieldType() {
            @Override
            public String getName() {
                return "days";
            }
        };
        DurationFieldType type2 = new DurationFieldType() {
            @Override
            public String getName() {
                return "day";
            }
        };
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type2);
        assertFalse(unsupported1.equals(unsupported2));
    }

    @Test
    public void testEqualsSameNameDifferentInstances() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type);
        assertFalse(unsupported1.equals(unsupported2));
    }

    @Test
    public void testResizeCache() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported3 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported4 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported5 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported6 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported7 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported8 = UnsupportedDurationField.getInstance(type);
        assertEquals(8, UnsupportedDurationField.cCache.size());
    }

    @Test
    public void testSerialize() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        // Serialize and deserialize
        Object readResolve = unsupported.readResolve();
        assertEquals(unsupported, readResolve);
    }

    @Test
    public void testUnsupportedOperationException() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        UnsupportedOperationException exception = unsupported.unsupported();
        assertEquals("days field is unsupported", exception.getMessage());
    }

}