import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.Seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertNotNull(unsupported);
        assertEquals(type, unsupported.getType());
        assertEquals(type.getName(), unsupported.getName());
        assertFalse(unsupported.isSupported());
        assertTrue(unsupported.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.Seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 1000;
        unsupported.getValue(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.Seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 1000;
        unsupported.getValueAsLong(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueLong() {
        DurationFieldType type = DurationFieldType.Seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 1000;
        long instant = 0;
        unsupported.getValue(duration, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongLong() {
        DurationFieldType type = DurationFieldType.Seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 1000;
        long instant = 0;
        unsupported.getValueAsLong(duration, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisInt() {
        DurationFieldType type = DurationFieldType.Seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        int value = 1;
        unsupported.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLong() {
        DurationFieldType type = DurationFieldType.Seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long value = 1;
        unsupported.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisIntLong() {
        DurationFieldType type = DurationFieldType.Seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        int value = 1;
        long instant = 0;
        unsupported.getMillis(value, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLongLong() {
        DurationFieldType type = DurationFieldType.Seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long value = 1;
        long instant = 0;
        unsupported.getMillis(value, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationFieldType type = DurationFieldType.Seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long instant = 0;
        unsupported.add(instant, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddLong() {
        DurationFieldType type = DurationFieldType.Seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long instant = 0;
        unsupported.add(instant, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceInt() {
        DurationFieldType type = DurationFieldType.Seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 0;
        long subtrahendInstant = 1;
        unsupported.getDifference(minuendInstant, subtrahendInstant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.Seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 0;
        long subtrahendInstant = 1;
        unsupported.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.Seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getUnitMillis();
    }

    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.Seconds();
        DurationFieldType type2 = DurationFieldType.Milliseconds();
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type2);
        assertEquals(0, unsupported1.compareTo(unsupported2));
    }

    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.Seconds();
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported3 = UnsupportedDurationField.getInstance(DurationFieldType.Milliseconds());
        assertTrue(unsupported1.equals(unsupported1));
        assertTrue(unsupported1.equals(unsupported2));
        assertFalse(unsupported1.equals(unsupported3));
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.Seconds();
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupported1.hashCode(), unsupported2.hashCode());
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.Seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[Seconds]", unsupported.toString());
    }
}