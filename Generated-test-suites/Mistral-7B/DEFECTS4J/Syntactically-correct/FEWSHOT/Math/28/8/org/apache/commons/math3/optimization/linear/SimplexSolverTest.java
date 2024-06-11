package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleBoundingBox;
import org.apache.commons.math3.optimization.SimpleConstraint;
import org.apache.commons.math3.optimization.SimpleObjectiveFunction;
import org.apache.commons.math3.optimization.SimpleLinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.AbstractLinearOptimizer;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.util.Precision;

public class SimplexSolverTest {

    @Test
    public void testSolveSimpleProblem() {
        // create an optimization problem
        LinearObjectiveFunction objective = new SimpleLinearObjectiveFunction(-1.0, new double[]{-1.0, 2.0});
        List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
        constraints.add(new LinearConstraint(1.0, new double[]{1.0, 1.0}, 0.0));
        constraints.add(new LinearConstraint(1.0, new double[]{-2.0, 1.0}, 0.0));
        LinearConstraint[] arrayConstraint = new LinearConstraint[constraints.size()];
        arrayConstraint = constraints.toArray(arrayConstraint);

        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(new SimpleObjectiveFunction(objective), arrayConstraint);

        // check the solution
        assertEquals(0.0, solution.getPoint()[0], 1e-6);
        assertEquals(1.5, solution.getPoint()[1], 1e-6);
        assertEquals(-1.5, solution.getValue(), 1e-6);
    }

    @Test
    public void testSolveProblemWithNoFeasibleSolution() {
        // create an optimization problem that has no feasible solution
        LinearObjectiveFunction objective = new SimpleLinearObjectiveFunction(-1.0, new double[]{-1.0, 2.0});
        List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
        constraints.add(new LinearConstraint(1.0, new double[]{1.0, 1.0}, 0.0));
        constraints.add(new LinearConstraint(-1.0, new double[]{1.0, 1.0}, 0.0));
        LinearConstraint[] arrayConstraint = new LinearConstraint[constraints.size()];
        arrayConstraint = constraints.toArray(arrayConstraint);

        SimplexSolver solver = new SimplexSolver();
        assertThrows(NoFeasibleSolutionException.class, () -> solver.optimize(new SimpleObjectiveFunction(objective), arrayConstraint));
    }

    @Test
    public void testSolveProblemWithUnboundedSolution() {
        // create an optimization problem that has an unbounded solution
        LinearObjectiveFunction objective = new SimpleLinearObjectiveFunction(-1.0, new double[]{-1.0, 2.0});
        List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
        constraints.add(new LinearConstraint(0.0, new double[]{1.0, 1.0}, Double.POSITIVE_INFINITY));
        LinearConstraint[] arrayConstraint = new LinearConstraint[constraints.size()];
        arrayConstraint = constraints.toArray(arrayConstraint);

        SimplexSolver solver = new SimplexSolver();
        assertThrows(UnboundedSolutionException.class, () -> solver.optimize(new SimpleObjectiveFunction(objective), arrayConstraint));
    }

    @Test
    public void testSolveProblemWithMaxCountExceeded() {
        // create an optimization problem that cannot be solved
        SimpleObjectiveFunction objective = new SimpleObjectiveFunction(new SimpleLinearObjectiveFunction(new double[]{-1.0, -1.0}, 0.0));
        LinearConstraint[] constraints = new LinearConstraint[]{new LinearConstraint(1.0, new double[]{1.0, 1.0}, 0.0), new LinearConstraint(1.0, new double[]{-1.0, -1.0}, Double.POSITIVE_INFINITY)};

        SimplexSolver solver = new SimplexSolver();
        assertThrows(MaxCountExceededException.class, () -> solver.optimize(objective, constraints));
    }

    @Test
    public void testSolveProblemWithEmptyConstraints() {
        // create an optimization problem with no constraints
        SimpleObjectiveFunction objective = new SimpleObjectiveFunction(new SimpleLinearObjectiveFunction(new double[]{-1.0, -1.0}, 0.0));
        LinearConstraint[] constraints = new LinearConstraint[0];

        SimplexSolver solver = new SimplexSolver();
        assertThrows(IllegalArgumentException.class, () -> solver.optimize(objective, constraints));
    }

