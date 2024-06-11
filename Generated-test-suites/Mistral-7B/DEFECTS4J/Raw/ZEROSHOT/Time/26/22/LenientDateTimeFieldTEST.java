package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ZoneUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the LenientDateTimeField class.
 */
public class LenientDateTimeFieldTest {

    private Chronology base;
    private DateTimeField field;
    private DateTimeField lenientField;

    @Before
    public void setUp() {
        base = mock(Chronology.class);
        field = mock(DateTimeField.class);
        when(field.getBaseDate()).thenReturn(new LocalDateTime());
        when(field.getType()).thenReturn(DateTimeFieldType.dayOfMonth());
        when(field.getBaseDate().getChronology()).thenReturn(base);
        lenientField = LenientDateTimeField.getInstance(field, base);
    }

    @Test
    public void testGetInstance() {
        DateTimeField strictField = new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), base);
        assertEquals(lenientField, LenientDateTimeField.getInstance(lenientField, base));
        assertNotSame(lenientField, LenientDateTimeField.getInstance(strictField, base));
        assertNull(LenientDateTimeField.getInstance(null, base));
    }

    @Test
    public void testIsLenient() {
        assertEquals(true, lenientField.isLenient());
        assertEquals(false, field.isLenient());
    }

    @Test
    public void testSet() {
        MutableDateTime dt = new MutableDateTime();
        LocalDateTime localDateTime = new LocalDateTime(base, new LocalDate(2018, 1, 1));
        long utc = ZoneUtils.toUTC(localDateTime.toDate());
        long value = DateTimeConstants.DAYS_PER_WEEK * 5 + 2;

        dt.setDateTime(utc);
        long originalValue = lenientField.get(dt.toLong());

        lenientField.set(dt.toLong(), value);
        long newValue = lenientField.get(dt.toLong());
        assertEquals(value, newValue);
        assertEquals(originalValue + (value - getValue(originalValue, DateTimeFieldType.dayOfWeek())), dt.getMillis());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetThrowsException() {
        lenientField.set(123456789L, 60);
    }

    private int getValue(long instant, DateTimeFieldType fieldType) {
        MutableDateTime dt = new MutableDateTime(instant);
        return fieldType.get(dt);
    }
}