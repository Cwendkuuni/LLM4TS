import org.junit.*;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {
    
    // Test constructor with valid data
    @Test
    public void testConstructor_ValidData() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data);
            assertNotNull(mnm);
        } catch (Exception e) {
            fail("Exception thrown when not expected");
        }
    }
    
    // Test constructor with non-strictly positive data length
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_NonStrictlyPositiveDataLength() throws Exception {
        double[][] data = {};
        MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data);
    }
    
    // Test constructor with dimension mismatch in data
    @Test(expected = DimensionMismatchException.class)
    public void testConstructor_DimensionMismatchInData() throws Exception {
        double[][] data = {{1.0, 2.0}, {3.0}};
        MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data);
    }
    
    // Test constructor with number too small in data
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_NumberTooSmallInData() throws Exception {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0}};
        MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data);
    }
    
    // Test fit method with valid input
    @Test
    public void testFit_ValidInput() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data);
            MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
            mnm.fit(initialMixture);
            assertNotNull(mnm.getFittedModel());
        } catch (Exception e) {
            fail("Exception thrown when not expected");
        }
    }
    
    // Test fit method with non-strictly positive max iterations
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFit_NonStrictlyPositiveMaxIterations() throws Exception {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        mnm.fit(initialMixture, 0, 1.0E-5);
    }
    
    // Test fit method with non-strictly positive threshold
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFit_NonStrictlyPositiveThreshold() throws Exception {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        mnm.fit(initialMixture, 1000, 0.0);
    }
    
    // Test fit method with dimension mismatch in initial mixture mean columns
    @Test(expected = DimensionMismatchException.class)
    public void testFit_DimensionMismatchInInitialMixtureMeanColumns() throws Exception {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        initialMixture.addComponent(0.5, new MultivariateNormalDistribution(new double[] {1.0, 2.0, 3.0}, new double[][] {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}}));
        mnm.fit(initialMixture);
    }
    
    // Test getLogLikelihood method
    @Test
    public void testGetLogLikelihood() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data);
            MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
            mnm.fit(initialMixture);
            double logLikelihood = mnm.getLogLikelihood();
            assertEquals(-Infinity, logLikelihood, 0.0);
        } catch (Exception e) {
            fail("Exception thrown when not expected");
        }
    }
    
    // Test getFittedModel method
    @Test
    public void testGetFittedModel() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data);
            MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
            mnm.fit(initialMixture);
            MixtureMultivariateNormalDistribution fittedModel = mnm.getFittedModel();
            assertNotNull(fittedModel);
        } catch (Exception e) {
            fail("Exception thrown when not expected");
        }
    }
    
    // Test estimate method with valid input
    @Test
    public void testEstimate_ValidInput() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        try {
            MixtureMultivariateNormalDistribution estimatedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
            assertNotNull(estimatedModel);
        } catch (Exception e) {
            fail("Exception thrown when not expected");
        }
    }
    
    // Test estimate method with non-strictly positive data length
    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimate_NonStrictlyPositiveDataLength() throws Exception {
        double[][] data = {};
        MixtureMultivariateNormalDistribution estimatedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
    }
    
    // Test estimate method with number too small in num components
    @Test(expected = NumberIsTooSmallException.class)
    public void testEstimate_NumberTooSmallNumComponents() throws Exception {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MixtureMultivariateNormalDistribution estimatedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 1);
    }
    
    // Test estimate method with number too large in num components
    @Test(expected = NumberIsTooLargeException.class)
    public void testEstimate_NumberTooLargeNumComponents() throws Exception {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MixtureMultivariateNormalDistribution estimatedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 4);
    }

}