import org.joda.time.DurationFieldType;
import org.junit.Assert;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.MONTHS);
        Assert.assertSame(field1, field2);
        Assert.assertNotSame(field1, field3);
    }

    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        Assert.assertEquals(DurationFieldType.YEARS, field.getType());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        Assert.assertEquals("years", field.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        Assert.assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        Assert.assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        field.getValue(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        field.getValueAsLong(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        field.getValue(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        field.getValueAsLong(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        field.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        field.getMillis(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        field.getMillis(1, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        field.getMillis(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_int() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        field.add(1000L, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        field.add(1000L, 1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        field.getDifference(2000L, 1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        field.getDifferenceAsLong(2000L, 1000L);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        Assert.assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        Assert.assertEquals(1, field1.compareTo(field2));
        Assert.assertEquals(0, field1.compareTo(field1));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.MONTHS);
        Assert.assertEquals(field1, field2);
        Assert.assertNotEquals(field1, field3);
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.MONTHS);
        Assert.assertEquals(field1.hashCode(), field2.hashCode());
        Assert.assertNotEquals(field1.hashCode(), field3.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        Assert.assertEquals("UnsupportedDurationField[years]", field.toString());
    }
}