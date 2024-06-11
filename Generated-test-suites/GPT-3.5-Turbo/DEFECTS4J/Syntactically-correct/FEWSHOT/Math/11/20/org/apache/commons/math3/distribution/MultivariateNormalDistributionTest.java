package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class MultivariateNormalDistributionTest {

    private MultivariateNormalDistribution distribution;
    private final double[] means = {1.0, 2.0, 3.0};
    private final double[][] covariance = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};

    @Before
    public void setUp() throws SingularMatrixException, DimensionMismatchException {
        RandomGenerator generator = Mockito.mock(RandomGenerator.class);
        this.distribution = new MultivariateNormalDistribution(generator, means, covariance);
    }

    @Test
    public void testGetMeans() {
        assertArrayEquals(means, distribution.getMeans(), 0.0);
    }

    @Test
    public void testGetCovariances() {
        RealMatrix expectedCovariance = new Array2DRowRealMatrix(covariance);
        assertEquals(expectedCovariance, distribution.getCovariances());
    }

    @Test
    public void testDensity() {
        double[] values = {1.0, 2.0, 3.0};
        double expectedDensity = Math.pow(2 * Math.PI, -3 / 2) * Math.pow(1.0, -0.5) * distribution.getExponentTerm(values);
        assertEquals(expectedDensity, distribution.density(values), 0.0001);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] expectedStandardDeviations = {1.0, 1.0, 1.0};
        assertArrayEquals(expectedStandardDeviations, distribution.getStandardDeviations(), 0.0);
    }
    
    @Test
    public void testSample() {
        //TODO: Write test case to verify the sampling behavior
        //Sample the distribution and verify that the resulting values have the expected properties
    }

    @Test
    public void testGetExponentTerm() {
        double[] values = {1.0, 2.0, 3.0};
        double[] centered = new double[values.length];
        for (int i = 0; i < centered.length; i++) {
            centered[i] = values[i] - distribution.getMeans()[i];
        }
        RealMatrix covarianceMatrixInverse = distribution.getCovariances().inverse();
        double[] preMultiplied = covarianceMatrixInverse.preMultiply(centered);
        double sum = 0;
        for (int i = 0; i < preMultiplied.length; i++) {
            sum += preMultiplied[i] * centered[i];
        }
        double expectedExponentTerm = Math.exp(-0.5 * sum);
        assertEquals(expectedExponentTerm, distribution.getExponentTerm(values), 0.0001);
    }
}