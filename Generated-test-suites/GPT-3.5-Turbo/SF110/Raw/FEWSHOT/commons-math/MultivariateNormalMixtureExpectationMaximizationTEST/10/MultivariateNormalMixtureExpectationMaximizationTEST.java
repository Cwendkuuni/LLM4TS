import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.distribution.fitting.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.*;
import java.util.*;
import org.apache.commons.math3.stat.correlation.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {
    
    @Test
    public void testConstructor_positiveData() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
            assertNotNull(em);
        } catch (NotStrictlyPositiveException | DimensionMismatchException | NumberIsTooSmallException e) {
            fail("Exception thrown");
        }
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_negativeData() throws NotStrictlyPositiveException, DimensionMismatchException, NumberIsTooSmallException {
        double[][] data = {{}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
    }
    
    @Test
    public void testFit_maxIterationsThreshold() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        try {
            MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
            em.fit(initialMixture, 1000, 1.0E-5);
        } catch (SingularMatrixException | NotStrictlyPositiveException | DimensionMismatchException | ConvergenceException e) {
            fail("Exception thrown");
        }
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFit_negativeMaxIterations() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        em.fit(initialMixture, -1, 1.0E-5);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFit_negativeThreshold() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        em.fit(initialMixture, 1000, -1.0E-5);
    }
    
    @Test
    public void testFit_noException() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        try {
            MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
            em.fit(initialMixture);
        } catch (SingularMatrixException | NotStrictlyPositiveException e) {
            fail("Exception thrown");
        }
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimate_negativeData() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = {{1.0, 2.0, 3.0}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testEstimate_smallNumComponents() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 1);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testEstimate_largeNumComponents() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 4);
    }
    
    @Test
    public void testGetLogLikelihood() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
            assertEquals(0.0, em.getLogLikelihood(), 0.0001);
        } catch (NotStrictlyPositiveException | DimensionMismatchException | NumberIsTooSmallException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testGetFittedModel() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
            assertNotNull(em.getFittedModel());
        } catch (NotStrictlyPositiveException | DimensionMismatchException | NumberIsTooSmallException e) {
            fail("Exception thrown");
        }
    }
}