import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.Relationship;
import org.apache.commons.math3.optimization.linear.LinearConstraintSet;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.util.Precision;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SimplexSolverTest {

    private SimplexSolver solver;
    private LinearObjectiveFunction function;
    private LinearConstraintSet constraints;

    @BeforeEach
    public void setUp() {
        solver = new SimplexSolver();
        function = new LinearObjectiveFunction(new double[] { -3, -4 }, 0);
        constraints = new LinearConstraintSet(
            new LinearConstraint(new double[] { 1, 2 }, Relationship.LEQ, 3),
            new LinearConstraint(new double[] { 2, 1 }, Relationship.LEQ, 3),
            new LinearConstraint(new double[] { -1, 1 }, Relationship.LEQ, 1)
        );
    }

    @Test
    public void testDoOptimize_noFeasibleSolution_exceptionThrown() {
        function = new LinearObjectiveFunction(new double[] { 1, 1 }, 0);
        constraints = new LinearConstraintSet(
            new LinearConstraint(new double[] { 1, -1 }, Relationship.LEQ, 0),
            new LinearConstraint(new double[] { -1, -1 }, Relationship.LEQ, -1)
        );

        assertThrows(NoFeasibleSolutionException.class, () -> solver.doOptimize(function, constraints));
    }

    @Test
    public void testDoOptimize_unboundedSolution_exceptionThrown() {
        constraints = new LinearConstraintSet(
            new LinearConstraint(new double[] { 1, -1 }, Relationship.LEQ, 0),
            new LinearConstraint(new double[] { 1, 1 }, Relationship.GEQ, 0)
        );

        assertThrows(UnboundedSolutionException.class, () -> solver.doOptimize(function, constraints));
    }

    @Test
    public void testDoOptimize_maxIterationsExceeded_exceptionThrown() {
        constraints = new LinearConstraintSet(
            new LinearConstraint(new double[] { 1, -1 }, Relationship.LEQ, 0),
            new LinearConstraint(new double[] { -1, -1 }, Relationship.LEQ, -2)
        );

        assertThrows(MaxCountExceededException.class, () -> solver.doOptimize(function, constraints));
    }

    @Test
    public void testDoOptimize_validSolution() {
        PointValuePair solution = solver.doOptimize(function, constraints);
        double[] expectedPoint = { 1.5, 0.75 };
        double expectedValue = -7.5;
        assertTrue(Precision.equals(solution.getPoint(), expectedPoint, 1e-6));
        assertTrue(Precision.equals(solution.getValue(), expectedValue, 1e-6));
    }

    @Test
    public void testDoOptimize_singleConstraint() {
        constraints = new LinearConstraintSet(
            new LinearConstraint(new double[] { 1, 2 }, Relationship.LEQ, 3)
        );

        PointValuePair solution = solver.doOptimize(function, constraints);
        double[] expectedPoint = { 1.5, 0 };
        double expectedValue = -4.5;
        assertTrue(Precision.equals(solution.getPoint(), expectedPoint, 1e-6));
        assertTrue(Precision.equals(solution.getValue(), expectedValue, 1e-6));
    }

    @Test
    public void testDoOptimize_zeroObjectiveCoefficients() {
        function = new LinearObjectiveFunction(new double[] { 0, 0 }, 0);

        PointValuePair solution = solver.doOptimize(function, constraints);
        double[] expectedPoint = { 0, 0 };
        double expectedValue = 0;
        assertTrue(Precision.equals(solution.getPoint(), expectedPoint, 1e-6));
        assertTrue(Precision.equals(solution.getValue(), expectedValue, 1e-6));
    }

}