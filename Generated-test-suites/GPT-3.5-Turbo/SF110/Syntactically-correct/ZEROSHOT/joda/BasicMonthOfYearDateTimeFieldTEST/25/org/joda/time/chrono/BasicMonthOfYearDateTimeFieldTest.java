import org.joda.time.field.*;
import org.joda.time.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {
    
    @Test
    public void testIsLenient() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 5);
        assertFalse(field.isLenient());
    }
    
    @Test
    public void testGet() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 5);
        long n = 123456789;
        assertEquals(6, field.get(n));
    }
    
    @Test
    public void testAdd_int() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 5);
        long n = 123456789;
        int n2 = 2;
        assertEquals(123456789, field.add(n, 0));
    }
    
    @Test
    public void testAdd_long() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 5);
        long n = 123456789;
        long n2 = 2;
        assertEquals(123456789, field.add(n, 0));
    }
    
    @Test
    public void testAdd_ReadablePartial_int() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 5);
        ReadablePartial readablePartial = new ReadablePartial();
        int n = 0;
        int[] array = new int[3];
        int n2 = 2;
        assertArrayEquals(array, field.add(readablePartial, n, array, n2));
    }
    
    @Test
    public void testAddWrapField() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 5);
        long n = 123456789;
        int n2 = 2;
        assertEquals(123456789, field.addWrapField(n, n2));
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 5);
        long n = 123456789;
        long set = 987654321;
        assertEquals(-8, field.getDifferenceAsLong(n, set));
    }
    
    @Test
    public void testSet() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 5);
        long n = 123456789;
        int n2 = 6;
        assertEquals(123456789, field.set(n, n2));
    }
    
    @Test
    public void testGetRangeDurationField() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 5);
        assertNotNull(field.getRangeDurationField());
    }
    
    @Test
    public void testIsLeap() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 5);
        long n = 123456789;
        assertTrue(field.isLeap(n));
    }
    
    @Test
    public void testGetLeapAmount() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 5);
        long n = 123456789;
        assertEquals(1, field.getLeapAmount(n));
    }
    
    @Test
    public void testGetLeapDurationField() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 5);
        assertNotNull(field.getLeapDurationField());
    }
    
    @Test
    public void testGetMinimumValue() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 5);
        assertEquals(1, field.getMinimumValue());
    }
    
    @Test
    public void testGetMaximumValue() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 5);
        assertEquals(iChronology.getMaxMonth(), field.getMaximumValue());
    }
    
    @Test
    public void testRoundFloor() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 5);        
        long n = 123456789;
        assertEquals(123456789, field.roundFloor(n));
    }
    
    @Test
    public void testRemainder() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 5);        
        long n = 123456789;
        assertEquals(0, field.remainder(n));
    }
    
    @Test
    public void testReadResolve() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 5);
        assertNotNull(field.readResolve());
    }
}