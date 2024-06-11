import org.junit.Test;

import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testFit() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {1.5, 4.0}, {3.0, 1.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        int maxIterations = 1000;
        double threshold = 1.0E-5;

        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        em.fit(initialMixture, maxIterations, threshold);

        MixtureMultivariateNormalDistribution fittedModel = em.getFittedModel();
        assertNotNull(fittedModel);
    }

    @Test
    public void testFitDefaultParameters() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {1.5, 4.0}, {3.0, 1.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();

        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        em.fit(initialMixture);

        MixtureMultivariateNormalDistribution fittedModel = em.getFittedModel();
        assertNotNull(fittedModel);
    }

    @Test
    public void testEstimate() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {1.5, 4.0}, {3.0, 1.0}};
        int numComponents = 2;

        MixtureMultivariateNormalDistribution estimatedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);
        assertNotNull(estimatedModel);
    }

    @Test
    public void testGetLogLikelihood() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {1.5, 4.0}, {3.0, 1.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        int maxIterations = 1000;
        double threshold = 1.0E-5;

        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        em.fit(initialMixture, maxIterations, threshold);

        double logLikelihood = em.getLogLikelihood();
        assertTrue(Double.isFinite(logLikelihood));
    }

}