    @Test
    public void testSolveProblemWithDummyConstraints() {
        // create an optimization problem with dummy constraints
        SimpleObjectiveFunction objective = new SimpleObjectiveFunction(new SimpleLinearObjectiveFunction(new double[]{-1.0, -1.0}, 0.0));
        LinearConstraint[] constraints = new LinearConstraint[]{new LinearConstraint(1.0, new double[]{1.0, 1.0}, 0.0), new LinearConstraint(1.0, new double[]{1.0, 1.0}, 0.0)};

        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(objective, constraints);
        assertEquals(0.0, solution.getPoint()[0], 1e-6);
        assertEquals(0.0, solution.getPoint()[1], 1e-6);
        assertEquals(0.0, solution.getValue(), 1e-6);
    }

    @Test
    public void testSolveProblemWithInfeasibleConstraints() {
        // create an optimization problem with infeasible constraints
        SimpleObjectiveFunction objective = new SimpleObjectiveFunction(new SimpleLinearObjectiveFunction(new double[]{-1.0, -1.0}, 0.0));
        LinearConstraint[] constraints = new LinearConstraint[]{new LinearConstraint(1.0, new double[]{1.0, 1.0}, -1.0), new LinearConstraint(1.0, new double[]{1.0, 1.0}, 1.0)};

        SimplexSolver solver = new SimplexSolver();
        assertThrows(IllegalArgumentException.class, () -> solver.optimize(objective, constraints));
    }

    @Test
    public void testSolveProblemWithBoundingBox() {
        // create an optimization problem with a bounding box
        SimpleObjectiveFunction objective = new SimpleObjectiveFunction(new SimpleLinearObjectiveFunction(new double[]{1.0, 1.0}, 0.0));
        LinearConstraint[] constraints = new LinearConstraint[]{new LinearConstraint(1.0, new double[]{1.0, 1.0}, 0.0), new LinearConstraint(1.0, new double[]{1.0, 1.0}, 2.0)};
        SimpleBoundingBox boundingBox = new SimpleBoundingBox(new double[]{0.0, 0.0}, new double[]{3.0, 3.0});

        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(objective, constraints, boundingBox);
        assertArrayEquals(new double[]{3.0, 3.0}, solution.getPoint(), 1e-6);
        assertEquals(3.0 * 3.0, solution.getValue(), 1e-6);
    }

    @Test
    public void testSolveProblemWithNonNegativeConstraints() {
        // create an optimization problem with non-negative constraints
        SimpleObjectiveFunction objective = new SimpleObjectiveFunction(new SimpleLinearObjectiveFunction(new double[]{-1.0, -1.0}, 0.0));
        LinearConstraint[] constraints = new LinearConstraint[]{new LinearConstraint(1.0, new double[]{1.0, 1.0}, 0.0), new LinearConstraint(1.0, new double[]{1.0, 1.0}, 0.0), new LinearConstraint(-1.0, new double[]{1.0, 1.0}, 0.0)};

        SimplexSolver solver = new SimplexSolver(1e-6, 10);
        PointValuePair solution = solver.optimize(objective, constraints);
        assertEquals(0.0, solution.getPoint()[0], 1e-6);
        assertEquals(0.0, solution.getPoint()[1], 1e-6);
        assertEquals(0.0, solution.getValue(), 1e-6);
    }

    @Test
    public void testSolveProblemWithMixedConstraints() {
        // create an optimization problem with mixed constraints
        SimpleObjectiveFunction objective = new SimpleObjectiveFunction(new SimpleLinearObjectiveFunction(new double[]{-1.0, -1.0}, 0.0));
        LinearConstraint[] constraints = new LinearConstraint[]{new LinearConstraint(1.0, new double[]{1.0, 1.0}, 0.0), new LinearConstraint(-1.0, new double[]{1.0, 1.0}, 0.0), new LinearConstraint(1.0, new double[]{1.0, 1.0}, 1.0)};

        SimplexSolver solver = new SimplexSolver(1e-6, 10);
        PointValuePair solution = solver.optimize(objective, constraints);
        assertEquals(0.0, solution.getPoint()[0], 1e-6);
        assertEquals(1.0, solution.getPoint()[1], 1e-6);
        assertEquals(0.0, solution.getValue(), 1e-6);
    }

