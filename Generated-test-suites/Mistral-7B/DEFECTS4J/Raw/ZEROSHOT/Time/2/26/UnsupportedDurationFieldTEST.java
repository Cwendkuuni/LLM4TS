import org.joda.time.BaseTestCase;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedDurationField;

public class UnsupportedDurationFieldTest extends BaseTestCase {

    @org.junit.Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField dur1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField dur2 = UnsupportedDurationField.getInstance(type);
        assertEquals(dur1, dur2);
    }

    @org.junit.Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField dur = UnsupportedDurationField.getInstance(type);
        assertEquals(DurationFieldType.DAYS.getName(), dur.getName());
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField dur = UnsupportedDurationField.getInstance(type);
        assertFalse(dur.isSupported());
    }

    @org.junit.Test
    public void testIsPrecise() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField dur = UnsupportedDurationField.getInstance(type);
        assertTrue(dur.isPrecise());
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testgetValue() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField dur = UnsupportedDurationField.getInstance(type);
        dur.getValue(1L);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testgetValueAsLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField dur = UnsupportedDurationField.getInstance(type);
        dur.getValueAsLong(1L);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testgetValueAsLongInstant() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField dur = UnsupportedDurationField.getInstance(type);
        dur.getValueAsLong(1L, 0L);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testgetValueInstant() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField dur = UnsupportedDurationField.getInstance(type);
        dur.getValue(1L, 0L);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testgetMillisValue() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField dur = UnsupportedDurationField.getInstance(type);
        dur.getMillis(1);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testgetMillisValueLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField dur = UnsupportedDurationField.getInstance(type);
        dur.getMillis(1L);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testgetMillisValueInstant() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField dur = UnsupportedDurationField.getInstance(type);
        dur.getMillis(1L, 0L);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testgetMillisValueInstantLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField dur = UnsupportedDurationField.getInstance(type);
        dur.getMillis(1L, 1L);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testadd() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField dur = UnsupportedDurationField.getInstance(type);
        dur.add(0L, 1);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testaddLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField dur = UnsupportedDurationField.getInstance(type);
        dur.add(0L, 1L);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testgetDifference() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField dur1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField dur2 = UnsupportedDurationField.getInstance(type);
        dur1.getDifference(0L, 0L);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testgetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField dur1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField dur2 = UnsupportedDurationField.getInstance(type);
        dur1.getDifferenceAsLong(0L, 0L);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testgetUnitMillis() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField dur = UnsupportedDurationField.getInstance(type);
        dur.getUnitMillis();
    }

    @org.junit.Test
    public void testcompareTo() {
        DurationFieldType type1 = DurationFieldType.DAYS;
        DurationFieldType type2 = DurationFieldType.MINUTES;
        UnsupportedDurationField dur1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField dur2 = UnsupportedDurationField.getInstance(type2);
        assertEquals(0, dur1.compareTo(dur2));
    }

    @org.junit.Test
    public void testEquals() {
        DurationFieldType type1 = DurationFieldType.DAYS;
        DurationFieldType type2 = DurationFieldType.MINUTES;
        UnsupportedDurationField dur1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField dur2 = UnsupportedDurationField.getInstance(type2);
        assertFalse(dur1.equals(dur2));

        DurationFieldType type3 = DurationFieldType.DAYS;
        UnsupportedDurationField dur3 = UnsupportedDurationField.getInstance(type1);
        assertEquals(dur1, dur3);
    }

    @org.junit.Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField dur = UnsupportedDurationField.getInstance(type);
        int hashcode = dur.hashCode();
        assertEquals(hashcode, dur.hashCode());
    }

    @org.junit.Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField dur = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[DAYS]", dur.toString());
    }
}