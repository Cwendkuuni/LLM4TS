import static org.junit.Assert.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.*;
import org.junit.*;
import java.util.*;
import org.apache.commons.math3.stat.correlation.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_ThrowsNotStrictlyPositiveException() {
        double[][] data = {};
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testConstructor_ThrowsDimensionMismatchException() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_ThrowsNumberIsTooSmallException() {
        double[][] data = {{1}};
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
    }
    
    @Test
    public void testFit_NoExceptions() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        m.fit(initialMixture, 1000, 1.0E-5);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFit_ThrowsNotStrictlyPositiveException() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        m.fit(initialMixture, 0, 1.0E-5);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFit_ThrowsNotStrictlyPositiveException2() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        m.fit(initialMixture, 1000, 0);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testFit_ThrowsDimensionMismatchException() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        m.fit(initialMixture, 1000, 1.0E-5);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testFit_ThrowsConvergenceException() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        m.fit(initialMixture, 1000, 1);
    }
    
    @Test
    public void testFit_NoExceptions2() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        m.fit(initialMixture);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimate_ThrowsNotStrictlyPositiveException() {
        double[][] data = {{1}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 1);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testEstimate_ThrowsNumberIsTooSmallException() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 1);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testEstimate_ThrowsNumberIsTooLargeException() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
    }
    
    @Test
    public void testEstimate_NoExceptions() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
    }
    
}