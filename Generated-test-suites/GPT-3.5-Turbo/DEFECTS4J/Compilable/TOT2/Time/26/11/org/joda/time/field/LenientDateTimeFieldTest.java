package org.joda.time.field;
import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;

public class LenientDateTimeFieldTest {

    private Chronology base;

    @Test
    public void testGetInstance() {
        // Typical use case
        DateTimeField field = new MockDateTimeField();
        DateTimeField result = LenientDateTimeField.getInstance(field, base);
        assertTrue(result instanceof LenientDateTimeField);

        // Edge case: field is already lenient
        DateTimeField result2 = LenientDateTimeField.getInstance(result, base);
        assertSame(result, result2);

        // Edge case: field is a StrictDateTimeField
        StrictDateTimeField strictField = new StrictDateTimeField(field);
        DateTimeField result3 = LenientDateTimeField.getInstance(strictField, base);
        assertFalse(result3 instanceof StrictDateTimeField);
    }

    @Test
    public void testSet() {
        // Typical use case
        DateTimeField field = new MockDateTimeField();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
        long instant = 1000L;
        int value = 5;
        long result = lenientField.set(instant, value);
        assertEquals(1005L, result);

        // Edge case: value is out of bounds
        long result2 = lenientField.set(instant, 100);
        assertEquals(1100L, result2);
    }

    private static class MockDateTimeField extends DelegatedDateTimeField {
        public MockDateTimeField() {
            super(null);
        }

        @Override
        public boolean isLenient() {
            return true;
        }

        @Override
        public long set(long instant, int value) {
            return 0;
        }
    }
}