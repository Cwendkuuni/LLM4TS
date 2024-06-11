package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ZoneUtils;

public class LenientDateTimeFieldTest {

    @Test
    public void testGetInstance() {
        Chronology base = mock(Chronology.class);
        DateTimeField strictField = mock(DateTimeField.class);
        DateTimeField lenientField = LenientDateTimeField.getInstance(strictField, base);

        assertEquals(strictField, lenientField.getWrappedField());
        assertEquals(base, lenientField.iBase);
        assertFalse(lenientField.isLenient());

        when(strictField.isLenient()).thenReturn(true);
        lenientField = LenientDateTimeField.getInstance(strictField, base);
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testLenientDateTimeField() {
        Chronology base = mock(Chronology.class);
        DateTimeField field = mock(DateTimeField.class);
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        assertEquals(field, lenientField.getWrappedField());
        assertEquals(base, lenientField.iBase);
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testSet() {
        Chronology base = mock(Chronology.class);
        DateTimeField field = mock(DateTimeField.class);
        when(field.getFieldType()).thenReturn(DateTimeFieldType.dayOfMonth());

        LocalDateTime dateTime = new LocalDateTime(ZoneUtils.UTC);
        MutableDateTime mutableDateTime = new MutableDateTime(dateTime);

        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
        long currentValue = lenientField.get(mutableDateTime.getLongValue());
        int newValue = currentValue + 1;

        long result = lenientField.set(mutableDateTime.getLongValue(), newValue);
        assertEquals(dateTime.withFieldAdded(DateTimeFieldType.dayOfMonth(), newValue - currentValue),
                ZoneUtils.toLocalDateTime(result));
    }

    @Test
    public void testSetOutOfBounds() {
        Chronology base = mock(Chronology.class);
        DateTimeField field = mock(DateTimeField.class);
        when(field.getFieldType()).thenReturn(DateTimeFieldType.dayOfMonth());

        LocalDateTime dateTime = new LocalDateTime(ZoneUtils.UTC);
        MutableDateTime mutableDateTime = new MutableDateTime(dateTime);

        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
        long currentValue = lenientField.get(mutableDateTime.getLongValue());
        int newValue = currentValue + 123;

        long result = lenientField.set(mutableDateTime.getLongValue(), newValue);
        assertEquals(dateTime.withFieldAdded(DateTimeFieldType.dayOfMonth(), newValue - currentValue),
                ZoneUtils.toLocalDateTime(result));
    }

    @Test
    public void testSetSameValue() {
        Chronology base = mock(Chronology.class);
        DateTimeField field = mock(DateTimeField.class);
        when(field.getFieldType()).thenReturn(DateTimeFieldType.dayOfMonth());

        LocalDateTime dateTime = new LocalDateTime(ZoneUtils.UTC);
        MutableDateTime mutableDateTime = new MutableDateTime(dateTime);

        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
        long currentValue = lenientField.get(mutableDateTime.getLongValue());

        long result = lenientField.set(mutableDateTime.getLongValue(), currentValue);
        assertEquals(dateTime, ZoneUtils.toLocalDateTime(result));
    }

    @Test
    public void testSetNull() {
        LenientDateTimeField lenientField = new LenientDateTimeField(DateTimeFieldType.dayOfMonth(), null);
        assertNull(lenientField.iBase);
    }

    @Test
    public void testSetDifferentWrappedFieldType() {
        DateTimeField field1 = mock(DateTimeField.class);
        when(field1.getFieldType()).thenReturn(DateTimeFieldType.dayOfMonth());

        DateTimeField field2 = mock(DateTimeField.class);
        when(field2.getFieldType()).thenReturn(DateTimeFieldType.hourOfDay());

        LenientDateTimeField lenientField1 = new LenientDateTimeField(field1, null);
        LenientDateTimeField lenientField2 = new LenientDateTimeField(field2, null);

        assertNotSame(lenientField1, lenientField2);
    }
}