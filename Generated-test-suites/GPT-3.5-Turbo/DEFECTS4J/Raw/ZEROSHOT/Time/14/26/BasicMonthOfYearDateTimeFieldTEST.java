import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalTime;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.junit.Before;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField field;
    private DateTime dateTime;

    @Before
    public void setUp() {
        BasicChronology chronology = new BasicChronology();
        field = new BasicMonthOfYearDateTimeField(chronology, 1);
        dateTime = new DateTime();
    }

    @Test
    public void testIsLenient() {
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        assertEquals(dateTime.getMonthOfYear(), field.get(dateTime.getMillis()));
    }

    @Test
    public void testAdd() {
        DateTime newDateTime = field.add(dateTime, 2);
        assertEquals(dateTime.getMillis() + 2 * field.getAverageMillisPerUnit(), newDateTime.getMillis());
    }

    @Test
    public void testAddWrapField() {
        DateTime newDateTime = field.addWrapField(dateTime, 2);
        assertEquals(dateTime.getMonthOfYear() + 2, newDateTime.getMonthOfYear());
    }

    @Test
    public void testGetDifferenceAsLong() {
        DateTime otherDateTime = dateTime.plusMonths(3);
        assertEquals(3, field.getDifferenceAsLong(otherDateTime.getMillis(), dateTime.getMillis()));
    }

    @Test
    public void testSet() {
        DateTime newDateTime = field.set(dateTime, 6);
        assertEquals(6, newDateTime.getMonthOfYear());
    }

    @Test
    public void testIsLeap() {
        assertFalse(field.isLeap(dateTime.getMillis()));
    }

    @Test
    public void testGetLeapAmount() {
        assertEquals(0, field.getLeapAmount(dateTime.getMillis()));
    }

    @Test
    public void testGetMinimumValue() {
        assertEquals(1, field.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        assertEquals(12, field.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        DateTime newDateTime = field.roundFloor(dateTime.getMillis());
        assertEquals(dateTime.getYear(), newDateTime.getYear());
        assertEquals(dateTime.getMonthOfYear(), newDateTime.getMonthOfYear());
        assertEquals(1, newDateTime.getDayOfMonth());
        assertEquals(0, newDateTime.getHourOfDay());
        assertEquals(0, newDateTime.getMinuteOfHour());
        assertEquals(0, newDateTime.getSecondOfMinute());
        assertEquals(0, newDateTime.getMillisOfSecond());
    }

    @Test
    public void testRemainder() {
        LocalTime time = new LocalTime();
        DateTime newDateTime = dateTime.withTime(time);
        long remainder = field.remainder(newDateTime.getMillis());
        assertEquals(time.getMillisOfDay(), remainder);
    }
}