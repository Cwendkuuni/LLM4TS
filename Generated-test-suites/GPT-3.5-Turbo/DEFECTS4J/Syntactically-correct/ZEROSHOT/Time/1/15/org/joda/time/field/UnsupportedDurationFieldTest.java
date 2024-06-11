import static org.junit.Assert.*;
import java.io.Serializable;
import java.util.HashMap;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Before;
import org.junit.Test;

public class UnsupportedDurationFieldTest implements Serializable {

    private UnsupportedDurationField unsupportedDurationField;
    private DurationFieldType durationFieldType;

    @Before
    public void setup() {
        durationFieldType = DurationFieldType.minutes();
        unsupportedDurationField = UnsupportedDurationField.getInstance(durationFieldType);
    }

    @Test
    public void testGetType() {
        assertEquals(durationFieldType, unsupportedDurationField.getType());
    }

    @Test
    public void testGetName() {
        assertEquals(durationFieldType.getName(), unsupportedDurationField.getName());
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
    public void testGetValue_long() {
        unsupportedDurationField.getValue(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long() {
        unsupportedDurationField.getValueAsLong(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long_long() {
        unsupportedDurationField.getValue(1000L, 1234L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long_long() {
        unsupportedDurationField.getValueAsLong(1000L, 1234L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int() {
        unsupportedDurationField.getMillis(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long() {
        unsupportedDurationField.getMillis(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int_long() {
        unsupportedDurationField.getMillis(1000, 1234L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long_long() {
        unsupportedDurationField.getMillis(1000L, 1234L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_int() {
        unsupportedDurationField.add(1000L, 1234);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_long() {
        unsupportedDurationField.add(1000L, 1234L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference_long_long() {
        unsupportedDurationField.getDifference(1000L, 1234L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong_long_long() {
        unsupportedDurationField.getDifferenceAsLong(1000L, 1234L);
    }

    @Test
    public void testGetUnitMillis() {
        assertEquals(0L, unsupportedDurationField.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        DurationField supportedDurationField = new DurationField() {
            public DurationFieldType getType() {
                return durationFieldType;
            }
            public String getName() {
                return durationFieldType.getName();
            }
            public boolean isSupported() {
                return true;
            }
            public boolean isPrecise() {
                return true;
            }
            public int getValue(long paramLong) {
                return 0;
            }
            public long getValueAsLong(long paramLong) {
                return 0L;
            }
            public int getValue(long paramLong1, long paramLong2) {
                return 0;
            }
            public long getValueAsLong(long paramLong1, long paramLong2) {
                return 0L;
            }
            public long getMillis(int paramInt) {
                return 0L;
            }
            public long getMillis(long paramLong) {
                return 0L;
            }
            public long getMillis(int paramInt, long paramLong) {
                return 0L;
            }
            public long getMillis(long paramLong1, long paramLong2) {
                return 0L;
            }
            public long add(long paramLong, int paramInt) {
                return 0L;
            }
            public long add(long paramLong1, long paramLong2) {
                return 0L;
            }
            public int getDifference(long paramLong1, long paramLong2) {
                return 0;
            }
            public long getDifferenceAsLong(long paramLong1, long paramLong2) {
                return 0L;
            }
            public long getUnitMillis() {
                return 0L;
            }
            public int compareTo(DurationField paramDurationField) {
                return 0;
            }
        };

        assertEquals(1, unsupportedDurationField.compareTo(supportedDurationField));
        assertEquals(0, unsupportedDurationField.compareTo(unsupportedDurationField));
    }

    @Test
    public void testEquals() {
        DurationField supportedDurationField = new DurationField() {
            public DurationFieldType getType() {
                return durationFieldType;
            }
            public String getName() {
                return durationFieldType.getName();
            }
            public boolean isSupported() {
                return true;
            }
            public boolean isPrecise() {
                return true;
            }
            public int getValue(long paramLong) {
                return 0;
            }
            public long getValueAsLong(long paramLong) {
                return 0L;
            }
            public int getValue(long paramLong1, long paramLong2) {
                return 0;
            }
            public long getValueAsLong(long paramLong1, long paramLong2) {
                return 0L;
            }
            public long getMillis(int paramInt) {
                return 0L;
            }
            public long getMillis(long paramLong) {
                return 0L;
            }
            public long getMillis(int paramInt, long paramLong) {
                return 0L;
            }
            public long getMillis(long paramLong1, long paramLong2) {
                return 0L;
            }
            public long add(long paramLong, int paramInt) {
                return 0L;
            }
            public long add(long paramLong1, long paramLong2) {
                return 0L;
            }
            public int getDifference(long paramLong1, long paramLong2) {
                return 0;
            }
            public long getDifferenceAsLong(long paramLong1, long paramLong2) {
                return 0L;
            }
            public long getUnitMillis() {
                return 0L;
            }
            public int compareTo(DurationField paramDurationField) {
                return 0;
            }
        };

        assertFalse(unsupportedDurationField.equals(null));
        assertTrue(unsupportedDurationField.equals(unsupportedDurationField));
        assertFalse(unsupportedDurationField.equals(durationFieldType));
        assertFalse(unsupportedDurationField.equals(supportedDurationField));
    }

    @Test
    public void testHashCode() {
        assertEquals(durationFieldType.getName().hashCode(), unsupportedDurationField.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("UnsupportedDurationField[" + durationFieldType.getName() + ']', unsupportedDurationField.toString());
    }

    @Test
    public void testReadResolve() {
        assertEquals(unsupportedDurationField, unsupportedDurationField.readResolve());
    }

    @Test
    public void testUnsupported() {
        UnsupportedOperationException exception = new UnsupportedOperationException(durationFieldType + " field is unsupported");
        assertEquals(exception.getMessage(), unsupportedDurationField.unsupported().getMessage());
    }

}