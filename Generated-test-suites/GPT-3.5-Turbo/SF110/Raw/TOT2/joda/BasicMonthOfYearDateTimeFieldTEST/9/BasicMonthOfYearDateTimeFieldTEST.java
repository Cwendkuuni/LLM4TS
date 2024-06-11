
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        assertEquals(false, field.isLenient());
    }

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = System.currentTimeMillis();
        assertEquals(field.iChronology.getMonthOfYear(n), field.get(n));
    }

    @Test
    public void testAddInt() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = System.currentTimeMillis();
        int n2 = 5;
        assertEquals(n + field.iChronology.getAverageMillisPerMonth() * n2, field.add(n, n2));
    }

    @Test
    public void testAddLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = System.currentTimeMillis();
        long n2 = 5;
        assertEquals(n + field.iChronology.getAverageMillisPerMonth() * n2, field.add(n, n2));
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = System.currentTimeMillis();
        assertEquals(field.iChronology.getMonthOfYear(n), field.addWrapField(n, 0));
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = System.currentTimeMillis();
        long set = System.currentTimeMillis() + 10000;
        assertEquals(field.getDifference(set, n), -field.getDifference(n, set));
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = System.currentTimeMillis();
        int n2 = 5;
        long expected = field.iChronology.getYearMonthDayMillis(field.iChronology.getYear(n), n2, field.iChronology.getDayOfMonth(n, field.iChronology.getYear(n)));
        assertEquals(expected, field.set(n, n2));
    }

    @Test
    public void testGetRangeDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        assertEquals(field.iChronology.years(), field.getRangeDurationField());
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = System.currentTimeMillis();
        int year = field.iChronology.getYear(n);
        assertEquals(field.iChronology.isLeapYear(year) && field.iChronology.getMonthOfYear(n, year) == field.iLeapMonth, field.isLeap(n));
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = System.currentTimeMillis();
        assertEquals(field.iChronology.isLeapYear(field.iChronology.getYear(n)) ? 1 : 0, field.getLeapAmount(n));
    }

    @Test
    public void testGetLeapDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        assertEquals(field.iChronology.days(), field.getLeapDurationField());
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        assertEquals(1, field.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        assertEquals(field.iMax, field.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = System.currentTimeMillis();
        int year = field.iChronology.getYear(n);
        assertEquals(field.iChronology.getYearMonthMillis(year, field.iChronology.getMonthOfYear(n, year)), field.roundFloor(n));
    }

    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = System.currentTimeMillis();
        assertEquals(n - field.roundFloor(n), field.remainder(n));
    }

    @Test
    public void testReadResolve() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        assertEquals(field.iChronology.monthOfYear(), field.readResolve());
    }

}

