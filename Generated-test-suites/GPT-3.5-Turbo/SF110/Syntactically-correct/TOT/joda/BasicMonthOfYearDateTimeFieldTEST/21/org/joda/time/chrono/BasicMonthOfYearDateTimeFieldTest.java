import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;

public class BasicMonthOfYearDateTimeFieldTest {
  
    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertFalse(field.isLenient());
    }
  
    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long millis = 0;
        assertEquals(1, field.get(millis));
        
        millis = new LocalDate(2021, 8, 1).toDateTimeAtStartOfDay().getMillis();
        assertEquals(8, field.get(millis));
    }
  
    @Test
    public void testAdd_Int() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long millis = 0;
        int amount = 2;
        long expectedMillis = 0;
        
        expectedMillis = new LocalDate(1970, 1, 1).toDateTimeAtStartOfDay().getMillis();
        assertEquals(expectedMillis, field.add(millis, amount));
        
        millis = new LocalDate(2021, 8, 1).toDateTimeAtStartOfDay().getMillis();
        expectedMillis = new LocalDate(2021, 10, 1).toDateTimeAtStartOfDay().getMillis();
        assertEquals(expectedMillis, field.add(millis, amount));
    }
  
    @Test
    public void testAdd_Long() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long millis = 0;
        long amount = 2L;
        long expectedMillis = 0L;
        
        expectedMillis = new LocalDate(1970, 1, 1).toDateTimeAtStartOfDay().getMillis();
        assertEquals(expectedMillis, field.add(millis, amount));
        
        millis = new LocalDate(2021, 8, 1).toDateTimeAtStartOfDay().getMillis();
        expectedMillis = new LocalDate(2021, 10, 1).toDateTimeAtStartOfDay().getMillis();
        assertEquals(expectedMillis, field.add(millis, amount));
    }
  
    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long millis = 0;
        int amount = 2;
        long expectedMillis = 0;
        
        expectedMillis = new LocalDate(1970, 1, 1).toDateTimeAtStartOfDay().getMillis();
        assertEquals(expectedMillis, field.addWrapField(millis, amount));
        
        millis = new LocalDate(2021, 8, 1).toDateTimeAtStartOfDay().getMillis();
        expectedMillis = new LocalDate(2021, 10, 1).toDateTimeAtStartOfDay().getMillis();
        assertEquals(expectedMillis, field.addWrapField(millis, amount));
    }
  
    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long millis1 = 0;
        long millis2 = 0;
        long expectedDifference = 0;
        
        millis1 = new LocalDate(1970, 1, 1).toDateTimeAtStartOfDay().getMillis();
        millis2 = new LocalDate(1970, 1, 1).toDateTimeAtStartOfDay().getMillis();
        expectedDifference = 0L;
        assertEquals(expectedDifference, field.getDifferenceAsLong(millis1, millis2));
        
        millis1 = new LocalDate(2021, 8, 1).toDateTimeAtStartOfDay().getMillis();
        millis2 = new LocalDate(2021, 9, 1).toDateTimeAtStartOfDay().getMillis();
        expectedDifference = 1L;
        assertEquals(expectedDifference, field.getDifferenceAsLong(millis1, millis2));
    }
  
    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long millis = 0;
        int value = 8;
        long expectedMillis = 0;
        
        expectedMillis = new LocalDate(1970, 1, 1).toDateTimeAtStartOfDay().getMillis();
        assertEquals(expectedMillis, field.set(millis, value));
        
        millis = new LocalDate(2021, 8, 1).toDateTimeAtStartOfDay().getMillis();
        expectedMillis = new LocalDate(2021, 9, 1).toDateTimeAtStartOfDay().getMillis();
        assertEquals(expectedMillis, field.set(millis, value));
    }
  
    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 8);
        long millis = 0;
        assertTrue(field.isLeap(millis));
    }
  
    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 8);
        long millis = 0;
        assertEquals(1, field.getLeapAmount(millis));
    }
  
    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertEquals(1, field.getMinimumValue());
    }
  
    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        int maxMonth = Integer.MAX_VALUE;
        assertEquals(maxMonth, field.getMaximumValue());
    }
  
    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long millis = 0;
        long expectedMillis = 0;
        
        millis = new LocalDate(1970, 1, 1).toDateTimeAtStartOfDay().getMillis();
        expectedMillis = new LocalDate(1970, 1, 1).toDateTimeAtStartOfDay().getMillis();
        assertEquals(expectedMillis, field.roundFloor(millis));
        
        millis = new LocalDate(2021, 8, 15).toDateTimeAtStartOfDay().getMillis();
        expectedMillis = new LocalDate(2021, 8, 1).toDateTimeAtStartOfDay().getMillis();
        assertEquals(expectedMillis, field.roundFloor(millis));
    }
  
    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long millis = 0;
        long expectedMillis = 0;
        
        millis = new LocalDate(1970, 1, 1).toDateTimeAtStartOfDay().getMillis();
        expectedMillis = 0;
        assertEquals(expectedMillis, field.remainder(millis));
        
        millis = new LocalDate(2021, 8, 15).toDateTimeAtStartOfDay().getMillis();
        expectedMillis = new LocalDate(2021, 8, 15).toDateTimeAtStartOfDay().getMillis();
        assertEquals(expectedMillis, field.remainder(millis));
    }
  
    @Test
    public void testGetRangeDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField((BasicChronology)null, 0);
        assertEquals(null, field.getRangeDurationField());
    }

    @Test
    public void testReadResolve() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertEquals(DateTimeFieldType.monthOfYear(), field.readResolve());
    }
  
    // Error scenarios
  
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_Int_InvalidAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long millis = 0;
        int amount = -1;
        field.add(millis, amount);
    }
  
    @Test(expected = IllegalArgumentException.class)
    public void testSet_InvalidValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long millis = 0;
        int value = 0;
        field.set(millis, value);
    }
  
}