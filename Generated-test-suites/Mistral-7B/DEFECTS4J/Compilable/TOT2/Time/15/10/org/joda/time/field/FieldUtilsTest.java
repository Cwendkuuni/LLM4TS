package org.joda.time.field;
import org.junit.Test;
import org.joda.time.field.FieldUtils;
import static org.junit.Assert.assertEquals;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        // Basic test
        int input = 5;
        int expectedOutput = -5;
        int output = FieldUtils.safeNegate(input);
        assertEquals(expectedOutput, output);

        // Edge cases
        int minInt = Integer.MIN_VALUE;
        int expectedMinIntNegate = -minInt;
        int outputMinIntNegate = FieldUtils.safeNegate(minInt);
        assertEquals(expectedMinIntNegate, outputMinIntNegate);

        int maxInt = Integer.MAX_VALUE;
        int expectedMaxIntNegate = -maxInt;
        int outputMaxIntNegate = FieldUtils.safeNegate(maxInt);
        assertEquals(expectedMaxIntNegate, outputMaxIntNegate);
    }

    // Test other methods similarly
}