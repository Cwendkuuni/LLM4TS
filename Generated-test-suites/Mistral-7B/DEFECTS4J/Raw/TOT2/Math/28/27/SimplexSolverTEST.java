import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.simple.functions.Add;
import org.apache.commons.math3.optim.nonlinear.simple.functions.Constant;
import org.apache.commons.math3.optim.nonlinear.simple.functions.Cubic;
import org.apache.commons.math3.optim.nonlinear.simple.functions.Exponential;
import org.apache.commons.math3.optim.nonlinear.simple.functions.Linear;
import org.apache.commons.math3.optim.nonlinear.simple.functions.Log;
import org.apache.commons.math3.optim.nonlinear.simple.functions.Quadratic;
import org.apache.commons.math3.optim.nonlinear.simple.functions.Sine;
import org.apache.commons.math3.optim.nonlinear.simple.functions.Square;
import org.apache.commons.math3.optim.linear.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class SimplexSolverTest {

    @Test
    public void testSimpleConstructor() {
        SimplexSolver solver = new SimplexSolver();
        assertNotNull(solver.getGoalType());
        assertEquals(GoalType.MINIMIZE, solver.getGoalType());
    }

    @Test
    public void testCustomConstructor() {
        double epsilon = 1e-4;
        int maxUlps = 10;
        SimplexSolver solver = new SimplexSolver(epsilon, maxUlps);
        assertEquals(epsilon, solver.epsilon, epsilon);
        assertEquals(maxUlps, solver.maxUlps);
    }

    @Test
    public void testGetFunction() {
        LinearFunction f = new LinearFunction(new double[]{-2.0, 3.0}, new double[]{1.0, 2.0});
        SimplexSolver solver = new SimplexSolver();
        assertEquals(f, solver.getFunction());
    }

    @Test
    public void testGetConstraints() {
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(new double[]{1.0, 2.0}, 0.0),
                new LinearConstraint(new double[]{1.0, 2.0}, 3.0)
        };
        SimplexSolver solver = new SimplexSolver();
        assertEquals(Arrays.asList(constraints), solver.getConstraints());
    }

    @Test
    public void testGetGoalType() {
        SimplexSolver solver = new SimplexSolver();
        assertEquals(GoalType.MINIMIZE, solver.getGoalType());
    }

    @Test
    public void testRestrictToNonNegative() {
        LinearFunction f = new LinearFunction(new double[]{-2.0, 3.0}, new double[]{1.0, 2.0});
        SimplexSolver solver = new SimplexSolver();
        LinearFunction restrictedFunction = solver.restrictToNonNegative(f);
        LinearFunction expectedFunction = new LinearFunction(new double[]{0.0, 3.0}, new double[]{1.0, 2.0});
        assertEquals(expectedFunction, restrictedFunction);
    }

    @Test
    public void testDoOptimize_LinearFunction() {
        LinearFunction f = new LinearFunction(new double[]{2.0, -3.0}, new double[]{1.0});
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.doOptimize();
        PointValuePair expectedResult = new PointValuePair(new double[]{1.0 / 3.0, 2.0 / 3.0}, 1.0 / 3.0);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDoOptimize_QuadraticFunction() {
        QuadraticFunction f = new QuadraticFunction(new double[]{-1.0, 2.0, -3.0}, new double[]{1.0, 1.0, -2.0});
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.doOptimize();
        PointValuePair expectedResult = new PointValuePair(new double[]{1.0 / 3.0, 2.0 / 3.0}, -1.0 / 3.0);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDoOptimize_MixedFunction() {
        LinearFunction lf = new LinearFunction(new double[]{-2.0, 3.0}, new double[]{1.0, 2.0});
        QuadraticFunction qf = new QuadraticFunction(new double[]{1.0, 2.0}, new double[]{1.0, 1.0});
        PointValuePair expectedResult = new PointValuePair(new double[]{1.0 / 3.0, 2.0 / 3.0}, -1.0 / 3.0);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.doOptimize(new Add(lf, qf));
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDoOptimize_ComplexFunction() {
        LinearFunction lf = new LinearFunction(new double[]{-2.0, 3.0}, new double[]{1.0, 2.0});
        QuadraticFunction qf = new QuadraticFunction(new double[]{1.0, 2.0}, new double[]{1.0, 1.0});
        Function f = new Add(lf, new Cubic(new double[]{1.0, 2.0}, 3.0));
        PointValuePair expectedResult = new PointValuePair(new double[]{1.0 / 3.0, 2.0 / 3.0}, -1.0 / 3.0);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.doOptimize(f);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDoOptimize_NonFeasibleFunction() {
        LinearFunction f = new LinearFunction(new double[]{2.0, -3.0}, new double[]{1.0});
        SimplexSolver solver = new SimplexSolver();
        assertThrows(NoFeasibleSolutionException.class, () -> solver.doOptimize(new LinearFunction(new double[]{2.0, -3.0}, new double[]{-1.0})));
    }

    @Test
    public void testDoOptimize_UnboundedFunction() {
        LinearFunction f = new LinearFunction(new double[]{2.0, -3.0}, new double[]{1.0});
        SimplexSolver solver = new SimplexSolver();
        assertThrows(UnboundedSolutionException.class, () -> solver.doOptimize(new LinearFunction(new double[]{2.0, -3.0}, new double[]{1.0, 1.0, -1.0})));
    }

    @Test
    public void testDoOptimize_MaxIterationsReached() {
        LinearFunction f = new LinearFunction(new double[]{2.0, -3.0}, new double[]{1.0});
        SimplexSolver solver = new SimplexSolver();
        solver.setMaxIterations(5);
        assertThrows(MaxCountExceededException.class, () -> solver.doOptimize(f));
    }

    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearFunction f = new LinearFunction(new double[]{-2.0, 3.0}, new double[]{1.0, 2.0});
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(f, null, GoalType.MINIMIZE, null, epsilon, maxUlps);
        solver.solvePhase1(tableau);
        tableau.dropPhase1Objective();
        PointValuePair expectedResult = new PointValuePair(new double[]{1.0 / 3.0, 2.0 / 3.0}, 1.0 / 3.0);
        PointValuePair result = tableau.getSolution();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSolvePhase1_Unbounded() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearFunction f = new LinearFunction(new double[]{2.0, -3.0}, new double[]{1.0});
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(f, null, GoalType.MINIMIZE, null, epsilon, maxUlps);
        tableau.getEntry(0, tableau.getNumObjectiveFunctions() + tableau.getNumConstraints() + tableau.getNumArtificialVariables()) = -1.0;
        solver.solvePhase1(tableau);
        assertThrows(UnboundedSolutionException.class, () -> tableau.getSolution());
    }

    @Test
    public void testSolvePhase1_MaxIterationsReached() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearFunction f = new LinearFunction(new double[]{2.0, -3.0}, new double[]{1.0});
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(f, null, GoalType.MINIMIZE, null, epsilon, maxUlps);
        solver.setMaxIterations(1);
        solver.solvePhase1(tableau);
        assertFalse(tableau.isOptimal());
    }

    @Test
    public void testSolvePhase1_NoFeasibleSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearFunction f = new LinearFunction(new double[]{2.0, -3.0}, new double[]{1.0});
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(f, null, GoalType.MINIMIZE, null, epsilon, maxUlps);
        tableau.getEntry(0, tableau.getNumObjectiveFunctions()) = -1.0;
        solver.solvePhase1(tableau);
        assertThrows(NoFeasibleSolutionException.class, () -> tableau.getSolution());
    }

    @Test
    public void testSolvePhase1_FeasibleSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearFunction f = new LinearFunction(new double[]{2.0, -3.0}, new double[]{1.0, 2.0});
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(f, null, GoalType.MINIMIZE, null, epsilon, maxUlps);
        solver.solvePhase1(tableau);
        PointValuePair expectedResult = new PointValuePair(new double[]{1.0 / 3.0, 2.0 / 3.0}, 1.0 / 3.0);
        PointValuePair result = tableau.getSolution();
        assertEquals(expectedResult, result);
    }

}