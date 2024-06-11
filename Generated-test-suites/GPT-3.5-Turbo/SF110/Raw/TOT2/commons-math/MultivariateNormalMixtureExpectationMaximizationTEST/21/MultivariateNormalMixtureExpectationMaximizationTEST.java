import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.SingularMatrixException;

public class MultivariateNormalMixtureExpectationMaximizationTest {
    
    @Test
    public void testConstructor_validData() {
        double[][] validData = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(validData);
        assertNotNull(em);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_invalidData() {
        double[][] invalidData = {};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(invalidData);
    }
    
    // Add more test cases for the constructors if needed
    
    @Test
    public void testFit_validParams() {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        
        try {
            em.fit(initialMixture, 1000, 1.0E-5);
        } catch (SingularMatrixException | NotStrictlyPositiveException | DimensionMismatchException e) {
            fail("Exception thrown for valid input");
        }
    }
    
    // Add more fit test cases for edge cases and exceptions if needed
    
    @Test
    public void testGetLogLikelihood() {
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        
        assertEquals(0.0, em.getLogLikelihood(), 0.0001);
    }
    
    // Add more test cases for other public methods if needed
    
}