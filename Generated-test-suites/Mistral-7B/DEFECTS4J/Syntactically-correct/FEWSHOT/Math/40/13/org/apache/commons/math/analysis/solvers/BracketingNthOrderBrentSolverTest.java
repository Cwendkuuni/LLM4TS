package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidMaximalOrder() {
        new BracketingNthOrderBrentSolver(1);
    }

    @Test
    public void testGetMaximalOrder() {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0.001, 5);
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test
    public void testSolveWithDefaults() {
        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5 * x;
            }
        };
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        final double solution = solver.solve(100, function, -10, 10);
        assertEquals(Math.PI / 2, solution, 1e-6);
    }

    @Test
    public void testSolveWithSpecifiedMaximalOrder() {
        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5 * x;
            }
        };
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0.001, 10);
        final double solution = solver.solve(100, function, -10, 10);
        assertEquals(Math.PI / 2, solution, 1e-10);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveWithInvalidRelativeAccuracy() {
        new BracketingNthOrderBrentSolver(-0.001, 5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveWithInvalidAbsoluteAccuracy() {
        new BracketingNthOrderBrentSolver(0.001, -5e-6);
    }

    @Test
    public void testSolveWithSpecifiedRelativeAndAbsoluteAccuracy() {
        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5 * x;
            }
        };
        final double relativeAccuracy = 1e-8;
        final double absoluteAccuracy = 1e-12;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(relativeAccuracy, absoluteAccuracy, 5);
        final double solution = solver.solve(100, function, -10, 10);
        assertEquals(Math.PI / 2, solution, 1e-12);
    }

    @Test
    public void testSolveWithSpecifiedFunctionValueAccuracy() {
        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5 * x;
            }
        };
        final double relativeAccuracy = 1e-6;
        final double absoluteAccuracy = 1e-6;
        final double functionValueAccuracy = 1e-8;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, 5);
        final double solution = solver.solve(100, function, -10, 10);
        assertEquals(Math.PI / 2, solution, 1e-8);
    }

    @Test
    public void testAllowedSolutions() {
        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0.001, 5);
        assertNull(solver.solve(100, function, -5, 5, AllowedSolution.BELOW_SIDE));
        assertEquals(-2.0, solver.solve(100, function, -5, 5, AllowedSolution.LEFT_SIDE), 1e-6);
        assertEquals(-2.0, solver.solve(100, function, -5, 5, AllowedSolution.ANY_SIDE), 1e-6);
        assertEquals(-2.0, solver.solve(100, function, -5, 5, AllowedSolution.RIGHT_SIDE), 1e-6);
        assertEquals(2.0, solver.solve(100, function, -5, 5, AllowedSolution.ABOVE_SIDE), 1e-6);
    }

    @Test
    public void testSolveWithBracketingInterval() {
        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5 * x;
            }
        };
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0.001, 5);
        final double min = -10;
        final double max = 10;
        final double x1 = 1.5708;
        final double x2 = 3.14159265;
        final double y1 = Math.sin(x1) - 0.5 * x1;
        final double y2 = Math.sin(x2) - 0.5 * x2;
        assertArrayEquals(new double[]{x1, x2}, new double[]{solver.getMin(), solver.getStartValue()}, 1e-6);
        assertArrayEquals(new double[]{x2, x1}, new double[]{solver.getMax(), solver.getEndValue()}, 1e-6);
        assertEquals(y1, solver.computeObjectiveValue(x1), 1e-6);
        assertEquals(y2, solver.computeObjectiveValue(x2), 1e-6);
        assertFalse(Precision.equals(y1, y2, 1e-6));
        final double solution = solver.solve(100, function, min, max);
        assertEquals(Math.PI / 2, solution, 1e-6);
    }

    @Test
    public void testSolveWithStartValue() {
        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5 * x;
            }
        };
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0.001, 5);
        final double min = -10;
        final double max = 10;
        final double startValue = 1.5708;
        final double x1 = 1.5708;
        final double x2 = 3.14159265;
        final double y1 = Math.sin(x1) - 0.5 * x1;
        final double y2 = Math.sin(x2) - 0.5 * x2;
        assertEquals(startValue, solver.getStartValue(), 1e-6);
        assertArrayEquals(new double[]{x1, x2}, new double[]{solver.getMin(), solver.getEndValue()}, 1e-6);
        assertArrayEquals(new double[]{x2, x1}, new double[]{solver.getMax(), solver.getValueX()}, 1e-6);
        assertEquals(y1, solver.computeObjectiveValue(x1), 1e-6);
        assertEquals(y2, solver.computeObjectiveValue(x2), 1e-6);
        assertFalse(Precision.equals(y1, y2, 1e-6));
        final double solution = solver.solve(100, function, min, max, startValue);
        assertEquals(Math.PI / 2, solution, 1e-6);
    }

    @Test
    public void testSolveWithMinValue() {
        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5 * x;
            }
        };
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0.001, 5);
        final double min = 1.5708;
        final double max = 10;
        final double x1 = 1.5708;
        final double x2 = 3.14159265;
        final double y1 = Math.sin(x1) - 0.5 * x1;
        final double y2 = Math.sin(x2) - 0.5 * x2;
        assertArrayEquals(new double[]{x1, x2}, new double[]{solver.getMin(), solver.getEndValue()}, 1e-6);
        assertArrayEquals(new double[]{x2, x1}, new double[]{solver.getMax(), solver.getValueX()}, 1e-6);
        assertArrayEquals(new double[]{x1, x2}, new double[]{solver.getStartValue(), solver.getEndValue()}, 1e-6);
        assertEquals(y1, solver.computeObjectiveValue(x1), 1e-6);
        assertEquals(y2, solver.computeObjectiveValue(x2), 1e-6);
        assertFalse(Precision.equals(y1, y2, 1e-6));
        try {
            solver.solve(100, function, min, max);
            assertTrue(false); // should throw NoBracketingException
        } catch (NoBracketingException e) {
            // expected
        }
    }

    @Test
    public void testSolveWithMaxValue() {
        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5 * x;
            }
        };
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0.001, 5);
        final double min = -10;
        final double max = 1.5708;
        final double x1 = 1.5708;
        final double x2 = 3.14159265;
        final double y1 = Math.sin(x1) - 0.5 * x1;
        final double y2 = Math.sin(x2) - 0.5 * x2;
        assertArrayEquals(new double[]{x1, x2}, new double[]{solver.getMin(), solver.getValueX()}, 1e-6);
        assertArrayEquals(new double[]{x2, x1}, new double[]{solver.getMax(), solver.getEndValue()}, 1e-6);
        assertArrayEquals(new double[]{x1, x2}, new double[]{solver.getStartValue(), solver.getEndValue()}, 1e-6);
        assertEquals(y1, solver.computeObjectiveValue(x1), 1e-6);
        assertEquals(y2, solver.computeObjectiveValue(x2), 1e-6);
        assertFalse(Precision.equals(y1, y2, 1e-6));
        try {
            solver.solve(100, function, min, max);
            assertTrue(false); // should throw NoBracketingException
        } catch (NoBracketingException e) {
            // expected
        }
    }

}