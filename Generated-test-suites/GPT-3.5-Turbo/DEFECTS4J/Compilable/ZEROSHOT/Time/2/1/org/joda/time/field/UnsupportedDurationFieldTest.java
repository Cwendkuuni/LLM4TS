package org.joda.time.field;
import static org.junit.Assert.*;
import java.util.HashMap;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertNotNull(field);
    }
    
    @Test
    public void test_GetType() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type, field.getType());
    }
    
    @Test
    public void test_GetName() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type.getName(), field.getName());
    }
    
    @Test
    public void test_IsSupported() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertFalse(field.isSupported());
    }
    
    @Test
    public void test_IsPrecise() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertTrue(field.isPrecise());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void test_GetValue() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(10L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void test_GetValueAsLong() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(10L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void test_GetValue_withInstant() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(10L, 20L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void test_GetValueAsLong_withInstant() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(10L, 20L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void test_GetMillis_withValue() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(10);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void test_GetMillis_withLongValue() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(10L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void test_GetMillis_withValueAndInstant() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(10, 20L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void test_GetMillis_withLongValueAndInstant() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(10L, 20L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void test_Add_withInstantAndValue() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(10L, 20);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void test_Add_withInstantAndLongValue() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(10L, 20L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void test_GetDifference_withInstant() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifference(10L, 20L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void test_GetDifferenceAsLong_withInstant() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifferenceAsLong(10L, 20L);
    }
    
    @Test
    public void test_GetUnitMillis() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(0L, field.getUnitMillis());
    }
    
    @Test
    public void test_CompareTo() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertEquals(0, field1.compareTo(field2));
    }
    
    @Test
    public void test_Equals() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertTrue(field1.equals(field2));
    }
    
}