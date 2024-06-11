import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MultivariateNormalMixtureExpectationMaximizationTest {
    
    private double[][] testData;
    private MultivariateNormalMixtureExpectationMaximization em;
    
    @Before
    public void setUp() {
        testData = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        em = new MultivariateNormalMixtureExpectationMaximization(testData);
    }
    
    @Test
    public void testFitWithInitialMixture() {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        em.fit(initialMixture);
        assertNotNull(em.getFittedModel());
    }
    
    @Test
    public void testFitWithInitialMixtureAndParameters() {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        int maxIterations = 100;
        double threshold = 0.001;
        em.fit(initialMixture, maxIterations, threshold);
        assertNotNull(em.getFittedModel());
    }
    
    @Test
    public void testEstimate() {
        int numComponents = 2;
        try {
            MixtureMultivariateNormalDistribution estimated = MultivariateNormalMixtureExpectationMaximization.estimate(testData, numComponents);
            assertNotNull(estimated);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testConstructorWithInvalidData() {
        double[][] invalidData = new double[][]{};
        try {
            MultivariateNormalMixtureExpectationMaximization invalidEm = new MultivariateNormalMixtureExpectationMaximization(invalidData);
            fail("No exception thrown for invalid data");
        } catch (Exception e) {
            // Expected exception
        }
    }
    
    @Test
    public void testFitWithInvalidParameters() {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        int maxIterations = 0;
        double threshold = -0.001;
        try {
            em.fit(initialMixture, maxIterations, threshold);
            fail("No exception thrown for invalid parameters");
        } catch (Exception e) {
            // Expected exception
        }
    }
    
    @Test
    public void testFitConvergenceException() {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        try {
            em.fit(initialMixture, 1, 1.0);
            fail("No exception thrown for non-converging fit");
        } catch (ConvergenceException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Incorrect exception thrown: " + e.getMessage());
        }
    }
}