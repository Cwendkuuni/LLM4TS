import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertEquals(DurationFieldType.years(), field.getType());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertEquals(DurationFieldType.months().getName(), field.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        field.getValue(100L);
        fail("UnsupportedOperationException should have been thrown");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getValueAsLong(100L);
        fail("UnsupportedOperationException should have been thrown");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        field.getValue(100L, 200L);
        fail("UnsupportedOperationException should have been thrown");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.micros());
        field.getValueAsLong(100L, 200L);
        fail("UnsupportedOperationException should have been thrown");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithIntValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.nanos());
        field.getMillis(100);
        fail("UnsupportedOperationException should have been thrown");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        field.getMillis(100L);
        fail("UnsupportedOperationException should have been thrown");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithIntValueAndInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        field.getMillis(100, 200L);
        fail("UnsupportedOperationException should have been thrown");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValueAndInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        field.getMillis(100L, 200L);
        fail("UnsupportedOperationException should have been thrown");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndIntValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.add(100L, 200);
        fail("UnsupportedOperationException should have been thrown");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndLongValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        field.add(100L, 200L);
        fail("UnsupportedOperationException should have been thrown");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.micros());
        field.getDifference(100L, 200L);
        fail("UnsupportedOperationException should have been thrown");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        field.getDifferenceAsLong(100L, 200L);
        fail("UnsupportedOperationException should have been thrown");
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        assertEquals(0L, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        DurationField otherField = new DurationField() {

            @Override
            public DurationFieldType getType() {
                return DurationFieldType.months();
            }

            @Override
            public String getName() {
                return "Custom Months";
            }

            @Override
            public boolean isSupported() {
                return true;
            }

            @Override
            public boolean isPrecise() {
                return true;
            }

            @Override
            public int getValue(long duration) {
                return 0;
            }

            @Override
            public long getValueAsLong(long duration) {
                return 0;
            }

            @Override
            public int getValue(long duration, long instant) {
                return 0;
            }

            @Override
            public long getValueAsLong(long duration, long instant) {
                return 0;
            }

            @Override
            public long getMillis(int value) {
                return 0;
            }

            @Override
            public long getMillis(long value) {
                return 0;
            }

            @Override
            public long getMillis(int value, long instant) {
                return 0;
            }

            @Override
            public long getMillis(long value, long instant) {
                return 0;
            }

            @Override
            public long add(long instant, int value) {
                return 0;
            }

            @Override
            public long add(long instant, long value) {
                return 0;
            }

            @Override
            public int getDifference(long minuendInstant, long subtrahendInstant) {
                return 0;
            }

            @Override
            public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
                return 0;
            }

            @Override
            public long getUnitMillis() {
                return 0;
            }

            @Override
            public int compareTo(DurationField durationField) {
                if (durationField.isSupported()) {
                    return -1;
                }
                return 0;
            }
        };

        assertEquals(1, field.compareTo(otherField));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.months());

        assertTrue(field1.equals(field1));
        assertTrue(field1.equals(field2));
        assertTrue(field2.equals(field1));
        assertFalse(field1.equals(field3));
        assertFalse(field3.equals(field1));
        assertFalse(field1.equals(null));
        assertFalse(field1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertEquals(DurationFieldType.days().getName().hashCode(), field.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertEquals("UnsupportedDurationField[hours]", field.toString());
    }
}