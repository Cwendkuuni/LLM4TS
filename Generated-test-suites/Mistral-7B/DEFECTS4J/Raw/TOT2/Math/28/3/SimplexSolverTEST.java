import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.simple.AmplitudeSineFunction;
import org.apache.commons.math3.optim.nonlinear.simple.RidgeFunction;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testEmptyConstraintSet() {
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearObjectiveFunction objective = new RidgeFunction(1.0);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(objective, constraints);
        assertNull(solution);
    }

    @Test
    public void testSimpleConstraintSet() {
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(1, 1, 3));
        LinearObjectiveFunction objective = new AmplitudeSineFunction(Math.PI / 2, 1.0);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(objective, constraints);
        assertEquals(2.0, solution.getPoint().getComponent(0), epsilon);
        assertEquals(0.0, solution.getPoint().getComponent(1), epsilon);
    }

    @Test
    public void testNegativeConstraintSet() {
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(-1, 1, 3));
        LinearObjectiveFunction objective = new AmplitudeSineFunction(Math.PI / 2, 1.0);
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.optimize(objective, constraints);
            fail();
        } catch (UnboundedSolutionException expected) {
            // expected
        }
    }

    @Test
    public void testInvalidConstraintSet() {
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(-1, 1, 3));
        LinearObjectiveFunction objective = new AmplitudeSineFunction(Math.PI / 2, 1.0);
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.optimize(objective, constraints);
            fail();
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    @Test
    public void testMaxIterations() {
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(1, 1, 3));
        LinearObjectiveFunction objective = new AmplitudeSineFunction(Math.PI / 2, 1.0);
        SimplexSolver solver = new SimplexSolver(1, 0);
        try {
            solver.optimize(objective, constraints);
            fail();
        } catch (MaxCountExceededException expected) {
            // expected
        }
    }

    @Test
    public void testOptimalSolution() {
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(1, 1, 3));
        LinearObjectiveFunction objective = new RidgeFunction(1.0);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(objective, constraints);
        assertEquals(0.0, solution.getValue(), epsilon);
        assertEquals(1.0 / 3.0, solution.getPoint().getComponent(0), epsilon);
        assertEquals(1.0 / 3.0, solution.getPoint().getComponent(1), epsilon);
    }

    @Test
    public void testFeasibleSolution() {
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(1, 1, 3));
        LinearObjectiveFunction objective = new RidgeFunction(2.0);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(objective, constraints);
        assertEquals(2.0, solution.getValue(), epsilon);
        assertEquals(2.0 / 3.0, solution.getPoint().getComponent(0), epsilon);
        assertEquals(2.0 / 3.0, solution.getPoint().getComponent(1), epsilon);
    }

    @Test
    public void testUnboundedSolution() {
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearObjectiveFunction objective = new RidgeFunction(Double.POSITIVE_INFINITY);
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.optimize(objective, constraints);
            fail();
        } catch (UnboundedSolutionException expected) {
            // expected
        }
    }
}