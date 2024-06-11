import static org.junit.Assert.*;

import org.junit.Test;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructor() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        assertNotNull(em);
    }

    @Test
    public void testFit() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution mixture = new MixtureMultivariateNormalDistribution();
        try {
            em.fit(mixture, 1000, 1.0E-5);
        } catch (Exception e) {
            fail("Exception thrown while fitting");
        }
    }

    @Test
    public void testFitDefault() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution mixture = new MixtureMultivariateNormalDistribution();
        try {
            em.fit(mixture);
        } catch (Exception e) {
            fail("Exception thrown while fitting with default parameters");
        }
    }

    @Test
    public void testEstimate() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}};
        try {
            MixtureMultivariateNormalDistribution mixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
            assertNotNull(mixture);
        } catch (Exception e) {
            fail("Exception thrown while estimating");
        }
    }

    @Test
    public void testGetLogLikelihood() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        assertEquals(0.0, em.getLogLikelihood(), 0.0);
    }

    @Test
    public void testGetFittedModel() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        assertNotNull(em.getFittedModel());
    }

}