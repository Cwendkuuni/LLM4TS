import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.LenientChronology;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    private static final long TEST_INSTANT = 123456789L;

    private DateTimeField lenientField;
    private DateTimeField strictField;
    private DateTimeField baseField;

    @Before
    public void setUp() {
        DateTimeUtils.setCurrentMillisFixed(TEST_INSTANT);

        baseField = ISOChronology.getInstance().year();
        strictField = new StrictDateTimeField(baseField);
        lenientField = new LenientDateTimeField(baseField, LenientChronology.getInstance());
    }

    @Test
    public void testGetInstanceWithNullField_shouldReturnNull() {
        Assert.assertNull(LenientDateTimeField.getInstance(null, ISOChronology.getInstance()));
    }

    @Test
    public void testGetInstanceWithStrictField_shouldReturnSameField() {
        DateTimeField field = LenientDateTimeField.getInstance(strictField, ISOChronology.getInstance());
        Assert.assertEquals(strictField, field);
    }

    @Test
    public void testGetInstanceWithLenientField_shouldReturnSameField() {
        DateTimeField field = LenientDateTimeField.getInstance(lenientField, ISOChronology.getInstance());
        Assert.assertEquals(lenientField, field);
    }

    @Test
    public void testGetInstanceWithNonLenientField_shouldReturnNewLenientField() {
        DateTimeField field = LenientDateTimeField.getInstance(baseField, ISOChronology.getInstance());
        Assert.assertNotEquals(baseField, field);
        Assert.assertTrue(field instanceof LenientDateTimeField);
    }

    @Test
    public void testIsLenient_shouldReturnTrue() {
        Assert.assertTrue(lenientField.isLenient());
    }

    @Test
    public void testSetWithinBounds_shouldReturnModifiedInstant() {
        long modifiedInstant = lenientField.set(TEST_INSTANT, 2022);
        Assert.assertNotEquals(TEST_INSTANT, modifiedInstant);
    }

    @Test
    public void testSetWithinBounds_shouldAddDifferenceToCurrentValue() {
        int currentValue = lenientField.get(TEST_INSTANT);
        long modifiedInstant = lenientField.set(TEST_INSTANT, currentValue + 1);
        Assert.assertEquals(currentValue + 1, lenientField.get(modifiedInstant));
    }

    @Test
    public void testSetOutOfBounds_shouldAddDifferenceToCurrentValue() {
        int currentValue = lenientField.get(TEST_INSTANT);
        long modifiedInstant = lenientField.set(TEST_INSTANT, 2500);
        Assert.assertEquals(currentValue + (2500 - currentValue), lenientField.get(modifiedInstant));
    }

    @Test
    public void testSetOutOfBoundsWithNegativeDifference_shouldAddDifferenceToCurrentValue() {
        int currentValue = lenientField.get(TEST_INSTANT);
        long modifiedInstant = lenientField.set(TEST_INSTANT, -1);
        Assert.assertEquals(currentValue - 1, lenientField.get(modifiedInstant));
    }

    @Test
    public void testSetOutOfBoundsWithZeroDifference_shouldNotModifyCurrentValue() {
        int currentValue = lenientField.get(TEST_INSTANT);
        long modifiedInstant = lenientField.set(TEST_INSTANT, currentValue);
        Assert.assertEquals(currentValue, lenientField.get(modifiedInstant));
    }

    @Test
    public void testSetEdgeCaseMinValue_shouldReturnModifiedInstant() {
        long modifiedInstant = lenientField.set(TEST_INSTANT, lenientField.getMinimumValue());
        Assert.assertNotEquals(TEST_INSTANT, modifiedInstant);
    }

    @Test
    public void testSetEdgeCaseMaxValue_shouldReturnModifiedInstant() {
        long modifiedInstant = lenientField.set(TEST_INSTANT, lenientField.getMaximumValue());
        Assert.assertNotEquals(TEST_INSTANT, modifiedInstant);
    }

    @Test
    public void testSetNullInstant_shouldSetInstantToZero() {
        long modifiedInstant = lenientField.set(0, 2022);
        Assert.assertNotEquals(0, modifiedInstant);
    }

    @Test
    public void testSetZeroInstant_shouldSetInstantToNonZero() {
        long modifiedInstant = lenientField.set(TEST_INSTANT, 2022);
        Assert.assertNotEquals(TEST_INSTANT, modifiedInstant);
    }
}