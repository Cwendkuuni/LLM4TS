import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testFit() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        try {
            em.fit(initialMixture, 1000, 1.0E-5);
            double logLikelihood = em.getLogLikelihood();
            MixtureMultivariateNormalDistribution fittedModel = em.getFittedModel();
            assertNotNull(fittedModel);
        } catch (SingularMatrixException | NotStrictlyPositiveException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFitWithDefaultValues() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        try {
            em.fit(initialMixture);
            double logLikelihood = em.getLogLikelihood();
            MixtureMultivariateNormalDistribution fittedModel = em.getFittedModel();
            assertNotNull(fittedModel);
        } catch (SingularMatrixException | NotStrictlyPositiveException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testEstimate() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        try {
            MixtureMultivariateNormalDistribution mixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
            assertNotNull(mixture);
        } catch (NotStrictlyPositiveException | DimensionMismatchException e) {
            fail(e.getMessage());
        }
    }
}