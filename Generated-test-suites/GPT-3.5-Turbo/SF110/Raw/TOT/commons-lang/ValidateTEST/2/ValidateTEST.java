import static org.junit.Assert.*;
import org.junit.Test;

public class ValidateTest {

    @Test
    public void testIsTrue_booleanStringLong_typicalCase() {
        try {
            // Arrange
            long value = 10;
            
            // Act
            Validate.isTrue(value >= 0, "The value must be greater than or equal to 0", value);
            
            // Assert
            // No exception should be thrown
        } catch (Exception e) {
            fail("An exception should not be thrown");
        }
    }
    
    @Test
    public void testIsTrue_booleanStringLong_edgeCase() {
        try {
            // Arrange
            long value = 0;
            
            // Act
            Validate.isTrue(value >= 0, "The value must be greater than or equal to 0", value);
            
            // Assert
            // No exception should be thrown
        } catch (Exception e) {
            fail("An exception should not be thrown");
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testIsTrue_booleanStringLong_errorCase() {
        // Arrange
        long value = -1;
        
        // Act
        Validate.isTrue(value >= 0, "The value must be greater than or equal to 0", value);
        
        // Assert
        // IllegalArgumentException should be thrown
    }

    // Repeat the above three test cases for the other overloaded methods of isTrue()
    
    // Add similar test cases for the other methods of Validate class
    
}