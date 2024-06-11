import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        assertEquals(DurationFieldType.MILLIS, UnsupportedDurationField.getInstance(DurationFieldType.MILLIS).getType());
        assertEquals(DurationFieldType.SECONDS, UnsupportedDurationField.getInstance(DurationFieldType.SECONDS).getType());
        assertEquals(DurationFieldType.MINUTES, UnsupportedDurationField.getInstance(DurationFieldType.MINUTES).getType());
    }

    @Test
    public void testGetName() {
        assertEquals("Millis", UnsupportedDurationField.getInstance(DurationFieldType.MILLIS).getName());
        assertEquals("Seconds", UnsupportedDurationField.getInstance(DurationFieldType.SECONDS).getName());
        assertEquals("Minutes", UnsupportedDurationField.getInstance(DurationFieldType.MINUTES).getName());
    }

    @Test
    public void testIsSupported() {
        assertFalse(UnsupportedDurationField.getInstance(DurationFieldType.MILLIS).isSupported());
        assertFalse(UnsupportedDurationField.getInstance(DurationFieldType.SECONDS).isSupported());
        assertFalse(UnsupportedDurationField.getInstance(DurationFieldType.MINUTES).isSupported());
    }

    @Test
    public void testIsPrecise() {
        assertTrue(UnsupportedDurationField.getInstance(DurationFieldType.MILLIS).isPrecise());
        assertTrue(UnsupportedDurationField.getInstance(DurationFieldType.SECONDS).isPrecise());
        assertTrue(UnsupportedDurationField.getInstance(DurationFieldType.MINUTES).isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField.getInstance(DurationFieldType.MILLIS).getValue(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField.getInstance(DurationFieldType.SECONDS).getValueAsLong(60);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField.getInstance(DurationFieldType.MINUTES).getValue(1000, 500);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField.getInstance(DurationFieldType.MILLIS).getValueAsLong(1000, 500);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithIntValue() {
        UnsupportedDurationField.getInstance(DurationFieldType.SECONDS).getMillis(60);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValue() {
        UnsupportedDurationField.getInstance(DurationFieldType.MINUTES).getMillis(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithIntValueAndInstant() {
        UnsupportedDurationField.getInstance(DurationFieldType.MILLIS).getMillis(1000, 500);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValueAndInstant() {
        UnsupportedDurationField.getInstance(DurationFieldType.SECONDS).getMillis(60L, 30);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithIntValue() {
        UnsupportedDurationField.getInstance(DurationFieldType.MINUTES).add(1000, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithLongValue() {
        UnsupportedDurationField.getInstance(DurationFieldType.MILLIS).add(1000L, 500L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField.getInstance(DurationFieldType.SECONDS).getDifference(2000L, 1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField.getInstance(DurationFieldType.MINUTES).getDifferenceAsLong(3000L, 2000L);
    }

    @Test
    public void testGetUnitMillis() {
        assertEquals(0L, UnsupportedDurationField.getInstance(DurationFieldType.MILLIS).getUnitMillis());
        assertEquals(0L, UnsupportedDurationField.getInstance(DurationFieldType.SECONDS).getUnitMillis());
        assertEquals(0L, UnsupportedDurationField.getInstance(DurationFieldType.MINUTES).getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        assertEquals(0, UnsupportedDurationField.getInstance(DurationFieldType.MILLIS).compareTo(UnsupportedDurationField.getInstance(DurationFieldType.MILLIS)));
        assertEquals(0, UnsupportedDurationField.getInstance(DurationFieldType.SECONDS).compareTo(UnsupportedDurationField.getInstance(DurationFieldType.SECONDS)));
        assertEquals(0, UnsupportedDurationField.getInstance(DurationFieldType.MINUTES).compareTo(UnsupportedDurationField.getInstance(DurationFieldType.MINUTES)));
    }

    @Test
    public void testEquals() {
        assertTrue(UnsupportedDurationField.getInstance(DurationFieldType.MILLIS).equals(UnsupportedDurationField.getInstance(DurationFieldType.MILLIS)));
        assertTrue(UnsupportedDurationField.getInstance(DurationFieldType.SECONDS).equals(UnsupportedDurationField.getInstance(DurationFieldType.SECONDS)));
        assertTrue(UnsupportedDurationField.getInstance(DurationFieldType.MINUTES).equals(UnsupportedDurationField.getInstance(DurationFieldType.MINUTES)));
    }

    @Test
    public void testHashCode() {
        assertEquals(UnsupportedDurationField.getInstance(DurationFieldType.MILLIS).hashCode(), UnsupportedDurationField.getInstance(DurationFieldType.MILLIS).hashCode());
        assertEquals(UnsupportedDurationField.getInstance(DurationFieldType.SECONDS).hashCode(), UnsupportedDurationField.getInstance(DurationFieldType.SECONDS).hashCode());
        assertEquals(UnsupportedDurationField.getInstance(DurationFieldType.MINUTES).hashCode(), UnsupportedDurationField.getInstance(DurationFieldType.MINUTES).hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("UnsupportedDurationField[Millis]", UnsupportedDurationField.getInstance(DurationFieldType.MILLIS).toString());
        assertEquals("UnsupportedDurationField[Seconds]", UnsupportedDurationField.getInstance(DurationFieldType.SECONDS).toString());
        assertEquals("UnsupportedDurationField[Minutes]", UnsupportedDurationField.getInstance(DurationFieldType.MINUTES).toString());
    }

    @Test
    public void testReadResolve() {
        assertEquals(UnsupportedDurationField.getInstance(DurationFieldType.MINUTES), UnsupportedDurationField.getInstance(DurationFieldType.MINUTES).readResolve());
    }

}