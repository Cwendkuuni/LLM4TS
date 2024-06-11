import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {
    
    private BasicMonthOfYearDateTimeField field;
    
    @Before
    public void setup() {
        field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
    }
    
    @Test
    public void testIsLenient() {
        assertFalse(field.isLenient());
    }
    
    @Test
    public void testGet() {
        assertEquals(1, field.get(1000L));
        assertEquals(5, field.get(2000L));
    }
    
    @Test
    public void testAddInt() {
        assertEquals(1000L, field.add(1000L, 0));
        assertEquals(2500L, field.add(2000L, 5));
    }
    
    @Test
    public void testAddLong() {
        assertEquals(1000L, field.add(1000L, 0L));
        assertEquals(2500L, field.add(2000L, 5L));
    }
    
    @Test
    public void testAddReadablePartial() {
        int[] values = {0, 1, 2};
        assertArrayEquals(new int[]{0, 2, 1}, field.add(null, 0, values, 1));
        assertArrayEquals(new int[]{0, 1, 1}, field.add(null, 0, values, -1));
    }
    
    @Test
    public void testAddWrapField() {
        assertEquals(1005L, field.addWrapField(1000L, 5));
        assertEquals(2003L, field.addWrapField(2000L, -3));
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        assertEquals(-110L, field.getDifferenceAsLong(100L, 210L));
        assertEquals(100L, field.getDifferenceAsLong(200L, 100L));
    }
    
    @Test
    public void testSet() {
        assertEquals(1100L, field.set(1000L, 10));
        assertEquals(2500L, field.set(2000L, 5));
    }
    
    @Test
    public void testGetRangeDurationField() {
        assertNotNull(field.getRangeDurationField());
    }
    
    @Test
    public void testIsLeap() {
        assertTrue(field.isLeap(1000L));
        assertFalse(field.isLeap(2000L));
    }
    
    @Test
    public void testGetLeapAmount() {
        assertEquals(1, field.getLeapAmount(1000L));
        assertEquals(0, field.getLeapAmount(2000L));
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
        assertEquals(12, field.getMaximumValue());
    }
    
    @Test
    public void testRoundFloor() {
        assertEquals(1000L, field.roundFloor(1234L));
    }
    
    @Test
    public void testRemainder() {
        assertEquals(234L, field.remainder(1234L));
    }
    
    @Test
    public void testReadResolve() {
        assertNotNull(field.readResolve());
    }
}