import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {
    
    @Test
    public void testConstructor() {
        double[][] data = {{1, 2}, {3, 4}, {5, 6}};
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        assertNotNull(m);
        assertArrayEquals(data, m.getData());
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidData() {
        double[][] data = {};
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithMismatchedDimensions() {
        double[][] data = {{1, 2}, {3, 4}, {5, 6}};
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithTooSmallDimension() {
        double[][] data = {{1}};
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testFit() {
        double[][] data = {{1, 2}, {3, 4}, {5, 6}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        initialMixture.addComponent(0.5, new MultivariateNormalDistribution(new double[]{1, 2}, new double[][]{{1,0},{0,1}}));
        initialMixture.addComponent(0.5, new MultivariateNormalDistribution(new double[]{3, 4}, new double[][]{{1,0},{0,1}}));
        
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        m.fit(initialMixture, 1000, 1.0E-5);
        
        assertEquals(0.0, m.getLogLikelihood(), 1.0E-5);
        assertNotNull(m.getFittedModel());
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInvalidMaxIterations() {
        double[][] data = {{1, 2}, {3, 4}, {5, 6}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        initialMixture.addComponent(0.5, new MultivariateNormalDistribution(new double[]{1, 2}, new double[][]{{1,0},{0,1}}));
        initialMixture.addComponent(0.5, new MultivariateNormalDistribution(new double[]{3, 4}, new double[][]{{1,0},{0,1}}));
        
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        m.fit(initialMixture, -1, 1.0E-5);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInvalidThreshold() {
        double[][] data = {{1, 2}, {3, 4}, {5, 6}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        initialMixture.addComponent(0.5, new MultivariateNormalDistribution(new double[]{1, 2}, new double[][]{{1,0},{0,1}}));
        initialMixture.addComponent(0.5, new MultivariateNormalDistribution(new double[]{3, 4}, new double[][]{{1,0},{0,1}}));
        
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        m.fit(initialMixture, 1000, -1.0E-5);
    }
    
    @Test
    public void testFitWithDefaultParameters() {
        double[][] data = {{1, 2}, {3, 4}, {5, 6}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        initialMixture.addComponent(0.5, new MultivariateNormalDistribution(new double[]{1, 2}, new double[][]{{1,0},{0,1}}));
        initialMixture.addComponent(0.5, new MultivariateNormalDistribution(new double[]{3, 4}, new double[][]{{1,0},{0,1}}));
        
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        m.fit(initialMixture);
        
        assertEquals(0.0, m.getLogLikelihood(), 1.0E-5);
        assertNotNull(m.getFittedModel());
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimateWithInvalidData() {
        double[][] data = {};
        MixtureMultivariateNormalDistribution m = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testEstimateWithInvalidNumComponents() {
        double[][] data = {{1, 2}, {3, 4}, {5, 6}};
        MixtureMultivariateNormalDistribution m = MultivariateNormalMixtureExpectationMaximization.estimate(data, 1);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testEstimateWithLargeNumComponents() {
        double[][] data = {{1, 2}, {3, 4}, {5, 6}};
        MixtureMultivariateNormalDistribution m = MultivariateNormalMixtureExpectationMaximization.estimate(data, 4);
    }
    
    @Test
    public void testGetters() {
        double[][] data = {{1, 2}, {3, 4}, {5, 6}};
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        
        assertEquals(data, m.getData());
        assertEquals(0.0, m.getLogLikelihood(), 1.0E-5);
        assertNotNull(m.getFittedModel());
    }
}