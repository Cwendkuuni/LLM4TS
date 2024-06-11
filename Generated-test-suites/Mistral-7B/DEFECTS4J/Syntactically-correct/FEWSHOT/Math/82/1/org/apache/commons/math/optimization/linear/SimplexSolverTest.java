package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math.optimization.PointValuePair;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.SimpleBoundingBox;
import org.apache.commons.math.optimization.SimpleConstraints;
import org.apache.commons.math.optimization.SimpleLinearObjectiveFunction;
import org.apache.commons.math.optimization.UnboundedSolutionException;
import org.apache.commons.math.optimization.NoFeasibleSolutionException;
import org.junit.Test;
import org.apache.commons.math3.util.MathUtils;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize() {
        final double[][] f = new double[][] { { 1.0, 5.0 }, { 2.0, -3.0 } };
        final SimpleConstraints constraints = new SimpleConstraints(new double[][] { { 1.0, 1.0 }, { 1.0, 1.0 } },
                new double[] { 0.0, 0.0 }, new double[] { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY });
        final SimpleLinearObjectiveFunction goal = new SimpleLinearObjectiveFunction(new double[] { 3.0, 2.0 });
        final SimplexTableau tableau = new SimplexTableau(f, constraints, goal, false, 1.0e-6);

        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = solver.doOptimize();

        PointValuePair expected = new PointValuePair(new double[] { 0.0, 1.0 }, new Double(2.5));
        assertEquals(expected, solution);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolutionException() {
        final double[][] f = new double[][] { { 1.0, 5.0 }, { 2.0, -3.0 }, { 0.0, Double.NEGATIVE_INFINITY } };
        final SimpleConstraints constraints = new SimpleConstraints(new double[][] { { 1.0, 1.0 }, { 1.0, 1.0 } },
                new double[] { 0.0, 0.0 }, new double[] { 0.0, 0.0 });
        final SimpleLinearObjectiveFunction goal = new SimpleLinearObjectiveFunction(new double[] { 3.0, 2.0 });
        final SimplexTableau tableau = new SimplexTableau(f, constraints, goal, false, 1.0e-6);

        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize();
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolutionException() {
        final double[][] f = new double[][] { { 1.0, 5.0 }, { 2.0, -3.0 }, { 0.0, 1.0 } };
        final SimpleConstraints constraints = new SimpleConstraints(new double[][] { { 1.0, 1.0 }, { 1.0, 1.0 } },
                new double[] { 0.0, 1.0 }, new double[] { 0.0, Double.POSITIVE_INFINITY });
        final SimpleLinearObjectiveFunction goal = new SimpleLinearObjectiveFunction(new double[] { 3.0, 2.0 });
        final SimplexTableau tableau = new SimplexTableau(f, constraints, goal, false, 1.0e-6);

        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize();
    }

    @Test
    public void testGetPivotColumn() {
        final double[][] tableau = new double[][] { { 1.0, 5.0, 3.0, 2.0, 1.0 },
            { 2.0, -3.0, 1.0, 4.0, 4.0 }, { 0.0, 0.0, 0.0, 1.0, 3.0 }, { 0.0, 0.0, 0.0, 0.0, 1.0 },
            { 0.0, 0.0, 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0, 0.0, 0.0 } };
        SimplexSolver solver = new SimplexSolver();
        Integer expected = 3;
        Integer result = solver.getPivotColumn(new SimplexTableau(tableau));
        assertEquals(expected, result);
    }

    @Test
    public void testGetPivotRow() {
        final double[][] tableau = new double[][] { { 1.0, 5.0, 3.0, 2.0, 1.0 },
            { 2.0, -3.0, 1.0, 4.0, 4.0 }, { 0.0, 0.0, 0.0, 1.0, 3.0 }, { 0.0, 0.0, 0.0, 0.0, 1.0 },
            { 0.0, 0.0, 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0, 0.0, 0.0 } };
        SimplexSolver solver = new SimplexSolver();
        Integer expected = 2;
        Integer result = solver.getPivotRow(3, new SimplexTableau(tableau));
        assertEquals(expected, result);
    }

    @Test
    public void testIsPhase1Solved() {
        final double[][] tableau = new double[][] { { 1.0, 5.0, 3.0, 2.0, -1.0, 1.0 },
            { 2.0, -3.0, 1.0, 4.0, -2.0, 4.0 }, { 0.0, 0.0, 0.0, 1.0, -1.0, 3.0 },
            { 0.0, 0.0, 0.0, 0.0, 1.0, 1.0 }, { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 } };
        SimplexSolver solver = new SimplexSolver();
        boolean expected = false;
        boolean result = solver.isPhase1Solved(new SimplexTableau(tableau));
        assertEquals(expected, result);

        tableau[3][5] = 0.0;
        expected = true;
        result = solver.isPhase1Solved(new SimplexTableau(tableau));
        assertEquals(expected, result);
    }

    @Test
    public void testIsOptimal() {
        final double[][] tableau = new double[][] { { 1.0, 5.0, 3.0, 2.0, 1.0, 1.0 },
            { 2.0, -3.0, 1.0, 4.0, 4.0, 4.0 }, { 0.0, 0.0, 0.0, 1.0, 3.0, 3.0 },
            { 0.0, 0.0, 0.0, 0.0, 1.0, 1.0 }, { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 } };
        SimplexSolver solver = new SimplexSolver();
        boolean expected = false;
        boolean result = solver.isOptimal(new SimplexTableau(tableau));
        assertEquals(expected, result);

        tableau[3][5] = 0.0;
        expected = true;
        result = solver.isOptimal(new SimplexTableau(tableau));
        assertEquals(expected, result);
    }

    @Test
    public void testSolvePhase1() {
        final double[][] f = new double[][] { { 1.0, 5.0, 3.0, 2.0, 1.0, 1.0 },
            { 2.0, -3.0, 1.0, 4.0, 4.0, 4.0 }, { 0.0, 0.0, 0.0, 1.0, 3.0, 3.0 },
            { 0.0, 0.0, 0.0, 0.0, 1.0, 1.0 }, { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 } };
        final SimpleConstraints constraints = new SimpleConstraints(new double[][] { { 1.0, 1.0 }, { 1.0, 1.0 } },
                new double[] { 0.0, 0.0 }, new double[] { 0.0, 0.0 });
        final SimpleLinearObjectiveFunction goal = new SimpleLinearObjectiveFunction(new double[] { 3.0, 2.0 });
        final SimplexTableau tableau = new SimplexTableau(f, constraints, goal, false, 1.0e-6);

        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(tableau);

        assertArrayEquals(new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 0.0 }, tableau.getColumns(), 0.0);
        assertEquals(0.0, tableau.getEntry(0, tableau.getWidth() - 1), 1.0e-6);
    }

    @Test
    public void testDoOptimizeWithConstraints() {
        final double[][] f = new double[][] { { 1.0, 5.0, 3.0, 2.0, 1.0 }, { 2.0, -3.0, 1.0, 4.0, 4.0 } };
        final SimpleConstraints constraints = new SimpleConstraints(new double[][] { { 1.0, 1.0 }, { 1.0, 1.0 } },
                new double[] { 0.0, 0.0 }, new double[] { 1.0, 1.0 });
        final SimpleLinearObjectiveFunction goal = new SimpleLinearObjectiveFunction(new double[] { 3.0, 2.0 });
        final SimplexTableau tableau = new SimplexTableau(f, constraints, goal, false, 1.0e-6);

        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = solver.doOptimize();

        PointValuePair expected = new PointValuePair(new double[] { 0.0, 1.0 }, new Double(2.5));
        assertEquals(expected, solution);
    }

    @Test
    public void testDoOptimizeWithBoundingBox() {
        final double[][] f = new double[][] { { 1.0, 5.0, 3.0, 2.0, 1.0 }, { 2.0, -3.0, 1.0, 4.0, 4.0 } };
        final SimpleBoundingBox bounds = new SimpleBoundingBox(new double[] { 0.0, 0.0 }, new double[] { 10.0, 10.0 });
        final SimpleLinearObjectiveFunction goal = new SimpleLinearObjectiveFunction(new double[] { 3.0, 2.0 });
        final SimplexTableau tableau = new SimplexTableau(f, null, goal, false, 1.0e-6, bounds);

        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = solver.doOptimize();

        PointValuePair expected = new PointValuePair(new double[] { 0.0, 1.0 }, new Double(2.5));
        assertEquals(expected, solution);
    }
}