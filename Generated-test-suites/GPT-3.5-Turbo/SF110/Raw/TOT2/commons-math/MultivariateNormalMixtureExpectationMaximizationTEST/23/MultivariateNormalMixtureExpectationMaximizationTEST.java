import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructor() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization testObj = new MultivariateNormalMixtureExpectationMaximization(data);
        assertNotNull(testObj);
    }

    @Test
    public void testFitWithInitialMixture() {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        initialMixture.addDistribution(0.5, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new double[][]{{1, 0}, {0, 1}}));
        initialMixture.addDistribution(0.5, new MultivariateNormalDistribution(new double[]{2.0, 2.0}, new double[][]{{2, 0}, {0, 2}}));
        MultivariateNormalMixtureExpectationMaximization testObj = new MultivariateNormalMixtureExpectationMaximization(new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}});
        try {
            testObj.fit(initialMixture, 100, 0.00001);
        } catch (Exception e) {
            fail("Exception should not be thrown for valid input");
        }
    }

    @Test
    public void testFitWithInvalidThreshold() {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        initialMixture.addDistribution(0.5, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new double[][]{{1, 0}, {0, 1}}));
        initialMixture.addDistribution(0.5, new MultivariateNormalDistribution(new double[]{2.0, 2.0}, new double[][]{{2, 0}, {0, 2}}));
        MultivariateNormalMixtureExpectationMaximization testObj = new MultivariateNormalMixtureExpectationMaximization(new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}});
        try {
            testObj.fit(initialMixture, 100, -0.00001);
            fail("Exception should be thrown for negative threshold");
        } catch (Exception e) {
            assertTrue(e instanceof NotStrictlyPositiveException);
        }
    }


    @Test
    public void testEstimate() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        int numComponents = 2;
        try {
            MixtureMultivariateNormalDistribution result = MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);
            assertNotNull(result);
        } catch (Exception e) {
            fail("Exception should not be thrown for valid input");
        }
    }

    @Test
    public void testGetLogLikelihood() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization testObj = new MultivariateNormalMixtureExpectationMaximization(data);
        assertEquals(0.0, testObj.getLogLikelihood(), 0.0001);
    }

    @Test
    public void testGetFittedModel() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization testObj = new MultivariateNormalMixtureExpectationMaximization(data);
        assertNotNull(testObj.getFittedModel());
    }
}