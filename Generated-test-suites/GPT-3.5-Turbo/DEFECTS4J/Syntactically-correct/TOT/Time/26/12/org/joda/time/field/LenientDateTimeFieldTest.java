import org.junit.Assert;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    @Test
    public void testGetInstance() {
        // Typical use case: lenient field
        LenientDateTimeField lenientField = (LenientDateTimeField) LenientDateTimeField.getInstance(new LenientDateTimeField(lenientMockField, chronology), chronology);
        Assert.assertEquals(lenientMockField, lenientField.getWrappedField());

        // Typical use case: strict field
        DateTimeField strictField = new StrictDateTimeField(strictMockField, chronology);
        DateTimeField lenientField = LenientDateTimeField.getInstance(strictField, chronology);
        Assert.assertEquals(strictMockField, ((StrictDateTimeField)lenientField).getWrappedField());

        // Error scenario: null field
        DateTimeField field = LenientDateTimeField.getInstance(null, chronology);
        Assert.assertNull(field);
    }

    @Test
    public void testIsLenient() {
        // Typical use case: lenient field
        DateTimeField lenientField = new LenientDateTimeField(mockField, chronology);
        Assert.assertTrue(lenientField.isLenient());

        // Typical use case: strict field
        DateTimeField strictField = new StrictDateTimeField(mockField, chronology);
        DateTimeField lenientField = new LenientDateTimeField(strictField, chronology);
        Assert.assertTrue(lenientField.isLenient());
    }

    @Test
    public void testSet() {
        // Typical use case: adding a positive difference
        DateTimeField lenientField = new LenientDateTimeField(mockField, chronology);
        long instant = 1579500000L; // 2020-01-20T00:00:00Z
        int value = 2;
        long newInstant = lenientField.set(instant, value);
        Assert.assertEquals(1579590000L, newInstant); // 2020-01-20T02:00:00Z

        // Typical use case: subtracting a negative difference
        instant = 1579500000L; // 2020-01-20T00:00:00Z
        value = -2;
        newInstant = lenientField.set(instant, value);
        Assert.assertEquals(1579410000L, newInstant); // 2020-01-19T22:00:00Z

        // Error scenario: out of bounds value
        instant = 1579500000L; // 2020-01-20T00:00:00Z
        value = 25; // Out of range for mockField, should add difference instead
        newInstant = lenientField.set(instant, value);
        Assert.assertEquals(1579586400L, newInstant); // 2020-01-20T23:00:00Z
    }

}