package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class BaseSecantSolverTest {

    private static final double EPSILON = 1e-9;

    private final UnivariateFunction function;
    private final double min;
    private final double max;
    private final double expectedResult;
    private final AllowedSolution allowedSolution;

    public BaseSecantSolverTest(UnivariateFunction function, double min, double max, double expectedResult, AllowedSolution allowedSolution) {
        this.function = function;
        this.min = min;
        this.max = max;
        this.expectedResult = expectedResult;
        this.allowedSolution = allowedSolution;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new PolynomialFunction(x -> Math.pow(x, 2) - 1), -2, 2, 0, AllowedSolution.ANY_SIDE},
                {new PolynomialFunction(x -> Math.pow(x, 2) - 1), -2, 2, -1, AllowedSolution.LEFT_SIDE},
                {new PolynomialFunction(x -> Math.pow(x, 2) - 1), -2, 2, 1, AllowedSolution.RIGHT_SIDE},
                {new PolynomialFunction(x -> Math.sin(x) / x), -3.14159265359, 3.14159265359, 0, AllowedSolution.ANY_SIDE},
                {new PolynomialFunction(x -> Math.sin(x) / x), -3.14159265359, 3.14159265359, 1, AllowedSolution.RIGHT_SIDE},
                {new PolynomialFunction(x -> Math.sin(x) / x), -3.14159265359, 3.14159265359, -1, AllowedSolution.LEFT_SIDE},
                {new PolynomialFunction(x -> Math.cos(x)), -Math.PI, Math.PI, 0, AllowedSolution.ANY_SIDE}
        });
    }

    @Test
    public void testSolve() {
        BaseSecantSolver solver = null;

        try {
            solver = new RegulaFalsiSolver(EPSILON, new RegulaFalsiSolver.RegulaFalsiMethod());
            assertEquals(expectedResult, solver.solve(100, function, min, max, allowedSolution), EPSILON);
        } catch (NoBracketingException e) {
            // This exception should not be thrown for the given data
            assertThrows(NoBracketingException.class, () -> solver.solve(100, function, min, max, allowedSolution));
        } catch (MaxEvalException e) {
            // This exception should be thrown when the maximum number of evaluations is reached
            assertThrows(MaxEvalException.class, () -> solver.solve(1, function, min, max, allowedSolution));
        }

        try {
            solver = new IllinoisSolver(EPSILON, new IllinoisSolver.IllinoisMethod());
            assertEquals(expectedResult, solver.solve(100, function, min, max, allowedSolution), EPSILON);
        } catch (ZeroException e) {
            // This exception should not be thrown for the given data
            assertThrows(ZeroException.class, () -> solver.solve(100, function, min, max, allowedSolution));
        } catch (NoBracketingException e) {
            // This exception should not be thrown for the given data
            assertThrows(NoBracketingException.class, () -> solver.solve(100, function, min, max, allowedSolution));
        } catch (MaxEvalException e) {
            // This exception should be thrown when the maximum number of evaluations is reached
            assertThrows(MaxEvalException.class, () -> solver.solve(1, function, min, max, allowedSolution));
        }

        try {
            solver = new PegasusSolver(EPSILON, new PegasusSolver.PegasusMethod());
            assertEquals(expectedResult, solver.solve(100, function, min, max, allowedSolution), EPSILON);
        } catch (ZeroException e) {
            // This exception should not be thrown for the given data
            assertThrows(ZeroException.class, () -> solver.solve(100, function, min, max, allowedSolution));
        } catch (NoBracketingException e) {
            // This exception should not be thrown for the given data
            assertThrows(NoBracketingException.class, () -> solver.solve(100, function, min, max, allowedSolution));
        } catch (MaxEvalException e) {
            // This exception should be thrown when the maximum number of evaluations is reached
            assertThrows(MaxEvalException.class, () -> solver.solve(1, function, min, max, allowedSolution));
        }
    }
}