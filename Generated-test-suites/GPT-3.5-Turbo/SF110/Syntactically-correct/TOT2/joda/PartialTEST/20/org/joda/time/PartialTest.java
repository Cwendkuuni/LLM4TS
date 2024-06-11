package org.joda.time;
import static org.junit.Assert.*;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.junit.Test;

public class PartialTest {
    
    @Test
    public void testPartialConstructorWithChronology() {
        Partial partial = new Partial(DateTimeFieldType.dayOfWeek(), 1);
        assertNotNull(partial);
        assertEquals(1, partial.size());
        assertEquals(DateTimeFieldType.dayOfWeek(), partial.getFieldType(0));
        assertEquals(1, partial.getValue(0));
    }
    
    @Test
    public void testPartialConstructorWithNullChronology() {
        try {
            new Partial((Chronology) null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    
    @Test
    public void testPartialConstructorWithNullFieldType() {
        try {
            new Partial((DateTimeFieldType) null, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    
    @Test
    public void testPartialConstructorWithNullArrays() {
        try {
            new Partial((DateTimeFieldType[]) null, (int[]) null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
}