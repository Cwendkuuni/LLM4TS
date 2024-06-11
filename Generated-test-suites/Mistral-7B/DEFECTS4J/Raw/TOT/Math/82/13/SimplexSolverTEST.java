package org.apache.commons.math.optimization.linear;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleLinearOptimizer;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testSimpleExample() {
        double[][] A = new double[][]{
            {1.0, 2.0, 3.0},
            {4.0, 1.0, -5.0},
            {-2.0, 1.0, 1.0}
        };
        RealMatrix matrixA = new Array2DRowRealMatrix(A);
        RealVector b = new ArrayRealVector(new double[]{3.0, -2.0, 1.0});
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new ArrayRealVector(new double[]{-1.0, 2.0}));
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new ArrayRealVector(new double[]{-Infinity, 0}), true),
            new LinearConstraint(new ArrayRealVector(new double[]{3.0, Infinity}), true)
        };
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = null;
        try {
            solution = solver.optimize(matrixA, b, objFunction, constraints, GoalType.MINIMIZE, false, 1000, 1.0e-6);
        } catch (MaxIterationsExceededException | UnboundedSolutionException | NoFeasibleSolutionException e) {
            Assert.fail("Solver should have found a solution");
        }
        Assert.assertNotNull(solution);
        Assert.assertEquals(Precision.round(solution.getValue(), 3), -3.0, 1e-6);
        RealVector x = solution.getPoint();
        Assert.assertEquals(Precision.round(x.getEntry(0), 3), 0.5, 1e-6);
        Assert.assertEquals(Precision.round(x.getEntry(1), 3), 1.5, 1e-6);
        Assert.assertEquals(Precision.round(x.getEntry(2), 3), 0.0, 1e-6);
    }

    @Test
    public void testUnboundedSolution() {
        double[][] A = new double[][]{
            {1.0, 2.0, 3.0},
            {4.0, 1.0, -5.0},
            {-2.0, 1.0, 1.0}
        };
        RealMatrix matrixA = new Array2DRowRealMatrix(A);
        RealVector b = new ArrayRealVector(new double[]{3.0, -2.0, 1.0});
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new ArrayRealVector(new double[]{-1.0, 2.0}));
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new ArrayRealVector(new double[]{-Infinity, 0}), true),
            new LinearConstraint(new ArrayRealVector(new double[]{3.0, Infinity}), true)
        };
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.optimize(matrixA, b, objFunction, constraints, GoalType.MINIMIZE, false, 1000, 1.0e-6);
            Assert.fail("Solver should have thrown UnboundedSolutionException");
        } catch (UnboundedSolutionException e) {
            // Expected
        }
    }

    @Test
    public void testNoFeasibleSolution() {
        double[][] A = new double[][]{
            {1.0, 2.0, 3.0},
            {4.0, 1.0, -5.0},
            {-2.0, 1.0, 1.0}
        };
        RealMatrix matrixA = new Array2DRowRealMatrix(A);
        RealVector b = new ArrayRealVector(new double[]{3.0, -2.0, -1.0});
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new ArrayRealVector(new double[]{-1.0, 2.0}));
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new ArrayRealVector(new double[]{-Infinity, 0}), true),
            new LinearConstraint(new ArrayRealVector(new double[]{3.0, Infinity}), true)
        };
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.optimize(matrixA, b, objFunction, constraints, GoalType.MINIMIZE, false, 1000, 1.0e-6);
            Assert.fail("Solver should have thrown NoFeasibleSolutionException");
        } catch (NoFeasibleSolutionException e) {
            // Expected
        }
    }

    @Test
    public void testMaxIterationsExceeded() {
        double[][] A = new double[][]{
            {1.0, 2.0, 3.0},
            {4.0, 1.0, -5.0},
            {-2.0, 1.0, 1.0}
        };
        RealMatrix matrixA = new Array2DRowRealMatrix(A);
        RealVector b = new ArrayRealVector(new double[]{3.0, -2.0, 1.0});
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new ArrayRealVector(new double[]{-1.0, 2.0}));
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new ArrayRealVector(new double[]{-Infinity, 0}), true),
            new LinearConstraint(new ArrayRealVector(new double[]{3.0, Infinity}), true)
        };
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.optimize(matrixA, b, objFunction, constraints, GoalType.MINIMIZE, false, 0, 1.0e-6);
            Assert.fail("Solver should have thrown MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }
}