import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

public class SimplexSolverTest {

    private SimplexSolver solver;
    private LinearObjectiveFunction objFunction;
    private LinearConstraint[] constraints;

    @Before
    public void setup() {
        double[] coefficients = {-2.0, -1.0, 3.0, -1.0};
        double[] constant = {5.0, 2.0};
        objFunction = new LinearObjectiveFunction(coefficients, constant);
        int numConstraints = 2;
        LinearConstraint[] constraintArray = new LinearConstraint[numConstraints];
        double[][] constraintMatrices = {{-1.0, 1.0, 2.0, 3.0}, {-1.0, -1.0, 0.0, 1.0}};
        double[] constraintConstants = {4.0, 3.0};
        for (int i = 0; i < numConstraints; i++) {
            constraintArray[i] = new LinearConstraint(constraintMatrices[i], constraintConstants[i]);
        }
        constraints = constraintArray;
        solver = new SimplexSolver();
    }

    @Test
    public void testSolveOptimization() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(objFunction, constraints, null, null, 1.0e-6);
        solver.solvePhase1(tableau);
        tableau.discardArtificialVariables();
        while (!solver.isOptimal(tableau)) {
            solver.doIteration(tableau);
        }
        RealPointValuePair result = solver.doOptimize();
        PointValuePair expected = new PointValuePair(new double[]{3.0/5.0, 2.0/5.0}, objFunction.value(3.0/5.0, 2.0/5.0));
        Assert.assertEquals(expected.getValue(), result.getValue(), 1.0e-6);
        Assert.assertArrayEquals(expected.getPoint(), result.getPoint(), 1.0e-6);
    }

    @Test(expected = OptimizationException.class)
    public void testSolveUnboundedSolutionException() throws OptimizationException {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{}, new double[]{0.0});
        SimplexTableau tableau = new SimplexTableau(objFunction, constraints, null, null, 1.0e-6);
        solver.solvePhase1(tableau);
        throw new OptimizationException("Unbounded solution");
    }

    @Test(expected = OptimizationException.class)
    public void testSolveNoFeasibleSolutionException() throws OptimizationException {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{-1.0, -1.0}, new double[]{1.0, 1.0});
        SimplexTableau tableau = new SimplexTableau(objFunction, constraints, null, null, 1.0e-6);
        solver.solvePhase1(tableau);
        throw new OptimizationException("No feasible solution");
    }

    @Test(expected = OptimizationException.class)
    public void testSolveNoOptimalSolutionException() throws OptimizationException {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{-1.0, -1.0, 3.0}, new double[]{1.0, 1.0, 1.0});
        SimplexTableau tableau = new SimplexTableau(objFunction, constraints, null, null, 1.0e-6);
        solver.solvePhase1(tableau);
        throw new OptimizationException("No optimal solution");
    }
}