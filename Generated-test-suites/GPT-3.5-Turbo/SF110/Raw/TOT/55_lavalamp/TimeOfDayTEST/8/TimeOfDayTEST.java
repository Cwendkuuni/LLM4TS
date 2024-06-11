package net.sf.lavalamp.device;

import org.junit.Assert;
import org.junit.Test;

public class TimeOfDayTest {
    @Test
    public void testConstructorWithIntParams_ValidInput_ReturnsTimeOfDayObject() {
        // Arrange
        int hours = 10;
        int minutes = 30;
        
        // Act
        TimeOfDay timeOfDay = new TimeOfDay(hours, minutes);
        
        // Assert
        Assert.assertEquals(hours, (int) timeOfDay.getHours());
        Assert.assertEquals(minutes, (int) timeOfDay.getMinutes());
    }
    
    @Test
    public void testConstructorWithStringParam_ValidInput_ReturnsTimeOfDayObject() {
        // Arrange
        String time = "12:45";
        
        // Act
        TimeOfDay timeOfDay = new TimeOfDay(time);
        
        // Assert
        Assert.assertEquals(12, (int) timeOfDay.getHours());
        Assert.assertEquals(45, (int) timeOfDay.getMinutes());
    }
    
    @Test(expected = NumberFormatException.class)
    public void testConstructorWithStringParam_InvalidInput_ThrowsNumberFormatException() {
        // Arrange
        String time = "12:4a";
        
        // Act
        new TimeOfDay(time);
        
        // Assert: Exception is thrown
    }
    
    @Test
    public void testCompareTo_SameTime_ReturnsZero() {
        // Arrange
        TimeOfDay time1 = new TimeOfDay(8, 15);
        TimeOfDay time2 = new TimeOfDay(8, 15);
        
        // Act
        int result = time1.compareTo(time2);
        
        // Assert
        Assert.assertEquals(0, result);
    }
    
    @Test
    public void testCompareTo_EarlierTime_ReturnsNegativeValue() {
        // Arrange
        TimeOfDay time1 = new TimeOfDay(6, 30);
        TimeOfDay time2 = new TimeOfDay(8, 15);
        
        // Act
        int result = time1.compareTo(time2);
        
        // Assert
        Assert.assertTrue(result < 0);
    }
    
    @Test
    public void testCompareTo_LaterTime_ReturnsPositiveValue() {
        // Arrange
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(8, 45);
        
        // Act
        int result = time1.compareTo(time2);
        
        // Assert
        Assert.assertTrue(result > 0);
    }
    
    @Test
    public void testToString_ReturnsFormattedTime() {
        // Arrange
        int hours = 9;
        int minutes = 20;
        TimeOfDay time = new TimeOfDay(hours, minutes);
        
        // Act
        String result = time.toString();
        
        // Assert
        Assert.assertEquals(hours + ":" + minutes, result);
    }
    
    @Test
    public void testHashCode_SameTime_ReturnsSameHashCode() {
        // Arrange
        TimeOfDay time1 = new TimeOfDay(5, 45);
        TimeOfDay time2 = new TimeOfDay(5, 45);
        
        // Act
        int hashCode1 = time1.hashCode();
        int hashCode2 = time2.hashCode();
        
        // Assert
        Assert.assertEquals(hashCode1, hashCode2);
    }
    
    @Test
    public void testHashCode_DifferentTime_ReturnsDifferentHashCode() {
        // Arrange
        TimeOfDay time1 = new TimeOfDay(7, 30);
        TimeOfDay time2 = new TimeOfDay(9, 10);
        
        // Act
        int hashCode1 = time1.hashCode();
        int hashCode2 = time2.hashCode();
        
        // Assert
        Assert.assertNotEquals(hashCode1, hashCode2);
    }
    
    @Test
    public void testEquals_SameTime_ReturnsTrue() {
        // Arrange
        TimeOfDay time1 = new TimeOfDay(3, 25);
        TimeOfDay time2 = new TimeOfDay(3, 25);
        
        // Act
        boolean result = time1.equals(time2);
        
        // Assert
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEquals_DifferentTime_ReturnsFalse() {
        // Arrange
        TimeOfDay time1 = new TimeOfDay(4, 50);
        TimeOfDay time2 = new TimeOfDay(6, 30);
        
        // Act
        boolean result = time1.equals(time2);
        
        // Assert
        Assert.assertFalse(result);
    }
}