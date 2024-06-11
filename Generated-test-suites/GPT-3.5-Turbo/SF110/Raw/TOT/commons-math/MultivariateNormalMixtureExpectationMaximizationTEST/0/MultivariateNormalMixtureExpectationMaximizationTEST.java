import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.stat.correlation.*;
import java.util.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    private MultivariateNormalMixtureExpectationMaximization M;

    @Before
    public void setUp() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        try {
            M = new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConstructor() {
        assertNotNull(M);
    }

    @Test
    public void testFit() {
        MixtureMultivariateNormalDistribution initialMixture = createMockMixture();
        int maxIterations = 1000;
        double threshold = 1.0E-5;

        try {
            M.fit(initialMixture, maxIterations, threshold);
            assertNotNull(M.getFittedModel());
            assertEquals(0.0, M.getLogLikelihood(), 1e-9);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFitWithDefaultParameters() {
        MixtureMultivariateNormalDistribution initialMixture = createMockMixture();

        try {
            M.fit(initialMixture);
            assertNotNull(M.getFittedModel());
            assertEquals(0.0, M.getLogLikelihood(), 1e-9);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInvalidMaxIterations() throws Exception {
        MixtureMultivariateNormalDistribution initialMixture = createMockMixture();
        int maxIterations = -1;
        double threshold = 1.0E-5;

        M.fit(initialMixture, maxIterations, threshold);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInvalidThreshold() throws Exception {
        MixtureMultivariateNormalDistribution initialMixture = createMockMixture();
        int maxIterations = 1000;
        double threshold = -1.0;

        M.fit(initialMixture, maxIterations, threshold);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testFitWithIncompatibleData() throws Exception {
        double[][] data = {{1, 2}, {4, 5, 6}, {7, 8, 9}};
        MultivariateNormalMixtureExpectationMaximization M = new MultivariateNormalMixtureExpectationMaximization(data);

        MixtureMultivariateNormalDistribution initialMixture = createMockMixture();
        int maxIterations = 1000;
        double threshold = 1.0E-5;

        M.fit(initialMixture, maxIterations, threshold);
    }

    @Test(expected = ConvergenceException.class)
    public void testFitWithNonConvergingIterations() throws Exception {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        MultivariateNormalMixtureExpectationMaximization M = new MultivariateNormalMixtureExpectationMaximization(data);

        MixtureMultivariateNormalDistribution initialMixture = createMockMixture();
        int maxIterations = 5;
        double threshold = 1.0E-9;

        M.fit(initialMixture, maxIterations, threshold);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimateWithInvalidNumComponents() throws Exception {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 1);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testEstimateWithIncompatibleData() throws Exception {
        double[][] data = {{1, 2}, {4, 5, 6}, {7, 8, 9}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidData() throws Exception {
        double[][] data = {{}};
        MultivariateNormalMixtureExpectationMaximization M = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    private MixtureMultivariateNormalDistribution createMockMixture() {
        List<Pair<Double, MultivariateNormalDistribution>> components = new ArrayList<>();
        double[] weight = {0.5, 0.5};
        double[][] means = {{1, 2, 3}, {4, 5, 6}};
        double[][][] covMats = {new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}},
                                new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}}};

        for (int i = 0; i < weight.length; i++) {
            MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(means[i], covMats[i]);
            Pair<Double, MultivariateNormalDistribution> component = new Pair<>(weight[i], mvn);
            components.add(component);
        }

        return new MixtureMultivariateNormalDistribution(components);
    }
}