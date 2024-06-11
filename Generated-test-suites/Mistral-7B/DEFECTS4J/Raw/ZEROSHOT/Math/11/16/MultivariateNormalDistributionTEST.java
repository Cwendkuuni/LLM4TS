import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class MultivariateNormalDistributionTest {

    @Test
    public void testCreateWithValidArguments() throws SingularMatrixException,
            DimensionMismatchException, NonPositiveDefiniteMatrixException {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        assertEquals(means.length, mvnd.getDimension());
        assertEquals(means.length, mvnd.getSupportPointCount());
        assertArrayEquals(means, mvnd.getMeans());
        RealMatrix covMatrix = mvnd.getCovariances();
        assertArrayEquals(covariances, covMatrix.getData());
    }

    @Test(expected = SingularMatrixException.class)
    public void testCreateWithSingularCovarianceMatrix() throws SingularMatrixException,
            DimensionMismatchException, NonPositiveDefiniteMatrixException {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 0.0}};
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testCreateWithNonPositiveDefiniteCovarianceMatrix() throws SingularMatrixException,
            DimensionMismatchException, NonPositiveDefiniteMatrixException {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{2.0, -1.0}, {-1.0, 2.0}};
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test
    public void testCreateWithValid2DArrayCovarianceMatrix() throws SingularMatrixException,
            DimensionMismatchException, NonPositiveDefiniteMatrixException {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        assertEquals(means.length, mvnd.getDimension());
        assertEquals(means.length, mvnd.getSupportPointCount());
        assertArrayEquals(means, mvnd.getMeans());
        RealMatrix covMatrix = mvnd.getCovariances();
        assertArrayEquals(covariances, covMatrix.getData());
        assertEquals(covMatrixDeterminant(covariances), mvnd.getCovarianceMatrixDeterminant(), 1e-14);
    }

    @Test
    public void testCreateWithIdentityCovarianceMatrix() throws SingularMatrixException,
            DimensionMismatchException, NonPositiveDefiniteMatrixException {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        assertEquals(means.length, mvnd.getDimension());
        assertEquals(means.length, mvnd.getSupportPointCount());
        assertArrayEquals(means, mvnd.getMeans());
        RealMatrix covMatrix = mvnd.getCovariances();
        assertArrayEquals(covariances, covMatrix.getData());
        assertEquals(1.0, mvnd.getCovarianceMatrixDeterminant(), 1e-14);
    }

    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        double[] testValues = {1.5, 2.5};
        double density = mvnd.density(testValues);
        double expectedDensity = FastMath.pow(2 * FastMath.PI, -2) * FastMath.exp(-0.5 * (Math.pow(testValues[0] - 1.0, 2) + Math.pow(testValues[1] - 2.0, 2)));
        assertEquals(expectedDensity, density, 1e-14);
    }

    @Test
    public void testDensityNegativeValues() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        double[] testValues = {-1.5, -2.5};
        double density = mvnd.density(testValues);
        assertFalse(Double.isFinite(density));
    }

    @Test
    public void testDensityDimensionMismatch() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        double[] testValues = {1.5, 2.5, 3.0};
        double density = mvnd.density(testValues);
        assertEquals(0.0, density, 1e-14);
    }

    @Test
    public void testSample() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        double[] sample = mvnd.sample();
        double[] expectedSample = new double[means.length];
        random.nextGaussians(expectedSample);
        Array2DRowRealMatrix samplingMatrix = mvnd.getSamplingMatrix();
        double[] shiftedSample = new double[means.length];
        samplingMatrix.operate(expectedSample, shiftedSample, 0);
        for (int i = 0; i < shiftedSample.length; i++) {
            shiftedSample[i] += means[i];
        }
        assertArrayEquals(shiftedSample, sample);
    }

    @Test
    public void testSampleSameRandomInstance() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        double[] sample1 = mvnd.sample();
        double[] sample2 = mvnd.sample();
        assertNotSame(sample1, sample2);
    }

    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        assertEquals(means.length, mvnd.getMeans().length);
        assertArrayEquals(means, mvnd.getMeans());
    }

    @Test
    public void testGetCovariances() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        RealMatrix covarianceMatrix = mvnd.getCovariances();
        assertEquals(covariances.length, covarianceMatrix.getRowDimension());
        assertEquals(covariances[0].length, covarianceMatrix.getColumnDimension());
        assertArrayEquals(covariances[0], covarianceMatrix.getColumnVector(0).toArray());
        assertArrayEquals(covariances[1], covarianceMatrix.getColumnVector(1).toArray());
    }

    @Test
    public void testCovarianceMatrixDeterminant() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        assertEquals(1.0, mvnd.getCovarianceMatrixDeterminant(), 1e-14);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        double[] standardDeviations = mvnd.getStandardDeviations();
        assertEquals(means.length, standardDeviations.length);
        for (int i = 0; i < standardDeviations.length; i++) {
            assertEquals(Math.sqrt(covariances[i][i]), standardDeviations[i], 1e-14);
        }
    }

    private static double covMatrixDeterminant(double[][] matrix) {
        EigenDecomposition eigenDecomposition = new EigenDecomposition(new Array2DRowRealMatrix(matrix));
        return eigenDecomposition.getDeterminant();
    }
}