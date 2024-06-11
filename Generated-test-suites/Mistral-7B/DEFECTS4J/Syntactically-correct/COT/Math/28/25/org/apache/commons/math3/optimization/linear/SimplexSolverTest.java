package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.MaxCountExceededException;
import org.apache.commons.math3.optimization.UnboundedSolutionException;
import org.apache.commons.math3.optimization.NoFeasibleSolutionException;
import org.apache.commons.math3.optim.PointValuePair.ComparisonType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;

/**
 * Test class for SimplexSolver
 */
public class SimplexSolverTest {

    /**
     * Test method for {@link SimplexSolver#SimplexSolver()}
     */
    @Test
    public void testSimplexSolverConstructorWithDefaults() {
        SimplexSolver solver = new SimplexSolver();
        assertEquals(1.0e-6, solver.getEpsilon(), 0);
        assertEquals(10, solver.getMaxUlps());
    }

    /**
     * Test method for {@link SimplexSolver#SimplexSolver(double, int)}
     */
    @Test
    public void testSimplexSolverConstructorWithCustomSettings() {
        double epsilon = 1.0e-5;
        int maxUlps = 5;
        SimplexSolver solver = new SimplexSolver(epsilon, maxUlps);
        assertEquals(epsilon, solver.getEpsilon(), 0);
        assertEquals(maxUlps, solver.getMaxUlps());
    }

