
package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.Months;
import org.joda.time.Partial;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {
    private static final int MIN_YEAR = 1900;
    private static final int MAX_YEAR = 2100;
    
    @Test
    public void testIsLenient() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        assertFalse(field.isLenient());
    }
    
    @Test
    public void testGet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        DateTimeUtils.setCurrentMillisFixed(1546300800000L); // 2019-01-01T00:00:00
        assertEquals(1, field.get(1546300800000L)); // January
    }
    
    @Test
    public void testAdd_int() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        DateTimeUtils.setCurrentMillisFixed(1546300800000L); // 2019-01-01T00:00:00
        long result = field.add(1546300800000L, 2);
        LocalDateTime dt = new LocalDateTime(result);
        assertEquals(2019, dt.getYear());
        assertEquals(3, dt.getMonthOfYear()); // March
    }
    
    @Test
    public void testAdd_long() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        DateTimeUtils.setCurrentMillisFixed(1546300800000L); // 2019-01-01T00:00:00
        long result = field.add(1546300800000L, 2L);
        LocalDateTime dt = new LocalDateTime(result);
        assertEquals(2019, dt.getYear());
        assertEquals(3, dt.getMonthOfYear()); // March
    }
    
    @Test
    public void testAddWrapField() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        DateTimeUtils.setCurrentMillisFixed(1546300800000L); // 2019-01-01T00:00:00
        long result = field.addWrapField(1546300800000L, 12);
        LocalDateTime dt = new LocalDateTime(result);
        assertEquals(2020, dt.getYear());
        assertEquals(1, dt.getMonthOfYear()); // January
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        DateTimeUtils.setCurrentMillisFixed(1546300800000L); // 2019-01-01T00:00:00
        long diff = field.getDifferenceAsLong(1546300800000L, 1577836800000L); // 2020-01-01T00:00:00
        assertEquals(12, diff); // 12 months
    }
    
    @Test
    public void testSet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        DateTimeUtils.setCurrentMillisFixed(1546300800000L); // 2019-01-01T00:00:00
        long result = field.set(1546300800000L, 3); // March
        LocalDateTime dt = new LocalDateTime(result);
        assertEquals(2019, dt.getYear());
        assertEquals(3, dt.getMonthOfYear());
    }
    
    @Test
    public void testIsLeap() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        DateTimeUtils.setCurrentMillisFixed(1546300800000L); // 2019-01-01T00:00:00
        assertFalse(field.isLeap(1546300800000L)); // January is not a leap month
        assertTrue(field.isLeap(1554076800000L)); // March is a leap month
    }
    
    @Test
    public void testGetLeapAmount() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        DateTimeUtils.setCurrentMillisFixed(1546300800000L); // 2019-01-01T00:00:00
        assertEquals(0, field.getLeapAmount(1546300800000L)); // January is not a leap month
        assertEquals(1, field.getLeapAmount(1554076800000L)); // March is a leap month
    }
    
    @Test
    public void testGetMinimumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        assertEquals(1, field.getMinimumValue());
    }
    
    @Test
    public void testGetMaximumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        assertEquals(12, field.getMaximumValue());
    }
    
    @Test
    public void testRoundFloor() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        DateTimeUtils.setCurrentMillisFixed(1546300800000L); // 2019-01-01T00:00:00
        long result = field.roundFloor(1546300800000L);
        LocalDateTime dt = new LocalDateTime(result);
        assertEquals(2019, dt.getYear());
        assertEquals(1, dt.getMonthOfYear()); // January
    }
    
    @Test
    public void testRemainder() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        DateTimeUtils.setCurrentMillisFixed(1546300800000L); // 2019-01-01T00:00:00
        assertEquals(0, field.remainder(1546300800000L));
    }
    
    @Test
    public void testAdd_ReadablePartial() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        DateTimeUtils.setCurrentMillisFixed(1546300800000L); // 2019-01-01T00:00:00
        LocalDateTime dt = new LocalDateTime(2019, 1, 1, 0, 0); // 2019-01-01T00:00:00
        Partial partial = new Partial(dt, new DateTimeFieldType[] { DateTimeFieldType.monthOfYear() });
        assertEquals(2019, field.add(partial, 2, new int[] { 1 }, 0)[0]); // March
    }
}
