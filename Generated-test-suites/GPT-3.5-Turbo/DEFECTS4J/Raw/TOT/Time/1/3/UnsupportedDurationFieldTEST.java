import org.junit.BeforeClass;
import org.junit.Test;

import org.joda.time.DurationFieldType;

import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    private static UnsupportedDurationField unsupportedDurationField;

    @BeforeClass
    public static void setUp() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.years());
    }

    @Test
    public void testGetType() {
        assertEquals(DurationFieldType.years(), unsupportedDurationField.getType());
    }

    @Test
    public void testGetName() {
        assertEquals("years", unsupportedDurationField.getName());
    }

    @Test
    public void testIsSupported() {
        assertFalse(unsupportedDurationField.isSupported());
    }

    @Test
    public void testIsPrecise() {
        assertTrue(unsupportedDurationField.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        unsupportedDurationField.getValue(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        unsupportedDurationField.getValueAsLong(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        unsupportedDurationField.getValue(1000, 2000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        unsupportedDurationField.getValueAsLong(1000, 2000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithIntValue() {
        unsupportedDurationField.getMillis(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValue() {
        unsupportedDurationField.getMillis(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithIntValueAndInstant() {
        unsupportedDurationField.getMillis(1000, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValueAndInstant() {
        unsupportedDurationField.getMillis(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndIntValue() {
        unsupportedDurationField.add(1000L, 10);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndLongValue() {
        unsupportedDurationField.add(1000L, 10L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceWithMinuendInstantAndSubtrahendInstant() {
        unsupportedDurationField.getDifference(10000L, 5000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLongWithMinuendInstantAndSubtrahendInstant() {
        unsupportedDurationField.getDifferenceAsLong(10000L, 5000L);
    }

    @Test
    public void testGetUnitMillis() {
        assertEquals(0, unsupportedDurationField.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        DurationFieldMock durationFieldMock = new DurationFieldMock();
        assertTrue(unsupportedDurationField.compareTo(durationFieldMock) >= 1);
    }

    private static class DurationFieldMock extends DurationField {

        @Override
        public DurationFieldType getType() {
            return DurationFieldType.arbitraryType();
        }

        @Override
        public int getValue(long duration) {
            return 0;
        }

        @Override
        public long getValueAsLong(long duration) {
            return 0L;
        }

        @Override
        public int getValue(long duration, long instant) {
            return 0;
        }

        @Override
        public long getValueAsLong(long duration, long instant) {
            return 0L;
        }

        @Override
        public long getMillis(int value) {
            return 0L;
        }

        @Override
        public long getMillis(long value) {
            return 0L;
        }

        @Override
        public long getMillis(int value, long instant) {
            return 0L;
        }

        @Override
        public long getMillis(long value, long instant) {
            return 0L;
        }

        @Override
        public long add(long instant, int value) {
            return 0L;
        }

        @Override
        public long add(long instant, long value) {
            return 0L;
        }

        @Override
        public int getDifference(long minuendInstant, long subtrahendInstant) {
            return 0;
        }

        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            return 0L;
        }

        @Override
        public long getUnitMillis() {
            return 0L;
        }

        @Override
        public int compareTo(DurationField durationField) {
            return -1;
        }
    }
}