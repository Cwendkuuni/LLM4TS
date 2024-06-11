import org.junit.Test;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

import static org.junit.Assert.assertEquals;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.MILLIS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField another = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupported, another);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsSupported() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLIS);
        unsupported.isSupported();
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLIS);
        assertEquals("Millis", unsupported.getName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLIS);
        unsupported.getValue(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLIS);
        unsupported.getValueAsLong(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLIS);
        unsupported.getValue(1L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLIS);
        unsupported.getValueAsLong(1L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLIS);
        unsupported.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLIS);
        unsupported.getMillis(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueInstant() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLIS);
        unsupported.getMillis(1L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLIS);
        unsupported.add(1L, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstant() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLIS);
        unsupported.add(1L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLIS);
        unsupported.getDifference(1L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLIS);
        unsupported.getDifferenceAsLong(1L, 1L);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLIS);
        assertEquals(0, unsupported.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField unsupported1 = new UnsupportedDurationField(DurationFieldType.MILLIS);
        UnsupportedDurationField unsupported2 = new UnsupportedDurationField(DurationFieldType.SECONDS);
        assertEquals(1, unsupported1.compareTo(unsupported2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField unsupported1 = new UnsupportedDurationField(DurationFieldType.MILLIS);
        UnsupportedDurationField unsupported2 = new UnsupportedDurationField(DurationFieldType.MILLIS);
        assertEquals(unsupported1, unsupported2);
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLIS);
        int hashCode = unsupported.hashCode();
        UnsupportedDurationField another = new UnsupportedDurationField(DurationFieldType.MILLIS);
        assertEquals(hashCode, another.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLIS);
        assertEquals("UnsupportedDurationField[Millis]", unsupported.toString());
    }

}