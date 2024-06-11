import org.joda.time.chrono.*;
import org.joda.time.*;
import org.junit.*;

public class BasicMonthOfYearDateTimeFieldTest {
    
    // Test isLenient()
	@Test
    public void testIsLenient() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        Assert.assertFalse(field.isLenient());
    }
    
    // Test get(long)
    @Test
    public void testGet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 0L;
        int expected = 1;
        int result = field.get(n);
        Assert.assertEquals(expected, result);
    }
    
    // Test add(long, int)
    @Test
    public void testAddInt() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 0L;
        int n2 = 1;
        long expected = 0L;
        long result = field.add(n, n2);
        Assert.assertEquals(expected, result);
    }
    
    // Test add(long, long)
    @Test
    public void testAddLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 0L;
        long n2 = 1L;
        long expected = 0L;
        long result = field.add(n, n2);
        Assert.assertEquals(expected, result);
    }
    
    // Test add(ReadablePartial, int, int[], int)
    @Test
    public void testAddPartial() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        ReadablePartial readablePartial = null;
        int n = 0;
        int[] array = null;
        int n2 = 0;
        int[] expected = null;
        int[] result = field.add(readablePartial, n, array, n2);
        Assert.assertArrayEquals(expected, result);
    }
    
    // Test addWrapField(long, int)
    @Test
    public void testAddWrapField() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 0L;
        int n2 = 1;
        long expected = 0L;
        long result = field.addWrapField(n, n2);
        Assert.assertEquals(expected, result);
    }
    
    // Test getDifferenceAsLong(long, long)
    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 0L;
        long set = 0L;
        long expected = 0L;
        long result = field.getDifferenceAsLong(n, set);
        Assert.assertEquals(expected, result);
    }
    
    // Test set(long, int)
    @Test
    public void testSet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 0L;
        int n2 = 1;
        long expected = 0L;
        long result = field.set(n, n2);
        Assert.assertEquals(expected, result);
    }
    
    // Test getRangeDurationField()
    @Test
    public void testGetRangeDurationField() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        DurationField expected = chronology.years();
        DurationField result = field.getRangeDurationField();
        Assert.assertEquals(expected, result);
    }
    
    // Test isLeap(long)
    @Test
    public void testIsLeap() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 0L;
        boolean expected = false;
        boolean result = field.isLeap(n);
        Assert.assertEquals(expected, result);
    }
    
    // Test getLeapAmount(long)
    @Test
    public void testGetLeapAmount() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 0L;
        int expected = 0;
        int result = field.getLeapAmount(n);
        Assert.assertEquals(expected, result);
    }
    
    // Test getLeapDurationField()
    @Test
    public void testGetLeapDurationField() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        DurationField expected = chronology.days();
        DurationField result = field.getLeapDurationField();
        Assert.assertEquals(expected, result);
    }
    
    // Test getMinimumValue()
    @Test
    public void testGetMinimumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        int expected = 1;
        int result = field.getMinimumValue();
        Assert.assertEquals(expected, result);
    }
    
    // Test getMaximumValue()
    @Test
    public void testGetMaximumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        int expected = chronology.getMaxMonth();
        int result = field.getMaximumValue();
        Assert.assertEquals(expected, result);
    }
    
    // Test roundFloor(long)
    @Test
    public void testRoundFloor() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 0L;
        long expected = 0L;
        long result = field.roundFloor(n);
        Assert.assertEquals(expected, result);
    }
    
    // Test remainder(long)
    @Test
    public void testRemainder() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 0L;
        long expected = 0L;
        long result = field.remainder(n);
        Assert.assertEquals(expected, result);
    }
    
    // Test readResolve()
    @Test
    public void testReadResolve() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        Object expected = chronology.monthOfYear();
        Object result = field.readResolve();
        Assert.assertEquals(expected, result);
    }
    
}