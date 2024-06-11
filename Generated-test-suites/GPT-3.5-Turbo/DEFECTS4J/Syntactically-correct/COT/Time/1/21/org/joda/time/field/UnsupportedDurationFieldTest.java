import org.joda.time.DurationFieldType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    private UnsupportedDurationField unsupportedField;

    @Before
    public void setUp() {
        unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    }

    @Test
    public void testGetType() {
        assertEquals(DurationFieldType.SECONDS, unsupportedField.getType());
    }

    @Test
    public void testIsSupported() {
        assertFalse(unsupportedField.isSupported());
    }

    @Test
    public void testIsPrecise() {
        assertTrue(unsupportedField.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        unsupportedField.getValue(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        unsupportedField.getValueAsLong(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        unsupportedField.getValue(1000, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        unsupportedField.getValueAsLong(1000, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        unsupportedField.getMillis(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueLong() {
        unsupportedField.getMillis(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstantValue() {
        unsupportedField.getMillis(1000, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstantValueLong() {
        unsupportedField.getMillis(1000L, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantValue() {
        unsupportedField.add(0, 1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantValueLong() {
        unsupportedField.add(0, 1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        unsupportedField.getDifference(0, 1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        unsupportedField.getDifferenceAsLong(0, 1000);
    }

    @Test
    public void testGetUnitMillis() {
        assertEquals(0, unsupportedField.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        assertEquals(1, unsupportedField.compareTo(DurationFieldType.MILLIS.getField()));
    }
    
    @Test
    public void testEquals() {
        UnsupportedDurationField otherField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        assertTrue(unsupportedField.equals(otherField));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField otherField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        assertEquals(unsupportedField.hashCode(), otherField.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("UnsupportedDurationField[seconds]", unsupportedField.toString());
    }
}