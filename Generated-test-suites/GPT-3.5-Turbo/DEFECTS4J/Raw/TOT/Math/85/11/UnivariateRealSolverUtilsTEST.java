import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    private UnivariateRealSolverUtils solverUtils;

    @Before
    public void setup() {
        solverUtils = new UnivariateRealSolverUtils();
    }

    // solve() tests

    @Test
    public void testSolve_typicalCase() throws Exception {
        // Given
        double expectedResult = 2;
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x - 2;
            }
        };
        double x0 = 1;
        double x1 = 3;

        // When
        double result = solverUtils.solve(function, x0, x1);

        // Then
        assertEquals(expectedResult, result, 0);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_convergenceException() throws Exception {
        // Given
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x - 2;
            }
        };
        double x0 = 1;
        double x1 = 1.5;

        // When
        double result = solverUtils.solve(function, x0, x1);

        // Then
        // Exception is expected
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolve_functionEvaluationException() throws Exception {
        // Given
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };
        double x0 = 1;
        double x1 = 3;

        // When
        double result = solverUtils.solve(function, x0, x1);

        // Then
        // Exception is expected
    }

    // solve(f, x0, x1, absoluteAccuracy) tests

    @Test
    public void testSolveWithAccuracy_typicalCase() throws Exception {
        // Given
        double expectedResult = 2;
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x - 2;
            }
        };
        double x0 = 1;
        double x1 = 3;
        double absoluteAccuracy = 0.1;

        // When
        double result = solverUtils.solve(function, x0, x1, absoluteAccuracy);

        // Then
        assertEquals(expectedResult, result, absoluteAccuracy);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveWithAccuracy_convergenceException() throws Exception {
        // Given
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x - 2;
            }
        };
        double x0 = 1;
        double x1 = 1.5;
        double absoluteAccuracy = 0.1;

        // When
        double result = solverUtils.solve(function, x0, x1, absoluteAccuracy);

        // Then
        // Exception is expected
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveWithAccuracy_functionEvaluationException() throws Exception {
        // Given
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };
        double x0 = 1;
        double x1 = 3;
        double absoluteAccuracy = 0.1;

        // When
        double result = solverUtils.solve(function, x0, x1, absoluteAccuracy);

        // Then
        // Exception is expected
    }

    // bracket() tests

    @Test
    public void testBracket_typicalCase() throws Exception {
        // Given
        double[] expectedArray = {1, 3};
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x - 2;
            }
        };
        double initial = 2;
        double lowerBound = 1;
        double upperBound = 3;

        // When
        double[] resultArray = solverUtils.bracket(function, initial, lowerBound, upperBound);

        // Then
        assertArrayEquals(expectedArray, resultArray, 0);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracket_convergenceException() throws Exception {
        // Given
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x - 2;
            }
        };
        double initial = 1;
        double lowerBound = 1;
        double upperBound = 1.5;

        // When
        double[] resultArray = solverUtils.bracket(function, initial, lowerBound, upperBound);

        // Then
        // Exception is expected
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBracket_functionEvaluationException() throws Exception {
        // Given
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };
        double initial = 2;
        double lowerBound = 1;
        double upperBound = 3;

        // When
        double[] resultArray = solverUtils.bracket(function, initial, lowerBound, upperBound);

        // Then
        // Exception is expected
    }

    // midpoint() tests

    @Test
    public void testMidpoint_typicalCase() {
        // Given
        double a = 1;
        double b = 3;
        double expectedMidpoint = 2;

        // When
        double midpoint = solverUtils.midpoint(a, b);

        // Then
        assertEquals(expectedMidpoint, midpoint, 0);
    }

}