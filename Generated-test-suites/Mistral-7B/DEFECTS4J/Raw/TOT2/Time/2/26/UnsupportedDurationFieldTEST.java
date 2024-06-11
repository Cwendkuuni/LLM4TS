package org.junit.test; // replace with the appropriate test package

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.DurationFieldType;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(DurationFieldType.DAYS, unsupportedField.getType());
    }

    @Test(expected = NullPointerException.class)
    public void testGetInstanceWithNullType() {
        UnsupportedDurationField.getInstance(null);
    }

    @Test
    public void testGetInstanceWithSameType() {
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(unsupportedField1, unsupportedField2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedMethod() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.toString(); // replace with any other method not defined in the class
    }

}