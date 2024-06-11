package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.PointValueFunction;
import org.apache.commons.math3.optim.nonlinear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.PointValuePair.PairType;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.MaxEvaluationException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonfiniteSpaceException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NotPositiveDefiniteMatrixException;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize_simpleExample() {
        RealPointValuePair solution = new SimplexSolver().doOptimize();
        PointValuePair pvp = solution.getPoint();
        assertEquals(2.0, pvp.getX(0), Precision.SAFE_EPSILON);
        assertEquals(1.0, pvp.getX(1), Precision.SAFE_EPSILON);
        assertEquals(3.0, pvp.getF(), Precision.SAFE_EPSILON);
    }

    @Test
    public void testDoOptimize_doubleObjectiveFunction() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[][]{
            new double[]{-1.0, -2.0},
            new double[]{-3.0, -4.0}
        });
        SimplexTableau tableau = new SimplexTableau(
            new double[][]{
                new double[]{1.0, 1.0},
                new double[]{2.0, 2.0},
                new double[]{3.0, 3.0}
            },
            new LinearConstraint[]{},
            PointValueFunction.MINIMIZE,
            false,
            1e-6,
            obj
        );

        try {
            new SimplexSolver().doOptimize(tableau);
            fail();
        } catch (UnsatisfiedConstraintException e) {
            // Expected
        }
    }

    @Test
    public void testDoOptimize_infeasibleSolution() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[][]{
            new double[]{-1.0, -1.0},
            new double[]{-2.0, 1.0}
        });
        SimplexTableau tableau = new SimplexTableau(
            new double[][]{
                new double[]{1.0, 1.0},
                new double[]{2.0, -1.0},
                new double[]{3.0, 2.0}
            },
            new LinearConstraint[]{},
            PointValueFunction.MINIMIZE,
            false,
            1e-6,
            obj
        );

        try {
            new SimplexSolver().doOptimize(tableau);
            fail();
        } catch (NoFeasibleSolutionException e) {
            // Expected
        }
    }

    @Test
    public void testDoOptimize_unboundedSolution() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[][]{
            new double[]{1.0, 0.0},
            new double[]{0.0, 1.0}
        });
        SimplexTableau tableau = new SimplexTableau(
            new double[][]{
                new double[]{1.0, 1.0},
                new double[]{0.0, 1.0},
                new double[]{0.0, 1.0}
            },
            new LinearConstraint[]{},
            PointValueFunction.MINIMIZE,
            false,
            1e-6,
            obj
        );

        try {
            new SimplexSolver().doOptimize(tableau);
            fail();
        } catch (UnboundedSolutionException e) {
            // Expected
        }
    }

    @Test
    public void testDoIteration() {
        SimplexTableau tableau = new SimplexTableau(
            new double[][]{
                new double[]{1.0, 2.0, -1.0, 0.0},
                new double[]{0.0, 1.0, 0.0, -2.0},
                new double[]{0.0, 0.0, 1.0, -3.0}
            },
            new LinearConstraint[]{},
            PointValueFunction.MINIMIZE,
            false,
            1e-6,
            null
        );

        int iterations = 0;
        while (iterations++ < 10 && tableau.getNumArtificialVariables() > 0) {
            try {
                new SimplexSolver().doIteration(tableau);
            } catch (OptimizationException e) {
                break;
            }
        }

        assertEquals(4, tableau.getHeight());
        assertEquals(3, tableau.getWidth());
        assertFalse(MathUtils.compareTo(tableau.getEntry(1, 2), 0, 1e-6) < 0);
        assertEquals(-1.0 / 3.0, tableau.getEntry(2, 1), Precision.SAFE_EPSILON);
        assertFalse(MathUtils.compareTo(tableau.getEntry(2, 2), 0, 1e-6) < 0);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(
            new double[][]{
                new double[]{1.0, 2.0, -3.0, 0.0},
                new double[]{0.0, 1.0, 0.0, -2.0},
                new double[]{0.0, 0.0, 1.0, -3.0}
            },
            new LinearConstraint[]{},
            PointValueFunction.MINIMIZE,
            false,
            1e-6,
            null
        );

        Integer pivotCol = new SimplexSolver().getPivotColumn(tableau);
        assertEquals(0, pivotCol.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(
            new double[][]{
                new double[]{1.0, 2.0, -3.0, 0.0},
                new double[]{0.0, 1.0, 0.0, -2.0},
                new double[]{0.0, 0.0, 1.0, -3.0}
            },
            new LinearConstraint[]{},
            PointValueFunction.MINIMIZE,
            false,
            1e-6,
            null
        );

        Integer pivotRow = new SimplexSolver().getPivotRow(0, tableau);
        assertEquals(1, pivotRow.intValue());
    }

    @Test
    public void testSolvePhase1() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[][]{
            new double[]{-1.0, -1.0},
            new double[]{1.0, 1.0}
        });
        SimplexTableau tableau = new SimplexTableau(
            new double[][]{
                new double[]{1.0, 1.0, -1.0, 0.0},
                new double[]{1.0, 1.0, 0.0, -2.0},
                new double[]{0.0, 0.0, 1.0, -3.0}
            },
            new LinearConstraint[]{},
            PointValueFunction.MINIMIZE,
            false,
            1e-6,
            obj
        );

        try {
            new SimplexSolver().solvePhase1(tableau);
            fail();
        } catch (OptimizationException e) {
            // Expected
        }

        tableau = new SimplexTableau(
            new double[][]{
                new double[]{1.0, 1.0, -1.0, 0.0},
                new double[]{1.0, 1.0, 0.0, -2.0},
                new double[]{0.0, 0.0, 1.0, -3.0}
            },
            new LinearConstraint[]{},
            PointValueFunction.MINIMIZE,
            false,
            1e-6,
            obj
        );

        new SimplexSolver().solvePhase1(tableau);
        assertEquals(3, tableau.getNumArtificialVariables());
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(
            new double[][]{
                new double[]{1.0, 1.0, -1.0, 0.0},
                new double[]{1.0, 1.0, 0.0, -2.0},
                new double[]{0.0, 0.0, 1.0, -3.0}
            },
            new LinearConstraint[]{},
            PointValueFunction.MINIMIZE,
            false,
            1e-6,
            null
        );

        assertFalse(new SimplexSolver().isPhase1Solved(tableau));

        new SimplexSolver().solvePhase1(tableau);
        assertTrue(new SimplexSolver().isPhase1Solved(tableau));
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(
            new double[][]{
                new double[]{1.0, 1.0, -1.0, 0.0},
                new double[]{1.0, 1.0, 0.0, -2.0},
                new double[]{0.0, 0.0, 1.0, -3.0}
            },
            new LinearConstraint[]{},
            PointValueFunction.MINIMIZE,
            false,
            1e-6,
            null
        );

        assertFalse(new SimplexSolver().isOptimal(tableau));

        new SimplexSolver().solvePhase1(tableau);
        assertFalse(new SimplexSolver().isOptimal(tableau));

        new SimplexSolver().doIteration(tableau);
        assertTrue(new SimplexSolver().isOptimal(tableau));
    }
}