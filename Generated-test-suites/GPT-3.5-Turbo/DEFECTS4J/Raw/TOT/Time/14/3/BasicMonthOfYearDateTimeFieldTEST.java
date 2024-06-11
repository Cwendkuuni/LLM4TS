import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testGet() {
        // typical use case
        DateTime dateTime = new DateTime(2021, 5, 15, 0, 0, 0);
        BasicMonthOfYearDateTimeField monthOfYear = new BasicMonthOfYearDateTimeField(dateTime);
        int month = monthOfYear.get();
        assertEquals(5, month);
        
        // edge case
        DateTime dateTime2 = new DateTime(2022, 1, 1, 0, 0, 0);
        BasicMonthOfYearDateTimeField monthOfYear2 = new BasicMonthOfYearDateTimeField(dateTime2);
        int month2 = monthOfYear2.get();
        assertEquals(1, month2);
        
        // error scenario
        DateTime dateTime3 = null;
        BasicMonthOfYearDateTimeField monthOfYear3 = new BasicMonthOfYearDateTimeField(dateTime3);
        int month3 = monthOfYear3.get();
        assertEquals(0, month3);
    }

    @Test
    public void testAdd() {
        // typical use case
        DateTime dateTime = new DateTime(2021, 5, 15, 0, 0, 0);
        BasicMonthOfYearDateTimeField monthOfYear = new BasicMonthOfYearDateTimeField(dateTime);
        DateTime newDateTime = monthOfYear.add(2);
        assertEquals(7, newDateTime.getMonthOfYear());
        
        // edge case
        DateTime dateTime2 = new DateTime(2021, 11, 30, 0, 0, 0);
        BasicMonthOfYearDateTimeField monthOfYear2 = new BasicMonthOfYearDateTimeField(dateTime2);
        DateTime newDateTime2 = monthOfYear2.add(2);
        assertEquals(1, newDateTime2.getMonthOfYear());
        assertEquals(2022, newDateTime2.getYear());
        
        // error scenario
        DateTime dateTime3 = null;
        BasicMonthOfYearDateTimeField monthOfYear3 = new BasicMonthOfYearDateTimeField(dateTime3);
        DateTime newDateTime3 = monthOfYear3.add(2);
        assertEquals(0, newDateTime3.getMonthOfYear());
    }

    @Test
    public void testSet() {
        // typical use case
        DateTime dateTime = new DateTime(2021, 5, 15, 0, 0, 0);
        BasicMonthOfYearDateTimeField monthOfYear = new BasicMonthOfYearDateTimeField(dateTime);
        DateTime newDateTime = monthOfYear.set(2);
        assertEquals(2, newDateTime.getMonthOfYear());
        
        // edge case
        DateTime dateTime2 = new DateTime(2022, 2, 28, 0, 0, 0);
        BasicMonthOfYearDateTimeField monthOfYear2 = new BasicMonthOfYearDateTimeField(dateTime2);
        DateTime newDateTime2 = monthOfYear2.set(4);
        assertEquals(4, newDateTime2.getMonthOfYear());
        assertEquals(30, newDateTime2.getDayOfMonth());
        
        // error scenario
        DateTime dateTime3 = null;
        BasicMonthOfYearDateTimeField monthOfYear3 = new BasicMonthOfYearDateTimeField(dateTime3);
        DateTime newDateTime3 = monthOfYear3.set(2);
        assertEquals(0, newDateTime3.getMonthOfYear());
    }

}