import org.junit.Test;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals(type, unsupported.getType());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertFalse(unsupported.isSupported());
    }

    @Test
    public void testGetValueWithLongArgument() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValue(1L));
    }

    @Test
    public void testGetValueWithIntArgument() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValue(1));
    }

    @Test
    public void testGetValueAsLongWithLongArgument() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValueAsLong(1L));
    }

    @Test
    public void testGetValueAsLongWithIntArgument() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValueAsLong(1));
    }

    @Test
    public void testGetMillisWithIntArgument() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(1));
    }

    @Test
    public void testGetMillisWithLongArgument() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(1L));
    }

    @Test
    public void testGetMillisWithIntArgumentAndLongInstant() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(1, 1L));
    }

    @Test
    public void testGetMillisWithLongArgumentAndLongInstant() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(1L, 1L));
    }

    @Test
    public void testAddWithLongArgumentAndLongInstant() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.add(1L, 1L));
    }

    @Test
    public void testAddWithIntArgumentAndLongInstant() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.add(1, 1L));
    }

    @Test
    public void testGetDifferenceWithLongArguments() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getDifference(1L, 1L));
    }

    @Test
    public void testGetDifferenceAsLongWithLongArguments() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getDifferenceAsLong(1L, 1L));
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField unsupported1 = new UnsupportedDurationField(DurationFieldType.DAYS);
        UnsupportedDurationField unsupported2 = new UnsupportedDurationField(DurationFieldType.HOURS);
        assertEquals(1, unsupported1.compareTo(unsupported2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField unsupported1 = new UnsupportedDurationField(DurationFieldType.DAYS);
        UnsupportedDurationField unsupported2 = new UnsupportedDurationField(DurationFieldType.DAYS);
        UnsupportedDurationField unsupported3 = new UnsupportedDurationField(DurationFieldType.HOURS);
        assertEquals(unsupported1, unsupported1);
        assertEquals(unsupported1, unsupported2);
        assertNotEquals(unsupported1, unsupported3);
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField unsupported1 = new UnsupportedDurationField(DurationFieldType.DAYS);
        UnsupportedDurationField unsupported2 = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertEquals(unsupported1.hashCode(), unsupported2.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertEquals("UnsupportedDurationField[DAYS]", unsupported.toString());
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertSame(UnsupportedDurationField.getInstance(DurationFieldType.DAYS), unsupported.readResolve());
    }

    @Test
    public void testUnsupported() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertInstanceOf(UnsupportedOperationException.class, unsupported.unsupported());
    }
}