import org.junit.Test;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.stat.correlation.*;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidDataLength() {
        double[][] data = {};
        MultivariateNormalMixtureExpectationMaximization mnmm = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithInvalidDataDimensions() {
        double[][] data = {{1, 2, 3}, {4, 5}};
        MultivariateNormalMixtureExpectationMaximization mnmm = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidDataSize() {
        double[][] data = {{1, 2}};
        MultivariateNormalMixtureExpectationMaximization mnmm = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInvalidMaxIterations() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        MultivariateNormalMixtureExpectationMaximization mnmm = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        int maxIterations = 0;
        double threshold = 1.0E-5;
        mnmm.fit(initialMixture, maxIterations, threshold);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInvalidThreshold() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        MultivariateNormalMixtureExpectationMaximization mnmm = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        int maxIterations = 1000;
        double threshold = 0;
        mnmm.fit(initialMixture, maxIterations, threshold);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testFitWithInvalidMeanColumns() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        MultivariateNormalMixtureExpectationMaximization mnmm = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        int maxIterations = 1000;
        double threshold = 1.0E-5;
        mnmm.fit(initialMixture, maxIterations, threshold);
    }

    @Test(expected = ConvergenceException.class)
    public void testFitWithNonConvergingLogLikelihood() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        MultivariateNormalMixtureExpectationMaximization mnmm = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        initialMixture.addComponent(0.5, new MultivariateNormalDistribution(new double[]{1, 2, 3}, new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}}));
        int maxIterations = 10;
        double threshold = 1.0E-5;
        mnmm.fit(initialMixture, maxIterations, threshold);
    }

    @Test
    public void testFitWithValidData() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        MultivariateNormalMixtureExpectationMaximization mnmm = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        initialMixture.addComponent(0.5, new MultivariateNormalDistribution(new double[]{1, 2, 3}, new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}}));
        int maxIterations = 1000;
        double threshold = 1.0E-5;
        mnmm.fit(initialMixture, maxIterations, threshold);

        MixtureMultivariateNormalDistribution fittedModel = mnmm.getFittedModel();
        assertNotNull(fittedModel);

        double logLikelihood = mnmm.getLogLikelihood();
        assertTrue(logLikelihood > -Double.MAX_VALUE);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimateWithInvalidDataLength() {
        double[][] data = {};
        MixtureMultivariateNormalDistribution estimate = MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testEstimateWithInvalidNumComponents() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        MixtureMultivariateNormalDistribution estimate = MultivariateNormalMixtureExpectationMaximization.estimate(data, 1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testEstimateWithInvalidNumComponents2() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        MixtureMultivariateNormalDistribution estimate = MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
    }

    @Test
    public void testEstimateWithValidData() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        MixtureMultivariateNormalDistribution estimate = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
        assertNotNull(estimate);
    }
}