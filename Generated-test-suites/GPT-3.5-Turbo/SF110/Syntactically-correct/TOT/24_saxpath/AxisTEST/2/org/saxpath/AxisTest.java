import org.junit.Assert;
import org.junit.Test;

public class AxisTest {

    @Test
    public void testLookup_WithValidAxisNum_ShouldReturnCorrectAxisName() {
        //Arrange
        int axisNum = 1;
        String expectedAxisName = "child";
        
        //Act
        String actualAxisName = Axis.lookup(axisNum);
        
        //Assert
        Assert.assertEquals(expectedAxisName, actualAxisName);
    }
    
    @Test
    public void testLookup_WithInvalidAxisNum_ShouldReturnNull() {
        //Arrange
        int axisNum = -1;
        
        //Act
        String actualAxisName = Axis.lookup(axisNum);
        
        //Assert
        Assert.assertNull(actualAxisName);
    }
    
    @Test
    public void testLookup_WithValidAxisName_ShouldReturnCorrectAxisNum() {
        //Arrange
        String axisName = "following";
        int expectedAxisNum = 7;
        
        //Act
        int actualAxisNum = Axis.lookup(axisName);
        
        //Assert
        Assert.assertEquals(expectedAxisNum, actualAxisNum);
    }
    
    @Test
    public void testLookup_WithInvalidAxisName_ShouldReturnZero() {
        //Arrange
        String axisName = "invalid";
        
        //Act
        int actualAxisNum = Axis.lookup(axisName);
        
        //Assert
        Assert.assertEquals(0, actualAxisNum);
    }
}