    /**
     * Test method for {@link SimplexSolver#getPivotColumn(SimplexTableau)}
     * @throws UnboundedSolutionException
     */
    @Test
    public void testGetPivotColumn() throws UnboundedSolutionException {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[] { 1.0, 2.0 }, new double[] { 3.0, 4.0 });
        LinearConstraint[] constraints = new LinearConstraint[] { new LinearConstraint(new double[] { -1.0 }, ComparisonType.LEQ, 0.0),
                new LinearConstraint(new double[] { 1.0, 1.0 }, ComparisonType.GEQ, new double[] { 2.0, 2.0 }) };
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(objFunction, constraints, GoalType.MINIMIZE, false, 1.0e-6, 10);
        int expectedColumn = 0;
        int pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(expectedColumn, pivotColumn);
    }

    /**
     * Test method for {@link SimplexSolver#getPivotRow(SimplexTableau, int)}
     * @throws UnboundedSolutionException
     */
    @Test
    public void testGetPivotRow() throws UnboundedSolutionException {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[] { 1.0, 2.0 }, new double[] { 3.0, 4.0 });
        LinearConstraint[] constraints = new LinearConstraint[] { new LinearConstraint(new double[] { -1.0 }, ComparisonType.LEQ, 0.0),
                new LinearConstraint(new double[] { 1.0, 1.0 }, ComparisonType.GEQ, new double[] { 2.0, 2.0 }) };
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(objFunction, constraints, GoalType.MINIMIZE, false, 1.0e-6, 10);
        int pivotColumn = 0;
        int expectedRow = 0;
        int row = solver.getPivotRow(tableau, pivotColumn);
        assertEquals(expectedRow, row);
    }

    /**
     * Test method for {@link SimplexSolver#doIteration(SimplexTableau)}
     * @throws MaxCountExceededException
     * @throws UnboundedSolutionException
     */
    @Test
    public void testDoIteration()
        throws MaxCountExceededException, UnboundedSolutionException {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[] { 1.0, 2.0 }, new double[] { 3.0, 4.0 });
        LinearConstraint[] constraints = new LinearConstraint[] { new LinearConstraint(new double[] { -1.0 }, ComparisonType.LEQ, 0.0),
                new LinearConstraint(new double[] { 1.0, 1.0 }, ComparisonType.GEQ, new double[] { 2.0, 2.0 }) };
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(objFunction, constraints, GoalType.MINIMIZE, false, 1.0e-6, 10);
        int pivotColumn = 0;
        Integer pivotRow = solver.getPivotRow(tableau, pivotColumn);
        double pivotValue = solver.getEntry(pivotRow, pivotColumn);
        solver.doIteration(tableau);
        double newPivotValue = tableau.getEntry(pivotRow, pivotColumn);
        assertEquals(1.0 / pivotValue, newPivotValue, 0.0);
    }

    /**
     * Test method for {@link SimplexSolver#solvePhase1(SimplexTableau)}
     * @throws MaxCountExceededException
     * @throws UnboundedSolutionException
     * @throws NoFeasibleSolutionException
     */
    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[] { 1.0, 2.0 }, new double[] { 3.0, 4.0 });
        LinearConstraint[] constraints = new LinearConstraint[] { new LinearConstraint(new double[] { -1.0 }, ComparisonType.LEQ, 0.0),
                new LinearConstraint(new double[] { 1.0, 1.0 }, ComparisonType.GEQ, new double[] { 2.0, 2.0 }) };
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(objFunction, constraints, GoalType.MINIMIZE, false, 1.0e-6, 10);
        solver.solvePhase1(tableau);
        assertTrue(tableau.isFeasible());
        // Check that the sum of the objective function coefficients is zero
        double sum = 0.0;
        for (int i = 0; i < tableau.getWidth(); i++) {
            sum += tableau.getEntry(0, i);
        }
        assertEquals(0.0, sum, epsilon);
    }

    /**
     * Test method for {@link SimplexSolver#doOptimize()}
     * @throws MaxCountExceededException
     * @throws UnboundedSolutionException
     * @throws NoFeasibleSolutionException
     */
    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[] { 1.0, 2.0 }, new double[] { 3.0, 4.0 });
        LinearConstraint[] constraints = new LinearConstraint[] { new LinearConstraint(new double[] { -1.0 }, ComparisonType.LEQ, 0.0),
                new LinearConstraint(new double[] { 1.0, 1.0 }, ComparisonType.GEQ, new double[] { 2.0, 2.0 }) };
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.doOptimize();
        assertEquals(ComparisonType.LEQ, result.getConstraint().getComparisonType(), 0);
        assertEquals(PointValuePair.OptimizationType.MINIMUM, result.getType(), 0);
        assertEquals(1.0, result.getValue(), epsilon);
    }

    /**
     * Test method for edge case: {@link SimplexSolver#solvePhase1(SimplexTableau)} when no feasible solution exists
     * @throws MaxCountExceededException
     * @throws UnboundedSolutionException
     * @throws NoFeasibleSolutionException
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1NoFeasibleSolution()
        throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[] { 1.0, 2.0 }, new double[] { 3.0, 4.0 });
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(objFunction, constraints, GoalType.MINIMIZE, false, 1.0e-6, 10);
        solver.solvePhase1(tableau);
    }

    /**
     * Test method for edge case: {@link SimplexSolver#solvePhase1(SimplexTableau)} when maximal iteration count is exceeded
     * @throws MaxCountExceededException
     * @throws UnboundedSolutionException
     * @throws NoFeasibleSolutionException
     */
    @Test(expected = MaxCountExceededException.class)
    public void testSolvePhase1MaxIterationsExceeded()
        throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[] { 1.0, 2.0 }, new double[] { 3.0, 4.0 });
        LinearConstraint[] constraints = new LinearConstraint[] { new LinearConstraint(new double[] { -1.0 }, ComparisonType.LEQ, 0.0),
                new LinearConstraint(new double[] { 1.0, 1.0 }, ComparisonType.GEQ, new double[] { 2.0, 2.0 }) };
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(objFunction, constraints, GoalType.MINIMIZE, false, 1.0e-6, 1);
        solver.solvePhase1(tableau);
    }

    /**
     * Test method for edge case: {@link SimplexSolver#doIteration(SimplexTableau)} when pivot row is null
     * @throws MaxCountExceededException
     * @throws UnboundedSolutionException
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationPivotRowNull()
        throws MaxCountExceededException, UnboundedSolutionException {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[] { 1.0, 2.0 }, new double[] { 3.0, 4.0 });
        LinearConstraint[] constraints = new LinearConstraint[] { new LinearConstraint(new double[] { -1.0 }, ComparisonType.LEQ, 0.0),
                new LinearConstraint(new double[] { 1.0, 1.0 }, ComparisonType.GEQ, new double[] { 2.0, 2.0 }) };
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(objFunction, constraints, GoalType.MINIMIZE, false, 1.0e-6, 10);
        int pivotColumn = 0;
        Integer pivotRow = solver.getPivotRow(tableau, pivotColumn);
        if (pivotRow == null) {
            throw new UnboundedSolutionException();
        }
        solver.doIteration(tableau);
    }

}