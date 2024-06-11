package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;

@Category(org.apache.commons.math3.FunctionCategory.REAL_SINGLE_VALUED)
public class BaseSecantSolverTest {

    protected static final double EPSILON = 1e-12;
    protected static final double ABSOLUTE_ACCURACY = 1e-6;
    protected static final double RELATIVE_ACCURACY = 1e-8;
    protected static final double FUNCTION_VALUE_ACCURACY = 1e-12;
    protected static final double MINIMUM = -1.0;
    protected static final double MAXIMUM = 1.0;

    protected BaseSecantSolver<UnivariateRealFunction> solver;

    @Before
    public void setUp() {
        UnivariateRealFunction function = new TestFunction();
        solver = new RegulaFalsiSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY,
                                       FUNCTION_VALUE_ACCURACY,
                                       BaseSecantSolver.Method.REGULA_FALSI);
        solver.setMaxIterations(1000);
        solver.setMin(MINIMUM);
        solver.setMax(MAXIMUM);
    }

    @Test
    public void testConstructorWithRelativeAccuracyAndAbsoluteAccuracy() {
        solver = new RegulaFalsiSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY,
                                       FUNCTION_VALUE_ACCURACY,
                                       BaseSecantSolver.Method.REGULA_FALSI);
        assertNotNull(solver);
    }

    @Test
    public void testConstructorWithRelativeAccuracyAbsoluteAccuracyFunctionValueAccuracy() {
        solver = new RegulaFalsiSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY,
                                       FUNCTION_VALUE_ACCURACY,
                                       BaseSecantSolver.Method.REGULA_FALSI);
        assertNotNull(solver);
    }

    @Test
    public void testConstructorWithMaxEval() throws MaxEvalExceededException,
            TooManyIterationsException {
        solver = new RegulaFalsiSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY,
                                       FUNCTION_VALUE_ACCURACY,
                                       BaseSecantSolver.Method.REGULA_FALSI, 10);
        assertNotNull(solver);
    }

    @Test
    public void testSolverWithAllowedSolutionAnySide() {
        UnivariateRealFunction function = new TestFunction();
        solver.setMin(MINIMUM);
        solver.setMax(MAXIMUM);
        double root = solver.solve(function);
        assertEquals(0.0, root, EPSILON);
    }

    @Test
    public void testSolverWithAllowedSolutionLeftSide() {
        UnivariateRealFunction function = new TestFunction();
        solver.setMin(MINIMUM);
        solver.setMax(MAXIMUM);
        solver.setAllowedSolution(AllowedSolution.LEFT_SIDE);
        double root = solver.solve(function);
        assertEquals(MINIMUM, root, EPSILON);
    }

    @Test
    public void testSolverWithAllowedSolutionRightSide() {
        UnivariateRealFunction function = new TestFunction();
        solver.setMin(MINIMUM);
        solver.setMax(MAXIMUM);
        solver.setAllowedSolution(AllowedSolution.RIGHT_SIDE);
        double root = solver.solve(function);
        assertEquals(MAXIMUM, root, EPSILON);
    }

    @Test
    public void testSolverWithAllowedSolutionBelowSide() {
        UnivariateRealFunction function = new TestFunction();
        solver.setMin(MINIMUM);
        solver.setMax(MAXIMUM);
        solver.setAllowedSolution(AllowedSolution.BELOW_SIDE);
        double root = solver.solve(function);
        assertTrue(MINIMUM < root);
    }

    @Test
    public void testSolverWithAllowedSolutionAboveSide() {
        UnivariateRealFunction function = new TestFunction();
        solver.setMin(MINIMUM);
        solver.setMax(MAXIMUM);
        solver.setAllowedSolution(AllowedSolution.ABOVE_SIDE);
        double root = solver.solve(function);
        assertTrue(MAXIMUM < root);
    }

    @Test
    public void testSolverWithAllowedSolutionAnySideAndInitialValue() {
        UnivariateRealFunction function = new TestFunction();
        double initialValue = new Random().nextDouble()
                * (MAXIMUM - MINIMUM) + MINIMUM;
        solver.setMin(MINIMUM);
        solver.setMax(MAXIMUM);
        solver.setAllowedSolution(AllowedSolution.ANY_SIDE);
        solver.setInitialGuess(initialValue);
        double root = solver.solve(function);
        assertEquals(0.0, root, EPSILON);
    }

    @Test
    public void testSolverWithAllowedSolutionLeftSideAndInitialValue() {
        UnivariateRealFunction function = new TestFunction();
        double initialValue = new Random().nextDouble()
                * (MAXIMUM - MINIMUM) + MINIMUM;
        solver.setMin(MINIMUM);
        solver.setMax(MAXIMUM);
        solver.setAllowedSolution(AllowedSolution.LEFT_SIDE);
        solver.setInitialGuess(initialValue);
        double root = solver.solve(function);
        assertEquals(MINIMUM, root, EPSILON);
    }

    @Test
    public void testSolverWithAllowedSolutionRightSideAndInitialValue() {
        UnivariateRealFunction function = new TestFunction();
        double initialValue = new Random().nextDouble()
                * (MAXIMUM - MINIMUM) + MINIMUM;
        solver.setMin(MINIMUM);
        solver.setMax(MAXIMUM);
        solver.setAllowedSolution(AllowedSolution.RIGHT_SIDE);
        solver.setInitialGuess(initialValue);
        double root = solver.solve(function);
        assertEquals(MAXIMUM, root, EPSILON);
    }

    @Test
    public void testSolverWithAllowedSolutionBelowSideAndInitialValue() {
        UnivariateRealFunction function = new TestFunction();
        double initialValue = new Random().nextDouble()
                * (MAXIMUM - MINIMUM) + MINIMUM;
        solver.setMin(MINIMUM);
        solver.setMax(MAXIMUM);
        solver.setAllowedSolution(AllowedSolution.BELOW_SIDE);
        solver.setInitialGuess(initialValue);
        double root = solver.solve(function);
        assertTrue(MINIMUM < root);
    }

    @Test
    public void testSolverWithAllowedSolutionAboveSideAndInitialValue() {
        UnivariateRealFunction function = new TestFunction();
        double initialValue = new Random().nextDouble()
                * (MAXIMUM - MINIMUM) + MINIMUM;
        solver.setMin(MINIMUM);
        solver.setMax(MAXIMUM);
        solver.setAllowedSolution(AllowedSolution.ABOVE_SIDE);
        solver.setInitialGuess(initialValue);
        double root = solver.solve(function);
        assertTrue(MAXIMUM < root);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolverWithNegativeAbsoluteAccuracy() {
        UnivariateRealFunction function = new TestFunction();
        solver = new RegulaFalsiSolver(-ABSOLUTE_ACCURACY, RELATIVE_ACCURACY,
                                       FUNCTION_VALUE_ACCURACY,
                                       BaseSecantSolver.Method.REGULA_FALSI);
        solver.setMin(MINIMUM);
        solver.setMax(MAXIMUM);
        solver.solve(function);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolverWithNegativeRelativeAccuracy() {
        UnivariateRealFunction function = new TestFunction();
        solver = new RegulaFalsiSolver(RELATIVE_ACCURACY, -ABSOLUTE_ACCURACY,
                                       FUNCTION_VALUE_ACCURACY,
                                       BaseSecantSolver.Method.REGULA_FALSI);
        solver.setMin(MINIMUM);
        solver.setMax(MAXIMUM);
        solver.solve(function);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolverWithNegativeFunctionValueAccuracy() {
        UnivariateRealFunction function = new TestFunction();
        solver = new RegulaFalsiSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY,
                                       -FUNCTION_VALUE_ACCURACY,
                                       BaseSecantSolver.Method.REGULA_FALSI);
        solver.setMin(MINIMUM);
        solver.setMax(MAXIMUM);
        solver.solve(function);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolverWithNullFunction() {
        UnivariateRealFunction function = null;
        solver = new RegulaFalsiSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY,
                                       FUNCTION_VALUE_ACCURACY,
                                       BaseSecantSolver.Method.REGULA_FALSI);
        solver.setMin(MINIMUM);
        solver.setMax(MAXIMUM);
        solver.solve(function);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolverWithNullMin() {
        UnivariateRealFunction function = new TestFunction();
        double min = null;
        solver = new RegulaFalsiSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY,
                                       FUNCTION_VALUE_ACCURACY,
                                       BaseSecantSolver.Method.REGULA_FALSI);
        solver.setFunction(function);
        solver.setMin(min);
        solver.solve(function);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolverWithNullMax() {
        UnivariateRealFunction function = new TestFunction();
        double max = null;
        solver = new RegulaFalsiSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY,
                                       FUNCTION_VALUE_ACCURACY,
                                       BaseSecantSolver.Method.REGULA_FALSI);
        solver.setFunction(function);
        solver.setMin(MINIMUM);
        solver.setMax(max);
        solver.solve(function);
    }

    @Test(expected = IllegalStateException.class)
    public void testSolverWithOverlappingBounds() {
        UnivariateRealFunction function = new TestFunction();
        solver.setFunction(function);
        solver.setMin(MAXIMUM);
        solver.setMax(MINIMUM);
        solver.solve(function);
    }

    @Test
    public void testSolverWithIllegalAllowedSolution() {
        UnivariateRealFunction function = new TestFunction();
        solver = new RegulaFalsiSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY,
                                       FUNCTION_VALUE_ACCURACY,
                                       BaseSecantSolver.Method.REGULA_FALSI);
        solver.setMin(MINIMUM);
        solver.setMax(MAXIMUM);
        try {
            solver.setAllowedSolution(AllowedSolution.SIDE_OUTSIDE);
            solver.solve(function);
            assertFalse(true); // This should not be reached
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid allowed solution: SIDE_OUTSIDE", e.getMessage());
        }
    }

    @FunctionalInterface
    private static interface TestFunction extends UnivariateRealFunction {
        @Override
        public double value(double x);
    }

}