import org.joda.time.BaseDateTime;
import org.joda.time.BasicDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.ImpreciseDateTimeField;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicAddSubtract() {
        // Create a chronology and a month of year field using it
        Chronology isoChronology = new ISOChronology();
        ImpreciseDateTimeField monthOfYearField = isoChronology.monthOfYear();

        // Prepare test cases
        long instant1 = new BasicDateTime(2012, 1, 31, 12, 0, 0, 0).getMillis();
        long instant2 = new BasicDateTime(2012, 2, 29, 12, 0, 0, 0).getMillis();
        long instant3 = new BasicDateTime(2013, 1, 31, 12, 0, 0, 0).getMillis();
        long instant4 = new BasicDateTime(2013, 2, 28, 12, 0, 0, 0).getMillis();

        // Test add method
        long result1 = monthOfYearField.add(instant1, 1);
        assertEquals(instant2, result1);

        long result2 = monthOfYearField.add(instant2, -1);
        assertEquals(instant1, result2);

        long result3 = monthOfYearField.add(instant3, 12);
        assertEquals(instant4, result3);

        long result4 = monthOfYearField.add(instant4, -12);
        assertEquals(instant3, result4);

        // Test addWrapField method
        long result5 = monthOfYearField.addWrapField(instant1, 13);
        assertEquals(instant3, result5);

        // Test subtract method
        long result6 = monthOfYearField.subtract(instant2, 1);
        assertEquals(instant1, result6);

        long result7 = monthOfYearField.subtract(instant3, 12);
        assertEquals(instant2, result7);

        long result8 = monthOfYearField.subtract(instant4, 13);
        assertEquals(instant3, result8);

        // Test add method with negative months
        long result9 = monthOfYearField.add(instant1, -1);
        assertEquals(instant2, result9);

        long result10 = monthOfYearField.add(instant2, -12);
        assertEquals(instant3, result10);
    }

    @Test
    public void testGetSetMethods() {
        // Create a chronology and a month of year field using it
        Chronology isoChronology = new ISOChronology();
        ImpreciseDateTimeField monthOfYearField = isoChronology.monthOfYear();

        // Prepare test cases
        long instant = new BasicDateTime(2012, 1, 31, 12, 0, 0, 0).getMillis();

        // Test get method
        int month = monthOfYearField.get(instant);
        assertEquals(1, month);

        // Test set method with valid month
        long newInstant = monthOfYearField.set(instant, 2);
        assertEquals(new BasicDateTime(2012, 2, 1, 12, 0, 0, 0).getMillis(), newInstant);

        // Test set method with invalid month
        try {
            monthOfYearField.set(instant, 14);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test set method with negative month
        try {
            monthOfYearField.set(instant, -1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test set method with the same month
        long newInstant2 = monthOfYearField.set(instant, 1);
        assertEquals(instant, newInstant2);
    }

    @Test
    public void testGetMinMaxValues() {
        // Create a chronology and a month of year field using it
        Chronology isoChronology = new ISOChronology();
        ImpreciseDateTimeField monthOfYearField = isoChronology.monthOfYear();

        // Test getMinimumValue and getMaximumValue
        assertEquals(1, monthOfYearField.getMinimumValue());
        assertEquals(12, monthOfYearField.getMaximumValue());
    }

    @Test
    public void testLeapYear() {
        // Create a chronology and a month of year field using it
        Chronology gregorianChronology = new BasicChronology("gregorian", org.joda.time.LocalDate.FEBRUARY, null);
        ImpreciseDateTimeField monthOfYearField = gregorianChronology.monthOfYear();

        // Test isLeap method
        long instant1 = new BasicDateTime(2012, 1, 31, 12, 0, 0, 0).getMillis();
        assertTrue(monthOfYearField.isLeap(instant1));

        long instant2 = new BasicDateTime(2012, 2, 29, 12, 0, 0, 0).getMillis();
        assertTrue(monthOfYearField.isLeap(instant2));

        long instant3 = new BasicDateTime(2012, 2, 28, 12, 0, 0, 0).getMillis();
        assertFalse(monthOfYearField.isLeap(instant3));

        long instant4 = new BasicDateTime(2013, 2, 28, 12, 0, 0, 0).getMillis();
        assertFalse(monthOfYearField.isLeap(instant4));

        long instant5 = new BasicDateTime(2016, 2, 29, 12, 0, 0, 0).getMillis();
        assertTrue(monthOfYearField.isLeap(instant5));

        long instant6 = new BasicDateTime(2017, 2, 28, 12, 0, 0, 0).getMillis();
        assertFalse(monthOfYearField.isLeap(instant6));

        // Test getLeapAmount method
        assertEquals(1, monthOfYearField.getLeapAmount(instant2));
        assertEquals(0, monthOfYearField.getLeapAmount(instant3));
        assertEquals(1, monthOfYearField.getLeapAmount(instant5));
        assertEquals(0, monthOfYearField.getLeapAmount(instant6));
    }

    @Test
    public void testGetRangeDurationField() {
        // Create a chronology and a month of year field using it
        Chronology isoChronology = new ISOChronology();
        ImpreciseDateTimeField monthOfYearField = isoChronology.monthOfYear();

        // Test getRangeDurationField method
        DurationField rangeDurationField = monthOfYearField.getRangeDurationField();
        assertEquals(DurationField.FIFTEEN, rangeDurationField.getValues());
    }
}