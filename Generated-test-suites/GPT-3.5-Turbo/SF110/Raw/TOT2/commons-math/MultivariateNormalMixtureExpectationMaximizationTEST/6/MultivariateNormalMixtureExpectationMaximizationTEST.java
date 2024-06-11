import static org.junit.Assert.*;

import org.junit.Test;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructor_ValidData_Success() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        
        assertNotNull(em);
        assertEquals(0.0, em.getLogLikelihood(), 0.001);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_EmptyData_ExceptionThrown() {
        double[][] data = {};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testConstructor_DataWithDifferentLength_ExceptionThrown() {
        double[][] data = {{1.0, 2.0}, {3.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testFit_ValidInput_Success() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        try {
            em.fit(initialMixture, 100, 0.0001);
        } catch (SingularMatrixException | NotStrictlyPositiveException | DimensionMismatchException e) {
            fail("Exception thrown for valid input");
        }
        
        assertNotNull(em.getFittedModel());
        assertTrue(em.getLogLikelihood() > Double.NEGATIVE_INFINITY);
    }
}