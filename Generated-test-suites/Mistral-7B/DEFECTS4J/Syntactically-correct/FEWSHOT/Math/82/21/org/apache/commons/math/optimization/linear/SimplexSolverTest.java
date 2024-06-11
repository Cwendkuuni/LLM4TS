package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.nonlinear.simple.AmplitudeConstrainedMinimizationProblem;
import org.apache.commons.math3.optim.nonlinear.simple.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.simple.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.simple.LinearObjectiveFunctionSet;
import org.apache.commons.math3.optim.nonlinear.simple.LinearConstraintSet;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testIterationsCounter() {
        SimplexSolver solver = new SimplexSolver();
        int initialCounter = solver.getIterationsCounter();
        solver.doIteration(null);
        int finalCounter = solver.getIterationsCounter();
        assertEquals("Iteration counter should increment", initialCounter + 1, finalCounter);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{
            {-2.5, 2.0, 1.0, 3.0, 4.0},
            {1.0, -1.5, 1.0, 2.0, 3.0},
            {1.5, 1.5, 1.0, 2.0, 3.0},
            {0.0, 0.0, 0.0, 1.0, 1.0},
            {0.0, 0.0, 0.0, 1.0, -1.0}
        }, null, LinearObjectiveFunction.MINIMIZE, true, 1.0e-6);

        int pivotColumn = new SimplexSolver().getPivotColumn(tableau);
        assertEquals("Expected pivot column to be 3", 3, pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{
            {-2.5, 2.0, 1.0, 3.0, 4.0},
            {1.0, -1.5, 1.0, 2.0, 3.0},
            {1.5, 1.5, 1.0, 2.0, 3.0},
            {0.0, 0.0, 0.0, 1.0, 1.0},
            {0.0, 0.0, 0.0, 1.0, -1.0}
        }, null, LinearObjectiveFunction.MINIMIZE, true, 1.0e-6);

        int pivotRow = new SimplexSolver().getPivotRow(3, tableau);
        assertEquals("Expected pivot row to be 1", 1, pivotRow);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolutionException() {
        SimplexSolver solver = new SimplexSolver(1.0e-3);
        SimplexTableau tableau = new SimplexTableau(new double[][]{
            {-2.5, 2.0, 1.0, 3.0, 4.0},
            {1.0, -1.5, 1.0, 2.0, 3.0},
            {1.5, 1.5, 1.0, 2.0, 3.0},
            {0.0, 0.0, 0.0, 1.0, 1.0},
            {0.0, 0.0, 0.0, 1.0, 1.0}
        }, null, LinearObjectiveFunction.MINIMIZE, true, 1.0e-3);
        solver.solvePhase1(tableau);
        solver.doIteration(tableau);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolutionException() {
        SimplexSolver solver = new SimplexSolver(1.0e-3);
        SimplexTableau tableau = new SimplexTableau(new double[][]{
            {-2.5, 2.0, 1.0, 3.0, 4.0},
            {1.0, -1.5, 1.0, 2.0, 3.0},
            {1.5, 1.5, 1.0, 2.0, 3.0},
            {0.0, 0.0, 0.0, 1.0, 1.0},
            {0.0, 0.0, 0.0, 1.0, -1.0}
        }, new LinearConstraintSet(new LinearConstraint[]{
            new LinearConstraint(new double[]{-1.0, 1.0, 1.0, 1.0, 1.0}, 0.0),
            new LinearConstraint(new double[]{-1.0, 1.0, 1.0, 1.0, 1.0}, 1.0),
            new LinearConstraint(new double[]{1.0, 1.0, 1.0, 1.0, 1.0}, 0.0),
            new LinearConstraint(new double[]{1.0, 1.0, 1.0, 1.0, 1.0}, 1.0)
        }), LinearObjectiveFunction.MINIMIZE, true, 1.0e-3);
        solver.solvePhase1(tableau);
    }

    @Test
    public void testSolvePhase1() {
        SimplexSolver solver = new SimplexSolver(1.0e-3);
        SimplexTableau tableau = new SimplexTableau(new double[][]{
            {-2.5, 2.0, 1.0, 3.0, 4.0},
            {1.0, -1.5, 1.0, 2.0, 3.0},
            {1.5, 1.5, 1.0, 2.0, 3.0},
            {0.0, 0.0, 0.0, 1.0, 1.0},
            {0.0, 0.0, 0.0, 1.0, -1.0}
        }, null, LinearObjectiveFunction.MINIMIZE, true, 1.0e-3);
        try {
            solver.solvePhase1(tableau);
        } catch (OptimizationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertTrue("Expected Phase 1 to be solved", solver.isPhase1Solved(tableau));
    }

    @Test
    public void testSolve() {
        SimplexSolver solver = new SimplexSolver(1.0e-3);
        LinearObjectiveFunctionSet objectiveFunctions = new LinearObjectiveFunctionSet(new LinearObjectiveFunction[]{
            new LinearObjectiveFunction(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, -1.0),
            new LinearObjectiveFunction(new double[]{6.0, 7.0, 8.0, 9.0, 10.0}, 1.0)
        });
        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraint[]{
            new LinearConstraint(new double[]{-1.0, 1.0, 1.0, 1.0, 1.0}, 0.0),
            new LinearConstraint(new double[]{-1.0, 1.0, 1.0, 1.0, 1.0}, 1.0),
            new LinearConstraint(new double[]{1.0, 1.0, 1.0, 1.0, 1.0}, 0.0),
            new LinearConstraint(new double[]{1.0, 1.0, 1.0, 1.0, 1.0}, 1.0)
        });
        AmplitudeConstrainedMinimizationProblem problem = new AmplitudeConstrainedMinimizationProblem(objectiveFunctions, constraints, LinearObjectiveFunction.MINIMIZE, true);
        RealPointValuePair result = solver.doOptimize();
        PointValuePair expectedResult = new PointValuePair(new double[]{-0.4999999999999999, -0.4999999999999999, -0.4999999999999999, 0.5, 0.5}, -1.1000000000000003);
        assertEquals("Expected solution to match", expectedResult, result);
    }

    @Test
    public void testSolveWithMaxIterations() {
        SimplexSolver solver = new SimplexSolver(1.0e-3, 10);
        LinearObjectiveFunctionSet objectiveFunctions = new LinearObjectiveFunctionSet(new LinearObjectiveFunction[]{
            new LinearObjectiveFunction(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, -1.0),
            new LinearObjectiveFunction(new double[]{6.0, 7.0, 8.0, 9.0, 10.0}, 1.0)
        });
        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraint[]{
            new LinearConstraint(new double[]{-1.0, 1.0, 1.0, 1.0, 1.0}, 0.0),
            new LinearConstraint(new double[]{-1.0, 1.0, 1.0, 1.0, 1.0}, 1.0),
            new LinearConstraint(new double[]{1.0, 1.0, 1.0, 1.0, 1.0}, 0.0),
            new LinearConstraint(new double[]{1.0, 1.0, 1.0, 1.0, 1.0}, 1.0)
        });
        AmplitudeConstrainedMinimizationProblem problem = new AmplitudeConstrainedMinimizationProblem(objectiveFunctions, constraints, LinearObjectiveFunction.MINIMIZE, true);
        try {
            solver.doOptimize();
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // expected
        }
    }
}