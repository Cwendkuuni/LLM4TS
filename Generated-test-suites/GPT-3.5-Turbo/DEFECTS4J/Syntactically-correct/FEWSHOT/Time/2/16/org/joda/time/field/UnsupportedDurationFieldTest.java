import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.joda.time.DurationFieldType;
import org.joda.time.field.UnsupportedDurationField;
import org.junit.Test;

public class UnsupportedDurationFieldTest {
    @Test
    public void testGetInstance() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertEquals(DurationFieldType.millis(), field.getType());
    }
    
    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertEquals("seconds", field.getName());
    }
    
    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        assertFalse(field.isSupported());
    }
    
    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertTrue(field.isPrecise());
    }
    
    @Test
    public void testGetValue_throwsUnsupportedOperationException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        try {
            field.getValue(1000);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("days field is unsupported", e.getMessage());
        }
    }
    
    @Test
    public void testGetValueAsLong_throwsUnsupportedOperationException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        try {
            field.getValueAsLong(1000);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("weeks field is unsupported", e.getMessage());
        }
    }
    
    @Test
    public void testGetValue_withInstant_throwsUnsupportedOperationException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        try {
            field.getValue(1000, 2000);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("months field is unsupported", e.getMessage());
        }
    }
    
    @Test
    public void testGetValueAsLong_withInstant_throwsUnsupportedOperationException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        try {
            field.getValueAsLong(1000, 2000);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("years field is unsupported", e.getMessage());
        }
    }
    
    @Test
    public void testGetMillis_withValue_throwsUnsupportedOperationException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.quarters());
        try {
            field.getMillis(1000);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("quarters field is unsupported", e.getMessage());
        }
    }
    
    @Test
    public void testGetMillis_withLongValue_throwsUnsupportedOperationException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.halfdays());
        try {
            field.getMillis(1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("halfdays field is unsupported", e.getMessage());
        }
    }
    
    @Test
    public void testGetMillis_withValueAndInstant_throwsUnsupportedOperationException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.eras());
        try {
            field.getMillis(1000, 2000);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("eras field is unsupported", e.getMessage());
        }
    }
    
    @Test
    public void testGetMillis_withLongValueAndInstant_throwsUnsupportedOperationException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        try {
            field.getMillis(1000L, 2000);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("centuries field is unsupported", e.getMessage());
        }
    }
    
    @Test
    public void testAdd_throwsUnsupportedOperationException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.add(1000, 5);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("millis field is unsupported", e.getMessage());
        }
    }
    
    @Test
    public void testAdd_withLongValue_throwsUnsupportedOperationException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        try {
            field.add(1000L, 5L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("seconds field is unsupported", e.getMessage());
        }
    }
    
    @Test
    public void testGetDifference_throwsUnsupportedOperationException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        try {
            field.getDifference(1000, 500);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("minutes field is unsupported", e.getMessage());
        }
    }
    
    @Test
    public void testGetDifferenceAsLong_throwsUnsupportedOperationException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        try {
            field.getDifferenceAsLong(1000L, 500L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("hours field is unsupported", e.getMessage());
        }
    }
    
    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        UnsupportedDurationField sameField = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        UnsupportedDurationField differentField = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertEquals(0, field.compareTo(sameField));
        assertTrue(field.compareTo(differentField) < 0);
    }
}