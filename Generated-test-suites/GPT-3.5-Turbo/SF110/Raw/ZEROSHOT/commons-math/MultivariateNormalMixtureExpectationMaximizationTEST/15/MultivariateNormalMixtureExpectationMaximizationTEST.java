import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.stat.correlation.*;
import java.util.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructor() {
        double[][] data = {{1,2,3}, {4,5,6}, {7,8,9}};
        try {
            MultivariateNormalMixtureExpectationMaximization testObject = new MultivariateNormalMixtureExpectationMaximization(data);
            assertNotNull(testObject);
        } catch (NotStrictlyPositiveException e) {
            fail("NotStrictlyPositiveException thrown");
        } catch (DimensionMismatchException e) {
            fail("DimensionMismatchException thrown");
        } catch (NumberIsTooSmallException e) {
            fail("NumberIsTooSmallException thrown");
        }
    }

    @Test
    public void testFit() {
        double[][] data = {{1,2,3}, {4,5,6}, {7,8,9}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        int maxIterations = 1000;
        double threshold = 1.0E-5;
        MultivariateNormalMixtureExpectationMaximization testObject = null;
        try {
            testObject = new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (NotStrictlyPositiveException e) {
            fail("NotStrictlyPositiveException thrown");
        } catch (DimensionMismatchException e) {
            fail("DimensionMismatchException thrown");
        } catch (NumberIsTooSmallException e) {
            fail("NumberIsTooSmallException thrown");
        }
        
        try {
            testObject.fit(initialMixture, maxIterations, threshold);
        } catch (SingularMatrixException e) {
            fail("SingularMatrixException thrown");
        } catch (NotStrictlyPositiveException e) {
            fail("NotStrictlyPositiveException thrown");
        } catch (DimensionMismatchException e) {
            fail("DimensionMismatchException thrown");
        } catch (ConvergenceException e) {
            fail("ConvergenceException thrown");
        }
    }

    @Test
    public void testFitDefaultParams() {
        double[][] data = {{1,2,3}, {4,5,6}, {7,8,9}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        MultivariateNormalMixtureExpectationMaximization testObject = null;
        try {
            testObject = new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (NotStrictlyPositiveException e) {
            fail("NotStrictlyPositiveException thrown");
        } catch (DimensionMismatchException e) {
            fail("DimensionMismatchException thrown");
        } catch (NumberIsTooSmallException e) {
            fail("NumberIsTooSmallException thrown");
        }
        
        try {
            testObject.fit(initialMixture);
        } catch (SingularMatrixException e) {
            fail("SingularMatrixException thrown");
        } catch (NotStrictlyPositiveException e) {
            fail("NotStrictlyPositiveException thrown");
        } catch (DimensionMismatchException e) {
            fail("DimensionMismatchException thrown");
        } catch (ConvergenceException e) {
            fail("ConvergenceException thrown");
        }
    }

    @Test
    public void testEstimate() {
        double[][] data = {{1,2,3}, {4,5,6}, {7,8,9}};
        int numComponents = 3;
        try {
            MixtureMultivariateNormalDistribution result = MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);
            assertNotNull(result);
        } catch (NotStrictlyPositiveException e) {
            fail("NotStrictlyPositiveException thrown");
        } catch (DimensionMismatchException e) {
            fail("DimensionMismatchException thrown");
        } catch (NumberIsTooSmallException e) {
            fail("NumberIsTooSmallException thrown");
        } catch (NumberIsTooLargeException e) {
            fail("NumberIsTooLargeException thrown");
        }
    }

    @Test
    public void testGetLogLikelihood() {
        double[][] data = {{1,2,3}, {4,5,6}, {7,8,9}};
        MultivariateNormalMixtureExpectationMaximization testObject = null;
        try {
            testObject = new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (NotStrictlyPositiveException e) {
            fail("NotStrictlyPositiveException thrown");
        } catch (DimensionMismatchException e) {
            fail("DimensionMismatchException thrown");
        } catch (NumberIsTooSmallException e) {
            fail("NumberIsTooSmallException thrown");
        }
        assertEquals(0.0, testObject.getLogLikelihood(), 1.0E-5);
    }

    @Test
    public void testGetFittedModel() {
        double[][] data = {{1,2,3}, {4,5,6}, {7,8,9}};
        MultivariateNormalMixtureExpectationMaximization testObject = null;
        try {
            testObject = new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (NotStrictlyPositiveException e) {
            fail("NotStrictlyPositiveException thrown");
        } catch (DimensionMismatchException e) {
            fail("DimensionMismatchException thrown");
        } catch (NumberIsTooSmallException e) {
            fail("NumberIsTooSmallException thrown");
        }
        assertNotNull(testObject.getFittedModel());
    }

}