import static org.junit.Assert.*;
import org.junit.Test;

public class MultivariateNormalMixtureExpectationMaximizationTest {
    
    // Test constructor with valid data
    @Test
    public void testConstructorValidData() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (Exception e) {
            fail("Constructor should not throw an exception with valid data");
        }
    }
    
    // Test constructor with empty data
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorEmptyData() {
        double[][] data = {};
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
    }
    
    // Test fitting with maxIterations < 1
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitMaxIterations() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initial = new MixtureMultivariateNormalDistribution();
        m.fit(initial, 0, 1.0E-5);
    }
    
    // Test estimate with data length < 2
    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimateDataLength() {
        double[][] data = {{1.0, 2.0}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
    }
    
    // Additional test cases for edge cases and error scenarios can be added here
    
}