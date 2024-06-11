import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.junit.Test;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructor() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        
        assertNotNull(em.getData());
        assertEquals(3, em.getData().length);
        assertEquals(2, em.getData()[0].length);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_NotStrictlyPositive() {
        double[][] data = {};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructor_DimensionMismatch() {
        double[][] data = {{1.0, 2.0}, {2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_NumberIsTooSmall() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testFit() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution initialMixture = createInitialMixture();
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        
        em.fit(initialMixture, 1000, 1.0E-5);
        
        assertNotNull(em.getFittedModel());
        assertTrue(em.getLogLikelihood() != 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFit_MaxIterations_NotStrictlyPositive() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution initialMixture = createInitialMixture();
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        
        em.fit(initialMixture, -1000, 1.0E-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFit_Threshold_NotStrictlyPositive() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution initialMixture = createInitialMixture();
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        
        em.fit(initialMixture, 1000, -1.0E-5);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testFit_DimensionMismatch() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution initialMixture = createInitialMixture();
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        
        em.fit(initialMixture, 1000, 1.0E-5);
    }

    @Test
    public void testFit_DefaultParameters() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution initialMixture = createInitialMixture();
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        
        em.fit(initialMixture);
        
        assertNotNull(em.getFittedModel());
        assertTrue(em.getLogLikelihood() != 0.0);
    }

    @Test
    public void testEstimate() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}};
        int numComponents = 2;
        
        MixtureMultivariateNormalDistribution mixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);
        
        assertNotNull(mixture);
        assertEquals(numComponents, mixture.getComponents().size());
    }
    
    private MixtureMultivariateNormalDistribution createInitialMixture() {
        double[] weights = {0.5, 0.5};
        double[][] means = {{1.0, 2.0}, {2.0, 3.0}};
        double[][][] covariances = {{{1.0, 0.0}, {0.0, 1.0}}, {{2.0, 0.0}, {0.0, 2.0}}};
        
        List<Pair<Double, MultivariateNormalDistribution>> components = new ArrayList<>();
        for (int i = 0; i < weights.length; i++) {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means[i], covariances[i]);
            components.add(new Pair<>(weights[i], distribution));
        }
        
        return new MixtureMultivariateNormalDistribution(components);
    }
}