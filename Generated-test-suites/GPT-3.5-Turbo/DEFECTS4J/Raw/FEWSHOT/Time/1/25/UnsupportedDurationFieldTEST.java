
import static org.junit.Assert.*;
import org.junit.Test;

public class UnsupportedDurationFieldTest {
  
    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertEquals(DurationFieldType.years(), field.getType());
    }
  
    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertEquals("years", field.getName());
    }
  
    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertFalse(field.isSupported());
    }
  
    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertTrue(field.isPrecise());
    }
  
    @Test
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertThrows(UnsupportedOperationException.class, () -> {
            field.getValue(100);
        });
    }
  
    @Test
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertThrows(UnsupportedOperationException.class, () -> {
            field.getValueAsLong(100);
        });
    }
  
    @Test
    public void testGetValueWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertThrows(UnsupportedOperationException.class, () -> {
            field.getValue(100, 200);
        });
    }
  
    @Test
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertThrows(UnsupportedOperationException.class, () -> {
            field.getValueAsLong(100, 200);
        });
    }
  
    @Test
    public void testGetMillisWithIntValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertThrows(UnsupportedOperationException.class, () -> {
            field.getMillis(100);
        });
    }
  
    @Test
    public void testGetMillisWithLongValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertThrows(UnsupportedOperationException.class, () -> {
            field.getMillis(100L);
        });
    }
  
    @Test
    public void testGetMillisWithIntValueAndInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertThrows(UnsupportedOperationException.class, () -> {
            field.getMillis(100, 200);
        });
    }
  
    @Test
    public void testGetMillisWithLongValueAndInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertThrows(UnsupportedOperationException.class, () -> {
            field.getMillis(100L, 200L);
        });
    }
  
    @Test
    public void testAddWithInstantAndIntValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertThrows(UnsupportedOperationException.class, () -> {
            field.add(100L, 200);
        });
    }
  
    @Test
    public void testAddWithInstantAndLongValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertThrows(UnsupportedOperationException.class, () -> {
            field.add(100L, 200L);
        });
    }
  
    @Test
    public void testGetDifferenceWithMinuendAndSubtrahendInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertThrows(UnsupportedOperationException.class, () -> {
            field.getDifference(100L, 200L);
        });
    }
  
    @Test
    public void testGetDifferenceAsLongWithMinuendAndSubtrahendInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertThrows(UnsupportedOperationException.class, () -> {
            field.getDifferenceAsLong(100L, 200L);
        });
    }
  
    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertEquals(0, field.getUnitMillis());
    }
  
    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertTrue(field1.compareTo(field2) > 0);
    }
  
    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertFalse(field1.equals(field2));
    }
  
    @Test
    public void testHashCode() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertEquals("years".hashCode(), field.hashCode());
    }
  
    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertEquals("UnsupportedDurationField[years]", field.toString());
    }
}

