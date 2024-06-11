import org.joda.time.field.FieldUtils;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.ReadablePartial;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationField;
import org.joda.time.DateTimeConstants;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {
    
    private BasicMonthOfYearDateTimeField monthField;

    @Before
    public void setUp() {
        BasicChronology chronology = new BasicChronology();
        monthField = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.FEBRUARY);
    }

    @Test
    public void testGet() {
        long instant = DateTimeUtils.currentTimeMillis();
        int expectedMonth = monthField.iChronology.getMonthOfYear(instant);
        
        int actualMonth = monthField.get(instant);
        
        assertEquals(expectedMonth, actualMonth);
    }

    @Test
    public void testAdd() {
        long instant = DateTimeUtils.currentTimeMillis();
        int monthsToAdd = 3;
        long expectedInstant = monthField.add(instant, monthsToAdd);
        
        long actualInstant = instant + monthField.iChronology.getAverageMillisPerMonth() * monthsToAdd;
        
        assertEquals(expectedInstant, actualInstant);
    }

    @Test
    public void testAddWrapField() {
        long instant = DateTimeUtils.currentTimeMillis();
        int monthsToAdd = 2;
        long expectedInstant = monthField.add(instant, monthsToAdd);
        
        long actualInstant = monthField.addWrapField(instant, monthsToAdd);
        
        assertEquals(expectedInstant, actualInstant);
    }

    @Test
    public void testSet() {
        long instant = DateTimeUtils.currentTimeMillis();
        int month = DateTimeConstants.JULY;
        long expectedInstant = monthField.set(instant, month);
        
        long actualInstant = monthField.iChronology.getYearMonthDayMillis(monthField.iChronology.getYear(instant), month, monthField.iChronology.getDayOfMonth(instant));
        
        assertEquals(expectedInstant, actualInstant);
    }

    @Test
    public void testIsLeap() {
        long instant = DateTimeUtils.currentTimeMillis();
        int year = monthField.iChronology.getYear(instant);
        boolean expectedLeap = monthField.iChronology.isLeapYear(year) && monthField.iChronology.getMonthOfYear(instant, year) == monthField.iLeapMonth;
        
        boolean actualLeap = monthField.isLeap(instant);
        
        assertEquals(expectedLeap, actualLeap);
    }

    @Test
    public void testGetLeapAmount() {
        long instant = DateTimeUtils.currentTimeMillis();
        int expectedLeapAmount = monthField.isLeap(instant) ? 1 : 0;
        
        int actualLeapAmount = monthField.getLeapAmount(instant);
        
        assertEquals(expectedLeapAmount, actualLeapAmount);
    }

    @Test
    public void testGetMinimumValue() {
        int expectedMinimumValue = DateTimeConstants.JANUARY;
        
        int actualMinimumValue = monthField.getMinimumValue();
        
        assertEquals(expectedMinimumValue, actualMinimumValue);
    }

    @Test
    public void testGetMaximumValue() {
        int expectedMaximumValue = monthField.iMax;
        
        int actualMaximumValue = monthField.getMaximumValue();
        
        assertEquals(expectedMaximumValue, actualMaximumValue);
    }

}