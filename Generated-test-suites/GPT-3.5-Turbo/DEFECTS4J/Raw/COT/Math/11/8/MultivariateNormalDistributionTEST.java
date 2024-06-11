
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class MultivariateNormalDistributionTest {

    private static final double EPSILON = 1e-9;

    private MultivariateNormalDistribution distribution;

    @Before
    public void setUp() throws NonPositiveDefiniteMatrixException, SingularMatrixException, DimensionMismatchException {
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        distribution = new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testGetMeans() {
        double[] expectedMeans = {0.0, 0.0};
        double[] actualMeans = distribution.getMeans();
        Assert.assertArrayEquals(expectedMeans, actualMeans, EPSILON);
    }

    @Test
    public void testGetCovariances() {
        double[][] expectedCovariances = {{1.0, 0.0}, {0.0, 1.0}};
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(expectedCovariances);
        RealMatrix actualMatrix = distribution.getCovariances();
        Assert.assertEquals(expectedMatrix, actualMatrix);
    }

    @Test
    public void testDensity() {
        double[] values = {0.0, 0.0};
        double expectedDensity = 0.15915494309189535;
        double actualDensity = distribution.density(values);
        Assert.assertEquals(expectedDensity, actualDensity, EPSILON);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] expectedDeviations = {1.0, 1.0};
        double[] actualDeviations = distribution.getStandardDeviations();
        Assert.assertArrayEquals(expectedDeviations, actualDeviations, EPSILON);
    }

    @Test
    public void testSample() {
        double[] sample = distribution.sample();
        Assert.assertEquals(2, sample.length);
    }

    @Test
    public void testGetExponentTerm() {
        double[] values = {0.0, 0.0};
        double expectedTerm = 1.0;
        double actualTerm = distribution.getExponentTerm(values);
        Assert.assertEquals(expectedTerm, actualTerm, EPSILON);
    }
}
