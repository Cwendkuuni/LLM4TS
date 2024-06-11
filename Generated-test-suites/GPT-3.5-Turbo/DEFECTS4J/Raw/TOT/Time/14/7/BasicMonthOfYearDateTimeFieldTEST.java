import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.LocalDate;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.junit.Before;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField monthOfYearField;
    
    @Before
    public void setup() {
        BasicChronology chronology = new BasicChronology();
        monthOfYearField = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.FEBRUARY);
    }

    @Test
    public void testGet() {
        long january = new LocalDate(2022, 1, 1).toDate().getTime();
        assertEquals(1, monthOfYearField.get(january));
        
        long february = new LocalDate(2022, 2, 1).toDate().getTime();
        assertEquals(2, monthOfYearField.get(february));
    }

    @Test
    public void testAdd() {
        long january = new LocalDate(2022, 1, 1).toDate().getTime();
        long february = monthOfYearField.add(january, 1);
        long expected = new LocalDate(2022, 2, 1).toDate().getTime();
        assertEquals(expected, february);
    }

    @Test
    public void testAddWrapField() {
        long january = new LocalDate(2022, 1, 1).toDate().getTime();
        long december = monthOfYearField.addWrapField(january, 11);
        long expected = new LocalDate(2022, 12, 1).toDate().getTime();
        assertEquals(expected, december);
    }

    @Test
    public void testGetDifferenceAsLong() {
        long january = new LocalDate(2022, 1, 1).toDate().getTime();
        long march = new LocalDate(2022, 3, 1).toDate().getTime();
        long difference = monthOfYearField.getDifferenceAsLong(march, january);
        assertEquals(1, difference);
    }

    @Test
    public void testIsLeap() {
        long january = new LocalDate(2022, 1, 1).toDate().getTime();
        assertFalse(monthOfYearField.isLeap(january));
        
        long februaryLeap = new LocalDate(2022, 2, 1).toDate().getTime();
        assertTrue(monthOfYearField.isLeap(februaryLeap));
    }

    @Test
    public void testGetLeapAmount() {
        long january = new LocalDate(2022, 1, 1).toDate().getTime();
        assertEquals(0, monthOfYearField.getLeapAmount(january));
        
        long februaryLeap = new LocalDate(2022, 2, 1).toDate().getTime();
        assertEquals(1, monthOfYearField.getLeapAmount(februaryLeap));
    }

    @Test
    public void testGetMinimumValue() {
        assertEquals(DateTimeConstants.JANUARY, monthOfYearField.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        assertEquals(DateTimeConstants.DECEMBER, monthOfYearField.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        long midJanuary = new LocalDate(2022, 1, 15).toDate().getTime();
        long expected = new LocalDate(2022, 1, 1).toDate().getTime();
        assertEquals(expected, monthOfYearField.roundFloor(midJanuary));
    }

    @Test
    public void testRemainder() {
        long midJanuary = new LocalDate(2022, 1, 15).toDate().getTime();
        long remainder = monthOfYearField.remainder(midJanuary);
        assertEquals(0, remainder);
    }

}