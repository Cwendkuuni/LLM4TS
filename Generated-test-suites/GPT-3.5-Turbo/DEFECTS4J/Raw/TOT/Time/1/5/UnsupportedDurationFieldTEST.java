import static org.junit.Assert.*;

import org.joda.time.DurationFieldType;
import org.joda.time.field.UnsupportedDurationField;
import org.junit.Before;
import org.junit.Test;

public class UnsupportedDurationFieldTest {
    
    private UnsupportedDurationField unsupportedDurationField;

    @Before
    public void setUp() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.hours());
    }
    
    // Testing the getType() method

    @Test
    public void testGetType() {
        assertEquals(DurationFieldType.hours(), unsupportedDurationField.getType());
    }
    
    // Testing the getName() method

    @Test
    public void testGetName() {
        assertEquals("hours", unsupportedDurationField.getName());
    }
    
    // Testing the isSupported() method

    @Test
    public void testIsSupported() {
        assertFalse(unsupportedDurationField.isSupported());
    }
    
    // Testing the isPrecise() method

    @Test
    public void testIsPrecise() {
        assertTrue(unsupportedDurationField.isPrecise());
    }
    
    // Testing the getValue(long duration) method with a positive value
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithPositiveValue() {
        unsupportedDurationField.getValue(1000L);
    }
    
    // Testing the getValue(long duration) method with a negative value
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithNegativeValue() {
        unsupportedDurationField.getValue(-1000L);
    }
    
    // Testing the getValueAsLong(long duration) method with a positive value
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithPositiveValue() {
        unsupportedDurationField.getValueAsLong(1000L);
    }
    
    // Testing the getValueAsLong(long duration) method with a negative value
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithNegativeValue() {
        unsupportedDurationField.getValueAsLong(-1000L);
    }
    
    // Testing the getValue(long duration, long instant) method with positive values
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithPositiveValues() {
        unsupportedDurationField.getValue(1000L, 100L);
    }
    
    // Testing the getValue(long duration, long instant) method with negative values
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithNegativeValues() {
        unsupportedDurationField.getValue(-1000L, -100L);
    }
    
    // Testing the getValueAsLong(long duration, long instant) method with positive values
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithPositiveValues() {
        unsupportedDurationField.getValueAsLong(1000L, 100L);
    }
    
    // Testing the getValueAsLong(long duration, long instant) method with negative values
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithNegativeValues() {
        unsupportedDurationField.getValueAsLong(-1000L, -100L);
    }
    
    // Testing the getMillis(int value) method
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithIntValue() {
        unsupportedDurationField.getMillis(10);
    }
    
    // Testing the getMillis(long value) method
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValue() {
        unsupportedDurationField.getMillis(1000L);
    }
    
    // Testing the getMillis(int value, long instant) method
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithIntValueAndInstant() {
        unsupportedDurationField.getMillis(10, 100L);
    }
    
    // Testing the getMillis(long value, long instant) method
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValueAndInstant() {
        unsupportedDurationField.getMillis(1000L, 100L);
    }
    
    // Testing the add(long instant, int value) method
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithIntValue() {
        unsupportedDurationField.add(100L, 10);
    }
    
    // Testing the add(long instant, long value) method
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithLongValue() {
        unsupportedDurationField.add(100L, 1000L);
    }
    
    // Testing the getDifference(long minuendInstant, long subtrahendInstant) method
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        unsupportedDurationField.getDifference(100L, 50L);
    }
    
    // Testing the getDifferenceAsLong(long minuendInstant, long subtrahendInstant) method
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        unsupportedDurationField.getDifferenceAsLong(100L, 50L);
    }
    
    // Testing the getUnitMillis() method
    
    @Test
    public void testGetUnitMillis() {
        assertEquals(0, unsupportedDurationField.getUnitMillis());
    }
    
    // Testing the compareTo(DurationField durationField) method with a supported field
    
    @Test
    public void testCompareToWithSupportedField() {
        assertEquals(1, unsupportedDurationField.compareTo(DurationFieldType.millis().getField()));
    }
    
    // Testing the compareTo(DurationField durationField) method with an unsupported field
    
    @Test
    public void testCompareToWithUnsupportedField() {
        assertEquals(0, unsupportedDurationField.compareTo(UnsupportedDurationField.getInstance(DurationFieldType.minutes())));
    }

    @After
    public void tearDown() {
        unsupportedDurationField = null;
    }
}