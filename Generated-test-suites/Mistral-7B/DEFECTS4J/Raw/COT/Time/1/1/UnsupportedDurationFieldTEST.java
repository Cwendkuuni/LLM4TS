import org.junit.Assert;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

public class UnsupportedDurationFieldTest {

    @org.junit.Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type);
        Assert.assertSame(unsupported, unsupported2);
    }

    @org.junit.Test
    public void testGetType() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        Assert.assertEquals(type, unsupported.getType());
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.isSupported();
    }

    @org.junit.Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        Assert.assertEquals("milliseconds", unsupported.getName());
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 1L;
        unsupported.getValue(duration);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 1L;
        unsupported.getValueAsLong(duration);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 1L;
        long instant = 1L;
        unsupported.getValue(duration, instant);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 1L;
        long instant = 1L;
        unsupported.getValueAsLong(duration, instant);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        int value = 1;
        unsupported.getMillis(value);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValue() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long value = 1L;
        unsupported.getMillis(value);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        int value = 1;
        long instant = 1L;
        unsupported.getMillis(value, instant);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValueAndInstant() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long value = 1L;
        long instant = 1L;
        unsupported.getMillis(value, instant);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long instant = 1L;
        int value = 1;
        unsupported.add(instant, value);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testAddLong() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long instant = 1L;
        long value = 1L;
        unsupported.add(instant, value);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 1L;
        long subtrahendInstant = 1L;
        unsupported.getDifference(minuendInstant, subtrahendInstant);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 1L;
        long subtrahendInstant = 1L;
        unsupported.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    @org.junit.Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long unitMillis = unsupported.getUnitMillis();
        Assert.assertEquals(0, unitMillis);
    }

    @org.junit.Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.MILLISECONDS;
        DurationFieldType type2 = DurationFieldType.SECONDS;
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type2);
        Assert.assertEquals(1, unsupported1.compareTo(unsupported2));
    }

    @org.junit.Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported3 = new UnsupportedDurationField(null);
        Assert.assertTrue(unsupported1.equals(unsupported1));
        Assert.assertTrue(unsupported1.equals(unsupported2));
        Assert.assertFalse(unsupported1.equals(unsupported3));
    }

    @org.junit.Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        String name = "milliseconds";
        Assert.assertEquals(name.hashCode(), unsupported.hashCode());
    }

    @org.junit.Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        Assert.assertEquals("UnsupportedDurationField[milliseconds]", unsupported.toString());
    }

    @org.junit.Test
    public void testReadResolve() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupported = new UnsupportedDurationField(type);
        UnsupportedDurationField unsupported2 = (UnsupportedDurationField) unsupported.readResolve();
        Assert.assertSame(unsupported, unsupported2);
    }

}