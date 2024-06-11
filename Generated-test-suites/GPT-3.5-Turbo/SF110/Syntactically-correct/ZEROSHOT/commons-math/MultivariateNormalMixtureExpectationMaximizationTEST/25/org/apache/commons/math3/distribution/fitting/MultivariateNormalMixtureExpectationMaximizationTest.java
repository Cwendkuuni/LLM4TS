import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.*;
import java.util.*;
import org.apache.commons.math3.stat.correlation.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_NotStrictlyPositiveException() {
        double[][] data = {};
        MultivariateNormalMixtureExpectationMaximization mvn = new MultivariateNormalMixtureExpectationMaximization(data);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testConstructor_DimensionMismatchException() {
        double[][] data = {{1, 2, 3}, {4, 5}};
        MultivariateNormalMixtureExpectationMaximization mvn = new MultivariateNormalMixtureExpectationMaximization(data);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_NumberIsTooSmallException() {
        double[][] data = {{1}, {2}};
        MultivariateNormalMixtureExpectationMaximization mvn = new MultivariateNormalMixtureExpectationMaximization(data);
    }
    
    @Test
    public void testConstructor_ValidData() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        MultivariateNormalMixtureExpectationMaximization mvn = new MultivariateNormalMixtureExpectationMaximization(data);
        assertNotNull(mvn);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFit_MaxIterations_NotStrictlyPositiveException() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        MultivariateNormalMixtureExpectationMaximization mvn = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        mvn.fit(initialMixture, 0, 1.0E-5);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFit_Threshold_NotStrictlyPositiveException() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        MultivariateNormalMixtureExpectationMaximization mvn = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        mvn.fit(initialMixture, 1000, -1.0E-5);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testFit_DimensionMismatchException() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        MultivariateNormalMixtureExpectationMaximization mvn = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        mvn.fit(initialMixture, 1000, 1.0E-5);
    }
    
    @Test(expected = SingularMatrixException.class)
    public void testFit_SingularMatrixException() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        MultivariateNormalMixtureExpectationMaximization mvn = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        mvn.fit(initialMixture, 1000, 1.0E-5);
    }
    
    @Test
    public void testFit_ValidInput() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        MultivariateNormalMixtureExpectationMaximization mvn = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        mvn.fit(initialMixture, 1000, 1.0E-5);
        assertNotNull(mvn.getFittedModel());
    }
    
    @Test
    public void testFit_DefaultInput() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        MultivariateNormalMixtureExpectationMaximization mvn = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        mvn.fit(initialMixture);
        assertNotNull(mvn.getFittedModel());
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimate_NotStrictlyPositiveException() {
        double[][] data = {{1}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 1);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testEstimate_NumberIsTooSmallException() {
        double[][] data = {{1}, {2}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 1);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testEstimate_NumberIsTooLargeException() {
        double[][] data = {{1, 2}, {3, 4}, {5, 6}, {7, 8}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 5);
    }
    
    @Test
    public void testEstimate_ValidInput() {
        double[][] data = {{1, 2}, {3, 4}, {5, 6}, {7, 8}};
        int numComponents = 2;
        MixtureMultivariateNormalDistribution mixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);
        assertNotNull(mixture);
    }
    
    @Test
    public void testGetters() {
        double[][] data = {{1, 2}, {3, 4}, {5, 6}, {7, 8}};
        MultivariateNormalMixtureExpectationMaximization mvn = new MultivariateNormalMixtureExpectationMaximization(data);
        assertNotNull(mvn.getLogLikelihood());
        assertNotNull(mvn.getFittedModel());
    }
}