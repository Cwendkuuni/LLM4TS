import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    private MultivariateNormalMixtureExpectationMaximization mixtureEM;
    private double[][] data;

    @Before
    public void setUp() {
        data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        mixtureEM = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testFit() {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        int maxIterations = 100;
        double threshold = 1e-6;

        try {
            mixtureEM.fit(initialMixture, maxIterations, threshold);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }

        MixtureMultivariateNormalDistribution fittedModel = mixtureEM.getFittedModel();

        // Assert statements for fittedModel properties
        assertNotNull(fittedModel);
    }

    @Test
    public void testFit_DefaultParams() {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();

        try {
            mixtureEM.fit(initialMixture);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }

        MixtureMultivariateNormalDistribution fittedModel = mixtureEM.getFittedModel();

        // Assert statements for fittedModel properties
        assertNotNull(fittedModel);
    }

    @Test
    public void testEstimate() {
        int numComponents = 3;

        try {
            MixtureMultivariateNormalDistribution estimate = MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);
            // Assert statements for estimate properties
            assertNotNull(estimate);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetLogLikelihood() {
        double expected = 0.0;

        double actual = mixtureEM.getLogLikelihood();

        // Assert statement for actual
        assertEquals(expected, actual, 0.001);
    }
}