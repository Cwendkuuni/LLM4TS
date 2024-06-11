package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.joda.time.DateTimeField;
import org.joda.time.Chronology;

public class LenientDateTimeFieldTest {
    private DateTimeField field;
    private Chronology base;
    private LenientDateTimeField lenientField;

    @Before
    public void setUp() {
        field = new MockDateTimeField();
        base = new MockChronology();
        lenientField = new LenientDateTimeField(field, base);
    }

    @Test
    public void testGetInstance_withNullField_returnsNull() {
        assertNull(LenientDateTimeField.getInstance(null, base));
    }

    @Test
    public void testGetInstance_withFieldAlreadyLenient_returnsField() {
        DateTimeField result = LenientDateTimeField.getInstance(field, base);
        assertEquals(field, result);
    }

    @Test
    public void testGetInstance_withNonLenientField_returnsNewLenientField() {
        DateTimeField result = LenientDateTimeField.getInstance(new StrictDateTimeField(field), base);
        assertTrue(result instanceof LenientDateTimeField);
    }

    @Test
    public void testIsLenient_returnsTrue() {
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testSet_withSafeValue_returnsExpectedValue() {
        long instant = 1000L;
        int value = 10;

        long result = lenientField.set(instant, value);

        assertEquals(1010L, result);
    }

    @Test
    public void testSet_withUnsafeValue_returnsExpectedValue() {
        long instant = 1000L;
        int value = 20;

        long result = lenientField.set(instant, value);

        assertEquals(1020L, result);
    }
}