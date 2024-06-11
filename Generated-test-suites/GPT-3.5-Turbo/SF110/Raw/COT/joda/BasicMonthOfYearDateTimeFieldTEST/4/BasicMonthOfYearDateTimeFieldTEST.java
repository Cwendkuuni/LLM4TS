
import static org.junit.Assert.*;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {
    
    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        assertFalse(field.isLenient());
    }
    
    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        assertEquals(1, field.get(0L));
    }
    
    @Test
    public void testAdd_long_int() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        assertEquals(0L, field.add(0L, 0));
        assertEquals(1L, field.add(0L, 1));
    }
    
    @Test
    public void testAdd_long_long() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        assertEquals(0L, field.add(0L, 0L));
        assertEquals(1L, field.add(0L, 1L));
    }
    
    @Test
    public void testAdd_readablePartial_int_array_int() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        int[] array = {1};
        assertArrayEquals(array, field.add(null, 0, array, 0));
    }
    
    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        assertEquals(1L, field.addWrapField(0L, 1));
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        assertEquals(0L, field.getDifferenceAsLong(0L, 0L));
    }
    
    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        assertEquals(0L, field.set(0L, 1));
    }
    
    @Test
    public void testGetRangeDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        assertNotNull(field.getRangeDurationField());
    }
    
    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        assertFalse(field.isLeap(0L));
    }
    
    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        assertEquals(0, field.getLeapAmount(0L));
    }
    
    @Test
    public void testGetLeapDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        assertNotNull(field.getLeapDurationField());
    }
    
    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        assertEquals(1, field.getMinimumValue());
    }
    
    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        assertEquals(Integer.MAX_VALUE, field.getMaximumValue());
    }
    
    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        assertEquals(0L, field.roundFloor(0L));
    }
    
    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        assertEquals(0L, field.remainder(0L));
    }
}
