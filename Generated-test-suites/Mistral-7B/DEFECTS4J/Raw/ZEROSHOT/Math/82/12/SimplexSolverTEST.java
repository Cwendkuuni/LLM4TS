import org.apache.commons.math3.optim.LinearConstraint;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleMaximizer;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{5.0, 6.0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize(f, constraints, SimpleMaximizer.MAXIMIZE, false, 1.0e-6);
        assertNotNull(solution);
        assertEquals(8.0, solution.getPoint()[0], 1e-6);
        assertEquals(10.0, solution.getPoint()[1], 1e-6);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimizeUnbounded() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, -1.0}, {1.0, 1.0}}, new double[]{0.0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize(f, constraints, SimpleMaximizer.MAXIMIZE, false, 1.0e-6);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testDoOptimizeNoFeasibleSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, 1.0}, {-1.0, -1.0}}, new double[]{0.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(new double[]{1.0}, 1.0, Relationship.LEQ),
                new LinearConstraint(new double[]{1.0}, 2.0, Relationship.LEQ)
        };
        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize(f, constraints, SimpleMaximizer.MAXIMIZE, false, 1.0e-6);
    }

    @Test(expected = OptimizationException.class)
    public void testDoOptimizeException() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{5.0, 6.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(new double[]{1.0}, 1.0, Relationship.EQ),
                new LinearConstraint(new double[]{1.0}, 2.0, Relationship.GT)
        };
        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize(f, constraints, SimpleMaximizer.MAXIMIZE, false, 1.0e-6);
    }
}