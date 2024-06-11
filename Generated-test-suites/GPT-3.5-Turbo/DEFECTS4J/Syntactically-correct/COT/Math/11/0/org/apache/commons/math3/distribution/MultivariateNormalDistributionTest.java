import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

@Test
public void testGetMeans() {
    double[] means = {1.0, 2.0, 3.0};
    
    MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
    
    double[] result = distribution.getMeans();
    
    assertArrayEquals(means, result, 0.0001);
}

@Test
public void testGetCovariances() {
    double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
    
    MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new double[]{1.0, 2.0, 3.0}, covariances);
    
    double[][] result = distribution.getCovariances().getData();
    
    assertArrayEquals(covariances, result);
}

@Test
public void testDensity() {
    double[] means = {0.0, 0.0};
    
    MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, new double[][]{{1.0, 0.0}, {0.0, 1.0}});
    
    double[] vals = {1.0, 1.0};
    
    double result = distribution.density(vals);
    
    assertEquals(0.05854983152, result, 0.0001);
}

@Test
public void testGetStandardDeviations() {
    double[][] covariances = {{4.0, 0.0}, {0.0, 9.0}};
    double[] expected = {2.0, 3.0};
    
    MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new double[]{0.0, 0.0}, covariances);
    
    double[] result = distribution.getStandardDeviations();
    
    assertArrayEquals(expected, result, 0.0001);
}

@Test
public void testSample() {
    double[] means = {0.0, 0.0};
    
    MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, new double[][]{{1.0, 0.0}, {0.0, 1.0}});
    
    double[] result = distribution.sample();
    
    assertEquals(2, result.length);
}

@Test
public void testGetExponentTerm() {
    double[] means = {0.0, 0.0};
    
    MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, new double[][]{{1.0, 0.0}, {0.0, 1.0}});
    
    double[] values = {1.0, 1.0};
    
    double result = distribution.getExponentTerm(values);
    
    assertEquals(0.01831563888, result, 0.0001);
}

}