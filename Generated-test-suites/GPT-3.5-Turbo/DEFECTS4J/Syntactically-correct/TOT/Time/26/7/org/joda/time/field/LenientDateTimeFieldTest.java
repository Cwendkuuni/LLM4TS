import org.joda.time.DateTimeField;
import org.joda.time.chrono.ISOChronology;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    private LenientDateTimeField lenientDateTimeField;

    @Before
    public void setUp() {
        DateTimeField field = ISOChronology.getInstance().dayOfMonth();
        lenientDateTimeField = new LenientDateTimeField(field, ISOChronology.getInstance());
    }

    @Test
    public void testGetInstance_shouldReturnLenientDateTimeField() {
        DateTimeField field = ISOChronology.getInstance().dayOfMonth();
        DateTimeField result = LenientDateTimeField.getInstance(field, ISOChronology.getInstance());
        Assert.assertTrue(result instanceof LenientDateTimeField);
    }

    @Test
    public void testGetInstance_withNullField_shouldReturnNull() {
        DateTimeField result = LenientDateTimeField.getInstance(null, ISOChronology.getInstance());
        Assert.assertNull(result);
    }

    @Test
    public void testSet_withValueWithinBounds_shouldReturnModifiedInstant() {
        long instant = 1577836800000L; // January 1, 2020 00:00:00 UTC
        int value = 15; // day of month
        long result = lenientDateTimeField.set(instant, value);
        Assert.assertEquals(1579003200000L, result); // January 15, 2020 00:00:00 UTC
    }

    @Test
    public void testSet_withValueOutOfBounds_shouldReturnModifiedInstantByAddingDifference() {
        long instant = 1577836800000L; // January 1, 2020 00:00:00 UTC
        int value = 32; // day of month (out of bounds)
        long result = lenientDateTimeField.set(instant, value);
        Assert.assertEquals(1580284800000L, result); // February 1, 2020 00:00:00 UTC
    }
}