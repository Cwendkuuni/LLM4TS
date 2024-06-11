import org.joda.time.DurationFieldType;
import org.junit.Assert;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);

        Assert.assertNotNull(field);
        Assert.assertEquals(type, field.getType());
    }

    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = new UnsupportedDurationField(type);

        Assert.assertNotNull(field);
        Assert.assertEquals(type.getName(), field.getName());
    }

    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.hours();
        UnsupportedDurationField field = new UnsupportedDurationField(type);

        Assert.assertNotNull(field);
        Assert.assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        DurationFieldType type = DurationFieldType.days();
        UnsupportedDurationField field = new UnsupportedDurationField(type);

        Assert.assertNotNull(field);
        Assert.assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.months();
        UnsupportedDurationField field = new UnsupportedDurationField(type);

        field.getValue(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.weeks();
        UnsupportedDurationField field = new UnsupportedDurationField(type);

        field.getValueAsLong(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = new UnsupportedDurationField(type);

        field.getValue(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = new UnsupportedDurationField(type);

        field.getValueAsLong(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = new UnsupportedDurationField(type);

        field.getMillis(100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueLong() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = new UnsupportedDurationField(type);

        field.getMillis(100L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = new UnsupportedDurationField(type);

        field.getMillis(100, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueLongAndInstant() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = new UnsupportedDurationField(type);

        field.getMillis(100L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = new UnsupportedDurationField(type);

        field.add(1000L, 10);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithLong() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = new UnsupportedDurationField(type);

        field.add(1000L, 10L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = new UnsupportedDurationField(type);

        field.getDifference(2000L, 1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = new UnsupportedDurationField(type);

        field.getDifferenceAsLong(2000L, 1000L);
    }

    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = new UnsupportedDurationField(type);

        Assert.assertEquals(0L, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field1 = new UnsupportedDurationField(type);
        UnsupportedDurationField field2 = new UnsupportedDurationField(type);

        Assert.assertEquals(0, field1.compareTo(field2));
    }

}