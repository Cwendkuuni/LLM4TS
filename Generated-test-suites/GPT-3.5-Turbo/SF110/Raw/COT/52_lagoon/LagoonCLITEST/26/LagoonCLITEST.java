
package nu.staldal.lagoon;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;

public class LagoonCLITest {

    @Test
    public void testMain() {
        // Arrange
        String[] args = {"property_file"};
        
        // Act
        LagoonCLI.main(args);
        
        // Assert
        // Test the functionality of the main method
        
    }

    @Test
    public void testGetProperty() {
        // Arrange
        String name = "targetURL";

        // Act
        try {
            String result = LagoonCLI.getProperty(name);

            // Assert
            // Test the functionality of the getProperty method
        } catch (LagoonException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test(expected = LagoonException.class)
    public void testGetProperty_ThrowsLagoonException() throws LagoonException {
        // Arrange
        String name = "nonExistingProperty";

        // Act
        String result = LagoonCLI.getProperty(name);

        // Assert
        // Test that a LagoonException is thrown when getting a non-existing property
    }

    @Test
    public void testShowTime_LessThan10000() {
        // Arrange
        long ms = 5000;
        
        // Act
        LagoonCLI.showTime(ms);

        // Assert
        // Test the showTime method when the time is less than 10000 ms
    }

    @Test
    public void testShowTime_GreaterThanOrEqual10000() {
        // Arrange
        long ms = 15000;
        
        // Act
        LagoonCLI.showTime(ms);

        // Assert
        // Test the showTime method when the time is greater than or equal to 10000 ms
    }
}
