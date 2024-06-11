import org.junit.Test;
import org.junit.Assert;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructor() {
        double[][] testData = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(testData);
        Assert.assertNotNull(em);
    }

    @Test
    public void testFitWithInitialMixture() {
        // Test fitting with an initial mixture
        double[][] testData = {{1.0, 2.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        initialMixture.addComponent(0.5, new MultivariateNormalDistribution(new double[]{1.0, 2.0}, new double[][]{{1.0, 0.0}, {0.0, 1.0}}));
        initialMixture.addComponent(0.5, new MultivariateNormalDistribution(new double[]{3.0, 4.0}, new double[][]{{1.0, 0.0}, {0.0, 1.0}}));
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(testData);
        try {
            em.fit(initialMixture, 1000, 1.0E-5);
        } catch (Exception e) {
            Assert.fail("Exception thrown during fitting with an initial mixture");
        }
    }

    @Test
    public void testFitWithoutInitialMixture() {
        // Test fitting without an initial mixture
        double[][] testData = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(testData);
        try {
            em.fit();
        } catch (Exception e) {
            Assert.fail("Exception thrown during fitting without an initial mixture");
        }
    }

    @Test
    public void testEstimate() {
        // Test estimating the mixture with given data and number of components
        double[][] testData = {{1.0, 2.0}, {3.0, 4.0}};
        int numComponents = 2;
        try {
            MixtureMultivariateNormalDistribution mixture = MultivariateNormalMixtureExpectationMaximization.estimate(testData, numComponents);
            Assert.assertNotNull(mixture);
        } catch (Exception e) {
            Assert.fail("Exception thrown during estimation of mixture");
        }
    }

    @Test
    public void testGetLogLikelihood() {
        double[][] testData = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(testData);
        Assert.assertEquals(0.0, em.getLogLikelihood(), 0.0001);
    }

    @Test
    public void testGetFittedModel() {
        double[][] testData = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(testData);
        MixtureMultivariateNormalDistribution fittedModel = em.getFittedModel();
        Assert.assertNotNull(fittedModel);
    }

}