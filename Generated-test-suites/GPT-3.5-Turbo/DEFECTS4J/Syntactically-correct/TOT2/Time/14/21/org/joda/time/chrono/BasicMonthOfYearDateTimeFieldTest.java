import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.DateTimeFieldType;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
        long instant = System.currentTimeMillis();
        assertEquals(field.get(instant), field.get(instant));
    }

    @Test
    public void testAdd() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
        long instant = System.currentTimeMillis();
        int months = 1;
        long newInstant = field.add(instant, months);
        assertEquals(field.get(instant) + months, field.get(newInstant));
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
        long instant = System.currentTimeMillis();
        int months = 14;
        long newInstant = field.addWrapField(instant, months);
        assertEquals(field.get(instant), field.get(newInstant));
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
        long minuendInstant = System.currentTimeMillis();
        long subtrahendInstant = minuendInstant - 1_000_000_000;
        assertEquals(1, field.getDifferenceAsLong(minuendInstant, subtrahendInstant));
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
        long instant = System.currentTimeMillis();
        int month = 6;
        long newInstant = field.set(instant, month);
        assertEquals(month, field.get(newInstant));
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long instant = System.currentTimeMillis();
        assertFalse(field.isLeap(instant));
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long instant = System.currentTimeMillis();
        assertEquals(0, field.getLeapAmount(instant));
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

    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
        long instant = System.currentTimeMillis();
        assertEquals(field.roundFloor(instant), field.roundFloor(instant));
    }

    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
        long instant = System.currentTimeMillis();
        assertEquals(0, field.remainder(instant));
    }
}