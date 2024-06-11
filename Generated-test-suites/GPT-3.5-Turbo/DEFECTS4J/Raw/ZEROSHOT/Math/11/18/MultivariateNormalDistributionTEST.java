import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.util.MathArrays;

public class MultivariateNormalDistributionTest {
    private static final double[] MEANS = {1.0, 2.0};
    private static final double[][] COVARIANCES = {{2.0, 0.5}, {0.5, 1.0}};
    
    @Test
    public void testConstructor() {
        try {
            MultivariateNormalDistribution dist = new MultivariateNormalDistribution(MEANS, COVARIANCES);
            assertEquals(MEANS.length, dist.getDimension());
            assertArrayEquals(MEANS, dist.getMeans(), 0.0);
            assertArrayEquals(COVARIANCES, dist.getCovariances().getData(), 0.0);
        } catch (DimensionMismatchException e) {
            fail("DimensionMismatchException thrown: " + e.getMessage());
        } catch (SingularMatrixException e) {
            fail("SingularMatrixException thrown: " + e.getMessage());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("NonPositiveDefiniteMatrixException thrown: " + e.getMessage());
        }
    }
    
    @Test(expected=DimensionMismatchException.class)
    public void testConstructorDimensionMismatch() throws DimensionMismatchException, SingularMatrixException, NonPositiveDefiniteMatrixException {
        double[] means = {1.0, 2.0, 3.0};
        new MultivariateNormalDistribution(means, COVARIANCES);
    }
    
    @Test(expected=SingularMatrixException.class)
    public void testConstructorSingularMatrix() throws DimensionMismatchException, SingularMatrixException, NonPositiveDefiniteMatrixException {
        double[][] covariances = {{2.0, 1.0}, {0.5, 0.5}};
        new MultivariateNormalDistribution(MEANS, covariances);
    }
    
    @Test(expected=NonPositiveDefiniteMatrixException.class)
    public void testConstructorNonPositiveDefiniteMatrix() throws DimensionMismatchException, SingularMatrixException, NonPositiveDefiniteMatrixException {
        double[][] covariances = {{2.0, 0.5}, {0.5, -1.0}};
        new MultivariateNormalDistribution(MEANS, covariances);
    }
    
    @Test
    public void testDensity() {
        try {
            MultivariateNormalDistribution dist = new MultivariateNormalDistribution(MEANS, COVARIANCES);
            double[] values = {1.5, 2.5};
            double expectedDensity = 0.0620328389895405;
            assertEquals(expectedDensity, dist.density(values), 1e-15);
        } catch (DimensionMismatchException e) {
            fail("DimensionMismatchException thrown: " + e.getMessage());
        } catch (SingularMatrixException e) {
            fail("SingularMatrixException thrown: " + e.getMessage());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("NonPositiveDefiniteMatrixException thrown: " + e.getMessage());
        }
    }
    
    @Test(expected=DimensionMismatchException.class)
    public void testDensityDimensionMismatch() throws DimensionMismatchException, SingularMatrixException, NonPositiveDefiniteMatrixException {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(MEANS, COVARIANCES);
        double[] values = {1.5, 2.5, 3.5};
        dist.density(values);
    }
    
    @Test
    public void testGetStandardDeviations() {
        try {
            MultivariateNormalDistribution dist = new MultivariateNormalDistribution(MEANS, COVARIANCES);
            double[] expectedDeviations = {1.4142135623730951, 1.0};
            assertArrayEquals(expectedDeviations, dist.getStandardDeviations(), 1e-15);
        } catch (DimensionMismatchException e) {
            fail("DimensionMismatchException thrown: " + e.getMessage());
        } catch (SingularMatrixException e) {
            fail("SingularMatrixException thrown: " + e.getMessage());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("NonPositiveDefiniteMatrixException thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testSample() {
        try {
            MultivariateNormalDistribution dist = new MultivariateNormalDistribution(MEANS, COVARIANCES);
            double[] sample = dist.sample();
            assertEquals(MEANS.length, sample.length);
        } catch (DimensionMismatchException e) {
            fail("DimensionMismatchException thrown: " + e.getMessage());
        } catch (SingularMatrixException e) {
            fail("SingularMatrixException thrown: " + e.getMessage());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("NonPositiveDefiniteMatrixException thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetExponentTerm() {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(MEANS, COVARIANCES);
        double[] values = {1.5, 2.5};
        double expectedExponentTerm = 0.0620328389895405;
        assertEquals(expectedExponentTerm, dist.getExponentTerm(values), 1e-15);
    }
}