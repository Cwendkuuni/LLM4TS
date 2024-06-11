package org.joda.time.chrono;
import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;

public class BasicMonthOfYearDateTimeFieldTest {
    
    private BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
    
    @Test
    public void testIsLenient() {
        assertFalse(field.isLenient());
    }
    
    @Test
    public void testGet() {
        assertEquals(1, field.get(0L));
    }
    
    @Test
    public void testAddInt() {
        assertEquals(31, field.add(0L, 30));
    }
    
    @Test
    public void testAddLong() {
        assertEquals(31, field.add(0L, 30L));
    }
    
    @Test
    public void testAddWithPartial() {
        int[] array = {1};
        int[] result = field.add(null, 0, array, 1);
        assertArrayEquals(new int[]{2}, result);
    }
    
    @Test
    public void testAddWrapField() {
        assertEquals(2, field.addWrapField(1L, 1));
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        assertEquals(1, field.getDifferenceAsLong(2L, 1L));
    }
    
    @Test
    public void testSet() {
        assertEquals(31, field.set(0L, 31));
    }
    
    @Test
    public void testGetRangeDurationField() {
        assertNotNull(field.getRangeDurationField());
    }
    
    @Test
    public void testIsLeap() {
        assertTrue(field.isLeap(0L));
    }
    
    @Test
    public void testGetLeapAmount() {
        assertEquals(1, field.getLeapAmount(0L));
    }
    
    @Test
    public void testGetLeapDurationField() {
        assertNotNull(field.getLeapDurationField());
    }
    
    @Test
    public void testGetMinimumValue() {
        assertEquals(1, field.getMinimumValue());
    }
    
    @Test
    public void testGetMaximumValue() {
        assertEquals(Integer.MAX_VALUE, field.getMaximumValue());
    }
    
    @Test
    public void testRoundFloor() {
        assertEquals(0L, field.roundFloor(1L));
    }
    
    @Test
    public void testRemainder() {
        assertEquals(0L, field.remainder(1L));
    }
}