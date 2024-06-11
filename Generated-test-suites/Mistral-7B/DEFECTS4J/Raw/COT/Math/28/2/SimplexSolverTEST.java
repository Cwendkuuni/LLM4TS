import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.LinearConstraintSet;
import org.apache.commons.math3.optimization.SimplexTableau;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize_ValidInput_ReturnsOptimalSolution() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{3.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, -1.0}, 2.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(constraint1);
        constraints.addConstraint(constraint2);

        SimplexSolver simplexSolver = new SimplexSolver();
        PointValuePair solution = simplexSolver.doOptimize(objectiveFunction, constraints, null, null);

        Assert.assertEquals(0.0, solution.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[]{2.0 / 3.0, 1.0 / 3.0}, solution.getPoint(), 1e-6);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testDoOptimize_MaxIterationsReached_ThrowsException() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{3.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, -1.0}, 2.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(constraint1);
        constraints.addConstraint(constraint2);

        SimplexSolver simplexSolver = new SimplexSolver();
        simplexSolver.setMaxIterations(0);
        Assert.assertThrows(MaxCountExceededException.class, () -> simplexSolver.doOptimize(objectiveFunction, constraints, null, null));
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimize_UnboundedSolution_ThrowsException() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{0.0, 1.0}, new double[]{0.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, -1.0}, 2.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(constraint1);
        constraints.addConstraint(constraint2);

        SimplexSolver simplexSolver = new SimplexSolver();
        Assert.assertThrows(UnboundedSolutionException.class, () -> simplexSolver.doOptimize(objectiveFunction, constraints, null, null));
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testDoOptimize_NoFeasibleSolution_ThrowsException() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{3.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, -1.0}, 1.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(constraint1);
        constraints.addConstraint(constraint2);

        SimplexSolver simplexSolver = new SimplexSolver();
        Assert.assertThrows(NoFeasibleSolutionException.class, () -> simplexSolver.doOptimize(objectiveFunction, constraints, null, null));
    }

    @Test
    public void testDoIteration_ValidInput_PerformsIteration() {
        double[][] tableau = new double[][]{{-1.0, 1.0, 1.0 / 3.0, 1.0 / 3.0, 0.0, 0.0},
                {3.0, 0.0, -1.0, -2.0 / 3.0, 1.0, 1.0},
                {0.0, 0.0, 0.0, 1.0 / 3.0, 1.0, 1.0 / 3.0}};
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau simplexTableau = new SimplexTableau(tableau);

        simplexSolver.doIteration(simplexTableau);

        Assert.assertEquals(0.0, simplexTableau.getEntry(1, 1), 1e-6);
        Assert.assertEquals(1.0 / 3.0, simplexTableau.getEntry(1, 4), 1e-6);
        Assert.assertEquals(1.0 / 3.0, simplexTableau.getEntry(2, 4), 1e-6);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testDoIteration_MaxIterationsReached_ThrowsException() {
        double[][] tableau = new double[][]{{-1.0, 1.0, 1.0 / 3.0, 1.0 / 3.0, 0.0, 0.0},
                {3.0, 0.0, -1.0, -2.0 / 3.0, 1.0, 1.0},
                {0.0, 0.0, 0.0, 1.0 / 3.0, 1.0, 1.0 / 3.0}};
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau simplexTableau = new SimplexTableau(tableau);
        simplexSolver.setMaxIterations(0);
        Assert.assertThrows(MaxCountExceededException.class, () -> simplexSolver.doIteration(simplexTableau));
    }

}