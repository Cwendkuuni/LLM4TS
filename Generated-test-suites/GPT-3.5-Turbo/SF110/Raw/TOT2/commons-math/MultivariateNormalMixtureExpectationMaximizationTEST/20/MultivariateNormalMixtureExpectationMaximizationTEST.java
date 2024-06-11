import static org.junit.Assert.*;
import org.junit.Test;

public class MultivariateNormalMixtureExpectationMaximizationTest {
    
    @Test
    public void testConstructor_ValidData_CreatesInstance() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        assertNotNull(em);
    }
    
    @Test
    public void testConstructor_EmptyData_ThrowsException() {
        double[][] data = {};
        assertThrows(NotStrictlyPositiveException.class, () -> new MultivariateNormalMixtureExpectationMaximization(data));
    }
    
    @Test
    public void testFit_MaxIterationsLessThanOne_ThrowsException() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        assertThrows(NotStrictlyPositiveException.class, () -> em.fit(initialMixture, 0, 1.0E-5));
    }
    
    @Test
    public void testFit_ThresholdBelowMinValue_ThrowsException() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        assertThrows(NotStrictlyPositiveException.class, () -> em.fit(initialMixture, 1000, Double.MIN_VALUE-0.1));
    }
    
    @Test
    public void testEstimate_NumComponentsLessThanTwo_ThrowsException() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        assertThrows(NumberIsTooSmallException.class, () -> MultivariateNormalMixtureExpectationMaximization.estimate(data, 1));
    }
    
    @Test
    public void testEstimate_NumComponentsGreaterThanDataLength_ThrowsException() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        assertThrows(NumberIsTooLargeException.class, () -> MultivariateNormalMixtureExpectationMaximization.estimate(data, 3));
    }

}