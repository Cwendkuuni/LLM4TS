import org.junit.Test;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.joda.time.field.FieldUtils;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertFalse(field.isLenient());
    }
    
    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = FieldUtils.getMillisOfDay(2018, 9, 10, 0, 0);
        int result = field.get(n);
        assertEquals(9, result);
    }
    
    @Test
    public void testAdd_zero() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = FieldUtils.getMillisOfDay(2018, 9, 10, 0, 0);
        long result = field.add(n, 0);
        assertEquals(n, result);
    }
    
    @Test
    public void testAdd_positive() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = FieldUtils.getMillisOfDay(2018, 9, 10, 0, 0);
        long result = field.add(n, 3);
        assertEquals(FieldUtils.getMillisOfDay(2018, 12, 10, 0, 0), result);
    }
    
    @Test
    public void testAdd_negative() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = FieldUtils.getMillisOfDay(2018, 9, 10, 0, 0);
        long result = field.add(n, -3);
        assertEquals(FieldUtils.getMillisOfDay(2018, 6, 10, 0, 0), result);
    }
    
    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = FieldUtils.getMillisOfDay(2018, 9, 10, 0, 0);
        long result = field.addWrapField(n, 3);
        assertEquals(FieldUtils.getMillisOfDay(2018, 12, 10, 0, 0), result);
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n1 = FieldUtils.getMillisOfDay(2019, 5, 17, 0, 0);
        long n2 = FieldUtils.getMillisOfDay(2018, 9, 10, 0, 0);
        long result = field.getDifferenceAsLong(n1, n2);
        assertEquals(8, result);
    }
    
    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = FieldUtils.getMillisOfDay(2018, 9, 10, 0, 0);
        long result = field.set(n, 3);
        assertEquals(FieldUtils.getMillisOfDay(2018, 3, 10, 0, 0), result);
    }
    
    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 3);
        long n1 = FieldUtils.getMillisOfDay(2019, 5, 17, 0, 0);
        long n2 = FieldUtils.getMillisOfDay(2020, 5, 17, 0, 0);
        assertFalse(field.isLeap(n1));
        assertTrue(field.isLeap(n2));
    }
    
    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 3);
        long n1 = FieldUtils.getMillisOfDay(2019, 5, 17, 0, 0);
        long n2 = FieldUtils.getMillisOfDay(2020, 5, 17, 0, 0);
        assertEquals(0, field.getLeapAmount(n1));
        assertEquals(1, field.getLeapAmount(n2));
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
        long n = FieldUtils.getMillisOfDay(2018, 9, 10, 0, 0);
        long result = field.roundFloor(n);
        assertEquals(FieldUtils.getMillisOfDay(2018, 9, 1, 0, 0), result);
    }
    
    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = FieldUtils.getMillisOfDay(2018, 9, 10, 0, 0);
        long result = field.remainder(n);
        assertEquals(10 * FieldUtils.MILLIS_PER_HOUR, result);
    }
}