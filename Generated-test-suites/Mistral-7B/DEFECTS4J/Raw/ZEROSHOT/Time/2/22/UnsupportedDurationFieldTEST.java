import org.junit.Test;
import org.joda.time.base.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertEquals(field1, field2);
    }

    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertFalse(field.isSupported());
    }

    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("seconds", field.getName());
    }

    @Test
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> field.getValue(1));
    }

    @Test
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> field.getValueAsLong(1));
    }

    @Test
    public void testGetValueWithInstant() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> field.getValue(1, 0));
        assertThrows(UnsupportedOperationException.class, () -> field.getValueAsLong(1, 0));
    }

    @Test
    public void testGetMillis() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1));
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1L));
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1, 0));
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1L, 0));
    }

    @Test
    public void testAdd() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> field.add(0, 1));
        assertThrows(UnsupportedOperationException.class, () -> field.add(0, 1L));
    }

    @Test
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> field.getDifference(0, 0));
        assertThrows(UnsupportedOperationException.class, () -> field.getDifferenceAsLong(0, 0));
    }

    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.seconds();
        DurationFieldType type2 = DurationFieldType.milliseconds();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        assertEquals(0, field1.compareTo(field2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEqualsNull() {
        UnsupportedDurationField field = new UnsupportedDurationField(null);
    }

    @Test
    public void testEqualsSameInstance() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.seconds());
        assertEquals(field, field);
    }

    @Test
    public void testEqualsDifferentInstance() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.seconds());
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.minutes());
        assertFalse(field1.equals(field2));
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = new UnsupportedDurationField(type);
        assertEquals(field1.hashCode(), field2.hashCode());
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[seconds]", field.toString());
    }

    @Test
    public void testReadResolve() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertSame(UnsupportedDurationField.getInstance(type), field.readResolve());
    }

    @Test
    public void testUnsupported() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertEquals(field.getType(), type);
        assertEquals(field.getName(), type.getName());
        assertEquals(field.isSupported(), false);
        assertEquals(field.isPrecise(), true);
        assertThrows(UnsupportedOperationException.class, () -> field.getValue(1));
        assertThrows(UnsupportedOperationException.class, () -> field.getValueAsLong(1));
        assertThrows(UnsupportedOperationException.class, () -> field.getValue(1, 0));
        assertThrows(UnsupportedOperationException.class, () -> field.getValueAsLong(1, 0));
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1));
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1L));
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1, 0));
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1L, 0));
        assertThrows(UnsupportedOperationException.class, () -> field.add(0, 1));
        assertThrows(UnsupportedOperationException.class, () -> field.add(0, 1L));
        assertThrows(UnsupportedOperationException.class, () -> field.getDifference(0, 0));
        assertThrows(UnsupportedOperationException.class, () -> field.getDifferenceAsLong(0, 0));
        assertEquals(field.getUnitMillis(), 0);
        assertEquals(field.compareTo(null), 0);
    }
}