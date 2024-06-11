import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.NonLinearConstraints;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.MaxCountExceededException;
import org.apache.commons.math3.optim.UnboundedSolutionException;
import org.apache.commons.math3.optim.NoFeasibleSolutionException;
import org.apache.commons.math3.optim.LinearOptimizer;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testEmptyConstructor() {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0}, new double[]{1.0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        NonLinearConstraints nonLinearConstraints = null;
        PointValuePair solution = solver.doOptimize(objectiveFunction, constraints, nonLinearConstraints);
        Assert.assertNull(solution);
    }

    @Test
    public void testConstructorWithArguments() {
        SimplexSolver solver = new SimplexSolver(0.001, 10);
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0}, new double[]{1.0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        NonLinearConstraints nonLinearConstraints = null;
        PointValuePair solution = solver.doOptimize(objectiveFunction, constraints, nonLinearConstraints);
        Assert.assertNull(solution);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeEpsilon() {
        SimplexSolver solver = new SimplexSolver(-0.001, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxUlps() {
        SimplexSolver solver = new SimplexSolver(0.001, -10);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(1, 2, 1.0, new double[0], new double[]{1.0, 2.0}, new double[]{3.0, 4.0});
        Integer pivotColumn = SimplexSolver.getInstance().getPivotColumn(tableau);
        Assert.assertEquals(0, pivotColumn.intValue());
    }

    @Test
    public void testGetPivotRowNull() {
        SimplexTableau tableau = new SimplexTableau(1, 2, 1.0, new double[]{-1.0, 1.0}, new double[]{3.0, 4.0}, new double[]{0.0, 0.0});
        Integer pivotRow = SimplexSolver.getInstance().getPivotRow(tableau, 0);
        Assert.assertNull(pivotRow);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(1, 3, 1.0, new double[]{-1.0, 1.0, 0.0}, new double[]{3.0, 4.0, 2.0}, new double[]{0.0, 0.0, 1.0});
        Integer pivotRow = SimplexSolver.getInstance().getPivotRow(tableau, 0);
        Assert.assertEquals(1, pivotRow.intValue());
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationUnboundedSolution() {
        SimplexTableau tableau = new SimplexTableau(1, 2, 1.0, new double[]{0.0, 1.0}, new double[]{3.0, Double.POSITIVE_INFINITY}, new double[]{0.0, 0.0});
        SimplexSolver.getInstance().doIteration(tableau);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testDoIterationMaxCountExceeded() {
        SimplexTableau tableau = new SimplexTableau(1, 2, 1.0, new double[]{0.0, 1.0}, new double[]{3.0, 3.0}, new double[]{0.0, 0.0});
        SimplexSolver solver = new SimplexSolver(0.001, 1);
        for (int i = 0; i < 50; i++) {
            solver.doIteration(tableau);
        }
    }

    @Test
    public void testSolvePhase1() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0, 1.0}, new double[]{2.0, 3.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(1, new double[]{0.0, 1.0}, 3.0),
            new LinearConstraint(2, new double[]{1.0, 0.0}, 2.0)
        };
        SimplexTableau tableau = new SimplexTableau(2, 3, 1.0, new double[]{0.0, 1.0, 1.0}, new double[]{2.0, 3.0, 1.0}, new double[]{3.0, 2.0, 0.0});

        SimplexSolver solver = new SimplexSolver(0.001, 10);
        solver.solvePhase1(tableau);

        Assert.assertEquals(0, tableau.getNumArtificialVariables());
        Assert.assertFalse(tableau.isOptimal());
    }

    @Test
    public void testSolvePhase1Optimal() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0, 1.0}, new double[]{2.0, 3.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(1, new double[]{0.0, 1.0}, 3.0),
            new LinearConstraint(2, new double[]{1.0, 0.0}, 2.0)
        };
        SimplexTableau tableau = new SimplexTableau(2, 3, 1.0, new double[]{0.0, 1.0, 1.0}, new double[]{2.0, 3.0, 0.0}, new double[]{3.0, 2.0, 0.0});

        SimplexSolver solver = new SimplexSolver(0.001, 10);
        solver.solvePhase1(tableau);
        Assert.assertEquals(0, tableau.getNumArtificialVariables());
        Assert.assertTrue(tableau.isOptimal());
    }

    @Test
    public void testDoOptimize() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0, 1.0}, new double[]{2.0, 3.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(1, new double[]{0.0, 1.0}, 3.0),
            new LinearConstraint(2, new double[]{1.0, 0.0}, 2.0)
        };
        SimplexTableau tableau = new SimplexTableau(2, 3, 1.0, new double[]{0.0, 1.0, 1.0}, new double[]{2.0, 3.0, 0.0}, new double[]{3.0, 2.0, 0.0});

        SimplexSolver solver = new SimplexSolver(0.001, 10);
        PointValuePair solution = solver.doOptimize();
        Assert.assertNotNull(solution);
        Assert.assertEquals(2, solution.getPoint().length);
        Assert.assertEquals(0.0, solution.getValue(), 0.001);
    }

    @Test
    public void testDoOptimizeUnboundedSolution() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0, 1.0}, new double[]{2.0, 3.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(1, new double[]{0.0, 1.0}, 3.0),
            new LinearConstraint(2, new double[]{1.0, 0.0}, 2.0)
        };
        SimplexTableau tableau = new SimplexTableau(2, 3, 1.0, new double[]{0.0, 1.0, 1.0}, new double[]{2.0, Double.POSITIVE_INFINITY, 0.0}, new double[]{3.0, 2.0, 0.0});

        SimplexSolver solver = new SimplexSolver(0.001, 10);
        PointValuePair solution = solver.doOptimize();
        Assert.assertNull(solution);
    }

    @Test
    public void testDoOptimizeNoFeasibleSolution() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0, 1.0}, new double[]{2.0, 3.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(1, new double[]{0.0, 1.0}, 3.0),
            new LinearConstraint(2, new double[]{1.0, 0.0}, 3.0)
        };
        SimplexTableau tableau = new SimplexTableau(2, 3, 1.0, new double[]{0.0, 1.0, 1.0}, new double[]{2.0, 3.0, 0.0}, new double[]{3.0, 3.0, 0.0});

        SimplexSolver solver = new SimplexSolver(0.001, 10);
        try {
            PointValuePair solution = solver.doOptimize();
            Assert.fail();
        } catch (NoFeasibleSolutionException e) {
            // expected
        }
    }
}