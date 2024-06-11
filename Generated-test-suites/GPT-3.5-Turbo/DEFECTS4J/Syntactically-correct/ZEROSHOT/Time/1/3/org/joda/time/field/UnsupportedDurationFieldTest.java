import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.field.UnsupportedDurationField;

public class UnsupportedDurationFieldTest {
    
    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.days();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type, field.getType());
    }
    
    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.hours();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type.getName(), field.getName());
    }
    
    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.minutes();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertFalse(field.isSupported());
    }
    
    @Test
    public void testIsPrecise() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertTrue(field.isPrecise());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.weeks();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        DurationFieldType type = DurationFieldType.days();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(1000, 2000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        DurationFieldType type = DurationFieldType.hours();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(1000, 2000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        DurationFieldType type = DurationFieldType.minutes();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLong() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstantAndValue() {
        DurationFieldType type = DurationFieldType.weeks();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1000, 2000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstantAndValueAsLong() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1000, 2000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValue() {
        DurationFieldType type = DurationFieldType.days();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(1000, 2000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValueAsLong() {
        DurationFieldType type = DurationFieldType.hours();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(1000, 2000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.minutes();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifference(1000, 2000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifferenceAsLong(1000, 2000);
    }
    
    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.weeks();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(0, field.getUnitMillis());
    }
    
    @Test
    public void testCompareTo() {
        DurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        DurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        
        assertEquals(1, field1.compareTo(field2));
        assertEquals(0, field2.compareTo(field1));
    }
    
    @Test
    public void testEquals() {
        DurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        DurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        DurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        
        assertTrue(field1.equals(field2));
        assertFalse(field1.equals(field3));
        assertFalse(field1.equals(null));
    }
    
    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.minutes();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type.getName().hashCode(), field.hashCode());
    }
    
    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[" + type.getName() + ']', field.toString());
    }
    
    @Test
    public void testReadResolve() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = (UnsupportedDurationField) field1.readResolve();
        assertEquals(field1, field2);
    }
}