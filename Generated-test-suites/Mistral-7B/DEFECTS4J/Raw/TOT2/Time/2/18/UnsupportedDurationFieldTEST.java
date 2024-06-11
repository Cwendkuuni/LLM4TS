package org.joda.time.field;

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.*;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @BeforeClass
    public static void init() {
        UnsupportedDurationField.cCache = new HashMap<DurationFieldType, UnsupportedDurationField>(7);
    }

    @AfterClass
    public static void cleanup() {
        UnsupportedDurationField.cCache = null;
    }

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertSame(field1, field2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInstanceNullType() {
        UnsupportedDurationField.getInstance(null);
    }

    @Test
    public void testGetInstanceSameTypeTwice() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertSame(field1, field2);
    }

    @Test
    public void testGetInstanceDifferentTypes() {
        DurationFieldType type1 = DurationFieldType.MILLISECONDS;
        DurationFieldType type2 = DurationFieldType.SECONDS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        assertNotSame(field1, field2);
    }

    @Test
    public void testGetInstanceConcurrentAccess() {
        final DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField field1 = null;
        UnsupportedDurationField field2 = null;

        new Thread(() -> {
            field1 = UnsupportedDurationField.getInstance(type);
        }).start();

        field2 = UnsupportedDurationField.getInstance(type);

        UnsupportedDurationField field3 = null;
        new Thread(() -> {
            field3 = UnsupportedDurationField.getInstance(type);
        }).start();

        UnsupportedDurationField field4 = UnsupportedDurationField.getInstance(type);

        assertSame(field1, field2);
        assertSame(field1, field3);
        assertSame(field1, field4);
    }

    @Test
    public void testGetValueThrowsUnsupportedOperationException() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        long duration = 1000L;
        try {
            field.getValue(duration);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testGetValueAsLongThrowsUnsupportedOperationException() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        long duration = 1000L;
        try {
            field.getValueAsLong(duration);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testGetValueNegativeDuration() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        long duration = -1000L;
        try {
            field.getValue(duration);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testGetValueLongDuration() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        long duration = Long.MAX_VALUE;
        try {
            field.getValue(duration);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testCompareToSelf() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        assertEquals(0, field1.compareTo(field2));
    }

    @Test
    public void testCompareToNull() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        assertEquals(0, field.compareTo(null));
    }

    @Test
    public void testCompareToSameType() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        assertEquals(0, field1.compareTo(field2));
    }

    @Test
    public void testCompareToDifferentTypes() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.SECONDS);
        assertEquals(0, field1.compareTo(field2));
    }
}