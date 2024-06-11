package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealColumnVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealPointValuePair;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

import org.apache.commons.cli.SimplexSolver;

public class SimplexSolverTest {

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolutionException() throws Exception {
        SimplexSolver solver = new SimplexSolver();
        double[][] f = {{1.0, 2.0, 3.0}, {-1.0, -1.0, -1.0}, {-0.5, -0.5, -0.5}, {-1.0, -1.0, 0.0}};
        RealMatrix constraints = new Array2DRowRealMatrix(new double[][]{{-Double.MAX_VALUE, -1.0, -1.0}, {0.0, 0.0, 1.0}});
        SimplexSolver.GoalType goalType = SimplexSolver.GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 1.0e-9;
        RealPointValuePair solution = solver.doOptimize();
        fail("Expected UnboundedSolutionException");
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolutionException() throws Exception {
        SimplexSolver solver = new SimplexSolver();
        double[][] f = {{1.0, 2.0, 3.0}, {-1.0, -1.0, -1.0}, {-0.5, -0.5, -0.5}, {-1.0, -1.0, 0.0}, {-1.0, -1.0, -1.0}};
        RealMatrix constraints = new Array2DRowRealMatrix(new double[][]{{-Double.MAX_VALUE, -1.0, -1.0}, {0.0, 0.0, 1.0}});
        SimplexSolver.GoalType goalType = SimplexSolver.GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 1.0e-9;
        RealPointValuePair solution = solver.doOptimize();
        fail("Expected NoFeasibleSolutionException");
    }

    @Test
    public void testSolvePhase1() throws Exception {
        SimplexSolver solver = new SimplexSolver();
        double[][] f = {{1.0, 2.0, 3.0}, {-1.0, -1.0, -1.0}, {-0.5, -0.5, -0.5}, {0.0, 0.0, 0.0}};
        RealMatrix constraints = new Array2DRowRealMatrix(new double[][]{{-Double.MAX_VALUE, -1.0, -1.0}, {0.0, 0.0, 1.0}});
        SimplexSolver.GoalType goalType = SimplexSolver.GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 1.0e-9;

        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        solver.solvePhase1(tableau);

        assertTrue(tableau.getNumArtificialVariables() == 0);
        for (int i = 0; i < tableau.getNumObjectiveFunctions(); i++) {
            assertTrue(MathUtils.compareTo(tableau.getEntry(0, i), 0, epsilon) >= 0);
        }
    }

    @Test
    public void testIsOptimal() throws Exception {
        SimplexSolver solver = new SimplexSolver();
        double[][] f = {{1.0, 2.0, 3.0}, {-1.0, -1.0, -1.0}, {-0.5, -0.5, -0.5}, {-1.0, -1.0, 0.0}};
        RealMatrix constraints = new Array2DRowRealMatrix(new double[][]{{-Double.MAX_VALUE, -1.0, -1.0}, {0.0, 0.0, 1.0}});
        SimplexSolver.GoalType goalType = SimplexSolver.GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 1.0e-9;

        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        solver.solvePhase1(tableau);
        tableau.discardArtificialVariables();

        assertTrue(solver.isOptimal(tableau));
    }

    @Test
    public void testIterationsCounter() throws Exception {
        SimplexSolver solver = new SimplexSolver();
        double[][] f = {{1.0, 2.0, 3.0}, {-1.0, -1.0, -1.0}, {-0.5, -0.5, -0.5}, {-1.0, -1.0, 0.0}};
        RealMatrix constraints = new Array2DRowRealMatrix(new double[][]{{-Double.MAX_VALUE, -1.0, -1.0}, {0.0, 0.0, 1.0}});
        SimplexSolver.GoalType goalType = SimplexSolver.GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 1.0e-9;

        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        int initialIterations = solver.getIterationsCounter();
        solver.solvePhase1(tableau);

        int finalIterations = solver.getIterationsCounter();
        assertTrue(finalIterations > initialIterations);
    }

    @Test
    public void testOptimize() throws Exception {
        SimplexSolver solver = new SimplexSolver();
        double[][] f = {{1.0, 2.0, 3.0}, {-1.0, -1.0, -1.0}, {-0.5, -0.5, -0.5}, {-1.0, -1.0, 0.0}};
        RealMatrix constraints = new Array2DRowRealMatrix(new double[][]{{-Double.MAX_VALUE, -1.0, -1.0}, {0.0, 0.0, 1.0}});
        SimplexSolver.GoalType goalType = SimplexSolver.GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 1.0e-9;

        RealPointValuePair solution = solver.doOptimize();

        double[] expectedSolution = {-2.0 / 3.0, -1.0 / 2.0, 1.0};
        RealVector expectedObjective = new RealColumnVector(expectedSolution);
        RealVector expectedConstraints = new RealColumnVector(new double[]{-2.0, -0.5});

        RealVector solutionVector = solution.getPoint();
        RealVector objective = solution.getValue();

        assertArrayEquals(expectedSolution, solutionVector.toArray(), epsilon);
        assertTrue(MathUtils.equals(expectedObjective, objective, epsilon));
        assertTrue(MathUtils.equals(expectedConstraints, solution.getConstraints(), epsilon));
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterationsException() throws Exception {
        SimplexSolver solver = new SimplexSolver();
        double[][] f = {{1.0, 2.0, 3.0}, {-1.0, -1.0, -1.0}, {-0.5, -0.5, -0.5}, {-1.0, -1.0, 0.0}};
        RealMatrix constraints = new Array2DRowRealMatrix(new double[][]{{-Double.MAX_VALUE, -1.0, -1.0}, {0.0, 0.0, 1.0}});
        SimplexSolver.GoalType goalType = SimplexSolver.GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 1.0e-9;
        int maxIterations = 1;
        solver.setMaxIterations(maxIterations);
        RealPointValuePair solution = solver.doOptimize();
        fail("Expected MaxCountExceededException");
    }
}