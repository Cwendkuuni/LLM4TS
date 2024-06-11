import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField another = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupported, another);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsSupported() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        assertFalse(unsupported.isSupported());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        assertEquals("hours", unsupported.getName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        unsupported.getValue(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        unsupported.getValueAsLong(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAtInstant() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        unsupported.getValue(1L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongAtInstant() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        unsupported.getValueAsLong(1L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        unsupported.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAtInstant() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        unsupported.getMillis(1, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        unsupported.add(1L, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAtInstant() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        unsupported.add(1L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        unsupported.getDifference(1L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        unsupported.getDifferenceAsLong(1L, 1L);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        assertEquals(0, unsupported.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField unsupported1 = new UnsupportedDurationField(DurationFieldType.HOURS());
        UnsupportedDurationField unsupported2 = new UnsupportedDurationField(DurationFieldType.MINUTES());
        assertTrue(unsupported1.compareTo(unsupported2) > 0);
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField unsupported1 = new UnsupportedDurationField(DurationFieldType.HOURS());
        UnsupportedDurationField unsupported2 = new UnsupportedDurationField(DurationFieldType.HOURS());
        UnsupportedDurationField unsupported3 = new UnsupportedDurationField(DurationFieldType.MINUTES());
        assertEquals(unsupported1, unsupported1);
        assertEquals(unsupported1, unsupported2);
        assertNotEquals(unsupported1, unsupported3);
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField unsupported1 = new UnsupportedDurationField(DurationFieldType.HOURS());
        UnsupportedDurationField unsupported2 = new UnsupportedDurationField(DurationFieldType.HOURS());
        UnsupportedDurationField unsupported3 = new UnsupportedDurationField(DurationFieldType.MINUTES());
        int hashCode1 = unsupported1.hashCode();
        int hashCode2 = unsupported2.hashCode();
        int hashCode3 = unsupported3.hashCode();
        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testToString() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        assertEquals("UnsupportedDurationField[hours]", unsupported.toString());
    }
}