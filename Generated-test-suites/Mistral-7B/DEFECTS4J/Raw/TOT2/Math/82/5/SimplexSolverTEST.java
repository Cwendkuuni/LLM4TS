import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimplexTableau;
import org.apache.commons.math3.optim.unconstrained.GoalType;
import org.apache.commons.math3.optim.unconstrained.LinearConstraint;
import org.apache.commons.math3.optim.unconstrained.NonNegativeConstraint;
import org.apache.commons.math3.optim.unconstrained.RealVector;
import org.apache.commons.math3.optim.unconstrained.UnboundedSolutionException;
import org.apache.commons.math3.optim.unconstrained.nofeasible.NoFeasibleSolutionException;
import org.apache.commons.math3.optim.unconstrained.nonlinear.GroebnerBasisConstraintFunction;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.math3.optim.unconstrained.LinearConstraint.EQ;
import static org.apache.commons.math3.optim.unconstrained.LinearConstraint.GT;
import static org.apache.commons.math3.optim.unconstrained.LinearConstraint.LE;
import static org.apache.commons.math3.optim.unconstrained.LinearConstraint.GE;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize_SimpleTest() {
        // Given
        double[] f = {1.0, 2.0};
        RealVector constraints = new ArrayRealVector(new double[][]{{-1.0, -1.0}, {1.0, 1.0}});
        GoalType goalType = GoalType.MINIMIZE;
        LinearConstraint[] linearConstraints = {new LinearConstraint(new ArrayRealVector(new double[]{-1.0}), EQ, 0.0),
                new LinearConstraint(new ArrayRealVector(new double[]{1.0}), GT, 0.0)};
        SimplexSolver solver = new SimplexSolver();

        // When
        RealPointValuePair solution = solver.doOptimize();

        // Then
        PointValuePair expectedSolution = new PointValuePair(new ArrayRealVector(new double[]{-1.0, 1.0}), 1.0);
        assertEquals(expectedSolution, solution);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimize_UnboundedSolution() {
        // Given
        double[] f = {1.0, 2.0};
        SimplexTableau tableau = new SimplexTableau(f, null, GoalType.MINIMIZE, null, 0.0);

        // When
        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize(tableau);

        // Then
        // Unbounded solution exception should be thrown
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testDoOptimize_NoFeasibleSolution() {
        // Given
        double[] f = {1.0, 2.0};
        SimplexTableau tableau = new SimplexTableau(f, null, GoalType.MINIMIZE, null, 0.0);
        tableau.addLinearConstraint(new LinearConstraint(new ArrayRealVector(new double[]{0.0}), LE, 0.0));

        // When
        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize(tableau);

        // Then
        // No feasible solution exception should be thrown
    }

    @Test
    public void testDoOptimize_GroebnerBasisTest() {
        // Given
        double[] f = {1.0, 2.0, 3.0};
        RealVector constraints = new ArrayRealVector(new double[][]{{-1.0, -1.0, 0.0}, {1.0, 1.0, 1.0}});
        GoalType goalType = GoalType.MINIMIZE;
        LinearConstraint[] linearConstraints = {
                new LinearConstraint(new ArrayRealVector(new double[]{-1.0, -1.0}), EQ, 0.0),
                new LinearConstraint(new ArrayRealVector(new double[]{1.0, 1.0}), GT, 0.0),
                new LinearConstraint(new ArrayRealVector(new double[]{0.0, 1.0}), GT, 0.0)};
        GroebnerBasisConstraintFunction constraintFunction = new GroebnerBasisConstraintFunction(
                new double[][]{
                        {-1.0, -1.0, 1.0},
                        {1.0, 1.0, -1.0},
                        {2.0, 2.0, 2.0}
                });
        SimplexSolver solver = new SimplexSolver();
        solver.addConstraint(constraintFunction);

        // When
        RealPointValuePair solution = solver.doOptimize();

        // Then
        PointValuePair expectedSolution = new PointValuePair(new ArrayRealVector(new double[]{-1.0 / 3.0, -1.0 / 3.0, 1.0 / 3.0}), 1.0 / 9.0);
        assertEquals(expectedSolution, solution);
    }
}