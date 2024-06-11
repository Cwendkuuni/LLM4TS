package org.joda.time.base;

import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {
    
    @Test
    public void testBetween_ReadableInstant() {
        // Test typical case
        ReadableInstant start = new DateTime(2021, 1, 1, 0, 0, 0, 0);
        ReadableInstant end = new DateTime(2021, 1, 2, 0, 0, 0, 0);
        assertEquals(86400000, BaseSingleFieldPeriod.between(start, end, DurationFieldType.days()));
        
        // Test edge case
        start = new DateTime(2021, 1, 1, 23, 59, 59, 999);
        end = new DateTime(2021, 1, 2, 0, 0, 0, 0);
        assertEquals(1, BaseSingleFieldPeriod.between(start, end, DurationFieldType.millis()));
        
        // Test exception handling
        try {
            BaseSingleFieldPeriod.between(null, end, DurationFieldType.days());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
    
    // More test cases for other public methods...

}