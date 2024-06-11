import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.distribution.fitting.MultivariateNormalMixtureExpectationMaximization;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructor_ValidData() {
        double[][] validData = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(validData);
        assertNotNull(em);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_EmptyDataArray() {
        double[][] emptyData = {};
        new MultivariateNormalMixtureExpectationMaximization(emptyData);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructor_DataWithDifferentDimensions() {
        double[][] data = {{1.0, 2.0}, {3.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    // Add more test cases for other methods here

}