// UnivariateRealSolverUtilsTest.java

import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.SingularMatrixException;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        // Test with simple polynomial function
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0});
        double expectedResult = -1.0;
        try {
            double result = UnivariateRealSolverUtils.solve(function, -2.0, 0.0);
            assertEquals(expectedResult, result, 0.001);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        
        // Test with function that has no root in the given interval
        UnivariateRealFunction functionNoRoot = new PolynomialFunction(new double[]{1.0, 1.0, 1.0});
        try {
            UnivariateRealSolverUtils.solve(functionNoRoot, -2.0, 2.0);
            fail("Expected NoRootsException");
        } catch (NoRootsException e) {
            // Expected
        }
        
        // Test with function that throws NumberIsTooSmallException
        UnivariateRealFunction functionSmall = new PolynomialFunction(new double[]{0.0, 1.0, 0.0});
        try {
            UnivariateRealSolverUtils.solve(functionSmall, -2.0, 2.0);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
        
        // Test with function that throws MaxIterationsExceededException
        UnivariateRealSolverUtils.setMaxIterations(1); // Set max iterations to 1 for testing
        UnivariateRealFunction functionMaxIterations = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0});
        try {
            UnivariateRealSolverUtils.solve(functionMaxIterations, -2.0, 0.0);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
        
        // Test with invalid input: function is null
        UnivariateRealFunction nullFunction = null;
        try {
            UnivariateRealSolverUtils.solve(nullFunction, 0.0, 0.0);
            fail("Expected IllegalArgumentException: function is null");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test with invalid input: lowerBound is greater than upperBound
        UnivariateRealFunction functionLowerBoundGreaterUpperBound = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0});
        try {
            UnivariateRealSolverUtils.solve(functionLowerBoundGreaterUpperBound, 2.0, 1.0);
            fail("Expected IllegalArgumentException: lower bound is greater than upper bound");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Reset max iterations to default
        UnivariateRealSolverUtils.setMaxIterations(Integer.MAX_VALUE);
    }

    @Test
    public void testBracket() {
        // Test with simple polynomial function
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0});
        double[] expectedBracket = {-1.5, -0.5};
        try {
            double[] bracketResult = UnivariateRealSolverUtils.bracket(function, -2.0, -1.0, 0.0);
            assertEquals(expectedBracket[0], bracketResult[0], 0.001);
            assertEquals(expectedBracket[1], bracketResult[1], 0.001);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        
        // Test with function that throws ConvergenceException
        UnivariateRealFunction functionConvergence = new PolynomialFunction(new double[]{1.0, 1.0, 1.0});
        try {
            UnivariateRealSolverUtils.bracket(functionConvergence, -2.0, 2.0, 0.0);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
        
        // Test with function that throws SingularMatrixException
        UnivariateRealFunction functionSingular = new PolynomialFunction(new double[]{1.0, 0.0, 1.0});
        try {
            UnivariateRealSolverUtils.bracket(functionSingular, -2.0, 2.0, 0.0);
            fail("Expected SingularMatrixException");
        } catch (SingularMatrixException e) {
            // Expected
        }
        
        // Test with invalid input: function is null
        UnivariateRealFunction nullFunction = null;
        try {
            UnivariateRealSolverUtils.bracket(nullFunction, 0.0, 0.0, 0.0);
            fail("Expected IllegalArgumentException: function is null");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test with invalid input: lowerBound is greater than upperBound
        UnivariateRealFunction functionLowerBoundGreaterUpperBound = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0});
        try {
            UnivariateRealSolverUtils.bracket(functionLowerBoundGreaterUpperBound, 2.0, 1.0, 0.0);
            fail("Expected IllegalArgumentException: lower bound is greater than upper bound");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Set max iterations for bracketing test
    private static void setMaxIterations(int maxIterations) {
        UnivariateRealSolverUtils.LazyHolder.FACTORY.setMaxIterations(maxIterations);
    }
}