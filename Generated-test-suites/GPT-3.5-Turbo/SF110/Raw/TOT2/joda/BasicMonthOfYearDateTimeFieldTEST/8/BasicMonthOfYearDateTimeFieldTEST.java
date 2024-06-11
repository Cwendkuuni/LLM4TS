package org.joda.time.chrono;

import org.joda.time.field.*;
import org.joda.time.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    BasicMonthOfYearDateTimeField field;

    @Test
    public void testIsLenient() {
        field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        assertFalse(field.isLenient());
    }

    // More test cases for other methods...
}