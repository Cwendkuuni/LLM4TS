import org.junit.Test;
import org.joda.time.BaseDateTime;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        assertEquals(type, unsupportedField.getType());
    }

    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        assertEquals("hours", unsupportedField.getName());
    }

    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        assertFalse(unsupportedField.isSupported());
    }

    @Test
    public void testIsPrecise() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        assertTrue(unsupportedField.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long duration = 1000L;
        unsupportedField.getValue(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long duration = 1000L;
        unsupportedField.getValueAsLong(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAtInstant() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long duration = 1000L;
        long instant = System.currentTimeMillis();
        unsupportedField.getValue(duration, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongAtInstant() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long duration = 1000L;
        long instant = System.currentTimeMillis();
        unsupportedField.getValueAsLong(duration, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        int value = 1;
        unsupportedField.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAtValue() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long value = 1000L;
        unsupportedField.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long instant = System.currentTimeMillis();
        int value = 1;
        unsupportedField.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddLong() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long instant = System.currentTimeMillis();
        long value = 1000L;
        unsupportedField.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long minuendInstant = System.currentTimeMillis();
        long subtrahendInstant = minuendInstant + 1000L;
        unsupportedField.getDifference(minuendInstant, subtrahendInstant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long minuendInstant = System.currentTimeMillis();
        long subtrahendInstant = minuendInstant + 1000L;
        unsupportedField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        assertEquals(0, unsupportedField.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.HOURS;
        DurationFieldType type2 = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type2);
        assertEquals(1, unsupportedField1.compareTo(unsupportedField2));
    }

    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type);
        assertTrue(unsupportedField1.equals(unsupportedField2));
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        assertEquals(type.getName().hashCode(), unsupportedField.hashCode());
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[hours]", unsupportedField.toString());
    }

    @Test
    public void testReadResolve() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(type);
        UnsupportedDurationField resolved = (UnsupportedDurationField) unsupportedField.readResolve();
        assertEquals(type, resolved.getType());
    }

    @Test
    public void testUnsupportedOperationException() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValue(1L));
    }
}