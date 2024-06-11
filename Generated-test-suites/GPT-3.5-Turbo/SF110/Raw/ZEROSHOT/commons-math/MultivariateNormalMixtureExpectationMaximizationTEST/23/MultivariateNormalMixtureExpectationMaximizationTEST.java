import org.apache.commons.math3.distribution.fitting.MultivariateNormalMixtureExpectationMaximization;
import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalMixtureExpectationMaximizationTest {
    
    @Test
    public void testFit() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        initialMixture.getComponents().add(new Pair<Double, MultivariateNormalDistribution>(0.5, new MultivariateNormalDistribution(new double[]{2.0, 4.0}, new double[][]{{1.0, 0.0}, {0.0, 1.0}})));
        initialMixture.getComponents().add(new Pair<Double, MultivariateNormalDistribution>(0.5, new MultivariateNormalDistribution(new double[]{4.0, 6.0}, new double[][]{{1.0, 0.0}, {0.0, 1.0}})));
        
        try {
            em.fit(initialMixture, 1000, 1.0E-5);
            MixtureMultivariateNormalDistribution fittedModel = em.getFittedModel();
            Assert.assertNotNull(fittedModel);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testFit_DefaultValues() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        initialMixture.getComponents().add(new Pair<Double, MultivariateNormalDistribution>(0.5, new MultivariateNormalDistribution(new double[]{2.0, 4.0}, new double[][]{{1.0, 0.0}, {0.0, 1.0}})));
        initialMixture.getComponents().add(new Pair<Double, MultivariateNormalDistribution>(0.5, new MultivariateNormalDistribution(new double[]{4.0, 6.0}, new double[][]{{1.0, 0.0}, {0.0, 1.0}})));
        
        try {
            em.fit(initialMixture);
            MixtureMultivariateNormalDistribution fittedModel = em.getFittedModel();
            Assert.assertNotNull(fittedModel);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testEstimate() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        
        try {
            MixtureMultivariateNormalDistribution estimatedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
            Assert.assertNotNull(estimatedModel);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetLogLikelihood() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        initialMixture.getComponents().add(new Pair<Double, MultivariateNormalDistribution>(0.5, new MultivariateNormalDistribution(new double[]{2.0, 4.0}, new double[][]{{1.0, 0.0}, {0.0, 1.0}})));
        initialMixture.getComponents().add(new Pair<Double, MultivariateNormalDistribution>(0.5, new MultivariateNormalDistribution(new double[]{4.0, 6.0}, new double[][]{{1.0, 0.0}, {0.0, 1.0}})));
        
        try {
            em.fit(initialMixture, 1000, 1.0E-5);
            double logLikelihood = em.getLogLikelihood();
            Assert.assertTrue(logLikelihood != 0.0);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetFittedModel() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        initialMixture.getComponents().add(new Pair<Double, MultivariateNormalDistribution>(0.5, new MultivariateNormalDistribution(new double[]{2.0, 4.0}, new double[][]{{1.0, 0.0}, {0.0, 1.0}})));
        initialMixture.getComponents().add(new Pair<Double, MultivariateNormalDistribution>(0.5, new MultivariateNormalDistribution(new double[]{4.0, 6.0}, new double[][]{{1.0, 0.0}, {0.0, 1.0}})));
        
        try {
            em.fit(initialMixture, 1000, 1.0E-5);
            MixtureMultivariateNormalDistribution fittedModel = em.getFittedModel();
            Assert.assertNotNull(fittedModel);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }
}