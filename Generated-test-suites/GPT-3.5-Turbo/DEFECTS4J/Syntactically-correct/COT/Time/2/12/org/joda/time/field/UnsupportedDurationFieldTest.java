import org.joda.time.DurationFieldType;
import org.joda.time.field.UnsupportedDurationField;
import org.junit.Assert;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        Assert.assertEquals(DurationFieldType.SECONDS, field.getType());
    }

    // Add test cases for other methods

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_NegativeDuration() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.getValue(-1);
    }

    // Add test cases for other methods with UnsupportedOperationException handling

    @Test
    public void testGetValueAsLong_NegativeDuration() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        try {
            field.getValueAsLong(-1);
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    // Add test cases for other methods with UnsupportedOperationException handling

    @Test
    public void testGetMillis_NegativeValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        try {
            field.getMillis(-1, 0);
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    // Add test cases for other methods with UnsupportedOperationException handling

    @Test
    public void testAdd_NegativeValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        try {
            field.add(0, -1);
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    // Add test cases for other methods with UnsupportedOperationException handling

    @Test
    public void testGetDifference_NegativeValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        try {
            field.getDifference(-1, 0);
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    // Add test cases for other methods with UnsupportedOperationException handling

    @Test(expected = UnsupportedOperationException.class)
    public void testCompareTo_NullDurationField() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.compareTo(null);
    }

    // Add test cases for other methods

    // Add other test cases as needed

}