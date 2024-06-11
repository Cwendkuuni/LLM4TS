import org.joda.time.DurationFieldType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {
  
    private BaseSingleFieldPeriod testPeriod;
  
    @Before
    public void setUp() {
        // Create a new test instance before each test
        testPeriod = new BaseSingleFieldPeriod(10);
    }
  
    @After
    public void tearDown() {
        // Clean up test instance after each test
        testPeriod = null;
    }
  
    @Test
    public void testGetFieldType() {
        // Test if the returned field type matches the expected one
        DurationFieldType expected = DurationFieldType.years();
        Assert.assertEquals(expected, testPeriod.getFieldType());
    }
  
    @Test
    public void testGetPeriodType() {
        // Test if the returned period type is not null
        Assert.assertNotNull(testPeriod.getPeriodType());
    }
  
    @Test
    public void testSize() {
        // Test if the size of the period is 1
        int expectedSize = 1;
        Assert.assertEquals(expectedSize, testPeriod.size());
    }
  
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeIndexOutOfBounds() {
        // Test if getting field type with invalid index throws IndexOutOfBoundsException
        int invalidIndex = 1;
        testPeriod.getFieldType(invalidIndex);
    }
  
    @Test
    public void testGetFieldTypeAtIndexZero() {
        // Test if the returned field type matches the expected one at index 0
        DurationFieldType expected = DurationFieldType.years();
        Assert.assertEquals(expected, testPeriod.getFieldType(0));
    }
  
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueIndexOutOfBounds() {
        // Test if getting value with invalid index throws IndexOutOfBoundsException
        int invalidIndex = 1;
        testPeriod.getValue(invalidIndex);
    }
  
    @Test
    public void testGetValueAtIndexZero() {
        // Test if the returned value matches the expected one at index 0
        int expectedValue = 10;
        Assert.assertEquals(expectedValue, testPeriod.getValue(0));
    }
  
    @Test
    public void testGet() {
        // Test if getting value for matching field type returns the expected value
        DurationFieldType fieldType = DurationFieldType.years();
        int expectedValue = 10;
        Assert.assertEquals(expectedValue, testPeriod.get(fieldType));
    }
  
    @Test
    public void testGetForMismatchedFieldType() {
        // Test if getting value for non-matching field type returns 0
        DurationFieldType fieldType = DurationFieldType.months();
        int expectedValue = 0;
        Assert.assertEquals(expectedValue, testPeriod.get(fieldType));
    }
  
    @Test
    public void testIsSupported() {
        // Test if the field type is supported (should return true)
        DurationFieldType supportedFieldType = DurationFieldType.years();
        Assert.assertTrue(testPeriod.isSupported(supportedFieldType));
    }
  
    @Test
    public void testIsNotSupported() {
        // Test if the field type is not supported (should return false)
        DurationFieldType unsupportedFieldType = DurationFieldType.months();
        Assert.assertFalse(testPeriod.isSupported(unsupportedFieldType));
    }
  
    @Test
    public void testToPeriod() {
        // Test if toPeriod returns a non-null period instance
        Assert.assertNotNull(testPeriod.toPeriod());
    }
  
    @Test
    public void testToMutablePeriod() {
        // Test if toMutablePeriod returns a non-null mutable period instance
        Assert.assertNotNull(testPeriod.toMutablePeriod());
    }
  
    @Test
    public void testEqualsWithSameInstance() {
        // Test if equals returns true for the same period instance
        Assert.assertTrue(testPeriod.equals(testPeriod));
    }
  
    @Test
    public void testEqualsWithNull() {
        // Test if equals returns false when compared with null
        Assert.assertFalse(testPeriod.equals(null));
    }
  
    @Test
    public void testEqualsWithDifferentClass() {
        // Test if equals returns false when compared with a different class instance
        Object obj = new Object();
        Assert.assertFalse(testPeriod.equals(obj));
    }
  
    @Test
    public void testEqualsWithDifferentPeriodType() {
        // Test if equals returns false when compared with a period of different type
        ReadablePeriod otherPeriod = new BaseSingleFieldPeriod(20);
        Assert.assertFalse(testPeriod.equals(otherPeriod));
    }
  
    @Test
    public void testEqualsWithSamePeriodTypeAndValue() {
        // Test if equals returns true for periods with same type and value
        ReadablePeriod samePeriod = new BaseSingleFieldPeriod(10);
        Assert.assertTrue(testPeriod.equals(samePeriod));
    }
  
    @Test
    public void testEqualsWithDifferentValue() {
        // Test if equals returns false for periods with different values
        ReadablePeriod differentPeriod = new BaseSingleFieldPeriod(5);
        Assert.assertFalse(testPeriod.equals(differentPeriod));
    }
  
    @Test
    public void testHashCode() {
        // Test if the hash code is consistent
        int expectedHashCode = 27 * (27 * 17 + 10) + DurationFieldType.years().hashCode();
        Assert.assertEquals(expectedHashCode, testPeriod.hashCode());
    }
  
    @Test(expected = ClassCastException.class)
    public void testCompareToWithDifferentClass() {
        // Test if comparing with a different class throws ClassCastException
        BaseSingleFieldPeriod differentPeriod = new BaseSingleFieldPeriod(20);
        testPeriod.compareTo(differentPeriod);
    }
  
    @Test
    public void testCompareToWithGreaterValue() {
        // Test if comparing with a period of greater value returns 1
        BaseSingleFieldPeriod greaterPeriod = new BaseSingleFieldPeriod(20);
        int expectedComparisonResult = 1;
        Assert.assertEquals(expectedComparisonResult, testPeriod.compareTo(greaterPeriod));
    }
  
    @Test
    public void testCompareToWithSmallerValue() {
        // Test if comparing with a period of smaller value returns -1
        BaseSingleFieldPeriod smallerPeriod = new BaseSingleFieldPeriod(5);
        int expectedComparisonResult = -1;
        Assert.assertEquals(expectedComparisonResult, testPeriod.compareTo(smallerPeriod));
    }
  
    @Test
    public void testCompareToWithSameValue() {
        // Test if comparing with a period of the same value returns 0
        BaseSingleFieldPeriod sameValuePeriod = new BaseSingleFieldPeriod(10);
        int expectedComparisonResult = 0;
        Assert.assertEquals(expectedComparisonResult, testPeriod.compareTo(sameValuePeriod));
    }
}