import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.junit.Before;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    private UnsupportedDurationField unsupportedField;
    private Duration moDuration;
    private Duration tuDuration;
    private Duration weDuration;
    private Duration thDuration;
    private Duration frDuration;
    private Duration saDuration;
    private Duration suDuration;

    @Before
    public void setUp() {
        unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.weekOfWeekyear());
        moDuration = new Duration(1);
        tuDuration = new Duration(2);
        weDuration = new Duration(3);
        thDuration = new Duration(4);
        frDuration = new Duration(5);
        saDuration = new Duration(6);
        suDuration = new Duration(7);
    }

    @Test
    public void testGetType() {
        assertEquals(DurationFieldType.weekOfWeekyear(), unsupportedField.getType());
    }

    @Test
    public void testGetName() {
        assertEquals(DurationFieldType.weekOfWeekyear().getName(), unsupportedField.getName());
    }
    
    @Test
    public void testIsSupported() {
        assertFalse(unsupportedField.isSupported());
    }
    
    @Test
    public void testIsPrecise() {
        assertTrue(unsupportedField.isPrecise());
    }
    
    @Test
    public void testGetValue() {
        try {
            unsupportedField.getValue(moDuration.getMillis());
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
    
    @Test
    public void testGetValueAsLong() {
        try {
            unsupportedField.getValueAsLong(tuDuration.getMillis());
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
    
    @Test
    public void testGetValueWithInstant() {
        try {
            unsupportedField.getValue(moDuration.getMillis(), tuDuration.getMillis());
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
    
    @Test
    public void testGetValueAsLongWithInstant() {
        try {
            unsupportedField.getValueAsLong(tuDuration.getMillis(), weDuration.getMillis());
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
    
    @Test
    public void testGetMillisWithValue() {
        try {
            unsupportedField.getMillis(1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
    
    @Test
    public void testGetMillisWithLongValue() {
        try {
            unsupportedField.getMillis(1234567890L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
    
    @Test
    public void testGetMillisWithValueAndInstant() {
        try {
            unsupportedField.getMillis(1, 1234567890L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
    
    @Test
    public void testGetMillisWithLongValueAndInstant() {
        try {
            unsupportedField.getMillis(1234567890L, 1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
    
    @Test
    public void testAdd() {
        try {
            unsupportedField.add(moDuration.getMillis(), 1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
    
    @Test
    public void testAddWithLongValue() {
        try {
            unsupportedField.add(moDuration.getMillis(), 1234567890L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
    
    @Test
    public void testGetDifference() {
        try {
            unsupportedField.getDifference(moDuration.getMillis(), tuDuration.getMillis());
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        try {
            unsupportedField.getDifferenceAsLong(tuDuration.getMillis(), weDuration.getMillis());
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
    
    @Test
    public void testGetUnitMillis() {
        assertEquals(0, unsupportedField.getUnitMillis());
    }
    
    @Test
    public void testCompareTo() {
        assertEquals(1, unsupportedField.compareTo(DurationFieldType.centuries()));
    }
    
    @Test
    public void testEquals() {
        UnsupportedDurationField otherUnsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.weekOfWeekyear());
        assertEquals(unsupportedField, unsupportedField);
        assertEquals(unsupportedField, otherUnsupportedField);
        assertFalse(unsupportedField.equals(null));
        assertFalse(unsupportedField.equals("unsupportedField"));
    }
    
    @Test
    public void testHashCode() {
        UnsupportedDurationField otherUnsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.weekOfWeekyear());
        assertEquals(unsupportedField.hashCode(), otherUnsupportedField.hashCode());
    }
    
    @Test
    public void testToString() {
        String expected = "UnsupportedDurationField[" + DurationFieldType.weekOfWeekyear().getName() + "]";
        assertEquals(expected, unsupportedField.toString());
    }
    
    @Test
    public void testReadResolve() {
        UnsupportedDurationField resolvedField = unsupportedField.readResolve();
        assertEquals(unsupportedField, resolvedField);
    }
}