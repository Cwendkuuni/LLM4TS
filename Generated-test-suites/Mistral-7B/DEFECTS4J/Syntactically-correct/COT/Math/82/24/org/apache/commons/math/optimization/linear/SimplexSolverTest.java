package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.simple.ArrayRealVector;
import org.apache.commons.math3.optim.nonlinear.simple.ArrayRealVectorFunction;
import org.apache.commons.math3.optim.nonlinear.simple.EuclideanDistance;
import org.apache.commons.math3.optim.nonlinear.simple.ScalarFunction;
import org.apache.commons.math3.optim.nonlinear.simple.SquaredDistance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class SimplexSolverTest {

    private static final double EPSILON = 1.0e-6;

    private SimplexSolver solver;
    private LinearObjectiveFunction f;
    private LinearConstraint[] constraints;
    private LinearConstraint goalType;
    private boolean restrictToNonNegative;

    public SimplexSolverTest(LinearObjectiveFunction f, LinearConstraint[] constraints,
                             LinearConstraint goalType, boolean restrictToNonNegative) {
        this.f = f;
        this.constraints = constraints;
        this.goalType = goalType;
        this.restrictToNonNegative = restrictToNonNegative;
        this.solver = new SimplexSolver();
    }

    @Parameterized.Parameters(name = "{0}, {1}, {2}, {3}")
    public static Collection<Object[]> data() {
        LinearObjectiveFunction f1 = new LinearObjectiveFunction(new double[][]{{1.0, 3.0}, {2.0, 1.0}}, new double[]{2.0, 1.0});
        LinearObjectiveFunction f2 = new LinearObjectiveFunction(new double[][]{{1.0, -1.0, 3.0}, {-1.0, 2.0, 1.0}}, new double[]{5.0, 5.0, 10.0});
        LinearObjectiveFunction f3 = new LinearObjectiveFunction(new double[][]{{1.0, 2.0, -3.0}, {-1.0, 1.0, 2.0}, {-3.0, -2.0, 1.0}}, new double[]{0.0, 0.0, 1.0});

        LinearConstraint c1 = new LinearConstraint(new double[]{1.0}, 1.0);
        LinearConstraint c2 = new LinearConstraint(new double[]{1.0, 1.0}, 2.0);
        LinearConstraint c3 = new LinearConstraint(new double[]{1.0, 1.0, 1.0}, 3.0);

        LinearConstraint g1 = new LinearConstraint(new double[]{1.0}, 0.0);
        LinearConstraint g2 = new LinearConstraint(new double[]{1.0, 1.0}, 1.0);

        return Arrays.asList(new Object[][]{
                new Object[]{f1, new LinearConstraint[0], null, false},
                new Object[]{f1, new LinearConstraint[]{c1}, g1, false},
                new Object[]{f2, new LinearConstraint[]{c1, c2}, null, false},
                new Object[]{f2, new LinearConstraint[]{c1, c2}, g2, false},
                new Object[]{f3, new LinearConstraint[]{c1, c2, c3}, g1, true},
                new Object[]{f3, new LinearConstraint[]{c1, c2, c3}, g2, true},
        });
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        PointValuePair solution = solver.doOptimize();
        RealPointValuePair expected = new RealPointValuePair(solution.getPoint(), solution.getValue());
        RealPointValuePair actual = solver.optimize(f, constraints, goalType, restrictToNonNegative);
        assertEquals(expected, actual, Precision.SAFE_EPSILON);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, EPSILON);
        Integer pivotCol = solver.getPivotColumn(tableau);
        Integer expected = getExpectedPivotColumn(tableau);
        assertEquals(expected, pivotCol);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, EPSILON);
        Integer pivotRow = solver.getPivotRow(0, tableau);
        Integer expected = getExpectedPivotRow(0, tableau);
        assertEquals(expected, pivotRow);
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, EPSILON);
        boolean result = solver.isPhase1Solved(tableau);
        boolean expected = getExpectedPhase1Solved(tableau);
        assertEquals(expected, result);
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, EPSILON);
        boolean result = solver.isOptimal(tableau);
        boolean expected = getExpectedOptimal(tableau);
        assertEquals(expected, result);
    }

    @Test
    public void testSolvePhase1() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, EPSILON);
        solver.solvePhase1(tableau);
        boolean result = tableau.isPhase1Solved();
        boolean expected = getExpectedPhase1Solved(tableau);
        assertEquals(expected, result);
    }

    @Test
    public void testUnboundedSolutionException() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[][]{{Double.POSITIVE_INFINITY}}, new double[]{}),
                new LinearConstraint[0], null, false, EPSILON);
        assertThrows(UnboundedSolutionException.class, () -> solver.solvePhase1(tableau));
    }

    @Test
    public void testNoFeasibleSolutionException() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[][]{{1.0, 1.0}, {-1.0, 1.0}}, new double[]{2.0, 1.0}),
                new LinearConstraint[]{new LinearConstraint(new double[]{1.0}, 0.0)}, null, false, EPSILON);
        assertThrows(NoFeasibleSolutionException.class, () -> solver.solvePhase1(tableau));
    }

    private Integer getExpectedPivotColumn(SimplexTableau tableau) {
        return Arrays.asList(tableau.getObjectiveFunction().getNonZeroCoefficients()).indexOf(MathUtils.min(tableau.getObjectiveFunction().getCoefficients()));
    }

    private Integer getExpectedPivotRow(int col, SimplexTableau tableau) {
        return Arrays.asList(tableau.getConstraintsForColumn(col)).stream()
                .mapToInt(lc -> Arrays.asList(lc.getNonZeroCoefficients()).indexOf(MathUtils.min(lc.getCoefficients()))).min().getAsInt();
    }

    private boolean getExpectedPhase1Solved(SimplexTableau tableau) {
        return tableau.getNumArtificialVariables() == 0;
    }

    private boolean getExpectedOptimal(SimplexTableau tableau) {
        for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getWidth() - 1; i++) {
            if (MathUtils.compareTo(tableau.getEntry(0, i), 0, EPSILON) < 0) {
                return false;
            }
        }
        return true;
    }
}