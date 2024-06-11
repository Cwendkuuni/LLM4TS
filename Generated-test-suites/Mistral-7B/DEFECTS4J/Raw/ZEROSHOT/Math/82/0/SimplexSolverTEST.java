import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleOptimizationProblem;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.optim.PointValuePair.Pair;
import static org.apache.commons.math3.optim.linear.LinearConstraint.Type.EQUALITY;
import static org.apache.commons.math3.optim.linear.LinearConstraint.Type.INEQUALITY;
import static org.apache.commons.math3.optim.linear.LinearConstraint.Type.LEQ;
import static org.apache.commons.math3.optim.linear.LinearConstraint.Type.GEQ;

public class SimplexSolverTest {

    @Test
    public void testSolver() {
        // Test with a simple linear optimization problem
        SimpleOptimizationProblem problem = new SimpleOptimizationProblem(
                new LinearObjectiveFunction(-new double[]{1.0, 2.0}, new double[]{3.0, 4.0}),
                new LinearConstraint[]{
                        new LinearConstraint(new double[]{-2.0, -1.0}, EQUALITY, 0),
                        new LinearConstraint(new double[]{1.0, 1.0}, GEQ, 2),
                        new LinearConstraint(new double[]{1.0, 1.0}, LEQ, 5)
                });

        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(problem);

        Assert.assertEquals(Pair.pair(new double[]{0.5, 0.5}, 5.5), solution, 0.001);

        // Test with a non-linear problem (should throw an exception)
        problem = new SimpleOptimizationProblem(
                new LinearObjectiveFunction(-new double[]{1.0, 2.0}, new double[]{3.0, 4.0}),
                new LinearConstraint[]{
                        new LinearConstraint(new double[]{-2.0, -1.0}, EQUALITY, 0),
                        new LinearConstraint(new double[]{1.0, 1.0}, GEQ, 2),
                        new LinearConstraint(new double[]{1.0, 1.0}, LEQ, 5),
                        new LinearConstraint(new double[]{1.0, 1.0}, INEQUALITY, 3.5)
                });

        try {
            solver.optimize(problem);
            Assert.fail("An exception should have been thrown.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with a problem with no solution
        problem = new SimpleOptimizationProblem(
                new LinearObjectiveFunction(-new double[]{1.0, 2.0}, new double[]{3.0, 4.0}),
                new LinearConstraint[]{
                        new LinearConstraint(new double[]{1.0, 1.0}, LEQ, 1),
                        new LinearConstraint(new double[]{1.0, 1.0}, LEQ, 1)
                });

        try {
            solver.optimize(problem);
            Assert.fail("An exception should have been thrown.");
        } catch (NoFeasibleSolutionException e) {
            // Expected
        }

        // Test with a problem with unbounded solution
        problem = new SimpleOptimizationProblem(
                new LinearObjectiveFunction(-new double[]{1.0}, new double[]{0.0}),
                new LinearConstraint[]{
                        new LinearConstraint(new double[]{1.0}, GEQ, 0)
                });

        try {
            solver.optimize(problem);
            Assert.fail("An exception should have been thrown.");
        } catch (UnboundedSolutionException e) {
            // Expected
        }
    }
}