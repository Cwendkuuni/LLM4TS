package org.joda.time.chrono;

import org.joda.time.DateTimeConstants;
import org.joda.time.MutableDateTime;
import org.joda.time.field.FieldUtils;
import org.junit.*;

import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField field;
    private MutableDateTime dateTime;

    @Before
    public void setUp() {
        BasicChronology chronology = new BasicChronology();
        int leapMonth = DateTimeConstants.JULY;
        field = new BasicMonthOfYearDateTimeField(chronology, leapMonth);
        dateTime = new MutableDateTime();
    }

    @Test
    public void testGet() {
        dateTime.setYear(2020);
        dateTime.setMonthOfYear(DateTimeConstants.APRIL);
        int result = field.get(dateTime.getMillis());
        assertEquals(DateTimeConstants.APRIL, result);
    }

    @Test
    public void testAdd() {
        dateTime.setYear(2020);
        dateTime.setMonthOfYear(DateTimeConstants.APRIL);
        long result = field.add(dateTime.getMillis(), 3);
        assertEquals(dateTime.getMillis() + 3 * DateTimeConstants.MILLIS_PER_MONTH, result);
    }

    @Test
    public void testSet() {
        dateTime.setYear(2020);
        dateTime.setMonthOfYear(DateTimeConstants.APRIL);
        long result = field.set(dateTime.getMillis(), DateTimeConstants.MARCH);
        assertEquals(dateTime.getMillis() - DateTimeConstants.MILLIS_PER_MONTH, result);
    }

    @Test
    public void testIsLeap() {
        dateTime.setYear(2020);
        dateTime.setMonthOfYear(DateTimeConstants.JULY);
        boolean result = field.isLeap(dateTime.getMillis());
        assertTrue(result);
    }

    @Test
    public void testGetLeapAmount() {
        dateTime.setYear(2020);
        dateTime.setMonthOfYear(DateTimeConstants.JULY);
        int result = field.getLeapAmount(dateTime.getMillis());
        assertEquals(1, result);
    }

    @Test
    public void testGetMinimumValue() {
        int result = field.getMinimumValue();
        assertEquals(1, result);
    }

    @Test
    public void testGetMaximumValue() {
        int result = field.getMaximumValue();
        assertEquals(12, result);
    }
}