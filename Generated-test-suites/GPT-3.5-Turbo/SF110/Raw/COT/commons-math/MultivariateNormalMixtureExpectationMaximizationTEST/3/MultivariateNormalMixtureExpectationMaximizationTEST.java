import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.distribution.fitting.*;
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.*;
import java.util.*;
import org.apache.commons.math3.stat.correlation.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructor() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        
        try {
            MultivariateNormalMixtureExpectationMaximization obj = new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidData() {
        double[][] data = {};
        
        MultivariateNormalMixtureExpectationMaximization obj = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithMismatchedDimensions() {
        double[][] data = {{1.0, 2.0}, {3.0}};
        
        MultivariateNormalMixtureExpectationMaximization obj = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInsufficientColumns() {
        double[][] data = {{1.0}, {3.0, 4.0}};
        
        MultivariateNormalMixtureExpectationMaximization obj = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testFit() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution mixture = new MixtureMultivariateNormalDistribution();
        
        try {
            MultivariateNormalMixtureExpectationMaximization obj = new MultivariateNormalMixtureExpectationMaximization(data);
            obj.fit(mixture, 1000, 0.00001);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInvalidMaxIterations() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution mixture = new MixtureMultivariateNormalDistribution();
        
        MultivariateNormalMixtureExpectationMaximization obj = new MultivariateNormalMixtureExpectationMaximization(data);
        obj.fit(mixture, 0, 0.00001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInvalidThreshold() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution mixture = new MixtureMultivariateNormalDistribution();
        
        MultivariateNormalMixtureExpectationMaximization obj = new MultivariateNormalMixtureExpectationMaximization(data);
        obj.fit(mixture, 1000, -0.00001);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testFitWithMismatchedDimensions() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution mixture = new MixtureMultivariateNormalDistribution(2);
        
        MultivariateNormalMixtureExpectationMaximization obj = new MultivariateNormalMixtureExpectationMaximization(data);
        obj.fit(mixture, 1000, 0.00001);
    }

    @Test
    public void testFitWithInsufficientColumns() {
        double[][] data = {{1.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution mixture = new MixtureMultivariateNormalDistribution(2);
        
        try {
            MultivariateNormalMixtureExpectationMaximization obj = new MultivariateNormalMixtureExpectationMaximization(data);
            obj.fit(mixture, 1000, 0.00001);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFitNoMaxIterationsAndThreshold() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution mixture = new MixtureMultivariateNormalDistribution();
        
        try {
            MultivariateNormalMixtureExpectationMaximization obj = new MultivariateNormalMixtureExpectationMaximization(data);
            obj.fit(mixture);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testEstimate() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        
        try {
            MixtureMultivariateNormalDistribution result = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
            assertNotNull(result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimateWithInvalidData() {
        double[][] data = {};
        
        MixtureMultivariateNormalDistribution result = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testEstimateWithInsufficientComponents() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        
        MixtureMultivariateNormalDistribution result = MultivariateNormalMixtureExpectationMaximization.estimate(data, 1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testEstimateWithExcessiveComponents() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        
        MixtureMultivariateNormalDistribution result = MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
    }

    @Test
    public void testGetLogLikelihood() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution mixture = new MixtureMultivariateNormalDistribution();
        MultivariateNormalMixtureExpectationMaximization obj = new MultivariateNormalMixtureExpectationMaximization(data);
        
        try {
            obj.fit(mixture, 1000, 0.00001);
            double logLikelihood = obj.getLogLikelihood();
            assertTrue(logLikelihood >= 0.0);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetFittedModel() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution mixture = new MixtureMultivariateNormalDistribution();
        MultivariateNormalMixtureExpectationMaximization obj = new MultivariateNormalMixtureExpectationMaximization(data);
        
        try {
            obj.fit(mixture, 1000, 0.00001);
            MixtureMultivariateNormalDistribution fittedModel = obj.getFittedModel();
            assertNotNull(fittedModel);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}