package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.ZoneUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.field.DateTimeFieldType;

import org.junit.Test;

public class LenientDateTimeFieldTest {

    /**
     * Create a lenient field for testing
     * @param fieldType the type of the field
     * @param base the base chronology
     * @return a lenient version of the given field
     */
    private static DateTimeField createLenientField(DateTimeFieldType fieldType, Chronology base) {
        DateTimeField field = DateTimeFieldType.forType(fieldType);
        return LenientDateTimeField.getInstance(field, base);
    }

    /**
     * Test lenient field creation
     */
    @Test
    public void testLenientFieldCreation() {
        Chronology base = DateTimeUtils.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateTimeField lenientField = createLenientField(DateTimeFieldType.HOUR_OF_DAY, base);
        assertNotNull(lenientField);
        assertTrue(lenientField.isLenient());
        assertNotSame(lenientField, fieldType.getField(base));
    }

    /**
     * Test setting a lenient field
     */
    @Test
    public void testLenientFieldSet() {
        Chronology base = DateTimeUtils.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateTimeField lenientField = createLenientField(DateTimeFieldType.HOUR_OF_DAY, base);
        BaseDateTime test = new BaseDateTime(1420070400000L, base);
        long originalValue = lenientField.get(test.toInstant());
        long newValue = originalValue + 1;
        long newInstant = lenientField.set(test.toInstant(), newValue);
        assertEquals(test.withFieldAdded(DateTimeFieldType.HOUR_OF_DAY, newValue), new BaseDateTime(newInstant, base));
    }

    /**
     * Test setting a lenient field with out of bounds value
     */
    @Test
    public void testLenientFieldSetOutOfBounds() {
        Chronology base = DateTimeUtils.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateTimeField lenientField = createLenientField(DateTimeFieldType.HOUR_OF_DAY, base);
        BaseDateTime test = new BaseDateTime(1420070400000L, base);
        long originalValue = lenientField.get(test.toInstant());
        long newValue = originalValue + 24 * 60 * 60;
        long newInstant = lenientField.set(test.toInstant(), newValue);
        assertEquals(test.withFieldAdded(DateTimeFieldType.HOUR_OF_DAY, newValue % 24), new BaseDateTime(newInstant, base));
    }

    /**
     * Test setting a lenient field with null argument
     */
    @Test
    public void testLenientFieldSetNull() {
        Chronology base = DateTimeUtils.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateTimeField lenientField = createLenientField(DateTimeFieldType.HOUR_OF_DAY, base);
        assertNull(lenientField.set(null, 0));
    }

    /**
     * Test setting a lenient field with zero value
     */
    @Test
    public void testLenientFieldSetZero() {
        Chronology base = DateTimeUtils.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateTimeField lenientField = createLenientField(DateTimeFieldType.HOUR_OF_DAY, base);
        BaseDateTime test = new BaseDateTime(1420070400000L, base);
        long originalValue = lenientField.get(test.toInstant());
        long newValue = 0;
        long newInstant = lenientField.set(test.toInstant(), newValue);
        assertEquals(test, new BaseDateTime(newInstant, base));
    }

    /**
     * Test getting a lenient field value
     */
    @Test
    public void testLenientFieldGet() {
        Chronology base = DateTimeUtils.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateTimeField lenientField = createLenientField(DateTimeFieldType.HOUR_OF_DAY, base);
        BaseDateTime test = new BaseDateTime(1420070400000L, base);
        long value = lenientField.get(test.toInstant());
        assertEquals(14, value);
        test = test.withFieldAdded(DateTimeFieldType.HOUR_OF_DAY, 1);
        value = lenientField.get(test.toInstant());
        assertEquals(15, value);
    }

    /**
     * Test getting a lenient field value with negative value
     */
    @Test
    public void testLenientFieldGetNegative() {
        Chronology base = DateTimeUtils.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateTimeField lenientField = createLenientField(DateTimeFieldType.HOUR_OF_DAY, base);
        BaseDateTime test = new BaseDateTime(1420070400000L, base);
        long value = lenientField.get(test.toInstant());
        assertEquals(14, value);
        test = test.withFieldAdded(DateTimeFieldType.HOUR_OF_DAY, -1);
        value = lenientField.get(test.toInstant());
        assertEquals(23, value);
    }

    /**
     * Test getting a lenient field with null argument
     */
    @Test
    public void testLenientFieldGetNull() {
        Chronology base = DateTimeUtils.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateTimeField lenientField = createLenientField(DateTimeFieldType.HOUR_OF_DAY, base);
        assertNull(lenientField.get(null));
    }

    /**
     * Test getting a lenient field with zero value
     */
    @Test
    public void testLenientFieldGetZero() {
        Chronology base = DateTimeUtils.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateTimeField lenientField = createLenientField(DateTimeFieldType.HOUR_OF_DAY, base);
        assertEquals(0, lenientField.get(new BaseDateTime(0, base).toInstant()));
    }

    /**
     * Test lenient field equality
     */
    @Test
    public void testLenientFieldEquality() {
        Chronology base = DateTimeUtils.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateTimeField lenientField1 = createLenientField(DateTimeFieldType.HOUR_OF_DAY, base);
        DateTimeField lenientField2 = createLenientField(DateTimeFieldType.HOUR_OF_DAY, base);
        assertSame(lenientField1, lenientField2);
    }

    /**
     * Test lenient field inequality
     */
    @Test
    public void testLenientFieldInequality() {
        Chronology base = DateTimeUtils.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateTimeField lenientField1 = createLenientField(DateTimeFieldType.HOUR_OF_DAY, base);
        DateTimeField lenientField2 = createLenientField(DateTimeFieldType.MINUTE_OF_HOUR, base);
        assertNotSame(lenientField1, lenientField2);
    }
}