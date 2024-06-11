package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.SimplexTableau;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-3.0, 2.0}, 0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1.0}, 0.0));
        constraints.addConstraint(new LinearConstraint(new double[]{-1.0, 1.0}, 1.0));
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = solver.doOptimize();
        assertEquals(new PointValuePair(new double[]{1.0 / 3.0, 2.0 / 3.0}, -3.0 / 3.0), solution, 0.0001);
    }

    @Test
    public void testSolvePhase1() throws OptimizationException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-3.0, 2.0}, 0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1.0}, 0.0));
        constraints.addConstraint(new LinearConstraint(new double[]{-1.0, 1.0}, 1.0));
        SimplexTableau tableau = new SimplexTableau(f.getValue(), constraints.getConstraints(), null, null, 1e-6);
        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(tableau);
        assertTrue(Precision.abs(tableau.getEntry(tableau.getHeight() - 1, tableau.getWidth() - 1), 1e-6) < 1e-6);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1NoFeasibleSolution() throws OptimizationException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-3.0, 2.0}, 0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1.0}, 0.0));
        constraints.addConstraint(new LinearConstraint(new double[]{1.0}, 1.0));
        SimplexTableau tableau = new SimplexTableau(f.getValue(), constraints.getConstraints(), null, null, 1e-6);
        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(tableau);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testSolvePhase1UnboundedSolution() throws OptimizationException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-3.0, 2.0}, 0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1.0}, Double.NEGATIVE_INFINITY));
        constraints.addConstraint(new LinearConstraint(new double[]{1.0}, Double.POSITIVE_INFINITY));
        SimplexTableau tableau = new SimplexTableau(f.getValue(), constraints.getConstraints(), null, null, 1e-6);
        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(tableau);
    }
}