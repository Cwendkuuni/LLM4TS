package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFields;
import org.joda.time.LocalDateTime;
import org.joda.time.ZoneUtils;
import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    private Chronology base;
    private DateTimeField field;
    private LenientDateTimeField lenientField;

    @Before
    public void setUp() {
        base = mock(Chronology.class);
        when(base.getZone()).thenReturn(ZoneUtils.utc());
        field = DateTimeFields.minuteOfHour();
        lenientField = LenientDateTimeField.getInstance(field, base);
    }

    @Test
    public void testGetInstance() {
        DateTimeField lenientField1 = LenientDateTimeField.getInstance(field, base);
        DateTimeField lenientField2 = LenientDateTimeField.getInstance(field, base);
        assertNotSame(field, lenientField1);
        assertNotSame(field, lenientField2);
        assertEquals(lenientField1, lenientField2);
    }

    @Test
    public void testIsLenient() {
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testSet() {
        LocalDateTime dateTime = new LocalDateTime(2000, 1, 1, 1, 0, 0, 0, base);
        long instant = dateTime.toInstant().getMillis();

        int newValue = 60;
        int oldValue = get(instant);
        long difference = newValue - oldValue;

        long newInstant = lenientField.set(instant, newValue);
        assertEquals(instant + difference, newInstant);

        newValue += 10;
        newInstant = lenientField.set(instant, newValue);
        assertEquals(instant + difference + 10 * 60 * 1000, newInstant);
    }
}