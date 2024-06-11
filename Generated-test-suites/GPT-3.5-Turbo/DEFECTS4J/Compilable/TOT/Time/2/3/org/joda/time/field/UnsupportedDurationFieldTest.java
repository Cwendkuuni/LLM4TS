package org.joda.time.field;
import static org.junit.Assert.*;

import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        // Positive Test Case: Verify if the getType() method returns the correct type
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        assertEquals(DurationFieldType.weeks(), field.getType());
    }

    @Test
    public void testGetName() {
        // Positive Test Case: Verify if the getName() method returns the correct name
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertEquals("hours", field.getName());
    }

    @Test
    public void testIsSupported() {
        // Negative Test Case: Verify if the isSupported() method returns false
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        // Positive Test Case: Verify if the isPrecise() method returns true
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        // Negative Test Case: Verify if the getValue() method throws UnsupportedOperationException
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        field.getValue(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        // Negative Test Case: Verify if the getValueAsLong() method throws UnsupportedOperationException
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        field.getValueAsLong(86400000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        // Negative Test Case: Verify if the getValue() method throws UnsupportedOperationException with instant parameter
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        field.getValue(30, System.currentTimeMillis());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        // Negative Test Case: Verify if the getValueAsLong() method throws UnsupportedOperationException with instant parameter
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        field.getValueAsLong(365, System.currentTimeMillis());
    }

    // ... other test cases for the remaining methods ...

}