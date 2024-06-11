import org.joda.time.base.DurationFieldType;
import org.joda.time.DurationField;
import org.junit.Test;
import org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type);

        Assert.assertEquals("Unexpected instance.", unsupportedField1, unsupportedField2);
    }

    @Test
    public void testGetType() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);

        Assert.assertEquals("Unexpected type.", type, unsupportedField.getType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);

        unsupportedField.isSupported();
    }

    @Test
    public void testIsPrecise() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);

        Assert.assertTrue("Unexpected value.", unsupportedField.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);

        long duration = 1L;
        unsupportedField.getValue(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);

        long duration = 1L;
        unsupportedField.getValueAsLong(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAtInstant() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);

        long duration = 1L;
        long instant = 1L;
        unsupportedField.getValue(duration, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongAtInstant() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);

        long duration = 1L;
        long instant = 1L;
        unsupportedField.getValueAsLong(duration, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);

        int value = 1;
        unsupportedField.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAtInstant() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);

        long value = 1L;
        long instant = 1L;
        unsupportedField.getMillis(value, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);

        long instant = 1L;
        long value = 1L;
        unsupportedField.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddLong() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);

        long instant = 1L;
        long value = 1L;
        unsupportedField.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);

        long minuendInstant = 1L;
        long subtrahendInstant = 1L;
        unsupportedField.getDifference(minuendInstant, subtrahendInstant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);

        long minuendInstant = 1L;
        long subtrahendInstant = 1L;
        unsupportedField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);

        long value = unsupportedField.getUnitMillis();
        assertEquals("Unexpected unit millis.", 0, value);
    }

    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.MILLISECONDS;
        DurationFieldType type2 = DurationFieldType.SECONDS;
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type2);

        int comparison = unsupportedField1.compareTo(unsupportedField2);
        assertEquals("Unexpected comparison result.", 0, comparison);
    }

    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupportedField3 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);

        assertTrue("Unexpected false result.", unsupportedField1.equals(unsupportedField2));
        assertFalse("Unexpected true result.", unsupportedField1.equals(unsupportedField3));
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);

        int hashCode = unsupportedField.hashCode();
        assertNotSame("Unexpected hash code.", 0, hashCode);
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);

        String toString = unsupportedField.toString();
        String expected = "UnsupportedDurationField[Milliseconds]";
        assertEquals("Unexpected toString value.", expected, toString);
    }

    @Test
    public void testReadResolve() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(type);

        UnsupportedDurationField readResolve = (UnsupportedDurationField) unsupportedField.readResolve();
        UnsupportedDurationField expected = UnsupportedDurationField.getInstance(type);

        assertEquals("Unexpected readResolve value.", expected, readResolve);
    }

    @Test
    public void testUnsupportedOperationException() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);

        UnsupportedOperationException exception = unsupportedField.unsupported();

        assertEquals("Unexpected exception message.", "Milliseconds field is unsupported", exception.getMessage());
    }
}