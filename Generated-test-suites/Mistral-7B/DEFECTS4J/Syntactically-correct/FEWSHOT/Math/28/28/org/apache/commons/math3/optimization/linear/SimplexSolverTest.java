package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.optimization.linear.linearConstraint.Constraint;
import org.apache.commons.math3.optimization.linear.linearObjective.LinearObjective;
import org.apache.commons.math3.optimization.linear.linearObjective.LinearlyConstrainedMinimizationProblem;
import org.apache.commons.math3.optimization.linear.linearObjective.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.linearObjective.LinearObjectiveFunction.MIN;
import org.apache.commons.math3.optimization.linear.linearObjective.LinearObjectiveFunction.ONE;
import org.apache.commons.math3.optimization.linear.linearObjective.LinearObjectiveFunction.ZERO;
import org.apache.commons.math3.optimization.linear.linearObjective.LinearObjectiveFunction.VariableType;
import org.apache.commons.math3.optimization.linear.simplestableau.SimplexTableauUtil;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize() throws Exception {
        LinearObjectiveFunction f1 = new LinearObjectiveFunction(MIN, new double[]{-1.0, 1.0}, new double[]{1.0, 1.0});
        LinearObjectiveFunction f2 = new LinearObjectiveFunction(MIN, new double[]{1.0, -1.0}, new double[]{1.0, 1.0});
        LinearObjectiveFunction[] functions = new LinearObjectiveFunction[]{f1, f2};
        LinearObjectiveFunction g = new LinearObjectiveFunction(ONE, new double[]{1.0}, new double[]{0.0});
        Constraint[] constraints = new Constraint[]{new Constraint(1.0, 0.0, 2.0), new Constraint(2.0, 0.0, 2.0)};
        SimplexSolver solver = new SimplexSolver(1e-6, 10);

        LinearlyConstrainedMinimizationProblem problem = new LinearlyConstrainedMinimizationProblem(functions, constraints, g, VariableType.CONTINUOUS);
        PointValuePair result = solver.doOptimize();

        assertArrayEquals(new double[]{1.0 / 3.0, 2.0 / 3.0}, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testDoOptimizeMaxIterations() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(MIN, new double[]{1.0}, new double[]{1.0});
        LinearObjectiveFunction g = new LinearObjectiveFunction(ONE, new double[]{1.0}, new double[]{0.0});
        Constraint[] constraints = new Constraint[0];
        SimplexSolver solver = new SimplexSolver(1e-6, 1);

        LinearlyConstrainedMinimizationProblem problem = new LinearlyConstrainedMinimizationProblem(new LinearObjectiveFunction[]{f}, constraints, g, VariableType.CONTINUOUS);
        solver.doOptimize();
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimizeUnbounded() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(MIN, new double[]{1.0}, new double[]{1.0});
        LinearObjectiveFunction g = new LinearObjectiveFunction(ZERO, new double[]{1.0}, new double[]{0.0});
        Constraint[] constraints = new Constraint[0];
        SimplexSolver solver = new SimplexSolver(1e-6, 10);

        LinearlyConstrainedMinimizationProblem problem = new LinearlyConstrainedMinimizationProblem(new LinearObjectiveFunction[]{f}, constraints, g, VariableType.CONTINUOUS);
        solver.doOptimize();
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testDoOptimizeNoFeasibleSolution() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(MIN, new double[]{-1.0, 1.0}, new double[]{1.0, 1.0});
        LinearObjectiveFunction g = new LinearObjectiveFunction(ZERO, new double[]{1.0}, new double[]{1.0});
        Constraint[] constraints = new Constraint[]{new Constraint(1.0, 0.0, 1.0), new Constraint(2.0, 0.0, 1.0)};
        SimplexSolver solver = new SimplexSolver(1e-6, 10);

        LinearlyConstrainedMinimizationProblem problem = new LinearlyConstrainedMinimizationProblem(new LinearObjectiveFunction[]{f}, constraints, g, VariableType.CONTINUOUS);
        solver.doOptimize();
    }

    @Test
    public void testSolvePhase1() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(MIN, new double[]{-1.0, 1.0}, new double[]{1.0, 1.0});
        LinearObjectiveFunction g = new LinearObjectiveFunction(ZERO, new double[]{1.0}, new double[]{1.0});
        Constraint[] constraints = new Constraint[]{new Constraint(1.0, 0.0, 2.0), new Constraint(2.0, 0.0, 2.0)};
        SimplexSolver solver = new SimplexSolver(1e-6, 10);

        SimplexTableau tableau = new SimplexTableau(f.getFunctionData(), constraints, g.getFunctionData(), VariableType.CONTINUOUS, 1e-6, 10);
        solver.solvePhase1(tableau);

        SimplexTableauUtil.assertOptimal(tableau);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testSolvePhase1Unbounded() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(MIN, new double[]{1.0}, new double[]{1.0});
        LinearObjectiveFunction g = new LinearObjectiveFunction(ZERO, new double[]{1.0}, new double[]{0.0});
        Constraint[] constraints = new Constraint[0];
        SimplexSolver solver = new SimplexSolver(1e-6, 10);

        SimplexTableau tableau = new SimplexTableau(f.getFunctionData(), constraints, g.getFunctionData(), VariableType.CONTINUOUS, 1e-6, 10);
        solver.solvePhase1(tableau);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1NoFeasibleSolution() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(MIN, new double[]{-1.0, 1.0}, new double[]{1.0, 1.0});
        LinearObjectiveFunction g = new LinearObjectiveFunction(ZERO, new double[]{1.0}, new double[]{1.0});
        Constraint[] constraints = new Constraint[]{new Constraint(1.0, 0.0, 1.0), new Constraint(2.0, 0.0, 1.0)};
        SimplexSolver solver = new SimplexSolver(1e-6, 10);

        SimplexTableau tableau = new SimplexTableau(f.getFunctionData(), constraints, g.getFunctionData(), VariableType.CONTINUOUS, 1e-6, 10);
        solver.solvePhase1(tableau);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction[]{new LinearObjectiveFunction(MIN, new double[]{-2.0, 1.0}, new double[]{1.0, 1.0})}, new Constraint[0], new LinearObjectiveFunction(ZERO, new double[]{1.0}, new double[]{1.0}), VariableType.CONTINUOUS, 1e-6, 10);

        Integer pivotCol = SimplexSolver.this.getPivotColumn(tableau);
        assertEquals(1, pivotCol.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction[]{new LinearObjectiveFunction(MIN, new double[]{-1.0, 1.0}, new double[]{1.0, 1.0}), new LinearObjectiveFunction(MIN, new double[]{1.0, -1.0}, new double[]{1.0, 1.0})}, new Constraint[0], new LinearObjectiveFunction(ZERO, new double[]{1.0}, new double[]{1.0}), VariableType.CONTINUOUS, 1e-6, 10);

        Integer pivotRow = SimplexSolver.this.getPivotRow(tableau, 0);
        assertNull(pivotRow);

        pivotRow = SimplexSolver.this.getPivotRow(tableau, 1);
        assertEquals(1, pivotRow.intValue());
    }
}