package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimplexTableau;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize_EmptyConstraintSet_ReturnsNull() {
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.doOptimize();
        assertNull(result);
    }

    @Test
    public void testDoOptimize_EmptyFunctionSet_ThrowsException() {
        SimplexSolver solver = new SimplexSolver();
        Constraint[] constraints = new Constraint[0];
        UnboundedSolutionException exception = assertThrows(UnboundedSolutionException.class, () -> solver.doOptimize(new LinearFunction[0], constraints));
        assertEquals("Function set is empty", exception.getMessage());
    }

    @Test
    public void testDoOptimize_ConstraintViolation_ThrowsNoFeasibleSolutionException() {
        SimplexSolver solver = new SimplexSolver();
        LinearFunction function = new LinearFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{0.0, 4.0});
        Constraint constraint = new LinearConstraint(new double[][]{{1.0, -1.0}, {2.0, -2.0}}, 0.0);
        Constraint[] constraints = new Constraint[]{constraint};
        NoFeasibleSolutionException exception = assertThrows(NoFeasibleSolutionException.class, () -> solver.doOptimize(function, constraints));
        assertEquals("Constraint violation: " + constraint.toString(), exception.getMessage());
    }

    @Test
    public void testDoOptimize_UnboundedSolution_ThrowsUnboundedSolutionException() {
        SimplexSolver solver = new SimplexSolver();
        LinearFunction function = new LinearFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{0.0, 4.0});
        Constraint[] constraints = new Constraint[0];
        UnboundedSolutionException exception = assertThrows(UnboundedSolutionException.class, () -> solver.doOptimize(function, constraints));
        assertEquals("Function set has an unbounded solution", exception.getMessage());
    }

    @Test
    public void testDoOptimize_OptimalSolution() {
        SimplexSolver solver = new SimplexSolver();
        LinearFunction function = new LinearFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{0.0, 4.0});
        Constraint constraint1 = new LinearConstraint(new double[][]{{1.0, -1.0}, {2.0, -2.0}}, 0.0);
        Constraint constraint2 = new LinearConstraint(new double[][]{{1.0, 1.0}, {2.0, 1.0}}, 2.0);
        Constraint[] constraints = new Constraint[]{constraint1, constraint2};
        PointValuePair result = solver.doOptimize(function, constraints);
        assertEquals(2.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{1.0 / 3.0, 1.0 / 3.0}, result.getPoint(), 1e-6);
    }

    @Test
    public void testDoOptimize_OptimalSolutionWithGoalType_Minimization() {
        SimplexSolver solver = new SimplexSolver();
        LinearFunction function = new LinearFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{0.0, 4.0});
        Constraint constraint1 = new LinearConstraint(new double[][]{{1.0, -1.0}, {2.0, -2.0}}, 0.0);
        Constraint constraint2 = new LinearConstraint(new double[][]{{1.0, 1.0}, {2.0, 1.0}}, 2.0);
        Constraint[] constraints = new Constraint[]{constraint1, constraint2};
        PointValuePair result = solver.doOptimize(function, constraints, LinearObjectiveFunction.Minimization);
        assertEquals(2.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{1.0 / 3.0, 1.0 / 3.0}, result.getPoint(), 1e-6);
    }

    @Test
    public void testDoOptimize_OptimalSolutionWithGoalType_Maximization() {
        SimplexSolver solver = new SimplexSolver();
        LinearFunction function = new LinearFunction(new double[][]{{1.0, 2.0}, {-3.0, -4.0}}, new double[]{0.0, 4.0});
        Constraint constraint1 = new LinearConstraint(new double[][]{{1.0, -1.0}, {2.0, -2.0}}, 0.0);
        Constraint constraint2 = new LinearConstraint(new double[][]{{1.0, 1.0}, {2.0, 1.0}}, 2.0);
        Constraint[] constraints = new Constraint[]{constraint1, constraint2};
        PointValuePair result = solver.doOptimize(function, constraints, LinearObjectiveFunction.Maximization);
        assertEquals(-2.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{1.0 / 3.0, 1.0 / 3.0}, result.getPoint(), 1e-6);
    }

    @Test
    public void testDoOptimize_MaxIterationsExceeded() {
        SimplexSolver solver = new SimplexSolver(1);
        LinearFunction function = new LinearFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{0.0, 4.0});
        Constraint constraint1 = new LinearConstraint(new double[][]{{1.0, -1.0}, {2.0, -2.0}}, 0.0);
        Constraint constraint2 = new LinearConstraint(new double[][]{{1.0, 1.0}, {2.0, 1.0}}, 2.0);
        Constraint[] constraints = new Constraint[]{constraint1, constraint2};
        MaxCountExceededException exception = assertThrows(MaxCountExceededException.class, () -> solver.doOptimize(function, constraints));
        assertEquals("Maximum number of iterations (1) exceeded", exception.getMessage());
    }
}