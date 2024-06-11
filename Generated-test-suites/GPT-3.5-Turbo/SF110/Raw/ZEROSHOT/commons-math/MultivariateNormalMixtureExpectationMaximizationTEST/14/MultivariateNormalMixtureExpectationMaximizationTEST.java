import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    private double[][] data;

    @Before
    public void setUp() {
        data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidDataLength() {
        double[][] invalidData = new double[0][2];
        new MultivariateNormalMixtureExpectationMaximization(invalidData);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorInvalidDataDimensions() {
        double[][] invalidData = new double[][]{{1.0, 2.0}, {3.0}};
        new MultivariateNormalMixtureExpectationMaximization(invalidData);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorInvalidDataLengthTooSmall() {
        double[][] invalidData = new double[][]{{1.0, 2.0}, {3.0, 4.0}};
        new MultivariateNormalMixtureExpectationMaximization(invalidData);
    }

    @Test
    public void testConstructorValidData() {
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        assertNotNull(m);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitInvalidMaxIterations() {
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = createInitialMixture();
        m.fit(initialMixture, 0, 1.0E-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitInvalidThreshold() {
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = createInitialMixture();
        m.fit(initialMixture, 1000, 0.0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testFitInvalidNumCols() {
        double[][] invalidData = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(invalidData);
        MixtureMultivariateNormalDistribution initialMixture = createInitialMixture();
        m.fit(initialMixture);
    }

    @Test(expected = ConvergenceException.class)
    public void testFitConvergenceException() {
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = createInitialMixture();
        m.fit(initialMixture, 1, 1.0E-5);
    }

    @Test
    public void testFitValidInput() {
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = createInitialMixture();
        m.fit(initialMixture);
        double logLikelihood = m.getLogLikelihood();
        assertNotNull(logLikelihood);
    }

    private MixtureMultivariateNormalDistribution createInitialMixture() {
        double[][] means = new double[][]{{0.0, 0.0}, {1.0, 1.0}, {2.0, 2.0}};
        double[][][] covMats = new double[][][]{
                {{1.0, 0.0}, {0.0, 1.0}},
                {{2.0, 0.0}, {0.0, 2.0}},
                {{3.0, 0.0}, {0.0, 3.0}}
        };
        double[] weights = new double[]{0.25, 0.35, 0.4};
        List<Pair<Double, MultivariateNormalDistribution>> components = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            components.add(new Pair<>(weights[i], new MultivariateNormalDistribution(means[i], covMats[i])));
        }
        return new MixtureMultivariateNormalDistribution(components);
    }
}