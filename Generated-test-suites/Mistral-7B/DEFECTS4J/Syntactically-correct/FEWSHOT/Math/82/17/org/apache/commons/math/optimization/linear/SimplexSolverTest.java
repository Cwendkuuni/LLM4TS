package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleTableau;
import org.apache.commons.math3.optim.point.PointValuePairFactory;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.util.MathUtils;

import java.util.Arrays;

public class SimplexSolverTest {

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() throws Exception {
        SimplexSolver solver = new SimplexSolver();

        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, 0.0);
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(new double[]{-1.0, 1.0}, 0.0, ConstraintDirection.LEQ),
                new LinearConstraint(new double[]{1.0, 1.0}, 0.0, ConstraintDirection.LEQ)
        };

        PointValuePair result = solver.optimize(objFunc, constraints);
        fail("Expected UnboundedSolutionException");
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() throws Exception {
        SimplexSolver solver = new SimplexSolver();

        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, 0.0);
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(new double[]{-1.0, 1.0}, 0.0, ConstraintDirection.LT)
        };

        PointValuePair result = solver.optimize(objFunc, constraints);
        fail("Expected NoFeasibleSolutionException");
    }

    @Test
    public void testOptimize() throws Exception {
        SimplexSolver solver = new SimplexSolver();

        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{1.0, -1.0}, 1.0);
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(new double[]{1.0, 1.0}, 2.0, ConstraintDirection.LEQ),
                new LinearConstraint(new double[]{-1.0, 1.0}, 1.0, ConstraintDirection.LEQ),
                new LinearConstraint(new double[]{1.0, -1.0}, 1.0, ConstraintDirection.LEQ)
        };

        PointValuePair result = solver.optimize(objFunc, constraints);

        SimplexTableau expectedTableau = new SimplexTableau(
                new double[][]{{1.0, 1.0, 2.0, 1.0, 1.0, 1.0},
                        {0.0, -1.0, 1.0, 1.0, 0.0, 0.0},
                        {0.0, 0.0, 0.0, 0.0, 1.0, 1.0}},
                0.0,
                new int[]{0, 3, 2, 4, 1, 1},
                new int[]{3, 3, 3, 0, 0, 0},
                new int[]{0, 1, 0, 1, 0, 0},
                new int[]{1, 0, 0, 0, 0, 0},
                new int[]{0, 0, 1, 0, 0, 0},
                new int[]{1, 0, 0, 0, 0, 0},
                new int[]{-1, 1, 0, 0, 0, 0},
                new int[]{0, 0, 0, 0, 0, 1},
                new int[]{0, 0, 0, 0, 1, 0},
                1.0e-6,
                new String[]{"x1", "x2", "s1", "s2", "s3", "s4"},
                new String[]{"x1", "x2", "s1", "s2", "s3", "s4"},
                new int[]{0, 2, 3, 3, 4, 4},
                new int[]{3, 3, 3, 0, 0, 0});

        SimplexTableau actualTableau = (SimplexTableau) result.getPoint();

        assertEquals(expectedTableau.getHeight(), actualTableau.getHeight());
        assertEquals(expectedTableau.getWidth(), actualTableau.getWidth());
        assertEquals(expectedTableau.getNumObjectiveFunctions(), actualTableau.getNumObjectiveFunctions());
        assertEquals(expectedTableau.getNumConstraints(), actualTableau.getNumConstraints());
        assertEquals(expectedTableau.getNumArtificialVariables(), actualTableau.getNumArtificialVariables());

        for (int i = 0; i < expectedTableau.getHeight(); i++) {
            assertArrayEquals(expectedTableau.getRow(i), actualTableau.getRow(i), 1e-6);
        }

        for (int i = 0; i < expectedTableau.getWidth(); i++) {
            assertArrayEquals(expectedTableau.getColumn(i), actualTableau.getColumn(i), 1e-6);
        }

        assertEquals(expectedTableau.getRhsOffset(), actualTableau.getRhsOffset(), 1e-6);
        assertEquals(expectedTableau.getRhsSize(), actualTableau.getRhsSize());

        PointValuePair expectedSolution = PointValuePairFactory.getInstance().createPointValuePair(
                new double[]{1.0 / 3.0, 1.0 / 3.0}, 4.0 / 3.0);

        assertEquals(expectedSolution.getPoint(), actualTableau.getSolution().getPoint());
        assertEquals(expectedSolution.getValue(), actualTableau.getSolution().getValue(), 1e-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws Exception {
        SimplexSolver solver = new SimplexSolver(1e-12);

        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{1.0, -1.0}, 1.0);
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(new double[]{1.0, 1.0}, 2.0, ConstraintDirection.LEQ),
                new LinearConstraint(new double[]{-1.0, 1.0}, 1.0, ConstraintDirection.LEQ),
                new LinearConstraint(new double[]{1.0, -1.0}, 1.0, ConstraintDirection.LEQ)
        };

        try {
            solver.optimize(objFunc, constraints);
            fail("Expected MaxIterationsExceededException");
        } catch (OptimizationException e) {
            if (e.getCause() instanceof MaxIterationsExceededException) {
                throw e;
            }
            throw e;
        }
    }
}