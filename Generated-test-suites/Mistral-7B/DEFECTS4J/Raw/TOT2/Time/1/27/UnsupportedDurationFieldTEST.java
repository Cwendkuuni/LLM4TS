package org.example.junit.test;

import static org.junit.Assert.*;

import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedDurationField;

public class UnsupportedDurationFieldTest {

    @Test
    public void testgetType() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        assertEquals("Millis", unsupportedField.getType().getName());
    }

    @Test
    public void testgetTypeDifferentInstance() {
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        assertEquals("Millis", unsupportedField2.getType().getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        assertFalse(unsupportedField.isSupported());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        unsupportedField.getValue(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        unsupportedField.getValueAsLong(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueDifferentInstances() {
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        unsupportedField2.getValue(1L);
    }

    // Add other test cases for other methods as needed

    @After
    public void tearDown() {
        // Add any necessary tear-down code here
    }

}