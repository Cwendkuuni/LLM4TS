package org.joda.time.chrono;

import org.joda.time.chrono.*;
import org.joda.time.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertFalse(field.isLenient());
    }

    // Add more test cases for other methods as well...
}
