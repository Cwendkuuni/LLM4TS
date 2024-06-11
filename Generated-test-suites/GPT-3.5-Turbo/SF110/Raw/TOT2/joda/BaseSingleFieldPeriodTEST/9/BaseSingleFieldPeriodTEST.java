package org.joda.time.base;

import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween_withNullReadableInstances_shouldThrowIllegalArgumentException() {
        try {
            BaseSingleFieldPeriod.between(null, null, DurationFieldType.years());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBetween_withDifferentSizeReadablePartials_shouldThrowIllegalArgumentException() {
        try {
            BaseSingleFieldPeriod.between(new Partial(), new Partial().withField(0, 1), new Period());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testStandardPeriodIn_withNullReadablePeriod_shouldReturnZero() {
        int result = BaseSingleFieldPeriod.standardPeriodIn(null, 1L);
        assertEquals(0, result);
    }

    // Add more test cases for other methods as per requirements

}