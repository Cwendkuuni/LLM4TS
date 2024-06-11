import org.junit.Test;

import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructor() {
        double[][] data1 = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization em1 = new MultivariateNormalMixtureExpectationMaximization(data1);
        assertNotNull(em1);

        double[][] data2 = {{1.0}, {2.0}, {3.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization em2 = new MultivariateNormalMixtureExpectationMaximization(data2);
            fail("Should throw NumberIsTooSmallException");
        } catch (Exception e) {
            assertTrue(e instanceof NumberIsTooSmallException);
        }
    }

    @Test
    public void testFit() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};

        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        try {
            MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
            em.fit(initialMixture, 100, 1.0E-6);
            assertNotNull(em.getFittedModel());
            assertNotNull(em.getLogLikelihood());
        } catch (Exception e) {
            fail("Should not throw any exceptions");
        }
    }

    @Test
    public void testFitWithDefaultParameters() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};

        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        try {
            MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
            em.fit(initialMixture);
            assertNotNull(em.getFittedModel());
            assertNotNull(em.getLogLikelihood());
        } catch (Exception e) {
            fail("Should not throw any exceptions");
        }
    }

    @Test
    public void testEstimate() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        try {
            MixtureMultivariateNormalDistribution mmnd = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
            assertNotNull(mmnd);
        } catch (Exception e) {
            fail("Should not throw any exceptions");
        }
    }
}