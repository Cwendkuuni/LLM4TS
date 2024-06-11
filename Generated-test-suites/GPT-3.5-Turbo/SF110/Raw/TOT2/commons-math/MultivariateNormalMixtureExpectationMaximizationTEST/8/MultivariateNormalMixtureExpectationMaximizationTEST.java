import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructor_WithValidData_InitializeDataCorrectly() {
        // Arrange
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        
        // Act
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        
        // Assert
        assertArrayEquals(data, em.getData());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_WithEmptyData_ThrowNotStrictlyPositiveException() throws NotStrictlyPositiveException {
        // Arrange
        double[][] data = {};
        
        // Act
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        
        // Assert - Exception expected
    }
    
    @Test
    public void testFit_WithValidInitialMixture_CalculateLogLikelihoodCorrectly() {
        // Arrange
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(someComponents);
        
        // Act
        em.fit(initialMixture);
        
        // Assert
        double logLikelihood = em.getLogLikelihood();
        assertTrue(logLikelihood >= 0.0);
    }
    
    // Add more test cases for other methods

}