package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.TestCompatibilityLevel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.IntStream;

@RunWith(Parameterized.class)
@TestCompatibilityLevel(TestCompatibilityLevel.JUNIT_4_12)
public class BracketingNthOrderBrentSolverTest {

    private static final double DEFAULT_ABSOLUTE_ACCURACY = 1e-6;
    private static final double DEFAULT_MAXIMAL_ORDER = 5;
    private static final double EPSILON = 1e-15;
    private static final double RELATIVE_ACCURACY = 1e-6;

    private final int maximalOrder;
    private final AllowedSolution allowedSolution;

    public BracketingNthOrderBrentSolverTest(int maximalOrder, AllowedSolution allowedSolution) {
        this.maximalOrder = maximalOrder;
        this.allowedSolution = allowedSolution;
    }

    @Parameterized.Parameters(name = "Maximal Order: {0}, Allowed Solution: {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {2, AllowedSolution.ANY_SIDE},
                {5, AllowedSolution.ANY_SIDE},
                {3, AllowedSolution.LEFT_SIDE},
                {5, AllowedSolution.LEFT_SIDE},
                {2, AllowedSolution.RIGHT_SIDE},
                {5, AllowedSolution.RIGHT_SIDE},
                {2, AllowedSolution.BELOW_SIDE},
                {5, AllowedSolution.BELOW_SIDE},
                {2, AllowedSolution.ABOVE_SIDE},
                {5, AllowedSolution.ABOVE_SIDE}
        });
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testInit_MaximalOrderTooSmall() throws NumberIsTooSmallException {
        new BracketingNthOrderBrentSolver(DEFAULT_ABSOLUTE_ACCURACY, 0);
    }

    @Test
    public void testConstructor_Defaults() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(DEFAULT_ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), EPSILON);
        assertEquals(DEFAULT_MAXIMAL_ORDER, solver.getMaximalOrder());
        assertEquals(AllowedSolution.ANY_SIDE, solver.getAllowedSolution());
    }

    @Test
    public void testConstructor_AbsoluteAccuracyRelativeAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(RELATIVE_ACCURACY, DEFAULT_ABSOLUTE_ACCURACY, DEFAULT_MAXIMAL_ORDER);
        assertEquals(RELATIVE_ACCURACY, solver.getRelativeAccuracy(), EPSILON);
        assertEquals(DEFAULT_ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), EPSILON);
        assertEquals(DEFAULT_MAXIMAL_ORDER, solver.getMaximalOrder());
        assertEquals(AllowedSolution.ANY_SIDE, solver.getAllowedSolution());
    }

    @Test
    public void testConstructor_AbsoluteAccuracyFunctionValueAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(
                RELATIVE_ACCURACY, DEFAULT_ABSOLUTE_ACCURACY, DEFAULT_ABSOLUTE_ACCURACY, DEFAULT_MAXIMAL_ORDER);
        assertEquals(RELATIVE_ACCURACY, solver.getRelativeAccuracy(), EPSILON);
        assertEquals(DEFAULT_ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), EPSILON);
        assertEquals(DEFAULT_ABSOLUTE_ACCURACY, solver.getFunctionValueAccuracy(), EPSILON);
        assertEquals(DEFAULT_MAXIMAL_ORDER, solver.getMaximalOrder());
        assertEquals(AllowedSolution.ANY_SIDE, solver.getAllowedSolution());
    }

    @Test
    public void testSolve_PolynomialFunction_AnySide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(maximalOrder);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, -2}, new double[]{1, 0, 1});
        double exactAnswer = PrecisionUtils.round(0.25, maximalOrder + 1);
        double result = solver.solve(32, f, -3, 3, AllowedSolution.ANY_SIDE);
        assertEquals(exactAnswer, result, EPSILON);
    }

    @Test
    public void testSolve_PolynomialFunction_LeftSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(maximalOrder, AllowedSolution.LEFT_SIDE);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, -2}, new double[]{1, 0, 1});
        double exactAnswer = PrecisionUtils.round(0.25, maximalOrder + 1);
        double result = solver.solve(32, f, -3, 3);
        assertEquals(exactAnswer, result, EPSILON);
    }

    @Test
    public void testSolve_PolynomialFunction_RightSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(maximalOrder, AllowedSolution.RIGHT_SIDE);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, -2}, new double[]{1, 0, 1});
        double exactAnswer = -PrecisionUtils.round(0.25, maximalOrder + 1);
        double result = solver.solve(32, f, -3, 3);
        assertEquals(exactAnswer, result, EPSILON);
    }

    @Test
    public void testSolve_PolynomialFunction_BelowSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(maximalOrder, AllowedSolution.BELOW_SIDE);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, -2}, new double[]{1, 0, 1});
        double exactAnswer = PrecisionUtils.round(0.25, maximalOrder + 1);
        double result = solver.solve(32, f, -3, 3, AllowedSolution.BELOW_SIDE);
        assertEquals(exactAnswer, result, EPSILON);
    }

    @Test
    public void testSolve_PolynomialFunction_AboveSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(maximalOrder, AllowedSolution.ABOVE_SIDE);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, -2}, new double[]{1, 0, 1});
        double exactAnswer = -PrecisionUtils.round(0.25, maximalOrder + 1);
        double result = solver.solve(32, f, -3, 3, AllowedSolution.ABOVE_SIDE);
        assertEquals(exactAnswer, result, EPSILON);
    }

    @Test
    public void testSolve_PolynomialFunction_NoBracketingException() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(maximalOrder);
        UnivariateFunction f = new PolynomialFunction(new double[]{1}, new double[]{0});
        assertThrows(NoBracketingException.class, () -> solver.solve(32, f, 0, 0));
    }

    @Test
    public void testSolve_PolynomialFunction_MaxEvalReached() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(maximalOrder);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, -2}, new double[]{1, 0, 1});
        assertThrows(IllegalStateException.class, () -> solver.solve(1, f, -3, 3));
    }

    @Test
    public void testSolve_PolynomialFunction_StartValue() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(maximalOrder);
        UnivariateFunction f = new PolynomialFunction(new double[]{1}, new double[]{0});
        double startValue = 1.1;
        double exactAnswer = -1.1;
        double result = solver.solve(32, f, -3, 3, startValue, AllowedSolution.ABOVE_SIDE);
        assertEquals(exactAnswer, result, EPSILON);
    }

    @Test
    public void testSolve_PolynomialFunction_StartValueBelowRoot() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(maximalOrder);
        UnivariateFunction f = new PolynomialFunction(new double[]{1}, new double[]{0});
        double startValue = -1.5;
        double exactAnswer = -1.1;
        double result = solver.solve(32, f, -3, 3, startValue, AllowedSolution.ABOVE_SIDE);
        assertEquals(exactAnswer, result, EPSILON);
    }

    @Test
    public void testSolve_PolynomialFunction_StartValueAboveRoot() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(maximalOrder);
        UnivariateFunction f = new PolynomialFunction(new double[]{1}, new double[]{0});
        double startValue = 1.5;
        double exactAnswer = 1.1;
        double result = solver.solve(32, f, -3, 3, startValue, AllowedSolution.BELOW_SIDE);
        assertEquals(exactAnswer, result, EPSILON);
    }

    @Test
    public void testSolve_PolynomialFunction_StartValueAboveRootWithMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(maximalOrder);
        UnivariateFunction f = new PolynomialFunction(new double[]{1}, new double[]{0});
        double startValue = 10;
        double exactAnswer = 1.1;
        double result = solver.solve(32, f, -3, 3, startValue, AllowedSolution.BELOW_SIDE);
        assertEquals(exactAnswer, result, EPSILON);
    }

    @Test
    public void testSolve_PolynomialFunction_MaximalOrderTooHigh() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(maximalOrder + 1);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, -2}, new double[]{1, 0, 1});
        assertThrows(IllegalArgumentException.class, () -> solver.solve(32, f, -3, 3, AllowedSolution.ANY_SIDE));
    }

    @Test
    public void testSolve_PolynomialFunction_MaximalOrderTooLow() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(maximalOrder - 1);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, -2}, new double[]{1, 0, 1});
        double result = solver.solve(32, f, -3, 3, AllowedSolution.ANY_SIDE);
        assertEquals(0.25, result, EPSILON);
    }
}