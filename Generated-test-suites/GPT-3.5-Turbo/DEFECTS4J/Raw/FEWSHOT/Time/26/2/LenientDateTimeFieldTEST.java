package org.joda.time.field;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTimeField;
import org.joda.time.LocalTime;
import org.joda.time.chrono.GJChronology;
import org.junit.Test;

public class LenientDateTimeFieldTest {
    
    private Chronology GJ_CHRONOLOGY = GJChronology.getInstance();

    @Test
    public void testIsLenient() {
        DateTimeField field = GJ_CHRONOLOGY.secondOfMinute();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, GJ_CHRONOLOGY);
        assertEquals(true, lenientField.isLenient());
    }

    @Test
    public void testSet() {
        DateTimeField field = GJ_CHRONOLOGY.minuteOfHour();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, GJ_CHRONOLOGY);

        LocalTime time = new LocalTime(2020, 1, 1, 12, 30, 0);
        assertEquals(30, lenientField.get(time.getMillis()));
        
        long newInstant = lenientField.set(time.getMillis(), 90);
        LocalTime newTime = new LocalTime(newInstant);
        assertEquals(0, lenientField.get(newTime.getMillis()));
    }

    @Test
    public void testGetInstance() {
        DateTimeField field = GJ_CHRONOLOGY.dayOfMonth();
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, GJ_CHRONOLOGY);
        assertEquals(field, lenientField);
        
        DateTimeField strictField = new StrictDateTimeField(field);
        lenientField = LenientDateTimeField.getInstance(strictField, GJ_CHRONOLOGY);
        assertEquals(field, lenientField);
    }
}