package org.apache.commons.math.optimization.linear;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testEmptyProblem() {
        SimplexSolver solver = new SimplexSolver(1e-6);
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[0], new double[0]);
        LinearConstraint[] constraints = new LinearConstraint[0];
        LinearObjectiveType goalType = LinearObjectiveType.MINIMIZE;
        boolean restrictToNonNegative = false;
        try {
            RealPointValuePair result = solver.doOptimize();
            fail("Expected OptimizationException for empty problem.");
        } catch (OptimizationException e) {
            // expected
        }
    }

    @Test
    public void testSimpleProblem() {
        SimplexSolver solver = new SimplexSolver(1e-6);
        double[] coefficients = new double[]{-2.0, -1.0, 3.0, 2.0};
        double[] constants = new double[]{-4.0, 0.0, 3.0};
        LinearObjectiveFunction f = new LinearObjectiveFunction(coefficients, constants);
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.GREATER_EQUAL, 0.0),
            new LinearConstraint(new double[]{3.0, 2.0}, Relationship.GREATER_EQUAL, 0.0)};
        LinearObjectiveType goalType = LinearObjectiveType.MINIMIZE;
        boolean restrictToNonNegative = false;

        try {
            RealPointValuePair result = solver.doOptimize();
            double[] expectedSolution = new double[]{1.0 / 3.0, 2.0 / 3.0};
            assertArrayEquals("Solution should be [1/3, 2/3]", expectedSolution, result.getPoint(), 1e-6);
            assertEquals("Value of objective function should be -3.0", -3.0, result.getValue(), 1e-6);
        } catch (OptimizationException e) {
            fail("Unexpected OptimizationException: " + e.getMessage());
        }
    }

    @Test
    public void testUnboundedProblem() {
        SimplexSolver solver = new SimplexSolver(1e-6);
        double[] coefficients = new double[]{0.0, 1.0, -1.0};
        double[] constants = new double[]{0.0};
        LinearObjectiveFunction f = new LinearObjectiveFunction(coefficients, constants);
        LinearConstraint[] constraints = new LinearConstraint[0];
        LinearObjectiveType goalType = LinearObjectiveType.MINIMIZE;
        boolean restrictToNonNegative = false;

        try {
            RealPointValuePair result = solver.doOptimize();
            fail("Expected UnboundedSolutionException for unbounded problem.");
        } catch (UnboundedSolutionException e) {
            // expected
        }
    }

    @Test
    public void testNoFeasibleSolution() {
        SimplexSolver solver = new SimplexSolver(1e-6);
        double[] coefficients = new double[]{-2.0, -1.0, 3.0, 2.0, 5.0};
        double[] constants = new double[]{-4.0, 0.0, 3.0, 10.0};
        LinearObjectiveFunction f = new LinearObjectiveFunction(coefficients, constants);
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.GREATER_EQUAL, 0.0),
            new LinearConstraint(new double[]{3.0, 2.0}, Relationship.GREATER_EQUAL, 0.0)};
        LinearObjectiveType goalType = LinearObjectiveType.MINIMIZE;
        boolean restrictToNonNegative = false;

        try {
            RealPointValuePair result = solver.doOptimize();
            fail("Expected NoFeasibleSolutionException for infeasible problem.");
        } catch (NoFeasibleSolutionException e) {
            // expected
        }
    }
}