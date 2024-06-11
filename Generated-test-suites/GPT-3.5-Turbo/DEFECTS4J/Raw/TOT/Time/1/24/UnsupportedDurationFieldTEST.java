import org.joda.time.DurationFieldType;
import org.joda.time.field.UnsupportedDurationField;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UnsupportedDurationFieldTest {
    
    private static final DurationFieldType TYPE = DurationFieldType.ERAS;

    @Test
    public void testGetInstance() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(TYPE);
        assertEquals(TYPE, field.getType());
    }
    
    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(TYPE);
        assertEquals(TYPE.getName(), field.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(TYPE);
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(TYPE);
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(TYPE);
        field.getValue(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(TYPE);
        field.getValueAsLong(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(TYPE);
        field.getValue(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(TYPE);
        field.getValueAsLong(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(TYPE);
        field.getMillis(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(TYPE);
        field.getMillis(0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(TYPE);
        field.getMillis(0, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstantAndValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(TYPE);
        field.getMillis(0L, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(TYPE);
        field.add(0L, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithLongValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(TYPE);
        field.add(0L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(TYPE);
        field.getDifference(0L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(TYPE);
        field.getDifferenceAsLong(0L, 0L);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(TYPE);
        assertEquals(0L, field.getUnitMillis());
    }

    @Test
    public void testCompareToWithSupportedField() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(TYPE);
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        assertTrue(field.compareTo(other) > 0);
    }

    @Test
    public void testCompareToWithUnsupportedField() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(TYPE);
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(TYPE);
        assertEquals(0, field.compareTo(other));
    } 

    @Test
    public void testEqualsWithSameInstance() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(TYPE);
        assertEquals(field, field);
    }
    
    @Test
    public void testEqualsWithDifferentInstance() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(TYPE);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(TYPE);
        assertEquals(field1, field2);
    }

    @Test
    public void testEqualsWithDifferentType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(TYPE);
        Object obj = new Object();
        assertFalse(field.equals(obj));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(TYPE);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(TYPE);
        assertEquals(field1.hashCode(), field2.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(TYPE);
        assertEquals("UnsupportedDurationField[eras]", field1.toString());
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(TYPE);
        UnsupportedDurationField resolvedField = (UnsupportedDurationField) field.readResolve();
        assertEquals(field, resolvedField);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(TYPE);
        throw field.unsupported();
    }
}