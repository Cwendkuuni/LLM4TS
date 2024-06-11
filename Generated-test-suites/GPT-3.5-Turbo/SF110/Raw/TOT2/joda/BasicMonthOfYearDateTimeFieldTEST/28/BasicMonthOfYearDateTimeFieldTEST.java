package org.joda.time.chrono;

import static org.junit.Assert.*;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField();
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField();
        assertEquals(1, field.get(0));
    }

    // More test cases for other methods

    @Test
    public void testReadResolve() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField();
        assertEquals(DateTimeFieldType.monthOfYear(), field.readResolve());
    }
}