import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.DateTime;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
        DateTime dateTime = new DateTime(2021, 6, 15, 0, 0); // June
        assertEquals(6, field.get(dateTime.getMillis()));
    }

    @Test
    public void testAdd() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
        DateTime dateTime = new DateTime(2021, 6, 15, 0, 0); // June
        long newTime = field.add(dateTime.getMillis(), 2);
        DateTime newDateTime = new DateTime(newTime);
        assertEquals(8, field.get(newTime));
        assertEquals(1, newDateTime.getDayOfMonth()); // Edge case: wrapping month change to maintain day
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
        DateTime dateTime = new DateTime(2021, 12, 15, 0, 0); // December
        long newTime = field.addWrapField(dateTime.getMillis(), 2);
        DateTime newDateTime = new DateTime(newTime);
        assertEquals(2, field.get(newTime));
        assertEquals(15, newDateTime.getDayOfMonth()); // Edge case: wrapping month change without day change
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
        DateTime dateTime = new DateTime(2021, 6, 15, 0, 0); // June
        long newTime = field.set(dateTime.getMillis(), 11);
        assertEquals(11, field.get(newTime));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidMonth() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
        DateTime dateTime = new DateTime(2021, 6, 15, 0, 0); // June
        field.set(dateTime.getMillis(), 13); // Should throw IllegalArgumentException
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 2); // February is the leap month
        DateTime leapDateTime = new DateTime(2020, 2, 15, 0, 0); // February in a leap year
        DateTime nonLeapDateTime = new DateTime(2021, 2, 15, 0, 0); // February in a non-leap year
        assertTrue(field.isLeap(leapDateTime.getMillis()));
        assertFalse(field.isLeap(nonLeapDateTime.getMillis()));
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 2); // February is the leap month
        DateTime leapDateTime = new DateTime(2020, 2, 15, 0, 0); // February in a leap year
        DateTime nonLeapDateTime = new DateTime(2021, 2, 15, 0, 0); // February in a non-leap year
        assertEquals(1, field.getLeapAmount(leapDateTime.getMillis()));
        assertEquals(0, field.getLeapAmount(nonLeapDateTime.getMillis()));
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
        assertEquals(1, field.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
        assertEquals(12, field.getMaximumValue());
    }
}