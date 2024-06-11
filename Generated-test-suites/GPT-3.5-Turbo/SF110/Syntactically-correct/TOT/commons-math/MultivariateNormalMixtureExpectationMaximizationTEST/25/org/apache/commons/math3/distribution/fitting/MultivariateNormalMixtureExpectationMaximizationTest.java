import org.junit.*;
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    // Test case for constructor
    @Test
    public void testConstructor() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization model = null;
        try {
            model = new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (NotStrictlyPositiveException | DimensionMismatchException | NumberIsTooSmallException e) {
            fail("Constructor threw an exception: " + e.getMessage());
        }
        
        assertNotNull("Constructor should create a valid object", model);
    }
    
    // Test case for constructor with negative number of data examples
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_NegativeNumData() throws NotStrictlyPositiveException, DimensionMismatchException, NumberIsTooSmallException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
    }
    
    // Test case for constructor with mismatching dimensions of data examples
    @Test(expected = DimensionMismatchException.class)
    public void testConstructor_DimensionMismatch() throws NotStrictlyPositiveException, DimensionMismatchException, NumberIsTooSmallException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0}};
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
    }
    
    // Test case for constructor with too few dimensions of data example
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_TooFewDimensions() throws NotStrictlyPositiveException, DimensionMismatchException, NumberIsTooSmallException {
        double[][] data = {{1.0, 2.0}, {4.0, 5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
    }
    
    // Test case for fit method with valid input
    @Test
    public void testFit() {
        // Create initial mixture
        double[][] means = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(means);
        
        // Create data
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        
        // Create model and fit
        MultivariateNormalMixtureExpectationMaximization model = null;
        try {
            model = new MultivariateNormalMixtureExpectationMaximization(data);
            model.fit(initialMixture);
        } catch (NotStrictlyPositiveException | DimensionMismatchException | SingularMatrixException e) {
            fail("fit method threw an exception: " + e.getMessage());
        }
        
        assertNotNull("Fitted model should not be null", model.getFittedModel());
        assertTrue("Log likelihood should be a valid value", !Double.isNaN(model.getLogLikelihood()));
    }
    
    // Test case for fit method with invalid input
    @Test(expected = SingularMatrixException.class)
    public void testFit_SingularMatrix() throws NotStrictlyPositiveException, DimensionMismatchException, SingularMatrixException {
        // Create initial mixture
        double[][] means = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(means);
        
        // Create data
        double[][] data = {{1.0, 2.0, 3.0}, {1.0, 2.0, 3.0}};
        
        // Create model and fit
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
        model.fit(initialMixture);
    }
    
    // Test case for getFittedModel method
    @Test
    public void testGetFittedModel() {
        // Create initial mixture
        double[][] means = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(means);
        
        // Create data
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        
        // Create model and fit
        MultivariateNormalMixtureExpectationMaximization model = null;
        try {
            model = new MultivariateNormalMixtureExpectationMaximization(data);
            model.fit(initialMixture);
        } catch (NotStrictlyPositiveException | DimensionMismatchException | SingularMatrixException e) {
            fail("fit method threw an exception: " + e.getMessage());
        }
        
        MixtureMultivariateNormalDistribution fittedModel = model.getFittedModel();
        
        assertNotNull("Fitted model should not be null", fittedModel);
        assertEquals("Fitted model should have the same number of components as the initial mixture",
                initialMixture.getComponents().size(), fittedModel.getComponents().size());
    }
    
    // Test case for getLogLikelihood method
    @Test
    public void testGetLogLikelihood() {
        // Create initial mixture
        double[][] means = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(means);
        
        // Create data
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        
        // Create model and fit
        MultivariateNormalMixtureExpectationMaximization model = null;
        try {
            model = new MultivariateNormalMixtureExpectationMaximization(data);
            model.fit(initialMixture);
        } catch (NotStrictlyPositiveException | DimensionMismatchException | SingularMatrixException e) {
            fail("fit method threw an exception: " + e.getMessage());
        }
        
        double logLikelihood = model.getLogLikelihood();
        
        assertTrue("Log likelihood should be a valid value", !Double.isNaN(logLikelihood));
    }
    
    // Test case for estimate method
    @Test
    public void testEstimate() {
        // Create data
        double[][] data = {{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}};
        
        MixtureMultivariateNormalDistribution estimatedModel = null;
        try {
            estimatedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
        } catch (NotStrictlyPositiveException | DimensionMismatchException e) {
            fail("estimate method threw an exception: " + e.getMessage());
        }
        
        assertNotNull("Estimated model should not be null", estimatedModel);
    }
    
}