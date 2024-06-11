import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;

public class AxisTest {
    
    @Test
    public void lookup_WithValidAxisNum_ReturnsValidAxisName() {
        // Arrange
        int validAxisNum = 1;
        
        // Act
        String axisName = Axis.lookup(validAxisNum);
        
        // Assert
        Assert.assertThat(axisName, is(equalTo("child")));
    }
    
    @Test
    public void lookup_WithInvalidAxisNum_ReturnsNull() {
        // Arrange
        int invalidAxisNum = -1;
        
        // Act
        String axisName = Axis.lookup(invalidAxisNum);
        
        // Assert
        Assert.assertNull(axisName);
    }
    
    @Test
    public void lookup_WithValidAxisName_ReturnsValidAxisNum() {
        // Arrange
        String validAxisName = "parent";
        
        // Act
        int axisNum = Axis.lookup(validAxisName);
        
        // Assert
        Assert.assertThat(axisNum, is(equalTo(3)));
    }
    
    @Test
    public void lookup_WithInvalidAxisName_ReturnsZero() {
        // Arrange
        String invalidAxisName = "invalid";
        
        // Act
        int axisNum = Axis.lookup(invalidAxisName);
        
        // Assert
        Assert.assertThat(axisNum, is(equalTo(0)));
    }
    
    // Add additional test cases for other methods as required
    
}