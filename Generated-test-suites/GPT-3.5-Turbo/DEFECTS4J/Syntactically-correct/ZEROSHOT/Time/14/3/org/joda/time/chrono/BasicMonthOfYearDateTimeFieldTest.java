import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertEquals(false, field.isLenient());
    }
    
    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long instant = 1593172800000L; // 2020-06-27
        assertEquals(6, field.get(instant));
    }

    @Test
    public void testAdd() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long instant = 1593172800000L; // 2020-06-27
        long result = field.add(instant, 3);
        assertEquals(9, field.get(result));
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long instant = 1593172800000L; // 2020-06-27
        long result = field.addWrapField(instant, 9);
        assertEquals(3, field.get(result));
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long minuendInstant = 1593572800000L; // 2020-07-01
        long subtrahendInstant = 1591172800000L; // 2020-06-03
        assertEquals(1, field.getDifferenceAsLong(minuendInstant, subtrahendInstant));
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long instant = 1593172800000L; // 2020-06-27
        long result = field.set(instant, 3);
        assertEquals(3, field.get(result));
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 6); // June is a leap month
        long instant1 = 1593172800000L; // 2020-06-27
        long instant2 = 1596268800000L; // 2020-08-01
        assertEquals(true, field.isLeap(instant1));
        assertEquals(false, field.isLeap(instant2));
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 6); // June is a leap month
        long instant1 = 1593172800000L; // 2020-06-27
        long instant2 = 1596268800000L; // 2020-08-01
        assertEquals(1, field.getLeapAmount(instant1));
        assertEquals(0, field.getLeapAmount(instant2));
    }
    
    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertEquals(1, field.getMinimumValue());
    }
    
    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertEquals(12, field.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long instant = 1593172800000L; // 2020-06-27
        long result = field.roundFloor(instant);
        assertEquals(1590969600000L, result); // 2020-06-01
    }

    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long instant = 1593172800000L; // 2020-06-27
        long result = field.remainder(instant);
        assertEquals(18400000, result);
    }
}