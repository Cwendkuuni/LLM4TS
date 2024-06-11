import org.junit.Assert;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.MINUTE_TYPE;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        Assert.assertNotNull(field);
        Assert.assertEquals(type, field.getType());
    }

    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.HOUR_OF_DAY_TYPE;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        Assert.assertEquals(type.getName(), field.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAY_TYPE);
        Assert.assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_WEEK_TYPE);
        Assert.assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.WEEKS_TYPE);
        field.getValue(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MONTH_DAY_TYPE);
        field.getValueAsLong(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECOND_TYPE);
        field.getValue(1, 1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND_OF_MINUTE_TYPE);
        field.getValueAsLong(1L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_OF_HOUR_TYPE);
        field.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.HOUR_TYPE);
        field.getMillis(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND_TYPE);
        field.getMillis(1, 3000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLongWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS_TYPE);
        field.getMillis(1L, 4000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.HOUR_OF_DAY_TYPE);
        field.add(1000L, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_OF_DAY_TYPE);
        field.add(2000L, 2L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND_OF_DAY_TYPE);
        field.getDifference(1000L, 500L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.HOUR_OF_WEEK_TYPE);
        field.getDifferenceAsLong(2000L, 1000L);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_MONTH_TYPE);
        Assert.assertEquals(0L, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.HOUR_TYPE);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.HOUR_TYPE);
        Assert.assertEquals(0, field1.compareTo(field2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_TYPE);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_TYPE);
        Assert.assertEquals(field1, field2);
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAY_TYPE);
        Assert.assertEquals(field.getName().hashCode(), field.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.HOUR_OF_DAY_TYPE);
        Assert.assertEquals("UnsupportedDurationField[hourOfDay]", field.toString());
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.SECOND_TYPE);
        UnsupportedDurationField field2 = (UnsupportedDurationField) field1.readResolve();
        Assert.assertEquals(field1, field2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedOperationException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS_TYPE);
        throw field.unsupported();
    }

}