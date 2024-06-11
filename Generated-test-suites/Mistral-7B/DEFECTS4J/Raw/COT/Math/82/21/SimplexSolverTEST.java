import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.GoalType;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.nonlinear.exception.ConstraintViolationException;
import org.apache.commons.math3.optim.nonlinear.exception.GoalTypeException;
import org.apache.commons.math3.optim.nonlinear.exception.OptimizationException;
import org.apache.commons.math3.optim.nonlinear.exception.UnboundedSolutionException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimplexSolverTest {

    SimplexSolver simplexSolver;
    LinearObjectiveFunction objectiveFunction;
    LinearConstraint[] constraints;
    GoalType goalType;

    @Before
    public void setUp() {
        simplexSolver = new SimplexSolver();
        objectiveFunction = new LinearObjectiveFunction(new double[]{-1.0, -1.0});
        constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, 0.0),
            new LinearConstraint(new double[]{1.0, -1.0}, Relationship.LEQ, 0.0),
            new LinearConstraint(new double[]{1.0, 1.0}, Relationship.EQ, 1.0)
        };
        goalType = GoalType.MINIMIZE;
    }

    @Test
    public void testDefaultConstructor() {
        simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction[]{objectiveFunction},
                constraints, goalType, false, 1.0e-6);
        try {
            simplexSolver.doOptimize();
        } catch (OptimizationException e) {
            Assert.assertEquals("Unexpected exception during optimization", e.getMessage());
        }
    }

    @Test
    public void testConstructorWithEpsilon() {
        simplexSolver = new SimplexSolver(1.0e-5);
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction[]{objectiveFunction},
                constraints, goalType, false, 1.0e-5);
        try {
            simplexSolver.doOptimize();
        } catch (OptimizationException e) {
            Assert.assertEquals("Unexpected exception during optimization", e.getMessage());
        }
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction[]{objectiveFunction},
                constraints, goalType, false, 1.0e-6);
        Integer pivotColumn = simplexSolver.getPivotColumn(tableau);
        Assert.assertNotNull("Expected a pivot column", pivotColumn);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testGetPivotColumnWhenNoPivot() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction[]{new LinearObjectiveFunction(new double[]{-1.0})},
                new LinearConstraint[]{new LinearConstraint(new double[]{0.0, 1.0}, Relationship.LEQ, 0.0)},
                goalType, false, 1.0e-6);
        simplexSolver.getPivotColumn(tableau);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction[]{objectiveFunction},
                constraints, goalType, false, 1.0e-6);
        Integer pivotRow = simplexSolver.getPivotRow(0, tableau);
        Assert.assertNotNull("Expected a pivot row", pivotRow);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testDoIterationThrowsNoFeasibleSolutionException() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction[]{new LinearObjectiveFunction(new double[]{0.0})},
                new LinearConstraint[]{new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, 0.0)},
                goalType, false, 1.0e-6);
        simplexSolver.doIteration(tableau);
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction[]{objectiveFunction},
                constraints, goalType, false, 1.0e-6);
        Assert.assertFalse("Phase 1 should not be solved initially",
                simplexSolver.isPhase1Solved(tableau));
    // Add more test cases for different tableau states
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction[]{objectiveFunction},
                constraints, goalType, false, 1.0e-6);
        simplexSolver.solvePhase1(tableau);
        tableau.discardArtificialVariables();
        Assert.assertFalse("Problem should not be optimal initially",
                simplexSolver.isOptimal(tableau));
        tableau.setEntry(0, tableau.getWidth() - 1, 0.0);
        Assert.assertTrue("Problem should be optimal after an optimal solution is reached",
                simplexSolver.isOptimal(tableau));
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testSolvePhase1ThrowsUnboundedSolutionException() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction[]{new LinearObjectiveFunction(new double[]{-1.0})},
                constraints, goalType, false, 1.0e-6);
        simplexSolver.solvePhase1(tableau);
    }

    @Test
    public void testSolvePhase1AndDiscardArtificialVariables() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction[]{objectiveFunction},
                constraints, goalType, false, 1.0e-6);
        simplexSolver.solvePhase1(tableau);
        tableau.discardArtificialVariables();
        RealPointValuePair solution = simplexSolver.doOptimize();
        Assert.assertNotNull("Expected a solution", solution);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testOptimizeThrowsNoFeasibleSolutionException() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction[]{new LinearObjectiveFunction(new double[]{0.0})},
                new LinearConstraint[]{new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, 0.0)},
                goalType, false, 1.0e-6);
        simplexSolver.doOptimize();
    }

    @Test
    public void testOptimizeReturnsSolution() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction[]{objectiveFunction},
                constraints, goalType, false, 1.0e-6);
        try {
            simplexSolver.solvePhase1(tableau);
            tableau.discardArtificialVariables();
            RealPointValuePair solution = simplexSolver.doOptimize();
            Assert.assertNotNull("Expected a solution", solution);
        } catch (OptimizationException e) {
            Assert.assertEquals("Unexpected exception during optimization", e.getMessage());
        }
    }

    @Test
    public void testOptimizeWithMultipleConstraints() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{-1.0, 0.0}, Relationship.LEQ, 0.0),
            new LinearConstraint(new double[]{1.0, 1.0}, Relationship.LEQ, 2.0),
            new LinearConstraint(new double[]{1.0, -1.0}, Relationship.LEQ, 0.0)
        };
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction[]{objectiveFunction},
                constraints, goalType, false, 1.0e-6);
        try {
            simplexSolver.solvePhase1(tableau);
            tableau.discardArtificialVariables();
            RealPointValuePair solution = simplexSolver.doOptimize();
            Assert.assertNotNull("Expected a solution", solution);
        } catch (OptimizationException e) {
            Assert.assertEquals("Unexpected exception during optimization", e.getMessage());
        }
    }

    @Test(expected = ConstraintViolationException.class)
    public void testOptimizeReturnsSolutionWithConstraintViolationException() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{-1.0, 0.0}, Relationship.LEQ, 0.0),
            new LinearConstraint(new double[]{1.0, 1.0}, Relationship.LEQ, 2.0),
            new LinearConstraint(new double[]{1.0, -1.0}, Relationship.LEQ, 0.0),
            new LinearConstraint(new double[]{2.0, 1.0}, Relationship.EQ, 2.0) // Violating constraint
        };
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction[]{objectiveFunction},
                constraints, goalType, false, 1.0e-6);
        try {
            simplexSolver.solvePhase1(tableau);
            tableau.discardArtificialVariables();
            simplexSolver.doOptimize();
        } catch (OptimizationException e) {
            Assert.assertEquals("Unexpected exception during optimization", e.getCause().getClass().getName());
        }
    }

    @Test
    public void testOptimizeWithGoalTypeException() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[]{new LinearConstraint(new double[]{-1.0, 0.0}, Relationship.LEQ, 0.0)};
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction[]{objectiveFunction}, constraints, goalType, false, 1.0e-6);
        try {
            simplexSolver.goalType = null;
            simplexSolver.doOptimize();
        } catch (GoalTypeException e) {
            Assert.assertEquals("Expected GoalTypeException", e.getClass().getName());
        }
    }
}