    @Test
    public void testSolveProblemWithObjectiveFunctionZero() {
        // create an optimization problem with objective function equal to zero
        SimpleObjectiveFunction objective = new SimpleObjectiveFunction(new SimpleLinearObjectiveFunction(new double[]{-1.0, -1.0}, 0.0));
        LinearConstraint[] constraints = new LinearConstraint[]{new LinearConstraint(1.0, new double[]{1.0, 1.0}, 0.0), new LinearConstraint(1.0, new double[]{1.0, 1.0}, 0.0)};

        SimplexSolver solver = new SimplexSolver(1e-6, 10);
        PointValuePair solution = solver.optimize(objective, constraints);
        assertEquals(0.0, solution.getPoint()[0], 1e-6);
        assertEquals(0.0, solution.getPoint()[1], 1e-6);
        assertEquals(0.0, solution.getValue(), 1e-6);
    }

    @Test
    public void testSolveProblemWithNoFeasibleSolutionException() {
        // create an optimization problem with no feasible solution exception
        SimpleObjectiveFunction objective = new SimpleObjectiveFunction(new SimpleLinearObjectiveFunction(new double[]{-1.0, -1.0}, 0.0));
        LinearConstraint[] constraints = new LinearConstraint[]{new LinearConstraint(-1.0, new double[]{1.0, 1.0}, 0.0)};

        SimplexSolver solver = new SimplexSolver(1e-6, 10);
        assertThrows(NoFeasibleSolutionException.class, () -> solver.optimize(objective, constraints));
    }

    @Test
    public void testSolveProblemWithMaxIterations() {
        // create an optimization problem that cannot be solved with the given number of iterations
        SimpleObjectiveFunction objective = new SimpleObjectiveFunction(new SimpleLinearObjectiveFunction(new double[]{-1.0, -1.0}, 0.0));
        LinearConstraint[] constraints = new LinearConstraint[]{new LinearConstraint(1.0, new double[]{1.0, 1.0}, 0.0), new LinearConstraint(1.0, new double[]{1.0, 1.0}, 2.0)};

        SimplexSolver solver = new SimplexSolver(1e-6, 1);
        assertThrows(MaxCountExceededException.class, () -> solver.optimize(objective, constraints));
    }

    @Test
    public void testSolveProblemWithEpsilon() {
        // create an optimization problem with a very small epsilon
        SimpleObjectiveFunction objective = new SimpleObjectiveFunction(new SimpleLinearObjectiveFunction(new double[]{-1.0, -1.0}, 0.0));
        LinearConstraint[] constraints = new LinearConstraint[]{new LinearConstraint(1.0, new double[]{1.0, 1.0}, 0.0), new LinearConstraint(1.0, new double[]{1.0, 1.0}, 2.0)};

        SimplexSolver solver = new SimplexSolver(1e-10, 10);
        PointValuePair solution = solver.optimize(objective, constraints);
        assertArrayEquals(new double[]{2.0, 2.0}, solution.getPoint(), 1e-10);
        assertEquals(4.0 * 4.0, solution.getValue(), 1e-10);
    }

    @Test
    public void testSolveProblemWithUlps() {
        // create an optimization problem with a very small ulps
        SimpleObjectiveFunction objective = new SimpleObjectiveFunction(new SimpleLinearObjectiveFunction(new double[]{-1.0, -1.0}, 0.0));
        LinearConstraint[] constraints = new LinearConstraint[]{new LinearConstraint(1.0, new double[]{1.0, 1.0}, 0.0), new LinearConstraint(1.0, new double[]{1.0, 1.0}, 2.0)};

        SimplexSolver solver = new SimplexSolver(1e-6, 10, 1);
        PointValuePair solution = solver.optimize(objective, constraints);
        assertArrayEquals(new double[]{2.0, 2.0}, solution.getPoint(), 1e-6);
        assertEquals(4.0 * 4.0, solution.getValue(), 1e-6);
    }

    @Test
    public void testSolveProblemWithNullObjectiveFunction() {
        // create an optimization problem with a null objective function
        LinearConstraint[] constraints = new LinearConstraint[]{new LinearConstraint(1.0, new double[]{1.0, 1.0}, 0.0), new LinearConstraint(1.0, new double[]{1.0, 1.0}, 2.0)};

        assertThrows(NullPointerException.class, () -> new SimplexSolver().optimize(null, constraints));
    }

    @Test
    public void testSolveProblemWithNullConstraints() {
        // create an optimization problem with null constraints
        SimpleObjectiveFunction objective = new SimpleObjectiveFunction(new SimpleLinearObjectiveFunction(new double[]{-1.0, -1.0}, 0.0));

        assertThrows(NullPointerException.class, () -> new SimplexSolver().optimize(objective, null));
    }
}