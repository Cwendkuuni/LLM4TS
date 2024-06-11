import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.exception.*;
import org.junit.*;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {
    
    private static final double[][] DATA = {
        {1, 2, 3},
        {4, 5, 6},
        {7, 8, 9}
    };

    @Test
    public void testConstructor_validData() {
        try {
            MultivariateNormalMixtureExpectationMaximization estimator = new MultivariateNormalMixtureExpectationMaximization(DATA);
            assertNotNull(estimator);
        } catch (Exception e) {
            fail("Exception thrown for valid data");
        }
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_invalidDataSize() throws Exception {
        double[][] invalidData = {{}};
        new MultivariateNormalMixtureExpectationMaximization(invalidData);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testConstructor_invalidDataSizeInconsistentColumns() throws Exception {
        double[][] invalidData = {{1, 2}, {3, 4, 5}};
        new MultivariateNormalMixtureExpectationMaximization(invalidData);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_invalidDataSizeTooSmall() throws Exception {
        double[][] invalidData = {{1}};
        new MultivariateNormalMixtureExpectationMaximization(invalidData);
    }
    
    @Test
    public void testFit_validArguments() {
        try {
            MultivariateNormalMixtureExpectationMaximization estimator = new MultivariateNormalMixtureExpectationMaximization(DATA);
            MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(DATA, 2);
            
            estimator.fit(initialMixture, 100, 1e-6);
            
            assertEquals(2, estimator.getFittedModel().getComponents().size());
        } catch (Exception e) {
            fail("Exception thrown for valid arguments");
        }
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFit_invalidMaxIterations() throws Exception {
        MultivariateNormalMixtureExpectationMaximization estimator = new MultivariateNormalMixtureExpectationMaximization(DATA);
        MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(DATA, 2);
        
        estimator.fit(initialMixture, 0, 1e-6);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFit_invalidThreshold() throws Exception {
        MultivariateNormalMixtureExpectationMaximization estimator = new MultivariateNormalMixtureExpectationMaximization(DATA);
        MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(DATA, 2);
        
        estimator.fit(initialMixture, 100, 0);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testFit_invalidInitialMixtureDimensions() throws Exception {
        MultivariateNormalMixtureExpectationMaximization estimator = new MultivariateNormalMixtureExpectationMaximization(DATA);
        double[][] invalidInitialMeans = {{1, 2}};
        MixtureMultivariateNormalDistribution invalidInitialMixture = new MixtureMultivariateNormalDistribution(invalidInitialMeans);
        
        estimator.fit(invalidInitialMixture, 100, 1e-6);
    }
    
    @Test
    public void testFit_invalidIterationLimit() {
        try {
            MultivariateNormalMixtureExpectationMaximization estimator = new MultivariateNormalMixtureExpectationMaximization(DATA);
            MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(DATA, 2);
            
            estimator.fit(initialMixture, 1, 1e-6);
            
            assertEquals(1, estimator.getLogLikelihood(), 0.0);
        } catch (Exception e) {
            fail("Exception thrown for valid arguments");
        }
    }
    
    @Test(expected = ConvergenceException.class)
    public void testFit_noConvergence() throws Exception {
        MultivariateNormalMixtureExpectationMaximization estimator = new MultivariateNormalMixtureExpectationMaximization(DATA);
        MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(DATA, 3);
        
        estimator.fit(initialMixture, 1000, 1e-5);
    }
    
    @Test
    public void testFit_alternateConstructor() {
        try {
            MultivariateNormalMixtureExpectationMaximization estimator = new MultivariateNormalMixtureExpectationMaximization(DATA);
            MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(DATA, 2);
            
            estimator.fit(initialMixture);
        } catch (Exception e) {
            fail("Exception thrown for valid arguments");
        }
    }
    
    @Test(expected = SingularMatrixException.class)
    public void testFit_singularMatrixException() throws Exception {
        double[][] singularData = {{1, 1}, {1, 1}};
        MultivariateNormalMixtureExpectationMaximization estimator = new MultivariateNormalMixtureExpectationMaximization(singularData);
        MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(singularData, 1);
        
        estimator.fit(initialMixture);
    }
    
    @Test
    public void testGetLogLikelihood() {
        try {
            MultivariateNormalMixtureExpectationMaximization estimator = new MultivariateNormalMixtureExpectationMaximization(DATA);
            MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(DATA, 2);
            
            estimator.fit(initialMixture);
            
            double logLikelihood = estimator.getLogLikelihood();
            assertTrue(logLikelihood < 0.0);
        } catch (Exception e) {
            fail("Exception thrown for valid arguments");
        }
    }
    
    @Test
    public void testGetFittedModel() {
        try {
            MultivariateNormalMixtureExpectationMaximization estimator = new MultivariateNormalMixtureExpectationMaximization(DATA);
            MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(DATA, 2);
            
            estimator.fit(initialMixture);
            
            MixtureMultivariateNormalDistribution fittedModel = estimator.getFittedModel();
            assertNotNull(fittedModel);
            assertEquals(2, fittedModel.getComponents().size());
        } catch (Exception e) {
            fail("Exception thrown for valid arguments");
        }
    }
}