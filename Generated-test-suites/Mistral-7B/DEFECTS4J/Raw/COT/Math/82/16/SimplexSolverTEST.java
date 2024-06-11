import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraints;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.nonlinear.simple.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.nonlinear.simple.exception.NoFeasibleSolutionException;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class SimplexSolverTest {

    @Test
    public void testDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        assertEquals(1.0e-6, solver.epsilon, 0);
    }

    @Test
    public void testCustomConstructor() {
        double customEpsilon = 1.0e-8;
        SimplexSolver solver = new SimplexSolver(customEpsilon);
        assertEquals(customEpsilon, solver.epsilon, 0);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolutionException() {
        SimplexSolver solver = new SimplexSolver();
        LinearTableau tableau = new LinearTableau(new LinearObjectiveFunction(new double[]{-1.0, -2.0}, new double[]{0.0}),
                new LinearConstraints(new LinearConstraint[]{
                        new LinearConstraint(1.0, new double[]{1.0, 1.0}, 1.0),
                        new LinearConstraint(2.0, new double[]{2.0, 1.0}, Double.POSITIVE_INFINITY),
                        new LinearConstraint(3.0, new double[]{1.0, 2.0}, Double.POSITIVE_INFINITY)
                }));
        solver.solvePhase1(tableau);
        solver.doIteration(tableau);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolutionException() {
        SimplexSolver solver = new SimplexSolver();
        LinearTableau tableau = new LinearTableau(new LinearObjectiveFunction(new double[]{-1.0, -2.0}, new double[]{0.0}),
                new LinearConstraints(new LinearConstraint[]{
                        new LinearConstraint(1.0, new double[]{1.0, 1.0}, 1.0),
                        new LinearConstraint(2.0, new double[]{1.0, 1.0}, Double.POSITIVE_INFINITY)
                }));
        solver.solvePhase1(tableau);
        solver.doIteration(tableau);
    }

    @Test
    public void testSolveOptimization() {
        SimplexSolver solver = new SimplexSolver();
        LinearTableau tableau = new LinearTableau(new LinearObjectiveFunction(new double[]{2.0, 1.0}, new double[]{5.0}),
                new LinearConstraints(new LinearConstraint[]{
                        new LinearConstraint(1.0, new double[]{1.0, 1.0}, 3.0),
                        new LinearConstraint(2.0, new double[]{2.0, 1.0}, 4.0),
                        new LinearConstraint(3.0, new double[]{1.0, 2.0}, 2.0)
                }));
        RealPointValuePair result = solver.doOptimize();
        PointValuePair expected = new PointValuePair(new double[]{3.0 / 4.0, 5.0 / 4.0}, 15.0 / 4.0);
        assertEquals(expected.getPoint(), result.getPoint(), 0);
        assertEquals(expected.getValue(), result.getValue(), 0);
    }

    @Test
    public void testSolveOptimizationWithNegativeCoefficients() {
        SimplexSolver solver = new SimplexSolver();
        LinearTableau tableau = new LinearTableau(new LinearObjectiveFunction(new double[]{-2.0, 3.0}, new double[]{0.0}),
                new LinearConstraints(new LinearConstraint[]{
                        new LinearConstraint(1.0, new double[]{1.0, 1.0}, 3.0),
                        new LinearConstraint(2.0, new double[]{1.0, -1.0}, 4.0),
                        new LinearConstraint(3.0, new double[]{2.0, 1.0}, 2.0)
                }));
        RealPointValuePair result = solver.doOptimize();
        PointValuePair expected = new PointValuePair(new double[]{-2.0 / 5.0, 3.0 / 5.0}, -2.0 / 5.0);
        assertEquals(expected.getPoint(), result.getPoint(), 0);
        assertEquals(expected.getValue(), result.getValue(), 0);
    }

    @Test
    public void testSolveOptimizationWithConstraints() {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{1.0});
        LinearConstraints constraints = new LinearConstraints(new LinearConstraint[]{
                new LinearConstraint(1.0, new double[]{1.0, 1.0}, 2.0),
                new LinearConstraint(2.0, new double[]{1.0, 2.0}, 3.0),
                new LinearConstraint(3.0, new double[]{1.0, 2.0}, 1.0),
                new LinearConstraint(4.0, new double[]{2.0, 1.0}, 4.0)
        });
        LinearTableau tableau = new LinearTableau(objective, constraints);
        RealPointValuePair result = solver.doOptimize();
        PointValuePair expected = new PointValuePair(new double[]{2.0 / 3.0, 1.0}, 1.0);
        assertEquals(expected.getPoint(), result.getPoint(), 0);
        assertEquals(expected.getValue(), result.getValue(), 0);
    }

    @Test
    public void testSolveOptimizationWithConstraintsAndNegativeCoefficients() {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1.0, 3.0}, new double[]{1.0});
        LinearConstraints constraints = new LinearConstraints(new LinearConstraint[]{
                new LinearConstraint(1.0, new double[]{1.0, 1.0}, 2.0),
                new LinearConstraint(2.0, new double[]{1.0, -1.0}, 3.0),
                new LinearConstraint(3.0, new double[]{2.0, 1.0}, 1.0),
                new LinearConstraint(4.0, new double[]{2.0, 1.0}, 4.0)
        });
        LinearTableau tableau = new LinearTableau(objective, constraints);
        RealPointValuePair result = solver.doOptimize();
        PointValuePair expected = new PointValuePair(new double[]{-1.0 / 4.0, 3.0 / 4.0}, -1.0 / 4.0);
        assertEquals(expected.getPoint(), result.getPoint(), 0);
        assertEquals(expected.getValue(), result.getValue(), 0);
    }

    @Test
    public void testSolveOptimizationWithMultipleObjectives() {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction objective1 = new LinearObjectiveFunction(new double[]{2.0, 1.0}, new double[]{1.0, 1.0});
        LinearObjectiveFunction objective2 = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{1.0, 1.0});
        LinearConstraints constraints = new LinearConstraints(new LinearConstraint[]{
                new LinearConstraint(1.0, new double[]{1.0, 1.0}, 2.0),
                new LinearConstraint(2.0, new double[]{1.0, 2.0}, 3.0),
                new LinearConstraint(3.0, new double[]{1.0, 2.0}, 1.0),
                new LinearConstraint(4.0, new double[]{2.0, 1.0}, 4.0)
        });
        LinearTableau tableau = new LinearTableau(new LinearObjectiveFunction[]{objective1, objective2}, constraints);
        RealPointValuePair result = solver.doOptimize();
        PointValuePair expectedObjective1 = new PointValuePair(new double[]{2.0 / 3.0, 1.0}, 2.0 / 3.0);
        PointValuePair expectedObjective2 = new PointValuePair(new double[]{1.0, 2.0}, 3.0);
        assertEquals(expectedObjective1.getPoint(), result.getPoint(), 0);
        assertEquals(expectedObjective1.getValue(), result.getObjectiveValue()[0], 0);
        assertEquals(expectedObjective2.getPoint(), result.getPoint(), 0);
        assertEquals(expectedObjective2.getValue(), result.getObjectiveValue()[1], 0);
    }
}