package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealPointValuePair;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testSolvePositiveLinearProblem() {
        SimplexSolver solver = new SimplexSolver(1.0e-8);
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {1.0, 1.0, 2.0, 3.0, 1.0},
                {1.0, 2.0, 1.0, 1.0, 1.0},
                {0.0, 1.0, 0.0, 1.0, 2.0},
                {0.0, 0.0, 1.0, 0.0, 1.0},
                {0.0, 0.0, 0.0, 1.0, -1.0},
                {1.0, 0.0, 0.0, 0.0, 4.0}
        });
        RealVector b = new ArrayRealVector(new double[] {-3.0, -2.0, -2.0, 0.0, 0.0, 1.0});
        RealPointValuePair solution = solver.optimize(matrix, b, GoalType.MINIMIZE);
        assertArrayEquals(new double[] {-2.0 / 3.0, -1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 0.0},
                solution.getPoint().toArray(), 1e-8);
        assertEquals(0.0, solution.getValue(), 1e-8);
    }

    @Test
    public void testSolveNegativeLinearProblem() {
        SimplexSolver solver = new SimplexSolver(1.0e-8);
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {-1.0, -1.0, 2.0, 3.0, 1.0},
                {-1.0, 2.0, 1.0, 1.0, 1.0},
                {0.0, 1.0, 0.0, 1.0, 2.0},
                {0.0, 0.0, 1.0, 0.0, 1.0},
                {0.0, 0.0, 0.0, 1.0, -1.0},
                {1.0, 0.0, 0.0, 0.0, 4.0}
        });
        RealVector b = new ArrayRealVector(new double[] {3.0, 2.0, 2.0, 0.0, 0.0, 1.0});
        RealPointValuePair solution = solver.optimize(matrix, b, GoalType.MAXIMIZE);
        assertArrayEquals(new double[] {-2.0 / 3.0, -1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 0.0},
                solution.getPoint().toArray(), 1e-8);
        assertEquals(-4.0, solution.getValue(), 1e-8);
    }

    @Test
    public void testUnboundedSolution() {
        SimplexSolver solver = new SimplexSolver(1.0e-8);
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {1.0, 1.0, 1.0, 1.0, 1.0},
                {-1.0, 0.0, 0.0, 1.0, 1.0},
                {0.0, -1.0, 0.0, 1.0, 1.0},
                {0.0, 0.0, -1.0, 1.0, 1.0},
                {0.0, 0.0, 0.0, 1.0, 1.0}
        });
        RealVector b = new ArrayRealVector(new double[] {1.0, 1.0, 1.0, 1.0, 1.0});
        try {
            solver.optimize(matrix, b, GoalType.MINIMIZE);
            fail("Expected UnboundedSolutionException");
        } catch (UnboundedSolutionException e) {
            // expected
        }
    }

    @Test
    public void testNoFeasibleSolution() {
        SimplexSolver solver = new SimplexSolver(1.0e-8);
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {1.0, 1.0, 1.0, 1.0, 1.0},
                {-1.0, 1.0, 0.0, 1.0, 1.0},
                {0.0, -1.0, 1.0, 1.0, 1.0},
                {0.0, 0.0, -1.0, 1.0, 1.0}
        });
        RealVector b = new ArrayRealVector(new double[] {1.0, 1.0, 0.0, 0.0, 1.0});
        try {
            solver.optimize(matrix, b, GoalType.MINIMIZE);
            fail("Expected NoFeasibleSolutionException");
        } catch (NoFeasibleSolutionException e) {
            // expected
        }
    }

    @Test
    public void testMaxIterations() {
        SimplexSolver solver = new SimplexSolver(1.0e-8, 1);
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {1.0, 1.0, 1.0, 1.0, 1.0},
                {-1.0, 1.0, 0.0, 1.0, 1.0},
                {0.0, -1.0, 0.0, 1.0, 1.0},
                {0.0, 0.0, -1.0, 1.0, 1.0},
                {0.0, 0.0, 0.0, 1.0, 1.0}
        });
        RealVector b = new ArrayRealVector(new double[] {1.0, 1.0, 1.0, 1.0, 1.0});
        try {
            solver.optimize(matrix, b, GoalType.MINIMIZE);
            fail("Expected OptimizationException with MaxCountExceededException");
        } catch (OptimizationException e) {
            assertTrue(e.getCause() instanceof MaxCountExceededException);
        }
    }
}