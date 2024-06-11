import org.junit.Test;
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
        assertEquals(1, field.get(1000));
    }
    
    @Test
    public void testAddWithZero() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertEquals(1000, field.add(1000, 0));
    }
    
    @Test
    public void testAddWithPositiveValueNoOverflow() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertEquals(1006, field.add(1000, 6));
    }
    
    @Test
    public void testAddWithNegativeValueNoOverflow() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertEquals(994, field.add(1000, -6));
    }
    
    @Test
    public void testAddWithPositiveValueOverflow() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertEquals(2006, field.add(1000, 1006));
    }
    
    @Test
    public void testAddWithNegativeValueOverflow() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertEquals(994, field.add(1000, -1006));
    }
    
    @Test
    public void testAddWithLongValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertEquals(123456789, field.add(1000, 123456789L));
    }
    
    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertEquals(1003, field.addWrapField(1000, 2));
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertEquals(6, field.getDifferenceAsLong(1006, 1000));
    }
    
    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertEquals(1006, field.set(1000, 6));
    }
    
    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 4);
        assertTrue(field.isLeap(1000));
        assertFalse(field.isLeap(1001));
    }
    
    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 4);
        assertEquals(1, field.getLeapAmount(1000));
        assertEquals(0, field.getLeapAmount(1001));
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
        assertEquals(1000, field.roundFloor(1055));
    }
    
    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertEquals(55, field.remainder(1055));
    }
}