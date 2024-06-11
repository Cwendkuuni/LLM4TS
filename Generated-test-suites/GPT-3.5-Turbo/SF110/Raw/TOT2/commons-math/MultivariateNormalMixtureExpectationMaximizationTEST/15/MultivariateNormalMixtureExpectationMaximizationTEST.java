import static org.junit.Assert.*;
import org.junit.Test;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructor() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        assertNotNull(em);
    }
    
    @Test
    public void testFit() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        try {
            em.fit(initialMixture, 1000, 1.0E-5);
            assertNotNull(em.getFittedModel());
        } catch (Exception e) {
            fail("Fit method threw an exception");
        }
    }
    
    @Test
    public void testEstimate() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        try {
            MixtureMultivariateNormalDistribution result = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
            assertNotNull(result);
        } catch (Exception e) {
            fail("Estimate method threw an exception");
        }
    }
}