package org.apache.commons.math.optimization.linear;

import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class SimplexSolverTest {

    private SimplexSolver solver;

    @BeforeEach
    public void setUp() {
        solver = new SimplexSolver();
    }

    @Test
    public void testOptimizeSimpleExample() {
        double[][] f = new double[][]{
            {-1.0, 2.0, -1.0},
            {-1.0, -1.0, 1.0},
            {0.0, 1.0, 0.0}
        };
        double[][] constraints = new double[][]{
            {1.0, 1.0, 1.0},
            {1.0, 1.0, -1.0}
        };
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1e-6);

        try {
            PointValuePair solution = solver.doOptimize();
            Assertions.assertEquals(-3.0, solution.getValue(), 1e-6);
            Assertions.assertArrayEquals(new double[]{1.0 / 3.0, 1.0 / 3.0}, solution.getPoint(), 1e-6);
        } catch (OptimizationException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testOptimizeUnboundedSolution() {
        double[][] f = new double[][]{
            {0.0, -1.0},
            {0.0, -1.0},
            {0.0, 0.0}
        };
        double[][] constraints = new double[][]{
            {1.0, 1.0},
            {1.0, 1.0}
        };

        Executable test = () -> solver.doOptimize();
        Assertions.assertThrows(UnboundedSolutionException.class, test);
    }

    @Test
    public void testOptimizeNoFeasibleSolution() {
        double[][] f = new double[][]{
            {0.0, 1.0},
            {0.0, 1.0},
            {1.0, 0.0}
        };
        double[][] constraints = new double[][]{
            {1.0, 1.0},
            {1.0, -1.0}
        };

        Executable test = () -> solver.doOptimize();
        Assertions.assertThrows(NoFeasibleSolutionException.class, test);
    }

    @Test
    public void testOptimizeMaxIterations() {
        double[][] f = new double[][]{
            {-1.0, 2.0, -1.0},
            {-1.0, -1.0, 1.0},
            {0.0, 1.0, 0.0}
        };
        double[][] constraints = new double[][]{
            {1.0, 1.0, 1.0},
            {1.0, 1.0, -1.0}
        };

        try {
            solver = new SimplexSolver(1e-3);
            PointValuePair solution = solver.doOptimize();
            Assertions.assertEquals(-3.0, solution.getValue(), 1e-3);
            Assertions.assertArrayEquals(new double[]{1.0 / 3.0, 1.0 / 3.0}, solution.getPoint(), 1e-3);

            int maxIterations = 1;
            solver = new SimplexSolver(1e-6) {
                @Override
                public int getMaxIterations() {
                    return maxIterations;
                }
            };

            Executable test = () -> solver.doOptimize();
            Assertions.assertThrows(MaxEvalException.class, test);
        } catch (OptimizationException e) {
            Assertions.fail(e.getMessage());
        }
    }
}