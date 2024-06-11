import static org.junit.Assert.*;

import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {
    
    @Test
    public void testGetInstance() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertNotNull(field);
        assertEquals(DurationFieldType.millis(), field.getType());
    }
    
    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertFalse(field.isSupported());
    }
    
    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertTrue(field.isPrecise());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        field.getValue(1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        field.getValueAsLong(1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue2() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        field.getValue(1000, 0);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong2() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        field.getValueAsLong(1000, 0);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        field.getMillis(1);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis2() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        field.getMillis(1L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis3() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        field.getMillis(1, 0);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis4() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        field.getMillis(1L, 0L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        field.add(0L, 1);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd2() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        field.add(0L, 1L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        field.getDifference(1L, 0L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        field.getDifferenceAsLong(1L, 0L);
    }
    
    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertEquals(0L, field.getUnitMillis());
    }
    
    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertEquals(1, field1.compareTo(field2));
        assertEquals(0, field1.compareTo(field1));
    }
    
    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertTrue(field1.equals(field1));
        assertTrue(field1.equals(field2));
        assertFalse(field1.equals(field3));
    }
    
    @Test
    public void testHashCode() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertEquals(field1.hashCode(), field2.hashCode());
    }
    
    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertEquals("UnsupportedDurationField[millis]", field.toString());
    }
    
    @Test
    public void testReadResolve() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        UnsupportedDurationField field2 = field1.readResolve();
        assertSame(field1, field2);
    }
    
    @Test
    public void testUnsupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        UnsupportedOperationException ex = field.unsupported();
        assertEquals("millis field is unsupported", ex.getMessage());
    }
}