import org.joda.time.DurationFieldType;
import static org.junit.Assert.*;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.year();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertNotNull(field);
        assertEquals(type, field.getType());
    }

    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.months();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type.getName(), field.getName());
    }

    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.days();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        DurationFieldType type = DurationFieldType.hours();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertTrue(field.isPrecise());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.minutes();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(1000L);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(1000L);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(1000L, 2000L);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        DurationFieldType type = DurationFieldType.weeks();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(1000L, 2000L);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        DurationFieldType type = DurationFieldType.halfdays();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(10);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testGetMillisWithValueLong() {
        DurationFieldType type = DurationFieldType.eras();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(10L);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        DurationFieldType type = DurationFieldType.centuries();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(10, 1000L);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testGetMillisWithValueLongAndInstant() {
        DurationFieldType type = DurationFieldType.millennia();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(10L, 1000L);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testAddWithInstantAndValue() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(1000L, 10);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testAddWithInstantAndValueLong() {
        DurationFieldType type = DurationFieldType.months();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(1000L, 10L);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.days();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifference(1000L, 2000L);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.hours();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifferenceAsLong(1000L, 2000L);
    }

    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.minutes();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        DurationFieldType type = DurationFieldType.seconds();
        DurationFieldType otherType = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField otherField = UnsupportedDurationField.getInstance(otherType);
        
        assertEquals(1, field.compareTo(otherField));
        assertEquals(0, field.compareTo(field));
    }

    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.weeks();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        
        assertTrue(field1.equals(field2));
        assertTrue(field2.equals(field1));
        assertFalse(field1.equals(field3));
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.hours();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        
        assertEquals(field1.hashCode(), field2.hashCode());
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.days();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[" + type.getName() + ']', field.toString());
    }

    @Test
    public void testReadResolve() {
        DurationFieldType type = DurationFieldType.hours();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = (UnsupportedDurationField) field1.readResolve();
        
        assertEquals(field1, field2);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testUnsupported() {
        DurationFieldType type = DurationFieldType.minutes();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.unsupported();
    }

}