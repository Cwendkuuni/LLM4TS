import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SolutionCallback;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.Relationship;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.optimization.PointValuePair;
import java.util.Arrays;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    private double epsilon = 1e-6;
    private int maxUlps = 10;

    private SimplexSolver simplexSolver;

    @Before
    public void setUp() {
        simplexSolver = new SimplexSolver(epsilon, maxUlps);
    }

    @Test
    public void testDoOptimize_typicalUseCase() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[]{2, 3}, 0);
        LinearConstraint[] constraints = new LinearConstraint[2];
        constraints[0] = new LinearConstraint(new double[]{2, 1}, Relationship.LEQ, 18);
        constraints[1] = new LinearConstraint(new double[]{3, 2}, Relationship.LEQ, 24);
        PointValuePair solution = simplexSolver.optimize(function, constraints, GoalType.MAXIMIZE);
        assertEquals(12, solution.getValue(), epsilon);
        assertTrue(Arrays.equals(new double[]{6,6}, solution.getPoint()));
    }

    @Test
    public void testDoOptimize_zeroObjectives() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[]{0, 0}, 0);
        LinearConstraint[] constraints = new LinearConstraint[2];
        constraints[0] = new LinearConstraint(new double[]{2, 1}, Relationship.LEQ, 18);
        constraints[1] = new LinearConstraint(new double[]{3, 2}, Relationship.LEQ, 24);
        PointValuePair solution = simplexSolver.optimize(function, constraints, GoalType.MAXIMIZE);
        assertEquals(0, solution.getValue(), epsilon);
        assertTrue(Arrays.equals(new double[]{0,0}, solution.getPoint()));
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testDoOptimize_noFeasibleSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[]{2, 3}, 0);
        LinearConstraint[] constraints = new LinearConstraint[2];
        constraints[0] = new LinearConstraint(new double[]{2, 1}, Relationship.LEQ, 10);
        constraints[1] = new LinearConstraint(new double[]{3, 2}, Relationship.LEQ, 5);
        simplexSolver.optimize(function, constraints, GoalType.MAXIMIZE);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimize_unboundedSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[]{2, 3}, 0);
        LinearConstraint[] constraints = new LinearConstraint[1];
        constraints[0] = new LinearConstraint(new double[]{-1, -1}, Relationship.LEQ, -10);
        simplexSolver.optimize(function, constraints, GoalType.MAXIMIZE);
    }

}