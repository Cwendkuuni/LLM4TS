import org.junit.Test;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;

public class MultivariateNormalDistributionTest {

    @Test
    public void testGetMeans() {
        double[] means = {1, 2, 3};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, new double[3][3]);
        
        double[] result = distribution.getMeans();
        
        for (int i = 0; i < result.length; i++) {
            assert(result[i] == means[i]);
        }
    }

    @Test
    public void testGetCovariances() {
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new double[3], covariances);
        
        RealMatrix result = distribution.getCovariances();
        
        for (int i = 0; i < result.getRowDimension(); i++) {
            for (int j = 0; j < result.getColumnDimension(); j++) {
                assert(result.getEntry(i, j) == covariances[i][j]);
            }
        }
    }

    @Test
    public void testDensity() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{2, 0, 0}, {0, 3, 0}, {0, 0, 4}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        
        double[] vals = {1, 2, 3};
        double result = distribution.density(vals);
        
        double expected = 0.0389731992199859;
        assert(Math.abs(result - expected) < 0.0001);
    }

    @Test
    public void testGetStandardDeviations() {
        double[][] covariances = {{2, 0, 0}, {0, 3, 0}, {0, 0, 4}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new double[3], covariances);
        
        double[] result = distribution.getStandardDeviations();
        double[] expected = {Math.sqrt(2), Math.sqrt(3), Math.sqrt(4)};
        
        for (int i = 0; i < result.length; i++) {
            assert(result[i] == expected[i]);
        }
    }

    @Test
    public void testSample() {
        double[][] covariances = {{2, 0, 0}, {0, 3, 0}, {0, 0, 4}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new double[3], covariances);
        
        double[] result = distribution.sample();
        assert(result.length == 3);
    }

}