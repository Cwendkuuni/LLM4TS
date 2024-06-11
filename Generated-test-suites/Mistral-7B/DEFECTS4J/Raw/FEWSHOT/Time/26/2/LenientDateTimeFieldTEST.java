package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.ZoneID;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class LenientDateTimeFieldTest {

    @Test
    public void testLenientInstance() {
        DateTimeField field = mock(DateTimeField.class);
        when(field.isLenient()).thenReturn(false);
        when(field.getType()).thenReturn(DateTimeFieldType.dayOfMonth());
        Chronology base = mock(Chronology.class);

        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);

        assertEquals(field, lenientField.getWrappedField());
        assertEquals(base, lenientField.iBase);
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testLenientSet() {
        DateTimeField field = mock(DateTimeField.class);
        when(field.isLenient()).thenReturn(false);
        when(field.getType()).thenReturn(DateTimeFieldType.dayOfMonth());
        Chronology base = mock(Chronology.class);

        DateTimeField lenientField = new LenientDateTimeField(field, base);

        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 0, 0);
        long instant = dateTime.toInstant().toEpochMilli();
        int value = 32;

        long result = lenientField.set(instant, value);

        assertEquals(2022, iBase.get(result, DateTimeFieldType.year()));
        assertEquals(32, iBase.get(result, lenientField.getType()));
    }

    @Test
    public void testLenientSetOutOfBounds() {
        DateTimeField field = mock(DateTimeField.class);
        when(field.isLenient()).thenReturn(false);
        when(field.getType()).thenReturn(DateTimeFieldType.dayOfMonth());
        Chronology base = mock(Chronology.class);

        DateTimeField lenientField = new LenientDateTimeField(field, base);

        LocalDateTime dateTime = new LocalDateTime(2022, 1, 31, 0, 0);
        long instant = dateTime.toInstant().toEpochMilli();
        int value = 32;

        long result = lenientField.set(instant, value);

        LocalDateTime expectedDateTime = new LocalDateTime(2022, 2, 2, 0, 0);
        LocalDateTime actualDateTime = new LocalDateTime(result);

        assertEquals(expectedDateTime.toLocalDate().getMonthValue(), actualDateTime.getMonthValue());
        assertEquals(expectedDateTime.toLocalDate().getDayOfMonth(), actualDateTime.getDayOfMonth());
    }

    @Test
    public void testLenientSetOutOfBoundsNegative() {
        DateTimeField field = mock(DateTimeField.class);
        when(field.isLenient()).thenReturn(false);
        when(field.getType()).thenReturn(DateTimeFieldType.dayOfMonth());
        Chronology base = mock(Chronology.class);

        DateTimeField lenientField = new LenientDateTimeField(field, base);

        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 0, 0);
        long instant = dateTime.toInstant().toEpochMilli();
        int value = -3;

        long result = lenientField.set(instant, value);

        LocalDateTime expectedDateTime = new LocalDateTime(2021, 12, 28, 0, 0);
        LocalDateTime actualDateTime = new LocalDateTime(result);

        assertEquals(expectedDateTime.toLocalDate().getMonthValue(), actualDateTime.getMonthValue());
        assertEquals(expectedDateTime.toLocalDate().getDayOfMonth(), actualDateTime.getDayOfMonth());
    }

    @Test
    public void testLenientSetOutOfBoundsPositive() {
        DateTimeField field = mock(DateTimeField.class);
        when(field.isLenient()).thenReturn(false);
        when(field.getType()).thenReturn(DateTimeFieldType.dayOfMonth());
        Chronology base = mock(Chronology.class);

        DateTimeField lenientField = new LenientDateTimeField(field, base);

        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 0, 0);
        long instant = dateTime.toInstant().toEpochMilli();
        int value = 33;

        long result = lenientField.set(instant, value);

        LocalDateTime expectedDateTime = new LocalDateTime(2022, 2, 1, 0, 0);
        LocalDateTime actualDateTime = new LocalDateTime(result);

        assertEquals(expectedDateTime.toLocalDate().getMonthValue(), actualDateTime.getMonthValue());
        assertEquals(expectedDateTime.toLocalDate().getDayOfMonth(), actualDateTime.getDayOfMonth());
    }

    @Test
    public void testLenientSetSameValue() {
        DateTimeField field = mock(DateTimeField.class);
        when(field.isLenient()).thenReturn(false);
        when(field.getType()).thenReturn(DateTimeFieldType.dayOfMonth());
        Chronology base = mock(Chronology.class);

        DateTimeField lenientField = new LenientDateTimeField(field, base);

        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 0, 0);
        long instant = dateTime.toInstant().toEpochMilli();
        int value = 1;

        long result = lenientField.set(instant, value);

        assertEquals(instant, result);
    }

    @Test
    public void testLenientSetZero() {
        DateTimeField field = mock(DateTimeField.class);
        when(field.isLenient()).thenReturn(false);
        when(field.getType()).thenReturn(DateTimeFieldType.dayOfMonth());
        Chronology base = mock(Chronology.class);

        DateTimeField lenientField = new LenientDateTimeField(field, base);

        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 0, 0);
        long instant = dateTime.toInstant().toEpochMilli();
        int value = 0;

        long result = lenientField.set(instant, value);

        assertEquals(instant, result);
    }

    @Test
    public void testLenientSetNull() {
        DateTimeField field = mock(DateTimeField.class);
        when(field.isLenient()).thenReturn(false);
        when(field.getType()).thenReturn(DateTimeFieldType.dayOfMonth());
        Chronology base = mock(Chronology.class);

        DateTimeField lenientField = new LenientDateTimeField(field, base);

        long result = lenientField.set(Long.MIN_VALUE, null);

        assertEquals(Long.MIN_VALUE, result);
    }

    @Test
    public void testLenientSetFuture() {
        DateTimeField field = mock(DateTimeField.class);
        when(field.isLenient()).thenReturn(false);
        when(field.getType()).thenReturn(DateTimeFieldType.dayOfMonth());
        Chronology base = mock(Chronology.class);

        DateTimeField lenientField = new LenientDateTimeField(field, base);

        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 0, 0);
        long instant = dateTime.toInstant().toEpochMilli();
        int value = 366;

        long result = lenientField.set(instant, value);

        LocalDateTime expectedDateTime = new LocalDateTime(2023, 1, 1, 0, 0);
        LocalDateTime actualDateTime = new LocalDateTime(result);

        assertEquals(expectedDateTime.toLocalDate().getMonthValue(), actualDateTime.getMonthValue());
        assertEquals(expectedDateTime.toLocalDate().getDayOfMonth(), actualDateTime.getDayOfMonth());
    }

    @Test
    public void testLenientSetFutureNegative() {
        DateTimeField field = mock(DateTimeField.class);
        when(field.isLenient()).thenReturn(false);
        when(field.getType()).thenReturn(DateTimeFieldType.dayOfMonth());
        Chronology base = mock(Chronology.class);

        DateTimeField lenientField = new LenientDateTimeField(field, base);

        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 0, 0);
        long instant = dateTime.toInstant().toEpochMilli();
        int value = -365;

        long result = lenientField.set(instant, value);

        LocalDateTime expectedDateTime = new LocalDateTime(2021, 1, 1, 0, 0);
        LocalDateTime actualDateTime = new LocalDateTime(result);

        assertEquals(expectedDateTime.toLocalDate().getMonthValue(), actualDateTime.getMonthValue());
        assertEquals(expectedDateTime.toLocalDate().getDayOfMonth(), actualDateTime.getDayOfMonth());
    }

    @Test
    public void testLenientSetLeapYear() {
        DateTimeField field = mock(DateTimeField.class);
        when(field.isLenient()).thenReturn(false);
        when(field.getType()).thenReturn(DateTimeFieldType.dayOfMonth());
        Chronology base = mock(Chronology.class);

        DateTimeField lenientField = new LenientDateTimeField(field, base);

        LocalDateTime dateTime = new LocalDateTime(2024, 2, 29, 0, 0); // leap year
        long instant = dateTime.toInstant().toEpochMilli();
        int value = 1;

        long result = lenientField.set(instant, value);

        LocalDateTime expectedDateTime = new LocalDateTime(2024, 3, 1, 0, 0);
        LocalDateTime actualDateTime = new LocalDateTime(result);

        assertEquals(expectedDateTime.toLocalDate().getMonthValue(), actualDateTime.getMonthValue());
        assertEquals(expectedDateTime.toLocalDate().getDayOfMonth(), actualDateTime.getDayOfMonth());
    }

    @Test
    public void testLenientSetLeapYearNegative() {
        DateTimeField field = mock(DateTimeField.class);
        when(field.isLenient()).thenReturn(false);
        when(field.getType()).thenReturn(DateTimeFieldType.dayOfMonth());
        Chronology base = mock(Chronology.class);

        DateTimeField lenientField = new LenientDateTimeField(field, base);

        LocalDateTime dateTime = new LocalDateTime(2024, 2, 29, 0, 0); // leap year
        long instant = dateTime.toInstant().toEpochMilli();
        int value = -1;

        long result = lenientField.set(instant, value);

        LocalDateTime expectedDateTime = new LocalDateTime(2024, 1, 31, 0, 0);
        LocalDateTime actualDateTime = new LocalDateTime(result);

        assertEquals(expectedDateTime.toLocalDate().getMonthValue(), actualDateTime.getMonthValue());
        assertEquals(expectedDateTime.toLocalDate().getDayOfMonth(), actualDateTime.getDayOfMonth());
    }

    @Test
    public void testLenientSetNullChronology() {
        DateTimeField field = mock(DateTimeField.class);
        when(field.isLenient()).thenReturn(false);
        when(field.getType()).thenReturn(DateTimeFieldType.dayOfMonth());
        Chronology base = null;

        DateTimeField lenientField = new LenientDateTimeField(field, base);

        long result = lenientField.set(Long.MIN_VALUE, 1);

        assertEquals(Long.MIN_VALUE, result);
    }
}