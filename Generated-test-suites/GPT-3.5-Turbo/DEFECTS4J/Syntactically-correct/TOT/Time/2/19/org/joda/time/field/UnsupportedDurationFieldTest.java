import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        // Typical use case
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.year());
        assertNotNull(field);
        
        // Edge case
        field = UnsupportedDurationField.getInstance(DurationFieldType.monthOfYear());
        assertNotNull(field);
        
        // Error scenario
        field = UnsupportedDurationField.getInstance(null);
        assertNull(field);
    }

    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertEquals(field.getType(), DurationFieldType.millis());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertEquals(field.getName(), "seconds");
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueLong_duration() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        field.getValue(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_duration() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        field.getValueAsLong(2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueLong_durationInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        field.getValue(3000L, 1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_durationInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        field.getValueAsLong(4000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisInt_value() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getMillis(5000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLong_value() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        field.getMillis(6000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisInt_valueInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.monthOfYear());
        field.getMillis(7000, 3000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLong_valueInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.year());
        field.getMillis(8000L, 4000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddLong_valueInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.add(5000L, 1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddLong_longValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        field.add(6000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceLong_minuendInstant_subtrahendInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        field.getDifference(4000L, 3000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        field.getDifferenceAsLong(2000L, 1000L);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertEquals(field.getUnitMillis(), 0);
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertEquals(field1.compareTo(field2), 0); // Since sort order is not relevant, it should return 0 always.
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertTrue(field1.equals(field2));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertEquals(field.hashCode(), field.getName().hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        assertEquals(field.toString(), "UnsupportedDurationField[minutes]");
    }
    
}