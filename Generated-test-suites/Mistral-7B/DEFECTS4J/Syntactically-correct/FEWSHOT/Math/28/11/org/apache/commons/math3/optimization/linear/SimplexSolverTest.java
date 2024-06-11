package org.apache.commons.cli;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.SimpleTableau;
import org.apache.commons.math3.optimization.nonlinear.GoalType;
import org.apache.commons.math3.optimization.linear.LinearConstraintSet;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.nonlinear.linear.LinearConstraintSetFiller;

public class SimplexSolverTest {

    @Test
    public void testOptimize() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-2.0, -3.0}, new double[]{1.0, 1.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraintSetFiller filler = new LinearConstraintSetFiller();
        filler.addConstraint(constraints, "x >= 0", new double[]{1.0}, null);
        filler.addConstraint(constraints, "y >= 0", new double[]{1.0}, null);
        filler.addConstraint(constraints, "x + y <= 5", new double[]{1.0, 1.0}, null);

        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(new MaxEval(obj, constraints), GoalType.MINIMIZE);
        assertEquals(-5.0, solution.getValue(), 0.0);
        assertEquals(new double[]{1.5, 2.5}, solution.getPoint(), 0.0);
    }

    @Test
    public void testUnboundedSolution() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{5.0}, new double[]{0.0});
        LinearConstraintSet constraints = new LinearConstraintSet();

        SimplexSolver solver = new SimplexSolver();
        assertThrows(UnboundedSolutionException.class, () -> solver.optimize(new MaxEval(obj, constraints), GoalType.MINIMIZE));
    }

    @Test
    public void testMaxCountExceeded() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-2.0, -3.0}, new double[]{1.0, 1.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraint constraint = new LinearConstraint(1.0, new double[]{1.0}, null);
        constraints.addConstraint(constraint);

        SimplexSolver solver = new SimplexSolver();
        solver.setMaxIterations(0);
        assertThrows(MaxCountExceededException.class, () -> solver.optimize(new MaxEval(obj, constraints), GoalType.MINIMIZE));
    }

    @Test
    public void testNoFeasibleSolution() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-2.0, -3.0}, new double[]{1.0, 1.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraint constraint = new LinearConstraint(0.0, new double[]{1.0}, null);
        constraints.addConstraint(constraint);

        SimplexSolver solver = new SimplexSolver();
        assertThrows(NoFeasibleSolutionException.class, () -> solver.optimize(new MaxEval(obj, constraints), GoalType.MINIMIZE));
    }

}