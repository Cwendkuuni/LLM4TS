import static org.junit.Assert.*;
import org.junit.Test;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testFit() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        int maxIterations = 1000;
        double threshold = 1.0E-5;

        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);

        em.fit(initialMixture, maxIterations, threshold);

        MixtureMultivariateNormalDistribution fittedModel = em.getFittedModel();
        double logLikelihood = em.getLogLikelihood();

        // Assertions
        assertNotNull(fittedModel);
        assertEquals(3, fittedModel.getComponents().size());
        assertNotNull(logLikelihood);
    }

    @Test
    public void testFitWithDefaultParameters() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();

        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);

        em.fit(initialMixture);

        MixtureMultivariateNormalDistribution fittedModel = em.getFittedModel();
        double logLikelihood = em.getLogLikelihood();

        // Assertions
        assertNotNull(fittedModel);
        assertEquals(3, fittedModel.getComponents().size());
        assertNotNull(logLikelihood);
    }

    @Test
    public void testEstimate() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        int numComponents = 3;

        MixtureMultivariateNormalDistribution estimatedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);

        // Assertions
        assertNotNull(estimatedModel);
        assertEquals(numComponents, estimatedModel.getComponents().size());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInvalidData() throws Exception {
        double[][] data = {};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();

        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);

        em.fit(initialMixture);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testFitWithInvalidInitialMixture() throws Exception {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();

        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);

        em.fit(initialMixture);
    }

    // Add more test methods for all other public methods in the class

}