
import org.joda.time.DurationFieldType;
import org.junit.Assert;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        // Test that the getInstance returns a non-null instance
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        Assert.assertNotNull(field);
    }

    @Test
    public void testGetType() {
        // Test that the getType returns the correct field type
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        Assert.assertEquals(DurationFieldType.ERA_TYPE, field.getType());
    }

    @Test
    public void testGetName() {
        // Test that the getName returns the correct field name
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        Assert.assertEquals("era", field.getName());
    }

    @Test
    public void testIsSupported() {
        // Test that the isSupported always returns false
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        Assert.assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        // Test that the isPrecise always returns true
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        Assert.assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        // Test that getValue throws UnsupportedOperationException
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        field.getValue(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        // Test that getValueAsLong throws UnsupportedOperationException
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        field.getValueAsLong(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        // Test that getValue with instant throws UnsupportedOperationException
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        field.getValue(1, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        // Test that getValueAsLong with instant throws UnsupportedOperationException
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        field.getValueAsLong(1, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        // Test that getMillis with value throws UnsupportedOperationException
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        field.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValue() {
        // Test that getMillis with long value throws UnsupportedOperationException
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        field.getMillis(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        // Test that getMillis with value and instant throws UnsupportedOperationException
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        field.getMillis(1, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValueAndInstant() {
        // Test that getMillis with long value and instant throws UnsupportedOperationException
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        field.getMillis(1L, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithValue() {
        // Test that add with value throws UnsupportedOperationException
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        field.add(1, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithLongValue() {
        // Test that add with long value throws UnsupportedOperationException
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        field.add(1, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        // Test that getDifference throws UnsupportedOperationException
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        field.getDifference(1, 2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        // Test that getDifferenceAsLong throws UnsupportedOperationException
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        field.getDifferenceAsLong(1, 2);
    }

    @Test
    public void testGetUnitMillis() {
        // Test that getUnitMillis returns zero
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        Assert.assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        // Test that compareTo returns 0 when passed an unsupported duration field
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        DurationField other = new UnsupportedDurationField(DurationFieldType.CALENDAR_TYPE);
        Assert.assertEquals(0, field.compareTo(other));
    }

}

