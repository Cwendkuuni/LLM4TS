import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.MillisDurationField;
import org.joda.time.field.PreciseDateTimeField;
import org.junit.Assert;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {
    
    private static final int LEAP_MONTH = 2; // February
    
    private BasicMonthOfYearDateTimeField basicMonthOfYearDateTimeField = new BasicMonthOfYearDateTimeField(new BasicChronology(), LEAP_MONTH);
    
    @Test
    public void testIsLenient() {
        boolean isLenient = basicMonthOfYearDateTimeField.isLenient();
        Assert.assertFalse(isLenient);
    }
    
    @Test
    public void testGet() {
        long dateTime = 1592629443000L; // Represents June 20, 2020
        int monthOfYear = basicMonthOfYearDateTimeField.get(dateTime);
        Assert.assertEquals(6, monthOfYear);
    }
    
    @Test
    public void testAdd_Int() {
        long dateTime = 1592629443000L; // Represents June 20, 2020
        long newDateTime = basicMonthOfYearDateTimeField.add(dateTime, 5);
        Assert.assertEquals(1605805443000L, newDateTime); // Represents November 20, 2020
    }
    
    @Test
    public void testAdd_Long() {
        long dateTime = 1592629443000L; // Represents June 20, 2020
        long newDateTime = basicMonthOfYearDateTimeField.add(dateTime, 5L);
        Assert.assertEquals(1605805443000L, newDateTime); // Represents November 20, 2020
    }
    
    @Test
    public void testAddWrapField() {
        long dateTime = 1592629443000L; // Represents June 20, 2020
        long newDateTime = basicMonthOfYearDateTimeField.addWrapField(dateTime, 10);
        Assert.assertEquals(1625504643000L, newDateTime); // Represents April 20, 2021
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        long dateTime1 = 1592629443000L; // Represents June 20, 2020
        long dateTime2 = 1605805443000L; // Represents November 20, 2020
        long difference = basicMonthOfYearDateTimeField.getDifferenceAsLong(dateTime2, dateTime1);
        Assert.assertEquals(5L, difference);
    }
    
    @Test
    public void testSet() {
        long dateTime = 1592629443000L; // Represents June 20, 2020
        long newDateTime = basicMonthOfYearDateTimeField.set(dateTime, 11);
        Assert.assertEquals(1608232643000L, newDateTime); // Represents December 20, 2020
    }
    
    @Test
    public void testIsLeap() {
        long dateTime = 1608232643000L; // Represents December 20, 2020
        boolean isLeap = basicMonthOfYearDateTimeField.isLeap(dateTime);
        Assert.assertFalse(isLeap);
        
        long leapDateTime = 1601567684000L; // Represents February 1, 2020
        boolean isLeapLeapDate = basicMonthOfYearDateTimeField.isLeap(leapDateTime);
        Assert.assertTrue(isLeapLeapDate);
    }
    
    @Test
    public void testGetLeapAmount() {
        long dateTime = 1608232643000L; // Represents December 20, 2020
        int leapAmount = basicMonthOfYearDateTimeField.getLeapAmount(dateTime);
        Assert.assertEquals(0, leapAmount);
        
        long leapDateTime = 1601567684000L; // Represents February 1, 2020
        int leapAmountLeapDate = basicMonthOfYearDateTimeField.getLeapAmount(leapDateTime);
        Assert.assertEquals(1, leapAmountLeapDate);
    }
    
    @Test
    public void testGetMinimumValue() {
        int minimumValue = basicMonthOfYearDateTimeField.getMinimumValue();
        Assert.assertEquals(1, minimumValue);
    }
    
    @Test
    public void testGetMaximumValue() {
        int maximumValue = basicMonthOfYearDateTimeField.getMaximumValue();
        Assert.assertEquals(12, maximumValue);
    }
    
    @Test
    public void testRoundFloor() {
        long dateTime = 1592629443000L; // Represents June 20, 2020
        long roundFloorDateTime = basicMonthOfYearDateTimeField.roundFloor(dateTime);
        Assert.assertEquals(1590969600000L, roundFloorDateTime); // Represents June 1, 2020
    }
    
    @Test
    public void testRemainder() {
        long dateTime = 1592629443000L; // Represents June 20, 2020
        long remainder = basicMonthOfYearDateTimeField.remainder(dateTime);
        Assert.assertEquals(165443000L, remainder);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddOutOfRange() {
        long dateTime = 1592629443000L; // Represents June 20, 2020
        basicMonthOfYearDateTimeField.add(dateTime, Integer.MAX_VALUE);
    }
}