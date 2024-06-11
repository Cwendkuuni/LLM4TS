import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.utils.RealVector;
import org.apache.commons.math3.util.MatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testSimpleExample() {
        // Define the optimization problem
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-2.0, -1.0, 3.0, 4.0}, new double[]{1.0, 1.0, 1.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[]{-1.0, -1.0, 1.0, 1.0}, 0.0),
            new LinearConstraint(new double[]{1.0, 1.0, -1.0, -1.0}, 0.0),
            new LinearConstraint(new double[]{1.0, -1.0, 1.0, -1.0}, 0.0),
            new LinearConstraint(new double[]{-1.0, 1.0, -1.0, 1.0}, 0.0),
            new LinearConstraint(new double[]{1.0, 1.0, 1.0, 1.0}, 2.0),
            new LinearConstraint(new double[]{-1.0, -1.0, -1.0, -1.0}, 1.0),
            new LinearConstraint(new double[]{0.0, 0.0, 0.0, 0.0}, 0.0) // free variable
        };
        GoalType goalType = GoalType.MINIMIZE;
        boolean freeVariablesAreNonNegative = true;

        // Solve the optimization problem
        SimplexSolver solver = new SimplexSolver();
        RealVector solution = solver.optimize(new LinearOptimizationProblem(f, constraints, goalType, freeVariablesAreNonNegative)).getPoint();

        // Check the solution
        double[] expectedSolution = new double[]{3.0 / 5.0, 2.0 / 5.0, 1.0 / 5.0, 1.0 / 5.0};
        assertArrayEquals(expectedSolution, solution.toArray(), 0.01);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() {
        // Define the optimization problem with an unbounded solution
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{0.0, 0.0, 1.0, 1.0}, new double[]{1.0, 1.0, 1.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[]{-1.0, -1.0, 1.0, 1.0}, 0.0),
            new LinearConstraint(new double[]{1.0, 1.0, -1.0, -1.0}, 0.0),
            new LinearConstraint(new double[]{1.0, -1.0, 1.0, -1.0}, 0.0),
            new LinearConstraint(new double[]{-1.0, 1.0, -1.0, 1.0}, 0.0),
            new LinearConstraint(new double[]{1.0, 1.0, 1.0, 1.0}, 2.0),
            new LinearConstraint(new double[]{-1.0, -1.0, -1.0, -1.0}, 1.0),
            new LinearConstraint(new double[]{0.0, 0.0, 0.0, 0.0}, 0.0) // free variable
        };
        GoalType goalType = GoalType.MINIMIZE;
        boolean freeVariablesAreNonNegative = true;

        // Try to solve the optimization problem
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(new LinearOptimizationProblem(f, constraints, goalType, freeVariablesAreNonNegative));
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterations() {
        // Define the optimization problem
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-2.0, -1.0, 3.0, 4.0}, new double[]{1.0, 1.0, 1.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[]{-1.0, -1.0, 1.0, 1.0}, 0.0),
            new LinearConstraint(new double[]{1.0, 1.0, -1.0, -1.0}, 0.0),
            new LinearConstraint(new double[]{1.0, -1.0, 1.0, -1.0}, 0.0),
            new LinearConstraint(new double[]{-1.0, 1.0, -1.0, 1.0}, 0.0),
            new LinearConstraint(new double[]{1.0, 1.0, 1.0, 1.0}, 2.0),
            new LinearConstraint(new double[]{-1.0, -1.0, -1.0, -1.0}, 1.0),
            new LinearConstraint(new double[]{0.0, 0.0, 0.0, 0.0}, 0.0) // free variable
        };
        GoalType goalType = GoalType.MINIMIZE;
        boolean freeVariablesAreNonNegative = true;

        // Create a solver with a small number of iterations
        SimplexSolver solver = new SimplexSolver(0.001, 1);

        // Try to solve the optimization problem
        try {
            solver.optimize(new LinearOptimizationProblem(f, constraints, goalType, freeVariablesAreNonNegative));
            fail();
        } catch (MaxCountExceededException e) {
            // Expected
        }
    }

    @Test
    public void testNoFeasibleSolution() {
        // Define the optimization problem with no feasible solution
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-2.0, -1.0, 3.0, 4.0}, new double[]{1.0, 1.0, 1.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[]{-1.0, -1.0, 1.0, 1.0}, 0.0),
            new LinearConstraint(new double[]{1.0, 1.0, -1.0, -1.0}, 0.0),
            new LinearConstraint(new double[]{1.0, -1.0, 1.0, -1.0}, 0.0),
            new LinearConstraint(new double[]{-1.0, 1.0, -1.0, 1.0}, 0.0),
            new LinearConstraint(new double[]{1.0, 1.0, 1.0, 1.0}, Double.POSITIVE_INFINITY),
            new LinearConstraint(new double[]{-1.0, -1.0, -1.0, -1.0}, Double.NEGATIVE_INFINITY),
            new LinearConstraint(new double[]{0.0, 0.0, 0.0, 0.0}, 0.0) // free variable
        };
        GoalType goalType = GoalType.MINIMIZE;
        boolean freeVariablesAreNonNegative = true;

        // Try to solve the optimization problem
        try {
            SimplexSolver solver = new SimplexSolver();
            solver.optimize(new LinearOptimizationProblem(f, constraints, goalType, freeVariablesAreNonNegative));
            fail();
        } catch (NoFeasibleSolutionException e) {
            // Expected
        }
    }

    @Test
    public void testDegenerateCase() {
        // Define the optimization problem with a degenerate case
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-2.0, -1.0, 3.0, 4.0}, new double[]{1.0, 1.0, 1.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[]{-1.0, -1.0, 1.0, 1.0}, 0.0),
            new LinearConstraint(new double[]{1.0, 1.0, -1.0, -1.0}, 0.0),
            new LinearConstraint(new double[]{1.0, -1.0, 1.0, -1.0}, 0.0),
            new LinearConstraint(new double[]{-1.0, 1.0, -1.0, 1.0}, 0.0),
            new LinearConstraint(new double[]{1.0, 1.0, 1.0, 1.0}, 2.0),
            new LinearConstraint(new double[]{-1.0, -1.0, -1.0, -1.0}, 1.0),
            new LinearConstraint(new double[]{0.0, 0.0, 0.0, 0.0}, 0.0) // free variable
        };
        GoalType goalType = GoalType.MINIMIZE;
        boolean freeVariablesAreNonNegative = true;

        // Create a solver with default settings
        SimplexSolver solver = new SimplexSolver();

        // Solve the optimization problem
        try {
            PointValuePair solution = solver.optimize(new LinearOptimizationProblem(f, constraints, goalType, freeVariablesAreNonNegative));

            // Check the solution
            double[] expectedSolution = new double[]{3.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0};
            assertEquals(expectedSolution[0], solution.getPoint()[0], 0.01);
            assertEquals(expectedSolution[1], solution.getPoint()[1], 0.01);
            assertEquals(expectedSolution[2], solution.getPoint()[2], 0.01);
            assertEquals(expectedSolution[3], solution.getPoint()[3], 0.01);
            assertEquals(expectedSolution[0] + expectedSolution[1] + expectedSolution[2] + expectedSolution[3], solution.getValue(), 0.01);

        } catch (MaxCountExceededException | UnboundedSolutionException | NoFeasibleSolutionException e) {
            fail();
        }
    }

    @Test
    public void testLargeExample() {
        // Define the optimization problem
        int size = 10;
        LinearObjectiveFunction f = new LinearObjectiveFunction(MatrixUtils.createColumnMatrix(new double[size * size]), MatrixUtils.createColumnVector(new double[size]));
        LinearConstraint[] constraints = new LinearConstraint[size * 3];
        for (int i = 0; i < size * 3; i++) {
            LinearConstraint c = new LinearConstraint(new double[]{}, 0.0);
            c.setConstraints(new double[]{}, new double[]{i % 3 + 1, i % 3 + 1, i / 3 + 1, i / 3 + 1}, new double[]{1.0, -1.0, 1.0, -1.0});
            constraints[i] = c;
        }
        GoalType goalType = GoalType.MINIMIZE;
        boolean freeVariablesAreNonNegative = true;

        // Create a solver with default settings
        SimplexSolver solver = new SimplexSolver();

        // Solve the optimization problem
        try {
            PointValuePair solution = solver.optimize(new LinearOptimizationProblem(f, constraints, goalType, freeVariablesAreNonNegative));

            // Check the solution
            double[] expectedSolution = new double[size];
            for (int i = 0; i < size; i++) {
                expectedSolution[i] = Precision.round(Math.pow(10.0, (i + 1) / 10.0));
            }
            for (int i = 0; i < size; i++) {
                assertEquals(expectedSolution[i], solution.getPoint()[i], 0.01);
            }

            double objectiveValue = 0.0;
            for (int i = 0; i < size; i++) {
                objectiveValue += expectedSolution[i] * f.getGradient()[i];
            }
            assertEquals(objectiveValue, solution.getValue(), 0.01);

        } catch (MaxCountExceededException | UnboundedSolutionException | NoFeasibleSolutionException e) {
            fail();
        }
    }
}