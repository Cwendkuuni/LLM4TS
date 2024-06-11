import static org.junit.Assert.*;
import org.junit.Test;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testFit() throws Exception {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        int maxIterations = 1000;
        double threshold = 1.0E-5;

        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        em.fit(initialMixture, maxIterations, threshold);
        
        // add assertions
    }

    @Test
    public void testFit2() throws Exception {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();

        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        em.fit(initialMixture);

        // add assertions
    }

    @Test
    public void testGetLogLikelihood() throws Exception {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        
        double logLikelihood = em.getLogLikelihood();
        
        // add assertions
    }

    @Test
    public void testGetFittedModel() throws Exception {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        
        MixtureMultivariateNormalDistribution fittedModel = em.getFittedModel();
        
        // add assertions
    }
}