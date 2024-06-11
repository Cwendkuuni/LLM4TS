import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleTableau;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearOptimizationFactory;
import org.apache.commons.math3.optim.linear.LinearSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SimplexSolverTest {

    @Test
    public void testDefaultConstructor() throws OptimizationException {
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();
        Assert.assertNotNull(solution);
    }

    @Test
    public void testCustomConstructor() throws OptimizationException {
        double epsilon = 1e-4;
        SimplexSolver solver = new SimplexSolver(epsilon);
        PointValuePair solution = solver.doOptimize();
        Assert.assertNotNull(solution);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testThrowsUnboundedSolutionException() throws OptimizationException {
        SimplexSolver solver = new SimplexSolver();
        SimpleTableau tableau = new SimpleTableau(
                // Objective function
                new LinearObjectiveFunction(-new double[]{1.0, 1.0}, new double[]{0.0, 0.0}),
                // Constraints
                new LinearConstraint[]{
                        new LinearConstraint(new double[]{-Double.POSITIVE_INFINITY, -1.0}, new double[]{0.0}, new double[]{-1.0, Double.POSITIVE_INFINITY}),
                        new LinearConstraint(new double[]{-Double.POSITIVE_INFINITY, 1.0}, new double[]{0.0}, new double[]{0.0, Double.POSITIVE_INFINITY})
                },
                // Goal type
                GoalType.MINIMIZE,
                // Restrict to non-negative variables
                true,
                // Epsilon
                solver.epsilon
        );
        solver.solvePhase1(tableau);
        Assert.fail("UnboundedSolutionException not thrown");
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testThrowsNoFeasibleSolutionException() throws OptimizationException {
        SimplexSolver solver = new SimplexSolver();
        SimpleTableau tableau = new SimpleTableau(
                // Objective function
                new LinearObjectiveFunction(new double[]{1.0, 1.0}, new double[]{0.0, 0.0}),
                // Constraints
                new LinearConstraint[]{
                        new LinearConstraint(new double[]{-Double.POSITIVE_INFINITY, -1.0}, new double[]{0.0}, new double[]{0.0, Double.POSITIVE_INFINITY}),
                        new LinearConstraint(new double[]{-Double.POSITIVE_INFINITY, 1.0}, new double[]{0.0}, new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY})
                },
                // Goal type
                GoalType.MINIMIZE,
                // Restrict to non-negative variables
                true,
                // Epsilon
                solver.epsilon
        );
        solver.solvePhase1(tableau);
        tableau.discardArtificialVariables();
        solver.doIteration(tableau);
        Assert.fail("NoFeasibleSolutionException not thrown");
    }

    @Test
    public void testSimpleOptimization() throws OptimizationException {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0, -2.0}, new double[]{0.0, 5.0});
        LinearConstraint[] constraints = {
                new LinearConstraint(new double[]{1.0, 1.0}, new double[]{1.0}, new double[]{0.0, 2.0}),
                new LinearConstraint(new double[]{1.0, 1.0}, new double[]{1.0}, new double[]{1.0, 1.0})
        };
        SimpleTableau tableau = new SimpleTableau(objectiveFunction, constraints, GoalType.MINIMIZE, true, solver.epsilon);
        solver.solvePhase1(tableau);
        tableau.discardArtificialVariables();
        solver.doIteration(tableau);
        PointValuePair solution = solver.doOptimize();
        Assert.assertEquals(new double[]{1.0 / 3.0, 2.0 / 3.0}, solution.getPoint(), 0.01);
        Assert.assertEquals(-5.0 / 3.0, solution.getValue(), 0.01);
    }

    @Test
    public void testCustomEpsilon() throws OptimizationException {
        SimplexSolver solver = new SimplexSolver(1e-5);
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0, -2.0}, new double[]{0.0, 5.0});
        LinearConstraint[] constraints = {
                new LinearConstraint(new double[]{1.0, 1.0}, new double[]{1.0}, new double[]{0.0, 2.0}),
                new LinearConstraint(new double[]{1.0, 1.0}, new double[]{1.0}, new double[]{1.0, 1.0})
        };
        SimpleTableau tableau = new SimpleTableau(objectiveFunction, constraints, GoalType.MINIMIZE, true, solver.epsilon);
        solver.solvePhase1(tableau);
        tableau.discardArtificialVariables();
        solver.doIteration(tableau);
        PointValuePair solution = solver.doOptimize();
        Assert.assertEquals(new double[]{1.0 / 3.0, 2.0 / 3.0}, solution.getPoint(), 0.005);
        Assert.assertEquals(-5.0 / 3.0, solution.getValue(), 0.005);
    }
}